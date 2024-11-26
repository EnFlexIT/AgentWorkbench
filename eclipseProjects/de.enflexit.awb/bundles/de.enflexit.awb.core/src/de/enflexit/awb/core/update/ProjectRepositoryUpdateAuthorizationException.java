package de.enflexit.awb.core.update;

/**
 * The Class ProjectRepositoryUpdateAuthorizationException.
 * @author Alexander Graute - SOFTEC - University of Duisburg - Essen
 */
public class ProjectRepositoryUpdateAuthorizationException extends ProjectRepositoryUpdateException {

	private static final long serialVersionUID = 4555792668841391604L;

	/**
	 * Instantiates a new project repository update authorization exception.
	 */
	public ProjectRepositoryUpdateAuthorizationException() {
		super();
	}
	/**
	 * Instantiates a new project repository update authorization exception.
	 * @param message the message
	 */
	public ProjectRepositoryUpdateAuthorizationException(String message) {
		super(message);
	}
	/**
	 * Instantiates a new project repository update authorization exception.
	 * @param cause the cause
	 */
	public ProjectRepositoryUpdateAuthorizationException(Throwable cause) {
		super(cause);
	}
	/**
	 * Instantiates a new project repository update authorization exception.
	 * @param message the message
	 * @param cause the cause
	 */
	public ProjectRepositoryUpdateAuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}
	/**
	 * Instantiates a new project repository update authorization exception.
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public ProjectRepositoryUpdateAuthorizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}