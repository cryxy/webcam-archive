package de.cryxy.homeauto.surveillance.client.io;

public interface FileWatcherService {

	void start();

	void stop() throws Exception;

}