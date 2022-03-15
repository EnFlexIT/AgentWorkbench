package de.enflexit.awb.ws.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;

import agentgui.core.application.Application;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import de.enflexit.awb.ws.AwbWebServerService;
import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettyConfiguration.StartOn;
import de.enflexit.awb.ws.core.JettyCustomizer;

/**
 * The Class DefaultAwbServer.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbServer implements AwbWebServerService, JettyCustomizer {

	public static final String DEFAULT_AWB_SERVER_NAME = "AWB-WebServer";
	public static final String AWB_SERVER_ROOT_PATH = "awbAdmin";
	
	private JettyConfiguration jettyConfiguration;
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbWebServerService#getJettyConfiguration()
	 */
	@Override
	public JettyConfiguration getJettyConfiguration() {
		if (jettyConfiguration==null) {
			jettyConfiguration = new JettyConfiguration(DEFAULT_AWB_SERVER_NAME, this.getStartOn(), null, true);
			jettyConfiguration.setJettyCustomizer(this);
		}
		return jettyConfiguration;
	}
	
	/**
	 * Depending on the {@link ExecutionMode} of AWB, this method returns the argument when the default server has to be started.
	 * @return the StartOn parameter for the DefaultAwbServer 
	 */
	private StartOn getStartOn() {
		
		// TODO Not finalized yet
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
	 * @see de.enflexit.awb.ws.core.JettyCustomizer#customizeConfiguration(org.eclipse.jetty.server.Server, org.eclipse.jetty.server.handler.HandlerCollection)
	 */
	@Override
	public Server customizeConfiguration(Server server, HandlerCollection handlerCollection) {

		// --- Define ResourceHandler for static content ------------
		ResourceHandler resHandler = new ResourceHandler();
        resHandler.setDirectoriesListed(true);
        resHandler.setWelcomeFiles(new String[]{ "index.html" });
        resHandler.setResourceBase(BundleHelper.getWebRootDirectory(true).getAbsolutePath());
        
        // --- Wrap ResourceHandler into ContextHandler -------------
        ContextHandler ctxHandler = new ContextHandler();
        ctxHandler.setHandler(resHandler);
        
        // --- Define a ContextHandlerCollection --------------------
        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection(ctxHandler);
        Handler[] handlerArray = handlerCollection.getHandlers();
        if (handlerArray!=null) {
        	for (int i = 0; i < handlerArray.length; i++) {
        		contextHandlerCollection.addHandler(handlerArray[i]);
        	} 
        }
        server.setHandler(contextHandlerCollection);
		
        
        // --- 
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.setContextPath("/");
        
        ServletHolder jersey = servletContextHandler.addServlet(ServletContainer.class, "/api/*");
        jersey.setInitOrder(1);
        
        jersey.setInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, AwbRestApplication.class.getName());
//        jersey.setInitParameter("jersey.config.server.provider.packages", "de.enflexit.awb.ws.restapi;io.swagger.v3.jaxrs2.integration.resources");

        contextHandlerCollection.addHandler(servletContextHandler);// Setup API resources to be intercepted by Jersey
        
		return server;
	}
	
	
}
