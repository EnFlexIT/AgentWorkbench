package de.enflexit.logging.appender;

import java.io.IOException;
import java.util.List;

import org.slf4j.Marker;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import de.enflexit.logging.console.ConsoleScanner;
import de.enflexit.logging.console.PrintStreamListener;

/**
 * The Class AwbConsoleAppender is mostly a ConsoleAppender. It
 * only appends messages without the SYSTEM_OUT_MARKER to avoid loops 
 * with @see ConsoleScanner.
 * Also, 
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class AwbConsoleAppender extends ConsoleAppender<ILoggingEvent> {
	
	public static final String SYSTEM_OUT_MARKER = "SYSTEM_OUT";
	
	
	private Boolean isAwbPrintStreamListener;
	
	
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
	
	
	// --------------------------------------------------------------
	// --- Here starts the tricky part ------------------------------
	// --------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see ch.qos.logback.core.ConsoleAppender#start()
	 */
	@Override
	public void start() {
		super.start();
		this.setOutputStream(ConsoleScanner.getInstance().getPrintStreamListenerSystemOut());
	}
	
	/* (non-Javadoc)
	 * @see ch.qos.logback.core.OutputStreamAppender#writeOut(java.lang.Object)
	 */
	@Override
	protected void writeOut(ILoggingEvent event) throws IOException {
		if (this.isAwbPrintStreamListener() == true) {
			byte[] byteArray = this.encoder.encode(event);
			this.writeBytesOwn(byteArray);
		} else {
			super.writeOut(event);
		}
	}
	
    
	/**
	 * Same write method as logback's, except it calls 
	 * our version of writeByteArrayToOutputStreamWithPossibleFlush
	 *
	 * @param byteArray the byte array
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeBytesOwn(byte[] byteArray) throws IOException {
		if (byteArray == null || byteArray.length == 0)
			return;

		streamWriteLock.lock();

		try {
			// guard against appender having been stop() in parallel
			// note that the encoding step is performed outside the protection of the
			// streamWriteLock
			if (isStarted()) {
				writeByteArrayToOutputStreamWithPossibleFlushOwn(byteArray);
				updateByteCount(byteArray);
			}
		} finally {
			streamWriteLock.unlock();
		}
	}

	private boolean isAwbPrintStreamListener() {
		if (isAwbPrintStreamListener==null) {
			isAwbPrintStreamListener = (this.getOutputStream() instanceof PrintStreamListener);
		}
		return isAwbPrintStreamListener;
	}
	
	/**
	 * Same write method as logbacks's, except it passes the additional boolean parameter to
	 * the printstreamlisteners write method to differentiate original sysOut/ sysErr messages
	 * from AwbConsoleAppender output. 
	 *
	 */
	protected final void writeByteArrayToOutputStreamWithPossibleFlushOwn(byte[] byteArray) throws IOException {
		
		PrintStreamListener awbPrintStreamListener = (PrintStreamListener) this.getOutputStream();
		awbPrintStreamListener.write(byteArray, true);
		if (isImmediateFlush()) {
			awbPrintStreamListener.flush();
		}
	}
	
}