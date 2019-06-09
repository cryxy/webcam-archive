package de.cryxy.homeauto.surveillance.services.impl;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.SortOrder;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import de.cryxy.homeauto.surveillance.constants.Config;
import de.cryxy.homeauto.surveillance.daos.AuthorizationDao;
import de.cryxy.homeauto.surveillance.daos.EventDao;
import de.cryxy.homeauto.surveillance.daos.SnapshotDao;
import de.cryxy.homeauto.surveillance.daos.WebcamDao;
import de.cryxy.homeauto.surveillance.entities.Event;
import de.cryxy.homeauto.surveillance.entities.Snapshot;
import de.cryxy.homeauto.surveillance.entities.Webcam;
import de.cryxy.homeauto.surveillance.enums.AccessRight;
import de.cryxy.homeauto.surveillance.enums.ImageSize;
import de.cryxy.homeauto.surveillance.events.SnapshotEvent;
import de.cryxy.homeauto.surveillance.exceptions.AuthorizationException;
import de.cryxy.homeauto.surveillance.exceptions.IOException;
import de.cryxy.homeauto.surveillance.io.IOHelper;
import de.cryxy.homeauto.surveillance.mail.MailService;
import de.cryxy.homeauto.surveillance.queries.EventQuery;
import de.cryxy.homeauto.surveillance.queries.SnapshotQuery;
import de.cryxy.homeauto.surveillance.services.ArchiveService;

@Singleton
@Transactional
public class ArchiveServiceImpl implements ArchiveService {

	private final Logger LOG = LoggerFactory.getLogger(ArchiveServiceImpl.class);

	@Inject
	private WebcamDao webcamDao;

	@Inject
	private SnapshotDao snapshotDao;
	@Inject
	private EventDao eventDao;
	@Inject
	private AuthorizationDao authDao;
	@Inject
	private MailService mailService;

	@Inject
	@ConfigurationValue(Config.MAIL_NOTIFICATION)
	private boolean activateMail;

	@Inject
	private javax.enterprise.event.Event<SnapshotEvent> snapshotEvent;

	@Override
	public Snapshot addSnapshot(String userId, Long webcamId, ZonedDateTime timestamp, String filename,
			Snapshot.Trigger trigger, Blob image) {
		// the user is only relevant when using the rest api
		if (userId != null)
			ensureAuthorized(userId, webcamId, AccessRight.WRITE);

		// check for existing snapshots with the given file name / md5 hash
		SnapshotQuery sq = SnapshotQuery.create().fileName(filename).webcamId(webcamId).build();
		List<Snapshot> existingSnapshots = snapshotDao.getSnapshots(sq);
		for (Snapshot snapshot : existingSnapshots) {
			try {
				// new
				Blob newBlob = image;
				int newBlobLength = (int) newBlob.length();
				byte[] newBlobAsBytes = newBlob.getBytes(1, newBlobLength);
				newBlob.free();
				// existing
				Blob existingBlob = snapshot.getImage();
				int existingBlobLength = (int) existingBlob.length();
				byte[] existingBlobAsBytes = existingBlob.getBytes(1, existingBlobLength);
				existingBlob.free();

				byte[] hashExisting = MessageDigest.getInstance("MD5").digest(existingBlobAsBytes);
				byte[] hashNew = MessageDigest.getInstance("MD5").digest(newBlobAsBytes);

				if (Arrays.equals(hashExisting, hashNew)) {
					LOG.info("A snapshot with the given filename={} already existis! It will be skipped!", filename);
					return snapshot;
				}
			} catch (NoSuchAlgorithmException e) {
				throw new IOException("Error while hashing file=" + filename, e);
			} catch (SQLException e) {
				throw new IOException("Error while hashing file=" + filename, e);
			}
		}

		Webcam webcam = webcamDao.getWebcam(webcamId);
		Snapshot snapshot = new Snapshot(webcam, timestamp, filename, trigger, image);
		Event event = eventDao.getEventByMaxAge(webcamId, timestamp, Duration.ofMinutes(2));
		if (event == null) {
			event = eventDao.createEvent(webcamId, timestamp, timestamp);
			LOG.info("Created a new event={}", event);
		}

		event.addSnapshot(snapshot);
		LOG.info("Adding snapshot={} to event. Event-Id={}", snapshot.getFileName(), event.getId());

		if (activateMail) {
			if (webcam.isAlertActive()) {
				if (eventDao.countSnapshots(event).equals(webcam.getAlertThreshold())) {
					eventDao.refresh(event);
					mailService.sendMessageWithSnapshotsForEvent(event.getId());
				}
			}
		}

		snapshotEvent.fire(new SnapshotEvent(snapshot));

		return snapshot;
	}

	@Override
	public Snapshot getSnapshot(String userId, Long snapshotId) {
		Snapshot snapshot = snapshotDao.getSnapshot(snapshotId);
		if (snapshot == null)
			return null;
		ensureAuthorized(userId, snapshot.getWebcam().getId(), AccessRight.READ);
		LOG.info("Returning snapshot={} for user={}", snapshot.getId(), userId);
		return snapshot;
	}

