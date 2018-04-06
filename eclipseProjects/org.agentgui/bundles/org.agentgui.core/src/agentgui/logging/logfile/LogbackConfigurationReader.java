/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.logging.logfile;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.agentgui.PlugInActivator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.slf4j.LoggerFactory;

import agentgui.core.application.Application;
import agentgui.core.config.GlobalInfo;
import agentgui.core.config.PropertyContentProvider.FileToProvide;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;


/**
 * The Class LogbackConfigurationReader adjusts the configuration of logback
 * for the Agent.Workbench RCP application.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class LogbackConfigurationReader {

	/**
	 * Reads the configuration.
	 */
	public static void readConfiguration() {
		try {
			configureLogback();
			doManualLoggerConfiguration();
			
		} catch (JoranException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Do a manual logger configuration as well.
	 */
	private static void doManualLoggerConfiguration() {

		// --- Set level of hibernate logging output ------ 
		@SuppressWarnings("unused")
		org.jboss.logging.Logger logger = org.jboss.logging.Logger.getLogger("org.hibernate");
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.WARNING);

		// --- Set level of C3P0 logging output -----------
		System.setProperty("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
		System.setProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "WARNING");
		
	}
	
	/**
	 * Configure logback in bundle.
	 *
	 * @param bundle the bundle
	 * @throws JoranException the joran exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void configureLogback() throws JoranException, IOException {
		
		// --- Introduced due a bug under Mac OS --------------------
		if (!(LoggerFactory.getILoggerFactory() instanceof LoggerContext)) return;
		
		// --- Configure the logger ---------------------------------
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		JoranConfigurator jc = new JoranConfigurator();
		jc.setContext(context);
		context.reset();

		// --- Overwrite the log directory property programmatically
		String logDirProperty = Application.getGlobalInfo().getLoggingBasePath(true);
		context.putProperty("LOG_DIR", logDirProperty);
		
		// --- Extract internal configuration file ------------------
		boolean available = isExternalLogbackFileAvailable(); 
		if (available==false) {
			Application.getGlobalInfo().getPropertyContentProvider().checkAndProvidePropertyContent(FileToProvide.LOGBACK_CONFIGURATION);
		}
		
		// --- Check if configuration file is available now ---------   
		if (available==true) {
			// --- Open external logback.xml ------------------------
			jc.doConfigure(getExternalLogbackFile().getAbsolutePath());	
		} else {
			// --- This takes the logback.xml from the bundle root --
			URL logbackConfigFileUrl = getInternalLogbackFileURL();
			jc.doConfigure(logbackConfigFileUrl.openStream());
		}
		//StatusPrinter.printInCaseOfErrorsOrWarnings(context);
	}
	
	/**
	 * Checks if the external logback configuration file is available.
	 * @return true, if the file is available
	 */
	private static boolean isExternalLogbackFileAvailable() {
		File logbackFile = getExternalLogbackFile();
		return logbackFile.exists();
	}
	/**
	 * Gets the logback file.
	 * @return the logback file
	 */
	private static File getExternalLogbackFile() {
		return new File(getExternalLogbackFileLocationPath());
	}
	/**
	 * Returns the logback file location.
	 * @return the logback file location
	 */
	private static String getExternalLogbackFileLocationPath() {
		String pathProperties = Application.getGlobalInfo().getPathProperty(true);
		String logbackFile = FileToProvide.LOGBACK_CONFIGURATION.toString();
	    return pathProperties + logbackFile;
	}
	
	/**
	 * Returns the internal logback file URL.
	 * @return the internal logback file URL
	 */
	private static URL getInternalLogbackFileURL() {
		String bundleFile = (GlobalInfo.getPathProperty() + FileToProvide.LOGBACK_CONFIGURATION.toString()).replace("\\", "/");;
		Bundle bundle = Platform.getBundle(PlugInActivator.PLUGIN_ID);
		return bundle.getResource(bundleFile);
	}
	
}
