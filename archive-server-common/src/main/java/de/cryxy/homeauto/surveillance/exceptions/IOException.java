package de.cryxy.homeauto.surveillance.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOException extends RuntimeException {
	
	private final Logger LOG = LoggerFactory.getLogger(IOException.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public IOException(String message) {
		super(message);
		log(message);
	}
	
	
	public IOException(String message, Throwable cause) {
		super(message, cause);
		log(message);
	}
	
	private void log(String message) {
		LOG.error(message, this);
	}
	
	

}
