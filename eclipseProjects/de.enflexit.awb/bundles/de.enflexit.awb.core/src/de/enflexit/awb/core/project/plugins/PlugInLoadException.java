package de.enflexit.awb.core.project.plugins;

/**
 * The exception class for Plug-In's 
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class PlugInLoadException extends RuntimeException {

	private static final long serialVersionUID = -3387741205485846804L;

	/**
	 * Instantiates a new plug-in load exception.
	 *
	 * @param throwable the throwable
	 */
	public PlugInLoadException(Throwable throwable) {
		super(throwable);		
	}
	
	/**
	 * Instantiates a new plug-in load exception.
	 *
	 * @param message the message
	 */
	public PlugInLoadException(String message) {
		super(message);		
	}
	
}
