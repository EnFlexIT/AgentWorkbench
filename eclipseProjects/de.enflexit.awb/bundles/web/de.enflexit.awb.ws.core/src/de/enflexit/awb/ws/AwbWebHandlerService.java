package de.enflexit.awb.ws;

import org.eclipse.jetty.server.Handler;

import de.enflexit.awb.ws.server.AwbServer;

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
	 * @see AwbServer
	 * @see AwbServer#NAME
	 */
	public String getServerName();
	
	/**
	 * Returns the server name to which the local handler has to be added.
	 * @return the server name not null
	 */
	public default String getServerNameNotNull() {
		return this.getServerName()==null ? AwbServer.NAME : this.getServerName();
	}
	
	/**
	 * Has to return the {@link Handler} to be added to the server specified by the server name.
	 * @return the handler
	 */
	public Handler getHandler();
	
	/**
	 * Has to dispose the current handler. Will be invoked during server shutdown.
	 */
	public void disposeHandler();
	
}
