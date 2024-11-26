package de.enflexit.awb.core.update;

/**
 * The Class ProjectRepositoryUpdateException.
 * 
 * @author Alexander Graute - SOFTEC - University of Duisburg - Essen
 */
public class ProjectRepositoryUpdateException extends Exception {

	private static final long serialVersionUID = 4890519136346013418L;

	/**
	 * Instantiates a new project repository update exception.
	 */
	public ProjectRepositoryUpdateException() {
	}
	/**
	 * Instantiates a new project repository update exception.
	 * @param message the message
	 */
	public ProjectRepositoryUpdateException(String message) {
		super(message);
	}
	/**
	 * Instantiates a new project repository update exception.
	 * @param cause the cause
	 */
	public ProjectRepositoryUpdateException(Throwable cause) {
		super(cause);
	}
	/**
	 * Instantiates a new project repository update exception.
	 * @param message the message
	 * @param cause the cause
	 */
	public ProjectRepositoryUpdateException(String message, Throwable cause) {
		super(message, cause);
	}
	/**
	 * Instantiates a new project repository update exception.
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public ProjectRepositoryUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
