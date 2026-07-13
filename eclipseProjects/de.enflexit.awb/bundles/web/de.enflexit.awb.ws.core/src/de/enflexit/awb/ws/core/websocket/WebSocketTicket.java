package de.enflexit.awb.ws.core.websocket;


/**
 * The Class WebSocketTicket serves as a one time 
 * ticket to authenticate the webSocket handshake.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class WebSocketTicket {

	private long expiresAt;
	private static final long TICKET_LIFETIME = 15_000; //15 seconds
	
	/**
	 * Instantiates a new web socket ticket.
	 */
	public WebSocketTicket() {
		expiresAt = System.currentTimeMillis() + TICKET_LIFETIME;
	}
	
	/**
	 * Returns true if the ticket is expired.
	 *
	 * @return the expires at
	 */
	public boolean isExpired() {
		return System.currentTimeMillis() > expiresAt;
	}
}