package de.enflexit.awb.ws.core.websocket;

import de.enflexit.logging.LogTransportService;

/**
 * The Class LogTransportServiceWebSocket.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class LogTransportServiceWebSocket implements LogTransportService {

	
	/* (non-Javadoc)
	* @see de.enflexit.logging.LogTransportService#sendLog(java.lang.String)
	*/
	@Override
	public void sendLog(String message) {
		LogWebSocket.broadcast(message);
	}

}