package de.enflexit.oshi;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * The Class OshiBundleActivator configures the Logging.
 */
public class OshiBundleActivator implements BundleActivator {

	private static final String LOGBACK_CONFIG_FILE_NAME = "logback.xml";
	
	
	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		this.configureLogbackInBundle(context.getBundle());
	}

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
	}

	/**
	 * Configure logback in bundle.
	 *
	 * @param bundle the bundle
	 * @throws JoranException the joran exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void configureLogbackInBundle(Bundle bundle) throws JoranException, IOException {
		
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		JoranConfigurator jc = new JoranConfigurator();
		jc.setContext(context);
		context.reset();

		// TODO overriding the log directory property programmatically
		String logDirProperty = null;
		context.putProperty("LOG_DIR", logDirProperty);

		if (this.isLogbackFileAvailable()==false) {
			// --- This takes the logback.xml from the bundle root --
			URL logbackConfigFileUrl = FileLocator.find(bundle, new Path(LOGBACK_CONFIG_FILE_NAME),null);
			jc.doConfigure(logbackConfigFileUrl.openStream());
			
		} else {
			 // --- Open external logback.xml -----------------------
		    jc.doConfigure(this.getLobackFile().getAbsolutePath());	
		}
	}

	
	/**
	 * Checks if the external logback configuration file is available.
	 * @return true, if the file is available
	 */
	private boolean isLogbackFileAvailable() {
		File logbackFile = this.getLobackFile();
		return logbackFile.exists();
	}
	
	/**
	 * Gets the logback file.
	 * @return the logback file
	 */
	private File getLobackFile() {
		return new File(this.getLobackFileLocationPath());
	}
	
	/**
	 * Returns the logback file location.
	 * @return the logback file location
	 */
	private String getLobackFileLocationPath() {
		Location configurationLocation = Platform.getConfigurationLocation();
	    String location = configurationLocation.getURL().getPath() + "/" + LOGBACK_CONFIG_FILE_NAME;
	    return location;
	}
	
	
}
