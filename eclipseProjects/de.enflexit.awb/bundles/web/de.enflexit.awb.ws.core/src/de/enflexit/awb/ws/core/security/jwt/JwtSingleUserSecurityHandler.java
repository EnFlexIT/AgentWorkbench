package de.enflexit.awb.ws.core.security.jwt;

import java.security.Principal;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import javax.security.auth.Subject;

import org.apache.commons.lang3.Strings;
import org.eclipse.jetty.security.DefaultIdentityService;
import org.eclipse.jetty.security.IdentityService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.UserIdentity;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Session;
import org.eclipse.jetty.util.security.Credential;
import org.eclipse.jetty.util.security.Password;

import de.enflexit.awb.ws.core.security.SecurityHandlerService;
import de.enflexit.awb.ws.core.security.jwt.JwtSingleUserSecurityService.JwtParameter;


/**
 * The Class JwtSingleUserSecurityHandler.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JwtSingleUserSecurityHandler extends SecurityHandler.PathMapped {
	
	private TreeMap<String, String> securityHandlerConfiguration;
	
	/**
	 * Instantiates a new SingleUserSecurityHandler that enables an unprotected access
	 * to the underlying resources (no login and no password is required).
	 */
	public JwtSingleUserSecurityHandler() {
		this(null, null);
	}
	
	/**
	 * Instantiates a new SingleUserSecurityHandler where access is granted, if the login
	 * information matching the specified login and password.
	 *
	 * @param securityHandlerConfiguration the TreeMap of the AWB security handler configuration
	 * @param jwtConfig the special JWT configuration parameter
	 */
	public JwtSingleUserSecurityHandler(TreeMap<String, String> securityHandlerConfiguration, Map<String, String> jwtConfig) {
		this.securityHandlerConfiguration = securityHandlerConfiguration;
		if (jwtConfig!=null) {
			this.setAuthenticator(new JwtAuthenticator(jwtConfig));
		}
		this.setIdentityService(new DefaultIdentityService());
		this.setLoginService(new JwtLoginService());
		
		SecurityHandlerService.putPathsToSecurityHandler(this, this.isSecured());
	}
	
	/**
	 * Checks if is secured.
	 * @return true, if is secured
	 */
	private boolean isSecured() {
		boolean isSecuredLogin    = this.getJwtUserName()!=null && this.getJwtUserName().isBlank()==false;
		boolean isSecuredPassword = this.getJwtPassword()!=null && this.getJwtPassword().isBlank()==false;
		return isSecuredLogin & isSecuredPassword;
	}

	/**
	 * Returns the JWT user name.
	 * @return the JWT user name
	 */
	private String getJwtUserName() {
		return this.securityHandlerConfiguration.get(JwtParameter.UserName.getKey());
	}
	/**
	 * Returns the JWT password.
	 * @return the JWT password
	 */
	private String getJwtPassword() {
		return this.securityHandlerConfiguration.get(JwtParameter.Password.getKey());
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
		
			if (Strings.CS.equals(JwtSingleUserSecurityHandler.this.getJwtUserName(), username) && Strings.CS.equals(JwtSingleUserSecurityHandler.this.getJwtPassword(), String.valueOf(credentials))) {
				
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
			Principal p = user.getUserPrincipal();
			if (p instanceof JwtPrincipal) {
				JwtPrincipal jwtP = (JwtPrincipal) p;
				JwtSessionStore.getInstance().removeAuthentication(jwtP.getJwtToken());
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
