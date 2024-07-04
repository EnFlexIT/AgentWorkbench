package de.enflexit.logging;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.Reporter;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.LogbackServiceProvider;
import ch.qos.logback.core.joran.spi.JoranException;
import de.enflexit.logging.PropertyContentProvider.FileToProvide;

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
		this.doLogbackInitialization();
		this.doLogbackConfiguration(context.getBundle());
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
	private void doLogbackConfiguration(Bundle bundle) throws JoranException, IOException {
		
		// --- Introduced due a bug under Mac OS --------------------
		if (!(LoggerFactory.getILoggerFactory() instanceof LoggerContext)) return;
		
		// --- Configure the logger ---------------------------------
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		
		// --- Create JoranConfigurator -----------------------------
		JoranConfigurator jc = new JoranConfigurator();
		jc.setContext(context);
		context.reset();

		// --- Overwrite log directory property programmatically ----
		context.putProperty("LOG_DIR", PathHandling.getLoggingFilesBasePathDefault().toString());
		
		// --- Check if configuration file is available now ---------   
		File logbackXmlFile = getExternalLogbackPath().toFile();
		if (logbackXmlFile.exists()==true) {
			// --- Open external logback.xml ------------------------
			jc.doConfigure(logbackXmlFile.getAbsolutePath());	
		} else {
			// --- Extract internal configuration file ------------------
			PropertyContentProvider pcp = new PropertyContentProvider(PathHandling.getPropertiesPath(true).toFile());
			pcp.checkAndProvidePropertyContent(FileToProvide.LOGBACK_CONFIGURATION);
			// --- This takes the logback.xml from the bundle root --
			URL logbackConfigFileUrl = getInternalLogbackFileURL();
			jc.doConfigure(logbackConfigFileUrl.openStream());
		}
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
	 * Returns the internal logback file URL.
	 * @return the internal logback file URL
	 */
	private URL getInternalLogbackFileURL() {
		String bundleFile = (PathHandling.getPropertiesPath(false) + FileToProvide.LOGBACK_CONFIGURATION.toString()).replace("\\", "/");
		Bundle bundle = FrameworkUtil.getBundle(LoggingActivator.class);
		return bundle.getResource(bundleFile);
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
	
}
