package de.enflexit.awb.ws.core.propertyBusServices;

import de.enflexit.awb.ws.core.websocket.WebSocketTicketStore;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.bus.PropertyBusService;

/**
 * The Class PropertyBusServiceWebSocketTicket is used 
 * to receive a one timer ticket to authenticate the 
 * web socket handshake @see LogWebSocket.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class PropertyBusServiceWebSocketTicket implements PropertyBusService {

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getPerformative()
	*/
	@Override
	public String getPerformative() {
		return "WEBSOCKET.TICKET";
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#setProperties(de.enflexit.common.properties.Properties, java.lang.String)
	*/
	@Override
	public boolean setProperties(Properties properties, String arguments) {
		return false;
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getProperties(de.enflexit.common.properties.Properties, java.lang.String)
	*/
	@Override
	public Properties getProperties(Properties properties, String arguments) {
		
		if (properties == null) properties = new Properties();
		
		properties.setStringValue("ticketID", WebSocketTicketStore.createTicket());
		
		return properties;
	}

}
