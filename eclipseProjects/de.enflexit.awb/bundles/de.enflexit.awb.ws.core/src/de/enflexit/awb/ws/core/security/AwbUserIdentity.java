package de.enflexit.awb.ws.core.security;

import java.security.Principal;

import javax.security.auth.Subject;

import org.eclipse.jetty.security.UserIdentity;
import org.eclipse.jetty.security.UserPrincipal;
import org.eclipse.jetty.util.security.Credential;
import org.eclipse.jetty.util.security.Password;

/**
 * The Class AwbUserIdentity.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbUserIdentity implements UserIdentity {

	private final Subject subject;
	private final Principal principal;
	private final boolean isUserInRole;
	
	/**
	 * Instantiates a new AWB UserIdentity.
	 *
	 * @param userName the user name
	 * @param credentials the credentials
	 * @param isUserInRole the is user in role
	 */
	public AwbUserIdentity(String userName, Object credentials, boolean isUserInRole) {
		
		Credential credential = new Password(String.valueOf(credentials));

		this.principal = new UserPrincipal(userName, credential);
		this.isUserInRole = isUserInRole;
		
		this.subject = new Subject();
		this.subject.getPrincipals().add(this.principal);
		this.subject.getPrivateCredentials().add(credential);

	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.UserIdentity#getSubject()
	 */
	@Override
	public Subject getSubject() {
		return this.subject;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.UserIdentity#getUserPrincipal()
	 */
	@Override
	public Principal getUserPrincipal() {
		return this.principal;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jetty.security.UserIdentity#isUserInRole(java.lang.String)
	 */
	@Override
	public boolean isUserInRole(String role) {
		return this.isUserInRole;
	}
}
