package de.cryxy.homeauto.surveillance.client.io;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.cryxy.homeauto.surveillance.client.Config;
import de.cryxy.homeauto.surveillance.dtos.SnapshotDto;
import de.cryxy.homeauto.surveillance.dtos.WebcamDto;
import de.cryxy.homeauto.surveillance.dtos.ZonedDateTimeParam;
import de.cryxy.homeauto.surveillance.resources.WebcamResource;

public class FileWatcherServiceImpl implements FileWatcherService {

	Logger LOG = LoggerFactory.getLogger(FileWatcherServiceImpl.class);

	@Inject
	private Config config;

	@Inject
	private WebcamResource webcamResourceService;

	@Inject
	private RelevantWebcamService filteredWebcamService;

	private ExecutorService executor;

	private List<FileWatcherRunnable> runnables = new ArrayList<>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cryxy.homeauto.surveillance.io.FileWatcherService#start()
	 */
	@Override
	public void start() {

		Boolean autoDelete = config.getClientIsAutoDelete();
		LOG.info("The autoDelete-value is {}", autoDelete);

		// retrieve webcams with snapshot dir
		List<WebcamDto> webcamsFiltered = filteredWebcamService.getFilteredWebcams();

		if (webcamsFiltered == null || webcamsFiltered.isEmpty()) {
			LOG.error("Found no webcams for this client.");
			return;
		}
		executor = Executors.newFixedThreadPool(webcamsFiltered.size());

		for (WebcamDto webcam : webcamsFiltered) {
			FileWatcherRunnable runnable = new FileWatcherRunnable(Paths.get(webcam.getSnapshotDir()), webcam.getId(),
					autoDelete);
			executor.submit(runnable);
			runnables.add(runnable);
			LOG.info("Submitted watcher for webcam={}", webcam.getId());
		}
		LOG.info("Successfully started watcher ...");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cryxy.homeauto.surveillance.io.FileWatcherService#stop()
	 */
	@Override
	public void stop() throws Exception {
		if (executor != null) {
			for (FileWatcherRunnable runnable : runnables) {
				runnable.stopWatching();
			}
			executor.awaitTermination(5, TimeUnit.SECONDS);
		}
	}

	private class FileWatcherRunnable implements Runnable {

		private Path path;
		private Long webcamId;
		private boolean autoDelete;
		private boolean stop = false;
		private ExecutorService fileAddExecutor;

		WatchService service;

		public FileWatcherRunnable(Path path, Long webcamId, boolean autoDelete) {
			this.path = path;
			this.webcamId = webcamId;
			this.autoDelete = autoDelete;
		}

		@Override
		public void run() {
			fileAddExecutor = Executors.newFixedThreadPool(3);
			watchDirectoryPath();
		}

		public void stopWatching() throws IOException {
			LOG.info("Attempt to stop watcher={}", Thread.currentThread().getName());
			stop = true;
			service.close();
			fileAddExecutor.shutdown();

		}

		@SuppressWarnings("unchecked")
		private void watchDirectoryPath() {
			// Sanity check - Check if path is a folder
			try {
				Boolean isFolder = (Boolean) Files.getAttribute(path, "basic:isDirectory", NOFOLLOW_LINKS);
				if (!isFolder) {
					throw new IllegalArgumentException("Path: " + path + " is not a folder");
				}
			} catch (IOException ioe) {
				// Folder does not exists
				throw new de.cryxy.homeauto.surveillance.exceptions.IOException("Folder does not exist", ioe);
			}

			LOG.info("Watching path={}", path);

			// We obtain the file system of the Path
			FileSystem fs = path.getFileSystem();

			// We create the new WatchService using the new try() block
			try (WatchService service = fs.newWatchService()) {

				this.service = service;

				// We register the path to the service
				// We watch for creation events
				path.register(service, ENTRY_CREATE);

				// Start the infinite polling loop
				WatchKey key = null;
				while (!stop) {
					if (Thread.currentThread().isInterrupted())
						break;
					key = service.take();

					// Dequeueing events
					Kind<?> kind = null;
					for (WatchEvent<?> watchEvent : key.pollEvents()) {
						// Get the type of the event
						kind = watchEvent.kind();
						if (OVERFLOW == kind) {
							continue; // loop
						} else if (ENTRY_CREATE == kind) {
							// A new Path was created
							WatchEvent<Path> ev = (WatchEvent<Path>) watchEvent;
							Path newPath = ev.context();
							// Output
							LOG.info("New path created: " + newPath);
							// Verify that the new
							// file is a image file.
							try {
								// Resolve the filename against the directory.
								Path child = path.resolve(newPath);

								ZonedDateTime creationTime = IOHelper.creationTimeFromPath(child);
								// verzoegern des eigentlichen hinzufügens des Snapshots, da die Datei ggf. noch
								// nicht komplett uebertragen worden ist
								fileAddExecutor.submit(() -> {
									try {
										Thread.sleep(1500);
										if (!Files.probeContentType(child).equals("image/jpeg")) {
											LOG.error("New file {} is not an image file.", newPath);
											return;
										}

										SnapshotDto addSnapshot = webcamResourceService.addSnapshot(webcamId,
												child.getFileName().toString(), new ZonedDateTimeParam(creationTime),
												SnapshotDto.Trigger.FILE, child.toFile());
										LOG.info("Added snapshot={}.", addSnapshot);

										if (autoDelete) {
											LOG.info("About to delete file={}.", child.getFileName());
											Files.delete(child);
										}
									} catch (Exception e) {
										LOG.error("Error on adding snapshot= " + newPath, e);
									}
								});

							} catch (Exception x) {
								LOG.error("Error on processing event", x);

							}
						}
					}

					if (!key.reset()) {
						break; // loop
					}
				}

			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}

		}

	}

}
