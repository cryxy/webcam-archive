package de.cryxy.homeauto.surveillance.resources;

import java.util.Date;

import javax.swing.SortOrder;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.cryxy.homeauto.surveillance.dtos.EventDto;
import de.cryxy.homeauto.surveillance.dtos.ZonedDateTimeParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@Path("/events")
public interface EventResource {

	@GET
	@Path("/{eventId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(response = EventDto.class, value = "getEvent")
	public Response getEvent(@PathParam("eventId") Long eventId);

	@GET
	@Path("/dates")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(response = Date.class, responseContainer = "List", value = "getEventDates")
	public Response getEventDates(@NotNull @QueryParam("webcamId") Long webcamId);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(response = EventDto.class, responseContainer = "List", value = "getEvents")
	public Response getEvents(@NotNull @QueryParam("webcamId") Long webcamId,
			@QueryParam("beginDate") ZonedDateTimeParam beginDate, @QueryParam("endDate") ZonedDateTimeParam endDate,
			@QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset,
			@QueryParam("minSnapshots") Integer minSnapshots,
			@DefaultValue("ASCENDING") @QueryParam("sortOrder") SortOrder sortOrder,
			@DefaultValue("false") @QueryParam("withSnapshots") Boolean withSnapshots,
			@DefaultValue("false") @QueryParam("withSnapshotsPreview") Boolean withSnapshotsPreview) throws javax.ws.rs.BadRequestException;

}
