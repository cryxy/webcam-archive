package de.cryxy.homeauto.surveillance.entities;

import java.sql.Blob;
import java.time.ZonedDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import de.cryxy.homeauto.surveillance.dtos.SnapshotDto;

@Entity
@Table(name = "snapshot", indexes = { @Index(columnList = "fileName", unique = false),
		@Index(columnList = "timestamp") })
public class Snapshot {

	public Snapshot() {

	}

	public Snapshot(Webcam webcam, ZonedDateTime timestamp, String fileName, Trigger trigger, Blob image) {
		super();
		this.webcam = webcam;
		this.timestamp = timestamp;
		this.fileName = fileName;
		this.trigger = trigger;
		this.image = image;
	}

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@ManyToOne(targetEntity = Webcam.class)
	private Webcam webcam;

	@NotNull
	private ZonedDateTime timestamp;

	private String fileName;

	@ManyToOne
	private Event event;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private Blob image;

	@Column(name = "triggeredBy")
	@Enumerated(EnumType.STRING)
	private Trigger trigger;

	public Webcam getWebcam() {
		return webcam;
	}

	public void setWebcam(Webcam webcam) {
		this.webcam = webcam;
	}

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(ZonedDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Blob getImage() {
		return image;
	}

	public void setImage(Blob image) {
		this.image = image;
	}

	public Long getId() {
		return id;
	}

	public Event getEvent() {
		return event;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}

	@Override
	public String toString() {
		return "Snapshot [id=" + id + ", webcam=" + webcam + ", timestamp=" + timestamp + ", fileName=" + fileName
				+ ", event=" + event + ", trigger=" + trigger + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		result = prime * result + ((webcam == null) ? 0 : webcam.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Snapshot other = (Snapshot) obj;
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		if (webcam == null) {
			if (other.webcam != null)
				return false;
		} else if (!webcam.equals(other.webcam))
			return false;
		return true;
	}

	@Transient
	public SnapshotDto toSnapshotDto() {

		return new SnapshotDto(this.getId(), this.getWebcam().getId(), this.getTimestamp(), this.getFileName(),
				this.getEvent().getId(),
				(this.getTrigger() != null ? SnapshotDto.Trigger.valueOf(this.getTrigger().name()) : null));

	}

	public static enum Trigger {
		FILE(1), DOORBELL(2);

		private Integer value;

		Trigger(Integer value) {
			this.value = value;
		}

		/**
		 * @return the value
		 */
		public Integer getValue() {
			return value;
		}

		/**
		 * @param value
		 *            the value to set
		 */
		public void setValue(Integer value) {
			this.value = value;
		}

	}

}
