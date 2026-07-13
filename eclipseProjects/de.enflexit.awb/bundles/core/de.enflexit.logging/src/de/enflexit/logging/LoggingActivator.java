package de.enflexit.logging;

import java.io.IOException;
import java.nio.file.Path;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.Reporter;

import ch.qos.logback.classic.spi.LogbackServiceProvider;
import ch.qos.logback.core.joran.spi.JoranException;
import de.enflexit.logging.PropertyContentProvider.FileToProvide;
import de.enflexit.logging.console.ConsoleScanner;

/**
 * The Class LoggingActivator configures the SLF4J for the usage of 
 * Logback in the current OSGI-envornment.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class LoggingActivator implements BundleActivator {

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		this.startConsoleScanner();
		this.doLogbackInitialization();
		this.doLogbackConfiguration();
		this.doHibernateLoggerConfiguration();
	}
	
	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		// --- Nothing to do here -----
	}
	
	/**
	 * Do logback initialization.
	 */
	private void doLogbackInitialization () {
		
		// --- Set output level or slf4j-Reporter -------------------
		System.setProperty(Reporter.SLF4J_INTERNAL_VERBOSITY_KEY, "Error");
		// --- Set LogbackServiceProvider as slf4j-Provider ---------
		System.setProperty(LoggerFactory.PROVIDER_PROPERTY_KEY, LogbackServiceProvider.class.getName());
		// --- Initialize Logging -----------------------------------
		LoggerFactory.getLogger(LoggingActivator.class);
	}
	
	
	/**
	 * Configure logback in bundle.
	 *
	 * @param bundle the bundle
	 * @throws JoranException the joran exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void doLogbackConfiguration() throws JoranException, IOException {
		
		// --- Check if configuration file is available now ---------   
		Path logbackXmlFile = getExternalLogbackPath();
		if (logbackXmlFile.toFile().exists()==false) {
			// --- Extract internal configuration file ------------------
			PropertyContentProvider pcp = new PropertyContentProvider(PathHandling.getPropertiesPath(true).toFile());
			pcp.checkAndProvidePropertyContent(FileToProvide.LOGBACK_CONFIGURATION);
		}
		AwbLogbackConfigurator.loadConfiguration(logbackXmlFile);
	}
	/**
	 * Returns the logback file location.
	 * @return the logback file location
	 */
	private Path getExternalLogbackPath() {
		Path pathProperties = PathHandling.getPropertiesPath(true);
	    return pathProperties.resolve(FileToProvide.LOGBACK_CONFIGURATION.toString());
	}

	/**
	 * Do a manual logger configuration as well.
	 */
	private void doHibernateLoggerConfiguration() {

		// --- Set level of hibernate logging output ------ 
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.WARNING);

		// --- Set level of C3P0 logging output -----------
		System.setProperty("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
		System.setProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "WARNING");
	}
	
	/**
	 * Starts the AWB {@link ConsoleScanner}.
	 */
	private void startConsoleScanner() {
		ConsoleScanner.getInstance();
	}
	
}
