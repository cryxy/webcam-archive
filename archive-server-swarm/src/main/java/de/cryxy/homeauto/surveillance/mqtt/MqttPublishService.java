/**
 * 
 */
package de.cryxy.homeauto.surveillance.mqtt;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import de.cryxy.homeauto.surveillance.constants.Config;
import de.cryxy.homeauto.surveillance.events.SnapshotEvent;
import de.cryxy.homeauto.surveillance.exceptions.IOException;

/**
 * @author fabian
 *
 */
@Startup
@Singleton
public class MqttPublishService {

	private final Logger LOG = LoggerFactory.getLogger(MqttPublishService.class);

	@Inject
	@ConfigurationValue(Config.MQTT_PUBLISH)
	private Boolean publish;

	@Inject
	@ConfigurationValue(Config.MQTT_TOPIC)
	private String rootTopic;

	@Inject
	@ConfigurationValue(Config.MQTT_SERVER_URI)
	private String serverUri;

	@Inject
	@ConfigurationValue(Config.MQTT_SERVER_USER_NAME)
	private String userName;

	@Inject
	@ConfigurationValue(Config.MQTT_SERVER_USER_PASSWORD)
	private String userPassword;

	@Inject
	@ConfigurationValue(Config.MQTT_CLIENT_ID)
	private String clientId;

	private MqttClient client;
	private ObjectMapper mapper;

	public void onSnapshotEvent(@Observes(during = TransactionPhase.AFTER_SUCCESS) SnapshotEvent event) {
		if (!publish) {
			LOG.info("Ignore publish event!");
			return;
		}

		// Create Jsonb and serialize
		String message;

		String topic = String.format("%s/webcam/%s/snapshot", rootTopic,
				event.getValue().toSnapshotDto().getWebcamId());
		try {
			message = mapper.writeValueAsString(event.getValue().toSnapshotDto());
			client.publish(topic, new MqttMessage(message.getBytes()));
		} catch (MqttPersistenceException e) {
			LOG.error("Error persisting MQTT message!", e);
		} catch (MqttException e) {
			LOG.error("Error publishing MQTT message!", e);
			throw new IOException("Error publishing mqtt message!", e);
		} catch (JsonProcessingException e) {
			LOG.error("Error writing value as json object!", e);
			throw new IOException("Error writing value as json object!", e);
		}
	}

	@PostConstruct
	public void setup() {

		if (!publish) {
			LOG.info("MqttPublishService is disabled!");
			return;
		}

		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		LOG.info("Connect to mqtt server={}, with user={}.", serverUri, userName);

		MqttConnectOptions options = new MqttConnectOptions();
		// Setzen einer Persistent Session
		options.setCleanSession(false);
		options.setUserName(userName.trim());
		options.setPassword(userPassword.trim().toCharArray());
		LOG.trace("Connect to mqtt server with password={}.", userPassword);

		try {
			client = new MqttClient(serverUri, clientId);
			client.setCallback(new MqttCallbackExtended() {

				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {

				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					LOG.trace("Message delivered: {}", token.getMessageId());

				}

				@Override
				public void connectionLost(Throwable cause) {
					LOG.error("Mqtt connection lost! Try a reconnect!");
					setup();

				}

				@Override
				public void connectComplete(boolean reconnect, String serverURI) {
					LOG.info("Successfully connected to mqtt server!");

				}
			});

			LOG.info("Try to connect to mqtt server!");
			client.connect(options);

		} catch (MqttException e) {
			LOG.error("Error while trying to connect to mqtt server.", e);
			throw new IOException("Error while trying to connect to mqtt server.", e);
		}
	}

	@PreDestroy
	public void tearDown() {
		try {
			client.disconnect();
		} catch (MqttException e) {
			LOG.error("Error while disconnecting ...", e);
		}
	}

}
