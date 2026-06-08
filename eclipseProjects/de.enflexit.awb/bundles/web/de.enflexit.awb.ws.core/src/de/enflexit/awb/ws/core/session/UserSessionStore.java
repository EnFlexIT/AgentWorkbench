package de.enflexit.awb.ws.core.session;

import java.security.Principal;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.security.openid.OpenIdUserPrincipal;

import de.enflexit.awb.ws.core.security.jwt.JwtPrincipal;

/**
 * The Class UserSessionStore.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class UserSessionStore {

	public static UserSessionStore instance;
	
	/**
	 * Returns the single instance of UserSessionStore.
	 * @return single instance of UserSessionStore
	 */
	public static UserSessionStore getInstance() {
		if (instance==null) {
			instance = new UserSessionStore();
		}
		return instance;
	}
	public static void dispose() {
		instance = null;
	}
	private UserSessionStore() {
		this.getSessionHashMap();
	}
	
	
	private ConcurrentHashMap<String , UserSession> userSessionHashMap;
	
	/**
	 * Returns the session hash map.
	 * @return the session hash map
	 */
	private ConcurrentHashMap<String, UserSession> getSessionHashMap() {
		if (userSessionHashMap==null) {
			userSessionHashMap = new ConcurrentHashMap<>();
		}
		return userSessionHashMap;
	}
	/**
	 * Returns the UserSession for the specified user name or user ID.
	 *
	 * @param userID the user ID
	 * @return the user session
	 */
	public UserSession getUserSession(String userID) {
		return this.getSessionHashMap().get(userID);
	}
	/**
	 * Returns the current or creates a new UserSession that stores the current login session times.
	 *
	 * @param userID the user ID
	 * @param sessionLengthSeconds the session length seconds
	 * @return the or create user session
	 */
	public UserSession getOrCreateUserSession(String userID, int sessionLengthSeconds) {
		UserSession uSess = this.getUserSession(userID);
		if (uSess == null) {
			uSess = new UserSession(userID, sessionLengthSeconds);
			this.getSessionHashMap().put(userID, uSess);
		}
		return uSess;
	}
	/**
	 * Based on the specified Principal, creates a new UserSession.
	 *
	 * @param principal the principal
	 * @param userSessionLength the user session length
	 * @return the user session
	 */
	public UserSession createUserSession(Principal principal, Integer userSessionLength) {

		String userID = principal.getName();
		UserSession uSess = this.getOrCreateUserSession(userID, userSessionLength);
		String accessToken = null;
		if (principal instanceof JwtPrincipal jwtPrincipal) {
			accessToken = jwtPrincipal.getJwtToken();
		} else if (principal instanceof OpenIdUserPrincipal openIdPrincipal) {
			accessToken = (String) openIdPrincipal.getCredentials().getResponse().get("access_token");
		}
		uSess.setAccessToken(accessToken);
		return uSess;
	}
	
	/**
	 * Destroy user session.
	 * @param userSession the UserSession to destroy
	 */
	public void destroyUserSession(UserSession userSession) {
		this.destroyUserSession(userSession.getUserID());
	}
	/**
	 * Destroy user session.
	 * @param userID the user ID
	 */
	public void destroyUserSession(String userID) {
		this.getSessionHashMap().remove(userID);
	}
	
	
}
