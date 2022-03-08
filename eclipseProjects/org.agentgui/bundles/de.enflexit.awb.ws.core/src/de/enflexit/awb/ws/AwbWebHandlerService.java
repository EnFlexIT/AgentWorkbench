package de.enflexit.awb.ws;

import org.eclipse.jetty.server.Handler;

import de.enflexit.awb.ws.defaultServer.DefaultAwbServer;

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
	 * 
	 * @return the server name
	 * @see DefaultAwbServer
	 * @see DefaultAwbServer#DEFAULT_AWB_SERVER_NAME
	 */
	public String getServerName();
	
	/**
	 * Returns the server name to which the local handler has to be added.
	 * @return the server name not null
	 */
	public default String getServerNameNotNull() {
		return this.getServerName()==null ? DefaultAwbServer.DEFAULT_AWB_SERVER_NAME : this.getServerName();
		
	}
	
	/**
	 * Has to return the {@link Handler} to be added to the server specified by the server name.
	 * @return the handler
	 */
	public Handler getHandler();
	
}
