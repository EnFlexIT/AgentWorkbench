package de.enflexit.db.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * The class HibernateUtilities provides static access to Hibernate SessionFactories
 * that are distinguished by an ID. This allows to use the hibernate bundle for several
 * purposes and from different bundles and their contexts and databases. 
 * if the argument factoryID is <code>null</code>, a 'defaultFactory' will be used.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 * 
 * @see #DEFAULT_SESSION_FACTORY_ID
 */
public class HibernateUtilities {

	public static final String DEFAULT_SESSION_FACTORY_ID = "defaultFactory";
	public static final String DB_SERVICE_REGISTRATION_ERROR = "NO DATABASE SYSTEM REGISTERD";
	
	private static HashMap<String, HibernateDatabaseService> databaseServices;
	private static HashMap<String, SessionFactory> sessionFactoryHashMap;
	
	/**
	 * Gets the session factory hash map.
	 * @return the session factory hash map
	 */
	private static HashMap<String, SessionFactory> getSessionFactoryHashMap() {
		if (sessionFactoryHashMap==null) {
			sessionFactoryHashMap = new HashMap<>();
		}
		return sessionFactoryHashMap;
	}
	
	
	/**
	 * Will return the default session factory.
	 * @return the session factory
	 */
	public static SessionFactory getSessionFactory() {
		return getSessionFactory(null, null, false, false);
	}
	/**
	 * Will return or create the default session factory. Use this method to 
	 * create the default session factory.
	 * @return the session factory
	 */
	public static SessionFactory getSessionFactory(Configuration configuration) {
		return getSessionFactory(null, configuration, false, false);
	}
	
	/**
	 * Gets the session factory with the specified ID.
	 *
	 * @param factoryID the factory ID (<code>null</code> will use the default factory)
	 * @return the session factory
	 */
	public static SessionFactory getSessionFactory(String factoryID) {
		return getSessionFactory(factoryID, null, false, false);	
	}
	/**
	 * Returns the hibernate session factory.
	 *
	 * @param factoryID the factory ID (<code>null</code> will use the default factory)
	 * @param configuration the configuration
	 * @return the session factory
	 */
	public static SessionFactory getSessionFactory(String factoryID, Configuration configuration) {
		return getSessionFactory(factoryID, configuration, false, false);
	}
	
	/**
	 * Returns the hibernate session factory.
	 *
	 * @param factoryID the factory ID (<code>null</code> will use the default factory)
	 * @param configuration the hibernate configuration object
	 * @param isResetSessionFactory the is reset session factory
	 * @param doSilentConnectionCheck set true, the do a silent connection check
	 * @return the session factory
	 */
	public static synchronized SessionFactory getSessionFactory(String factoryID, Configuration configuration, boolean isResetSessionFactory, boolean doSilentConnectionCheck) {

		// --- Check for an available SessionFactory -------------------------- 
		String idFactory = factoryID;
		if (idFactory==null || idFactory.isEmpty()) {
			idFactory = DEFAULT_SESSION_FACTORY_ID;
		}		
		SessionFactory factory = getSessionFactoryHashMap().get(idFactory);
		
		// --- Reset the session factory first? -------------------------------
		if (factory!=null && isResetSessionFactory==true) {
			closeSessionFactory(factory);
			factory = null;
		}
		
		// --- Create the session factory, if not already available ----------- 
		if (factory==null) {
			try {
				if (configuration==null) {
					// --- Use Hibernate auto configuration here --------------
					configuration =  new Configuration().configure();
				}
				
				// --- Check the database connection here ---------------------
				String dbCheckMessage = isAvailableDatabase(configuration, doSilentConnectionCheck); 
				if (dbCheckMessage==null) {
					// --- Create the session factory -------------------------
					ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
					factory = configuration.buildSessionFactory(registry);
					getSessionFactoryHashMap().put(idFactory, factory);
					
				} else {
					if (doSilentConnectionCheck==false) {
						System.err.println("[" + HibernateUtilities.class.getSimpleName() + "]: No database connection could be established.");
					}
				}
				
			} catch (Throwable th) {
				System.err.println("Failed to create session factory " + th);
				th.printStackTrace();
			}	
		}
		return factory;
	}
	
	/**
	 * Close session factory specified by the factory ID.
	 * @param factoryID the factory ID (<code>null</code> will use the default factory)
	 */
	public static void closeSessionFactory(String factoryID) {
		if (factoryID==null) {
			factoryID = DEFAULT_SESSION_FACTORY_ID;
		}
		closeSessionFactory(getSessionFactoryHashMap().get(factoryID));
	}
	/**
	 * Closes the specified hibernate session factory.
	 * @param factory the factory
	 */
	public static void closeSessionFactory(SessionFactory factory) {
		if (factory!=null) {
			factory.close();
			factory = null;
		}
	}

	
	/**
	 * Checks if the configured database connection is available.
	 *
	 * @param configuration the current hibernate configuration
	 * @param doSilentConnectionCheck the do silent connection check
	 * @return true, if is available database
	 */
	private static String isAvailableDatabase(Configuration configuration, boolean doSilentConnectionCheck) {
		
		String errMessage = null;
		String driverClassName = configuration.getProperty("hibernate.connection.driver_class");
		if (driverClassName==null) {
			// --- Write driver class error -------------------------
			errMessage = "No JDBC driver class wass specified";
			
		} else {
			// --- Get the corresponding database service -----------
			HibernateDatabaseService dbService = getDatabaseServices().get(driverClassName);
			if (dbService==null) {
				// --- Set error message ----------------------------
				errMessage = "Could not find the corresponding hibernate database service for the JDBC connection test!";
			} else {
				// --- Extract settings for connection test --------- 
				Properties jdbcCheckProperties = getPropertiesForConnectionCheckOnJDBC(configuration, dbService.getHibernateConfigurationPropertyNamesForDbCheckOnJDBC());
				// --- Do the JDBC connection test ------------------
				boolean isAvailableConnection =  dbService.isDatabaseAccessible(jdbcCheckProperties, doSilentConnectionCheck);
				if (isAvailableConnection==false) {
					errMessage = "Connection test failed";
				}
			}
		}
		return errMessage;
	}

