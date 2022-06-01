package de.enflexit.awb.ws.core.security;

import java.io.IOException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.http.HttpClientTransportOverHTTP;
import org.eclipse.jetty.io.ClientConnector;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.DefaultIdentityService;
import org.eclipse.jetty.security.RoleInfo;
import org.eclipse.jetty.security.openid.OpenIdAuthenticator;
import org.eclipse.jetty.security.openid.OpenIdConfiguration;
import org.eclipse.jetty.security.openid.OpenIdLoginService;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * The Class OpenIDSecurityHandler ... TODO
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class OpenIDSecurityHandler extends ConstraintSecurityHandler {

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
	public OpenIDSecurityHandler(String issuer, String clientId, String clientSecret, boolean isTrustAll) {
		
		this.issuer = issuer;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		
		OpenIdConfiguration openIdConfig = new OpenIdConfiguration(this.issuer, null, null, this.clientId, this.clientSecret, this.getHTTPClient(isTrustAll));
		OpenIdLoginService loginService = new OpenIdLoginService(openIdConfig);
		OpenIdAuthenticator authenticator = new OpenIdAuthenticator(openIdConfig, "/error");
		
		this.setLoginService(loginService);
		this.setAuthenticator(authenticator);
//		this.setRealmName("EOMID");
//		this.setIdentityService(loginService.getIdentityService());
		this.setIdentityService(new DefaultIdentityService());
				
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
	
	
	/**
	 * Checks if is secured.
	 * @return true, if is secured
	 */
	private boolean isSecured() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.SecurityHandler#prepareConstraintInfo(java.lang.String, org.eclipse.jetty.server.Request)
	 */
	@Override
	protected RoleInfo prepareConstraintInfo(String pathInContext, Request request) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.SecurityHandler#checkUserDataPermissions(java.lang.String, org.eclipse.jetty.server.Request, org.eclipse.jetty.server.Response, org.eclipse.jetty.security.RoleInfo)
	 */
	@Override
	protected boolean checkUserDataPermissions(String pathInContext, Request request, Response response, RoleInfo constraintInfo) throws IOException {
		return this.isSecured();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.SecurityHandler#isAuthMandatory(org.eclipse.jetty.server.Request, org.eclipse.jetty.server.Response, java.lang.Object)
	 */
	@Override
	protected boolean isAuthMandatory(Request baseRequest, Response base_response, Object constraintInfo) {
		return this.isSecured();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.SecurityHandler#checkWebResourcePermissions(java.lang.String, org.eclipse.jetty.server.Request, org.eclipse.jetty.server.Response, java.lang.Object, org.eclipse.jetty.server.UserIdentity)
	 */
	@Override
	protected boolean checkWebResourcePermissions(String pathInContext, Request request, Response response, Object constraintInfo, UserIdentity userIdentity) throws IOException {
		
		return isSecured();
//		return userIdentity!=null && StringUtils.equals(login, userIdentity.getUserPrincipal().getName());
	}

}
