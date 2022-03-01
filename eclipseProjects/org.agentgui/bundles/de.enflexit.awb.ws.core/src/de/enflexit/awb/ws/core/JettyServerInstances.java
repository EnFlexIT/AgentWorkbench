package de.enflexit.awb.ws.core;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;

public class JettyServerInstances {

	private Server server;
	private HandlerCollection handlerCollection;

	
	/**
	 * Instantiates a new reminder of known and managed jetty instances.
	 *
	 * @param server the server
	 * @param handlerCollection the handler collection
	 */
	public JettyServerInstances(Server server, HandlerCollection handlerCollection) { 
		if (server==null) {
			throw new NullPointerException("The server instance is not allowed to null!");
		}
		this.setServer(server);
		this.setHandlerCollection(handlerCollection);
	}
	
	/**
	 * Sets the server instance.
	 * @param server the new server
	 */
	public void setServer(Server server) {
		this.server = server;
	}
	/**
	 * Returns the server instance.
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}
	
	/**
	 * Sets the handler collection.
	 * @param handlerCollection the new handler collection
	 */
	public void setHandlerCollection(HandlerCollection handlerCollection) {
		this.handlerCollection = handlerCollection;
	}
	/**
	 * Returns the handler collection.
	 * @return the handler collection
	 */
	public HandlerCollection getHandlerCollection() {
		return handlerCollection;
	}
	
}
