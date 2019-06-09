package de.cryxy.homeauto.surveillance.resources;

import java.io.File;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.cryxy.homeauto.surveillance.constants.Roles;
import de.cryxy.homeauto.surveillance.dtos.SnapshotDto;
import de.cryxy.homeauto.surveillance.dtos.WebcamDto;
import de.cryxy.homeauto.surveillance.dtos.ZonedDateTimeParam;
import de.cryxy.homeauto.surveillance.enums.ImageSize;
import de.cryxy.homeauto.surveillance.dtos.SnapshotDto.Trigger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@Path("/webcams")
public interface WebcamResource {

	@GET
	@Path("/{webcamId}")
	@RolesAllowed({ Roles.ADMIN, Roles.USER })
	@Produces(MediaType.APPLICATION_JSON)
	Response getWebcam(@PathParam("webcamId") Long webcamId);

	@PUT
	@Path("/{webcamId}")
	@RolesAllowed({ Roles.ADMIN })
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateWebcam(@PathParam("webcamId") Long webcamId, WebcamDto webcamDto);

	@GET
	@RolesAllowed({ Roles.ADMIN, Roles.USER })
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(response = WebcamDto.class, responseContainer = "List", value = "getWebcams")
	Response getWebcams();

	@GET
	@Path("/{webcamId}/snapshots/latest")
	@RolesAllowed({ Roles.ADMIN, Roles.USER })
	@Produces(MediaType.APPLICATION_JSON)
	Response getLatestSnapshot(@PathParam("webcamId") Long webcamId);

	@GET
	@Path("/{webcamId}/snapshots/latest/image.jpg")
	@RolesAllowed({ Roles.USER })
	@Produces("image/jpeg")
	Response getLatestSnapshotImage(@PathParam("webcamId") Long webcamId,
			@QueryParam("size") @DefaultValue("ORIGINAL") ImageSize size);

	@POST
	@Path("/{webcamId}/snapshots")
	@RolesAllowed({ Roles.ADMIN })
	@Consumes("image/jpeg")
	@Produces(MediaType.APPLICATION_JSON)
	SnapshotDto addSnapshot(@PathParam("webcamId") Long webcamId, @NotNull @HeaderParam("fileName") String fileName,
			@NotNull @HeaderParam("fileCreationDate") ZonedDateTimeParam fileCreationDate,
			@DefaultValue("FILE") @HeaderParam("trigger") SnapshotDto.Trigger trigger, File file);

}
