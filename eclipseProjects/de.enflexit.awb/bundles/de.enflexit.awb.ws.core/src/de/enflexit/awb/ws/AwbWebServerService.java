package de.enflexit.awb.ws;

import de.enflexit.awb.ws.core.JettyConfiguration;

/**
 * The interface AwbWebServerService describes the information to be provided 
 * by an implementation to start an individual Jetty server instance. 
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface AwbWebServerService {

	/**
	 * Has to return a {@link JettyConfiguration} to start an individual Jetty server.
	 * @return the jetty configuration to be used to start an individual server.
	 */
	public JettyConfiguration getJettyConfiguration();
	
}
