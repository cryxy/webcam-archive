package de.cryxy.homeauto.surveillance.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import de.cryxy.homeauto.surveillance.enums.AccessRight;

@Entity
@Table(name="authorization")
public class Authorization {
	
	public Authorization() {
	}
	
	public Authorization(AuthorizationId authorizationId, AccessRight accessRight) {
		super();
		this.authorizationId = authorizationId;
		this.accessRight = accessRight;
	}



	@EmbeddedId
	private AuthorizationId authorizationId;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private AccessRight accessRight;


	public AccessRight getAccessRight() {
		return accessRight;
	}

	public void setAccessRight(AccessRight accessRight) {
		this.accessRight = accessRight;
	}
	
	


	public AuthorizationId getAuthorizationId() {
		return authorizationId;
	}

	@Override
	public String toString() {
		return "Authorization [authorizationId=" + authorizationId + ", accessRight=" + accessRight + "]";
	}
	
	@Embeddable
	public static class AuthorizationId implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private String userId;	
		@ManyToOne
		private Webcam webcam;
		
		public AuthorizationId() {
			
		}
		
		public AuthorizationId(String userId, Webcam webcam) {
			super();
			this.userId = userId;
			this.webcam = webcam;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public Webcam getWebcam() {
			return webcam;
		}
		public void setWebcam(Webcam webcam) {
			this.webcam = webcam;
		}
		@Override
		public String toString() {
			return "AuthorizationId [userId=" + userId + ", webcam=" + webcam + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
			AuthorizationId other = (AuthorizationId) obj;
			if (userId == null) {
				if (other.userId != null)
					return false;
			} else if (!userId.equals(other.userId))
				return false;
			if (webcam == null) {
				if (other.webcam != null)
					return false;
			} else if (!webcam.equals(other.webcam))
				return false;
			return true;
		}

	
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessRight == null) ? 0 : accessRight.hashCode());
		result = prime * result + ((authorizationId == null) ? 0 : authorizationId.hashCode());
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
		Authorization other = (Authorization) obj;
		if (accessRight != other.accessRight)
			return false;
		if (authorizationId == null) {
			if (other.authorizationId != null)
				return false;
		} else if (!authorizationId.equals(other.authorizationId))
			return false;
		return true;
	}
	
	

}
