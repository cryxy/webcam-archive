package de.cryxy.homeauto.surveillance.daos;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import de.cryxy.homeauto.surveillance.entities.Event;
import de.cryxy.homeauto.surveillance.entities.Webcam;
import de.cryxy.homeauto.surveillance.queries.EventQuery;

public class EventDao {

	@Inject
	private WebcamDao webcamDao;

	@Inject
	private Provider<EntityManager> em;

	public List<Event> getEvents(EventQuery eq) {

		if (eq.getWebcamId() == null) {
			throw new IllegalArgumentException("WebcamId can not be null");
		}
		StringBuilder qlBuilder = new StringBuilder();
		qlBuilder.append("SELECT e FROM Event e WHERE e.webcam.id =:webcamId");

		if (eq.getBeginPeriod() != null)
			qlBuilder.append(" AND e.startDate >= :beginPeriod");

		if (eq.getEndPeriod() != null)
			qlBuilder.append(" AND e.endDate <= :endPeriod");

		if (eq.getMinSnapshots() != null)
			qlBuilder.append(" AND size(e.snapshots) >= :minSnapshots");

		if (eq.getSortOrder() != null) {
			switch (eq.getSortOrder()) {
			case ASCENDING:
				qlBuilder.append(" ORDER BY e.endDate ASC");
				break;
			case DESCENDING:
				qlBuilder.append(" ORDER BY e.endDate DESC");
				break;
			default:
				break;
			}
		}

		TypedQuery<Event> query = em.get().createQuery(qlBuilder.toString(), Event.class);

		query.setParameter("webcamId", eq.getWebcamId());
		if (eq.getBeginPeriod() != null)
			query.setParameter("beginPeriod", eq.getBeginPeriod());
		if (eq.getEndPeriod() != null)
			query.setParameter("endPeriod", eq.getEndPeriod());

		if (eq.getMinSnapshots() != null)
			query.setParameter("minSnapshots", eq.getMinSnapshots());

		if (eq.getLimit() != null)
			query.setMaxResults(eq.getLimit());

		if (eq.getOffset() != null)
			query.setFirstResult(eq.getOffset());

		List<Event> events = query.getResultList();

		return events;
	}

	public Event getEvent(Long eventId) {
		Event event = em.get().find(Event.class, eventId);
		if (event == null)
			return null;

		return event;
	}

	public Integer countSnapshots(Event event) {

		Query q = em.get().createQuery("SELECT count(*) FROM Snapshot s WHERE s.event=:event");
		q.setParameter("event", event);
		int count = ((Number) q.getSingleResult()).intValue();

		return count;
	}

	public Event getEventByMaxAge(Long webcamId, ZonedDateTime endPeriod, Duration maxAge) {

		ZonedDateTime beginPeriod = endPeriod.minus(maxAge);

		TypedQuery<Event> q = em.get().createQuery(
				"SELECT e FROM Event e WHERE e.webcam.id =:webcamId AND e.endDate BETWEEN :beginPeriod AND :endPeriod ORDER BY e.endDate DESC",
				Event.class);

		q.setParameter("webcamId", webcamId);
		q.setParameter("beginPeriod", beginPeriod);
		q.setParameter("endPeriod", endPeriod);
		q.setMaxResults(1);

		List<Event> resultList = q.getResultList();
		if (resultList.isEmpty())
			return null;

		return resultList.get(0);
	}

	public List<ZonedDateTime> getDatesForEvents(Long webcamId) {

		TypedQuery<String> q = em.get().createQuery(
				"SELECT DISTINCT DATE_FORMAT(e.startDate, '%Y-%m-%d') FROM Event e WHERE e.webcam.id =:webcamId",
				String.class);
		q.setParameter("webcamId", webcamId);

		List<String> resultList = q.getResultList();

		// Map to ZonedDateTime and sort desc
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		List<ZonedDateTime> datesForEvents = resultList.stream().map(d -> {
			LocalDate localDate = LocalDate.parse(d, dtf);
			ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneOffset.systemDefault());
			return zonedDateTime;
		}).sorted((d1, d2) -> d2.compareTo(d1)).collect(Collectors.toList());

		return datesForEvents;

	}

	public Event createEvent(Long webcamId, ZonedDateTime beginPeriod, ZonedDateTime endPeriod) {

		Webcam webcam = webcamDao.getWebcam(webcamId);

		Event event = new Event(webcam, beginPeriod, endPeriod, null);
		em.get().persist(event);

		return event;

	}

	// @Transactional(value = TxType.REQUIRES_NEW)
	public void deleteEvent(Event event) {
		em.get().remove(event);
		em.get().flush();
	}

	public void refresh(Event event) {
		em.get().flush();
		em.get().refresh(event);
	}

}
