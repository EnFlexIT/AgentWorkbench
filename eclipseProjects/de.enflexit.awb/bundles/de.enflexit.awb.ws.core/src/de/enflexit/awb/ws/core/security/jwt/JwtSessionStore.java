package de.enflexit.awb.ws.core.security.jwt;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The Class JwtSessionStore stores all sessions.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JwtSessionStore {

	private ConcurrentMap<String, JwtAuthentication> sessionMap;
	
	/**
	 * Returns the currently cached sessions as JwtAuthentication.
	 * @return the session map
	 */
	private ConcurrentMap<String, JwtAuthentication> getSessionMap() {
		if (sessionMap==null) {
			sessionMap = new ConcurrentHashMap<>();
		}
		return sessionMap;
	}
	
	/**
	 * Will cache the specified authentication.
	 * @param authentication the authentication
	 */
	public void cacheAuthentication(JwtAuthentication authentication) {
		
		if (authentication!=null) {
			// --- First cleanup invalid token ----------------------
			List<String> invalidToken = authentication.getInvalidJwtToken(); 
			for (String token : invalidToken) {
				authentication.removeJwtToken(token);
				this.removeAuthentication(token);
			}
			
			// --- Store valid token in local session map -----------
			for (String token : authentication.getJwtTokenList()) {
				this.getSessionMap().put(token, authentication);
			}
		}
	}
	
	/**
	 * Returns the authentication for the specified JWT token.
	 *
	 * @param jwtToken the jwt token
	 * @return the authentication
	 */
	public JwtAuthentication getAuthentication(String jwtToken) {
		if (jwtToken!=null) {
			return this.getSessionMap().get(jwtToken);
		}
		return null;
	}
	
	/**
	 * Removes the JwtAuthentication of the specified JWT token.
	 *
	 * @param jwtToken the JWT token
	 * @return the removed JwtAuthentication
	 */
	public JwtAuthentication removeAuthentication(String jwtToken) {
		if (jwtToken!=null) {
			JwtAuthentication jwtAuth = this.getSessionMap().remove(jwtToken); 
			if (jwtAuth!=null) {
				jwtAuth.removeJwtToken(jwtToken);
			}
			return jwtAuth;
		}
		return null;
	}
	
}
