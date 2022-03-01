package de.enflexit.awb.ws.core;

import java.util.TreeMap;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;

import de.enflexit.awb.ws.AwbWebHandlerService;
import de.enflexit.awb.ws.AwbWebServerService;

/**
 * The Singleton ServerManager .
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JettyServerManager {

	// ----------------------------------------------------
	// --- The singleton create / access area -------------
	// ----------------------------------------------------
	private static JettyServerManager jettyServerManager;
	private JettyServerManager() { }
	public static JettyServerManager getInstance() {
		if (jettyServerManager==null) {
			jettyServerManager = new JettyServerManager();
		}
		return jettyServerManager;
	}

	// ----------------------------------------------------
	// --- Detail methods of the class --------------------
	// ----------------------------------------------------
	private TreeMap<String, JettyServerInstances> jettyServerHash;
	
	/**
	 * Returns the jetty server instance hash.
	 * @return the jetty server instance hash
	 */
	private TreeMap<String, JettyServerInstances> getJettyServerInstaceHash() {
		if (jettyServerHash==null) {
			jettyServerHash = new TreeMap<String, JettyServerInstances>();
		}
		return jettyServerHash;
	}
	/**
	 * Can be used to register server instances. This will allow to couple the shutdown of 
	 * Agent.Workbench with the shutdown of all registered server instances.
	 *
	 * @param serverName the server name
	 * @param serverInstances the server
	 */
	private JettyServerInstances registerServerInstances(String serverName, JettyServerInstances serverInstances) {
		return this.getJettyServerInstaceHash().put(serverName, serverInstances);
	}
	/**
	 * Removes the server instances for the specified server name.
	 *
	 * @param serverName the server name
	 * @return the server removed from the JettyServerManager
	 */
	private JettyServerInstances unregisterServerInstances(String serverName) {
		return this.getJettyServerInstaceHash().remove(serverName);
	}
	/**
	 * Returns the server instances with the specified server name.
	 *
	 * @param serverName the server name
	 * @return the managed Jetty server instances
	 */
	private JettyServerInstances getServerInstances(String serverName) {
		return this.getJettyServerInstaceHash().get(serverName);
	}
	
	
	// ----------------------------------------------------
	// --- From here, access methods to server instances --
	// ----------------------------------------------------
	/**
	 * Returns the server with the specified server name.
	 *
	 * @param serverName the server name
	 * @return the server
	 */
	public Server getServer(String serverName) {
		JettyServerInstances sInst = this.getServerInstances(serverName);
		if (sInst!=null) return sInst.getServer();
		return null;
	}
	/**
	 * Returns the {@link HandlerCollection} for the specified server name.
	 *
	 * @param serverName the server name
	 * @return the handler collection
	 */
	public HandlerCollection getHandlerCollection(String serverName) {
		JettyServerInstances sInst = this.getJettyServerInstaceHash().get(serverName);
		if (sInst!=null) return sInst.getHandlerCollection();
		return null;
	}
	
	
	// ----------------------------------------------------
	// --- From here, start and stop methods for Server ---
	// ----------------------------------------------------
	/**
	 * Start server.
	 * @param serverConfig the server configuration
	 */
	public boolean startServer(JettyConfiguration serverConfig) {
		
		// --- Create new server instance ---------------------------
		Server server = new Server(serverConfig.getHttpPort());

		// --- Read & set server configuration ----------------------
		String[] keyArray = serverConfig.keySet().toArray(new String[serverConfig.keySet().size()]);
		for (int i = 0; i < keyArray.length; i++) {
			String key   = keyArray[i];
			JettyAttribute<?> attribute = serverConfig.get(key);
			if (attribute!=null && attribute.getValue()!=null) {
				server.setAttribute(key, attribute.getValue());	
			}
		}
		
		// --- Set the Handler according to the configuration -------
		HandlerCollection hCollection = serverConfig.isMutableHandlerCollection()==true ? new HandlerCollection(true) : null;
		if (serverConfig.getHandler()!=null) {
			if (hCollection!=null) {
				hCollection.addHandler(serverConfig.getHandler());
				server.setHandler(hCollection);
			} else {
				server.setHandler(serverConfig.getHandler());
			}
		}
		
		// --- Execute the start of the server ----------------------
		boolean isStarted = this.startConfiguredServer(server, serverConfig.getServerName());
		if (isStarted==true) {
			this.registerServerInstances(serverConfig.getServerName(), new JettyServerInstances(server, hCollection));
			System.err.println("[" + this.getClass().getSimpleName() + "] Started server '" + serverConfig.getServerName() + "'.");
		}
		return isStarted;
	}
	/**
	 * Start configured server.
	 *
	 * @param server the server
	 * @param serverName the server name
	 * @return true, if successful
	 */
	private boolean startConfiguredServer(final Server server, String serverName) {
		
		// --- Start the server ---------------------------
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					server.setStopAtShutdown(true);
					server.start();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}, "Jetty_" + serverName).start();
		
		// --- Wait for the start of the server -----------
		long waitTime    = 5 * 1000; // --- maximum 5 s ---
 		long waitTimeEnd = System.currentTimeMillis() + waitTime;
		while (server.isStarted()==false && System.currentTimeMillis()<=waitTimeEnd) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException iEx) {
				iEx.printStackTrace();
			}
		}
		return server.isStarted();
	}
	
	
	/**
	 * Stops the server with the specified server name.
	 *
	 * @param serverName the server name
	 * @return true, if successful
	 */
	public boolean stopServer(String serverName) {
		if (this.stopServer(this.getServer(serverName))==true) {
			this.unregisterServerInstances(serverName);
			return true;
		}
		return false;
	}
	/**
	 * Stops the specified server instance.
	 *
	 * @param server the server
	 * @return true, if successful
	 */
	private boolean stopServer(Server server) {
		
		if (server==null) return false;
			
		// --- Stop the server ----------------------------
		try {
			server.stop();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// --- Wait for the stop of the server ------------
		long waitTime    = 5 * 1000; // --- maximum 5 s ---
 		long waitTimeEnd = System.currentTimeMillis() + waitTime;
		while (server.isStopped()==false && System.currentTimeMillis()<=waitTimeEnd) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException iEx) {
				iEx.printStackTrace();
			}
		}
		return server.isStopped();
	}
	
	// ----------------------------------------------------
	// --- From here, methods to act on Handler -----------
	// ----------------------------------------------------
	/**
	 * If possible, adds the specified handler to the server specified its name.
	 *
	 * @param serverName the server name
	 * @param handler the handler
	 */
	public void addHandler(String serverName, Handler handler) {
		
		// --- Check for parameter errors ---------------------------
		if (handler==null) {
			System.err.println("[" + this.getClass().getSimpleName() + "] No Handler was specified to add to any server.");
			return;
		}
		if (serverName==null) {
			System.err.println("[" + this.getClass().getSimpleName() + "] No server name was specified to add handler '" + handler.getClass().getSimpleName() + "'");
			return;
		}
		
		// --- Get the HandlerCollection of the server --------------
		HandlerCollection hCollection = this.getHandlerCollection(serverName);
		if (hCollection==null) {
			System.err.println("[" + this.getClass().getSimpleName() + "] No HandlerCollection could be found for server '" + serverName + "'! Thus, could not add specified Handler '" + handler.getClass().getName() + "'.");
			return;
		}
		
		// --- Add the new handler to the HandlerCollection ---------
		try {
			hCollection.addHandler(handler);
			handler.start();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	// ----------------------------------------------------
	// --- From here, methods for the service handling ----
	// ----------------------------------------------------

	/**
	 * Adds the specified {@link AwbWebServerService} to the running servers.
	 * @param newServer the new server to start
	 */
	public void addAwbWebServerService(AwbWebServerService newServer) {
		
		JettyConfiguration config = newServer.getJettyConfiguration();
		Server server = this.getServer(config.getServerName());
		if (server==null) {
			// --- Start the server -----------------------
			this.startServer(newServer.getJettyConfiguration());
		} else {
			// --- Server already there
			System.err.println("[" + this.getClass().getSimpleName() + "] Server '" + config.getServerName() + "' was already started." );
		}
	}
	
	/**
	 * Adds the specified {@link AwbWebHandlerService} to the running servers.
	 * @param newHandler the new handler
	 */
	public void addAwbWebHandlerService(AwbWebHandlerService newHandler) {
		System.out.println("[" + this.getClass().getSimpleName() + "] Add AwbWebHandlerService " + newHandler.getClass().getName());
	}
	
}
