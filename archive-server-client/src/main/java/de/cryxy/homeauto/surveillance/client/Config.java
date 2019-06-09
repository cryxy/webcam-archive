package de.cryxy.homeauto.surveillance.client;

import java.io.FileInputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import org.jboss.weld.exceptions.IllegalArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class Config {

	private String pathToConfig = "config/config.properties";

	private final Logger LOG = LoggerFactory.getLogger(Config.class);

	/*
	 * Client
	 */
	private final String PROP_VARCHIVE_CLIENT_ID = "de.cryxy.homeauto.surveillance.client.id";
	private final String PROP_VARCHIVE_CLIENT_AUTO_DELETE = "de.cryxy.homeauto.surveillance.client.autoDelete";
	private final String PROP_VARCHIVE_CLIENT_INITIAL_WALK = "de.cryxy.homeauto.surveillance.client.initialWalk";

	/*
	 * VArchive-Server
	 */
	private final String PROP_VARCHIVE_API_URL = "de.cryxy.homeauto.surveillance.client.varchive.api.url";
	private final String PROP_VARCHIVE_API_USERNAME = "de.cryxy.homeauto.surveillance.client.varchive.api.user";
	private final String PROP_VARCHIVE_API_PASSWORD = "de.cryxy.homeauto.surveillance.client.varchive.api.password";

	private Properties config;

	public Config() {

	}

	public Config(String pathToConfig) {

		this.pathToConfig = pathToConfig;

	}

	@PostConstruct
	public void init() {

		try {

			String recorderEnvironmentConfig = System.getenv("RECORDER_CONFIG");
			if (recorderEnvironmentConfig != null) {
				pathToConfig = recorderEnvironmentConfig;
			}

			LOG.info("Startup, read config from: " + pathToConfig);

			config = new Properties();
			FileInputStream in = new FileInputStream(pathToConfig);
			config.load(in);
			in.close();
			LOG.debug("Configuration readed.");
		} catch (Exception e) {
			throw new RuntimeException("Error reading config.", e);
		}

	}

	public String getClientId() {
		String property = config.getProperty(PROP_VARCHIVE_CLIENT_ID);
		if (property == null)
			throw new IllegalArgumentException("The client id can not be null.");
		return property;
	}

	public Boolean getClientIsAutoDelete() {
		return Boolean.valueOf(config.getProperty(PROP_VARCHIVE_CLIENT_AUTO_DELETE));
	}

	public Boolean getClientShouldRunInitialWalk() {
		return Boolean.valueOf(config.getProperty(PROP_VARCHIVE_CLIENT_INITIAL_WALK));
	}

	public String getApiUrl() {
		return config.getProperty(PROP_VARCHIVE_API_URL);
	}

	public String getApiUser() {
		return config.getProperty(PROP_VARCHIVE_API_USERNAME);
	}

	public String getApiPassword() {
		return config.getProperty(PROP_VARCHIVE_API_PASSWORD);
	}

}
