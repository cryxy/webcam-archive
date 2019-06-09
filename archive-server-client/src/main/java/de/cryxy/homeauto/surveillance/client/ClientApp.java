/**
 * 
 */
package de.cryxy.homeauto.surveillance.client;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.cryxy.homeauto.surveillance.client.io.FileWatcherService;
import de.cryxy.homeauto.surveillance.client.io.InitialFileWalker;

/**
 * @author fabian
 *
 */
public class ClientApp {

	private final Logger LOG = LoggerFactory.getLogger(ClientApp.class);

	public ClientApp() {

		LOG.info("Starting ...");

		SeContainerInitializer initializer = SeContainerInitializer.newInstance();

		SeContainer container = initializer.initialize();

		LOG.info("Container initialized ...");

		FileWatcherService fileWatcherService = container.select(FileWatcherService.class).get();
		fileWatcherService.start();

		Config config = container.select(Config.class).get();

		if (config.getClientShouldRunInitialWalk()) {
			InitialFileWalker initialFileWalker = container.select(InitialFileWalker.class).get();
			initialFileWalker.walkWebcams();
		} else {
			LOG.info("Skipping initial walk ...");
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {

			public void run() {
				LOG.info("Shutdown ...");
				try {
					fileWatcherService.stop();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new ClientApp();

	}

}
