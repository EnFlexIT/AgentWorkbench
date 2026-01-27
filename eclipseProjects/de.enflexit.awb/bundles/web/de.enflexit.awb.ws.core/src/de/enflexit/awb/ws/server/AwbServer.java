package de.enflexit.awb.ws.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Handler.Sequence;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.resource.Resources;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.config.GlobalInfo.ExecutionMode;
import de.enflexit.awb.ws.AwbSecurityHandlerService;
import de.enflexit.awb.ws.AwbWebServerService;
import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettyConfiguration.StartOn;
import de.enflexit.awb.ws.core.JettyCustomizer;
import de.enflexit.awb.ws.core.JettySecuritySettings;
import de.enflexit.awb.ws.core.JettyWebApplicationSettings;
import de.enflexit.awb.ws.core.JettyWebApplicationSettings.UpdateStrategy;
import de.enflexit.awb.ws.core.ServletSecurityConfiguration;
import de.enflexit.awb.ws.core.model.HandlerHelper;
import de.enflexit.awb.ws.core.security.OIDCSecurityHandler;
import de.enflexit.awb.ws.core.security.SecurityHandlerService;
import de.enflexit.awb.ws.core.security.jwt.JwtSingleUserSecurityHandler;
import de.enflexit.awb.ws.core.security.jwt.JwtSingleUserSecurityService.JwtParameter;
import de.enflexit.awb.ws.core.session.AWBSessionHandler;
import de.enflexit.awb.ws.core.util.WebApplicationUpdate;
import de.enflexit.awb.ws.core.util.WebApplicationUpdateProcess;
import de.enflexit.awb.ws.core.util.WebApplicationVersion;

