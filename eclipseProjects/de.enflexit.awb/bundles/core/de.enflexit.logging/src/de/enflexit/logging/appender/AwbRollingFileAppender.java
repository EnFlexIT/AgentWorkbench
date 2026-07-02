package de.enflexit.logging.appender;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

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

		Marker myMarker = MarkerFactory.getMarker(this.getClass().getSimpleName());
		if (iLoggingEvent.getMarkerList().contains(myMarker) == false) {
			super.append(iLoggingEvent);
		}
		iLoggingEvent.getMarkerList().add(myMarker);
	}
	
}