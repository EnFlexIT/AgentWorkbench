package de.enflexit.awb.ws.restapi;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

import de.enflexit.awb.ws.restapi.gen.InfoApi;
import de.enflexit.awb.ws.restapi.gen.JacksonJsonProvider;
import de.enflexit.awb.ws.restapi.gen.LoadApi;
import de.enflexit.awb.ws.restapi.gen.StateApi;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

/**
 * The Class AwbRestApplication describes the JERSEY application to start on the {@link DefaultAwbServer}.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
@ApplicationPath("/" + JerseyResourceConfig.APPLICATION_CONTEXT_PATH)
public class JerseyResourceConfig extends ResourceConfig {

	public static final String APPLICATION_NAME = "AWB - Rest API";
	public static final String APPLICATION_CONTEXT_PATH = "api";
	
	/**
	 * Instantiates a AwbRestApplication.
	 */
	public JerseyResourceConfig() { 
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
		this.register(InfoApi.class);
		this.register(LoadApi.class);
		this.register(StateApi.class);
		
	}
	/**
	 * Configure swagger.
	 */
	private void configureSwagger() {
		
		// --- For swagger 1.x.x (OpenAPI v2) -------------
		this.register(ApiListingResource.class);
		this.register(SwaggerSerializers.class);

		BeanConfig config = new BeanConfig();
		config.setConfigId("de.enflexit.awb.ws.restapi");
		config.setTitle(APPLICATION_NAME);
		config.setVersion("v1");
		config.setBasePath("/");
		config.setResourcePackage("de.enflexit.awb.ws.restapi.gen");
		config.setPrettyPrint(true);
		config.setScan(true);
		
	}
}
