package de.cryxy.homeauto.surveillance.dtos;

public class WebcamDto {

	private Long id;

	private String name;

	private String location;

	private String snapshotUri;

	private String snapshotDir;

	private String clientId;

	private Integer minSnapshotsThreshold;
	private Integer alertThreshold;
	private String alertMail;

	private Integer maxAge;

	public Integer getAlertThreshold() {
		return alertThreshold;
	}

	public void setAlertThreshold(Integer alertThreshold) {
		this.alertThreshold = alertThreshold;
	}

	public String getAlertMail() {
		return alertMail;
	}

	public void setAlertMail(String alertMail) {
		this.alertMail = alertMail;
	}

	public WebcamDto() {

	}

	public WebcamDto(Long id, String name, String location, String snapshotUri, String snapshotDir) {
		super();
		this.id = id;
		this.name = name;
		this.location = location;
		this.snapshotUri = snapshotUri;
		this.snapshotDir = snapshotDir;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSnapshotUri() {
		return snapshotUri;
	}

	public void setSnapshotUri(String snapshotUri) {
		this.snapshotUri = snapshotUri;
	}

	public String getSnapshotDir() {
		return snapshotDir;
	}

	public void setSnapshotDir(String snapshotDir) {
		this.snapshotDir = snapshotDir;
	}

	public Integer getMinSnapshotsThreshold() {
		return minSnapshotsThreshold;
	}

	public void setMinSnapshotsThreshold(Integer minSnapshotsThreshold) {
		this.minSnapshotsThreshold = minSnapshotsThreshold;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	@Override
	public String toString() {
		return "WebcamDto [id=" + id + ", name=" + name + ", location=" + location + ", snapshotUri=" + snapshotUri
				+ ", snapshotDir=" + snapshotDir + ", clientId=" + clientId + ", minSnapshotsThreshold="
				+ minSnapshotsThreshold + ", alertThreshold=" + alertThreshold + ", alertMail=" + alertMail
				+ ", maxAge=" + maxAge + "]";
	}

}
