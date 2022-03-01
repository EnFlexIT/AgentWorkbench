package de.enflexit.awb.ws;

import org.eclipse.jetty.server.Handler;

/**
 * The interface AwbWebHandlerService describes the information to be provided 
 * by an implementation to add another handler to a Jetty instance. 
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface AwbWebHandlerService {

	/**
	 * Has to return the name of the server to which the local {@link Handler} has to be added.<br>
	 * If this method return <code>null</code>, the default AWB server will be used to add the specified Handler.
	 * @return the server name
	 */
	public String getServerName();
	
	/**
	 * Has to return the handler.
	 * @return the handler
	 */
	public Handler getHandler();
	
}
