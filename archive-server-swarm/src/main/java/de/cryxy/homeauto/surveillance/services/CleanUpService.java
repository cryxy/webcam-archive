package de.cryxy.homeauto.surveillance.services;

import java.time.Duration;

public interface CleanUpService {

	public void deleteEventsOlderThan(Long webcamId, Duration maxAge);

	void cleanUpEvents();
}
