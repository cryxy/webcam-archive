/**
 * 
 */
package de.cryxy.homeauto.surveillance.io;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import de.cryxy.homeauto.surveillance.constants.Config;

/**
 * @author fabian
 *
 */
@Startup
@Singleton
public class IOControlBean {

	private final Logger LOG = LoggerFactory.getLogger(IOControlBean.class);

	@Inject
	@ConfigurationValue(Config.IO_INITIALWALK)
	private Boolean initialWalk;

	@Inject
	@ConfigurationValue(Config.IO_WATCH)
	private Boolean watch;

	@Inject
	@ConfigurationValue(Config.IO_AUTODELETE)
	private Boolean autoDelete;

	@Inject
	private Instance<InitialFileWalker> initialFileWalker;

	@Inject
	private Instance<FileWatcherService> fileWatcherService;

	@PostConstruct
	public void start() {

		LOG.info("Start IO-Operations!");

		if (initialWalk) {
			initialFileWalker.get().walkWebcams();
		} else {
			LOG.info("Initial walk is deactivated.");
		}

		if (watch) {
			fileWatcherService.get().start(autoDelete);
		} else {
			LOG.info("FileWatcher is deactivated.");
		}

	}

}
