package de.enflexit.logging.db;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import de.enflexit.logging.db.dataModel.LoggingEvent;
import de.enflexit.logging.db.dataModel.LoggingEventException;
import de.enflexit.logging.provider.LogFileInfo;
import de.enflexit.logging.provider.LogProviderService;
import de.enflexit.logging.provider.LogProviderType;

/**
 * The Class DatabaseLogProviderService.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class DatabaseLogProviderService implements LogProviderService {

	private DatabaseHandler dbHandler;
	
	/* (non-Javadoc)
	* @see de.enflexit.logging.provider.LogProviderService#getAvailableLogs()
	*/
	@Override
	public List<String> getAvailableLogs() {
		
		List<String> availableDates = new ArrayList<>();
		// --- Determine the dates of oldest and latest entry -----------------
		LocalDate oldestEntryDate = this.getDatabaseHandler().getOldestLogDate();
		LocalDate today = LocalDate.now();
		
		for (LocalDate day = oldestEntryDate; day.isAfter(today) == false; day = day.plusDays(1)) {
			
			long from = day.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
			long to = day.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
			
			if (this.getDatabaseHandler().hasLogsInBetween(from, to)) {
				availableDates.add(day.toString());
			}
		}
		return availableDates;
	}

	/* (non-Javadoc)
	* @see de.enflexit.logging.provider.LogProviderService#getLogsBetween(java.lang.String, java.lang.String)
	*/
	@Override
	public List<LogFileInfo> getLogsBetween(String from, String to) throws IOException {

		LocalDate fromDate;
		LocalDate toDate;
		try {
			// --- parse the date strings as LocalDate ------------------------
			fromDate = LocalDate.parse(from);
			toDate = LocalDate.parse(to);
		} catch (DateTimeParseException dtpe) {
			dtpe.printStackTrace();
			return null;
		}
		
		// --- startingTime is 00:00 at fromDate ------------------------------
		long startTime = fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		// --- endTime is 00:00 on the day after toDate (since inclusive)------
		long endTime = toDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		
		// --- Get the requested logging events -------------------------------
		List<LoggingEvent> logsBetweenFromAndTo = this.getDatabaseHandler().getLoggingEventsInBetween(startTime, endTime);
		List<LogFileInfo> logfiles = new ArrayList<>();

		if (logsBetweenFromAndTo == null || logsBetweenFromAndTo.size() == 0) {
			return logfiles;
		}
		
		// --- Order the events by unique dates -------------------------------
		Map<LocalDate, List<LoggingEvent>> eventsByDate = new TreeMap<>();
		for (LoggingEvent lEvent : logsBetweenFromAndTo) {
			
			LocalDate date = Instant.ofEpochMilli(lEvent.getTimestmp()).atZone(ZoneId.systemDefault()).toLocalDate();
			
			// --- Create a new list of logs for every unique date ------------
			eventsByDate.computeIfAbsent(date, ignored -> new ArrayList<LoggingEvent>()).add(lEvent);
		}
		
		for (Entry<LocalDate, List<LoggingEvent>> entry : eventsByDate.entrySet()) {
			
			// --- Create a temp file for every date entry --------------------
			String fileName = entry.getKey().toString() +".log";
			Path tempFile = Files.createTempFile(fileName, ".log");

			try (BufferedWriter writer = Files.newBufferedWriter(tempFile)) {

				for (LoggingEvent event : entry.getValue()) {
					// --- Write the formatted event to the file --------------
					writer.write(this.formatLogEvent(event));
					writer.newLine();
					
					if (event.getExceptions() != null) {
						// --- Add the Exception stack trace if present -------
						for (LoggingEventException exception : event.getExceptions()) {
							writer.write(exception.getTraceLine());
							writer.newLine();
						}
					}
				}
			}
			logfiles.add(new LogFileInfo(fileName, tempFile));
		}
		return logfiles;
	}

	/* (non-Javadoc)
	* @see de.enflexit.logging.provider.LogProviderService#getType()
	*/
	@Override
	public LogProviderType getType() {
		return LogProviderType.DATABASE;
	}
	
	/**
	 * Formats the LoggingEvent 
	 *
	 * @param event the event
	 * @return the log event as a formatted string
	 */
	private String formatLogEvent(LoggingEvent event) {

		String timestamp = Instant.ofEpochMilli(event.getTimestmp()).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

	    return String.format(
	            "%s [%s] %-5s %s - %s",
	            timestamp,
	            event.getThreadName(),
	            event.getLevelString(),
	            event.getLoggerName(),
	            event.getFormattedMessage());
	}

	/**
	 * Returns the database handler.
	 *
	 * @return the database handler
	 */
	private DatabaseHandler getDatabaseHandler() {
		if (dbHandler == null) {
			dbHandler = new DatabaseHandler();
		}
		return dbHandler;
	}

	/* (non-Javadoc)
	* @see de.enflexit.logging.provider.LogProviderService#cleanUp(java.util.List)
	*/
	@Override
	public void cleanUp(List<Path> filesToCleanUp) {

	    Path tempDir = Paths.get(System.getProperty("java.io.tmpdir")).toAbsolutePath().normalize();
	    
	    for (Path path : filesToCleanUp) {
	        try {
	            Path normalizedPath = path.toAbsolutePath().normalize();
	            // --- Check if the file is really a temp file ----------------
	            if (normalizedPath.startsWith(tempDir) == false) {
	            	continue;
	            }
	            Files.deleteIfExists(normalizedPath);

	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
	    }
	}

}