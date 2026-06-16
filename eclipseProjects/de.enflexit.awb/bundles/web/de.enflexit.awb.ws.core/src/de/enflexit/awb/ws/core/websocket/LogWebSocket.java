package de.enflexit.awb.ws.core.websocket;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

/**
 * The Class LogWebSocket.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
@ServerEndpoint(value = "/ws/logs")
public class LogWebSocket {

	private static final Set<Session> sessions = ConcurrentHashMap.newKeySet();
	
	/**
	 * On open.
	 *
	 * @param session the session
	 */
	@OnOpen
	public void onOpen(Session session) {
		sessions.add(session);

		session.getAsyncRemote().sendText("Hello from server");
	}
	
	/**
	 * On close.
	 *
	 * @param session the session
	 */
	@OnClose
	public void onClose(Session session) {
		sessions.remove(session);
		System.out.println("Session removed");
	}
	
	/**
	 * Broadcast.
	 *
	 * @param message the message
	 */
	public static void broadcast(String message) {
	    System.out.println("Broadcast to " + sessions.size() + " sessions");
		for (Session session : sessions) {
			if (session != null && session.isOpen()) {
				session.getAsyncRemote().sendText(message);
			}
		}
	}
}
