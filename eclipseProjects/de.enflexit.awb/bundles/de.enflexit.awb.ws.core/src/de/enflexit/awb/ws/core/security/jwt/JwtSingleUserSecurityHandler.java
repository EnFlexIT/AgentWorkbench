package de.enflexit.awb.ws.core.security.jwt;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

import javax.security.auth.Subject;
import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.DefaultIdentityService;
import org.eclipse.jetty.security.IdentityService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.RoleInfo;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.util.security.Credential;
import org.eclipse.jetty.util.security.Password;

import de.enflexit.awb.ws.core.util.ServletHelper;


/**
 * The Class JwtSingleUserSecurityHandler.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JwtSingleUserSecurityHandler extends ConstraintSecurityHandler {
	
	private final String login;
	private final String password;
	
	/**
	 * Instantiates a new SingleUserSecurityHandler that enables an unprotected access
	 * to the underlying resources (no login and no password is required).
	 */
	public JwtSingleUserSecurityHandler() {
		this(null, null, null);
	}
	/**
	 * Instantiates a new SingleUserSecurityHandler where access is granted, if the login
	 * information matching the specified login and password.
	 *
	 * @param login the login
	 * @param password the password
	 * @param jwtConfig the JWT configuration parameter
	 */
	public JwtSingleUserSecurityHandler(String login, String password, Map<String, String> jwtConfig) {
		this.login = login;
		this.password = password;
		
		if (jwtConfig!=null) this.setAuthenticator(new JwtAuthenticator(jwtConfig));
		this.setIdentityService(new DefaultIdentityService());
		this.setLoginService(new JwtLoginService()); // TODO
	}
	
	/**
	 * Checks if is secured.
	 * @return true, if is secured
	 */
	private boolean isSecuredByPassword() {
		boolean isSecuredLogin    = this.login!=null && this.login.isBlank()==false;
		boolean isSecuredPassword = this.password!=null && this.password.isBlank()==false;
		return isSecuredLogin & isSecuredPassword;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.SecurityHandler#checkUserDataPermissions(java.lang.String, org.eclipse.jetty.server.Request, org.eclipse.jetty.server.Response, org.eclipse.jetty.security.RoleInfo)
	 */
	@Override
	protected boolean checkUserDataPermissions(String pathInContext, Request request, Response response, RoleInfo roleInfo) throws IOException {
		return this.isSecuredByPassword();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.SecurityHandler#isAuthMandatory(org.eclipse.jetty.server.Request, org.eclipse.jetty.server.Response, java.lang.Object)
	 */
	@Override
	protected boolean isAuthMandatory(Request baseRequest, Response base_response, Object constraintInfo) {
		if (ServletHelper.isPreflightRequest(baseRequest)==true) return false;
		return this.isSecuredByPassword();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.SecurityHandler#checkWebResourcePermissions(java.lang.String, org.eclipse.jetty.server.Request, org.eclipse.jetty.server.Response, java.lang.Object, org.eclipse.jetty.server.UserIdentity)
	 */
	@Override
	protected boolean checkWebResourcePermissions(String pathInContext, Request request, Response response, Object constraintInfo, UserIdentity userIdentity) throws IOException {
		return userIdentity!=null && StringUtils.equals(login, userIdentity.getUserPrincipal().getName());
	}
	

	
	// --------------------------------------------------------------
	// --- From here, the locally used JwtLoginService -------
	// --------------------------------------------------------------	
	/**
	 * The Class JwtLoginService.
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	public class JwtLoginService implements LoginService {

		private IdentityService identityService;
		
		/* (non-Javadoc)
		 * @see org.eclipse.jetty.security.LoginService#getName()
		 */
		@Override
		public String getName() {
			return this.getClass().getSimpleName();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jetty.security.LoginService#login(java.lang.String, java.lang.Object, javax.servlet.ServletRequest)
		 */
		@Override
		public UserIdentity login(String username, Object credentials, ServletRequest request) {
			
			if (StringUtils.equals(JwtSingleUserSecurityHandler.this.login, username) && StringUtils.equals(JwtSingleUserSecurityHandler.this.password, String.valueOf(credentials))) {
				
				final Credential credential = new Password(String.valueOf(credentials));
				final Principal principal   = new JwtPrincipal(username);
				final Subject subject = new Subject();
				subject.getPrincipals().add(principal);
				subject.getPrivateCredentials().add(credential);
				
				return new UserIdentity() {

					@Override
					public Subject getSubject() {
						return subject;
					}

					@Override
					public Principal getUserPrincipal() {
						return principal;
					}

					@Override
					public boolean isUserInRole(String role, Scope scope) {
						return true;
					}
				};
				
			} else {
				return null;
			}
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jetty.security.LoginService#logout(org.eclipse.jetty.server.UserIdentity)
		 */
		@Override
		public void logout(UserIdentity user) {
			
			// TODO
//			this.authenticator.clearCachedAuthentication(jwtToken);
			
		}
		/* (non-Javadoc)
		 * @see org.eclipse.jetty.security.LoginService#validate(org.eclipse.jetty.server.UserIdentity)
		 */
		@Override
		public boolean validate(UserIdentity user) {
			return false;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jetty.security.LoginService#getIdentityService()
		 */
		@Override
		public IdentityService getIdentityService() {
			return identityService;
		}
		/* (non-Javadoc)
		 * @see org.eclipse.jetty.security.LoginService#setIdentityService(org.eclipse.jetty.security.IdentityService)
		 */
		@Override
		public void setIdentityService(IdentityService service) {
			this.identityService = service;
		}
		
	}
	
}
