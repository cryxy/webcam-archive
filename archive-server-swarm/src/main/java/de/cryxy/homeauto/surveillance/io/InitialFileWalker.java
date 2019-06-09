package de.cryxy.homeauto.surveillance.io;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.hibernate.engine.jdbc.BlobProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.cryxy.homeauto.surveillance.entities.Snapshot.Trigger;
import de.cryxy.homeauto.surveillance.entities.Webcam;
import de.cryxy.homeauto.surveillance.services.ArchiveService;

@Singleton
public class InitialFileWalker {

	private final Logger LOG = LoggerFactory.getLogger(InitialFileWalker.class);

	@Inject
	private ArchiveService archiveService;

	public void walkWebcams() {

		LOG.info("Start initial walk.");
		List<Webcam> webcams = archiveService.getWebcams();
		for (Webcam webcam : webcams) {

			walkWebcam(webcam);

		}
	}

	public void walkWebcam(Webcam webcam) {
		List<Path> paths;
		try {
			paths = Files.list(Paths.get(webcam.getSnapshotDir())).filter(p -> p.toFile().getName().endsWith(".jpg"))
					.collect(Collectors.toList());
		} catch (Exception e) {
			LOG.error("Error walking webcam=" + webcam, e);
			return;
		}

		paths.stream().parallel().forEach(p -> {
			try {
				ZonedDateTime creationTime = IOHelper.creationTimeFromPath(p);

				Blob blobProxy = BlobProxy.generateProxy(Files.newInputStream(p), -1);

				archiveService.addSnapshot(null, webcam.getId(), creationTime, p.getFileName().toString(), Trigger.FILE,
						blobProxy);
				LOG.info("About to delete file={}.", p.getFileName());
				Files.delete(p);
			} catch (Exception e) {
				LOG.error("Error adding snapshot from path=" + p, e);
			}

		});

	}

}
