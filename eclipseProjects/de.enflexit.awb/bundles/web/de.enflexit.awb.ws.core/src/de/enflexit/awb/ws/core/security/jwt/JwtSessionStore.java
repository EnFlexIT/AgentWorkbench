package de.enflexit.awb.ws.core.security.jwt;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The singleton class JwtSessionStore stores all {@link JwtAuthentication} instances for every instance
 * of the {@link JwtSingleUserSecurityHandler} and the {@link JwtAuthenticator}.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JwtSessionStore {
	
	private static JwtSessionStore instance;
	/**
	 * Return the single instance of the JwtSessionStore.
	 * @return single instance of JwtSessionStore
	 */
	public static JwtSessionStore getInstance() {
		if (instance==null) {
			instance = new JwtSessionStore();
		}
		return instance;
	}
	/**
	 * Private constructor for the JWT session store.
	 */
	private JwtSessionStore() { }
	
	
	
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
				this.getSessionMap().remove(token);
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
	 * Removes the JwtAuthentication from the session store by considering all JWT token.
	 *
	 * @param jwtToken the JWT token
	 * @return the removed JwtAuthentication
	 */
	public JwtAuthentication removeAuthentication(String jwtToken) {
		if (jwtToken!=null) {
			JwtAuthentication jwtAuth = this.getSessionMap().remove(jwtToken); 
			if (jwtAuth!=null) {
				jwtAuth.removeJwtToken(jwtToken);
				// --- Remove all other known token -------
				for (String token : jwtAuth.getJwtTokenList()) {
					this.getSessionMap().remove(token);
				}
			}
			return jwtAuth;
		}
		return null;
	}
	
}
