package de.cryxy.homeauto.surveillance.dtos;

import java.time.ZonedDateTime;
import java.util.List;

public class EventDto {

	private Long id;
	private Long webcamId;

	private ZonedDateTime startDate;

	private ZonedDateTime endDate;

	private Integer snapshotsSize;

	private List<SnapshotDto> snapshots;

	public EventDto() {

	}

	public EventDto(Long id, Long webcamId, ZonedDateTime startDate, ZonedDateTime endDate,
			List<SnapshotDto> snapshots) {
		super();
		this.id = id;
		this.webcamId = webcamId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.snapshots = snapshots;
	}

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

	public List<SnapshotDto> getSnapshots() {
		return snapshots;
	}

	public void setSnapshots(List<SnapshotDto> snapshots) {
		this.snapshots = snapshots;
	}

	public Integer getSnapshotsSize() {
		return snapshotsSize;
	}

	public void setSnapshotsSize(Integer snapshotsSize) {
		this.snapshotsSize = snapshotsSize;
	}

	@Override
	public String toString() {
		return "EventDto [id=" + id + ", webcamId=" + webcamId + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", snapshotsSize=" + snapshotsSize + ", snapshots=" + snapshots + "]";
	}

}
