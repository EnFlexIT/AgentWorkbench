package de.enflexit.awb.ws.core.security;

import java.util.TreeMap;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.security.Constraint;
import org.eclipse.jetty.security.IdentityService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.UserIdentity;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Session;

import de.enflexit.awb.ws.core.security.jwt.JwtSingleUserSecurityService.JwtParameter;


/**
 * The Class SingleUserSecurityHandler enables to secure an API by a basic authentication
 * with an user name and a password.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class SingleUserSecurityHandler extends SecurityHandler.PathMapped {

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
		this.setLoginService(new SingleUserLoginService());
        
		if (this.isSecured()==true) {
			this.put("/*", Constraint.ANY_USER);
		} else {
			this.put("/*", Constraint.ALLOWED);
		}
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
	
	
	// --------------------------------------------------------------
	// --- From her the locally used SingleUserLoginService ---------
	// --------------------------------------------------------------	
	/**
	 * The Class SingleUserLoginService.
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
		 * @see org.eclipse.jetty.security.LoginService#login(java.lang.String, java.lang.Object, org.eclipse.jetty.server.Request, java.util.function.Function)
		 */
		@Override
		public UserIdentity login(String username, Object credentials, Request request, Function<Boolean, Session> getOrCreateSession) {
			
			if (StringUtils.equals(SingleUserSecurityHandler.this.getUserName(), username) && StringUtils.equals(SingleUserSecurityHandler.this.getPassword(), String.valueOf(credentials))) {
				return new AwbUserIdentity(username, credentials, true);
			}
			return null;
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
	
}
