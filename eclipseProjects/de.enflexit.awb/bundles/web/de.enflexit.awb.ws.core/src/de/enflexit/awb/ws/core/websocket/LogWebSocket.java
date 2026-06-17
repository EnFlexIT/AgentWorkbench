package de.enflexit.awb.ws.core.websocket;

import java.io.IOException;
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
	 * Called whenever a new web socket session is opened
	 *
	 * @param session the session to add
	 */
	@OnOpen
	public void onOpen(Session session) {
		sessions.add(session);
		session.getAsyncRemote().sendText("Hello from server");
	}
	
	/**
	 * Called whenever a web socket session is closed.
	 *
	 * @param session the session to remove
	 */
	@OnClose
	public void onClose(Session session) {
		sessions.remove(session);
		System.out.println("Session removed");
	}
	
	/**
	 * Attempts to close all open sessions.
	 */
	public static void shutdown() {
		for (Session session: sessions) {
			if (session.isOpen()) {
				try {
					session.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Broadcasts the message to all open sessions.
	 *
	 * @param message the message to broadcast
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