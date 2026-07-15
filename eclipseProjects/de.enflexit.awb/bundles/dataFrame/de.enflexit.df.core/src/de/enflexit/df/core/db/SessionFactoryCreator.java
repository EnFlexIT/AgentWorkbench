package de.enflexit.df.core.db;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;

import de.enflexit.db.dataSources.AbstractDataSource;
import de.enflexit.db.dataSources.CsvDataSource;
import de.enflexit.db.dataSources.DatabaseDataSource;
import de.enflexit.db.dataSources.ExcelDataSource;
import de.enflexit.db.hibernate.ColumnOrderingStrategyAsDefinedInClass;
import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.HibernateUtilities;
import de.enflexit.db.hibernate.connection.DatabaseConnectionManager;
import de.enflexit.db.hibernate.connection.HibernateDatabaseConnectionService;
import de.enflexit.db.hibernate.gui.DatabaseSettings;
import de.enflexit.db.userManagement.UserManagementDataModelHelper;

/**
 * The Class SessionFactoryCreator provides static help functions 
 * to control the SessionFactory of the Background Systems Bundle.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SessionFactoryCreator implements HibernateDatabaseConnectionService {

	public static final String SESSION_FACTORY_PREFIX = "de.enflexit.df.core";
	
	private static final String cfgFile = 				"/de/enflexit/df/core/db/hibernate.cfg.xml";
	private static final String modelClassesPackage = 	"/de/enflexit/df/core/db/";
	
	private Bundle localBundle;
	
	private Integer workbookID;
	private String factoryID;
	private Configuration configuration;

	private int hibernateBatchSize = 10;
	
	
	/**
	 * Instantiates a new session factory creator.
	 * @param workbookID the workbook ID
	 */
	public SessionFactoryCreator(Integer workbookID) {
		this.setWorkbookID(workbookID);
	}
	
	/**
	 * Sets the workbook ID.
	 * @param workbookID the new workbook ID
	 */
	public void setWorkbookID(Integer workbookID) {
		this.workbookID = workbookID;
	}
	/**
	 * Returns the workbook ID.
	 * @return the workbook ID
	 */
	public Integer getWorkbookID() {
		return workbookID;
	}
	
	/**
	 * Sets the session factory ID for this database service.
	 * @param factoryID the new factory ID
	 */
	public void setFactoryID(String factoryID) {
		this.factoryID = factoryID;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.connection.HibernateDatabaseConnectionService#getFactoryID()
	 */
	@Override
	public String getFactoryID() {
		if (factoryID==null) {
			factoryID = SESSION_FACTORY_PREFIX + "." + this.getWorkbookID(); 
		}
		return factoryID;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.connection.HibernateDatabaseConnectionService#getConfiguration()
	 */
	@Override
	public Configuration getConfiguration() {
		if (configuration==null) {
			URL url = this.getLocalBundle().getResource(cfgFile);
			configuration = new Configuration().configure(url);
			this.addMappingFileResources(configuration);
			this.addColumnOrderingStrategy(configuration);
			this.addInternalHibernateProperties(configuration);
		}
		return configuration;
	}
	
	/**
	 * Returns the local bundle.
	 * @return the local bundle
	 */
	public Bundle getLocalBundle() {
		if (localBundle==null) {
			localBundle = FrameworkUtil.getBundle(SessionFactoryCreator.class);
		}
		return localBundle;
	}
	
	/**
	 * Adds internal hibernate configuration properties.
	 * @param configuration the configuration to be used
	 */
	private void addInternalHibernateProperties(Configuration configuration) {
		configuration.setProperty("hibernate.jdbc.batch_size", this.getHibernateBatchSize().toString());
	}
	/**
	 * Gets the hibernate batch size.
	 * @return the hibernate batch size
	 */
	public Integer getHibernateBatchSize() {
		return hibernateBatchSize;
	}
	
	/**
	 * Adds the column ordering strategy.
	 * @param configuration the configuration
	 */
	private void addColumnOrderingStrategy(Configuration configuration) {
		configuration.setColumnOrderingStrategy(new ColumnOrderingStrategyAsDefinedInClass());
		//configuration.setColumnOrderingStrategy(new ColumnOrderingStrategyStandard());
	}
	
	/**
	 * Adds the hibernate mapping files to the configuration.
	 * @param conf the current hibernate configuration
	 */
	private void addMappingFileResources(Configuration conf) {
		
		Bundle bundle = getLocalBundle();
		if (conf==null || bundle==null) return;
		
		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
		if (bundleWiring==null) return; 
		
		// --- Just for a stepwise development and verification ---------------
		List<String> excludeList = new ArrayList<>();
		excludeList.add("SessionFactoryCreator");
		
		Vector<String> modelClasses = new Vector<>(bundleWiring.listResources(modelClassesPackage, "*.class", BundleWiring.LISTRESOURCES_LOCAL));
		for (int i = 0; i < modelClasses.size(); i++) {
			try {
				
				String modelClassName = modelClasses.get(i).replace("/", ".").replace(".class", "");
				if (excludeList!=null && excludeList.size()>0 && this.isExcludedClass(modelClassName, excludeList)==true) continue;
				
				Class<?> modelClass = Class.forName(modelClassName);
				conf.addAnnotatedClass(modelClass);
				
			} catch (ClassNotFoundException cnfEx) {
				cnfEx.printStackTrace();
			}
		}
		
		// --- Add classes of DataSources -------------------------------------
		conf.addAnnotatedClass(AbstractDataSource.class);
		conf.addAnnotatedClass(CsvDataSource.class);
		conf.addAnnotatedClass(ExcelDataSource.class);
		conf.addAnnotatedClass(DatabaseDataSource.class);
		
		// --- Load user management data model classes from separate bundle ---
		UserManagementDataModelHelper.addUserManagementDataModelClasses(conf);
	}
	/**
	 * Checks if the excludeList contains the specified class name.
	 *
	 * @param className the class name
	 * @param excludeList the exclude list
	 * @return true, if is excluded class
	 */
	private boolean isExcludedClass(String className, List<String> excludeList) {
		String simpleClassName = className.substring(className.lastIndexOf(".")+1);
		return excludeList.contains(simpleClassName);
	}
	
	
	// ------------------------------------------------------------------------
	// --- Handling for DB session factory and its configuration --------------
	// ------------------------------------------------------------------------
	/**
	 * Creates a new DataWorkbookDatabaseHandler.
	 *
	 * @param dbDS the DatabaseDataSource to use
	 * @return the data frame database handler
	 */
	public DataWorkbookDatabaseHandler createDataWorkbookDatabaseHandler(DatabaseDataSource dbDS) {
		if (dbDS==null) return null;
		return new DataWorkbookDatabaseHandler(this.getNewDatabaseSession(dbDS));
	}
	/**
	 * Returns the new hibernate database session.
	 *
	 * @param dbDS the DatabaseDataSource to use
	 * @return the new database session
	 */
	public Session getNewDatabaseSession(DatabaseDataSource dbDS) {
		return getNewDatabaseSession(dbDS, false);
	}
	/**
	 * Returns the new hibernate database session.
	 *
	 * @param dbDS the DatabaseDataSource to use
	 * @param isResetSessionFactory the is reset session factory
	 * @return the new database session
	 */
	public Session getNewDatabaseSession(DatabaseDataSource dbDS, boolean isResetSessionFactory) {
		DatabaseSettings dbSettings = DatabaseSettings.fromDataSource(dbDS);
		return getNewDatabaseSession(dbSettings, isResetSessionFactory);
	}
	
	/**
	 * Creates a new DataWorkbookDatabaseHandler.
	 * @param factoryID the factory ID to be used for the connection
	 * @return the data frame database handler
	 */
	public DataWorkbookDatabaseHandler createDataWorkbookDatabaseHandler(String factoryID) {
		if (factoryID==null || factoryID.isBlank()==true) return null;
		return new DataWorkbookDatabaseHandler(this.getNewDatabaseSession(factoryID));
	}
	/**
	 * Returns the new hibernate database session.
	 * @param factoryID the factory ID to be used for the connection
	 * @return the new database session
	 */
	public Session getNewDatabaseSession(String factoryID) {
		return this.getNewDatabaseSession(factoryID, false);
	}
	/**
	 * Returns the new hibernate database session.
	 *
	 * @param factoryID the factory ID to be used for the connection
	 * @param isResetSessionFactory the is reset session factory
	 * @return the new database session
	 */
	public Session getNewDatabaseSession(String factoryID, boolean isResetSessionFactory) {
		DatabaseSettings dbSettings = DatabaseConnectionManager.getInstance().getDatabaseSettings(factoryID);
		return getNewDatabaseSession(dbSettings, isResetSessionFactory);
	}
	
	/**
	 * Gets the new hibernate database session.
	 *
	 * @param dbSettings the db settings
	 * @param isResetSessionFactory the reset session factory
	 * @return the new database session
	 */
	private Session getNewDatabaseSession(DatabaseSettings dbSettings, boolean isResetSessionFactory) {
		
		if (dbSettings==null) return null;
		Properties propsToRead = dbSettings.getHibernateDatabaseSettings();

		// --- Put current DB settings into configuration -----------
		Configuration configuration = this.getConfiguration();
		Properties propsToEdit = configuration.getProperties();
		List<Object> keyList = new ArrayList<>(propsToRead.keySet());
		for (int i = 0; i < keyList.size(); i++) {
			String key = (String) keyList.get(i);
			String value = propsToRead.getProperty(key);
			propsToEdit.setProperty(key, value);
		}
		
		HibernateDatabaseService dbService = HibernateUtilities.getDatabaseService(dbSettings.getDatabaseSystemName());
		if (dbService!=null) {
			propsToEdit.put(HibernateDatabaseService.HIBERNATE_PROPERTY_DriverClass, dbService.getDriverClassName());
		} else {
			return null;
		}
		
		// --- Create SessionFactory -------------------------------- 
		SessionFactory sf = HibernateUtilities.getSessionFactory(this.getFactoryID(), configuration, isResetSessionFactory, true);
		if (sf!=null) {
			return sf.openSession();
		}
		return null;
	}
	/**
	 * Closes the current session factory.
	 */
	public void closeSessionFactory() {
		HibernateUtilities.closeSessionFactory(this.getFactoryID());
	}

	
	/**
	 * Sets the statistics for the SessionFactory enabled (or not).
	 * @param setEnabled the set enabled
	 */
	public void setStatisticsEnabled(boolean setEnabled) {
		HibernateUtilities.setStatisticsEnabled(this.getFactoryID(), setEnabled);
	}
	/**
	 * Write the SessionFactory statistics.
	 */
	public void writeStatistics() {
		HibernateUtilities.writeStatistics(this.getFactoryID());
	}
	
}
