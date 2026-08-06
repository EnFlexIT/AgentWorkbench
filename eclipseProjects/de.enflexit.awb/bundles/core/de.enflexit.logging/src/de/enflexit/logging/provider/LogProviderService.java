package de.enflexit.logging.provider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * The Interface LogProviderService
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public interface LogProviderService {

	/**
	 * Returns the type.
	 *
	 * @return the type
	 */
	public LogProviderType getType();
   /**
    * Should return a list containing the names of all available log files
    *
    * @return the available logs
    */
   public List<String> getAvailableLogs();


   /**
    * Returns the logs between.
    *
    * @param from the from
    * @param to the to
    * @return the logs between
    * @throws IOException Signals that an I/O exception has occurred.
    */
   public List<LogFileInfo> getLogsBetween(String from, String to) throws IOException;

   /**
    * Should delete the specified files if the service produced temporary files.
    *
    * @param filesToCleanUp the files to clean up
    */
   public void cleanUp(List<Path> filesToCleanUp);
}
