package de.cryxy.homeauto.surveillance.client;

import static org.junit.Assert.*;

import org.junit.Test;

import de.cryxy.homeauto.surveillance.client.http.ResourceServicesProducer;
import de.cryxy.homeauto.surveillance.dtos.WebcamDto;

public class ResourceServicesProducerTestIT {
	
	@Test
	public void testGetWebcamResource() {
		ResourceServicesProducer producer = new ResourceServicesProducer();
		producer.setup();
		WebcamDto webcamDto = producer.getWebcamResource().getWebcam(1l).readEntity(WebcamDto.class);
		System.out.println(webcamDto);
	}

}
