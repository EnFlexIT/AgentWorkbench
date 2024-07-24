package de.enflexit.awb.ws.core.security.jwt;

import org.eclipse.jetty.security.UserIdentity;

import javax.security.auth.Subject;
import java.security.Principal;


public class JwtUserIdentity implements UserIdentity {

	private JwtPrincipal principal;

    /**
     * Creates a new instance from a JWT principal.
     *
     * @param principal JwtPrincipal resulting from successful jwt validation.
     */
    public JwtUserIdentity(JwtPrincipal principal) {
        this.principal = principal;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jetty.server.UserIdentity#getSubject()
     */
    @Override
    public Subject getSubject() {
        final Subject subject = new Subject();
        subject.getPrincipals().add(principal);
        return subject;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jetty.server.UserIdentity#getUserPrincipal()
     */
    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jetty.server.UserIdentity#isUserInRole(java.lang.String, org.eclipse.jetty.server.UserIdentity.Scope)
     */
    @Override
    public boolean isUserInRole(final String role) {
		if (role.equals(this.principal.getRole()) == true) {
			return true;
		}
		return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return principal.toString();
    }
	
}
