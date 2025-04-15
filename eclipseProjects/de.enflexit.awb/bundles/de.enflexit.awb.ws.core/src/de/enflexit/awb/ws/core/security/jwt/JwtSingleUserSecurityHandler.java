package de.enflexit.awb.ws.core.security.jwt;

import java.util.Map;
import java.util.function.Function;

import javax.security.auth.Subject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.security.Authenticator;
import org.eclipse.jetty.security.Constraint;
import org.eclipse.jetty.security.DefaultIdentityService;
import org.eclipse.jetty.security.IdentityService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.UserIdentity;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Session;
import org.eclipse.jetty.util.security.Credential;
import org.eclipse.jetty.util.security.Password;

/**
 * The Class JwtSingleUserSecurityHandler.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JwtSingleUserSecurityHandler extends SecurityHandler.PathMapped {
	
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
		
		if (jwtConfig!=null) {
			this.setAuthenticator(new JwtAuthenticator(jwtConfig));
		}
		this.setIdentityService(new DefaultIdentityService());
		this.setLoginService(new JwtLoginService());
		
		if (this.isSecured()==true) {
			this.put("/*", Constraint.ANY_USER);
		} else {
			this.put("/*", Constraint.ALLOWED);
		}
		
	}
	
	/**
	 * Checks if is secured.
	 * @return true, if is secured
	 */
	private boolean isSecured() {
		boolean isSecuredLogin    = this.login!=null && this.login.isBlank()==false;
		boolean isSecuredPassword = this.password!=null && this.password.isBlank()==false;
		return isSecuredLogin & isSecuredPassword;
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

		@Override
		public UserIdentity login(String username, Object credentials, Request request, Function<Boolean, Session> getOrCreateSession) {
		
			if (StringUtils.equals(JwtSingleUserSecurityHandler.this.login, username) && StringUtils.equals(JwtSingleUserSecurityHandler.this.password, String.valueOf(credentials))) {
				
				final Credential credential = new Password(String.valueOf(credentials));
				final JwtPrincipal principal   = new JwtPrincipal(username);
				final Subject subject = new Subject();
				subject.getPrincipals().add(principal);
				subject.getPrivateCredentials().add(credential);
				
				return new JwtUserIdentity(principal);
				
			} else {
				return null;
			}
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jetty.security.LoginService#logout(org.eclipse.jetty.server.UserIdentity)
		 */
		@Override
		public void logout(UserIdentity user) {
			Authenticator auth = JwtSingleUserSecurityHandler.this.getAuthenticator();
			if (auth instanceof JwtAuthenticator) {
				JwtAuthenticator jwtAuthenticator = (JwtAuthenticator) auth;
				//TODO
				
			}
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
