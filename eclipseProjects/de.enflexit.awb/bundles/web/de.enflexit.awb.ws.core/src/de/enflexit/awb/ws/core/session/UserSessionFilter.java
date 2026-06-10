package de.enflexit.awb.ws.core.session;

import java.io.IOException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import de.enflexit.awb.ws.core.security.AccessTokenRefreshment;
import de.enflexit.awb.ws.core.security.AccessTokenRefreshment.TokenResponse;
import de.enflexit.awb.ws.core.security.OIDCSecurityService;
import de.enflexit.awb.ws.core.security.OIDCTokenRefreshment;
import de.enflexit.awb.ws.core.security.jwt.JwtSingleUserSecurityService;
import de.enflexit.common.DateTimeHelper;
import de.enflexit.common.NumberHelper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * The Class UserSessionFilter.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class UserSessionFilter implements Filter {

	private static final String OIDC_SESSION_RESPONSE = "org.eclipse.jetty.security.openid.response";
	
	public static final String SECURITY_HANDLER_SERVICE = "securityHandlerService";
	public static final String USER_SESSION_LENGTH_IN_SECONDS = "userSessionLength";
	
	private boolean isPrintDebugOutput = false;
	
	private FilterConfig filterConfig;
	private Integer userSessionLength;
	
	private HashSet<String> excludedUriForSessionExtension;
	
	
	/* (non-Javadoc)
	 * @see jakarta.servlet.Filter#init(jakarta.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.debugPrint("Using " + this.getSecurityHandlerServiceSimpleClassName());
	}
	/**
	 * Returns the security handler service class name.
	 * @return the security handler service class name
	 */
	protected String getSecurityHandlerServiceClassName() {
		return this.filterConfig==null ? null : this.filterConfig.getInitParameter(SECURITY_HANDLER_SERVICE);
	}
	/**
	 * Returns the security handler service simple class name.
	 * @return the security handler service simple class name
	 */
	protected String getSecurityHandlerServiceSimpleClassName() {
		String className = this.getSecurityHandlerServiceClassName();
		if (className!=null) {
			return className.substring(className.lastIndexOf(".")+1);
		}
		return null;
	}
	
	/**
	 * Returns the user session length in seconds.
	 * @return the user session length
	 */
	protected Integer getUserSessionLength() {
		if (userSessionLength==null) {
			userSessionLength = NumberHelper.parseInteger(this.filterConfig.getInitParameter(USER_SESSION_LENGTH_IN_SECONDS));
			this.debugPrint("User Session Length: " + userSessionLength);
		}
		return userSessionLength;
	}
	
	/**
	 * Returns the excluded uri for session extension.
	 * @return the excluded uri for session extension
	 */
	protected HashSet<String> getExcludedUriForSessionExtension() {
		if (excludedUriForSessionExtension==null) {
			excludedUriForSessionExtension = new HashSet<>();
			excludedUriForSessionExtension.add("/api/alive".toLowerCase());
			excludedUriForSessionExtension.add("/api/user/sessionTime".toLowerCase());
		}
		return excludedUriForSessionExtension;
	}
	
	/**
	 * Returns the current principal.
	 *
	 * @param sRequest the s request
	 * @return the principal
	 */
	private Principal getPrincipal(HttpServletRequest sRequest) {
		Principal principal = null;
		try {
			principal = sRequest.getUserPrincipal();
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
		return principal;
	}
	
	/* (non-Javadoc)
	 * @see jakarta.servlet.Filter#doFilter(jakarta.servlet.ServletRequest, jakarta.servlet.ServletResponse, jakarta.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		// --- Check request --------------------------------------------------
		if (request instanceof HttpServletRequest sRequest) {

			String reqURI = sRequest.getRequestURI();
			if (this.getExcludedUriForSessionExtension().contains(reqURI.toLowerCase())==false) {
				
				String addedTimeDesc = null;
				// --- Get the registered user / Principal --------------------
				Principal principal = this.getPrincipal(sRequest);
				if (principal!=null) {
					// --- Find the UserID ------------------------------------
					String userID = principal.getName();
					this.debugPrint( reqURI +  " [User-ID is: " + userID + "]");
					
					// --- Try getting the user session -----------------------
					UserSession uSess = UserSessionStore.getInstance().getUserSession(userID);
					if (uSess==null) {
						// ----------------------------------------------------
						// --- Newly remind the current UserSession -----------
						// ----------------------------------------------------
						uSess = UserSessionStore.getInstance().createUserSession(principal, this.getUserSessionLength());
						
					} else {
						// ----------------------------------------------------
						// --- Session is known -------------------------------
						// ----------------------------------------------------
						if (uSess.isExpired()==true || uSess.isExpiredAccessToken()==true) {
							UserSessionStore.getInstance().destroyUserSession(uSess);
							uSess = null;
							this.debugPrint("=> Session was expired - create a new one !");
							uSess = UserSessionStore.getInstance().createUserSession(principal, this.getUserSessionLength());
							
						} else {
							// --- Extend the user session --------------------
							long oldExpiration = uSess.getExpiration();
							uSess.setLastActivityToNow();
							long addedTime = uSess.getExpiration() - oldExpiration;
							addedTimeDesc = "[ + " + addedTime + " ms] ";
							
							// ------------------------------------------------ 
							// --- Extend the token expiration? ---------------
							// ------------------------------------------------
							if (uSess.getExpiration() > uSess.getAccessTokenExpiration()) {
								// ------------------------------------------------------------------------------------
								// --- For both, a new JWT-token can be obtained by calling '/api/user/login' ! -------
								if (this.getSecurityHandlerServiceClassName().equals(JwtSingleUserSecurityService.class.getName())==true) {
									// --- The JWT use-case: ---------------------------------------------------------- 
									// --- => Nothing to do here, since renew of token will already be executed
									// ---    by the JwtAuthenticator for this path. 
									// --------------------------------------------------------------------------------
									
								} else if (this.getSecurityHandlerServiceClassName().equals(OIDCSecurityService.class.getName())==true) {
									// --- The OIDC use-case ----------------------------------------------------------
									// --- => Call OIDC token end point to renew JWT ----------------------------------
									// --------------------------------------------------------------------------------
									// --- The access token needs to be renewed ---------------------------------------
									// --------------------------------------------------------------------------------
									synchronized (uSess) {
										if (uSess.getAccessTokenRefreshment()==null) {
											// --- Create a task to get a new access token ------------ 
											this.createAccessTokenRefreshment(uSess, sRequest.getSession(false));
											
										} else {
											// --- Check if a new token has already arrived -----------
											TokenResponse tr = uSess.getAccessTokenRefreshment().getTokenResponse();
											if (tr!=null) {
												// --- Set values to HTTP- and USerSession ------------
												this.setAccessTokenRefreshmentResultToSession(uSess, sRequest.getSession(false), tr);
											}
										}
									}
									
								} // --- end security handler ---
							}
							
						}
					}
					
					if (uSess!=null) {
						this.debugPrint((addedTimeDesc==null ? "" : addedTimeDesc) + "Session Time: " + DateTimeHelper.getTimeAsString(uSess.getLastActivity(), "HH:mm:ss") + " - " + DateTimeHelper.getTimeAsString(uSess.getExpiration(), "HH:mm:ss") + " - A.-Token-Exp.: " + DateTimeHelper.getTimeAsString(uSess.getAccessTokenExpiration(), "HH:mm:ss"));
					}
					
					
				} // --- end principal ----
			} // --- end excluded URI's ---
		} // --- end HttpServletRequest ---
		
		// ----------------------------------------------------------
		// --- Forward to further filter jobs -----------------------
		chain.doFilter(request, response);
	}
	
	/**
	 * Sets the access token refreshment result to session.
	 *
	 * @param uSess the UserSession
	 * @param session the session
	 * @param tr the TokenResponse 
	 */
	private void setAccessTokenRefreshmentResultToSession(UserSession uSess, HttpSession session, TokenResponse tr) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> oidcResponse = (Map<String, Object>) session.getAttribute(OIDC_SESSION_RESPONSE);
		if (oidcResponse!=null) {
			oidcResponse.put(OIDCTokenRefreshment.ACCESS_TOKEN, tr.getAccessToken());
			if (tr.getRefreshToken()!=null) {
				oidcResponse.put(OIDCTokenRefreshment.REFRESH_TOKEN, tr.getRefreshToken()); // rotating tokens
			}
			session.setAttribute(OIDC_SESSION_RESPONSE, oidcResponse);
		}
		
		String oldAccessToken = uSess.getAccessToken();
		
		uSess.setAccessToken(tr.getAccessToken());
		uSess.setAccessTokenRefreshment(null);
		this.debugPrint("=> AccessToken was extended to " + DateTimeHelper.getTimeAsString(uSess.getAccessTokenExpiration(), "HH:mm:ss") );
		this.debugPrint("=> Old AccessToken: " + oldAccessToken);
		this.debugPrint("=> New AccessToken: " + uSess.getAccessToken());
	}

	/**
	 * Creates the access token refreshment for the specified UserSession.
	 *
	 * @param uSess the UserSession at which the AccessTokenRefreshment has to be added
	 * @param session the current HttpSession
	 */
	private void createAccessTokenRefreshment(UserSession uSess, HttpSession session) {
		
		String refreshToken = null;
		
        @SuppressWarnings("unchecked")
		Map<String, Object> oidcResponse = (Map<String, Object>) session.getAttribute(OIDC_SESSION_RESPONSE);
		if (oidcResponse != null) {
			refreshToken = (String) oidcResponse.get(OIDCTokenRefreshment.REFRESH_TOKEN);
		}

		// --- Collect the configuration ------------------------------
		HashMap<String, String> config = this.copyFilterConfigToHashMap();
		config.put(OIDCTokenRefreshment.REFRESH_TOKEN, refreshToken);
		
		// --- Create an AccessTokenRefreshment -----------------------
		AccessTokenRefreshment accTknRefresh = new OIDCTokenRefreshment();
		accTknRefresh.setConfiguration(config);
		
		// --- Set refreshment action to UserSession ------------------
		uSess.setAccessTokenRefreshment(accTknRefresh);
		
		// --- Remind for token refreshment ---------------------------
		UserSessionStore.getInstance().setRemindForTokenRefreshment(uSess);
	}
	
	/**
	 * Copies the local FilterConfig to a HashMap.
	 * @return a configuration HashMap or <code>null</code>
	 */
	private HashMap<String, String> copyFilterConfigToHashMap() {
		
		if (this.filterConfig==null) return null;
		
		HashMap<String, String> config = new HashMap<>();
		Enumeration<String> paraEnum = this.filterConfig.getInitParameterNames();
		while (paraEnum.hasMoreElements()) {
			String para  = paraEnum.nextElement();
			String value = this.filterConfig.getInitParameter(para);
			config.put(para, value);
		}
		return config;
	}
	
	
	/**
	 * Debug prints the specified message to the console.
	 * @param message the message
	 */
	private void debugPrint(String message) {
		this.debugPrint(message, false);
	}
	/**
	 * Debug prints the specified message to the console.
	 *
	 * @param message the message
	 * @param isError the is error
	 */
	private void debugPrint(String message, boolean isError) {
		if (this.isPrintDebugOutput==false) return;
		this.print(message, isError);
	}
	/**
	 * Prints the specified message to the console.
	 *
	 * @param message the message
	 * @param isError the is error
	 */
	private void print(String message, boolean isError) {
		if (message==null || message.isBlank()==true) return;
		if (isError==true) {
			System.err.println("[" + this.getClass().getSimpleName() + "] " + message);
		} else {
			System.out.println("[" + this.getClass().getSimpleName() + "] " + message);
		}
	}
	
}
