package de.enflexit.logging.provider;

import java.util.List;
import java.util.Objects;

import de.enflexit.logging.AwbLogbackConfigurator;

/**
 * A factory for creating LogProviderService objects.
 */
public class LogProviderHelper {

	/**
	 * Returns the appropriate log provider.
	 *
	 * @return the log provider
	 */
	public static LogProviderService getLogProvider() {

	    List<LogProviderService> logProviderServices = ServiceFinder.findServices(LogProviderService.class);

	    LogProviderService fileService = null;
	    LogProviderService dbService = null;
	    LogProviderService noOpService = null;

	    for (LogProviderService lps : logProviderServices) {

	        switch (lps.getType()) {
	        case FILE:
	            fileService = lps;
	            break;
	        case DATABASE:
	            dbService = lps;
	            break;
	        case NO_OP:
	            noOpService = lps;
	            break;
	        }
	    }
	    
	    if (fileService != null && AwbLogbackConfigurator.isFileLoggingEnabled()) {
	        return fileService;
	    }
	    if (dbService != null && AwbLogbackConfigurator.isDbLoggingEnabled()) {
	        return dbService;
	    }
	    return Objects.requireNonNull(noOpService, "No NoOpLogProviderService found");
	}
	
}