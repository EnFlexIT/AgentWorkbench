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
	private long conservationPeriod = 1000 * 60 *60 * 24 * 360;
	
	private String loggingBasePath; 
	
	private SimpleDateFormat dateFormatter;
	private long nextMidnightTimeStamp;

	private FileWriter fileWriter;
	private BufferedWriter bufferedWriter;
	
	private boolean logActive = true;
	
	
	/**
	 * Instantiates a new log file writer that uses the log directory for the logging file.
	 */
	public LogFileWriter() {
		this.initialize();
	}
	/**
	 * Instantiates a new log file writer that uses the log directory for the logging file.
	 * @param loggingBasePath the logging base path
	 */
	public LogFileWriter(String loggingBasePath) {
		this.setLoggingBasePath(loggingBasePath);
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		new SysOutScanner(this);
	}
	/**
	 * Sets the logging base path.
	 * @param newLoggingBasePath the new logging base path
	 */
	private void setLoggingBasePath(String newLoggingBasePath) {
		if (newLoggingBasePath!=null && newLoggingBasePath.trim().equals("")==false) {
			this.loggingBasePath = newLoggingBasePath;	
		}
	}
	/**
	 * Returns the logging base path for the Agent.GUI logging. Within Windows and other systems, the
	 * local local directory './log' will be used. Within Linux the folder '/var/log/agentgui'
	 * will be returned and used. 
	 * 
	 * @return the logging base path
	 */
	private String getLoggingBasePath() {
		if (loggingBasePath==null) {
			// --- The default case -------------
			loggingBasePath = "./log";
			String os = System.getProperty("os.name");
			if (os.toLowerCase().contains("windows")==true) {
				// --- nothing to do here ---
			} else if (os.toLowerCase().contains("linux")==true) {
				loggingBasePath = "/var/log/agentgui";
			}
			loggingBasePath = loggingBasePath.replace("/", File.separator);	
		}
		return loggingBasePath;
	}
	/**
	 * Returns the logging path and file name as string.
	 * @return the logging path and file name 
	 * @throws IOException 
	 */
	public String getLoggingFileName(long timeStamp) {

		// --- Check, if the logging directory exists -----
		String logBasePath = this.getLoggingBasePath();
		String monthDescriptor = this.getMonthDescriptor(timeStamp);
		try {

			File logBasePathFile = new File(logBasePath);
			if (logBasePathFile.exists()==false && this.creatrDirectory(logBasePathFile)==false) {
				logActive = false;
				return null;
			}
			
			File logMonPathFile = new File(logBasePath  + File.separator + monthDescriptor); 
			if (logMonPathFile.exists()==false && this.creatrDirectory(logMonPathFile)==false) {
				logActive = false;
				return null;
			}
			
			return logBasePath + File.separator + monthDescriptor + File.separator + this.getTimeStampPrefix(timeStamp) + "_" + this.getPID() + "_AgentGui.log";
			
		} catch (Exception ex) { 
			ex.printStackTrace();
		}
		return null;
	}
	/**
	 * Gets the time stamp prefix for the log file.
	 * @return the time stamp prefix
	 */
	private String getTimeStampPrefix(long timeStamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		return "DAY_" + sdf.format(new Date(timeStamp));
	}
	/**
	 * Gets the month descriptor.
	 * @return the month descriptor
	 */
	private String getMonthDescriptor(long timeStamp) {
		Date date = new Date(timeStamp);
		return new SimpleDateFormat("MM").format(date) + "_" + new SimpleDateFormat("MMM").format(date);		
	}
	/**
	 * Returns the local process ID (PID).
	 * @return the PID
	 */
	private String getPID() {
		String localProcessName = ManagementFactory.getRuntimeMXBean().getName();
		String pidChar = localProcessName.substring(0, localProcessName.indexOf("@"));
		return "PID_" + pidChar;
	}
	/**
	 * Tries to create the specified directory.
	 * @return true, if successful
	 */
	private boolean creatrDirectory(File dirToCreate) throws IOException {
		
		boolean dirCreated = false;
		if (dirToCreate!=null) {
			
			try {
				dirCreated = dirToCreate.mkdir();
				if (dirCreated ==false) {
					throw new IOException("Agent.GUI-Logging: Directory creation failed for '" + dirToCreate.toString() + "'");
				}

			} catch (Exception ex) {
				throw new IOException("Agent.GUI-Logging: Directory creation failed for. Unsufficient permissions for "+ dirToCreate.toString() +"? File logging will be turned off.", ex);
			}
			
		}
		return dirCreated;
	}
	
	/**
	 * Start file writer.
	 * @throws IOException 
	 */
	private void startFileWriter() throws IOException {
		String logFileName = this.getLoggingFileName(System.currentTimeMillis());
		if (logFileName==null) {
			System.err.println("Agent.GUI-Logging: Log file name was null. Thus, the log file writer will not be started.");
		} else {
			this.fileWriter = new FileWriter(logFileName, true);
			this.bufferedWriter = new BufferedWriter(this.fileWriter);
		}
	}
	/**
	 * Stop file writer.
	 * @throws IOException 
	 */
	public void stopFileWriter() {
		if (this.fileWriter!=null && this.bufferedWriter!=null) {
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
	 * Deletes old log files. Based on the current date and its corresponding day prefix, the 
	 * files that are older than {@link #conservationPeriod} will be removed out of the corresponding 
	 * month directory  
	 * 
	 * @param timeStamp the current time in milliseconds
	 */
	private void deleteOldLogFiles(long timeStamp) {
		
		String newLogFile = this.getLoggingFileName(timeStamp);
		File logDirectory = new File(newLogFile).getParentFile();
		if (logDirectory.exists()==true) {
			
			// ------------------------------------------------------
			// --- Search for older files with current day prefix ---
			// ------------------------------------------------------
			final String oldFilePrefix = this.getTimeStampPrefix(timeStamp);
			File[] oldLogs = logDirectory.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					boolean accept = name.startsWith(oldFilePrefix);
					if (accept==true) {
						// --- Check the date of the log file -------
						long lastMod = new File(dir, name).lastModified();
						if (lastMod + conservationPeriod > System.currentTimeMillis()) {
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
	private long getNextMidnightFromTimeStamp(long timeStamp) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeStamp);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}
	
	/**
	 * This method can be used in order to append the console output from a remote container
	 * @param text the text to print out
	 */
	public void appendText(String text) {
		
		if (logActive==false)return;
		try {

			// --- Writing to a new log file? -----------------
			if (System.currentTimeMillis()>=this.nextMidnightTimeStamp) {
				// --- Next day, new log file! ----------------
				this.deleteOldLogFiles(System.currentTimeMillis());
				this.stopFileWriter();
				this.startFileWriter();
				this.nextMidnightTimeStamp = this.getNextMidnightFromTimeStamp(System.currentTimeMillis());
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

			// --- Start file writer if needed ------------
			if (this.bufferedWriter==null) startFileWriter();

			// --- Write to file --------------------------
			this.bufferedWriter.write(newText);
			this.bufferedWriter.flush();
			
		} catch (Exception e) {
			logActive=false;
			e.printStackTrace();
		}
		
	}
	/**
	 * Appends the specified text vector to the log file.
	 * @param stack the stack
	 */
	public void appendText(Vector<String> stack) {
		for (int i = 0; i < stack.size(); i++) {
			this.appendText(stack.get(i));
		}
	}
	
	
}
