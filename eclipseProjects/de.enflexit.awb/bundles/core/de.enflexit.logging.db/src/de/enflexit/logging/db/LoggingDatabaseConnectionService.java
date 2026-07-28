package de.enflexit.logging.db;

import java.net.URL;
import java.util.Vector;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;

import de.enflexit.db.hibernate.ColumnOrderingStrategyAsDefinedInClass;
import de.enflexit.db.hibernate.HibernateUtilities;
import de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState;
import de.enflexit.db.hibernate.connection.HibernateDatabaseConnectionService;
import de.enflexit.db.hibernate.gui.HibernateStateVisualizationService;
import de.enflexit.db.hibernate.gui.HibernateStateVisualizer;
import de.enflexit.logging.AwbLogbackConfigurator;
import de.enflexit.logging.appender.AwbDatabaseAppender;

/**
 * The Class LoggingDatabaseConnectionService provides static help functions 
 * to control the SessionFactory of the Background Systems Bundle.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class LoggingDatabaseConnectionService implements HibernateDatabaseConnectionService, HibernateStateVisualizationService {

	public static final String SESSION_FACTORY_ID = "de.enflexit.logging.db";
	
	private static final String pathPrefix = "/" + SESSION_FACTORY_ID.replace(".", "/") + "/";
	
	private static final String cfgFile = 				pathPrefix + "cfg/hibernate.cfg.xml";
	private static final String mappingFilesPackage = 	pathPrefix + "mappings/";
	private static final String modelClassesPackage = 	pathPrefix + "dataModel/";
	
	private Bundle localBundle;
	private Configuration configuration;
	
	private int hibernateBatchSize = 50;
	
	
	// ----------------------------------------------------
	// --- Singleton access for the current instance ------
	private static LoggingDatabaseConnectionService instance;
	public static LoggingDatabaseConnectionService getInstance() {
		if (instance==null) {
			instance = new LoggingDatabaseConnectionService();
		}
		return instance;
	}
	// --- Singleton access for the current instance ------
	// ----------------------------------------------------
	
	/**
	 * Instantiates a new database session factory handler.
	 * (Even we use a singleton here, for OSGI-service initiation a public construction is required)
	 */
	public LoggingDatabaseConnectionService() { 
		instance = this;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.db.hibernate.connection.HibernateDatabaseConnectionService#getFactoryID()
	 */
	@Override
	public String getFactoryID() {
		return SESSION_FACTORY_ID;
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
			HibernateStateVisualizer.registerStateVisualizationService(this);
		
		}
		return configuration;
	}
	
	/* (non-Javadoc)
	* @see de.enflexit.db.hibernate.gui.HibernateStateVisualizationService#setSessionFactoryState(java.lang.String, de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState)
	*/
	@Override
	public void setSessionFactoryState(String factoryID, SessionFactoryState sessionFactoryState) {
		
		if (factoryID.equals(LoggingDatabaseConnectionService.SESSION_FACTORY_ID)) {
			switch(sessionFactoryState) {
			case Created:
				AwbLogbackConfigurator.startAwbDatabaseAppender(new LoggingDataSource());
				break;
			case CheckDBConectionFailed:
			case CheckDBConnection:
			case Destroyed:
			case InitializationProcessFailed:
			case InitializationProcessStarted:
			case NotAvailableYet:
			default:
				AwbDatabaseAppender.getInstance().setWriteToLoggingStorage(false);
			}
		}
	}
	
	/**
	 * Gets the local bundle.
	 * @return the local bundle
	 */
	public Bundle getLocalBundle() {
		if (localBundle==null) {
			localBundle = FrameworkUtil.getBundle(LoggingDatabaseConnectionService.class);
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
		
		Vector<String> mappingResources = new Vector<>(bundleWiring.listResources(mappingFilesPackage, "*.xml", BundleWiring.LISTRESOURCES_LOCAL));
		for (int i = 0; i < mappingResources.size(); i++) {
			String mappingResource = mappingResources.get(i);
			conf.addResource(mappingResource);
		}
		
		Vector<String> modelClasses = new Vector<>(bundleWiring.listResources(modelClassesPackage, "*.class", BundleWiring.LISTRESOURCES_LOCAL));
		for (int i = 0; i < modelClasses.size(); i++) {
			
			try {		
				String modelClassName = modelClasses.get(i).replace("/", ".").replace(".class", "");
				Class<?> modelClass = Class.forName(modelClassName);
				conf.addAnnotatedClass(modelClass);
				
			} catch (ClassNotFoundException cnfEx) {
				cnfEx.printStackTrace();
			}
		}
	}
	
	// ------------------------------------------------------------------------
	// --- Handling for DB session factory and its configuration --------------
	// ------------------------------------------------------------------------
	/**
	 * Gets the new hibernate database session.
	 * @return the new database session
	 */
	public Session getNewDatabaseSession() {
		return getNewDatabaseSession(false);
	}
	/**
	 * Gets the new hibernate database session.
	 *
	 * @param isResetSessionFactory the reset session factory
	 * @return the new database session
	 */
	public Session getNewDatabaseSession(boolean isResetSessionFactory) {
		Session session = null;
		SessionFactory sf = HibernateUtilities.getSessionFactory(this.getFactoryID(), this.getConfiguration(), isResetSessionFactory, true);
		if (sf!=null) {
			session = sf.openSession();
		}
		return session;
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