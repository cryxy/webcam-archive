package de.cryxy.homeauto.surveillance.daos;

import java.sql.Blob;
import java.time.ZonedDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import de.cryxy.homeauto.surveillance.entities.Snapshot;
import de.cryxy.homeauto.surveillance.entities.Snapshot.Trigger;
import de.cryxy.homeauto.surveillance.entities.Webcam;
import de.cryxy.homeauto.surveillance.queries.SnapshotQuery;

public class SnapshotDao {

	@Inject
	private WebcamDao webcamDao;

	@Inject
	private Provider<EntityManager> em;

	public Snapshot getSnapshot(Long snapshotId) {

		Snapshot snapshot = em.get().find(Snapshot.class, snapshotId);
		return snapshot;

	}

	public List<Snapshot> getSnapshots(SnapshotQuery sq) {

		if (sq.getWebcamId() == null)
			throw new IllegalArgumentException("webcamId is a required querys parameter");

		StringBuilder qlBuilder = new StringBuilder();
		qlBuilder.append("Select s FROM Snapshot s WHERE s.webcam.id = :webcamId");

		if (sq.getFileName() != null)
			qlBuilder.append(" AND s.fileName = :fileName");

		if (sq.getEventId() != null) {
			qlBuilder.append(" AND s.event.id = :eventId");
		}

		if (sq.getSortOrder() != null) {
			switch (sq.getSortOrder()) {
			case ASCENDING:
				qlBuilder.append(" ORDER BY s.timestamp ASC");
				break;
			case DESCENDING:
				qlBuilder.append(" ORDER BY s.timestamp DESC");
				break;
			default:
				break;
			}
		}

		TypedQuery<Snapshot> query = em.get().createQuery(qlBuilder.toString(), Snapshot.class);

		query.setParameter("webcamId", sq.getWebcamId());

		if (sq.getFileName() != null)
			query.setParameter("fileName", sq.getFileName());

		if (sq.getEventId() != null) {
			query.setParameter("eventId", sq.getEventId());
		}

		if (sq.getMaxResults() != null)
			query.setMaxResults(sq.getMaxResults());

		return query.getResultList();
	}

	public Snapshot getSnapshotWithImage(Long snapshotId) {

		Snapshot snapshot = getSnapshot(snapshotId);
		snapshot.getImage();

		return snapshot;
	}

	public Snapshot createSnapshot(Long webcamId, ZonedDateTime timestamp, String fileName, Trigger trigger,
			Blob image) {

		Webcam webcam = webcamDao.getWebcam(webcamId);
		Snapshot snapshot = new Snapshot(webcam, timestamp, fileName, trigger, image);

		return snapshot;

	}

	public void removeSnapshot(Snapshot snapshot) {

		em.get().remove(snapshot);

	}

}
