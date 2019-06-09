package de.cryxy.homeauto.surveillance.daos;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import de.cryxy.homeauto.surveillance.entities.Webcam;

public class WebcamDao {

	@Inject
	private Provider<EntityManager> em;

	@Inject
	private AuthorizationDao authorizationDao;

	public Webcam addWebcam(String name, String location) {

		Webcam webcam = new Webcam(name, location, null, null);
		em.get().persist(webcam);
		return webcam;
	}

	public void removeWebcam(Long webcamId) {

		Webcam webcam = getWebcam(webcamId);
		authorizationDao.removeAuthorizationForWebcam(webcamId);

		em.get().remove(webcam);

	}

	public List<Webcam> getWebcams() {
		TypedQuery<Webcam> q = em.get().createQuery("SELECT w FROM Webcam w", Webcam.class);
		List<Webcam> webcams = q.getResultList();

		return webcams;
	}

	public Webcam getWebcam(Long webcamId) {

		return em.get().find(Webcam.class, webcamId);

	}

	public void updateWebcam(Long webcamId, Webcam webcamToUpdate) {
		Webcam currentWebcam = getWebcam(webcamId);

		if (!currentWebcam.getId().equals(webcamToUpdate.getId())) {
			throw new IllegalArgumentException("No matching ids");
		}

		em.get().merge(webcamToUpdate);
	}

}
