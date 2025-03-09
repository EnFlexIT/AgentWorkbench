package de.enflexit.common.http;

/**
 * The Class HttpURLConnectorAuthorizationException.
 * @author Alexander Graute - SOFTEC - University of Duisburg - Essen
 */
public class HttpURLConnectorAuthorizationException extends HttpURLConnectorException {

	private static final long serialVersionUID = 4555792668841391604L;

	/**
	 * Instantiates a new project repository update authorization exception.
	 */
	public HttpURLConnectorAuthorizationException() {
		super();
	}
	/**
	 * Instantiates a new project repository update authorization exception.
	 * @param message the message
	 */
	public HttpURLConnectorAuthorizationException(String message) {
		super(message);
	}
	/**
	 * Instantiates a new project repository update authorization exception.
	 * @param cause the cause
	 */
	public HttpURLConnectorAuthorizationException(Throwable cause) {
		super(cause);
	}
	/**
	 * Instantiates a new project repository update authorization exception.
	 * @param message the message
	 * @param cause the cause
	 */
	public HttpURLConnectorAuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}
	/**
	 * Instantiates a new project repository update authorization exception.
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public HttpURLConnectorAuthorizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}