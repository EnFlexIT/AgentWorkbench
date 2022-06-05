package de.enflexit.awb.ws.core.security;

import java.util.TreeMap;

import org.eclipse.jetty.security.SecurityHandler;

import de.enflexit.awb.ws.AwbSecurityHandlerService;

/**
 * The Class SingleUserSecurityService describes the {@link SingleUserSecurityHandler}
 * as a service for the local AWB-WS bundle.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class SingleUserSecurityService implements AwbSecurityHandlerService {

	private final String[] configParameterKeys = new String[]{"User Name", "User Password"};
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbSecurityHandlerService#getSecurityHandlerName()
	 */
	@Override
	public String getSecurityHandlerName() {
		return SingleUserSecurityHandler.class.getSimpleName();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbSecurityHandlerService#getConfigurationKeys()
	 */
	@Override
	public String[] getConfigurationKeys() {
		return this.configParameterKeys;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbSecurityHandlerService#getNewSecurityHandler(java.util.TreeMap)
	 */
	@Override
	public SecurityHandler getNewSecurityHandler(TreeMap<String, String> securityHandlerConfiguration) {
		// --- Get the required parameter ---------------------------
		String userName = securityHandlerConfiguration.get(this.configParameterKeys[0]);
		String password = securityHandlerConfiguration.get(this.configParameterKeys[1]);
		// --- Return the new instance of the SecurtiyHandler -------
		return new SingleUserSecurityHandler(userName, password);
	}

}
