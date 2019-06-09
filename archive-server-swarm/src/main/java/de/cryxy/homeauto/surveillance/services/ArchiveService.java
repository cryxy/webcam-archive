package de.cryxy.homeauto.surveillance.services;

import java.io.InputStream;
import java.sql.Blob;
import java.time.ZonedDateTime;
import java.util.List;

import javax.transaction.Transactional;

import de.cryxy.homeauto.surveillance.entities.Event;
import de.cryxy.homeauto.surveillance.entities.Snapshot;
import de.cryxy.homeauto.surveillance.entities.Webcam;
import de.cryxy.homeauto.surveillance.enums.ImageSize;
import de.cryxy.homeauto.surveillance.queries.EventQuery;

@Transactional
public interface ArchiveService {
	
	void setup(boolean activateMail);
	
	List<Webcam> getWebcams();
	
	Webcam getWebcam(String userId, Long webcamId);
	
	List<Webcam> getWebcams(String userId);
	
	void updateWebcam(String userId, Long webcamId, Webcam webcam);

	// erstellt / erweitert auch das zugeh�rige Event
	Snapshot addSnapshot(String userId, Long webcamId, ZonedDateTime timestamp, String filename, Snapshot.Trigger trigger, Blob image);

	Snapshot getSnapshot(String userId, Long snapshotId);
	
	Snapshot getEventPreviewSnapshot(String userId, Long webcamId, Long eventId);
	
	Snapshot getLatestSnapshot(String userId, Long webcamId);

	InputStream getImage(String userId, Long snapshotId, ImageSize quality);

	Event getEvent(String userId, Long eventId);
	
	List<Event> getEventsForPeriod(String userId, EventQuery eq);
	
	List<ZonedDateTime> getDatesForEvents(String userId, Long webcamId);
	
	Integer countSnapshotsForEvent(String userId, Long eventId);
	
	
	// aktuellester Snapshot pro Webcam
	
	// Sortierung von Events
	
	// Events filtern mit Mindestanzahl an Snapshots 

}
