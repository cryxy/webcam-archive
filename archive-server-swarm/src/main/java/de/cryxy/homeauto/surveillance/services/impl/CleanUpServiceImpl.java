package de.cryxy.homeauto.surveillance.services.impl;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jboss.ejb3.annotation.TransactionTimeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import de.cryxy.homeauto.surveillance.constants.Config;
import de.cryxy.homeauto.surveillance.daos.EventDao;
import de.cryxy.homeauto.surveillance.daos.WebcamDao;
import de.cryxy.homeauto.surveillance.entities.Event;
import de.cryxy.homeauto.surveillance.entities.Webcam;
import de.cryxy.homeauto.surveillance.queries.EventQuery;
import de.cryxy.homeauto.surveillance.services.CleanUpService;

@Startup
@Singleton
public class CleanUpServiceImpl implements CleanUpService {

	@Inject
	private EventDao eventDao;

	@Inject
	private WebcamDao webcamDao;

	@Inject
	@ConfigurationValue(Config.CLEAN_UP_INTERVAL)
	private Integer cleanUpInterval;

	private final Logger LOG = LoggerFactory.getLogger(CleanUpServiceImpl.class);

	@Resource
	private TimerService timerService;

	@PostConstruct
	public void initialize() {
		if (cleanUpInterval != null) {
			LOG.info("Schedule clean up timer for {} hours.", cleanUpInterval);
			timerService.createTimer(5000, cleanUpInterval * 60 * 60 * 1000,
					"Delay 5 seconds then every " + cleanUpInterval + " hours");
		} else {
			LOG.warn("No cleanUpInterval defined.");
		}
	}

	@Override
	@Transactional
	public void deleteEventsOlderThan(Long webcamId, Duration maxAge) {

		ZonedDateTime endPeriod = ZonedDateTime.now().minus(maxAge);

		EventQuery query = EventQuery.create().webcamId(webcamId).endPeriod(endPeriod).withSnapshots(false).limit(50)
				.build();

		List<Event> events = eventDao.getEvents(query);
		while (!events.isEmpty()) {
			LOG.info("Delete {} events older than days={} for webcam={}", events.size(), maxAge, webcamId);

			for (Event event : events) {

				eventDao.deleteEvent(event);

			}
			events = eventDao.getEvents(query);
		}

	}

	@Timeout
	@Override
	@Transactional
	@TransactionTimeout(value = 1, unit = TimeUnit.HOURS)
	public void cleanUpEvents() {

		LOG.info("Starting clean up ...");
		List<Webcam> webcams = webcamDao.getWebcams();
		for (Webcam webcam : webcams) {
			if (webcam.getMaxAge() != null) {
				deleteEventsOlderThan(webcam.getId(), Duration.ofDays(webcam.getMaxAge()));
			} else {
				LOG.info("Skip cleanUp for webcam {} because of missing maxAge.", webcam.getId());
			}

		}
		LOG.info("Finishing clean up ...");

	}

}
