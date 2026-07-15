package de.enflexit.awb.ws.core.websocket;

import java.util.ArrayList;
import java.util.List;

import de.enflexit.logging.console.ConsoleListener;
import de.enflexit.logging.console.ConsoleScanner;

/**
 * The class WebSocketConsoleListener serves as a bridge between the 
 * {@link ConsoleScanner} and {@link LogWebSocket}. It formats and
 * transmits every output from the ConsoleScanner to the LogWebSocket class.
 * The singleton instance is mainly used to register and unregister as
 * a listener at the ConsoleScanner.
 */
public class WebSocketConsoleListener implements ConsoleListener {

	private static WebSocketConsoleListener instance;
	
	private WebSocketConsoleListener() {}
	
	public static WebSocketConsoleListener getInstance() {
		if (instance == null) {
			instance = new WebSocketConsoleListener();
		}
		return instance;
	}
	
	/* (non-Javadoc)
	* @see de.enflexit.logging.console.ConsoleListener#appendConsoleOutput(java.lang.String)
	*/
	@Override
	public void appendConsoleOutput(String line) {
		line = this.format(line);
		if (line.isBlank() == false) {
			LogWebSocket.broadcast(line);
		}
	}

	/**
	 * Returns a formatted copy of the ConsoleScanners stack.
	 *
	 * @return the formatted stack
	 */
	public List<String> getFormattedStack(){
		
		List<String> formattedStack = new ArrayList<>();
		for (String line : ConsoleScanner.getInstance().getStack()) {
			line = this.format(line);
			if (line.isBlank() == false) {
				formattedStack.add(line);
			}
		}
		return formattedStack;
	}
	
	/**
	 * Formats the passed line by removing [SysOut] / [SysErr] prefixes
	 * and trimming.
	 *
	 * @param line the line
	 * @return the string
	 */
	private String format(String line) {
		line = line.replace("[SysOut]", "");
		line = line.replace("[SysErr]", "");
		return line.trim();
	}
}
