package de.enflexit.logging.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;

/**
 * The Class AwbRollingFileAppender.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class AwbRollingFileAppender extends RollingFileAppender<ILoggingEvent> {

	@Override
	protected void append(ILoggingEvent iLoggingEvent) {
			super.append(iLoggingEvent);
	}
	
}