package de.enflexit.awb.ws;

import java.util.TreeMap;

import org.eclipse.jetty.security.SecurityHandler;

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
	 * Has to return a new instance of the SecurityHandlerService based on the specified security handler configuration.
	 *
	 * @param securityHandlerConfiguration the security handler configuration
	 * @return the new security handler
	 */
	public SecurityHandler getNewSecurityHandler(TreeMap<String, String> securityHandlerConfiguration);
	
}
