package de.enflexit.logging.appender;

import java.util.List;

import org.slf4j.Marker;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;

/**
 * The Class AwbConsoleAppender is basically a ConsoleAppender, except it
 * only appends messages without the SYSTEM_OUT_MARKER to avoid loops and
 * duplicates with @see ConsoleScanner.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class AwbConsoleAppender extends ConsoleAppender<ILoggingEvent> {
	
	public static final String SYSTEM_OUT_MARKER = "SYSTEM_OUT";
	
	/* (non-Javadoc)
	* @see ch.qos.logback.core.OutputStreamAppender#append(java.lang.Object)
	*/
	@Override
	protected void append(ILoggingEvent iLoggingEvent) {
		if (this.hasSysOutMarker(iLoggingEvent) == false) { 
			super.append(iLoggingEvent);
		}
	}
	
	/**
	 * Checks whether the loggingEvent contains the SYSTEM_OUT marker
	 *
	 * @param iLoggingEvent the logging event
	 * @return true, if it contains the marker
	 */
	private boolean hasSysOutMarker(ILoggingEvent iLoggingEvent) {
		
		List<Marker> markerList = iLoggingEvent.getMarkerList();
		if (markerList == null) return false;
		
		for (Marker marker : markerList) {
			if (marker.getName().equals(SYSTEM_OUT_MARKER)) return true;
		}
		
		return false;
	}
	
}