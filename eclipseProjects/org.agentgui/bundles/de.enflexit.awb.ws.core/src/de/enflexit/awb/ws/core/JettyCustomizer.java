package de.enflexit.awb.ws.core;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;

/**
 * The Interface JettyCustomizer can be used with a {@link JettyConfiguration} to
 * programmatically customize a Jetty instance before the server is started.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 * 
 * @see JettyConfiguration#setJettyCustomizer(JettyCustomizer)
 */
public interface JettyCustomizer {

	/**
	 * Can be used to programmatically customize the specified Jetty {@link Server} before it is started.
	 *
	 * @param server the server
	 * @param handlerCollection the previously configured handler collection (may be <code>null</code>). 
	 * If not null, it represents the current handler of the server
	 * @return has to return the customized server
	 */
	public Server customizeConfiguration(Server server, HandlerCollection handlerCollection);
	
	
}
