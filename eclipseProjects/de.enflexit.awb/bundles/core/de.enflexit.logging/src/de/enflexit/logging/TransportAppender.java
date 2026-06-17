package de.enflexit.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;


/**
 * The Class TransportAppender relays received log messages to
 * the LogTransportServiceRegistry, where they are distributed to all
 * registered log transport services.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class TransportAppender extends AppenderBase<ILoggingEvent> {

	/* (non-Javadoc)
	* @see ch.qos.logback.core.AppenderBase#append(java.lang.Object)
	*/
	@Override
	protected void append(ILoggingEvent event) {
		LogTransportServiceRegistry.send(event.getFormattedMessage());
	}


}