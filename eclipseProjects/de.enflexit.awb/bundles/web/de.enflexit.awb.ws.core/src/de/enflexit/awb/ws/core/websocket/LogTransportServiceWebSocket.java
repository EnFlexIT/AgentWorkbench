package de.enflexit.awb.ws.core.websocket;

import de.enflexit.logging.LogTransportService;
import org.osgi.service.component.annotations.Component;

@Component(service = LogTransportService.class)
public class LogTransportServiceWebSocket implements LogTransportService {

	/* (non-Javadoc)
	* @see de.enflexit.logging.LogTransportService#sendLog(java.lang.String)
	*/
	@Override
	public void sendLog(String message) {
		LogWebSocket.broadcast(message);
	}

}
