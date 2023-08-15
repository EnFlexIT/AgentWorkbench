package de.enflexit.awb.ws.restapi;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import de.enflexit.awb.ws.AwbWebHandlerService;
import de.enflexit.awb.ws.server.AwbServer;
import io.swagger.v3.jaxrs2.integration.OpenApiServlet;

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
			servletContextHandler.setContextPath("/" + RestApiConfiguration.APPLICATION_CONTEXT_PATH + "/");

			ServletHolder jersey = servletContextHandler.addServlet(JereseyServletContainer.class, "/*");
			jersey.setInitParameters(new ServletInitParameter());
			jersey.setInitParameter("jersey.config.server.provider.packages", "de.enflexit.awb.ws.restapi; io.swagger.v3.jaxrs2.integration.resources");
			jersey.setInitParameter("openApi.configuration.resourcePackages", "de.enflexit.awb.ws.restapi");
			jersey.setInitOrder(1);
			
			// Expose API definition independently into yaml/json
			ServletHolder openApi = servletContextHandler.addServlet(OpenApiServlet.class, "/openapi/*");
			openApi.setInitParameter("openApi.configuration.resourcePackages", "de.enflexit.awb.ws.restapi");
			openApi.setInitOrder(2);
			
//			servletContextHandler.addFilter(ApiOriginFilter.class, "/*", EnumSet.of(DispatcherType.INCLUDE, DispatcherType.REQUEST));
			
		}
		return servletContextHandler;
	}

}
