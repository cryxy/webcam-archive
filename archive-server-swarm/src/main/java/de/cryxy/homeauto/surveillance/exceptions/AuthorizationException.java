package de.cryxy.homeauto.surveillance.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorizationException extends RuntimeException {

	private final Logger LOG = LoggerFactory.getLogger(AuthorizationException.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuthorizationException() {
		super();
		log("No message given");
	}

	public AuthorizationException(String message) {
		super(message);
		log(message);
	}

	private void log(String message) {
		LOG.error(message, this);
	}

}
