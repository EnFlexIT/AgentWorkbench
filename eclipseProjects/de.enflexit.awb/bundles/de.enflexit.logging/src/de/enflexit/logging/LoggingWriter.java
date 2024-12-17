package de.enflexit.logging;

import ch.qos.logback.classic.db.DBAppender;

/**
 * If initialized this logger writes the logging output to the AWB DB.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class LoggingWriter extends DBAppender {


	private static LoggingWriter thisInstance;
	/**
	 * Returns the single instance of LoggingWriter if available.
	 * @return single instance of LoggingWriter
	 */
	public static LoggingWriter getInstance() {
		if (thisInstance==null) {
			thisInstance = new LoggingWriter();
		}
		return thisInstance;
	}
	/**
	 * Instantiates a new logging file writer.
	 */
	private LoggingWriter() { }

	
	/**
	 * Checks if is write to logging storage.
	 * @return true, if is write to logging storage
	 */
	public boolean isWriteToLoggingStorage() {
		return isStarted();
	}
	/**
	 * Sets the write to logging storage.
	 * @param writeToLoggingStorage the new write to logging storage
	 */
	public void setWriteToLoggingStorage(boolean writeToLoggingStorage) {
		if (writeToLoggingStorage==true) {
			if (this.isStarted()==false) this.start();
		} else {
			if (this.isStarted()==true) this.stop();
		}
	}
	
}
