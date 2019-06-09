package de.cryxy.homeauto.surveillance.dtos;

import java.time.ZonedDateTime;

public class SnapshotDto {

	private Long id;
	private Long webcamId;
	private Long eventId;
	private ZonedDateTime timestamp;
	private String fileName;
	private Trigger trigger;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWebcamId() {
		return webcamId;
	}

	public void setWebcamId(Long webcamId) {
		this.webcamId = webcamId;
	}

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public SnapshotDto() {

	}

	/**
	 * @param id
	 * @param webcamId
	 * @param eventId
	 * @param timestamp
	 * @param fileName
	 * @param trigger
	 */
	public SnapshotDto(Long id, Long webcamId, ZonedDateTime timestamp, String fileName, Long eventId,
			Trigger trigger) {
		super();
		this.id = id;
		this.webcamId = webcamId;
		this.eventId = eventId;
		this.timestamp = timestamp;
		this.fileName = fileName;
		this.trigger = trigger;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SnapshotDto [id=" + id + ", webcamId=" + webcamId + ", eventId=" + eventId + ", timestamp=" + timestamp
				+ ", fileName=" + fileName + ", trigger=" + trigger + "]";
	}

	public static enum Trigger {
		FILE, DOORBELL;

	}

}
