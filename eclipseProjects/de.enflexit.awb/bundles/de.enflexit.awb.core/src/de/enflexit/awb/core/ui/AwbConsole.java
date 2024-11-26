package de.enflexit.awb.core.ui;

import java.util.Vector;

/**
 * The Interface AwbConsole defines the required methods for a console.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface AwbConsole {


	/**
	 * Return if the current console is a local console (from current JVM).
	 * @return true, if is local console
	 */
	public boolean isLocalConsole();
	
	/**
	 * Sets if the current console is a local console.
	 * @param isLocalConsole the new checks if is local console
	 */
	public void setLocalConsole(boolean isLocalConsole);

	
	/**
	 * Appends a Vector&lt;String&gt; of lines to this console  .
	 * @param linesToPrint the lines to print
	 */
	public void appendText(Vector<String> linesToPrint);
	
	/**
	 * This method can be used in order to append the console output from a remote container.
	 * A line may start with '[SysOut]' or '[SysErr]' to indicate the type of output.
	 * 
	 * @param text the text to print out
	 */
	public void appendText(String text);
	
	/**
	 * This method can be used in order to append text to the current console
	 * @param text the text to print
	 * @param isError specifies if the text is an error or not
	 */
	public void appendText(String text, boolean isError);
	
	
}
