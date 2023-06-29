package de.enflexit.expression;

/**
 * The Class TimeSeriesException.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class TimeSeriesException extends Exception {

	private static final long serialVersionUID = -5383992246286468332L;
	
	/**
	 * Instantiates a new time series exception.
	 */
	public TimeSeriesException() {
		super();
	}
	/**
	 * Instantiates a new time series exception.
	 * @param message the message
	 */
	public TimeSeriesException(String message) {
		super(message);
	}
	/**
	 * Instantiates a new time series exception.
	 * @param throwable the throwable
	 */
	public TimeSeriesException(Throwable throwable) {
		super(throwable);
	}
	/**
	 * Instantiates a new time series exception.
	 *
	 * @param message the message
	 * @param throwable the throwable
	 */
	public TimeSeriesException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
