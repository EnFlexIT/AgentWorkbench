package de.enflexit.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;


/**
 * The Class WebSocketAppender.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class WebSocketAppender extends AppenderBase<ILoggingEvent> {

	private static LogTransportService logTransportService;
	
	
	/* (non-Javadoc)
	* @see ch.qos.logback.core.AppenderBase#append(java.lang.Object)
	*/
	@Override
	public void start() {
		super.start();
		addInfo("Websocket appender started");
	}
	
	/* (non-Javadoc)
	* @see ch.qos.logback.core.AppenderBase#append(java.lang.Object)
	*/
	@Override
	protected void append(ILoggingEvent event) {
		String message = format(event);
		if (logTransportService != null) {
			logTransportService.sendLog(message);
		}
	}

    /**
     * Format.
     *
     * @param event the event
     * @return the formatted string
     */
    private String format(ILoggingEvent event) {
        return String.format(
            "[%s] %s - %s",
            event.getLevel(),
            event.getLoggerName(),
            event.getFormattedMessage()
        );
    }

}
