package de.enflexit.awb.ws;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.glassfish.jersey.servlet.ServletContainer;

/**
 * The Class AbstractJerseyServletWrapper.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class AbstractJerseyServletWrapper implements Servlet {

	private String contextPath;
	private ServletContainer jerseyContainer;
	
	/**
	 * Instantiates a new abstract jersey servlet wrapper.
	 * @param contextPath the context path
	 */
	public AbstractJerseyServletWrapper(String contextPath) {
		this.contextPath = contextPath;
	}
	
	/**
	 * Has to create the Jersey ServletContainer.
	 *
	 * @param contextPath the context path
	 * @return the ServletContainer
	 */
	public abstract ServletContainer createServletContainer(String contextPath);
	
	/**
	 * Returns or calls to create the Jersey ServletContainer.
	 * @return the ServletContainer
	 */
	private ServletContainer getServletContainer() {
		if (jerseyContainer==null) {
			jerseyContainer = this.createServletContainer(this.contextPath);
		}
		return jerseyContainer;
	}
	
	
	/* (non-Javadoc)
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		this.getServletContainer().init(config);
		
	}
	/* (non-Javadoc)
	 * @see javax.servlet.Servlet#getServletConfig()
	 */
	@Override
	public ServletConfig getServletConfig() {
		return this.getServletContainer().getServletConfig();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Servlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		this.getServletContainer().service(req, res);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Servlet#getServletInfo()
	 */
	@Override
	public String getServletInfo() {
		return this.getServletContainer().getServletInfo();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Servlet#destroy()
	 */
	@Override
	public void destroy() {
		this.getServletContainer().destroy();
	}

}
