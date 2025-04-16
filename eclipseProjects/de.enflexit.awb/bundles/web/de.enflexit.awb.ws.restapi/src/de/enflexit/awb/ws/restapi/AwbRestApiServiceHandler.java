package de.enflexit.awb.ws.restapi;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;

import de.enflexit.awb.ws.AwbWebHandlerService;
import de.enflexit.awb.ws.server.AwbServer;

/**
 * The Class AwbRestApiServiceHandler defines the AWB Rest API for the {@link AwbServer}.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbRestApiServiceHandler implements AwbWebHandlerService {

	private ServletContextHandler servletContextHandler;
	
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
		}
		return servletContextHandler;
	}

}
