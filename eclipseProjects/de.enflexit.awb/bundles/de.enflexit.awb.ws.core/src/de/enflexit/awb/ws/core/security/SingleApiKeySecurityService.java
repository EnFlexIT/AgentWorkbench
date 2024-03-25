package de.enflexit.awb.ws.core.security;

import java.util.TreeMap;

import org.eclipse.jetty.security.SecurityHandler;

import de.enflexit.awb.ws.AwbSecurityHandlerService;

/**
 * The Class SingleApiKeySecurityService describes the {@link SingleApiKeySecurityHandler}
 * as a service for the local AWB-WS bundle.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class SingleApiKeySecurityService implements AwbSecurityHandlerService {

	private final String[] configParameterKeys = new String[]{"API-Key Name", "API-Key Value"};
	
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
		return this.configParameterKeys;
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
		// --- Get the required parameter ---------------------------
		String apiKeyName  = securityHandlerConfiguration.get(this.configParameterKeys[0]);
		String apiKeyValue = securityHandlerConfiguration.get(this.configParameterKeys[1]);
		// --- Return the new instance of the SecurtiyHandler -------
		return new SingleApiKeySecurityHandler(apiKeyName, apiKeyValue);
	}


}
