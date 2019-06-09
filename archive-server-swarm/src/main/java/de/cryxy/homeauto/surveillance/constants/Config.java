package de.cryxy.homeauto.surveillance.constants;

public class Config {

	/**
	 * Clean-up-interval in hours.
	 */
	public static final String CLEAN_UP_INTERVAL = "de.cryxy.homeauto.surveillance.cleanUpInterval";

	/**
	 * Activate mail notification.
	 */
	public static final String MAIL_NOTIFICATION = "de.cryxy.homeauto.surveillance.mail.active";

	/**
	 * SMTP-Server
	 */
	public static final String MAIL_HOST = "de.cryxy.homeauto.surveillance.mail.smtp.host";

	/**
	 * Port-SMTP-Server
	 */
	public static final String MAIL_PORT = "de.cryxy.homeauto.surveillance.mail.smtp.port";

	/**
	 * Mail-From
	 */
	public static final String MAIL_FROM = "de.cryxy.homeauto.surveillance.mail.smtp.from";

	/**
	 * Activate scanning for new files on startup.
	 */
	public static final String IO_INITIALWALK = "de.cryxy.homeauto.surveillance.io.initialwalk";

	/**
	 * Watch for new files.
	 */
	public static final String IO_WATCH = "de.cryxy.homeauto.surveillance.io.watch";

	/**
	 * Autodelete added files.
	 */
	public static final String IO_AUTODELETE = "de.cryxy.homeauto.surveillance.io.autoDelete";

	/**
	 * MQTT: publish events?
	 */
	public static final String MQTT_PUBLISH = "de.cryxy.homeauto.surveillance.mqtt.publishEvents";

	/**
	 * MQTT: topic
	 */
	public static final String MQTT_TOPIC = "de.cryxy.homeauto.surveillance.mqtt.topic";

	/**
	 * MQTT: server.uri
	 */
	public static final String MQTT_SERVER_URI = "de.cryxy.homeauto.surveillance.mqtt.server.uri";

	/**
	 * MQTT: server.user.name
	 */
	public static final String MQTT_SERVER_USER_NAME = "de.cryxy.homeauto.surveillance.mqtt.server.user.name";

	/**
	 * MQTT: server.user.password
	 */
	public static final String MQTT_SERVER_USER_PASSWORD = "de.cryxy.homeauto.surveillance.mqtt.server.user.password";

	/**
	 * MQTT: clientId
	 */
	public static final String MQTT_CLIENT_ID = "de.cryxy.homeauto.surveillance.mqtt.clientId";

}
