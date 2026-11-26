package de.enflexit.awb.ws.core.exceptions;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ServiceException(String message, Throwable cause) {
        super(message, cause);
	}

	public ServiceException(String message) {
		super (message);
	}
}
