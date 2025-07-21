package de.enflexit.awb.ws.core.security;

import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.UserStore;
import org.eclipse.jetty.security.openid.OpenIdAuthenticator;
import org.eclipse.jetty.security.openid.OpenIdConfiguration;
import org.eclipse.jetty.security.openid.OpenIdLoginService;

/**
 * The Class OIDCSecurityHandler
 * 
 * Jetty sample can be found under: https://jetty.org/docs/jetty/12/programming-guide/security/openid-support.html
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class OIDCSecurityHandler extends SecurityHandler.PathMapped {

	
	/**
	 * Instantiates a new OIDC security handler.
	 *
	 * @param openIdConfig the open id config
	 * @param openIdAuth the open id auth
	 */
	public OIDCSecurityHandler(OpenIdConfiguration openIdConfig, OpenIdAuthenticator openIdAuth) {
		
		// --- Optional configuration to allow new users not listed in the nested LoginService to be authenticated. -------------
		openIdConfig.setAuthenticateNewUsers(true);
		openIdConfig.setLogoutWhenIdTokenIsExpired(true);
		
		// --- An OpenIdLoginService should be used which can optionally wrap the nestedLoginService to support roles. ----------
		this.setLoginService(new OpenIdLoginService(openIdConfig, this.createNestedLoginService()));

		// --- Add the specified OpenIdAuthenticator ----------------------------------------------------------------------------
		this.setAuthenticator(openIdAuth);
				
		// --- Use central method to secure the individual paths ----------------------------------------------------------------
		SecurityHandlerService.putPathsToSecurityHandler(this, true);
	}
	
	/**
	 * Instantiates a new OpenIDSecurityHandler where access is granted, if the specified OpenID issuer provides access.
	 *
	 * @param issuer the issuer
	 * @param clientId the client id
	 * @param clientSecret the client secret
	 * @param isTrustAllSSL the is trust all SSL
	 */
	public OIDCSecurityHandler(String issuer, String clientId, String clientSecret, boolean isTrustAll) {
		
		// --- Optional configuration to allow new users not listed in the nested LoginService to be authenticated. -------------
		OpenIdConfiguration openIdConfig = new OpenIdConfiguration(issuer, clientId, clientSecret);
		openIdConfig.setAuthenticateNewUsers(true);
		openIdConfig.setLogoutWhenIdTokenIsExpired(true);
		
		// --- An OpenIdLoginService should be used which can optionally wrap the nestedLoginService to support roles. ----------
		this.setLoginService(new OpenIdLoginService(openIdConfig, this.createNestedLoginService()));

		this.setAuthenticator(new OpenIdAuthenticator(openIdConfig,
		    "/j_security_check", 	// --- The path where the OIDC provider redirects back to Jetty.
		    "/error", 				// --- Optional page where authentication errors are redirected.
		    "/logoutRedirect" 		// --- Optional page where the user is redirected to this page after logout.
		));
		
		// --- Use central method to secure the individual paths ----------------------------------------------------------------
		SecurityHandlerService.putPathsToSecurityHandler(this, true);
	}
	
	
	/**
	 * Creates a nested login service.
	 * @return the nested login service
	 */
	private HashLoginService createNestedLoginService() {
		
		// --- A nested LoginService is optional and used to specify known users with defined roles. ----------------------------
		// --- This can be any instance of LoginService and is not restricted to be a HashLoginService. -------------------------
		UserStore userStore = new UserStore();
		userStore.addUser("<awb-admin-user-subject-identifier>", null, new String[]{"admin"});

		HashLoginService nestedLoginService = new HashLoginService();
		nestedLoginService.setUserStore(userStore);
		
		return nestedLoginService;
	}
	
}
