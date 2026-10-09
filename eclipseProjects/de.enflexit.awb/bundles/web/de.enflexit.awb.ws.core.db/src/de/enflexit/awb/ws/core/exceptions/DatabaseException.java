package de.enflexit.awb.ws.core.exceptions;

public class DatabaseException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public DatabaseException(String message, Throwable cause) {
        super(message, cause);
	}
}
