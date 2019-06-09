package de.cryxy.homeauto.surveillance.resources.impl;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.sql.Blob;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriBuilder;

import org.hibernate.engine.jdbc.BlobProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.cryxy.homeauto.surveillance.dtos.SnapshotDto;
import de.cryxy.homeauto.surveillance.dtos.WebcamDto;
import de.cryxy.homeauto.surveillance.dtos.ZonedDateTimeParam;
import de.cryxy.homeauto.surveillance.entities.Snapshot;
import de.cryxy.homeauto.surveillance.entities.Webcam;
import de.cryxy.homeauto.surveillance.enums.ImageSize;
import de.cryxy.homeauto.surveillance.exceptions.AuthorizationException;
import de.cryxy.homeauto.surveillance.resources.SnapshotResource;
import de.cryxy.homeauto.surveillance.resources.WebcamResource;
import de.cryxy.homeauto.surveillance.services.ArchiveService;

@RequestScoped
public class WebcamResourceImpl implements WebcamResource {

	@Inject
	private ArchiveService archiveService;

	// TODO: provider?
	@Context
	private SecurityContext securityContext;

	private final Logger LOG = LoggerFactory.getLogger(WebcamResourceImpl.class);

	@Override
	public Response getLatestSnapshot(Long webcamId) {

		LOG.debug("Returning latest snapshot for webcam={}", webcamId);

		String userId = securityContext.getUserPrincipal().getName();

		Snapshot latestSnapshot = archiveService.getLatestSnapshot(userId, webcamId);

		if (latestSnapshot != null) {

			SnapshotDto snapshotDto = latestSnapshot.toSnapshotDto();

			return Response.ok(snapshotDto).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}

	}

	@Override
	public Response getLatestSnapshotImage(Long webcamId, ImageSize size) {

		LOG.debug("Returning latest snapshot image for webcam={}", webcamId);

		String userId = securityContext.getUserPrincipal().getName();

		Snapshot latestSnapshot = archiveService.getLatestSnapshot(userId, webcamId);

		if (latestSnapshot == null) {
			LOG.debug("No latest snapshot found.");
			throw new NotFoundException("No latest snapshot found.");
		}

		URI uri = UriBuilder.fromResource(SnapshotResource.class).path("/" + latestSnapshot.getId()).path("/image.jpg")
				.queryParam("size", size.name()).build();
		return Response.seeOther(uri).build();

		// InputStream image;
		// try {
		// image = archiveService.getImage(userId, latestSnapshot.getId(), size);
		//
		// if (image == null) {
		// throw new NotFoundException("Snapshot not found!");
		// }
		//
		// return Response.ok(image).build();
		//
		// } catch (AuthorizationException e) {
		// return Response.status(Status.FORBIDDEN).build();
		// }

	}

	@Override
	public Response getWebcams() {
		LOG.debug("Returning webcams ...");

		String userId = securityContext.getUserPrincipal().getName();
		try {
			List<Webcam> webcams = archiveService.getWebcams(userId);

			if (webcams == null || webcams.isEmpty())
				return Response.status(Status.NOT_FOUND).build();

			List<WebcamDto> webcamDtos = webcams.stream().map(w -> w.toWebcamDto()).collect(Collectors.toList());

			return Response.ok(webcamDtos).cacheControl(createCacheControlHeader(900)).build();

		} catch (AuthorizationException e) {
			return Response.status(Status.FORBIDDEN).build();
		}
	}

	@Override
	public Response getWebcam(Long webcamId) {
		LOG.debug("Returning webcam={} ...", webcamId);

		String userId = securityContext.getUserPrincipal().getName();
		try {
			Webcam webcam = archiveService.getWebcam(userId, webcamId);

			if (webcam == null)
				return Response.status(Status.NOT_FOUND).build();

			return Response.ok(webcam.toWebcamDto()).cacheControl(createCacheControlHeader(900)).build();

		} catch (AuthorizationException e) {
			return Response.status(Status.FORBIDDEN).build();
		}
	}

	@Override
	public Response updateWebcam(Long webcamId, WebcamDto webcamDto) {
		LOG.debug("Updating webcam={} ...", webcamId);

		String userId = securityContext.getUserPrincipal().getName();
		try {
			archiveService.updateWebcam(userId, webcamId, Webcam.fromWebcamDto(webcamDto));

			return Response.ok().build();

		} catch (AuthorizationException e) {
			return Response.status(Status.FORBIDDEN).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.cryxy.homeauto.surveillance.resources.WebcamResource#addSnapshot(java.lang
	 * .Long, java.io.File)
	 */
	@Override
	public SnapshotDto addSnapshot(Long webcamId, String fileName, ZonedDateTimeParam fileCreationDate,
			SnapshotDto.Trigger trigger, File file) {

		LOG.debug("Creating snapshot for webcamId={} ...", webcamId);

		String userId = securityContext.getUserPrincipal().getName();
		try (FileInputStream stream = new FileInputStream(file)) {
			Blob blobProxy = BlobProxy.generateProxy(stream, file.length());
			Snapshot snapshot = archiveService.addSnapshot(userId, webcamId, fileCreationDate.getZonedDateTime(),
					fileName, Snapshot.Trigger.valueOf(trigger.name()), blobProxy);

			return snapshot.toSnapshotDto();

		} catch (AuthorizationException e) {
			throw new WebApplicationException(e, Status.FORBIDDEN);
		} catch (Exception e) {
			throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
		}

	}

	private CacheControl createCacheControlHeader(int maxAge) {
		CacheControl cc = new CacheControl();
		cc.setNoCache(false);
		cc.setMaxAge(maxAge);
		cc.setPrivate(true);
		return cc;
	}

}
