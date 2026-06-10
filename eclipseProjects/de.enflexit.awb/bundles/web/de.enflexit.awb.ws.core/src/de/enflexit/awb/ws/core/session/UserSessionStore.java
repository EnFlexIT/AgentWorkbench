package de.enflexit.awb.ws.core.session;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.security.openid.OpenIdUserPrincipal;

import de.enflexit.awb.ws.core.security.AccessTokenRefreshment;
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
			instance.getUserSessionMaintenanceThread();
		}
		return instance;
	}
	/**
	 * Disposes the UserSessionStore.
	 */
	public static void dispose() {
		if (instance!=null) {
			instance.terminateUserSessionMaintenanceThread();
		}
		instance = null;
	}
	/**
	 * Private constructor for the singleton.
	 */
	private UserSessionStore() {
		this.getUserSessionHashMap();
	}
	
	
	// --------------------------------------------------------------
	// --- From here, runtime variable s ----------------------------
	// --------------------------------------------------------------
	private ConcurrentHashMap<String , UserSession> userSessionHashMap;
	private ConcurrentHashMap<String , UserSession> userSessionRefreshTokenHashMap;

	private UserSessionMaintenanceThread userSessionMaintenanceThread;
	
	
	/**
	 * Returns the session hash map.
	 * @return the session hash map
	 */
	private ConcurrentHashMap<String, UserSession> getUserSessionHashMap() {
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
		return this.getUserSessionHashMap().get(userID);
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
			this.getUserSessionHashMap().put(userID, uSess);
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
		this.getUserSessionHashMap().remove(userID);
		this.getUserSessionRefreshTokenHashMap().remove(userID);
	}
	
	
	// --------------------------------------------------------------	
	// --- Token refresh area ---------------------------------------
	// --------------------------------------------------------------
	/**
	 * Returns the user session refresh token hash map.
	 * @return the user session refresh token hash map
	 */
	private ConcurrentHashMap<String, UserSession> getUserSessionRefreshTokenHashMap() {
		if (userSessionRefreshTokenHashMap==null) {
			userSessionRefreshTokenHashMap = new ConcurrentHashMap<String, UserSession>();
		}
		return userSessionRefreshTokenHashMap;
	}
	/**
	 * Remind the specified UserSession for token refreshment.
	 * @param uSess the UserSession that requires an access token refreshment
	 */
	public void setRemindForTokenRefreshment(UserSession uSess) {
		if (uSess==null) return;
		this.getUserSessionRefreshTokenHashMap().put(uSess.getUserID(), uSess);
	}
	/**
	 * Removes the remind for token refreshment.
	 * @param uSess the UserSession that was just refreshed
	 */
	public void removeRemindForTokenRefreshment(UserSession uSess) {
		this.getUserSessionRefreshTokenHashMap().remove(uSess.getUserID());
	}

	
	// --------------------------------------------------------------	
	// --- Area of UserSession Maintenance --------------------------
	// --------------------------------------------------------------
	/**
	 * Returns the user session maintenance thread.
	 * @return the user session maintenance thread
	 */
	private UserSessionMaintenanceThread getUserSessionMaintenanceThread() {
		if (userSessionMaintenanceThread==null) {
			userSessionMaintenanceThread = new UserSessionMaintenanceThread();
			userSessionMaintenanceThread.start();
		}
		return userSessionMaintenanceThread;
	}
	/**
	 * Will call to terminate the UserSessionMaintenanceThread.
	 */
	private void terminateUserSessionMaintenanceThread() {
		if (userSessionMaintenanceThread!=null) {
			userSessionMaintenanceThread.doTerminate();
		}
	}
	/**
	 * The Class UserSessionMaintenanceThread.
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	private class UserSessionMaintenanceThread extends Thread {
		
		private boolean isDebug = false;
		
		private boolean doTerminate;
		private int intervalSec = 5;
		private int minDurationSec = 30;
		private int minDurationMillis = minDurationSec * 1000;
		
		/**
		 * Instantiates a new user SessionMaintenanceThread.
		 */
		public UserSessionMaintenanceThread() {
			super(UserSessionMaintenanceThread.class.getSimpleName());
		}
		/**
		 * Calls the threat to terminate.
		 */
		public void doTerminate() {
			this.doTerminate = true;
		}
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			super.run();
			
			// --- Run until requested to terminate ---------------------------
			while (this.doTerminate==false) {
				
				try {
					// --- Check to refresh token -----------------------------
					List<String> userIDList = new ArrayList<>(UserSessionStore.this.getUserSessionRefreshTokenHashMap().keySet());
					String debugMsg = "Found " + userIDList.size() + " UserSession for refreshment";
					for (String userID : userIDList) {
						UserSession uSess = UserSessionStore.this.getUserSessionRefreshTokenHashMap().get(userID);
						// --- Reached latest time to do token refreshment ----
						long timeToLogoutMillis = uSess.getAccessTokenExpiration() - System.currentTimeMillis();
						long timeToLogoutSec = timeToLogoutMillis / 1000;
						boolean isRefreshNow = timeToLogoutMillis <= this.minDurationMillis;
						debugMsg += "\nUserID: " + userID + ", time to logout: " + timeToLogoutSec + " s, Refresh now: " + isRefreshNow;
						if (isRefreshNow==true) {
							// --- Execute update -----------------------------
							if (this.refreshAccessToken(uSess)==true) {
								UserSessionStore.this.removeRemindForTokenRefreshment(uSess);
							}
						}
						if (this.doTerminate==true) break;
					}
					
					if (this.isDebug==true) {
						System.out.println("[" + this.getClass().getSimpleName() + "] " + debugMsg);
					}
					
				} catch (Exception ex) {
					System.err.println("[" + this.getClass().getSimpleName() + "] Error while extending access token:");
					ex.printStackTrace();
				}
				
				// --- Check for expired UserSessions -------------------------
				try {
					List<String> userIDList = new ArrayList<>(UserSessionStore.this.getUserSessionHashMap().keySet());
					for (String userID : userIDList) {
						UserSession uSess = UserSessionStore.this.getUserSessionRefreshTokenHashMap().get(userID);
						if (uSess!=null && uSess.isExpired()==true) {
							UserSessionStore.this.destroyUserSession(uSess);
						}
					}
					
				} catch (Exception ex) {
					System.err.println("[" + this.getClass().getSimpleName() + "] Error while cleaning-up:");
					ex.printStackTrace();
				}
				
				// --- Exit thread? ------------------------------------------- 
				if (this.doTerminate==true) break;
				
				// --- Wait until the next cycle time -------------------------
				try {
					Thread.sleep(this.getSleepTime());
				} catch (InterruptedException iEx) {
					//iEx.printStackTrace();
				}
			}
		}
		
		/**
		 * Refresh access token.
		 * @param uSess the UserSession
		 */
		private boolean refreshAccessToken(UserSession uSess) {
			
			boolean success = false;
			
			AccessTokenRefreshment atr = uSess.getAccessTokenRefreshment();
			if (atr!=null) {
				try {
					atr.refreshToken();
					success = true;
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			return success;
		}
		
		/**
		 * Returns the sleep time.
		 * @return the sleep time
		 */
		private long getSleepTime() {
			
			LocalTime now = LocalTime.now();
			LocalTime nextEventTime = now;
			
			// --- Round current time with intervalSec length ----------  
			int roundedSeconds = Math.round(now.getSecond() / (float)intervalSec) * intervalSec;
			if (roundedSeconds == 60) {
				nextEventTime = now.plusMinutes(1);
			    roundedSeconds = 0;
			}
			// --- Calculate the next event time -------------------- 
			nextEventTime = nextEventTime.withSecond(roundedSeconds).withNano(0);
			if (nextEventTime.isBefore(now)==true) {
				nextEventTime = nextEventTime.plusSeconds(this.intervalSec);
			}
			// --- Calculate sleep time -----------------------------
			long sleepTimeMillis = Duration.between(now, nextEventTime).toMillis();
			if (this.isDebug==true) {
				// --- Some debug output, if required ---------------
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
				String nextEventTimeFormatted = nextEventTime.format(formatter);
				System.out.println("[" + this.getClass().getSimpleName() + "] Next event time: " + nextEventTimeFormatted + " => Sleep Time:" + sleepTimeMillis + " ms");
			}
			return sleepTimeMillis;
		}
	} // -- end UserSessionMaintenanceThread --
	
}
