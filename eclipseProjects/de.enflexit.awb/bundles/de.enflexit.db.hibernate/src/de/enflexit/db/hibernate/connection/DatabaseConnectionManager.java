package de.enflexit.db.hibernate.connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.hibernate.cfg.Configuration;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.prefs.BackingStoreException;

import de.enflexit.common.ServiceFinder;
import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.HibernateUtilities;
import de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState;
import de.enflexit.db.hibernate.gui.DatabaseSettings;

/**
 * The singleton class DatabaseConnectionManager does what the name promises.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DatabaseConnectionManager {
	
	// ------------------------------------------
	// --- Singleton construction - Start -------
	private static DatabaseConnectionManager instance;
	
	public static DatabaseConnectionManager getInstance() {
		if (instance==null) {
			instance = new DatabaseConnectionManager();
			instance.startHibernateDatabaseConnectionServiceTracker();
			instance.startRegisteredDatabaseConnections();
		}
		return instance;
	}
	private DatabaseConnectionManager() { }
	// --- Singleton construction - End ---------
	// ------------------------------------------	
	
	
	private HashMap<String, HibernateDatabaseConnectionService> dbConnectionServiceHashMap;
	private HibernateDatabaseConnectionServiceTracker dbConnectionsTracker;
	
	/**
	 * Start the local HibernateDatabaseConnectionServiceTracker.
	 */
	private void startHibernateDatabaseConnectionServiceTracker() {
		if (dbConnectionsTracker==null) {
			BundleContext context = FrameworkUtil.getBundle(HibernateUtilities.class).getBundleContext();
			dbConnectionsTracker = new HibernateDatabaseConnectionServiceTracker(context, HibernateDatabaseConnectionService.class, null);
			dbConnectionsTracker.open();
		}
	}
	
	/**
	 * Returns the known HibernateDatabaseConnectionService.
	 * @return the hibernate data base connection service list
	 */
	private HashMap<String, HibernateDatabaseConnectionService> getHibernateDatabaseConnectionServiceHashMap() {
		if (dbConnectionServiceHashMap==null) {
			dbConnectionServiceHashMap = new HashMap<>();
			// --- Add the registered services to the HashMap -----------------
			List<HibernateDatabaseConnectionService> dbConnectionsServiceList = ServiceFinder.findServices(HibernateDatabaseConnectionService.class);
			for (int i = 0; i < dbConnectionsServiceList.size(); i++) {
				String factoryID = dbConnectionsServiceList.get(i).getFactoryID();
				dbConnectionServiceHashMap.put(factoryID, dbConnectionsServiceList.get(i));
			}
		}
		return dbConnectionServiceHashMap;
	}
	/**
	 * Returns the HibernateDatabaseConnectionService with the specified factoryID
	 *
	 * @param factoryID the factory ID
	 * @return the hibernate database connection service found or <code>null</code> if the service is not available
	 */
	public HibernateDatabaseConnectionService getHibernateDatabaseConnectionService(String factoryID) {
		return this.getHibernateDatabaseConnectionServiceHashMap().get(factoryID);
	}
	
	/**
	 * Starts all registered database connection services.
	 */
	private void startRegisteredDatabaseConnections() {
		List<HibernateDatabaseConnectionService> dbConnectionsServiceList = new ArrayList<>(this.getHibernateDatabaseConnectionServiceHashMap().values());
		for (int i = 0; i < dbConnectionsServiceList.size(); i++) {
			this.addHibernateDataBaseConnectionService(dbConnectionsServiceList.get(i));
		}
	}
	
	/**
	 * Will be invoked, if a new {@link HibernateDatabaseConnectionService} was added to the OSGI registry.
	 * @param connectionService the connection service
	 */
	public void addHibernateDataBaseConnectionService(HibernateDatabaseConnectionService connectionService) {

		try {
			// --- Get the necessary service instances ------------------------ 
			String factoryID = connectionService.getFactoryID();
			Configuration configuration = connectionService.getConfiguration();
			
			// --- Load JDBC connection data ----------------------------------
			this.loadDatabaseConfigurationProperties(factoryID, configuration);
			
			// --- Add to the locally known list ------------------------------
			this.getHibernateDatabaseConnectionServiceHashMap().put(factoryID, connectionService);
			
			// --- Try to create a Hibernate SessionFactory -------------------
			HibernateUtilities.startSessionFactoryInThread(factoryID, configuration, false, true);
			
		} catch (Exception ex) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error while starting database connection of service '" + connectionService.getClass().getName() + "':");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Will be invoked, if a {@link HibernateDatabaseConnectionService} was removed from the OSGI registry.
	 * @param dbConnectionService the db connection service
	 */
	public void removeHibernateDataBaseConnectionService(HibernateDatabaseConnectionService dbConnectionService) {
		
		if (dbConnectionService==null) return;
		
		// --- Remove from the local HashMap ----------------------------------
		String factoryID = dbConnectionService.getFactoryID();
		this.getHibernateDatabaseConnectionServiceHashMap().remove(factoryID);
		// --- Destroy the SessionFactory -------------------------------------
		HibernateUtilities.closeSessionFactory(factoryID);
		
	}
	
	

	
	// ------------------------------------------------------------------------
	// --- From here, methods for loading & saving preferences ---------------- 
	// ------------------------------------------------------------------------
	/**
	 * Returns the database settings that can be used for the visual configuration of the database connection.
	 *
	 * @param factoryID the factory ID
	 * @return the database settings
	 */
	public DatabaseSettings getDatabaseSettings(String factoryID) {

		HibernateDatabaseConnectionService dbConnectionService = this.getHibernateDatabaseConnectionService(factoryID);
		if (dbConnectionService!=null) {
			// --- Get configuration and derive database system name ----------
			Configuration hiberanteConfig = dbConnectionService.getConfiguration();
			String databaseSystemName = HibernateUtilities.getDatabaseSystemNameByHibernateConfiguration(hiberanteConfig);
			return new DatabaseSettings(databaseSystemName, hiberanteConfig);
		}
		return null;
	}
	
	
	/**
	 * Returns the eclipse preferences.
	 *
	 * @param factoryID the factory ID
	 * @return the eclipse preferences
	 */
	public IEclipsePreferences getEclipsePreferences(String factoryID) {
		IScopeContext iScopeContext = ConfigurationScope.INSTANCE;
		return iScopeContext.getNode(factoryID);
	}
	/**
	 * Load configuration properties.
	 *
	 * @param factoryID the factory ID
	 * @param hibernateConfig the Hibernate configuration
	 */
	public void loadDatabaseConfigurationProperties(String factoryID, Configuration hibernateConfig) {
		
		IEclipsePreferences eclipsePreferences = getEclipsePreferences(factoryID);
		
		String driverClass = eclipsePreferences.get("hibernate.connection.driver_class", null);
		HibernateDatabaseService hds = HibernateUtilities.getDatabaseServiceByDriverClassName(driverClass);
		if (hds!=null) {
			// --- Get the key required for the database system -----
			Properties defaultProperties = hds.getHibernateDefaultPropertySettings();
			Vector<String> dbPropertiyNames = hds.getHibernateConfigurationPropertyNamesForDbCheckOnJDBC();
			for (int i = 0; i < dbPropertiyNames.size(); i++) {
				// --- Get property name and value ------------------
				String propertyName = dbPropertiyNames.get(i);
				String propertyValue = eclipsePreferences.get(propertyName, null);
				if (propertyValue==null) {
					// --- Set the default value --------------------
					propertyValue = defaultProperties.getProperty(propertyName);
				}
				// --- Set to hibernate configuration ---------------
				if (propertyValue!=null) {
					hibernateConfig.setProperty(propertyName, propertyValue);
				}
			}
			
		} else {
			//System.err.println("No HibernateDatabaseService could be found for the driver class '" + driverClass + "'");
		}
	}
	
	/**
	 * Saves the specified database configuration properties and updates the corresponding Hibernate {@link Configuration} (if possible).
	 * After successful saving, the SessionFactory will be (re-)initialized. 
	 *
	 * @param factoryID the factory ID
	 * @param properties the new eclipse preferences for the database connection
	 * @return true, if the settings were successfully saved
	 */
	public boolean saveDatabaseConfigurationProperties(String factoryID, Properties properties) {
		return this.saveDatabaseConfigurationProperties(factoryID, properties, null);
	}
	/**
	 * Saves the specified database configuration properties and updates the corresponding Hibernate {@link Configuration} (if possible).
	 * After successful saving, the corresponding SessionFactory will be (re-)initialized. 
	 *
	 * @param factoryID the factory ID
	 * @param properties the new eclipse preferences for the database connection
	 * @param hibernateConfig the hibernate config
	 * @return true, if the settings were successfully saved
	 */
	public boolean saveDatabaseConfigurationProperties(String factoryID, Properties properties, Configuration hibernateConfig) {
		
		if (properties==null) return false;
		
		// --- Create DatabaseSettings for a setting comparison -----
		String driverClassNew = properties.getProperty("hibernate.connection.driver_class", null);
		String dbSystemNameNew = HibernateUtilities.getDatabaseSystemNameByDriverClassName(driverClassNew);
		DatabaseSettings dbSettingsNew = new DatabaseSettings(dbSystemNameNew, properties);
		DatabaseSettings dbSettingsOld = this.getDatabaseSettings(factoryID);
		boolean hasChangedSettings = (dbSettingsNew.equals(dbSettingsOld)==false);

		
		// --- Try to get the database connection service first -----
		if (hibernateConfig==null) {
			HibernateDatabaseConnectionService hDbCS = this.getHibernateDatabaseConnectionServiceHashMap().get(factoryID);
			if (hDbCS!=null) {
				hibernateConfig = hDbCS.getConfiguration();
			}
		}
		
		// --- Get the corresponding eclipse preferences instance --- 
		IEclipsePreferences eclipsePreferences = getEclipsePreferences(factoryID);
		try {
			ArrayList<Object> propertyList = new ArrayList<>(properties.keySet());
			for (int i = 0; i < propertyList.size(); i++) {
				// --- Save the eclipse preferences -----------------
				String propName = (String) propertyList.get(i);
				String propValue = properties.getProperty(propName);
				eclipsePreferences.put(propName, propValue);
				// --- Change the hibernate configuration -----------
				if (hibernateConfig!=null) {
					hibernateConfig.setProperty(propName, propValue);
				}
			}
			eclipsePreferences.flush();
			
		} catch (BackingStoreException bsEx) {
			bsEx.printStackTrace();
			return false;
		}
		
		// --- Restart the database connection ----------------------
		if (hibernateConfig!=null) {
			if (hasChangedSettings==true) {
				HibernateUtilities.startSessionFactoryInThread(factoryID, hibernateConfig, true, false);
			} else {
				if (this.isAllowSessionFactoryStart(HibernateUtilities.getSessionFactoryMonitor(factoryID).getSessionFactoryState())==true) {
					HibernateUtilities.startSessionFactoryInThread(factoryID, hibernateConfig, true, false);
				}
			}
		}
		return true;
	}
	
	/**
	 * Checks, based on the current state, if a new SessionFactory start is allowed.
	 *
	 * @param currentState the current state
	 * @return true, if is allow session start
	 */
	private boolean isAllowSessionFactoryStart(SessionFactoryState currentState) {
		
		boolean allowSessionStart = false;
		switch (currentState) {
		case CheckDBConectionFailed:
		case InitializationProcessFailed:
		case Destroyed:
			allowSessionStart = true;
			break;

		case NotAvailableYet:
		case CheckDBConnection:
		case InitializationProcessStarted:
		case Created:
			allowSessionStart = false;
			break;
		}
		return allowSessionStart;
	}
	
}
