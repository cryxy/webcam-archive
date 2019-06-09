package de.cryxy.homeauto.surveillance.mail.impl;

import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import de.cryxy.homeauto.surveillance.constants.Config;
import de.cryxy.homeauto.surveillance.daos.EventDao;
import de.cryxy.homeauto.surveillance.entities.Event;
import de.cryxy.homeauto.surveillance.entities.Snapshot;
import de.cryxy.homeauto.surveillance.entities.Webcam;
import de.cryxy.homeauto.surveillance.enums.ImageSize;
import de.cryxy.homeauto.surveillance.io.IOHelper;
import de.cryxy.homeauto.surveillance.mail.MailService;

@Singleton
@Startup
public class MailServiceImpl implements MailService {

	private Session session;

	@Inject
	private EventDao eventDao;
	
	@Inject
	@ConfigurationValue(Config.MAIL_HOST)
	private String host;
	
	@Inject
	@ConfigurationValue(Config.MAIL_PORT)
	private String port;
	
	@Inject
	@ConfigurationValue(Config.MAIL_FROM)
	private String fromAddress;

	final String subjectFormat = "Alert for webcam %s";

	private final Logger LOG = LoggerFactory.getLogger(MailServiceImpl.class);
	
	@PostConstruct
	public void init() {
		LOG.info("Init with host={}, port={}, fromAdress={}...",host,port,fromAddress);
		setup(host, port, fromAddress);
	}

	@Override
	public void setup(String smtpHost, String smtpPort, String fromAddress) {
		LOG.info("Seting up MailService.");

		final Properties props = new Properties();
		props.setProperty("mail.smtp.host", smtpHost);
		// props.setProperty( "mail.smtp.auth", "true" );
		props.setProperty("mail.smtp.port", smtpPort);

		this.session = Session.getInstance(props);

		this.fromAddress = fromAddress;
	}

	@Override
	public void sendMessageWithSnapshotsForEvent(Long eventId) {
		LOG.info("Sending mail for eventId={}.", eventId);

		if (session == null) {
			throw new IllegalStateException("Call the setup method before sending a mail.");
		}

		final Event event = eventDao.getEvent(eventId);
		if (event == null) {
			LOG.error("Could not find event.");
			return;
		}
		final Webcam webcam = event.getWebcam();
		final List<Snapshot> snapshots = event.getSnapshots();

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(webcam.getAlertMail()));
			message.setSubject(String.format(subjectFormat, webcam.getName()));

			// Adding the HTML part
			Multipart multipart = new MimeMultipart();
			BodyPart htmlPart = new MimeBodyPart();
			StringBuilder content = new StringBuilder();
			content.append("<html>");
			content.append("<body>");
			content.append("<p>").append("An alert was triggerd by webcam ").append(webcam.getName()).append(" on ");
			content.append(event.getStartDate().toString()).append(":").append("</p>");

			for (Snapshot snapshot : snapshots) {
				content.append("<img src=");
				content.append("'");
				content.append("cid:" + snapshot.getFileName()).append("' ");
				content.append("alt='").append(snapshot.getFileName());
				content.append("'>");
			}
			content.append("</body>");
			content.append("</html>");
			htmlPart.setContent(content.toString(), "text/html");
			htmlPart.setDisposition(BodyPart.INLINE);
			multipart.addBodyPart(htmlPart);

			// Adding the Image parts
			for (Snapshot snapshot : snapshots) {
				BodyPart attach = new MimeBodyPart();
				DataSource source = new ByteArrayDataSource(IOHelper.resizeSnapshot(snapshot, ImageSize.SMALL),
						"image/jpeg");
				attach.setDataHandler(new DataHandler(source));
				attach.setFileName(snapshot.getFileName());
				attach.setHeader("Content-ID", "<" + snapshot.getFileName() + ">");
				multipart.addBodyPart(attach);
				
			}
			
			message.setContent(multipart);
			Transport.send(message);

		} catch (Exception e) {
			LOG.error("Error sending mail for event.",e);
		} 

	}

}
