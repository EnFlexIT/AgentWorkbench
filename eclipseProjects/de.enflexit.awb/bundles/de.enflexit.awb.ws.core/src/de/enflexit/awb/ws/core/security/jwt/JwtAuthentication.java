package de.enflexit.awb.ws.core.security.jwt;

import java.util.List;

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
     * Returns the current JwtPrincipal.
     * @return the JwtPrincipal
     */
    private JwtPrincipal getJwtPrincipal() {
    	return (JwtPrincipal)this.getUserIdentity().getUserPrincipal();
    }
    
    /**
	 * Returns the list of JWT token for the current principal.
	 * @return the jwtTokenList
	 */
    public List<String> getJwtTokenList() {
		return this.getJwtPrincipal().getJwtTokenList();
	}
    /**
     * Return the list of invalid JWT token.
     * @return the invalid JWT token
     */
    public List<String> getInvalidJwtToken() {
    	return this.getJwtPrincipal().getInvalidJwtToken();
    }
    /**
     * Removes the specified JWT token.
     * @param jwtToken the JWT token to remove
     */
    public void removeJwtToken(String jwtToken) {
    	this.getJwtPrincipal().removeJwtToken(jwtToken);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jetty.security.UserAuthentication#toString()
     */
    @Override
    public String toString() {
    	return super.toString();
    }


}
