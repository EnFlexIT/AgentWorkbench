package de.enflexit.logging.console;

/**
 * The interface describe the method to recive any type of line input from the System console. 
 *  
 * @author Christian Derksen - SOFTEC - University Duisburg-Essen
 */
public interface ConsoleListener {

	/**
	 * Has to append console line output.
	 * @param line the line that ist to be printed into the console
	 */
	public void appendConsoleOutput(String line);
	
}
