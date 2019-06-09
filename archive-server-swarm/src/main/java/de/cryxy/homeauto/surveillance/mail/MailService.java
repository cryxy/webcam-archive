package de.cryxy.homeauto.surveillance.mail;

public interface MailService {

	void setup(String smtpHost, String smtpPort, String fromAddress);

	void sendMessageWithSnapshotsForEvent(Long eventId);

}
