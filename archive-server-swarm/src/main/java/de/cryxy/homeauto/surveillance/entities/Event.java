package de.cryxy.homeauto.surveillance.entities;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import de.cryxy.homeauto.surveillance.dtos.EventDto;

@Entity
@Table(name = "event", indexes = { @Index(columnList = "startDate"), @Index(columnList = "endDate") })
public class Event {

	public Event() {
	}

	public Event(Webcam webcam, ZonedDateTime startDate, ZonedDateTime endDate, List<Snapshot> snapshots) {
		super();
		this.webcam = webcam;
		this.startDate = startDate;
		this.endDate = endDate;
		this.snapshots = snapshots;
	}

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Webcam webcam;

	@NotNull
	private ZonedDateTime startDate;

	@NotNull
	private ZonedDateTime endDate;

	@OrderBy(value = "timestamp ASC")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "event", fetch = FetchType.LAZY)
	private List<Snapshot> snapshots;

	public Webcam getWebcam() {
		return webcam;
	}

	public void setWebcam(Webcam webcam) {
		this.webcam = webcam;
	}

	public ZonedDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(ZonedDateTime startDate) {
		this.startDate = startDate;
	}

	public ZonedDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(ZonedDateTime endDate) {
		this.endDate = endDate;
	}

	public List<Snapshot> getSnapshots() {
		return snapshots;
	}

	public void setSnapshots(List<Snapshot> snapshots) {
		this.snapshots = snapshots;
	}

	public void addSnapshot(Snapshot snapshot) {
		if (snapshots == null) {
			this.snapshots = new ArrayList<>();
		}
		snapshot.setEvent(this);
		snapshots.add(snapshot);
		updateStartDate();
		updateEndDate();
	}

	public Long getId() {
		return id;
	}

	public void updateStartDate() {
		if (snapshots != null) {
			Snapshot lastSnapshot = snapshots.stream()
					.sorted((s1, s2) -> s1.getTimestamp().compareTo(s2.getTimestamp())).findFirst().get();
			this.setEndDate(lastSnapshot.getTimestamp());
		}
	}

	public void updateEndDate() {
		if (snapshots != null) {
			Snapshot lastSnapshot = snapshots.stream()
					.sorted((s1, s2) -> s2.getTimestamp().compareTo(s1.getTimestamp())).findFirst().get();
			this.setEndDate(lastSnapshot.getTimestamp());
		}
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", webcam=" + webcam + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", snapshots=" + snapshots + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((snapshots == null) ? 0 : snapshots.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
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
		Event other = (Event) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (snapshots == null) {
			if (other.snapshots != null)
				return false;
		} else if (!snapshots.equals(other.snapshots))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (webcam == null) {
			if (other.webcam != null)
				return false;
		} else if (!webcam.equals(other.webcam))
			return false;
		return true;
	}

	@Transient
	public EventDto toEventDto(boolean withSnapshots) {

		EventDto dto = new EventDto();
		dto.setId(this.getId());
		dto.setWebcamId(this.getWebcam().getId());
		dto.setStartDate(this.getStartDate());
		dto.setEndDate(this.getEndDate());

		if (withSnapshots) {
			dto.setSnapshots(this.getSnapshots().stream().map(s -> s.toSnapshotDto()).collect(Collectors.toList()));
			dto.setSnapshotsSize(this.getSnapshots().size());
		}

		return dto;

	}

}
