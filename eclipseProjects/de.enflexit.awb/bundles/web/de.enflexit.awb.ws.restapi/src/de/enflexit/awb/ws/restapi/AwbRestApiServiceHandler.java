package de.enflexit.awb.ws.restapi;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.ee10.websocket.jakarta.server.config.JakartaWebSocketServletContainerInitializer;
import de.enflexit.awb.ws.AwbWebHandlerService;
import de.enflexit.awb.ws.core.websocket.LogTransportServiceWebSocket;
import de.enflexit.awb.ws.core.websocket.LogWebSocket;
import de.enflexit.awb.ws.server.AwbServer;
import de.enflexit.logging.LogTransportServiceRegistry;

/**
 * The Class AwbRestApiServiceHandler defines the AWB Rest API for the {@link AwbServer}.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbRestApiServiceHandler implements AwbWebHandlerService {

	private ServletContextHandler servletContextHandler;
	private LogTransportServiceWebSocket logTransportServiceWebSocket;
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbWebHandlerService#getServerName()
	 */
	@Override
	public String getServerName() {
		return null; // null is equals to default AWB server 
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbWebHandlerService#getHandler()
	 */
	@Override
	public Handler getHandler() {
		if (servletContextHandler==null) {
			servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
			servletContextHandler.setContextPath("/" + RestApiConfiguration.APPLICATION_CONTEXT_PATH);
			
			ServletHolder jersey = servletContextHandler.addServlet(JereseyServletContainer.class, "/*");
			jersey.setInitParameters(new ServletInitParameter());
			jersey.setInitOrder(1);

			// --- Initialize the web socket ----------------------------------
			JakartaWebSocketServletContainerInitializer.configure(servletContextHandler,
					(ctx, container) -> {
						container.addEndpoint(LogWebSocket.class);
					});
			// --- Register a transport service for the web socket ------------
			if (logTransportServiceWebSocket == null) {
				logTransportServiceWebSocket = new LogTransportServiceWebSocket();
				LogTransportServiceRegistry.registerTransportService(logTransportServiceWebSocket);
			}
			
		}
		return servletContextHandler;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbWebHandlerService#disposeHandler()
	 */
	@Override
	public void disposeHandler() {
		if (this.servletContextHandler!=null) {
			this.servletContextHandler.destroy();
		}
		// --- Unregister the web socket transport service --------------------
		if  (this.logTransportServiceWebSocket != null) {
			LogTransportServiceRegistry.unregisterTransportService(logTransportServiceWebSocket);
			this.logTransportServiceWebSocket = null;
		}
		this.servletContextHandler = null;
	}
	
}