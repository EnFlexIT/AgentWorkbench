package de.enflexit.awb.ws;

import java.util.TreeMap;

import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.security.SecurityHandler;

import de.enflexit.awb.ws.core.JettyConfiguration;

/**
 * The interface AwbSecurityHandlerService describes the information to be provided 
 * by an implementation to add another security handler to the AWB-WS infrastructure. 
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface AwbSecurityHandlerService {

	/**
	 * Has to return a unique security handler name.
	 * @return the security handler name
	 */
	public String getSecurityHandlerName();
	
	
	/**
	 * Has to return the configuration keys that are to be used in the security handler configuration.
	 * @return the configuration keys
	 */
	public String[] getConfigurationKeys();
	
	/**
	 * Returns the type of the specified key.
	 * @param key the key
	 * @return the key type
	 */
	public Class<?> getKeyType(String key);
	
	
	/**
	 * Has to return a new instance of the SecurityHandlerService based on the specified security handler configuration.
	 *
	 * @param securityHandlerConfiguration the security handler configuration
	 * @return the new security handler
	 */
	public SecurityHandler getNewSecurityHandler(TreeMap<String, String> securityHandlerConfiguration);

	/**
	 * Enables to further customize the currently used ServletContextHandler (e.g. to apply an additional filter).
	 *
	 * @param jConfiguration the current JettyConfiguration
	 * @param serCtxHandle the servlet context handle to customize
	 */
	public void customizeServletContextHandler(JettyConfiguration jConfiguration, ServletContextHandler serCtxHandle);
	
	
}
