package de.enflexit.awb.ws.restapi;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

import de.enflexit.awb.ws.restapi.endPoints.HelloWorld;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;

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
		this.register(GsonProvider.class);
		this.register(HelloWorld.class);
	}
	/**
	 * Configure swagger.
	 */
	private void configureSwagger() {
		this.registerClasses(OpenApiResource.class, AcceptHeaderOpenApiResource.class);
	}
}
