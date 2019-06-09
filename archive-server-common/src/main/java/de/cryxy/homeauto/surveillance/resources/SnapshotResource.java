package de.cryxy.homeauto.surveillance.resources;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.cryxy.homeauto.surveillance.constants.Roles;
import de.cryxy.homeauto.surveillance.enums.ImageSize;
import io.swagger.annotations.Api;

@Api
@Path("/snapshots")
public interface SnapshotResource {

	@GET
	@Path("/{snapshotId}")
	@RolesAllowed({ Roles.ADMIN, Roles.USER })
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSnapshot(@PathParam("snapshotId") Long snapshotId);

	@GET
	@Path("/{snapshotId}/image.jpg")
	@RolesAllowed({ Roles.USER })
	@Produces("image/jpeg")
	public Response getSnapshotImage(@PathParam("snapshotId") Long snapshotId,
			@QueryParam("size") @DefaultValue("ORIGINAL") ImageSize size);

}