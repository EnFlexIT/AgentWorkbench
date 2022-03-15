package de.enflexit.awb.ws.server;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import de.enflexit.awb.ws.restapi.JerseyHelloWorld;

/**
 * The Class AwbRestApplication describes the JERSEY application to start on the {@link DefaultAwbServer}.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbRestApplication extends Application {

	public static String NAME = "AWB - Rest API";
	
	/**
	 * Instantiates a AwbRestApplication.
	 */
	public AwbRestApplication() { }

	/* (non-Javadoc)
	 * @see javax.ws.rs.core.Application#getClasses()
	 */
	@Override
	public final Set<Class<?>> getClasses() {
		
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(JerseyHelloWorld.class);
		return classes;
	}
	
}
