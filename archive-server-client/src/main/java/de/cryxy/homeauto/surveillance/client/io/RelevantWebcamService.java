package de.cryxy.homeauto.surveillance.client.io;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.GenericType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.cryxy.homeauto.surveillance.client.Config;
import de.cryxy.homeauto.surveillance.dtos.WebcamDto;
import de.cryxy.homeauto.surveillance.resources.WebcamResource;

/**
 * Provides a list of filtered webcam items.
 * 
 * @author fabian
 *
 */
@Singleton
public class RelevantWebcamService {

	private Logger LOG = LoggerFactory.getLogger(RelevantWebcamService.class);

	@Inject
	private Config config;

	@Inject
	private WebcamResource webcamResourceService;

	public List<WebcamDto> getFilteredWebcams() {

		String clientId = config.getClientId();
		LOG.info("Using clientId={}", clientId);

		// retrieve webcams with snapshot dir
		List<WebcamDto> webcams = webcamResourceService.getWebcams().readEntity(new GenericType<List<WebcamDto>>() {
		});

		List<WebcamDto> webcamsFiltered = webcams.stream().filter(
				w -> (w.getSnapshotDir() != null && w.getClientId() != null && w.getClientId().equals(clientId)))
				.collect(Collectors.toList());

		return webcamsFiltered;

	}

}
