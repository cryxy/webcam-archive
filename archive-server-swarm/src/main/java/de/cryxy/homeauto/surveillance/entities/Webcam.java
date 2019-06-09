package de.cryxy.homeauto.surveillance.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import de.cryxy.homeauto.surveillance.dtos.WebcamDto;

@Entity
@Table(name = "webcam")
public class Webcam {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private String name;

	private String location;

	private String snapshotUri;

	private String snapshotDir;
	private String clientId;

	private Integer minSnapshotsThreshold;
	private Integer alertThreshold;
	private String alertMail;

	// maximales Alter in Tagen, bevor ein automatisches Löschen stattfindet
	private Integer maxAge;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "authorizationId.webcam")
	private List<Authorization> authorizations;

	public Webcam() {

	}

	public Webcam(String name, String location, String snapshotUri, String snapshotDir) {
		super();
		this.name = name;
		this.location = location;
		this.snapshotUri = snapshotUri;
		this.snapshotDir = snapshotDir;
	}

	public Integer getMinSnapshotsThreshold() {
		return minSnapshotsThreshold;
	}

	public void setMinSnapshotsThreshold(Integer minSnapshotsThreshold) {
		this.minSnapshotsThreshold = minSnapshotsThreshold;
	}

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

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Long getId() {
		return id;
	}

	public List<Authorization> getAuthorizations() {
		return authorizations;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	@Override
	public String toString() {
		return "Webcam [id=" + id + ", name=" + name + ", location=" + location + ", snapshotUri=" + snapshotUri
				+ ", snapshotDir=" + snapshotDir + ", minSnapshotsThreshold=" + minSnapshotsThreshold
				+ ", alertThreshold=" + alertThreshold + ", alertMail=" + alertMail + ", maxAge=" + maxAge + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alertMail == null) ? 0 : alertMail.hashCode());
		result = prime * result + ((alertThreshold == null) ? 0 : alertThreshold.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((maxAge == null) ? 0 : maxAge.hashCode());
		result = prime * result + ((minSnapshotsThreshold == null) ? 0 : minSnapshotsThreshold.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((snapshotDir == null) ? 0 : snapshotDir.hashCode());
		result = prime * result + ((snapshotUri == null) ? 0 : snapshotUri.hashCode());
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
		Webcam other = (Webcam) obj;
		if (alertMail == null) {
			if (other.alertMail != null)
				return false;
		} else if (!alertMail.equals(other.alertMail))
			return false;
		if (alertThreshold == null) {
			if (other.alertThreshold != null)
				return false;
		} else if (!alertThreshold.equals(other.alertThreshold))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (maxAge == null) {
			if (other.maxAge != null)
				return false;
		} else if (!maxAge.equals(other.maxAge))
			return false;
		if (minSnapshotsThreshold == null) {
			if (other.minSnapshotsThreshold != null)
				return false;
		} else if (!minSnapshotsThreshold.equals(other.minSnapshotsThreshold))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (snapshotDir == null) {
			if (other.snapshotDir != null)
				return false;
		} else if (!snapshotDir.equals(other.snapshotDir))
			return false;
		if (snapshotUri == null) {
			if (other.snapshotUri != null)
				return false;
		} else if (!snapshotUri.equals(other.snapshotUri))
			return false;
		return true;
	}

	@Transient
	public boolean isAlertActive() {
		return alertThreshold != null && alertThreshold > 0 && alertMail != null && !alertMail.isEmpty();
	}

	@Transient
	public static Webcam fromWebcamDto(WebcamDto dto) {
		Webcam entity = new Webcam();
		entity.id = dto.getId();
		entity.setAlertMail(dto.getAlertMail());
		entity.setAlertThreshold(dto.getAlertThreshold());
		entity.setLocation(dto.getLocation());
		entity.setMinSnapshotsThreshold(dto.getMinSnapshotsThreshold());
		entity.setName(dto.getName());
		entity.setSnapshotDir(dto.getSnapshotDir());
		entity.setMaxAge(dto.getMaxAge());
		entity.setClientId(dto.getClientId());

		return entity;
	}

	@Transient
	public WebcamDto toWebcamDto() {
		WebcamDto dto = new WebcamDto();
		dto.setId(this.getId());
		dto.setName(this.getName());
		dto.setLocation(this.getLocation());
		dto.setSnapshotDir(this.getSnapshotDir());
		dto.setSnapshotUri(this.getSnapshotUri());
		dto.setClientId(this.getClientId());
		dto.setMinSnapshotsThreshold(this.getMinSnapshotsThreshold());
		dto.setAlertThreshold(this.getAlertThreshold());
		dto.setAlertMail(this.getAlertMail());
		dto.setMaxAge(this.getMaxAge());
		return dto;

	}

}
