package de.enflexit.awb.ws.credential;

/**
 * The Class BearerTokenCredential.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class BearerTokenCredential extends AbstractCredential {
	
	private static final long serialVersionUID = 5553518664784944192L;
	
	private JwtToken jwtToken;


	/**
	 * Gets the {@link JwtToken}.
	 * @return the jwt token
	 */
	public JwtToken getJwtToken() {
		return jwtToken;
	}
	/**
	 * Sets the {@link JwtToken}.
	 * @param jwtToken the new jwt token
	 */
	public void setJwtToken(JwtToken jwtToken) {
		this.jwtToken = jwtToken;
	}
	
}
