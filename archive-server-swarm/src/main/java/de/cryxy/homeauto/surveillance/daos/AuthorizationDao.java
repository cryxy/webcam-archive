package de.cryxy.homeauto.surveillance.daos;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.cryxy.homeauto.surveillance.entities.Authorization;
import de.cryxy.homeauto.surveillance.entities.Authorization.AuthorizationId;
import de.cryxy.homeauto.surveillance.entities.Webcam;
import de.cryxy.homeauto.surveillance.enums.AccessRight;

public class AuthorizationDao {

	@Inject
	private Provider<EntityManager> em;

	private final Logger LOG = LoggerFactory.getLogger(AuthorizationDao.class);



	public Authorization addAuthorization(String userId, Webcam webcam, AccessRight right) {
		Authorization authorization = new Authorization(new Authorization.AuthorizationId(userId, webcam), right);
		em.get().persist(authorization);
		return authorization;
	}
	

	public boolean isAuthorized(String userId, Long webcamId, AccessRight requestedRight) {

		Webcam webcam = em.get().find(Webcam.class, webcamId);
		if (webcam == null) {
			LOG.warn("No webcam object for the given webcamId found");
			return false;
		}

		Authorization authorization = em.get().find(Authorization.class,
				new Authorization.AuthorizationId(userId, webcam));
		if (authorization == null) {
			LOG.warn("No authorization object for the given userId found");
			return false;
		}

		switch (authorization.getAccessRight()) {
		case WRITE:

			return true;

		case READ:
			if (requestedRight == AccessRight.WRITE)
				return false;
			return true;

		default:
			return false;
		}

	}
	
	protected void removeAuthorizationForWebcam(Long webcamId) {
		TypedQuery<Authorization> q = em.get().createQuery("SELECT a FROM Authorization a WHERE a.authorizationId.webcam.id =:webcamId", Authorization.class);
		q.setParameter("webcamId", webcamId);
		List<Authorization> authorizations = q.getResultList();
		
		for(Authorization authorization : authorizations) {
			removeAuthorization(authorization.getAuthorizationId());
		}
		
	}
	
	protected void removeAuthorization(AuthorizationId authorizationId){
		Authorization authorization = em.get().find(Authorization.class, authorizationId);
		em.get().remove(authorization);
	}
	

}
