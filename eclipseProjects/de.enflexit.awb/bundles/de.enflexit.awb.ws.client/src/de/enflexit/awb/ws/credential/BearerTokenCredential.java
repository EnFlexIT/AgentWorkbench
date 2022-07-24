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
	
	@Override
	public boolean equals(Object obj) {
		boolean equals=super.equals(obj);
		if(obj instanceof BearerTokenCredential) {
			BearerTokenCredential cred=(BearerTokenCredential) obj;
			if(!this.getJwtToken().equals(cred.getJwtToken())) {
				equals=false;
			}
		}else {
			equals=false;
		}
		return equals;
	}
	
}
