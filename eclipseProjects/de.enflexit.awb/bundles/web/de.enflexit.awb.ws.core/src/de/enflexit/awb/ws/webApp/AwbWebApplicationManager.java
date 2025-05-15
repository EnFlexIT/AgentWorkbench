package de.enflexit.awb.ws.webApp;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.enflexit.awb.ws.core.JettyServerManager;
import de.enflexit.awb.ws.webApp.AwbWebApplication.PropertyType;
import de.enflexit.common.ServiceFinder;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.PropertyValue;
import de.enflexit.db.derby.server.DerbyNetworkServer;
import de.enflexit.db.derby.server.DerbyNetworkServerProperties;
import de.enflexit.db.derby.tools.DerbyPathHandling;
import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.HibernateUtilities;
import de.enflexit.db.hibernate.connection.DatabaseConnectionManager;
import de.enflexit.db.hibernate.connection.GeneralDatabaseSettings;

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
	 * If provided by an OSIG service, initializes the first AwbWebApplication before
	 * the {@link JettyServerManager} starts the Jetty server.
	 * 
	 * @see JettyServerManager#startServer(de.enflexit.awb.ws.core.JettyConfiguration)
	 */
	public static void initialize() {
	
		// --- If already defined, skip initialization --------------
		if (AwbWebApplicationManager.isDefinedAwbWebApplication(false)==true) return;
		
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
		
		// --- Exit if no web application was locally assigned ------
		if (AwbWebApplicationManager.isDefinedAwbWebApplication(false)==false) return;
		
		try {
			// --- Load the application properties ------------------
			Properties webAppProperties = AwbWebApplicationManager.getWebApplicationProperties().getProperties();
			Properties webAppPropertiesComparison = webAppProperties.getCopy();
			
			// --- Call to check the defaults settings --------------
			AwbWebApplicationManager.getAwbWebApplication().doCheckDefaultProperties(webAppProperties);
			if (webAppProperties.equals(webAppPropertiesComparison)==false) {
				AwbWebApplicationManager.getWebApplicationProperties().save();
			}
			// --- Assign the properties to the web application -----
			AwbWebApplicationManager.getAwbWebApplication().setProperties(webAppProperties);

			// --- Apply database settings --------------------------
			AwbWebApplicationManager.applyDatabaseSettings();
			
			// -- Call to initialize the web application ------------
			AwbWebApplicationManager.getAwbWebApplication().initialize();
			System.out.println("[" + JettyServerManager.class.getSimpleName() + "] Initialized server-side base for web application '" + AwbWebApplicationManager.getAwbWebApplication().getApplicationName() + "' (class: '" + AwbWebApplicationManager.getAwbWebApplication().getClass().getName() + "')");
			
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
			Properties propertiesForComparison = null;
			File webAppPropFile = AwbWebApplicationProperties.getFileWebApplicationProperties(webAppName);
			if (webAppPropFile.exists()==false) {
				webAppProperties = new AwbWebApplicationProperties(webAppName, new Properties());
				webAppProperties.addDefaultWebApplicationProperties();
				webAppProperties.save();
			} else {
				webAppProperties = AwbWebApplicationProperties.load(webAppPropFile);
				propertiesForComparison = webAppProperties.getProperties().getCopy();
				webAppProperties.addDefaultWebApplicationProperties();
				if (webAppProperties.getProperties().equals(propertiesForComparison)==false) {
					webAppProperties.save();
				}
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


	/**
	 * Apply database settings.
	 * @return true, if successful
	 */
	private static boolean applyDatabaseSettings() {

		if (isDefinedAwbWebApplication(true)==false) return false;
		
		// --- Get the web application properties first -----------------------
		Properties webAppProperties = AwbWebApplicationManager.getWebApplicationProperties().getProperties();
		// --- Apply Derby DB-settings first ----------------------------------
		boolean success = AwbWebApplicationManager.applyDerbyServerSettings(webAppProperties);
		// --- Apply GeneralDatabaseSettings for database factories -----------
		success = success && AwbWebApplicationManager.applyGeneralDatabaseSettings(webAppProperties);
		
		return success;
	}

	
	/**
	 * Applies the derby network server settings .
	 * @return true, if successful
	 */
	private static boolean applyDerbyServerSettings(Properties webAppProperties) {
		
		// --- Define indicator variables for restarting the DB server --------
		boolean isPrintWarning = true;
		boolean isDifferentDatabaseDir   = false;
		boolean isDifferentServerSetting = false;
		
		// --- Check the "derby.system.home" ----------------------------------
		String dbDirCurrent = System.getProperties().getProperty("derby.system.home");
		String dbDirWebApp = DerbyPathHandling.getDatabasePath(AwbWebApplicationManager.getAwbWebApplication().getClass(), true).toString();
		if (dbDirWebApp.equals(dbDirCurrent)==false) {
			isDifferentDatabaseDir = true;
		}
		
		
		// --- Read derby settings from web application properties file -------
		boolean isStartServer = webAppProperties.getBooleanValue(DerbyNetworkServerProperties.DERBY_SERVER_PREFERENCES_START_SERVER);
		String host = webAppProperties.getStringValue(DerbyNetworkServerProperties.DERBY_SERVER_PREFERENCES_HOST);
		Integer port = webAppProperties.getIntegerValue(DerbyNetworkServerProperties.DERBY_SERVER_PREFERENCES_PORT);
		String userName = webAppProperties.getStringValue(DerbyNetworkServerProperties.DERBY_SERVER_PREFERENCES_USERNAME);
		String password = webAppProperties.getStringValue(DerbyNetworkServerProperties.DERBY_SERVER_PREFERENCES_PASSWORD);
		// --- Set to a new instance ------------------------------------------
		DerbyNetworkServerProperties dsWebApp = new DerbyNetworkServerProperties();
		dsWebApp.setStartDerbyNetworkServer(isStartServer);
		dsWebApp.setHost(host);
		dsWebApp.setPort(port);
		dsWebApp.setUserName(userName);
		dsWebApp.setPassword(password);
		
		// --- Load current settings ------------------------------------------
		DerbyNetworkServerProperties dsCurrent = new DerbyNetworkServerProperties();
		if (dsWebApp.equals(dsCurrent)==false) {
			// --- Save new server settings -----
			dsWebApp.save();
			isDifferentServerSetting = true;
		}
		
		
		// --- Is the server to be restarted? ---------------------------------
		if (isDifferentDatabaseDir==true || isDifferentServerSetting==true) {
			// --- Stop the server --------------
			DerbyNetworkServer.terminate();
			// --- Set "derby.system.home" ------
			if (isPrintWarning) LOGGER.warn("Current Derby root direcotry is: " + dbDirCurrent);
			if (isDifferentDatabaseDir==true) {
				System.getProperties().setProperty("derby.system.home", dbDirWebApp);
				if (isPrintWarning) LOGGER.warn("Derby root direcotry was changed to: " + dbDirWebApp);
			}
			// --- Start (if configured so) -----
			DerbyNetworkServer.execute();
		}
		
		return true;
	}
	
	/**
	 * Applies the general factory connection settings .
	 * @return true, if successful
	 */
	private static boolean applyGeneralDatabaseSettings(Properties webAppProperties) {
		
		// --------------------------------------------------------------------
		// --- Apply the General connection settings --------------------------
		// --------------------------------------------------------------------
		
		// --- Create GeneralDatabaseSettings from webApp properties ---------- 
		GeneralDatabaseSettings gdbSettingsWebApp = new GeneralDatabaseSettings();
		gdbSettingsWebApp.setHibernateDatabaseSettings(new java.util.Properties());
		gdbSettingsWebApp.setUseForEveryFactory(true);
		
		String dbPropertyNames = webAppProperties.getStringValue(AwbWebApplicationProperties.DEFAULT_WEB_APP_DATABASE_PROPERTIES);
		String[] dbPropertyNameArray = dbPropertyNames.split(",");
		for (String dbPropertyName : dbPropertyNameArray) {

			PropertyValue pValue = webAppProperties.getPropertyValue(dbPropertyName);
			if (pValue!=null) {
				gdbSettingsWebApp.getHibernateDatabaseSettings().put(dbPropertyName, pValue.getValue());
			}
		}

		// --- Set the DB system name -----------------------------------------
		String driverClass = gdbSettingsWebApp.getHibernateDatabaseSettings().getProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_DriverClass);
		String dbSystemName = HibernateUtilities.getDatabaseSystemNameByDriverClassName(driverClass); 
		gdbSettingsWebApp.setDatabaseSystemName(dbSystemName);

		// --- Get the current GeneralDatabaseSettings ------------------------
		GeneralDatabaseSettings gdbSettingsCurrent = DatabaseConnectionManager.getInstance().getGeneralDatabaseSettings();
		if (gdbSettingsWebApp.equals(gdbSettingsCurrent)==false) {
			DatabaseConnectionManager.getInstance().saveGeneralDatabaseSettings(gdbSettingsWebApp);
		}
		return true;
	}
	
}
