/**
 * 
 */
package de.cryxy.homeauto.surveillance.health;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;

import de.cryxy.homeauto.surveillance.daos.WebcamDao;

/**
 * @author fabian
 *
 */
@Liveness
@ApplicationScoped
public class VarchiveHealthCheck implements HealthCheck {

	@Inject
	private WebcamDao webcamDao;

	@Override
	public HealthCheckResponse call() {
		HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Database connection health check");

		try {
			webcamDao.getWebcams();
			responseBuilder.up();
		} catch (Exception e) {
			responseBuilder.down().withData("error", e.getMessage());
		}

		return responseBuilder.build();
	}

}
