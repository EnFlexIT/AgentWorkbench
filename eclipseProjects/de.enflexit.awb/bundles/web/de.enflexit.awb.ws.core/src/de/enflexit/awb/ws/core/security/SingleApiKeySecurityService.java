package de.enflexit.awb.ws.core.security;

import java.util.TreeMap;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.security.SecurityHandler;

import de.enflexit.awb.ws.AwbSecurityHandlerService;
import de.enflexit.awb.ws.core.JettyConfiguration;

/**
 * The Class SingleApiKeySecurityService describes the {@link SingleApiKeySecurityHandler}
 * as a service for the local AWB-WS bundle.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class SingleApiKeySecurityService implements AwbSecurityHandlerService {

	final static String[] configParameterKeys = new String[]{"API-Key Name", "API-Key Value"};
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbSecurityHandlerService#getSecurityHandlerName()
	 */
	@Override
	public String getSecurityHandlerName() {
		return SingleApiKeySecurityHandler.class.getSimpleName();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbSecurityHandlerService#getConfigurationKeys()
	 */
	@Override
	public String[] getConfigurationKeys() {
		return configParameterKeys;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbSecurityHandlerService#getKeyType(java.lang.String)
	 */
	@Override
	public Class<?> getKeyType(String key) {
		return String.class;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbSecurityHandlerService#getNewSecurityHandler(java.util.TreeMap)
	 */
	@Override
	public SecurityHandler getNewSecurityHandler(TreeMap<String, String> securityHandlerConfiguration) {
		return new SingleApiKeySecurityHandler(securityHandlerConfiguration);
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbSecurityHandlerService#customizeServletContextHandler(de.enflexit.awb.ws.core.JettyConfiguration, org.eclipse.jetty.ee10.servlet.ServletContextHandler)
	 */
	@Override
	public void customizeServletContextHandler(JettyConfiguration jConfiguration, ServletContextHandler serCtxHandle) {
		// --- Nothing to do here --------------- 
	}

}
