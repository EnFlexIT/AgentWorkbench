package de.enflexit.awb.ws.core;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;

/**
 * The Class JettyServerInstances stores the runtime instance of a server and enables to
 * access the corresponding handler.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
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
			throw new NullPointerException("The server instance is not allowed to be null!");
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

	// --------------------------------------------------------------
	// --- From here, some help methods -----------------------------
	// --------------------------------------------------------------
	/**
	 * Returns the list of {@link Handler} running on Jetty server. This may be different to the {@link #getHandlerCollection(String)} method
	 * of this class, since a {@link JettyConfiguration} can be customized by {@link JettyCustomizer}.
	 *
	 * @return the server handler on the server.
	 */
	public List<Handler> getServerHandlerList() {
		if (this.getServer()!=null ) {
			return Arrays.asList(this.getServer().getHandlers());
		}
		return null;
	}
	
}
