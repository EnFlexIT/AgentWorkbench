package de.enflexit.logging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import de.enflexit.logging.appender.AwbRollingFileAppender;
import de.enflexit.logging.provider.LogFileInfo;
import de.enflexit.logging.provider.LogProviderService;
import de.enflexit.logging.provider.LogProviderType;

/**
 * The Class FileLogProviderService provides access to log files created by
 * the AwbRollingFileAppender or it's asynchronous wrapper.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class FileLogProviderService implements LogProviderService{
	
	private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{4}-\\d{2}-\\d{2})");
	
	/* (non-Javadoc)
	* @see de.enflexit.logging.provider.LogProviderService#getAvailableLogs()
	*/
	@Override
	public List<String> getAvailableLogs() {
		
		List<String> availableLogs = new ArrayList<>();
		
		for (Path path : this.getLogFilesFromDefaultDirectory()) {
			LocalDate fileDate = this.extractDate(path);
			if (fileDate != null) {
				availableLogs.add(fileDate.toString());
			}
		}
		// --- Current log file has a different name, has to be added manually ----------
		if (AwbRollingFileAppender.getCurrentLogFile() != null) {
			availableLogs.add(LocalDate.now().toString());
		}
		return availableLogs;
	}
	
	/**
	 * Returns all log files from the default log directory.
	 *
	 * @return the the absolute path to every file ending with .log
	 */
	private List<Path> getLogFilesFromDefaultDirectory() {
		
		Path logDirectory = PathHandling.getLoggingFilesBasePathDefault();
		List<Path> logFiles = new ArrayList<>();

		try (Stream<Path> paths = Files.walk(logDirectory)) {
			
			for (Path path : paths.toList()) {
				if (Files.isRegularFile(path) && path.getFileName().toString().endsWith(".log")) {
					logFiles.add(path);
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return logFiles;
	}

	/* (non-Javadoc)
	* @see de.enflexit.logging.provider.LogProviderService#getLogsBetween(java.lang.String, java.lang.String)
	*/
	@Override
	public List<LogFileInfo> getLogsBetween(String from, String to) {
		
		LocalDate fromDate;
		LocalDate toDate;
		try {
			fromDate = LocalDate.parse(from);
			toDate = LocalDate.parse(to);
		} catch (DateTimeParseException dtpe) {
			dtpe.printStackTrace();
			return new ArrayList<LogFileInfo>();
		}
		
		List<LogFileInfo> logfiles = new ArrayList<>();
		for (Path logFile : this.getLogFilesFromDefaultDirectory()) {
			
			LocalDate date = this.extractDate(logFile);
			// --- If date is null, the file has an invalid name and should be ignored --
			if (date == null) {
				continue;
			}
			// --- Determine whether the date is >= from and <= to ----------------------
			if (date.isBefore(fromDate) == false && date.isAfter(toDate) == false) {
				String fileName = date.toString()+".log";
				logfiles.add(new LogFileInfo(fileName, logFile));
			}
		}
		// --- Add the current log file if todays' logs are requested -------------------
		if (LocalDate.now().isEqual(toDate) && AwbRollingFileAppender.getCurrentLogFile() != null) {
			logfiles.add(new LogFileInfo(LocalDate.now().toString() +".log", Paths.get(AwbRollingFileAppender.getCurrentLogFile())));
		}
		return logfiles;
	}
	
	/**
	 * Extract the date from the name of a log file.
	 *
	 * @param file the file
	 * @return the local date
	 */
	private LocalDate extractDate(Path file) {

	    String fileName = file.getFileName().toString();
	    Matcher matcher = DATE_PATTERN.matcher(fileName);

	    if (matcher.find() == false) {
	        return null;
	    }
	    return LocalDate.parse(matcher.group(1));
	}

	/* (non-Javadoc)
	* @see de.enflexit.logging.provider.LogProviderService#getType()
	*/
	@Override
	public LogProviderType getType() {
		return LogProviderType.FILE;
	}

	/* (non-Javadoc)
	* @see de.enflexit.logging.provider.LogProviderService#cleanUp(java.util.List)
	*/
	@Override
	public void cleanUp(List<Path> filesToCleanUp) {
		// --- Nothing to do here ---
	}

}