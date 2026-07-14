package de.enflexit.logging.console;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.osgi.service.prefs.BackingStoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import de.enflexit.logging.AwbLogbackConfigurator;
import de.enflexit.logging.appender.AwbConsoleAppender;

/**
 * This Class can be used in order to listen to the output which will be generated<br> 
 * through the console by using System.out and System.err - commands.<br>
 * <br>
 * The received output will be received in the local <code>Vector&lt;String&gt;</code> outputStack., which<br>
 * can be accessed by the getStack()-method in a synchronized way.<br>   
 * 
 * @see SysOutBoard
 * @see PrintStreamListener
 * @see System#out
 * @see System#err
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ConsoleScanner {
	
	private static Logger logger = LoggerFactory.getLogger(ConsoleScanner.class);

	private static ConsoleScanner scanner;
	/**
	 * Returns the single instance of ConsoleScanner.
	 * @return single instance of ConsoleScanner
	 */
	public static ConsoleScanner getInstance() {
		if (scanner==null) {
			scanner = new ConsoleScanner();
		}
		return scanner;
	}
	/**
	 * Private constructor.
	 */
	private ConsoleScanner() {
		this.setScanner();
	}
	
	
	public final static int DEFAULT_STACK_SIZE = 600;
	
	private Integer stackSize;
	private Vector<String> outputStack = new Vector<String>(); 
	private List<ConsoleListener> consoleListener;
	
	/**
	 * This method will set PrintStreamListener to the System.out and the System.err PrintStream's
	 */
	private void setScanner() {

		PrintStream orgStreamOut = System.out;
		PrintStream orgStreamErr = System.err;
		try {
			// --- Build new PrintStream's ------ 
			PrintStream listenStreamOut = new PrintStreamListener(orgStreamOut, this, PrintStreamListener.SystemOutput);
			PrintStream listenStreamErr = new PrintStreamListener(orgStreamErr, this, PrintStreamListener.SystemError);

			System.setOut(listenStreamOut);
			System.setErr(listenStreamErr);
			
		} catch (Exception ex) {
			// --- Restoring back to console ----
			System.setOut(orgStreamOut);
			System.setErr(orgStreamErr);
			// --- Notify about it --------------
			System.out.println("Unsuccessful tried to redirect output & exceptions to PrintStreamListener for distribute debugging.");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Returns all registered console listener.
	 * @return the console listener
	 */
	public List<ConsoleListener> getConsoleListener() {
		if (consoleListener==null) {
			consoleListener = new ArrayList<>();
		}
		return consoleListener;
	}
	/**
	 * Adds the console listener.
	 *
	 * @param consoleListener the console listener
	 * @return true, if successful
	 */
	public boolean addConsoleListener(ConsoleListener consoleListener) {
		if (consoleListener!=null && this.getConsoleListener().contains(consoleListener)==false) {
			return this.getConsoleListener().add(consoleListener);
		}
		return false;
	}
	/**
	 * Removes the console listener.
	 *
	 * @param consoleListener the console listener
	 * @return true, if successful
	 */
	public boolean removeConsoleListener(ConsoleListener consoleListener) {
		if (consoleListener!=null && this.getConsoleListener().contains(consoleListener)==true) {
			return this.getConsoleListener().remove(consoleListener);
		}
		return false;
	}
	
	
	/**
	 * Can be used in order to get the current outputStack of the local console.
	 * @return the current output stack
	 */
	public Vector<String> getStack() {
		if (this.outputStack==null) {
			this.outputStack = new Vector<String>();
		}
		return this.outputStack;
	}
	
	/**
	 * This method will be used in order to append an output line (System.out or System.err) 
	 * to the local outputStack
	 *
	 * @param lineOutput the line output to display
	 */
	public void append2Stack(String lineOutput) {
		
		// --- Append to the current stack -------------------------- 
		synchronized (this.getStack()) {
			this.getStack().add(lineOutput);
			// --- Reduce to the specified stack size ---------------
			while (this.getStack().size()>this.getStackSize()) {
				this.getStack().remove(0);
			}
		}
		
		// --- Forward information to each ConsoleListener ----------
		for (ConsoleListener cl : this.getConsoleListener()) {
			try {
				cl.appendConsoleOutput(lineOutput);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		// --- Log the output with a marker to avoid recursion ------
		Marker sysOutMarker = MarkerFactory.getMarker(AwbConsoleAppender.SYSTEM_OUT_MARKER);
		String tempString;
		if (lineOutput.startsWith(PrintStreamListener.SystemError)) {
			tempString = lineOutput.substring(lineOutput.indexOf(PrintStreamListener.SystemError)+PrintStreamListener.SystemError.length()).trim();
			if (tempString != null && tempString.isBlank() == false) {
				logger.error(sysOutMarker, tempString);
			}
		} else if (lineOutput.startsWith(PrintStreamListener.SystemOutput)) {
			tempString = lineOutput.substring(lineOutput.indexOf(PrintStreamListener.SystemOutput)+PrintStreamListener.SystemOutput.length()).trim();
			if (tempString != null && tempString.isBlank() == false) {
				logger.info(sysOutMarker, tempString);
			}
		} else {
			tempString = lineOutput.trim();
			if (tempString != null && tempString.isBlank() == false) {
				logger.info(sysOutMarker, lineOutput.trim());
			}
	
		}
	}
	

	// ------------------------------------------------------------------------
	// --- From here stack handling methods -----------------------------------
	// ------------------------------------------------------------------------
	/**
	 * Sets the stack size (must be >=20 and <1500).
	 * @param newStackSize the new stack size
	 */
	public void setStackSize(int newStackSize) {
		
		if (newStackSize<20) return;
		if (newStackSize>1500) newStackSize=1500;
		
		this.stackSize = newStackSize;
		try {
			AwbLogbackConfigurator.getEclipsePreferences().putInt("Console-StackSize", this.stackSize);
			AwbLogbackConfigurator.getEclipsePreferences().flush();
		} catch (BackingStoreException bsEx) {
			bsEx.printStackTrace();
		}
	}
	/**
	 * Returns the stack size to be used.
	 * @return the stack size
	 */
	public Integer getStackSize() {
		if (stackSize==null) {
			stackSize = AwbLogbackConfigurator.getEclipsePreferences().getInt("Console-StackSize", DEFAULT_STACK_SIZE);
		}
		return stackSize;
	}
	
}