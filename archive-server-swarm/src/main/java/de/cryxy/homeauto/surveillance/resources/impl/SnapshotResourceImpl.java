package de.cryxy.homeauto.surveillance.resources.impl;

import java.io.InputStream;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.cryxy.homeauto.surveillance.dtos.SnapshotDto;
import de.cryxy.homeauto.surveillance.entities.Snapshot;
import de.cryxy.homeauto.surveillance.enums.ImageSize;
import de.cryxy.homeauto.surveillance.exceptions.AuthorizationException;
import de.cryxy.homeauto.surveillance.resources.SnapshotResource;
import de.cryxy.homeauto.surveillance.services.ArchiveService;

@RequestScoped
public class SnapshotResourceImpl implements SnapshotResource {

	private final Logger LOG = LoggerFactory.getLogger(SnapshotResourceImpl.class);

	@Inject
	private ArchiveService archiveService;

	// TODO: provider?
	@Context
	private SecurityContext securityContext;

	@Override
	public Response getSnapshotImage(Long snapshotId, ImageSize size) {

		LOG.info("Get snapshot={}", snapshotId);

		String userId = securityContext.getUserPrincipal().getName();

		InputStream image;
		try {
			image = archiveService.getImage(userId, snapshotId, size);

			if (image == null) {
				throw new NotFoundException("Snapshot not found!");
			}

			CacheControl cc = new CacheControl();
			cc.setMaxAge(86400);
			cc.setPrivate(true);

			return Response.ok(image).cacheControl(cc).build();

		} catch (AuthorizationException e) {
			return Response.status(Status.FORBIDDEN).build();
		}

	}

	@Override
	public Response getSnapshot(Long snapshotId) {

		LOG.info("Get snapshot={}", snapshotId);

		String userId = securityContext.getUserPrincipal().getName();

		try {
			Snapshot snapshot = archiveService.getSnapshot(userId, snapshotId);

			if (snapshot == null) {
				return Response.status(Status.NOT_FOUND).build();
			}

			SnapshotDto snapshotDto = snapshot.toSnapshotDto();
			return Response.ok(snapshotDto).build();

		} catch (AuthorizationException e) {
			return Response.status(Status.FORBIDDEN).build();
		}

	}

}
