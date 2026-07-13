package de.enflexit.awb.ws.core.websocket;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketTicketStore {

	private static Map <String, WebSocketTicket> tickets;
	
	/**
	 * Creates a ticket to authenticate the web socket handshake.
	 *
	 * @return the id of the generated ticket
	 */
	public static String createTicket() {
		
		String id = UUID.randomUUID().toString();
		WebSocketTicketStore.getTickets().put(id, new WebSocketTicket());
		return id;
	}
	/**
	 * Removes the ticket corresponding to the specified id.
	 * Used to check if the ticket is valid.
	 *
	 * @param ticketId the ticket id
	 * @return true, if the ticket was found and not yet expired
	 */
	public static boolean consumeTicket(String ticketId) {
		
		// --- Remove every ticket which is already expired ---------
		removeExpiredTickets();
		
		if (ticketId == null) return false;
		
		WebSocketTicket ticket = WebSocketTicketStore.getTickets().remove(ticketId);
		if (ticket == null) {
			return false;
		}
		return ticket.isExpired() == false;
	}
	
	/**
	 * Removes the expired tickets.
	 */
	private static void removeExpiredTickets() {
		WebSocketTicketStore.getTickets().entrySet().removeIf(key -> key.getValue().isExpired());
	}

	/**
	 * @return the tickets
	 */
	private static Map <String, WebSocketTicket> getTickets() {
		if (tickets == null) {
			tickets = new ConcurrentHashMap<String, WebSocketTicket>();
		}
		return tickets;
	}
}