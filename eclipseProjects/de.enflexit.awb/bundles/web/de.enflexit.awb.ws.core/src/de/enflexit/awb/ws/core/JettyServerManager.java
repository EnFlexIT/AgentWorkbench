package de.enflexit.awb.ws.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.FilterMapping;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Handler.Sequence;
import org.eclipse.jetty.server.Handler.Wrapper;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.CrossOriginHandler;
import org.eclipse.jetty.server.handler.SecuredRedirectHandler;
import org.eclipse.jetty.util.component.AttributeContainerMap;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo.AWBProduct;
import de.enflexit.awb.ws.AwbSecurityHandlerService;
import de.enflexit.awb.ws.AwbWebHandlerService;
import de.enflexit.awb.ws.AwbWebRegistry;
import de.enflexit.awb.ws.AwbWebServerService;
import de.enflexit.awb.ws.AwbWebServerServiceWrapper;
import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.awb.ws.core.JettyConfiguration.StartOn;
import de.enflexit.awb.ws.core.security.NoSecurityHandler;
import de.enflexit.awb.ws.core.security.SecurityHandlerService;
import de.enflexit.awb.ws.core.session.AWBSessionHandler;
import de.enflexit.awb.ws.webApp.AwbWebApplicationManager;

/**
 * The Singleton <i>JettyServerManager</i> is used to control the start of {@link Server} instances
 * with respect to the registered instances of {@link AwbWebServerService} and {@link AwbWebHandlerService}.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JettyServerManager {

	private static final String AWB_SECURED = "AWB_SECURED";
	
	private AwbWebRegistry awbWebRegistry;
	private TreeMap<String, JettyServerInstances> jettyServerInstanceHash;
	
	private StartOn startOn;
	
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
			Sequence hCollection = this.getHandlerCollection(serverName);
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
			Sequence hCollection = this.getHandlerCollection(serverName);
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
	/**
	 * Returns the jetty server instance hash.
	 * @return the jetty server instance hash
	 */
	private TreeMap<String, JettyServerInstances> getJettyServerInstanceHash() {
		if (jettyServerInstanceHash==null) {
			jettyServerInstanceHash = new TreeMap<String, JettyServerInstances>();
		}
		return jettyServerInstanceHash;
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
		return this.getJettyServerInstanceHash().put(serverName, serverInstances);
	}
	/**
	 * Removes the server instances for the specified server name.
	 *
	 * @param serverName the server name
	 * @return the server removed from the JettyServerManager
	 */
	private JettyServerInstances unregisterServerInstances(String serverName) {
		return this.getJettyServerInstanceHash().remove(serverName);
	}
	/**
	 * Returns the server instances with the specified server name.
	 *
	 * @param serverName the server name
	 * @return the managed Jetty server instances
	 */
	public JettyServerInstances getServerInstances(String serverName) {
		return this.getJettyServerInstanceHash().get(serverName);
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
	public Sequence getHandlerCollection(String serverName) {
		JettyServerInstances sInst = this.getJettyServerInstanceHash().get(serverName);
		if (sInst!=null) return sInst.getHandlerCollection();
		return null;
	}
	
	// ----------------------------------------------------
	// --- From here, start and stop methods for Server ---
	// ----------------------------------------------------
	/**
	 * Starts the specified server.
	 *
	 * @param serverName the server name
	 * @return true, if successful
	 */
	public boolean startServer(String serverName) {
		AwbWebServerServiceWrapper serviceWrapped = this.getAwbWebRegistry().getRegisteredWebServerService(serverName);
		if (serviceWrapped!=null && this.getServerInstances(serverName)==null) {
			return this.startServer(serviceWrapped.getJettyConfiguration());
		}
		return false;
	}
	/**
	 * Starts the server as specified by the {@link JettyConfiguration}.
	 *
	 * @param jConfiguration the JettyConfiguration instance for the server configuration
	 * @return true, if successful
	 */
	public boolean startServer(JettyConfiguration jConfiguration) {
		
		// ----------------------------------------------------------
		// --- Check if the server is already running ---------------
		if (this.getServerInstances(jConfiguration.getServerName())!=null) return false;
		
		// ----------------------------------------------------------
		// --- Try to initialize a AwbWebApplication, if defined ----
		AwbWebApplicationManager.initialize(jConfiguration);
		
		// ----------------------------------------------------------
		// --- Create new server instance ---------------------------
		Server server = new Server(this.getThreadPool(jConfiguration));
		
		// --- Add required beans for session handling --------------
		AWBSessionHandler.addBeanSessionDataStoreFactory(server);
		AWBSessionHandler.addBeanSessionCacheFactory(server);
		
		// ----------------------------------------------------------
		// --- Read & set server configuration ----------------------
		String[] keyArray = jConfiguration.keySet().toArray(new String[jConfiguration.keySet().size()]);
		for (int i = 0; i < keyArray.length; i++) {
			String key   = keyArray[i];
			JettyAttribute<?> attribute = jConfiguration.get(key);
			if (attribute!=null && attribute.getValue()!=null) {
				server.setAttribute(key, attribute.getValue());	
			}
		}

		// ----------------------------------------------------------
		// --- Add HTTP / HTTPS connectors to server? ---------------
		boolean isStartHTTP  = (boolean) server.getAttribute(JettyConstants.HTTP_ENABLED.getJettyKey());
		boolean isStartHTTPS = (boolean) server.getAttribute(JettyConstants.HTTPS_ENABLED.getJettyKey());
		if (isStartHTTP==false && isStartHTTPS==false) {
			String errorMsg = "Error in the configuration for server '" + jConfiguration.getServerName() + "'!";
			BundleHelper.systemPrintln(this, errorMsg, true);
			throw new IllegalArgumentException("Neither HTTP nor HTTPS connections are enabled for the server!");
		}
		if (isStartHTTP==true)  this.configureHTTP(server);
		if (isStartHTTPS==true) this.configureHTTPS(server);
		
		// ----------------------------------------------------------
		// --- Set Handler according to configuration and services --
		Handler initialHandler = jConfiguration.getHandler();
		Sequence hCollection = null;
		if (jConfiguration.isMutableHandlerCollection()==false) {
			// --- NO handler collection ----------------------------
			if (initialHandler==null) {
				// --- Notify about the error -----------------------
				String error = "No handler was specified, nor mutable handler collection was configured for server '" + jConfiguration.getServerName() + "'.";
				BundleHelper.systemPrintln(this, error, true);
				return false;
			} else {
				// --- Add the initial/single handler ---------------
				server.setHandler(initialHandler);
			}
			
		} else {
			List<Handler> handlerList = new ArrayList<>();
			// --- USE Handler collection ---------------------------
			if (initialHandler!=null) handlerList.add(initialHandler);
			// --- Check for handler services -----------------------
			List<AwbWebHandlerService> handlerServiceList = this.getAwbWebRegistry().getAwbWebHandlerService(jConfiguration.getServerName());
			handlerServiceList.forEach(hService -> handlerList.add(hService.getHandler()));
			// --- Add handler collection ---------------------------
			hCollection = new Sequence(true, handlerList);
			server.setHandler(hCollection);
			
		}
		
		
		// ----------------------------------------------------------
		// --- Check for a customizer -------------------------------
		if (jConfiguration.getJettyCustomizer()!=null) {
			try {
				Server customServer = jConfiguration.getJettyCustomizer().customizeConfiguration(jConfiguration, server, hCollection);
				if (customServer!=null) {
					server = customServer;
				}
				
			} catch (Exception ex) {
				String errorMsg = "Error while customizing server instance of server '" + jConfiguration.getServerName() + "' - use standard configuration!";
				BundleHelper.systemPrintln(this, errorMsg, false);
				ex.printStackTrace();
			}
		}
		
		// ----------------------------------------------------------
		// --- Enable CrossOriginFilter ? ---------------------------
		boolean isCorsEnabled = (boolean) server.getAttribute(JettyConstants.CORS_ENABLED.getJettyKey());
		if (isCorsEnabled==true) {
			this.enableCrossOriginHandler(server);
		}
		
		// ----------------------------------------------------------
		// --- Set session handler for the server -------------------
		JettySessionSettings sessionSettings = jConfiguration.getSessionSettings();
		if (sessionSettings.isUseIndividualSettings()==true) {
			if (hCollection==null) {
				this.setSessionHandler(initialHandler, sessionSettings);
			} else {
				this.setSessionHandler(hCollection, sessionSettings);
			}
		}
		
		// ----------------------------------------------------------
		// --- Secure the server ------------------------------------
		JettySecuritySettings securitySettings = jConfiguration.getSecuritySettings();
		if (hCollection==null) {
			this.secureHandler(initialHandler, securitySettings);
		} else {
			this.secureHandler(hCollection, securitySettings);
		}

		// ----------------------------------------------------------
		// --- Always redirect HTTP to HTTPS? -----------------------
		Object redirect = server.getAttribute(JettyConstants.HTTP_TO_HTTPS.getJettyKey());
		if (redirect!=null) {
			boolean isRedirectToHTTPS = (boolean) redirect;
			if (isStartHTTPS==true && isRedirectToHTTPS==true) {
				SecuredRedirectHandler secRedirHandler = new SecuredRedirectHandler();
				secRedirHandler.setHandler(server.getHandler());
				server.setHandler(secRedirHandler);
			}
		}
		
		// ----------------------------------------------------------
		// --- Execute the start of the server ----------------------
		boolean isStarted = this.startConfiguredServer(server, jConfiguration.getServerName());
		if (isStarted==true) {
			this.registerServerInstances(jConfiguration.getServerName(), new JettyServerInstances(server, hCollection));
			BundleHelper.systemPrintln(this, "Started server '" + jConfiguration.getServerName() + "'.", false);
		} else {
			BundleHelper.systemPrintln(this, "Server not started: '" + jConfiguration.getServerName() + "'.", true);
		}
		return isStarted;
	}
	
	/**
	 * Return the thread pool for the current server.
	 *
	 * @param jettyConfiguration the jetty configuration
	 * @return the thread pool
	 */
	private ThreadPool getThreadPool(JettyConfiguration jettyConfiguration) {
		int minThreads = (int)jettyConfiguration.get(JettyConstants.HTTP_MINTHREADS).getValue();
		int maxThreads = (int)jettyConfiguration.get(JettyConstants.HTTP_MAXTHREADS).getValue();
		return new QueuedThreadPool(maxThreads, minThreads);
	}
	
	/**
	 * Configures the HTTP part of the server.
	 * @param server the server to configure
	 */
	private void configureHTTP(Server server) {
		
		boolean isStartHTTP = (boolean) server.getAttribute(JettyConstants.HTTP_ENABLED.getJettyKey());
		int port = (int)server.getAttribute(JettyConstants.HTTP_PORT.getJettyKey());
		int securePort = (int) server.getAttribute(JettyConstants.HTTPS_PORT.getJettyKey());
		String host = (String) server.getAttribute(JettyConstants.HTTP_HOST.getJettyKey());
		if (isStartHTTP==false) return;
		
		try {
			// --- Define HTTP configuration ------------------------
			HttpConfiguration httpConfig = new HttpConfiguration();
			httpConfig.addCustomizer(new SecureRequestCustomizer());
			httpConfig.setSecureScheme(HttpScheme.HTTPS.asString());
			httpConfig.setSecurePort(securePort);
			
			// --- Define the HTTP connector ------------------------
			ServerConnector connector = new ServerConnector(server);
			connector.setHost(host);
			connector.setPort(port);
			connector.addConnectionFactory(new HttpConnectionFactory(httpConfig));

			// --- Add the connector to the server ------------------
			server.setConnectors(new Connector[]{connector});
			server.addBean(new AttributeContainerMap());
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * Configures the HTTP part of the server.
	 * @param server the server to configure
	 */
	private void configureHTTPS(Server server) {
		
		boolean isStartHTTPS = (boolean) server.getAttribute(JettyConstants.HTTPS_ENABLED.getJettyKey());
		if (isStartHTTPS==false) return;

		int securePort = (int) server.getAttribute(JettyConstants.HTTPS_PORT.getJettyKey());
		String secureHost = (String) server.getAttribute(JettyConstants.HTTPS_HOST.getJettyKey());
		
		String keyStoreRelativePath = (String) server.getAttribute(JettyConstants.SSL_KEYSTORE.getJettyKey());
		String keyStoreType = (String) server.getAttribute(JettyConstants.SSL_KEYSTORETYPE.getJettyKey());
		String sslPassword  = (String) server.getAttribute(JettyConstants.SSL_PASSWORD.getJettyKey());
		String sslKeyPassword  = (String) server.getAttribute(JettyConstants.SSL_KEYPASSWORD.getJettyKey());
		
		String sslProtocol  = (String) server.getAttribute(JettyConstants.SSL_PROTOCOL.getJettyKey());
//		String sslAlgorithm = (String) server.getAttribute(JettyConstants.SSL_ALGORITHM.getJettyKey());
		
		boolean isNeedClientAuth = (boolean) server.getAttribute(JettyConstants.SSL_NEEDCLIENTAUTH.getJettyKey());
		boolean isWantClientAuth = (boolean) server.getAttribute(JettyConstants.SSL_WANTCLIENTAUTH.getJettyKey());
		
		try {
			// --- Get the Key-/ Trust-Store file for SSL -----------
			File keyStoreFile = SSLJettyConfiguration.getKeyStoreFileFromRelativePath(keyStoreRelativePath);
			if (keyStoreFile.exists()==false) {
				throw new FileNotFoundException(keyStoreFile.toString());
			}
			String keyStorePath = keyStoreFile.getAbsolutePath();
			
			
			// --- Define the SslContextFactory for the server ------ 
			SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
			sslContextFactory.setKeyStorePath(keyStorePath);
			sslContextFactory.setKeyStoreType(keyStoreType);
			sslContextFactory.setKeyStorePassword(sslPassword);
			sslContextFactory.setKeyManagerPassword(sslKeyPassword);
			
			sslContextFactory.setTrustStorePath(keyStorePath);
			sslContextFactory.setTrustStorePassword(sslPassword);
			
			sslContextFactory.setProtocol(sslProtocol);
			
			sslContextFactory.setNeedClientAuth(isNeedClientAuth);
			sslContextFactory.setWantClientAuth(isWantClientAuth);
			
			// --- Adjust SecureRequestCustomizer -------------------
			SecureRequestCustomizer secReqCustom = new SecureRequestCustomizer();
			secReqCustom.setSniHostCheck(false);

			// --- Define HTTPS Configuration -----------------------
			HttpConfiguration httpsConfig = new HttpConfiguration();
			httpsConfig.addCustomizer(secReqCustom);
			httpsConfig.setSecurePort(securePort);
			
			// --- Create the SSL Connector -------------------------
			ServerConnector sslConnector = new ServerConnector(server,  new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()), new HttpConnectionFactory(httpsConfig));
			sslConnector.setHost(secureHost);
			sslConnector.setPort(securePort);
			
			// --- Add the connector to the server ------------------
			server.addConnector(sslConnector);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Secure the specified handler collection.
	 *
	 * @param hCollection the handler collection to secure
	 * @param securitySettings the security settings
	 */
	private void setSessionHandler(Sequence hCollection, JettySessionSettings sessionSettings) {
		List<Handler> handlerList = hCollection.getHandlers();
		if (handlerList==null) return;
		for (int i = 0; i < handlerList.size(); i++) {
			this.setSessionHandler(handlerList.get(i), sessionSettings);
		}
	}
	/**
	 * Secures the specified handler.
	 *
	 * @param handler the handler
	 * @param sessionSettings the session settings
	 */
	private void setSessionHandler(Handler handler, JettySessionSettings sessionSettings) {

		if (handler==null) return;
		if (! (handler instanceof ServletContextHandler)) return;
		
		// --- Get the servlet context handler to secure ----------------------
		ServletContextHandler serCtxHandler = (ServletContextHandler) handler;
		serCtxHandler.setSessionHandler(new AWBSessionHandler(sessionSettings));
	}
	
	/**
	 * Secure the specified handler collection.
	 *
	 * @param hCollection the handler collection to secure
	 * @param securitySettings the security settings
	 */
	private void secureHandler(Sequence hCollection, JettySecuritySettings securitySettings) {
		List<Handler> handlerList = hCollection.getHandlers();
		if (handlerList==null) return;
		for (int i = 0; i < handlerList.size(); i++) {
			this.secureHandler(handlerList.get(i), securitySettings);
		}
	}
	/**
	 * Secures the specified handler.
	 *
	 * @param handler the handler
	 * @param securitySettings the security settings
	 */
	private void secureHandler(Handler handler, JettySecuritySettings securitySettings) {

		if (handler==null) return;
		if (! (handler instanceof ServletContextHandler)) return;
		
		// --- Get the servlet context handler to secure ----------------------
		ServletContextHandler serCtxHandler = (ServletContextHandler) handler;
		String contextPath = serCtxHandler.getContextPath();
		
		// --- Get the activated security configuration -----------------------
		ServletSecurityConfiguration ssc = securitySettings.getActivedServletSecurityConfiguration(contextPath);
		if (ssc==null) return;

		// --- Get the corresponding security handler service ----------------- 
		AwbSecurityHandlerService securityService = SecurityHandlerService.getAwbSecurityHandlerService(ssc.getSecurityHandlerName());
		if (securityService==null) return;
		
		// --- Initiate the security handler ----------------------------------
		try {

			// --- Get handler from specified service -------------------------
			SecurityHandler securtiyHandler = securityService.getNewSecurityHandler(ssc.getSecurityHandlerConfiguration());
			serCtxHandler.setSecurityHandler(securtiyHandler);
			serCtxHandler.getInitParams().put(AWB_SECURED, AWB_SECURED);
			
		} catch (Exception ex) {
			BundleHelper.systemPrintln(this, "Error while trying to secure servlet for context path '" + contextPath+ "':", true);
			ex.printStackTrace() ;
		}
	}
	
	/**
	 * Wraps a CrossOriginHandler around the specified server handler.
	 *
	 * @param server the current server instance to configure
	 * @return the handler collection that wraps the original handler
	 */
	private void enableCrossOriginHandler(Server server) {
		
		// --- Create CORS handler instance ---------------
		CrossOriginHandler corsHandler = new CrossOriginHandler();
		
		// --- Get CORS-settings --------------------------
		for (JettyConstants jettyConstant : JettyConstants.values()) {
			
			if (jettyConstant.getJettyKey().startsWith("cors.")==false) continue;
			if (jettyConstant.getJettyKey().startsWith("cors.filter")==true) continue;
			
			String corsValue = server.getAttribute(jettyConstant.getJettyKey()).toString();
			
			switch (jettyConstant) {
			case CORS_ALLOWED_ORIGINS_PARAM:
				corsHandler.setAllowedOriginPatterns(Set.of(corsValue.split(",")));
				break;
			case CORS_ALLOWED_TIMING_ORIGINS_PARAM:
				corsHandler.setAllowedTimingOriginPatterns(Set.of(corsValue.split(",")));
				break;
			case CORS_ALLOWED_METHODS_PARAM:
				corsHandler.setAllowedMethods(Set.of(corsValue.split(",")));
				break;
			case CORS_ALLOWED_HEADERS_PARAM:
				corsHandler.setAllowedHeaders(Set.of(corsValue.split(",")));
				break;
			case CORS_PREFLIGHT_MAX_AGE_PARAM:
				corsHandler.setPreflightMaxAge(Duration.ofSeconds(Long.valueOf(corsValue)));
				break;
			case CORS_ALLOW_CREDENTIALS_PARAM:
				corsHandler.setAllowCredentials(Boolean.valueOf(corsValue));
				break;
			case CORS_EXPOSED_HEADERS_PARAM:
				corsHandler.setExposedHeaders(Set.of(corsValue.split(",")));
				break;
			case CORS_CHAIN_PREFLIGHT_PARAM:
				corsHandler.setDeliverPreflightRequests(Boolean.valueOf(corsValue));
				break;
				
			default:
				break;
			}
		}
		// --- Set server handler as cors child handler ---
		corsHandler.setHandler(server.getHandler());
		// --- Set Cors as server handler -----------------
		server.setHandler(corsHandler);
	}
	
	/**
	 * Start configured server.
	 *
	 * @param server the server
	 * @param serverName the server name
	 * @return true, if successful
	 */
	private boolean startConfiguredServer(final Server server, final String serverName) {
		
		// --- Start the server -------------------------------------
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					server.setServer(server);
					server.setStopAtShutdown(true);
					server.start();
					
				} catch (Exception ex) {
					System.err.println("[" + JettyServerManager.this.getClass().getSimpleName() + "] Thread '" + Thread.currentThread().getName() + "' Error while trying to start server '" + serverName + "':");
					ex.printStackTrace();

					// --- Shutdown Agent.Workbench ??? -------------
					if (Application.getGlobalInfo().getAWBProduct()==AWBProduct.WEB) {
						System.err.println("[" + JettyServerManager.this.getClass().getSimpleName() + "] Failure while starting server - shuting down " + Application.getGlobalInfo().getApplicationTitle() + " ... ");
						Application.stop();
					}
				}
			}
		}, "Jetty_" + serverName).start();
		
		// --- Wait for the start of the server ---------------------
		long waitTime    = 10 * 1000; // --- maximum 5 s -------------
 		long waitTimeEnd = System.currentTimeMillis() + waitTime;
		while (server.isStarting() || (server.isStarted()==false && System.currentTimeMillis()<=waitTimeEnd)) {
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
			// --- Dispose the relevant AwbWebHandlerServices -------
			this.disposeAwbWebHandlerServices(serverName);
			// --- Unregister server instance -----------------------
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
		
		try {
			// --- Stop the server ----------------------------------
			server.stop();
			// --- Remove security handler -------------------------
			List<Handler> handlerList = server.getHandlers();
			this.removeSecurityHandler(handlerList);
			this.removeCorsFilter(handlerList);
			
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
	
	/**
	 * Removes the security handler from the specified handler array.
	 * @param handlerList the handler list
	 */
	private void removeSecurityHandler(List<Handler> handlerList) {
		if (handlerList==null) return;
		for (int i = 0; i < handlerList.size(); i++) {
			this.removeSecurityHandler(handlerList.get(i));
		}
	}
	/**
	 * Removes the security handler.
	 * @param handler the handler
	 */
	private void removeSecurityHandler(Handler handler) {

		if (handler instanceof ServletContextHandler) {
			ServletContextHandler servletContextHandler = (ServletContextHandler) handler;
			if (servletContextHandler.getInitParameter(AWB_SECURED)!=null) {
				servletContextHandler.setSecurityHandler(new NoSecurityHandler());
				servletContextHandler.getInitParams().remove(AWB_SECURED);
			}
			
		} else if (handler instanceof Wrapper) {
			Wrapper handlerWrapper = (Wrapper) handler;
			this.removeSecurityHandler(handlerWrapper.getHandlers());
			
		} else if (handler instanceof Sequence) {
			Sequence handlerCollection = (Sequence) handler;
			this.removeSecurityHandler(handlerCollection.getHandlers());
		}
	}
	
	/**
	 * Removes the CORS filter from the specified handler array..
	 * @param handlerList the handler array
	 */
	private void removeCorsFilter(List<Handler> handlerList) {
		if (handlerList==null) return;
		for (int i = 0; i < handlerList.size(); i++) {
			this.removeCorsFilter(handlerList.get(i));
		}
	}
	/**
	 * Removes the CORS filter.
	 * @param handler the handler
	 */
	private void removeCorsFilter(Handler handler) {
		
		if (handler instanceof ServletContextHandler) {
			
			// --- Cast to required type ----------------------------
			ServletContextHandler servletContextHandler = (ServletContextHandler) handler;
			
			// --- Check the FilterMappings -------------------------
			FilterMapping[] fmArray = servletContextHandler.getServletHandler().getFilterMappings();
			for (FilterMapping fm : fmArray) {
				if (fm.getFilterName().contains(CrossOriginHandler.class.getName())==true) {
					servletContextHandler.getServletHandler().removeFilterMapping(fm);
				}
			}
			
			// --- Check the FilterHolder ---------------------------
			FilterHolder[] fhArray = servletContextHandler.getServletHandler().getFilters();
			for (FilterHolder fh : fhArray) {
				if (fh.getClassName().equals(CrossOriginHandler.class.getName())==true) {
					servletContextHandler.getServletHandler().removeFilterHolder(fh);
					continue;
				}
				if (fh.getHeldClass()!=null && fh.getHeldClass().equals(CrossOriginHandler.class)==true) {
					servletContextHandler.getServletHandler().removeFilterHolder(fh);
					continue;
				}
			}
			
		} else if (handler instanceof Wrapper) {
			Wrapper handlerWrapper = (Wrapper) handler;
			this.removeCorsFilter(handlerWrapper.getHandlers());
			
		} else if (handler instanceof Sequence) {
			Sequence handlerCollection = (Sequence) handler;
			this.removeCorsFilter(handlerCollection.getHandlers());
		}
	}
	
	/**
	 * Removes the session handler from the specified handler array.
	 * @param handlserServices the list of handler services
	 */
	private void disposeAwbWebHandlerServices(String serverName) {
		try {
			this.getAwbWebRegistry().getAwbWebHandlerService(serverName).forEach(hs -> hs.disposeHandler());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	// ------------------------------------------------------------------------------------------------------
	// --- From here, methods to start/stop the configured server based on AWB - ApplicationEvents ----------
	// ------------------------------------------------------------------------------------------------------
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
