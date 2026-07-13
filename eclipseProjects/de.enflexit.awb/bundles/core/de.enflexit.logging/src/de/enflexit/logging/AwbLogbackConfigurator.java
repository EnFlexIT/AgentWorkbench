package de.enflexit.logging;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * The Class AwbLogbackConfigurator provides static methods to 
 * validate a logback.xml configuration and reload logback
 * with a new configuration.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class AwbLogbackConfigurator {

	/**
	 * Load logback configuration from specified path, including file 'logback.xml'.
	 *
	 * @param newConfig the new config
	 * @return true, if successful
	 * @throws JoranException the joran exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean loadConfiguration(Path newConfig) throws JoranException, IOException {
		
	// --- Introduced due a bug under Mac OS ------------------------
	if (!(LoggerFactory.getILoggerFactory() instanceof LoggerContext)) return false;
	
	// --- Configure the logger -------------------------------------
	LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
	
	// --- Create JoranConfigurator ---------------------------------
	JoranConfigurator jc = new JoranConfigurator();
	jc.setContext(context);
	context.reset();

	// --- Overwrite log directory property programmatically --------
	context.putProperty("LOG_DIR", PathHandling.getLoggingFilesBasePathDefault().toString());
	
	// --- Check if configuration file is available now -------------
	File logbackXmlFile = newConfig.toFile();
	if (logbackXmlFile.exists()==true) {
		// --- Apply configuration ----------------------------------
		jc.doConfigure(logbackXmlFile.getAbsolutePath());
		return true;
	}
	return false;
}
	
	/**
	 * Attempts to apply the configuration from the specified file in
	 * a temporary test context. 
	 *
	 * @param logbackFile2Validate the logback file to validate
	 * @return true, if the configuration was applied without throwing exceptions
	 */
	public static boolean isValidLogbackConfiguration(InputStream inputStream) {
		
		LoggerContext testContext = new LoggerContext();
		try {
			JoranConfigurator jc = new JoranConfigurator();
			jc.setContext(testContext);
			jc.doConfigure(inputStream);
			return true;

		} catch (JoranException joEx) {
			// --- Config threw JoranException, must be invalid -----
			return false;

		} finally {
			// --- Clean-up -----------------------------------------
			testContext.stop();
		}
		
	}
}