package de.cryxy.homeauto.surveillance.client.io;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class IOHelper {

	public static ZonedDateTime creationTimeFromPath(Path path) throws java.io.IOException {
		BasicFileAttributes fileAttributes = Files.readAttributes(path, BasicFileAttributes.class);
		Instant instant = fileAttributes.creationTime().toInstant();
		ZonedDateTime creationTime = instant.atZone(ZoneId.systemDefault());
		return creationTime;
	}

}
