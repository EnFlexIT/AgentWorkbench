package de.enflexit.awb.ws.core.security.jwt;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jetty.security.SecurityHandler;

import de.enflexit.awb.ws.AwbSecurityHandlerService;
import de.enflexit.awb.ws.core.security.SingleUserSecurityHandler;

/**
 * The Class JwtSingleUserSecurityService describes the {@link SingleUserSecurityHandler}
 * as a service for the local AWB-WS bundle.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JwtSingleUserSecurityService implements AwbSecurityHandlerService {

	private final String[] configParameterKeys = new String[]{"User Name", "User Password"};
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbSecurityHandlerService#getSecurityHandlerName()
	 */
	@Override
	public String getSecurityHandlerName() {
		return JwtSingleUserSecurityHandler.class.getSimpleName();
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

		// --- Set JWT configuration to system environment ----------
		Map<String, String> jwtConfig = new HashMap<>();
		jwtConfig.put(JwtAuthenticator.JWT_SECRET, "Das ist ein Test String Das ist ein Test String Das ist ein Test String ");
		jwtConfig.put(JwtAuthenticator.JWT_ISSUER, "AWB-Webserver");
		jwtConfig.put(JwtAuthenticator.JWT_COOKIE_NAME, "");
		jwtConfig.put(JwtAuthenticator.JWT_VERBOSE, "true");
		
		// --- Return the new instance of the SecurtiyHandler -------
		return new JwtSingleUserSecurityHandler(userName, password, jwtConfig);
	}

}
