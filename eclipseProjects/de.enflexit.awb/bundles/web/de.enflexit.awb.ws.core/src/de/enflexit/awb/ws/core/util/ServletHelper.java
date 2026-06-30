package de.enflexit.awb.ws.core.util;

import jakarta.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Request;

/**
 * The Class ServletHelper provides some static help methods for servlets.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ServletHelper {

	public static final String lOGIN_PATH = "/user/login";
	public static final String lOGOUT_PATH = "/user/logout";
	
	public static final String INDEX_HTML = "/index.html";
	public static final String LOGOUT_REDIRECT_PATH = INDEX_HTML;
	
	
	/**
	 * Checks if the specified request is a preflight request.
	 *
	 * @param request the request
	 * @return true, if is preflight request
	 */
	public static boolean isPreflightRequest(HttpServletRequest request) {
		String method = request.getMethod();
		if (!"OPTIONS".equalsIgnoreCase(method))
			return false;
		if (request.getHeader("Access-Control-Request-Method") == null)
			return false;
		return true;
	}

	
	/**
	 * Checks if the specified request is a login request.
	 *
	 * @param request the request
	 * @return true, if is login path request
	 */
	public static boolean isLoginPathRequest(Request request) {
		return Request.getPathInContext(request).endsWith(lOGIN_PATH);
	}
	/**
	 * Checks if the specified contextPath is a login request.
	 *
	 * @param contextPath the context path
	 * @return true, if is login path request
	 */
	public static boolean isLoginPathRequest(String contextPath) {
		return contextPath.toLowerCase().endsWith(lOGIN_PATH);
	}
	
	
	/**
	 * Checks if the specified request is a logout request.
	 *
	 * @param request the request
	 * @return true, if is logout path request
	 */
	public static boolean isLogoutPathRequest(Request request) {
		return Request.getPathInContext(request).endsWith(lOGOUT_PATH);
	}
	/**
	 * Checks if the specified contextPath is a logout request.
	 *
	 * @param contextPath the context path
	 * @return true, if is logout path request
	 */
	public static boolean isLogoutPathRequest(String contextPath) {
		return contextPath.toLowerCase().endsWith(lOGOUT_PATH);
	}
}
