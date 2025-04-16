package de.enflexit.common.http;

/**
 * The Class HttpURLConnectorException.
 * 
 * @author Alexander Graute - SOFTEC - University of Duisburg - Essen
 */
public class HttpURLConnectorException extends Exception {

	private static final long serialVersionUID = 4890519136346013418L;

	/**
	 * Instantiates a new project repository update exception.
	 */
	public HttpURLConnectorException() {
	}
	/**
	 * Instantiates a new project repository update exception.
	 * @param message the message
	 */
	public HttpURLConnectorException(String message) {
		super(message);
	}
	/**
	 * Instantiates a new project repository update exception.
	 * @param cause the cause
	 */
	public HttpURLConnectorException(Throwable cause) {
		super(cause);
	}
	/**
	 * Instantiates a new project repository update exception.
	 * @param message the message
	 * @param cause the cause
	 */
	public HttpURLConnectorException(String message, Throwable cause) {
		super(message, cause);
	}
	/**
	 * Instantiates a new project repository update exception.
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public HttpURLConnectorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
