package de.enflexit.awb.ws.restapi;

import org.glassfish.jersey.servlet.ServletContainer;

import de.enflexit.awb.ws.core.AbstractJerseyServletWrapper;
import de.enflexit.awb.ws.restapi.gen.Bootstrap;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;


/**
 * The Class AwbRestApiServlet.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JereseyServletContainer extends AbstractJerseyServletWrapper {

	public JereseyServletContainer() {
		super(RestApiConfiguration.APPLICATION_CONTEXT_PATH);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AbstractJerseyServletWrapper#createServletContainer(java.lang.String)
	 */
	@Override
	public ServletContainer createServletContainer(String contextPath) {
		return new ServletContainer(new RestApiConfiguration());
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
//		new Bootstrap().init(config);
		super.init(config);
	}
	
}
