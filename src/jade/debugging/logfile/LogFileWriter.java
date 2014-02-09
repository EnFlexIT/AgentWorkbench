package jade.debugging.logfile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/**
 * The Class LogFileWriter enables to redirect any 
 * console output to a specified log file. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class LogFileWriter {

	private final String newLine = System.getProperty("line.separator");
	
	private String logFile = null;
	private FileWriter fileWriter = null;
	private BufferedWriter bufferedWriter = null;
	
	
	/**
	 * Instantiates a new log file writer.
	 */
	public LogFileWriter(String logFile) {
		this.logFile = logFile;
		this.startFileWriter();
		new SysOutScanner(this);
	}
	
	
	/**
	 * Start file writer.
	 */
	private void startFileWriter(){
		try {
			fileWriter = new FileWriter(this.logFile);
			bufferedWriter = new BufferedWriter(fileWriter);
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Stop file writer.
	 */
	public void stopFileWriter() {
		try {
			bufferedWriter.flush();
			bufferedWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method can be used in order to append the console output from a remote container
	 * @param text the text to print out
	 */
	public void appendText(String text) {
		
		String newText = text;
		if (newText.startsWith(PrintStreamListener.SystemOutput)) {
			newText = newText.substring(PrintStreamListener.SystemOutput.length()).trim();
		} else if (text.startsWith(PrintStreamListener.SystemError)) {
			newText = newText.substring(PrintStreamListener.SystemError.length()).trim();
		}

		if (newText==null || newText.equals("")) {
			return;
		} else if (newText.endsWith(newLine)==false) {
			newText = newText + newLine;
		}
		
		try {
			bufferedWriter.write(newText);
			bufferedWriter.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Append text to the log file.
	 * @param stack the stack
	 */
	public void appendText(Vector<String> stack) {
		for (int i = 0; i < stack.size(); i++) {
			this.appendText(stack.get(i));
		}
	}
	
}
