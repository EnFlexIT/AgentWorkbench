package de.enflexit.awb.ws.restapi;

import java.util.Locale;

import org.glassfish.jersey.server.ResourceConfig;

import de.enflexit.awb.ws.restapi.gen.InfoApi;
import de.enflexit.awb.ws.restapi.gen.JacksonJsonProvider;
import de.enflexit.awb.ws.restapi.gen.LoadApi;
import de.enflexit.awb.ws.restapi.gen.StateApi;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Variant;

/**
 * The Class AwbRestApplication describes the JERSEY application to start on the configured AWB-Server.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@ApplicationPath("/" + RestApiConfiguration.APPLICATION_CONTEXT_PATH)
public class RestApiConfiguration extends ResourceConfig {

	public static final String APPLICATION_NAME = "AWB - Rest API";
	public static final String APPLICATION_CONTEXT_PATH = "api";
	
	public static final String RESPONSE_ENCODING = "UTF-8";
	public static final Locale RESPONSE_LOCALE= Locale.ENGLISH;
	public static final MediaType RESPONSE_MEDIA_TYPE = MediaType.APPLICATION_JSON_TYPE;
	
	
	/**
	 * Instantiates a AwbRestApplication.
	 */
	public RestApiConfiguration() { 
		super();
		this.setApplicationName(APPLICATION_NAME);
		this.configureEndpoints();
	}
	/**
	 * Configure REST end points.
	 */
	private void configureEndpoints() {
		this.register(JacksonJsonProvider.class);
	
		this.register(InfoApi.class);
		this.register(LoadApi.class);
		this.register(StateApi.class);
		
	}
	
	/**
	 * Return the {@link Variant} for a {@link Response} of a service end point.
	 * @return the response variant
	 */
	public static Variant getResponseVariant() {
		return new Variant(RESPONSE_MEDIA_TYPE, RESPONSE_LOCALE, RESPONSE_ENCODING);
	}
	
}
