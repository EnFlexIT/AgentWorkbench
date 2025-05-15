package de.enflexit.db.derby.tools;

import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DerbyBridgeToSLF4J.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public final class DerbyBridgeToSLF4J {

	private static final Logger derbyLogger = LoggerFactory.getLogger(DerbyBridgeToSLF4J.class);

	private DerbyBridgeToSLF4J() { }

	/**
	 * A basic adapter that funnels Derby's logs through an SLF4J logger.
	 */
	public static final class LoggingWriter extends Writer {
		
		@Override
		public void write(final char[] cbuf, final int off, final int len) {
			// Don't bother with empty lines.
			if (len > 1) {
				derbyLogger.error(new String(cbuf, off, len));
			}
		}

		@Override
		public void flush() {
			// noop.
		}

		@Override
		public void close() {
			// noop.
		}
	}

	public static Writer bridge() {
		return new LoggingWriter();
	}

}
