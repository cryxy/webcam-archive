package de.cryxy.homeauto.surveillance.io;

public interface FileWatcherService {

	void start(boolean autoDelete);

	void stop() throws Exception;

}