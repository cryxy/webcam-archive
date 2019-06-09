package de.cryxy.homeauto.surveillance.mapper;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Jackson JSON processor could be controlled via providing a custom Jackson 2
 * ObjectMapper (or ObjectMapper for Jackson 1) instance. This could be handy if
 * you need to redefine the default Jackson behaviour and to fine-tune how your
 * JSON data structures look like. Detailed description of all Jackson features
 * is out of scope of this guide. The example below gives you a hint on how to
 * wire your ObjectMapper (ObjectMapper) instance into your Jersey application.
 * 
 * @author Fabian
 * 
 * @see https://jersey.java.net/documentation/latest/media.html#json.jackson
 *
 */
@Provider
public class MyJacksonObjectMapperProvider implements ContextResolver<ObjectMapper> {

	final ObjectMapper defaultObjectMapper;

	public MyJacksonObjectMapperProvider() {
		defaultObjectMapper = createDefaultMapper();
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return defaultObjectMapper;
	}

	private static ObjectMapper createDefaultMapper() {
		final ObjectMapper result = new ObjectMapper();
		// umstellen, sobald jersey jackson 2.6 mit JavaTimeModule unterstuetzt.
		result.registerModule(new JavaTimeModule());
		result.enable(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS);
		result.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		return result;
	}

}
