package de.enflexit.awb.ws.core.security;

import java.security.Principal;

import javax.security.auth.Subject;
import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.security.IdentityService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.server.UserIdentity;
import org.eclipse.jetty.util.security.Credential;
import org.eclipse.jetty.util.security.Password;


/**
 * The Class SingleUserLoginService implements a {@link LoginService} that is
 * specified be the arguments of this class. 
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class SingleUserLoginService implements LoginService {

	private final String login;
	private final String password;

	private IdentityService identityService;
	
	/**
	 * Instantiates a new single user login service.
	 *
	 * @param login the login
	 * @param password the password
	 */
	public SingleUserLoginService(final String login, final String password) {
		this.login = login;
		this.password = password;
	}

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
		
		if (StringUtils.equals(login, username) && StringUtils.equals(password, String.valueOf(credentials))) {
			
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
