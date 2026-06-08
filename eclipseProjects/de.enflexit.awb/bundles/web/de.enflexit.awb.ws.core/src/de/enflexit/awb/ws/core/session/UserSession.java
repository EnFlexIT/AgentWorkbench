package de.enflexit.awb.ws.core.session;

import de.enflexit.awb.ws.core.security.jwt.JwtHelper;

/**
 * The Class UserSession.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class UserSession {
	
	private String userID;
	
	private long lastActivity;
	private int sessionLengthSeconds;
	private long expiration;

	private String accessToken;
	private long accessTokenExpiration;
	
	/**
	 * Instantiates a new user session.
	 *
	 * @param userId the user id
	 * @param sessionLengthSeconds the session length
	 */
	public UserSession(String userId, int sessionLengthSeconds) {
		this.setUserID(userId);
		this.setSessionLengthSeconds(sessionLengthSeconds);
		this.setLastActivityToNow();
	}
	
	/**
	 * Sets the user ID.
	 * @param userID the new user ID
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}
	/**
	 * Returns the user ID.
	 * @return the user ID
	 */
	public String getUserID() {
		return userID;
	}
	
	/**
	 * Sets the last activity to now and extends the expiration .
	 */
	public void setLastActivityToNow() {
		long now = System.currentTimeMillis();
		this.setLastActivity(now);
		this.setExpiration(now + (this.getSessionLengthSeconds()*1000));
	}
	/**
	 * Sets the last activity.
	 * @param lastActivity the new last activity
	 */
	public void setLastActivity(long lastActivity) {
		this.lastActivity = lastActivity;
	}
	/**
	 * Returns the last activity.
	 * @return the last activity
	 */
	public long getLastActivity() {
		return lastActivity;
	}
	
	/**
	 * Sets the session length in seconds.
	 * @param sessionLengthSeconds the new session length in seconds
	 */
	public void setSessionLengthSeconds(int sessionLengthSeconds) {
		this.sessionLengthSeconds = sessionLengthSeconds;
	}
	/**
	 * Returns the session length in seconds.
	 * @return the session length
	 */
	public int getSessionLengthSeconds() {
		return sessionLengthSeconds;
	}
	
	/**
	 * Sets the expiration.
	 * @param expiration the new expiration
	 */
	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}
	/**
	 * Returns the expiration.
	 * @return the expiration
	 */
	public long getExpiration() {
		return expiration;
	}
	/**
	 * Checks if the UserSession is expired.
	 * @return true, if this UserSession is expired
	 */
	public boolean isExpired() {
		return System.currentTimeMillis()>=this.getExpiration();
	}


	/**
	 * Returns the access token.
	 * @return the access token
	 */
	public String getAccessToken() {
		return accessToken;
	}
	/**
	 * Sets the access token.
	 * @param accessToken the new access token
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
		Long accessTokenExpiration = JwtHelper.getExpirationTimeOfJWT(this.accessToken);
		if (accessTokenExpiration==null) {
			this.setAccessTokenExpiration(-1);
		} else {
			this.setAccessTokenExpiration(accessTokenExpiration);
		}
	}
	
	/**
	 * Returns the access token expiration.
	 * @return the access token expiration
	 */
	public long getAccessTokenExpiration() {
		return accessTokenExpiration;
	}
	/**
	 * Sets the access token expiration.
	 * @param accessTokenExpiration the new access token expiration
	 */
	public void setAccessTokenExpiration(long accessTokenExpiration) {
		this.accessTokenExpiration = accessTokenExpiration;
	}
	
}