	@Override
	public InputStream getImage(String userId, Long snapshotId, ImageSize quality) {
		Snapshot snapshot = getSnapshot(userId, snapshotId);
		if (snapshot == null)
			return null;

		if (quality == ImageSize.ORIGINAL) {

			try {
				return snapshot.getImage().getBinaryStream();
			} catch (SQLException e) {
				throw new IOException("Error while loading image!");
			}
		} else {
			try {
				return IOHelper.resizeSnapshot(snapshot, quality);
			} catch (java.io.IOException e) {
				throw new IOException("Error while resizing image!");
			} catch (SQLException e) {
				throw new IOException("Error while loading image!");
			}
		}

	}

	@Override
	public List<Event> getEventsForPeriod(String userId, EventQuery eq) {
		ensureAuthorized(userId, eq.getWebcamId(), AccessRight.READ);
		List<Event> eventsForPeriod = eventDao.getEvents(eq);
		// lazy initialization
		if (eq.isWithSnapshots())
			eventsForPeriod.stream().forEach(e -> {
				List<Snapshot> snapshots = e.getSnapshots();
				snapshots.isEmpty();
			});
		LOG.info("Returning events for user={} and webcam={}", userId, eq.getWebcamId());
		return eventsForPeriod;
	}

	@Override
	public List<Webcam> getWebcams(String userId) {
		List<Webcam> webcams = webcamDao.getWebcams();

		// return only cams which are accessible by the user
		List<Webcam> webcamsForUser = webcams.stream()
				.filter(webcam -> authDao.isAuthorized(userId, webcam.getId(), AccessRight.READ))
				.collect(Collectors.toList());
		LOG.info("Returning {} webcams for user={}.", webcamsForUser.size(), userId);
		return webcamsForUser;
	}

	private void ensureAuthorized(String userId, Long webcamId, AccessRight accessRight) {
		if (!authDao.isAuthorized(userId, webcamId, accessRight)) {
			AuthorizationException e = new AuthorizationException(
					"No access right (user = " + userId + ",   webcam=" + webcamId + ")");
			LOG.warn("No access right!", e);
			throw e;
		}
	}

	@Override
	public List<Webcam> getWebcams() {
		List<Webcam> webcams = webcamDao.getWebcams();
		LOG.info("Returning webcams={} webcams.", webcams);
		return webcams;
	}

	@Override
	public Snapshot getLatestSnapshot(String userId, Long webcamId) {
		ensureAuthorized(userId, webcamId, AccessRight.READ);
		SnapshotQuery sq = SnapshotQuery.create().webcamId(webcamId).maxResults(1).sortOrder(SortOrder.DESCENDING)
				.build();
		List<Snapshot> snapshots = snapshotDao.getSnapshots(sq);
		if (!snapshots.isEmpty())
			return snapshots.get(0);
		else
			return null;
	}

	@Override
	public Snapshot getEventPreviewSnapshot(String userId, Long webcamId, Long eventId) {
		ensureAuthorized(userId, webcamId, AccessRight.READ);
		List<Snapshot> snapshots = snapshotDao.getSnapshots(SnapshotQuery.create().eventId(eventId).webcamId(webcamId)
				.sortOrder(SortOrder.ASCENDING).maxResults(1).build());

		return snapshots.get(0);
	}

	@Override
	public Integer countSnapshotsForEvent(String userId, Long eventId) {
		Event event = eventDao.getEvent(eventId);

		if (event != null) {
			ensureAuthorized(userId, event.getWebcam().getId(), AccessRight.READ);
			return eventDao.countSnapshots(event);
		}

		return null;
	}

	@Override
	public Event getEvent(String userId, Long eventId) {
		Event event = eventDao.getEvent(eventId);
		if (event != null) {
			ensureAuthorized(userId, event.getWebcam().getId(), AccessRight.READ);
			event.getSnapshots().isEmpty();
			return event;
		}

		return null;
	}

	@Override
	public List<ZonedDateTime> getDatesForEvents(String userId, Long webcamId) {
		ensureAuthorized(userId, webcamId, AccessRight.READ);
		return eventDao.getDatesForEvents(webcamId);
	}

	@Override
	public Webcam getWebcam(String userId, Long webcamId) {
		ensureAuthorized(userId, webcamId, AccessRight.READ);
		return webcamDao.getWebcam(webcamId);
	}

	@Override
	public void updateWebcam(String userId, Long webcamId, Webcam webcam) {
		ensureAuthorized(userId, webcamId, AccessRight.WRITE);
		webcamDao.updateWebcam(webcamId, webcam);
		LOG.info("Updated webcam={}", webcam.getId());

	}

	@Override
	public void setup(boolean activateMail) {
		LOG.info("Configure activateMail={}", activateMail);
		this.activateMail = activateMail;
	}

}
