package de.enflexit.logging;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import javax.sql.DataSource;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.db.DataSourceConnectionSource;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusManager;
import de.enflexit.logging.PropertyContentProvider.FileToProvide;
import de.enflexit.logging.appender.AwbDatabaseAppender;

/**
 * The Class AwbLogbackConfigurator provides static methods to 
 * validate a logback.xml configuration and reload logback
 * with a new configuration.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class AwbLogbackConfigurator {

	private static IEclipsePreferences eclipsePreferences;
	
	
	/**
	 * Load configuration from default location
	 *
	 * @return true, if successful
	 * @throws JoranException the joran exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean loadConfiguration() throws JoranException, IOException {
		// --- Check if configuration file is available now ---------   
		Path logbackXmlFile = getExternalLogbackPath();
		if (logbackXmlFile.toFile().exists()==false) {
			// --- Extract internal configuration file ------------------
			PropertyContentProvider pcp = new PropertyContentProvider(PathHandling.getPropertiesPath(true).toFile());
			pcp.checkAndProvidePropertyContent(FileToProvide.LOGBACK_CONFIGURATION);
		}
		return loadConfiguration(logbackXmlFile);
	}
	
	/**
	 * Load logback configuration from specified path, including file 'logback.xml'.
	 *
	 * @param newConfig the new config
	 * @return true, if successful
	 * @throws JoranException the joran exception
	 * @throws IOException    Signals that an I/O exception has occurred.
	 */
	public static boolean loadConfiguration(Path newConfig) throws JoranException, IOException {

		// --- Introduced due a bug under Mac OS ------------------------------
		if (!(LoggerFactory.getILoggerFactory() instanceof LoggerContext))
			return false;

		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		
		/* Because the DatabaseAppender is configured programmatically (not in logback.xml),
		 * it has to be restarted manually when a new config is loaded */
		boolean isRestartDbAppender = AwbDatabaseAppender.getInstance().isStarted();
		
		// --- Create JoranConfigurator ---------------------------------------
		JoranConfigurator jc = new JoranConfigurator();
		jc.setContext(context);
		context.reset();

		// --- Overwrite log directory property programmatically --------------
		context.putProperty("LOG_DIR", PathHandling.getLoggingFilesBasePathDefault().toString());
		
		// --- Check if configuration file is available now -------------------
		File logbackXmlFile = newConfig.toFile();
		if (logbackXmlFile.exists() == true) {
			// --- Apply configuration ----------------------------------------
			jc.doConfigure(logbackXmlFile.getAbsolutePath());
			if (isRestartDbAppender == true) {
				startAwbDatabaseAppender(AwbDatabaseAppender.getInstance().getDataSource());
			}
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
			
			// --- Check if logback registered errors without throwing exception --------
			StatusManager statusManager = testContext.getStatusManager();
			for (Status status : statusManager.getCopyOfStatusList()) {
				if (status.getLevel() == Status.ERROR) {
					return false;
				}
			}
		    return true;

		} catch (JoranException joEx) {
			// --- Config threw JoranException, must be invalid -------------------------
			return false;

		} finally {
			// --- Clean-up -------------------------------------------------------------
			testContext.stop();
		}
	}	
	
	/**
	 * Start awb database appender with the specified data source
	 *
	 * @param dataSource the data source connection source
	 */
	public static void startAwbDatabaseAppender(DataSource dataSource) {
		
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		
		// --- Clean up to avoid multiple registrations ---------------------------------
		Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
		Appender<ILoggingEvent> existingAsyncDB = rootLogger.getAppender(AwbDatabaseAppender.ASYNC_WRAPPER_NAME);
		if (existingAsyncDB != null) {
			rootLogger.detachAppender(existingAsyncDB);
			existingAsyncDB.stop();
		}
		
		// --- Configure connection source ----------------------------------------------
		DataSourceConnectionSource connectionSource = new DataSourceConnectionSource();
		connectionSource.setContext(context);
		connectionSource.setDataSource(dataSource);
		connectionSource.start();
		
		// --- Configure AwbDatabaseAppender --------------------------------------------
		AwbDatabaseAppender.getInstance().setName(AwbDatabaseAppender.NAME);
		AwbDatabaseAppender.getInstance().setConnectionSource(connectionSource);
		AwbDatabaseAppender.getInstance().setContext(context);
		AwbDatabaseAppender.getInstance().setWriteToLoggingStorage(true);
		
		// --- Configure asyncAppender --------------------------------------------------
		if (AwbDatabaseAppender.getInstance().isStarted() == true) {
			AsyncAppender asyncAppender = new AsyncAppender();
			asyncAppender.setName(AwbDatabaseAppender.ASYNC_WRAPPER_NAME);
			asyncAppender.setContext(context);
			asyncAppender.addAppender(AwbDatabaseAppender.getInstance());
			asyncAppender.start();
			
			rootLogger.addAppender(asyncAppender);
		}
		// --- Save the dataSource for re-use in case a new logback.xml is loaded -------
		AwbDatabaseAppender.getInstance().setDataSource(dataSource);
	}
	
	/**
	 * Returns the logback file location.
	 * @return the logback file location
	 */
	private static Path getExternalLogbackPath() {
		Path pathProperties = PathHandling.getPropertiesPath(true);
	    return pathProperties.resolve(FileToProvide.LOGBACK_CONFIGURATION.toString());
	}	

	/**
	 * Checks if file logging is enabled.
	 *
	 * @return true, if FILE_APPENDER or ASYNC_FILE_APPENDER is started
	 */
	public static boolean isFileLoggingEnabled() {
		
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger rootLogger = context.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
		Appender<?> fileAppender = rootLogger.getAppender("FILE_APPENDER");
		if (fileAppender != null) {
			return fileAppender.isStarted();
		}
		fileAppender = rootLogger.getAppender("ASYNC_FILE_APPENDER");
		return fileAppender != null && fileAppender.isStarted() == true;
	}
	
	/**
	 * Checks if is db logging enabled.
	 *
	 * @return true, if AwbDatabaseAppender is started
	 */
	public static boolean isDbLoggingEnabled() {
		return AwbDatabaseAppender.getInstance().isStarted();
	}
	
	/**
	 * Returns the local bundle.
	 * @return the bundle
	 */
	private static Bundle getBundle() {
		return FrameworkUtil.getBundle(AwbLogbackConfigurator.class);
	}
	/**
	 * Returns the current eclipse preferences.
	 * @return the eclipse preferences
	 */
	public static IEclipsePreferences getEclipsePreferences() {
		if (eclipsePreferences==null) {
			IScopeContext iScopeContext = ConfigurationScope.INSTANCE;
			eclipsePreferences = iScopeContext.getNode(getBundle().getSymbolicName());
		}
		return eclipsePreferences;
	}
	
}