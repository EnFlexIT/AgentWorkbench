package de.enflexit.db.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.stat.Statistics;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState;

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

	public static final String DEFAULT_SESSION_FACTORY_ID = "awbSessionFactory";
	public static final String DB_SERVICE_REGISTRATION_ERROR = "NO DATABASE SYSTEM REGISTERD";
	
	private static HashMap<String, HibernateDatabaseService> databaseServices;
	private static ConcurrentHashMap<String, SessionFactoryMonitor> sessionFactoryMonitorHashMap;
	private static HashMap<String, SessionFactory> sessionFactoryHashMap;
	
	
	/**
	 * Returns the session factory monitor hash map.
	 * @return the session factory monitor hash map
	 */
	private static ConcurrentHashMap<String, SessionFactoryMonitor> getSessionFactoryMonitorHashMap() {
		if (sessionFactoryMonitorHashMap==null) {
			sessionFactoryMonitorHashMap = new ConcurrentHashMap<>();
		}
		return sessionFactoryMonitorHashMap;
	}
	/**
	 * Returns the session factory monitor for the specified factory defined by its ID.
	 * @param factoryID the factory ID (<code>null</code> will use the default factory)
	 * @return the session factory monitor
	 * @see #DEFAULT_SESSION_FACTORY_ID
	 */
	public static synchronized SessionFactoryMonitor getSessionFactoryMonitor(String factoryID) {
		
		String idFactory = getFactoryID(factoryID);
		// --- Try to get the SessionFactoryMonitor ----------------- 
		SessionFactoryMonitor monitor = getSessionFactoryMonitorHashMap().get(idFactory);
		if (monitor==null) {
			// --- Put monitor instance into the local hash map -----
			monitor = new SessionFactoryMonitor(idFactory);
			getSessionFactoryMonitorHashMap().put(idFactory, monitor);
		}
		return monitor;
	}
	
	/**
	 * Calling this method, the invoking thread will be paused until all session factory  
	 * start-ups (successful or not) are finalized. - The maximum wait time is 30s.
	 */
	public static void waitForSessionFactoryCreation() {
		
		if (getSessionFactoryMonitorHashMap().size()==0) return;

		long sleepTime 	= 1000;			// ms
		long timeout 	= 1000 * 30; 	// s
		long exitTime 	= System.currentTimeMillis() + timeout;
		
		boolean sfBusy = true;
		while (sfBusy==true) {
			
			// --- Get list of SessionFactoryMonitor ------  
			List<SessionFactoryMonitor> sfMonitorList = new ArrayList<>(getSessionFactoryMonitorHashMap().values());
			for (int i = 0; i < sfMonitorList.size(); i++) {
				// --- Check state of single SessionFactory
				boolean sfBusySingle = isSessionFactoryInCreation(sfMonitorList.get(i));
				if (sfBusySingle==true) {
					// --- Exit for-loop ------------------
					break;
				} else {
					// --- Checked the last monitor? ------
					if (i==(sfMonitorList.size()-1)) {
						sfBusy = false;
					}
				}
			}
			
			// --- Reached the exit time? -----------------
			if (System.currentTimeMillis()>=exitTime) {
				break;
			}
			
			if (sfBusy==true) {
				// --- Sleep for a while ------------------
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException iEx) {
					System.err.println("[" + HibernateUtilities.class.getSimpleName() + "] Waiting for SessionFactory creation was interrupted.");
					//iEx.printStackTrace();
				}
			}
		} // end while
		
	}
	
	/**
	 * Checks if is session factory in creation.
	 *
	 * @param factoryID the factory ID (<code>null</code> will use the default factory)
	 * @return true, if is session factory in creation
	 */
	public static boolean isSessionFactoryInCreation(String factoryID) {
		String idFactory = getFactoryID(factoryID);
		SessionFactoryMonitor monitor = getSessionFactoryMonitorHashMap().get(idFactory);
		return isSessionFactoryInCreation(monitor);
	}
	/**
	 * Checks if a session factory is in creation.
	 *
	 * @param sessionFactoryMonitor the session factory monitor
	 * @return true, if the session factory is in creation
	 */
	public static boolean isSessionFactoryInCreation(SessionFactoryMonitor sessionFactoryMonitor) {
		
		if (sessionFactoryMonitor==null) return false;
		
		boolean sfInCreation = false;
		SessionFactoryMonitor sfm = sessionFactoryMonitor;
		switch (sfm.getSessionFactoryState()) {
		case CheckDBConectionFailed:
		case InitializationProcessFailed:
		case Created:
		case Destroyed:
			sfInCreation = false;
			break;
		case NotAvailableYet:
		case CheckDBConnection:
		case InitializationProcessStarted:
			sfInCreation = true;
			break;
		}
		return sfInCreation;
	}
	
	/**
	 * Returns the session factory hash map.
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
	 *
	 * @param configuration the configuration to use
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
	 * Checks and return a factory ID. If the argument is not null the specified ID will be used. 
	 * In case of a <code>null</code> argument, the default factory ID will be returned.
	 *
	 * @param factoryID the factory ID
	 * @return the factory ID
	 */
	private static String getFactoryID(String factoryID) {
		String idFactory = factoryID;
		if (idFactory==null || idFactory.isEmpty()) {
			idFactory = DEFAULT_SESSION_FACTORY_ID;
		}
		return idFactory;
	}
	
	/**
	 * Returns the hibernate session factory.
	 *
	 * @param factoryID the factory ID (<code>null</code> will use the default factory)
	 * @param configuration the hibernate configuration object
	 * @param isResetSessionFactory the is reset session factory
	 * @param doSilentConnectionCheck set true, the do a silent connection check
	 * @return the session factory
	 * @see #DEFAULT_SESSION_FACTORY_ID
	 */
	public static SessionFactory getSessionFactory(String factoryID, Configuration configuration, boolean isResetSessionFactory, boolean doSilentConnectionCheck) {

		// --- Check for an available SessionFactory -------------------------- 
		String idFactory = getFactoryID(factoryID);
		SessionFactory factory = getSessionFactoryHashMap().get(idFactory);
		
		// --- Reset the session factory first? -------------------------------
		if (factory!=null) {
			if (isResetSessionFactory==true) {
				closeSessionFactory(factoryID);
				factory = null;
			} else {
				return factory;
			}
		}
		
		// --- Synchronize on monitor object ----------------------------------		
		SessionFactoryMonitor monitor = getSessionFactoryMonitor(idFactory);
		switch (monitor.getSessionFactoryState()) {
		case NotAvailableYet:
		case CheckDBConectionFailed:
		case InitializationProcessFailed:
		case Destroyed:
			// --- (Retry to) Create the session factory ----------------------
			try {
				if (configuration==null) {
					// --- Use Hibernate auto configuration here --------------
					configuration =  new Configuration().configure();
				}
				
				// --- Check the database connection here ---------------------
				monitor.setSessionFactoryState(SessionFactoryState.CheckDBConnection);
				String dbCheckMessage = isAvailableDatabase(configuration, doSilentConnectionCheck); 
				if (dbCheckMessage==null) {
					// --- Create the session factory -------------------------
					monitor.setSessionFactoryState(SessionFactoryState.InitializationProcessStarted);
					ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
					factory = configuration.buildSessionFactory(registry);
					getSessionFactoryHashMap().put(idFactory, factory);
					monitor.setSessionFactoryState(SessionFactoryState.Created);
					
				} else {
					monitor.setSessionFactoryState(SessionFactoryState.CheckDBConectionFailed);
					if (doSilentConnectionCheck==false) {
						System.err.println("[" + HibernateUtilities.class.getSimpleName() + "]: No database connection could be established: " + dbCheckMessage);
					}
				}
				
			} catch (Throwable th) {
				monitor.setSessionFactoryState(SessionFactoryState.InitializationProcessFailed);
				System.err.println("Failed to create session factory " + th);
				th.printStackTrace();
				
			} finally {
				// --- notify all waiters ---------------------------------
				synchronized (monitor) {
					monitor.notifyAll();
				}
			}
			break;

		case CheckDBConnection:
		case InitializationProcessStarted:
			// --- Wait for the creation ----------------------------------
			waitForSessionFactoryCreation(monitor);
			// --- Get the Factory from the local HashMap -----------------
			factory = getSessionFactoryHashMap().get(idFactory);
			break;
			
		case Created:
			// --- Get the Factory from the local HashMap -----------------
			factory = getSessionFactoryHashMap().get(idFactory);
			break;
		}
		
	
		return factory;
	}
	/**
	 * Wait for the session factory creation.
	 * @param monitor the monitor
	 */
	private static void waitForSessionFactoryCreation(SessionFactoryMonitor monitor) {
		try {
			synchronized (monitor) {
				monitor.wait();
			}
			
		} catch (IllegalMonitorStateException | InterruptedException imse) {
			// imse.printStackTrace();
		}
	}
	
	
	/**
	 * Sets the statistics for the specified SessionFactory enabled (or not).
	 *
	 * @param factoryID the factory ID
	 * @param setEnabled the set enabled
	 */
	public static void setStatisticsEnabled(String factoryID, boolean setEnabled) {
	
		String idFactory = getFactoryID(factoryID);
		SessionFactory factory = getSessionFactoryHashMap().get(idFactory);
		if (factory!=null) {
			// --- Enable / disable statistics ----------------------
			Statistics stats = factory.getStatistics();
			stats.setStatisticsEnabled(true);
			
		} else {
			System.err.println("[" + HibernateUtilities.class.getSimpleName() + "]: SessionFactory '" + idFactory + "' not found!");
		}
	}
	
	/**
	 * Write the SessionFactory statistics for the specified factory.
	 * @param factoryID the factory ID
	 */
	public static void writeStatistics(String factoryID) {
		
		String idFactory = getFactoryID(factoryID);
		SessionFactory factory = getSessionFactoryHashMap().get(idFactory);
		if (factory!=null) {
			
			Statistics stats = factory.getStatistics();
			if (stats.isStatisticsEnabled()==true) {
				// --- Write statistics -----------------------------
				System.out.println(stats.toString());
			} else {
				System.err.println("[" + HibernateUtilities.class.getSimpleName() + "]: SessionFactory '" + idFactory + "' statistics are not enabled!");
			}
			
		} else {
			System.err.println("[" + HibernateUtilities.class.getSimpleName() + "]: SessionFactory '" + idFactory + "' not found!");
		}
	}
	
	
	/**
	 * Close session factory specified by the factory ID.
	 * @param factoryID the factory ID (<code>null</code> will use the default factory)
	 */
	public static void closeSessionFactory(String factoryID) {
		String idFactory = getFactoryID(factoryID);
		SessionFactory factory = getSessionFactoryHashMap().remove(idFactory);
		SessionFactoryMonitor monitor = getSessionFactoryMonitor(idFactory);
		monitor.setSessionFactoryState(SessionFactoryState.Destroyed);
		closeSessionFactory(factory);
	}
	/**
	 * Closes the specified hibernate session factory.
	 * @param factory the factory
	 */
	private static void closeSessionFactory(SessionFactory factory) {
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
			HibernateDatabaseService dbService = getDatabaseServiceByDriverClassName(driverClassName);
			if (dbService==null) {
				// --- Set error message ----------------------------
				errMessage = "Could not find the corresponding hibernate database service for the JDBC connection test!";
			} else {
				// --- Extract settings for connection test --------- 
				Properties jdbcCheckProperties = getPropertiesForConnectionCheckOnJDBC(configuration, dbService.getHibernateConfigurationPropertyNamesForDbCheckOnJDBC());
				// --- Do the JDBC connection test ------------------
				Vector<String> userMessages = new Vector<>(); 
				boolean isAvailableConnection =  dbService.isDatabaseAccessible(jdbcCheckProperties, userMessages, !doSilentConnectionCheck);
				if (isAvailableConnection==false) {
					errMessage = "Connection test failed!";
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
	private static HashMap<String, HibernateDatabaseService> getDatabaseServices() {
		if (databaseServices==null) {
			databaseServices = new HashMap<>();
		}
		// --- Dynamically check for corresponding OSGI services ----
		Bundle bundle = FrameworkUtil.getBundle(HibernateUtilities.class);
		if (bundle!=null) {
			try {
				// --- Get context and services ---------------------
				BundleContext context =  bundle.getBundleContext();
				if (context!=null) {
					ServiceReference<?>[] serviceRefs = context.getAllServiceReferences(HibernateDatabaseService.class.getName(), null);
					if (serviceRefs!=null) {
						for (int i = 0; i < serviceRefs.length; i++) {
							HibernateDatabaseService dbService = (HibernateDatabaseService) context.getService(serviceRefs[i]);
							HibernateDatabaseService dbServiceAvailable = databaseServices.get(dbService.getDatabaseSystemName());
							if (dbServiceAvailable==null) {
								databaseServices.put(dbService.getDatabaseSystemName(), dbService);
							}
						}
					}	
				}
				
			} catch (InvalidSyntaxException isEx) {
				isEx.printStackTrace();
			}
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
			if (oldDbService!=null && databaseService!=oldDbService) {
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
	
	/**
	 * Returns the database service by driver class name.
	 * @param driverClaName the driver cla name
	 * @return the database service by driver class name
	 */
	private static HibernateDatabaseService getDatabaseServiceByDriverClassName(String driverClaName) {
		Vector<HibernateDatabaseService> dbServices = new Vector<>(getDatabaseServices().values()); 
		for (int i = 0; i < dbServices.size(); i++) {
			if (dbServices.get(i).getDriverClassName().equals(driverClaName)) {
				return dbServices.get(i);
			}
		}
		return null;
	}
	
}
