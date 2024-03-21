package de.enflexit.awb.ws.core.security.jwt;

import org.eclipse.jetty.security.UserAuthentication;

/**
 * The Class JwtAuthentication.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JwtAuthentication extends UserAuthentication {

	private static final long serialVersionUID = 6670353102691509897L;

    /**
     * Creates a new instance.
     *
     * @param authenticator The authenticator that produced this authentication.
     * @param jwtTokwn The passed JWT token
     * @param principle The user principle extracted from the JWT token.
     */
    public JwtAuthentication(JwtAuthenticator authenticator, final JwtPrincipal principle) {
        super(authenticator.getAuthMethod(), new JwtUserIdentity(principle));
    }
    /**
     * Returns the JWT Token that was successfully validated to permit authentication.
     * @return the JWT Token that was successfully validated to permit authentication.
     */
    public String getJwtToken() {
        return ((JwtPrincipal)this.getUserIdentity().getUserPrincipal()).getJwtToken();
    }

    /* (non-Javadoc)
     * @see org.eclipse.jetty.security.UserAuthentication#toString()
     */
    @Override
    public String toString() {
    	return super.toString();
    }
    
}
