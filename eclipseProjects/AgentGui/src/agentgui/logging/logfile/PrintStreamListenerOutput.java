package agentgui.logging.logfile;

import agentgui.logging.logfile.PrintStreamListener.PrintStreamListenerType;

/**
 * The Class PrintStreamListenerOutput represent an output type of the {@link PrintStreamListener}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class PrintStreamListenerOutput {
	
	private PrintStreamListenerType listenerType;
	private String lineOutput;
	
	/**
	 * Instantiates a new prints the stream listener output.
	 *
	 * @param type the PrintStreamListenerType
	 * @param lineOutput the line output
	 */
	public PrintStreamListenerOutput(PrintStreamListenerType type, String lineOutput) {
		this.listenerType = type;
		this.lineOutput = lineOutput;
	}
	
	/**
	 * Returns the prints the stream listener type.
	 * @return the prints the stream listener type
	 */
	public PrintStreamListenerType getPrintStreamListenerType() {
		return listenerType;
	}
	/**
	 * Returns the line output.
	 * @return the line output
	 */
	public String getLineOutput() {
		return lineOutput;
	}
}