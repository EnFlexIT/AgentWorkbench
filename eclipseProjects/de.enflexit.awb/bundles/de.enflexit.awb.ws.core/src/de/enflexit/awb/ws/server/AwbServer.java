package de.enflexit.awb.ws.server;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Handler.Sequence;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.resource.Resources;

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

	public static final String NAME = "AWB-WebServer";
	public static final String AWB_SERVER_ROOT_PATH = "awbAdmin";
	
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
	 * @see de.enflexit.awb.ws.core.JettyCustomizer#customizeConfiguration(org.eclipse.jetty.server.Server, org.eclipse.jetty.server.handler.HandlerCollection)
	 */
	@Override
	public Server customizeConfiguration(Server server, Sequence handlerCollection) {

		// --- Define ResourceHandler for static content ------------
		ResourceHandler resHandler = new ResourceHandler();
        resHandler.setDirAllowed(true);
        resHandler.setWelcomeFiles(new String[]{ "index.html" });
        
        // --- Get base Path ----------
        Path pathResBase = Path.of(BundleHelper.getWebRootDirectory(true).getAbsolutePath());
        if (Files.isDirectory(pathResBase)==false) {
        	System.err.println("[" + this.getClass().getSimpleName() + "] Path is not a directory: " + pathResBase);
        }
        if (Files.isReadable(pathResBase)==false) {
        	System.err.println("[" + this.getClass().getSimpleName() + "] Path is not readable: " + pathResBase);
        }
        // --- Create Resource --------
        Resource resBase = ResourceFactory.of(resHandler).newResource(pathResBase);
        if (Resources.isReadableDirectory(resBase)==false) {
        	System.err.println("[" + this.getClass().getSimpleName() + "] Resource is not a readable directory");
        }
        resHandler.setBaseResource(resBase);

        
        // --- Wrap ResourceHandler into ContextHandler -------------
        ContextHandler ctxHandler = new ContextHandler();
        ctxHandler.setHandler(resHandler);
        
        // --- Define a ContextHandlerCollection --------------------
        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection(ctxHandler);
        List<Handler> handlerList = handlerCollection.getHandlers();
        if (handlerList!=null) {
        	for (int i = 0; i < handlerList.size(); i++) {
        		contextHandlerCollection.addHandler(handlerList.get(i));
        	} 
        }
        server.setHandler(contextHandlerCollection);
        
		return server;
	}
	
	
}
