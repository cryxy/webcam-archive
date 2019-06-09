/**
 * 
 */
package de.cryxy.homeauto.surveillance.provider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import de.cryxy.homeauto.surveillance.exceptions.AuthorizationException;

/**
 * @author fabian
 *
 */
@Provider
public class AuthorizationExceptionMapper implements ExceptionMapper<AuthorizationException>{

	/* (non-Javadoc)
	 * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
	 */
	@Override
	public Response toResponse(AuthorizationException arg0) {
		
		return Response.status(403).type(MediaType.APPLICATION_JSON).entity(arg0).build();
	}
	

}