	/**
	 * Gets the properties for the connection check on JDBC.
	 *
	 * @param configuration the configuration
	 * @param parameterNames the parameter names
	 * @return the properties for connection check on JDBC
	 */
	private static Properties getPropertiesForConnectionCheckOnJDBC(Configuration configuration, Vector<String> parameterNames) {
		Properties jdbcProps = new Properties();
		for (int i = 0; i < parameterNames.size(); i++) {
			String propName = parameterNames.get(i);
			String propValue = configuration.getProperty(propName);
			jdbcProps.setProperty(propName, propValue);
		}
		return jdbcProps;
	}
	/**
	 * Returns the available {@link HibernateDatabaseService}.
	 * @return the database services
	 */
	public static HashMap<String, HibernateDatabaseService> getDatabaseServices() {
		if (databaseServices==null) {
			databaseServices = new HashMap<>();
		}
		return databaseServices;
	}
	/**
	 * Registers a {@link HibernateDatabaseService}.
	 * @param databaseService the database service
	 */
	public static void registerDatabaseService(HibernateDatabaseService databaseService) {
		
		if (databaseService==null) return;
		
		if (isValidHibernateDatabaseService(databaseService)) {
			String dbSystemName = databaseService.getDatabaseSystemName();
			HibernateDatabaseService oldDbService = getDatabaseServices().get(dbSystemName);
			if (oldDbService!=null) {
				System.err.println("Overwrite service for database system '" + dbSystemName + "'!");
			}
			getDatabaseServices().put(dbSystemName, databaseService);
		}
	}
	/**
	 * Checks if is valid hibernate database service.
	 * @param databaseService the database service
	 * @return true, if is valid hibernate database service
	 */
	private static boolean isValidHibernateDatabaseService(HibernateDatabaseService databaseService) {
		
		String errMsg = "[" + HibernateUtilities.class.getSimpleName() + "] Invalid database service: ";
		String serviceClassName = databaseService.getClass().getName();
		
		if (databaseService.getDatabaseSystemName()==null || databaseService.getDatabaseSystemName().isEmpty()) {
			System.err.println(errMsg + "No database system name is returned by '" + serviceClassName + "'!");
			return false;
		}
		if (databaseService.getDriverClassName()==null || databaseService.getDriverClassName().isEmpty()) {
			System.err.println(errMsg + "No JDBC driver class is returned by '" + serviceClassName + "'!");
			return false;
		} else {
			try {
				Class.forName(databaseService.getDriverClassName());
			} catch (ClassNotFoundException e) {
				System.err.println(errMsg + "Can't load JDBC driver '" + databaseService.getDriverClassName() + "'!");
				return false;
			}
		}
		Vector<String> jdbcCheckParams = databaseService.getHibernateConfigurationPropertyNamesForDbCheckOnJDBC();
		if (jdbcCheckParams==null || jdbcCheckParams.size()==0) {
			System.err.println(errMsg + "No JDBC check parameters are returned by '" + serviceClassName + "'!");
			return false;
		}
		return true;
	}
	/**
	 * Unregisters a {@link HibernateDatabaseService}.
	 * @param databaseService the database service
	 */
	public static void unregisterDatabaseService(HibernateDatabaseService databaseService) {
		if (databaseService==null || databaseService.getDatabaseSystemName()==null || databaseService.getDatabaseSystemName().isEmpty()) return;
		getDatabaseServices().remove(databaseService.getDatabaseSystemName());
	}
	/**
	 * Returns a sorted list of known/available database systems that are registered at the {@link HibernateUtilities}.
	 * @return the database system list
	 */
	public static List<String> getDatabaseSystemList() {
		List<String> dbSystems = new ArrayList<>(getDatabaseServices().keySet());
		if (dbSystems.isEmpty()==true) {
			dbSystems.add(DB_SERVICE_REGISTRATION_ERROR);
		} else {
			Collections.sort(dbSystems);
		}
		return dbSystems;
	}
	/**
	 * Returns the {@link HibernateDatabaseService} for the specified database system.
	 *
	 * @param databaseSystemName the database system name
	 * @return the database service
	 */
	public static HibernateDatabaseService getDatabaseService(String databaseSystemName) {
		if (databaseSystemName!=null) {
			return getDatabaseServices().get(databaseSystemName);
		}
		return null;
	}
	
}
