package de.enflexit.awb.ws.core;

import java.util.List;
import java.util.TreeMap;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;

import de.enflexit.awb.ws.AwbWebHandlerService;
import de.enflexit.awb.ws.AwbWebRegistry;
import de.enflexit.awb.ws.AwbWebServerService;
import de.enflexit.awb.ws.AwbWebServerServiceWrapper;
import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.awb.ws.core.JettyConfiguration.StartOn;

/**
 * The Singleton <i>JettyServerManager</i> is used to control the start of {@link Server} instances
 * with respect to the registered instances of {@link AwbWebServerService} and {@link AwbWebHandlerService}.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JettyServerManager {

	// ----------------------------------------------------
	// --- The singleton create / access area -------------
	// ----------------------------------------------------
	private static JettyServerManager jettyServerManager;
	/**
	 * Instantiates a new jetty server manager.
	 */
	private JettyServerManager() { }
	/**
	 * Returns the single instance of JettyServerManager.
	 * @return single instance of JettyServerManager
	 */
	public static JettyServerManager getInstance() {
		if (jettyServerManager==null) {
			jettyServerManager = new JettyServerManager();
		}
		return jettyServerManager;
	}

	
	// --------------------------------------------------------------
	// --- From here, methods for the AwbWebService handling --------
	// --------------------------------------------------------------
	private AwbWebRegistry awbWebRegistry;
	/**
	 * Returns the local/current {@link AwbWebRegistry}.
	 * @return the AwbWebRegistry
	 */
	public AwbWebRegistry getAwbWebRegistry() {
		if (awbWebRegistry==null) {
			awbWebRegistry = new AwbWebRegistry();
		}
		return awbWebRegistry;
	}
	/**
	 * Adds the specified {@link AwbWebServerService} to the running servers.
	 * @param newServer the new server to start
	 */
	public void addAwbWebServerService(AwbWebServerService newServer) {
		// --- Add to local registry ----------------------
		AwbWebServerServiceWrapper serviceWrapper = this.getAwbWebRegistry().addAwbWebServerService(newServer);
		if (serviceWrapper!=null && this.startOn!=null) {
			// --- Start that server? ---------------------   
			JettyConfiguration config = serviceWrapper.getJettyConfiguration();
			if (config.getStartOn().ordinal()<=this.startOn.ordinal()) {
				// --- Start the server -------------------
				this.startServer(config);
			}
		}
	}
	/**
	 * Removes the specified {@link AwbWebServerService} from the running servers.
	 * @param serverToRemove the server to remove
	 */
	public void removeAwbWebServerService(AwbWebServerService serverToRemove) {
		// --- Remove from local registry -----------------
		boolean removed = this.getAwbWebRegistry().removeAwbWebServerService(serverToRemove);
		if (removed==true) {
			// --- Stop if server is running --------------
			String serverName = serverToRemove.getJettyConfiguration().getServerName();
			Server server = this.getServer(serverName);
			if (server!=null) {
				this.stopServer(serverName);
			}
		}
	}
	
	
	/**
	 * Adds the specified {@link AwbWebHandlerService} to the running servers.
	 * @param newHandlerService the new handler service
	 */
	public void addAwbWebHandlerService(AwbWebHandlerService newHandlerService) {
		// --- Add to local registry --------------------------------
		boolean added = this.getAwbWebRegistry().addAwbWebHandlerService(newHandlerService);
		// --- Check if the corresponding server is running ---------
		if (added==true) {
			// --- Check if server is running -----------------------
			String serverName = newHandlerService.getServerNameNotNull();
			Server server = this.getServer(serverName);
			HandlerCollection hCollection = this.getHandlerCollection(serverName);
			if (server!=null && hCollection!=null && this.getAwbWebRegistry().isValidAwbWebHandlerService(newHandlerService, true)==null) {
				// --- Add the new handler to the HandlerCollection ---------
				try {
					Handler handler = newHandlerService.getHandler();
					hCollection.addHandler(handler);
					handler.start();
					BundleHelper.systemPrintln(this, "Added handler of service '" + newHandlerService.getClass().getName() + "' to server '" + serverName + "'.", false);
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
					
			} else {
				// --- Show message, if server runs and no HandlerCollection was found --------------------
				if (server!=null && hCollection==null) {
					String error = "The server '" + serverName + "' does not use a mutable handler collection. Thus, it can not be extended by another Handler.";
					BundleHelper.systemPrintln(this, error, true);
				}
			}
		}
	}
	/**
	 * Removes the specified {@link AwbWebHandlerService} to the running servers.
	 * @param handlerToRemove the handler to remove
	 */
	public void removeAwbWebHandlerService(AwbWebHandlerService handlerToRemove) {
		
		boolean removed = this.getAwbWebRegistry().removeAwbWebHandlerService(handlerToRemove);
		// --- Check, if the handler needs to be removed from a server instance ---------
		if (removed==true) {
			// --- Check if server is running -----------------------
			String serverName = handlerToRemove.getServerNameNotNull();
			Server server = this.getServer(serverName);
			HandlerCollection hCollection = this.getHandlerCollection(serverName);
			if (server!=null && hCollection!=null && this.getAwbWebRegistry().isValidAwbWebHandlerService(handlerToRemove, false)==null) {
				// --- Remove the handler ---------------------------
				try {
					Handler handler = handlerToRemove.getHandler();
					handler.stop();
					hCollection.removeHandler(handler);
					BundleHelper.systemPrintln(this, "Removed handler of service '" + handlerToRemove.getClass().getName() + "' from server '" + serverName + "'.", false);
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			
		}
	}
	
	
	// ----------------------------------------------------
	// --- From here, instance handling -------------------
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
	 * @return the jetty server instances
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
	public JettyServerInstances getServerInstances(String serverName) {
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
	 *
	 * @param serverConfig the server configuration
	 * @return true, if successful
	 */
	public boolean startServer(JettyConfiguration serverConfig) {
		
		// ----------------------------------------------------------
		// --- Create new server instance ---------------------------
		Server server = new Server(serverConfig.getHttpPort());

		// ---------------------------------------------------------- 
		// --- Read & set server configuration ----------------------
		String[] keyArray = serverConfig.keySet().toArray(new String[serverConfig.keySet().size()]);
		for (int i = 0; i < keyArray.length; i++) {
			String key   = keyArray[i];
			JettyAttribute<?> attribute = serverConfig.get(key);
			if (attribute!=null && attribute.getValue()!=null) {
				server.setAttribute(key, attribute.getValue());	
			}
		}
		
		// ----------------------------------------------------------
		// --- Secure the server ------------------------------------
//		HashLoginService  loginService = new HashLoginService();
//		loginService.setConfig("ToDo");
//		server.addBean(loginService);
		
		// ----------------------------------------------------------
		// --- Set the Handler according to the configuration -------
		Handler initialHandler = serverConfig.getHandler();
		HandlerCollection hCollection = serverConfig.isMutableHandlerCollection()==true ? new HandlerCollection(true) : null;
		if (hCollection==null) {
			// --- NO handler collection ----------------------------
			if (initialHandler==null) {
				// --- Notify about the error -----------------------
				String error = "No handler was specified, nor mutable handler collection was configured for server '" + serverConfig.getServerName() + "'.";
				BundleHelper.systemPrintln(this, error, true);
				return false;
			} else {
				// --- Add the initial/single handler ---------------
				server.setHandler(serverConfig.getHandler());
			}
		} else {
			// --- USE Handler collection ---------------------------
			if (initialHandler!=null) {
				hCollection.addHandler(initialHandler);
			}
			server.setHandler(hCollection);
		}

		// ----------------------------------------------------------
		// --- Check to add further handler -------------------------
		List<AwbWebHandlerService> handlerList = this.getAwbWebRegistry().getAwbWebHandlerService(serverConfig.getServerName());
		if (handlerList.size()>0) {
			for (int i = 0; i < handlerList.size(); i++) {
				AwbWebHandlerService handlerService = handlerList.get(i);
				hCollection.addHandler(handlerService.getHandler());
			}
		}
		
		// ----------------------------------------------------------
		// --- Check for a customizer -------------------------------
		if (serverConfig.getJettyCustomizer()!=null) {
			try {
				Server customServer = serverConfig.getJettyCustomizer().customizeConfiguration(server, hCollection);
				if (customServer!=null) {
					server = customServer;
				}
				
			} catch (Exception ex) {
				String errorMsg = "Error while customizing server instance of server '" + serverConfig.getServerName() + "' - use standard configuration!";
				BundleHelper.systemPrintln(this, errorMsg, false);
				ex.printStackTrace();
			}
		}
		
		
		
		// ----------------------------------------------------------
		// --- Execute the start of the server ----------------------
		boolean isStarted = this.startConfiguredServer(server, serverConfig.getServerName());
		if (isStarted==true) {
			this.registerServerInstances(serverConfig.getServerName(), new JettyServerInstances(server, hCollection));
			BundleHelper.systemPrintln(this, "Started server '" + serverConfig.getServerName() + "'.", false);
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
			BundleHelper.systemPrintln(this, "Stopped  server '" + serverName + "'.", false);
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
	
	
	// ------------------------------------------------------------------------------------------------------
	// --- From here, methods to start/stop the configured server based on AWB - ApplicationEvents ----------
	// ------------------------------------------------------------------------------------------------------
	private StartOn startOn;
	/**
	 * Returns the current local {@link StartOn} value.
	 * @param startOn the new start on
	 */
	private void setStartOn(StartOn startOn) {
		this.startOn = startOn;
	}
	/**
	 * Decreases the current local start on value.
	 */
	private void decreaseStartOnValue() {
		this.startOn = StartOn.decrease(this.startOn);
	}
	
	/**
	 * Does the server start for all servers that are using the specified {@link StartOn} option.
	 * 
	 * @param startOn the start on option
	 */
	public void doServerStart(StartOn startOn) {
		
		// --- Set the local start on value ---------------
		this.setStartOn(startOn);
		
		// --- Get the server to be started ---------------
		List<AwbWebServerServiceWrapper> serverServicesWrapped = this.getAwbWebRegistry().getAwbWebServerService(startOn);
		for (int i = 0; i < serverServicesWrapped.size(); i++) {
			// --- Get the wrapped server service ---------
			AwbWebServerServiceWrapper serverToStartWrapped = serverServicesWrapped.get(i);
			// --- Get the configuration ------------------
			JettyConfiguration config = serverToStartWrapped.getJettyConfiguration();
			// --- Check if server is running -------------
			Server server = this.getServer(config.getServerName());
			if (server==null) {
				// --- Start the server -------------------
				this.startServer(config);
			} else {
				// --- Server is already there ------------
				BundleHelper.systemPrintln(this, "Server '" + config.getServerName() + "' was already started.", true);
			}
		}
	}
	
	/**
	 * Does the server stop for all servers that are using the specified {@link StartOn} option.
	 *
	 * @param startOn the start on
	 */
	public void doServerStop(StartOn startOn) {
		
		// --- Get the server to be started ---------------
		List<AwbWebServerServiceWrapper> serverServicesWrapped = this.getAwbWebRegistry().getAwbWebServerService(startOn);
		for (int i = 0; i < serverServicesWrapped.size(); i++) {
			// --- Get the server service -----------------
			AwbWebServerServiceWrapper serverToStartWrapped = serverServicesWrapped.get(i);
			// --- Get the configuration ------------------
			JettyConfiguration config = serverToStartWrapped.getJettyConfiguration();
			// --- Check if server is running -------------
			Server server = this.getServer(config.getServerName());
			if (server!=null) {
				this.stopServer(config.getServerName());
			}
		}
		
		// --- Revert the local 'start on' value by one ---
		this.decreaseStartOnValue();
	}
	
}
