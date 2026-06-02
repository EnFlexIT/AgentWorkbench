package de.enflexit.awb.ws.core.security;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.openid.OpenIdAuthenticator;
import org.eclipse.jetty.security.openid.OpenIdConfiguration;
import org.eclipse.jetty.security.openid.OpenIdConfiguration.Builder;

import de.enflexit.awb.ws.AwbSecurityHandlerService;
import jakarta.servlet.DispatcherType;

/**
 * The Class OpenIDSecurityService.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class OIDCSecurityService implements AwbSecurityHandlerService {

	public enum OIDCParameter {
		Issuer("Issuer", String.class),
		ClientID("ClientID", String.class),
		ClientSecrete("ClientSecrete", String.class)
		;
		
		private String displayText;
		private Class<?> keyType;
		
		private OIDCParameter(String displayText, Class<?> keyType) {
			this.displayText = displayText;
			this.keyType = keyType;
		}
		public String getKey() {
			return displayText;
		}
		public Class<?> getKeyType() {
			return keyType;
		}
		
		public static OIDCParameter fromKey(String key) {
			for (OIDCParameter oidcPara : OIDCParameter.values()) {
				if (oidcPara.getKey().equals(key)==true) {
					return oidcPara;
				}
			}
			return null;
		}
	}
	
	private static boolean useOneAuthenticator = false;
	private static OpenIdAuthenticator openIdAuthenticator;
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbSecurityHandlerService#getSecurityHandlerName()
	 */
	@Override
	public String getSecurityHandlerName() {
		return OIDCSecurityHandler.class.getSimpleName();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbSecurityHandlerService#getConfigurationKeys()
	 */
	@Override
	public String[] getConfigurationKeys() {
		List<String> configList = new ArrayList<>();
		for (OIDCParameter oidcParameter : OIDCParameter.values()) {
			configList.add(oidcParameter.getKey());
		}
		return configList.toArray(new String[configList.size()]);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbSecurityHandlerService#getKeyType(java.lang.String)
	 */
	@Override
	public Class<?> getKeyType(String key) {
		OIDCParameter oidcParameter = OIDCParameter.fromKey(key);
		if (oidcParameter!=null) {
			return oidcParameter.getKeyType();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbSecurityHandlerService#getNewSecurityHandler(java.util.TreeMap)
	 */
	@Override
	public SecurityHandler getNewSecurityHandler(TreeMap<String, String> securityHandlerConfiguration) {
		
		// --- Get the required parameter ---------------------------
		String issuer = securityHandlerConfiguration.get(OIDCParameter.Issuer.getKey());
		String clientID = securityHandlerConfiguration.get(OIDCParameter.ClientID.getKey());
		String clientSecret = securityHandlerConfiguration.get(OIDCParameter.ClientSecrete.getKey());
		
		if (issuer==null || issuer.isBlank()) return null;
		if (clientID==null || clientID.isBlank()) return null;
		if (clientSecret==null || clientSecret.isBlank()) return null;

		if (useOneAuthenticator==true) {
			// --- Only used in the context of development tests ----
			Builder oidcBuilder = new Builder(issuer, clientID, clientSecret);
			oidcBuilder.authenticateNewUsers(true);
			oidcBuilder.logoutWhenIdTokenIsExpired(true);
			
			OpenIdConfiguration openIdConfig = oidcBuilder.build();
			
			return new OIDCSecurityHandler(openIdConfig, OIDCSecurityService.getOpenIdAuthenticator(openIdConfig));
		}
		// --- The regular way to create an OIDCSecurityHandler -----
		return new OIDCSecurityHandler(issuer, clientID, clientSecret, true);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbSecurityHandlerService#customizeServletContextHandler(java.util.TreeMap, org.eclipse.jetty.ee10.servlet.ServletContextHandler)
	 */
	@Override
	public void customizeServletContextHandler(TreeMap<String, String> securityHandlerConfiguration, ServletContextHandler serCtxHandle) {
		
		// --- Get the required parameter ---------------------------
		String issuer = securityHandlerConfiguration.get(OIDCParameter.Issuer.getKey());
		String clientID = securityHandlerConfiguration.get(OIDCParameter.ClientID.getKey());
		String clientSecret = securityHandlerConfiguration.get(OIDCParameter.ClientSecrete.getKey());
		
		// --- As example:  https://your-idp.example.com/realms/myrealm/protocol/openid-connect/token
		String tokenEndPoint = issuer + "/protocol/openid-connect/token";
		
		FilterHolder refreshFilter = new FilterHolder(OIDCTokenRefreshFilter.class);
		refreshFilter.setInitParameter("tokenEndpoint", tokenEndPoint); 
		refreshFilter.setInitParameter("clientId",     clientID);
		refreshFilter.setInitParameter("clientSecret", clientSecret);

		// --- Apply to secured paths -------------------------------
		serCtxHandle.addFilter(refreshFilter, "/*", EnumSet.of(DispatcherType.REQUEST));
	}

	
	
	/**
	 * Resets the  OpenIdAuthenticator.
	 */
	public static void resetOpenIdAuthenticator() {
		OIDCSecurityService.setOpenIdAuthenticator(null);
	}
	/**
	 * Returns the current OpenIdAuthenticator.
	 *
	 * @param openIdConfig the open id config
	 * @return the open id authenticator
	 */
	private static OpenIdAuthenticator getOpenIdAuthenticator(OpenIdConfiguration openIdConfig) {
		if (openIdAuthenticator==null) {
			openIdAuthenticator = new OpenIdAuthenticator(openIdConfig, "/j_security_check", "/error", "/logoutRedirect");
		}
		return openIdAuthenticator;
	}
	/**
	 * Sets the open id authenticator.
	 * @param openIdAuthenticator the new open id authenticator
	 */
	private static void setOpenIdAuthenticator(OpenIdAuthenticator openIdAuthenticator) {
		OIDCSecurityService.openIdAuthenticator = openIdAuthenticator;
	}
	
}
