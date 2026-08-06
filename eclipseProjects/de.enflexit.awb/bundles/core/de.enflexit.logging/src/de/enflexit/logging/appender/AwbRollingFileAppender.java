package de.enflexit.logging.appender;

import java.util.Iterator;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
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
	/**
	 * Returns the AwbRollingFileAppender if it is currently attached to the root logger
	 * either directly or indirectly (through an Async wrapper).
	 *
	 * @return the file appender
	 */
	private static FileAppender<?> getAwbRollingFileAppender() {

		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger rootLogger = context.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
		
		// --- Case 1: FILE_APPENDER is attached to the rootLogger ----------------------
		Appender<?> fileAppender = rootLogger.getAppender("FILE_APPENDER");

		if (fileAppender instanceof FileAppender<?>) {
			return (FileAppender<?>) fileAppender;
		}
		
		// --- Case 2: FILE_APPENDER is attached to the ASYNC_FILE_APPENDER -------------
		Appender<?> asyncAppender = rootLogger.getAppender("ASYNC_FILE_APPENDER");

		if (asyncAppender instanceof AsyncAppender async) {
			Iterator<Appender<ILoggingEvent>> iterator = async.iteratorForAppenders();

			if (iterator.hasNext()) {
				Appender<?> nested = iterator.next();

				if (nested instanceof FileAppender<?>) {
					return (FileAppender<?>) nested;
				}
			}
		}
		// --- FILE_APPENDER is not part of the configuration ---------------------------
		return null;
	}

	/**
	 * Returns the current log file of the awbRollingFileAppender
	 *
	 * @return the current log file, or null, if the fileAppender isn't configured
	 */
	public static String getCurrentLogFile() {
		
		FileAppender<?> fileAppender = getAwbRollingFileAppender();
		return fileAppender == null ? null : fileAppender.getFile();
	}	
}