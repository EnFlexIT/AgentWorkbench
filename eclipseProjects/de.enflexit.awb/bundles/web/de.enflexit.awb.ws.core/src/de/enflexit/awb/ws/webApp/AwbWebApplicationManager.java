package de.enflexit.awb.ws.webApp;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.enflexit.awb.ws.core.JettyServerManager;
import de.enflexit.awb.ws.webApp.AwbWebApplication.PropertyType;
import de.enflexit.common.ServiceFinder;
import de.enflexit.common.properties.Properties;

/**
 * The Class AwbWebApplicationManager provides static access methods to handle 
 * data and information for an AWB Web-Application .
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbWebApplicationManager {
	
	static Logger LOGGER = LoggerFactory.getLogger(AwbWebApplicationManager.class);
	
	private static AwbWebApplication awbWebApplication;
	private static AwbWebApplicationProperties webAppProperties;
	
	
	/**
	 * Initializes.
	 */
	public static void initialize() {
	
		// --- If already defined, skip initialization --------------
		if (isDefinedAwbWebApplication(false)==true) return;
		
		// --- Get all services defined -----------------------------
		List<AwbWebApplication> webAppList = ServiceFinder.findServices(AwbWebApplication.class);
		if (webAppList.size()==0) {
			LOGGER.warn("No OSGI service was found that implements an AwbWebApplication!");
		} else {
			if (webAppList.size()>1) {
				LOGGER.error("More than one AwbWebApplication were provided as OSGI service. Please, reduce this number to one by deactivating all bundles that are not required!");
			}
			// --- Try setting an AwbWebApplication -----------------
			webAppList.forEach(webApp -> AwbWebApplicationManager.setAwbWebApplication(webApp));
		}
		
		// --- Exit if no web application was started ---------------
		if (AwbWebApplicationManager.isDefinedAwbWebApplication(false)==false) return;
		
		try {
			// --- Load the application properties ------------------
			AwbWebApplicationManager.getAwbWebApplication().setProperties(AwbWebApplicationManager.getWebApplicationProperties().getProperties());
			// -- Call to initialize the web application ------------
			AwbWebApplicationManager.getAwbWebApplication().initialize();
			System.out.println("[" + JettyServerManager.class.getSimpleName() + "] Initialized server-side base for web application '" + AwbWebApplicationManager.getAwbWebApplication().getClass().getName() + "'");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Returns the web application properties.
	 * @return the web application properties
	 */
	private static AwbWebApplicationProperties getWebApplicationProperties() {
		if (webAppProperties==null) {
			String webAppName = getAwbWebApplication().getApplicationName();
			File webAppPropFile = AwbWebApplicationProperties.getFileWebApplicationProperties(webAppName);
			if (webAppPropFile.exists()==false) {
				webAppProperties = new AwbWebApplicationProperties(webAppName, new Properties());
				webAppProperties.save();
			} else {
				webAppProperties = AwbWebApplicationProperties.load(webAppPropFile);
			}
		}
		return webAppProperties;
	}
	
	
	/**
	 * Returns the current instance of an AWB web application.
	 * @return the AwbWebApplication
	 */
	public static AwbWebApplication getAwbWebApplication() {
		return awbWebApplication;
	}
	/**
	 * Sets the current instance of an AWB web application.
	 * @param awbWebApplication the new awb web application
	 */
	private static void setAwbWebApplication(AwbWebApplication awbWebApplication) {
		if (AwbWebApplicationManager.awbWebApplication!=null && awbWebApplication!=AwbWebApplicationManager.awbWebApplication) {
			// --- Prepare error message ----------------------------
			String awbImplClass = AwbWebApplicationManager.awbWebApplication.getClass().getName();
			LOGGER.error("Error while trying to define the AwbWebApplication using class '" + awbWebApplication.getClass().getName() + "'!");
			LOGGER.error("The AwbWebApplication was already defined with class " + awbImplClass + " and can not be overwritten. Consider to deactivate the corresponding bundle!");
			return;
		}
		AwbWebApplicationManager.awbWebApplication = awbWebApplication;
		LOGGER.info("The AwbWebApplication was set to class " + awbWebApplication.getClass().getName() + "!");
	}
	/**
	 * Checks if a {@link AwbWebApplication} is already defined.
	 *
	 * @param doWarn the indicator to warn with logging output n
	 * @return true, if a {@link AwbWebApplication} is already defined.
	 */
	private static boolean isDefinedAwbWebApplication(boolean doWarn) {
		if (AwbWebApplicationManager.awbWebApplication==null) {
			if (doWarn)LOGGER.warn("The AwbWebApplication to be used is not spcified yet!");
			return false;
		}
		return true;
	}
	
	
	/**
	 * Returns the properties of the web application.
	 *
	 * @param typeOfProperty the type of property
	 * @return the properties
	 */
	public static Properties getProperties(PropertyType typeOfProperty) {
		if (isDefinedAwbWebApplication(true)==false) return null;
		return getAwbWebApplication().getProperties(typeOfProperty);
	}


}