/**
 * The Class for the default AWB Server.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbServer implements AwbWebServerService, JettyCustomizer {

	public static final String NAME = "AWB-WebServer";
	public static final String AWB_SERVER_ROOT_PATH = "webRoot";
	public static final String AWB_SERVER_DOWNLOAD_PATH = "webDownloads";
	public static final String AWB_SERVER_WEB_APP_ARCHIVE_PATH = "webAppArchive";
	
	public static final String AWB_BASE_WEB_APP_DOWNLOAD_URL = "https://p2.enflex.it/web/baseTemplate/";
	
	private JettyConfiguration jettyConfiguration;
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbWebServerService#getJettyConfiguration()
	 */
	@Override
	public JettyConfiguration getJettyConfiguration() {
		if (jettyConfiguration==null) {
			jettyConfiguration = new JettyConfiguration(NAME, this.getStartOn(), null, true);
			jettyConfiguration.setJettyCustomizer(this);
		}
		return jettyConfiguration;
	}
	
	/**
	 * Depending on the {@link ExecutionMode} of AWB, this method returns the default argument for when the default server has to be started.
	 * @return the StartOn parameter for the DefaultAwbServer 
	 */
	private StartOn getStartOn() {
		
		switch (Application.getGlobalInfo().getExecutionMode()) {
		case APPLICATION:
			return StartOn.AwbStart;
			
		case SERVER:
		case SERVER_MASTER:
		case SERVER_SLAVE:
			return StartOn.AwbStart;

		case DEVICE_SYSTEM:
			return StartOn.AwbStart;
		}
		return StartOn.AwbStart;
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.core.JettyCustomizer#customizeConfiguration(de.enflexit.awb.ws.core.JettyConfiguration, org.eclipse.jetty.server.Server, org.eclipse.jetty.server.Handler.Sequence)
	 */
	@Override
	public Server customizeConfiguration(JettyConfiguration jettyConfiguration, Server server, Sequence handlerCollection) {

		// ----------------------------------------------------------
		// --- Define ServletContextHandler for static content ------
		// ----------------------------------------------------------
		ServletContextHandler servletContextHandler = new ServletContextHandler("/", ServletContextHandler.SESSIONS);
        servletContextHandler.setContextPath("/");
        servletContextHandler.setWelcomeFiles(new String[] { "index.html" });
        servletContextHandler.addServlet(DefaultServlet.class, "/"); 
        
        // --- Create web root resource -----------------------------
        Path webRootPath = this.getWebRootPathValidated();
        
        // --- Check if web root directory is empty -----------------
        if (this.isEmptyWebRootPath(webRootPath)==true ) {
    		// --- Install the base application (template) ----------
    		this.installBaseApplication(jettyConfiguration);
        } else {
        	// --- Check to Update to the latest version ------------  
        	this.checkAndInstallApplicationUpdate(jettyConfiguration);
        }

        // --- Create web root resource -----------------------------
        Resource resBase = ResourceFactory.of(servletContextHandler).newResource(webRootPath);
        if (Resources.isReadableDirectory(resBase)==false) {
        	System.err.println("[" + this.getClass().getSimpleName() + "] Resource is not a readable directory");
        }
        servletContextHandler.setBaseResource(resBase);
        if (jettyConfiguration.getSessionSettings().isUseIndividualSettings()==true) {
        	servletContextHandler.setSessionHandler(new AWBSessionHandler(jettyConfiguration.getSessionSettings()));
        }
        
        // --- Create error handler that's redirects to index.html --
        ErrorPageErrorHandler errorHandler = new ErrorPageErrorHandler();
        errorHandler.addErrorPage(404, "/"); // return root ... being index.html
        servletContextHandler.setErrorHandler(errorHandler);
        
        // --- Check to secure via OIDC/OAuth -----------------------
        ServletSecurityConfiguration securitySettiongs = jettyConfiguration.getSecuritySettings().getSecurityConfiguration(JettySecuritySettings.ID_SERVER_SECURITY);
        if (securitySettiongs!=null && securitySettiongs.isSecurityHandlerActivated()==true && securitySettiongs.getSecurityHandlerName().equals(OIDCSecurityHandler.class.getSimpleName())==true) {
        	AwbSecurityHandlerService securityService = SecurityHandlerService.getAwbSecurityHandlerService(securitySettiongs.getSecurityHandlerName());
    		if (securityService!=null) {
    			SecurityHandler securtiyHandler = securityService.getNewSecurityHandler(securitySettiongs.getSecurityHandlerConfiguration());
    			servletContextHandler.setSecurityHandler(securtiyHandler);
    		}
        }
        
		// ----------------------------------------------------------
        // --- Define a ContextHandlerCollection --------------------
		// ----------------------------------------------------------
        List<String> ctxPathList = new ArrayList<>();
        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection(servletContextHandler);
        List<Handler> handlerList = handlerCollection.getHandlers();
        if (handlerList!=null) {
        	for (Handler handler : handlerList) {
        		contextHandlerCollection.addHandler(handler);
        		String ctxPath = HandlerHelper.getContextPath(handler);
        		ctxPath = ctxPath.replace("/", "");
        		ctxPathList.add(ctxPath);
        	}
        }
        
		// ----------------------------------------------------------
        // --- Set handler of the server ----------------------------
		// ----------------------------------------------------------
        server.setHandler(contextHandlerCollection);
        
		return server;
	}
	
	/**
	 * Returns the validated (usable!) web root path.
	 * @return the web root validated
	 */
	private Path getWebRootPathValidated() {
		
		 // --- Get base Path ----------
        Path webRootPath = Path.of(BundleHelper.getWebRootDirectory(true).getAbsolutePath());
        if (Files.isDirectory(webRootPath)==false) {
        	System.err.println("[" + this.getClass().getSimpleName() + "] Path is not a directory: " + webRootPath);
        	return null;
        }
        if (Files.isReadable(webRootPath)==false) {
        	System.err.println("[" + this.getClass().getSimpleName() + "] Path is not readable: " + webRootPath);
        	return null;
        }
        return webRootPath;
	}
	
	/**
	 * Checks if is empty web root path.
	 *
	 * @param webRootPath the web root pat
	 * @return true, if is empty web root path
	 */
	private boolean isEmptyWebRootPath(Path webRootPath) {
		
		boolean isEmpty = true;
		try {
			isEmpty = (Files.list(webRootPath).findAny().isPresent() == false);
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		return isEmpty;
	}
	/**
	 * Checks if is empty configured download URL.
	 *
	 * @param jettyConfiguration the jetty configuration
	 * @return true, if is empty configured download URL
	 */
	private boolean isEmptyConfiguredDownloadURL(JettyConfiguration jettyConfiguration) {
		
		if (jettyConfiguration==null || jettyConfiguration.getWebApplicationSettings()==null) return false;
		
		JettyWebApplicationSettings jwas = jettyConfiguration.getWebApplicationSettings();
		return jwas.getDownloadURL()==null || jwas.getDownloadURL().isBlank(); 
	}
	/**
	 * Install base application.
	 */
	private void installBaseApplication(JettyConfiguration jettyConfiguration) {
		
		// --- If webRoot directory is not empty, do nothing -------- 
		if (this.isEmptyConfiguredDownloadURL(jettyConfiguration)==false) return; 
		
		// --- Adjust JettyConfiguration ----------------------------
		// --- => Web application settings ------
		jettyConfiguration.getWebApplicationSettings().setDownloadURL(AWB_BASE_WEB_APP_DOWNLOAD_URL);
		jettyConfiguration.getWebApplicationSettings().setUpdateStrategy(UpdateStrategy.Automatic);

		// --- => Security settings -------------
		ServletSecurityConfiguration sConfig = jettyConfiguration.getSecuritySettings().getSecurityConfiguration(JettySecuritySettings.ID_SERVER_SECURITY);
		if (sConfig==null) {
			sConfig = new ServletSecurityConfiguration();
			jettyConfiguration.getSecuritySettings().setSecurityConfiguration(JettySecuritySettings.ID_SERVER_SECURITY, sConfig);
		}
		sConfig.setContextPath(JettySecuritySettings.ID_SERVER_SECURITY);
		sConfig.setSecurityHandlerName(JwtSingleUserSecurityHandler.class.getSimpleName());
		sConfig.setSecurityHandlerActivated(true);
		
		// --- => JWT settings ------------------
		TreeMap<String, String> jwtPara = new TreeMap<>();
		jwtPara.put(JwtParameter.JwtIssuer.getKey(), "AWB-Base-WebApp");
		jwtPara.put(JwtParameter.JwtSecrete.getKey(), "-ThIs-Is-A-verY-LoNg-StRiNg-RePrESEnting-A-Default-SecreTe-4-ThE-JwT-ToKen-G@eNerAtiOn-");
		jwtPara.put(JwtParameter.JwtRenew.getKey(), "false");
		jwtPara.put(JwtParameter.JwtValidityPeriod.getKey(), "10");
		jwtPara.put(JwtParameter.JwtVerbose.getKey(), "false");
		jwtPara.put(JwtParameter.UserName.getKey(), "admin");
		jwtPara.put(JwtParameter.Password.getKey(), "admin");
		
		sConfig.setSecurityHandlerConfiguration(jwtPara);
		
		jettyConfiguration.save();
		
		// --- Download from web and extract zip --------------------
		WebApplicationVersion baseWebAppVersion = WebApplicationUpdate.getWebApplicationUpdate(AWB_BASE_WEB_APP_DOWNLOAD_URL);
		if (baseWebAppVersion!=null) {
			new WebApplicationUpdateProcess(baseWebAppVersion, null).start();
		}
	}
	
	/**
	 * Check and install application update.
	 * @param jettyConfiguration the jetty configuration
	 */
	private void checkAndInstallApplicationUpdate(JettyConfiguration jettyConfiguration) {
		
		if (this.isEmptyConfiguredDownloadURL(jettyConfiguration)==true) return;
		
		// --- Check update strategy --------------------------------
		JettyWebApplicationSettings jwas = jettyConfiguration.getWebApplicationSettings();
		if (jwas.getUpdateStrategy()!=null && jwas.getUpdateStrategy()!=UpdateStrategy.Automatic) return;

		// --- Check for an update of the web application -----------
		WebApplicationVersion webAppVersion = WebApplicationUpdate.getWebApplicationUpdate(jwas.getDownloadURL());
		if (webAppVersion!=null) {
			new WebApplicationUpdateProcess(webAppVersion, null).start();
		}
	}
	
	
}
