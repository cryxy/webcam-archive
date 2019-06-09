/**
 * 
 */
package de.cryxy.homeauto.surveillance.client.http;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;

import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import de.cryxy.homeauto.surveillance.client.Config;
import de.cryxy.homeauto.surveillance.resources.WebcamResource;

/**
 * @author fabian
 *
 */
@Singleton
public class ResourceServicesProducer {

	@Inject
	private Config config;

	private ResteasyWebTarget target;

	private WebcamResource webcamResource;

	@PostConstruct
	public void setup() {

		Client client = ResteasyClientBuilder.newBuilder().register(createResteasyJacksonProvider(), 100).build();

		target = (ResteasyWebTarget) client.target(config.getApiUrl());
		target.register(new BasicAuthentication(config.getApiUser(), config.getApiPassword()));
	}

	@Produces
	public WebcamResource getWebcamResource() {
		if (webcamResource == null)
			webcamResource = target.proxy(WebcamResource.class);

		return webcamResource;
	}

	private ResteasyJackson2Provider createResteasyJacksonProvider() {

		ResteasyJackson2Provider resteasyJacksonProvider = new MyResteasyJackson2Provider();
		ObjectMapper mapper = new ObjectMapper();

		mapper.registerModule(new JavaTimeModule());
		mapper.findAndRegisterModules();
		resteasyJacksonProvider.setMapper(mapper);

		return resteasyJacksonProvider;

	}

	/**
	 * Verhindert, dass der Provider auf Grund einer doppelten Registrierung
	 * ignoriert wird.
	 *
	 */
	public class MyResteasyJackson2Provider extends ResteasyJackson2Provider {
	}

}
