package de.enflexit.awb.ws.dynSiteApi;

import java.util.Locale;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Variant;

import org.glassfish.jersey.server.ResourceConfig;

import de.enflexit.awb.ws.dynSiteApi.gen.ContentApi;
import de.enflexit.awb.ws.dynSiteApi.gen.JacksonJsonProvider;
import de.enflexit.awb.ws.dynSiteApi.gen.MenuApi;


/**
 * The Class AwbRestApplication describes the JERSEY application to start on the configured AWB-Server.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@ApplicationPath("/" + RestApiConfiguration.APPLICATION_CONTEXT_PATH)
public class RestApiConfiguration extends ResourceConfig {

	public static final String APPLICATION_NAME = "Dynamic Content - API";
	public static final String APPLICATION_CONTEXT_PATH = "dc";
	
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
		this.configureSwagger();
	}
	/**
	 * Configure REST end points.
	 */
	private void configureEndpoints() {
		this.register(JacksonJsonProvider.class);
	
		this.register(ContentApi.class);
		this.register(MenuApi.class);
	}
	
	/**
	 * Configure swagger.
	 */
	private void configureSwagger() {
		
		// --- For swagger 1.x.x (OpenAPI v2) -------------
//		this.register(ApiListingResource.class);
//		this.register(SwaggerSerializers.class);

//		BeanConfig config = new BeanConfig();
//		config.setTitle(APPLICATION_NAME);
//
//		config.setVersion("1.0.0");
//		config.setSchemes(new String[] {"http", "https"});
//		config.setHost("localhost:8080");
//		config.setBasePath("/api");
//
//		config.setConfigId("de.enflexit.awb.ws.dynSiteApi");
//
//		config.setResourcePackage("de.enflexit.awb.ws.dynSiteApi.gen");
//		config.setScan(true);
//		config.setPrettyPrint(true);
	}
	
	
	/**
	 * Return the {@link Variant} for a {@link Response} of a service end point.
	 * @return the response variant
	 */
	public static Variant getResponseVariant() {
		return new Variant(RESPONSE_MEDIA_TYPE, RESPONSE_LOCALE, RESPONSE_ENCODING);
	}
	
}
