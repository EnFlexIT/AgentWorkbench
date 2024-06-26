package de.enflexit.awb.ws.core.security;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.TreeMap;

import javax.security.auth.Subject;
import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.DefaultIdentityService;
import org.eclipse.jetty.security.IdentityService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.RoleInfo;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.util.security.Credential;
import org.eclipse.jetty.util.security.Password;

import de.enflexit.awb.ws.core.security.jwt.JwtSingleUserSecurityService.JwtParameter;
import de.enflexit.awb.ws.core.util.ServletHelper;

/**
 * The Class SingleUserSecurityHandler enables to secure an API by a basic authentication
 * with an user name and a password.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class SingleUserSecurityHandler extends ConstraintSecurityHandler {

	private TreeMap<String, String> securityHandlerConfiguration;
	
	/**
	 * Instantiates a new SingleUserSecurityHandler that enables an unprotected access
	 * to the underlying resources (no login and no password is required).
	 */
	public SingleUserSecurityHandler() {
		this(null);
	}
	
	/**
	 * Instantiates a new SingleUserSecurityHandler where access is granted, if the login
	 * information matching the specified login and password.
	 *
	 * @param securityHandlerConfiguration the security handler configuration
	 */
	public SingleUserSecurityHandler(TreeMap<String, String> securityHandlerConfiguration) {
		this.securityHandlerConfiguration = securityHandlerConfiguration;
		this.setAuthenticator(new BasicAuthenticator());
		this.setIdentityService(new DefaultIdentityService());
		this.setLoginService(new SingleUserLoginService());
	}

	/**
	 * Return the current user name.
	 * @return the user name
	 */
	private String getUserName() {
		return securityHandlerConfiguration!=null ? securityHandlerConfiguration.get(JwtParameter.UserName.getKey()) : null;
	}
	/**
	 * Returns the current password.
	 * @return the password
	 */
	private String getPassword() {
		return securityHandlerConfiguration!=null ? securityHandlerConfiguration.get(JwtParameter.Password.getKey()) : null;
	}
	
	/**
	 * Checks if is secured.
	 * @return true, if is secured
	 */
	private boolean isSecured() {
		
		String login = this.getUserName();
		String password = this.getPassword();
		
		boolean isSecuredLogin    = login!=null && login.isBlank()==false;
		boolean isSecuredPassword = password!=null && password.isBlank()==false;
		return isSecuredLogin & isSecuredPassword;
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
	protected boolean isAuthMandatory(Request request, Response base_response, Object constraintInfo) {
		if (ServletHelper.isPreflightRequest(request)==true) return false;
		return this.isSecured();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.SecurityHandler#checkWebResourcePermissions(java.lang.String, org.eclipse.jetty.server.Request, org.eclipse.jetty.server.Response, java.lang.Object, org.eclipse.jetty.server.UserIdentity)
	 */
	@Override
	protected boolean checkWebResourcePermissions(String pathInContext, Request request, Response response, Object constraintInfo, UserIdentity userIdentity) throws IOException {
		return userIdentity!=null && StringUtils.equals(this.getUserName(), userIdentity.getUserPrincipal().getName());
	}
	
	
	// --------------------------------------------------------------
	// --- From her the locally used JwtLoginService ---------
	// --------------------------------------------------------------	
	/**
	 * The Class JwtLoginService.
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	public class SingleUserLoginService implements LoginService {

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
			
			if (StringUtils.equals(SingleUserSecurityHandler.this.getUserName(), username) && StringUtils.equals(SingleUserSecurityHandler.this.getPassword(), String.valueOf(credentials))) {
				
				final Credential credential = new Password(String.valueOf(credentials));
				final Principal principal   = new KnownUser(username, credential);
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

	// --------------------------------------------------------------
	// --- From her the locally used KnownUser ----------------------
	// --------------------------------------------------------------	
	/**
	 * The Class KnownUser.
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	public class KnownUser implements UserPrincipal {

		private final String _name;
		private final Credential _credential;
		
		public KnownUser(String name, Credential credential) {
			_name = name;
			_credential = credential;
		}
		public boolean authenticate(Object credentials) {
			return _credential != null && _credential.check(credentials);
		}
		public boolean isAuthenticated() {
			return true;
		}
		public String toString() {
			return _name;
		}
		@Override
		public String getName() {
			return _name;
		}
	}
	
}
