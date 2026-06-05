package de.enflexit.awb.ws.core.session;

import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;

import org.eclipse.jetty.security.openid.OpenIdUserPrincipal;

import de.enflexit.awb.ws.core.security.jwt.JwtPrincipal;
import de.enflexit.common.DateTimeHelper;
import de.enflexit.common.NumberHelper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * The Class UserSessionFilter.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class UserSessionFilter implements Filter {

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
	}
	/**
	 * Returns the security handler service class name.
	 * @return the security handler service class name
	 */
	protected String getSecurityHandlerServiceClassName() {
		return this.filterConfig==null ? null : this.filterConfig.getInitParameter(SECURITY_HANDLER_SERVICE);
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
			ex.printStackTrace();
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
				
				// --- Get the registered user / Principal --------------------
				Principal principal = this.getPrincipal(sRequest);
				if (principal!=null) {
					// --- Find the UserID ------------------------------------
					String userID = principal.getName();
					this.debugPrint("Security Service: " + this.getSecurityHandlerServiceClassName() +  " - User-ID is: " + userID + " - Requested URL: " + reqURI);
					
					// --- Try getting the user session -----------------------
					UserSession uSess = UserSessionStore.getInstance().getUserSession(userID);
					if (uSess==null) {
						// ----------------------------------------------------
						// --- Newly remind the current UserSession -----------
						// ----------------------------------------------------
						uSess = UserSessionStore.getInstance().getOrCreateUserSession(userID, this.getUserSessionLength());
						String accessToken = null;
						if (principal instanceof JwtPrincipal jwtPrincipal) {
							accessToken = jwtPrincipal.getJwtToken();
						} else if (principal instanceof OpenIdUserPrincipal openIdPrincipal) {
							accessToken = (String) openIdPrincipal.getCredentials().getResponse().get("access_token");
						}
						// --- Remind access token and its expiration ---------
						uSess.setAccessToken(accessToken);
						
					} else {
						// ----------------------------------------------------
						// --- Session is known -------------------------------
						// ----------------------------------------------------
						if (uSess.isExpired()==true ) {
							UserSessionStore.getInstance().destroyUserSession(uSess);
							uSess = null;
							this.debugPrint("=> Session was expired!");
							
						} else {
							// --- Extend the user session --------------------
							String msg = DateTimeHelper.getTimeAsString(uSess.getExpiration());
							uSess.setLastActivityToNow();
							this.debugPrint("=> Extended session end from " + msg + " to " + DateTimeHelper.getTimeAsString(uSess.getExpiration()));
							
							// ------------------------------------------------ 
							// --- Extend the token expiration? ---------------
							// ------------------------------------------------
							
							
							
							
							
						}
					}
					
					if (uSess!=null) {
						this.debugPrint("Session Time: " + DateTimeHelper.getTimeAsString(uSess.getLastActivity()) + " - " + DateTimeHelper.getTimeAsString(uSess.getExpiration()) + " - A.-Token-Exp.: " + DateTimeHelper.getTimeAsString(uSess.getAccessTokenExpiration()));
					}
					
					
					
				} // --- end principal ----
			} // --- end excluded URI's ---
		} // --- end HttpServletRequest ---
		
		// ----------------------------------------------------------
		// --- Forward to further filter jobs -----------------------
		chain.doFilter(request, response);
		
	}
	
	
	/**
	 * Debug prints the specified message to the console.
	 *
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
