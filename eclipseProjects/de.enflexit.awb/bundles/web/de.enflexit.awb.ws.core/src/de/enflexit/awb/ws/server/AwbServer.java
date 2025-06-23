package de.enflexit.awb.ws.server;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
import de.enflexit.awb.ws.core.ServletSecurityConfiguration;
import de.enflexit.awb.ws.core.model.HandlerHelper;
import de.enflexit.awb.ws.core.security.OIDCSecurityHandler;
import de.enflexit.awb.ws.core.security.SecurityHandlerService;
import de.enflexit.awb.ws.core.session.AWBSessionHandler;

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
        Resource resBase = ResourceFactory.of(servletContextHandler).newResource(webRootPath);
        if (Resources.isReadableDirectory(resBase)==false) {
        	System.err.println("[" + this.getClass().getSimpleName() + "] Resource is not a readable directory");
        }
        servletContextHandler.setBaseResource(resBase);
        servletContextHandler.setSessionHandler(new AWBSessionHandler());
        
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
	
}
