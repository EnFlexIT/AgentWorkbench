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
package agentgui.logging.logfile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import agentgui.core.application.Application;
import agentgui.core.config.GlobalInfo;
import agentgui.logging.logfile.PrintStreamListener.PrintStreamListenerType;

/**
 * The Class LogFileWriter enables to redirect 
 * console output to a specified log file. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class LogFileWriter {

	private static final long CONSERVATION_PERIOD = 1000 * 60 *60 * 24 * 360;
	
	private static final String SYSTEM_OUTPUT = "[SysOut]";
	private static final String SYSTEM_ERROR  = "[SysErr]";
	
	private final String newLine = System.getProperty("line.separator");
	
	private SimpleDateFormat dateFormatter;
	private long nextMidnightTimeStamp;

	private FileWriter fileWriter;
	private BufferedWriter bufferedWriter;
	
	
	/**
	 * Instantiates a new log file writer that uses the log directory for the logging file.
	 */
	public LogFileWriter() {
		this.initialize();
		this.appendText(new PrintStreamListenerOutput(PrintStreamListenerType.SystemOutput, Application.getGlobalInfo().getVersionInfo().getVersionInfo()));
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		new SysOutScanner(this);
	}
	
	/**
	 * Returns the logging path and file name as string.
	 * @return the logging path and file name 
	 * @throws IOException 
	 */
	public String getLoggingFileName(long timeStamp) {

		try {

			String logPathByMonth = Application.getGlobalInfo().getLoggingPathByMonth(timeStamp, true);
			String dayPrefix = Application.getGlobalInfo().getLoggingDayPrefix(timeStamp);
			String processID = Application.getGlobalInfo().getProcessID();
			
			// --- Check, if the logging directory exists -----
			File logBasePathFile = new File(logPathByMonth);
			if (logBasePathFile.exists()==false || logBasePathFile.canWrite()==false) {
				return null;
			}
			return logPathByMonth + dayPrefix + "_" + processID + "_AgentGui.log";
			
		} catch (Exception ex) { 
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Starts the log file writer.
	 * @throws IOException 
	 */
	private void startFileWriter() throws IOException {
		String logFileName = this.getLoggingFileName(System.currentTimeMillis());
		if (logFileName==null) {
			System.err.println(Application.getGlobalInfo().getApplicationTitle() + "-Logging: Log file name was null. Thus, the log file writer will not be started.");
		} else {
			this.fileWriter = new FileWriter(logFileName, true);
			this.bufferedWriter = new BufferedWriter(this.fileWriter);
		}
	}
	/**
	 * Stop the log file writer.
	 * @throws IOException 
	 */
	public void stopFileWriter() {
		if (this.fileWriter!=null && this.bufferedWriter!=null) {
			try {
				this.bufferedWriter.flush();
				this.bufferedWriter.close();
				this.fileWriter.close();
				
				this.bufferedWriter = null;
				this.fileWriter = null;
				
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
	 * This method can be used in order to append the console output from a remote container
	 * @param pslOutput the PrintStreamListenerOutput
	 */
	public void appendText(PrintStreamListenerOutput pslOutput) {
		
		try {

			// --- Writing to a new log file? -----------------
			long timeStamp = System.currentTimeMillis();
			if (timeStamp>=this.nextMidnightTimeStamp) {
				// --- Next day, new log file! ----------------
				String newLogFile = this.getLoggingFileName(timeStamp);
				File logDirectory = new File(newLogFile).getParentFile();
				LogFileWriter.deleteOldLogFiles(timeStamp, logDirectory);
				
				this.stopFileWriter();
				this.startFileWriter();
				this.nextMidnightTimeStamp = GlobalInfo.getNextMidnightFromTimeStamp(System.currentTimeMillis());
			}

			// --- Get the new output line ----------------
			if (pslOutput.getLineOutput()==null) return;
			String newText = pslOutput.getLineOutput().trim();
			if (newText.equals("")) return;
			
			// --- Format the current system time ---------
			String timePrefix = this.getDateFormatter().format(new Date(System.currentTimeMillis())) + " ";
			switch (pslOutput.getPrintStreamListenerType()) {
			case SystemOutput:
				timePrefix += SYSTEM_OUTPUT + " ";
				break;
			case SystemError:
				timePrefix += SYSTEM_ERROR + " ";
				break;
			}
			newText = timePrefix + newText;
			
			// --- Handle new lines -----------------------
			if (newText.endsWith(this.newLine)==false) {
				newText = newText.replace("\r\n", "\n");
				newText = newText.replace("\r", "\n");
				newText = newText.replace("\n"+"\n", "\n");
				newText = newText.replace("\n", this.newLine + "                      ") + this.newLine;
			}

			// --- Start file writer if needed ------------
			if (this.bufferedWriter==null) startFileWriter();

			// --- Write to file --------------------------
			this.bufferedWriter.write(newText);
			this.bufferedWriter.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * Appends the specified text vector to the log file.
	 * @param stack the stack
	 */
	public void appendText(Vector<PrintStreamListenerOutput> stack) {
		for (int i = 0; i < stack.size(); i++) {
			this.appendText(stack.get(i));
		}
	}
	

	/**
	 * Deletes old log files. Based on the current date and its corresponding day prefix, the 
	 * files that are older than {@link #CONSERVATION_PERIOD} will be removed out of the corresponding 
	 * month directory  
	 *
	 * @param timeStamp the current time in milliseconds
	 * @param logDirectory the logging directory to search in
	 */
	public static void deleteOldLogFiles(long timeStamp, File logDirectory) {
		
		if (logDirectory.exists()==true) {
			// ------------------------------------------------------
			// --- Search for older files with current day prefix ---
			// ------------------------------------------------------
			final String oldFilePrefix = Application.getGlobalInfo().getLoggingDayPrefix(timeStamp);
			File[] oldLogs = logDirectory.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					boolean accept = name.startsWith(oldFilePrefix);
					if (accept==true) {
						// --- Check the date of the log file -------
						long lastMod = new File(dir, name).lastModified();
						if (lastMod + CONSERVATION_PERIOD > System.currentTimeMillis()) {
							accept = false;
						}
					}
					return accept;
				}
			});
			// ------------------------------------------------------
			
			// ------------------------------------------------------
			// --- Remove the files found ---------------------------
			// ------------------------------------------------------
			for (int i = 0; i < oldLogs.length; i++) {
				File oldLog = oldLogs[i]; 
				try {
					if (oldLog.delete()==false) {
						System.err.println("Agent.GUI-Logging: Log file '" + oldLog.getAbsolutePath() + "' could not be removed.");
					} else {
						System.out.println("Agent.GUI-Logging: Removed old log file '" + oldLog.getAbsolutePath() + "'.");
					}
					
				} catch (SecurityException se) {
					System.err.println("Agent.GUI-Logging: Log file removement failed: " + se.getMessage());
				} catch (Exception ex) {
					System.err.println("Agent.GUI-Logging: Log file removement failed: " + ex.getMessage());
				}
			}
			// ------------------------------------------------------
		}
		
	}
	
}
