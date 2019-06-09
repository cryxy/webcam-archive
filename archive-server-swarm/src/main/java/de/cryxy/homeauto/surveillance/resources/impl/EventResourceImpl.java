package de.cryxy.homeauto.surveillance.resources.impl;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.swing.SortOrder;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import de.cryxy.homeauto.surveillance.dtos.EventDto;
import de.cryxy.homeauto.surveillance.dtos.ZonedDateTimeParam;
import de.cryxy.homeauto.surveillance.entities.Event;
import de.cryxy.homeauto.surveillance.entities.Snapshot;
import de.cryxy.homeauto.surveillance.exceptions.AuthorizationException;
import de.cryxy.homeauto.surveillance.queries.EventQuery;
import de.cryxy.homeauto.surveillance.queries.EventQuery.EventQueryBuilder;
import de.cryxy.homeauto.surveillance.resources.EventResource;
import de.cryxy.homeauto.surveillance.services.ArchiveService;

@RequestScoped
public class EventResourceImpl implements EventResource {

	@Context
	private SecurityContext securityContext;

	@Inject
	private ArchiveService archiveService;

	@Override
	public Response getEvents(Long webcamId, ZonedDateTimeParam beginDate, ZonedDateTimeParam endDate, Integer limit,
			Integer offset, Integer minSnapshots, SortOrder sortOrder, Boolean withSnapshots,
			Boolean withSnapshotsPreview) {

		if (withSnapshots && withSnapshotsPreview) {
			throw new BadRequestException("withSnapshots and withSnapshotsPreview can not be used at the same time");
		}

		String userId = securityContext.getUserPrincipal().getName();

		EventQueryBuilder eqb = EventQuery.create().webcamId(webcamId).limit(limit).offset(offset)
				.minSnapshots(minSnapshots).sortOrder(sortOrder).withSnapshots(withSnapshots);
		if (beginDate != null)
			eqb.beginPeriod(beginDate.getZonedDateTime());
		if (endDate != null)
			eqb.endPeriod(endDate.getZonedDateTime());
		EventQuery eventQuery = eqb.build();

		try {
			List<Event> eventsForPeriod = archiveService.getEventsForPeriod(userId, eventQuery);

			if (eventsForPeriod == null)
				throw new NotFoundException();

			List<EventDto> eventDtos = eventsForPeriod.stream().map(e -> {
				EventDto eventDto = e.toEventDto(withSnapshots);
				// performance optimization
				if (!withSnapshots) {
					eventDto.setSnapshotsSize(archiveService.countSnapshotsForEvent(userId, e.getId()));

					// set a snapshot preview
					if (withSnapshotsPreview) {
						Snapshot previewSnapshot = archiveService.getEventPreviewSnapshot(userId, webcamId, e.getId());
						eventDto.setSnapshots(Arrays.asList(previewSnapshot.toSnapshotDto()));
					}
				}
				return eventDto;
			}).collect(Collectors.toList());

			CacheControl cc = createCacheControlHeader(45);

			return Response.ok(eventDtos).cacheControl(cc).build();

		} catch (AuthorizationException e) {

			throw new NotAuthorizedException("No access to the given webcam.", e);
		}

	}

	private CacheControl createCacheControlHeader(int maxAge) {
		CacheControl cc = new CacheControl();
		cc.setNoCache(false);
		cc.setMaxAge(maxAge);
		cc.setPrivate(true);
		return cc;
	}

	@Override
	public Response getEvent(Long eventId) {
		String userId = securityContext.getUserPrincipal().getName();

		Event event;
		try {
			event = archiveService.getEvent(userId, eventId);
		} catch (AuthorizationException e) {
			throw new NotAuthorizedException("No access to the given event.", e);
		}

		if (event == null)
			throw new NotFoundException();

		EventDto eventDto = event.toEventDto(true);
		return Response.ok(eventDto).cacheControl(createCacheControlHeader(60)).build();

	}

	@Override
	public Response getEventDates(Long webcamId) {
		String userId = securityContext.getUserPrincipal().getName();

		List<ZonedDateTime> datesForEvents;
		try {
			datesForEvents = archiveService.getDatesForEvents(userId, webcamId);
		} catch (AuthorizationException e) {
			throw new NotAuthorizedException("No access to the given webcam.", e);
		}

		if (datesForEvents == null)
			throw new NotFoundException();

		return Response.ok(datesForEvents).cacheControl(createCacheControlHeader(300)).build();
	}

}
