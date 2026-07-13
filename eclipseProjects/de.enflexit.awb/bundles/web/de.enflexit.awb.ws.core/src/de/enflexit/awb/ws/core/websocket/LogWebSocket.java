package de.enflexit.awb.ws.core.websocket;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.enflexit.logging.console.ConsoleListener;
import de.enflexit.logging.console.ConsoleScanner;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

/**
 * The Class LogWebSocket is used to send log messages 
 * to connected clients via websocket.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
@ServerEndpoint(value = "/ws/logs")
public class LogWebSocket implements ConsoleListener {

	private static final Set<Session> sessions = ConcurrentHashMap.newKeySet();
	
	/**
	 * Instantiates a new log web socket.
	 */
	public LogWebSocket() {
		ConsoleScanner.getInstance().addConsoleListener(this);
	}
	
	/**
	 * Called whenever a new websocket session is opened
	 *
	 * @param session the session to add
	 */
	@OnOpen
	public void onOpen(Session session) {

		// --- Extract ticket from URL ----------------------------------------
	    List<String> tickets = session.getRequestParameterMap().get("ticket");
	    String ticket = null;
	    if (tickets != null && tickets.isEmpty() == false) {
	    	ticket = tickets.get(0);
	    }
	    // --- Check if ticket is valid ---------------------------------------
	    if (WebSocketTicketStore.consumeTicket(ticket) == true) {
	    	// --- Add the session and send the buffered messages -------------
	    	sessions.add(session);
	    	ConsoleScanner.getInstance().getStack().forEach(line -> session.getAsyncRemote().sendText(line));
	    } else {
	    	// --- Invalid ticket, close session ------------------------------
			try {
				session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Invalid ticket"));
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	    	
	}
	
	/**
	 * Called whenever a websocket session is closed.
	 *
	 * @param session the session to remove
	 */
	@OnClose
	public void onClose(Session session) {
		sessions.remove(session);
	}
	
	/**
	 * Called when a websocket related error occurs.
	 *
	 * @param session the session
	 * @param error the error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		//TODO How to handle errors? Websocket usually throws a
		//ClosedChannelException when the server stops. 
//		System.out.println("Error in websocket");
	}
	
	/**
	 * Attempts to close all open sessions.
	 */
	public static void closeAllSessions() {
		
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
		for (Session session : sessions) {
			if (session != null && session.isOpen()) {
				session.getAsyncRemote().sendText(message);
			}
		}
	}

	/* (non-Javadoc)
	* @see de.enflexit.logging.console.ConsoleListener#appendConsoleOutput(java.lang.String)
	*/
	@Override
	public void appendConsoleOutput(String line) {
		LogWebSocket.broadcast(line);
	}
	
}