package de.cryxy.homeauto.surveillance.client.io;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.cryxy.homeauto.surveillance.client.Config;
import de.cryxy.homeauto.surveillance.dtos.SnapshotDto;
import de.cryxy.homeauto.surveillance.dtos.WebcamDto;
import de.cryxy.homeauto.surveillance.dtos.ZonedDateTimeParam;
import de.cryxy.homeauto.surveillance.exceptions.IOException;
import de.cryxy.homeauto.surveillance.resources.WebcamResource;

public class InitialFileWalker {

	private final Logger LOG = LoggerFactory.getLogger(InitialFileWalker.class);

	@Inject
	private WebcamResource webcamResourceService;

	@Inject
	private RelevantWebcamService relevantWebcamService;

	@Inject
	private Config config;

	private Boolean shouldDelete = false;

	@PostConstruct
	public void setup() {
		shouldDelete = config.getClientIsAutoDelete();
	}

	public void walkWebcams() {
		List<WebcamDto> webcams = relevantWebcamService.getFilteredWebcams();
		webcams.stream().parallel().forEach(webcam -> {
			walkWebcam(webcam);
		});
	}

	public void walkWebcam(WebcamDto webcam) {
		List<Path> paths;
		try {
			paths = Files.list(Paths.get(webcam.getSnapshotDir())).filter(p -> p.toFile().getName().endsWith(".jpg"))
					.collect(Collectors.toList());
		} catch (Exception e) {
			throw new IOException("Error walking wecam=" + webcam, e);
		}

		paths.stream().parallel().forEach(p -> {
			try {
				ZonedDateTime creationTime = IOHelper.creationTimeFromPath(p);

				SnapshotDto snapshotDto = webcamResourceService.addSnapshot(webcam.getId(), p.getFileName().toString(),
						new ZonedDateTimeParam(creationTime), SnapshotDto.Trigger.FILE, p.toFile());

				LOG.info("Snapshot added: {}", snapshotDto);

				if (shouldDelete) {
					LOG.info("About to delete file={}.", p.getFileName());
					Files.delete(p);
				}
			} catch (Exception e) {
				throw new de.cryxy.homeauto.surveillance.exceptions.IOException("Error adding snapshot from path=" + p,
						e);
			}

		});

	}

}
