/**
 * 
 */
package de.cryxy.homeauto.surveillance.daos;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author fabian
 *
 */
@ApplicationScoped
public class EntityManagerProducer {

	@Produces
	@PersistenceContext(unitName = "archiveUnit")
	private EntityManager em;

}
