package de.enflexit.oshi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * The Class OshiBundleActivator basically configures the Logging.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
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
		
		// --- Introduced due a bug under Mac OS --------------------
		if (! (LoggerFactory.getILoggerFactory() instanceof LoggerContext)) return;
		
		// --- Configure the logger ---------------------------------
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		JoranConfigurator jc = new JoranConfigurator();
		jc.setContext(context);
		context.reset();

		// +++ TODO overriding the log directory property programmatically
		String logDirProperty = null;
		context.putProperty("LOG_DIR", logDirProperty);
		
		// --- Extract internal configuration file ------------------
		// +++
		// +++ TODO May be reactivated if a decision for the file location has been made
		// +++
//		if (this.isExternalLogbackFileAvailable(bundle)==false) {
//			this.extractFromBundle(this.getInternalLogbackFileURL(bundle), this.getExternalLogbackFile(bundle));
//		}
//		
//		// --- Check if configuration file is available now ---------   
//		if (this.isExternalLogbackFileAvailable(bundle)==true) {
//			// --- Open external logback.xml ------------------------
//			jc.doConfigure(this.getExternalLogbackFile(bundle).getAbsolutePath());	
//		} else {
			// --- This takes the logback.xml from the bundle root --
			URL logbackConfigFileUrl = this.getInternalLogbackFileURL(bundle);
			jc.doConfigure(logbackConfigFileUrl.openStream());
//		}
		
	}
	
	/**
	 * Checks if the external logback configuration file is available.
	 * @return true, if the file is available
	 */
	@SuppressWarnings("unused")
	private boolean isExternalLogbackFileAvailable(Bundle bundle) {
		File logbackFile = this.getExternalLogbackFile(bundle);
		return logbackFile.exists();
	}
	
	/**
	 * Gets the logback file.
	 * @return the logback file
	 */
	private File getExternalLogbackFile(Bundle bundle) {
		return new File(this.getExternalLogbackFileLocationPath(bundle));
	}
	
	/**
	 * Returns the logback file location.
	 * @return the logback file location
	 */
	private String getExternalLogbackFileLocationPath(Bundle bundle) {
	    String location = LOGBACK_CONFIG_FILE_NAME;
	    return location;
	}
	
	/**
	 * Returns the internal logback file URL.
	 * @param bundle the bundle
	 * @return the internal logback file URL
	 */
	private URL getInternalLogbackFileURL(Bundle bundle) {
		return bundle.getResource(LOGBACK_CONFIG_FILE_NAME);
	}
	
	/**
	 * Extracts an internal file from the bundle to the specified destination.
	 *
	 * @param internalFileURL the internal file URL
	 * @param destinationFile the destination file
	 */
	@SuppressWarnings("unused")
	private void extractFromBundle(URL internalFileURL, File destinationFile) {

		InputStream is = null;
		FileOutputStream fos = null;
		try {
			if (internalFileURL!=null) {
				// --- Write file to directory ------------
				is = internalFileURL.openStream();
				fos = new FileOutputStream(destinationFile);
				byte[] buffer = new byte[1024];
				int len;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				
			} else {
				// --- Could not find fileURL -------------
				System.err.println(this.getClass().getSimpleName() + ": Internal file URL was not defined.");
			}
			
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		} finally {
			try {
				if (fos!=null) fos.close();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
			try {
				if (is!=null) is.close();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
		}
		
	}
	
}
