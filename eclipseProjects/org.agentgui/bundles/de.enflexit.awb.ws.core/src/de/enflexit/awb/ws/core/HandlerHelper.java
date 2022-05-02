package de.enflexit.awb.ws.core;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;

/**
 * The Class HandlerHelper provides some static help methods to work with {@link Handler}.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class HandlerHelper {

	public static String getContextPath(Handler handler) {
		
		String contextPath = "?";
		try {
			// --- Get the handler ------------------------
			if (handler instanceof ContextHandler) {
				ContextHandler contHandler = (ContextHandler) handler;
				contextPath = contHandler.getContextPath();
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return contextPath;
	}
	
}
