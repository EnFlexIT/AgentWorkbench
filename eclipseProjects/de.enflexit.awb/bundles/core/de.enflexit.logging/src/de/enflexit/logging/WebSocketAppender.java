package de.enflexit.logging;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;


/**
 * The Class WebSocketAppender.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
@Component
public class WebSocketAppender extends AppenderBase<ILoggingEvent> {

	private volatile LogTransportService logTransportService;
	
	@Reference
	void setTransportService(LogTransportService logTransportService) {
		System.out.println("Transport service successfully set.");
		this.logTransportService = logTransportService;
	}

	public void unsetTransportService(LogTransportService transport) {
		if (this.logTransportService == transport) {
			this.logTransportService = null;
		}
	}

	
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
