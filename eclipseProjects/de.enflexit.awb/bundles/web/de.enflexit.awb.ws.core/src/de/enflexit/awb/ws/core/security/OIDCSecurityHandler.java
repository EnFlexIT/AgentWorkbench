package de.enflexit.awb.ws.core.security;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.transport.HttpClientTransportOverHTTP;
import org.eclipse.jetty.io.ClientConnector;
import org.eclipse.jetty.security.Constraint;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.UserStore;
import org.eclipse.jetty.security.openid.OpenIdAuthenticator;
import org.eclipse.jetty.security.openid.OpenIdConfiguration;
import org.eclipse.jetty.security.openid.OpenIdLoginService;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * The Class OpenIDSecurityHandler ... TODO
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class OIDCSecurityHandler extends SecurityHandler.PathMapped {

	private final String issuer;
	private final String clientId;
	private final String clientSecret;
	
	/**
	 * Instantiates a new OpenIDSecurityHandler where access is granted, if the specified OpenID issuer provides access.
	 *
	 * @param issuer the issuer
	 * @param clientId the client id
	 * @param clientSecret the client secret
	 * @param isTrustAllSSL the is trust all SSL
	 */
	public OIDCSecurityHandler(String issuer, String clientId, String clientSecret, boolean isTrustAll) {
		
		this.issuer = issuer;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		
		OpenIdConfiguration openIdConfig = new OpenIdConfiguration(this.issuer, this.clientId, this.clientSecret);
		this.put("/auth/*", Constraint.ANY_USER);

		// A nested LoginService is optional and used to specify known users with defined roles.
		// This can be any instance of LoginService and is not restricted to be a HashLoginService.
		HashLoginService nestedLoginService = new HashLoginService();
		UserStore userStore = new UserStore();
		userStore.addUser("<admin-user-subject-identifier>", null, new String[]{"admin"});
		nestedLoginService.setUserStore(userStore);

		// Optional configuration to allow new users not listed in the nested LoginService to be authenticated.
		openIdConfig.setAuthenticateNewUsers(true);

		// An OpenIdLoginService should be used which can optionally wrap the nestedLoginService to support roles.
		LoginService loginService = new OpenIdLoginService(openIdConfig, nestedLoginService);
		this.setLoginService(loginService);

		// Configure an OpenIdAuthenticator.
		this.setAuthenticator(new OpenIdAuthenticator(openIdConfig,
		    "/j_security_check", // The path where the OIDC provider redirects back to Jetty.
		    "/error", // Optional page where authentication errors are redirected.
		    "/logoutRedirect" // Optional page where the user is redirected to this page after logout.
		));

//		server.insertHandler(new SessionHandler());
		
	}

	/**
	 * Returns the HTTP client to be used for the OpenID connection.
	 *
	 * @param isTrustAll the is trust all
	 * @return the HTTP client
	 */
	private HttpClient getHTTPClient(boolean isTrustAll) {
		ClientConnector connector = new ClientConnector();
		connector.setSslContextFactory(new SslContextFactory.Client(isTrustAll));
		return new HttpClient(new HttpClientTransportOverHTTP(connector));
	}

}
