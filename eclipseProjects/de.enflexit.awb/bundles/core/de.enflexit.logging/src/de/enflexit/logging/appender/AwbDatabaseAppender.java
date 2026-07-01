package de.enflexit.logging.appender;

import ch.qos.logback.classic.db.DBAppender;

/**
 * If initialized this logger writes the logging output to the AWB DB.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbDatabaseAppender extends DBAppender {

	private static AwbDatabaseAppender thisInstance;

	/**
	 * Returns the single instance of AwbDatabaseAppender if available.
	 * @return single instance of AwbDatabaseAppender
	 */
	public static AwbDatabaseAppender getInstance() {
		if (thisInstance==null) {
			thisInstance = new AwbDatabaseAppender();
		}
		return thisInstance;
	}
	/**
	 * Instantiates a new logging file writer.
	 */
	private AwbDatabaseAppender() { }

	
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
		if (writeToLoggingStorage==true && this.connectionSource!=null && this.getConnectionSource().isStarted()==true) {
			if (this.isStarted()==false) this.start();
		} else {
			if (this.isStarted()==true) this.stop();
		}
	}
	
}
