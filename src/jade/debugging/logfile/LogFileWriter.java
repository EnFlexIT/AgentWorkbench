/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package jade.debugging.logfile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

/**
 * The Class LogFileWriter enables to redirect 
 * console output to a specified log file. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class LogFileWriter {

	private final String newLine = System.getProperty("line.separator");
	
	private SimpleDateFormat dateFormatter;
	private long nextMidnightTimeStamp;

	private FileWriter fileWriter;
	private BufferedWriter bufferedWriter;
	
	private boolean logActive = true;
	
	
	/**
	 * Instantiates a new log file writer that uses the
	 * log directory for the logging file.
	 */
	public LogFileWriter() {
		new SysOutScanner(this);	
	}
	
	/**
	 * Returns the logging path for the Agent.GUI logging. Within Windows and other systems, the
	 * local local directory './log' will be used. Within Linux the folder '/var/log/agentgui'
	 * will be returned and used. 
	 * 
	 * @return the logging path
	 */
	private String getLoggingPath() {
		
		String logPath = "./log";
		String os = System.getProperty("os.name");
		
		if (os.toLowerCase().contains("windows")==true) {
			// --- nothing to do here ---
		} else if (os.toLowerCase().contains("linux")==true) {
			logPath = "/var/log/agentgui";
		}
		return logPath;
	}
	/**
	 * Gets the time stamp prefix for the log file.
	 * @return the time stamp prefix
	 */
	private String getTimeStampPrefix() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date(System.currentTimeMillis()));
	}
	/**
	 * Returns the local process ID (PID).
	 * @return the PID
	 */
	private Integer getPID() {
		// --- Determine the local process ID (PID) -------
		String localProcessName = ManagementFactory.getRuntimeMXBean().getName();
		String pidChar = localProcessName.substring(0, localProcessName.indexOf("@"));
		return Integer.parseInt(pidChar);
	}
	/**
	 * Returns the logging path and file name as string.
	 * @return the logging path and file name 
	 * @throws IOException 
	 */
	private String getLoggingFileName() throws IOException {
//		System.out.println("1");

		// --- Check, if the logging directory exists -----
		File logPath = new File(this.getLoggingPath());
//		System.out.println("2");

		try {
//			System.out.println("3");

			if (logPath.exists()==false) {
//				System.out.println("4");

				try {

					if (!logPath.mkdir()){
						throw new IOException("Logfile path creation failed");
					}
//					System.out.println("6");

				} catch (Exception ex) {
//					System.out.println("heylo");
					IOException newEx = new IOException("Creation of logfile path failed. Unsufficient permissions at "+logPath.toString()+"? Logging will be turned off.", ex);
					logActive = false;
					throw newEx;
				}
			}
			return this.getLoggingPath() + "/" + this.getTimeStampPrefix() + "_" + this.getPID() + "_AgentGui.log";
		} catch (IOException ex) { // pass the usual exception on
			throw ex;
		} catch (Exception ex) { // catch everything else
			ex.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Start file writer.
	 * @throws IOException 
	 */
	private void startFileWriter() throws IOException{
		this.fileWriter = new FileWriter(this.getLoggingFileName(), true);
		this.bufferedWriter = new BufferedWriter(this.fileWriter);
	}
	/**
	 * Stop file writer.
	 * @throws IOException 
	 */
	public void stopFileWriter() {
		if (this.fileWriter!=null && bufferedWriter!=null) {
			try {
				this.bufferedWriter.flush();
				this.bufferedWriter.close();
				this.fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	/**
	 * Returns the local date formatter.
	 * @return the date formatter
	 */
	private SimpleDateFormat getDateFormatter() {
		if (dateFormatter==null) {
			dateFormatter = new SimpleDateFormat("HH:mm:ss,SSS");
		}
		return dateFormatter;
	}
	
	/**
	 * Gets the next midnight time stamp.
	 * @return the next midnight time stamp
	 */
	private long getNextMidnightTimestamp() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}
	
	/**
	 * This method can be used in order to append the console output from a remote container
	 * @param text the text to print out
	 */
	public void appendText(String text) {
		if(!logActive){
			return;
		}
		try {

			// --- Writing to a new log file? -----------------
			if (System.currentTimeMillis()>=this.nextMidnightTimeStamp) {
				this.stopFileWriter();
				this.startFileWriter();
				this.nextMidnightTimeStamp = this.getNextMidnightTimestamp();
			}
			
			// --- Format the current system time ------------- 
			String prefix = this.getDateFormatter().format(new Date(System.currentTimeMillis())) + " ";
			String newText = text;
			if (newText.startsWith(PrintStreamListener.SystemOutput)) {
				newText = newText.substring(PrintStreamListener.SystemOutput.length()).trim();
				prefix += PrintStreamListener.SystemOutput + " ";
			} else if (text.startsWith(PrintStreamListener.SystemError)) {
				newText = newText.substring(PrintStreamListener.SystemError.length()).trim();
				prefix += PrintStreamListener.SystemError + " ";
			}
	
			if (newText==null || newText.equals("")) {
				return;
			} else if (newText.endsWith(this.newLine)==false) {
				newText = newText.replace("\r\n", "\n");
				newText = newText.replace("\r", "\n");
				newText = newText.replace("\n"+"\n", "\n");
				newText = prefix + newText.replace("\n", this.newLine + "                      ") + newLine;
			}

			this.bufferedWriter.write(newText);
			this.bufferedWriter.flush();
			
		} catch (Exception e) {
			logActive=false;
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
