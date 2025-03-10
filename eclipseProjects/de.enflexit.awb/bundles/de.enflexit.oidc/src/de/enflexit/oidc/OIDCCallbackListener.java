package de.enflexit.oidc;

import com.sun.net.httpserver.HttpExchange;

/**
 * The listener interface for receiving OIDCCallback events.
 * The class that is interested in processing a OIDCCallback
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addOIDCCallbackListener<code> method. When
 * the OIDCCallback event occurs, that object's appropriate
 * method is invoked.
 *
 * @see OIDCCallbackEvent
 */
public interface OIDCCallbackListener {
	
	public void callbackReceived(HttpExchange exchange);
}
