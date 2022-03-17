package de.enflexit.awb.ws.restapi;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

import de.enflexit.awb.ws.restapi.endPoints.HelloWorld;

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
		this.initialize();
	}
	/**
	 * Initializes the AWB - Rest API.
	 */
	private void initialize() {
		this.setApplicationName(APPLICATION_NAME);
		this.register(GsonProvider.class);
		this.register(HelloWorld.class);
	}
	
}
