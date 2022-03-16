package de.enflexit.awb.ws.restapi;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;

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
		return null; // null equal 
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbWebHandlerService#getHandler()
	 */
	@Override
	public Handler getHandler() {
		if (servletContextHandler==null) {
			servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
			servletContextHandler.setContextPath("/");
			ServletHolder jersey = servletContextHandler.addServlet(ServletContainer.class, "/api/*");
			jersey.setInitOrder(1);
			jersey.setInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, AwbRestApplication.class.getName());
//			jersey.setInitParameter("jersey.config.server.provider.packages", "de.enflexit.awb.ws.restapi;io.swagger.v3.jaxrs2.integration.resources");
		}
		return servletContextHandler;
	}

}
