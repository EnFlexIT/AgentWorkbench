package de.enflexit.awb.ws.core.security;

import java.util.HashMap;

/**
 * The Interface AccessTokenRefreshment.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface AccessTokenRefreshment {

	/**
	 * Has to receive the specified configuration.
	 * @param configuration the configuration
	 */
	public void setConfiguration(HashMap<String, String> configuration);
	
	/**
	 * Has to try the token refreshment.
	 */
	public void refreshToken();
	

	/**
	 * Has to return the token response that contains the new access and refresh token. 
	 * @return the token response
	 */
	public TokenResponse getTokenResponse();
	
	
	
	/**
     * The Class TokenResponse.
     * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
     */
    public class TokenResponse {
        
    	private String accessToken;
        private String refreshToken;
        private long expiresIn;
		
        public String getAccessToken() {
			return accessToken;
		}
		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}
		
		public String getRefreshToken() {
			return refreshToken;
		}
		public void setRefreshToken(String refreshToken) {
			this.refreshToken = refreshToken;
		}
		
		public long getExpiresIn() {
			return expiresIn;
		}
		public void setExpiresIn(long expiresIn) {
			this.expiresIn = expiresIn;
		}
    }
}
