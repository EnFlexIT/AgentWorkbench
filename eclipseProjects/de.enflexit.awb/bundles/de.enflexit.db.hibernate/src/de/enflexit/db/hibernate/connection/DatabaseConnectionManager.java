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
import org.osgi.service.prefs.BackingStoreException;

import de.enflexit.common.ServiceFinder;
import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.HibernateUtilities;
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
			instance.startRegisteredDatabaseConnections();
		}
		return instance;
	}
	private DatabaseConnectionManager() { }
	// --- Singleton construction - End ---------
	// ------------------------------------------	
	
	
	private HashMap<String, HibernateDatabaseConnectionService> dbConnectionsServiceHashMap;
	
	/**
	 * Returns the known HibernateDatabaseConnectionService.
	 * @return the hibernate data base connection service list
	 */
	private HashMap<String, HibernateDatabaseConnectionService> getHibernateDataBaseConnectionServiceHashMap() {
		if (dbConnectionsServiceHashMap==null) {
			dbConnectionsServiceHashMap = new HashMap<>();
			// --- Add the registered services to the HashMap -----------------
			List<HibernateDatabaseConnectionService> dbConnectionsServiceList = ServiceFinder.findServices(HibernateDatabaseConnectionService.class);
			for (int i = 0; i < dbConnectionsServiceList.size(); i++) {
				String factoryID = dbConnectionsServiceList.get(i).getFactoryID();
				dbConnectionsServiceHashMap.put(factoryID, dbConnectionsServiceList.get(i));
			}
		}
		return dbConnectionsServiceHashMap;
	}
	
	/**
	 * Starts all registered database connection services.
	 */
	private void startRegisteredDatabaseConnections() {
		List<HibernateDatabaseConnectionService> dbConnectionsServiceList = new ArrayList<>(this.getHibernateDataBaseConnectionServiceHashMap().values());
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
			Configuration connection = connectionService.getConfiguration();
			
			// --- Add to the locally known list ------------------------------
			this.getHibernateDataBaseConnectionServiceHashMap().put(factoryID, connectionService);
			
			// --- Try to create a Hibernate SessionFactory -------------------
			this.startSessionFactoryInThread(factoryID, connection);
			
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
		this.getHibernateDataBaseConnectionServiceHashMap().remove(factoryID);
		// --- Destroy the SessionFactory -------------------------------------
		HibernateUtilities.closeSessionFactory(factoryID);
		
	}
	
	/**
	 * Start the EOM SessionFactory within an extra thread.
	 * @param isResetSessionFactory the is reset session factory
	 * @param doSilentConnectionCheck the do silent connection check
	 */
	private void startSessionFactoryInThread(final String factoryID, final Configuration configuration) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
				HibernateUtilities.getSessionFactory(factoryID, configuration, false, true);
			}
		}, "Start of SessionFactory " + factoryID).start();
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

		HibernateDatabaseConnectionService dbConnectionService = this.getHibernateDataBaseConnectionServiceHashMap().get(factoryID);
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
				// -- Set to hibernate configuration ----------------
				if (propertyValue!=null) {
					hibernateConfig.setProperty(propertyName, propertyValue);
				}
			}
			
		} else {
			System.err.println("No HibernateDatabaseService could be found for the driver class '" + driverClass + "'");
		}
	}
	
	/**
	 * Saves the specified database configuration properties and updates the Hibernate {@link Configuration} (if specified).
	 *
	 * @param factoryID the factory ID
	 * @param properties the new eclipse preferences for the database connection
	 * @param hibernateConfig the current Hibernate {@link Configuration} to update (can be <code>null</code>).
	 */
	public void saveDatabaseConfigurationProperties(String factoryID, Properties properties, Configuration hibernateConfig) {
		
		if (properties==null) return;
		
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
		}
	}
	
}
