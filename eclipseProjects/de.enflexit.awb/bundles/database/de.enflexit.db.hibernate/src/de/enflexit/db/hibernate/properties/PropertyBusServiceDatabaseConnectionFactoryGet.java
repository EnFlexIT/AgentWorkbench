package de.enflexit.db.hibernate.properties;

import java.util.List;

import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.bus.PropertyBusService;
import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.HibernateUtilities;
import de.enflexit.db.hibernate.connection.DatabaseConnectionManager;
import de.enflexit.db.hibernate.gui.DatabaseSettings;

/**
 * The Class PropertyBusServiceDatabaseConnectionFactoryGet is used to get the individual database settings 
 * for each factory through the {@link de.enflexit.common.properties.bus.ApplicationPropertyBus}.
 * Use {@link PropertyBusServiceDatabaseConnectionFactorySet#setProperties(Properties)} to change the factory settings.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class PropertyBusServiceDatabaseConnectionFactoryGet implements PropertyBusService {

	public static final String FACTORYID = "factoryID";
	public static final String DBSYSTEM = "dbSystem";

	@Override
	public String getPerformative() {
		
		return "DB.CONN.FACTORY.GET";
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#setProperties(de.enflexit.common.properties.Properties)
	* @see PropertyBusServiceDatabaseConnectionFactorySet
	*/
	@Override
	public boolean setProperties(Properties properties) {

		return false;
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getProperties(de.enflexit.common.properties.Properties)
	*/
	
	@Override
	public Properties getProperties(Properties properties) {

		if (properties == null) properties = new Properties();

		List<String> factoryList = HibernateUtilities.getSessionFactoryIDList();
		// --- Iterate over list of known factories ---------------------------------------------------------
		for (int i = 0; i < factoryList.size(); i++) {
			String factoryID = factoryList.get(i);
			
			// --- Get the database settings for the current factory ----------------------------------------
			DatabaseSettings dbs = DatabaseConnectionManager.getInstance().getDatabaseSettings(factoryID);
			String dbSystem = dbs.getDatabaseSystemName();
			
			// --- Get hibernate database settings and extract required values ------------------------------
			java.util.Properties hibernateDbs = dbs.getHibernateDatabaseSettings();
			String driver = hibernateDbs.getProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_DriverClass);
			String url = hibernateDbs.getProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_URL);
			String defaultCatalog = hibernateDbs.getProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_Catalog);
			String user = hibernateDbs.getProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_UserName);
			String password = hibernateDbs.getProperty(HibernateDatabaseService.HIBERNATE_PROPERTY_Password);

			// --- Set properties for the current factory ---------------------------------------------------
			properties.setStringValue("factory[" + i + "]."+FACTORYID , factoryID);
			properties.setStringValue("factory[" + i + "]."+DBSYSTEM, dbSystem);
			properties.setStringValue("factory[" + i + "]."+HibernateDatabaseService.HIBERNATE_PROPERTY_DriverClass, driver);
			properties.setStringValue("factory[" + i + "]."+HibernateDatabaseService.HIBERNATE_PROPERTY_URL, url);
			properties.setStringValue("factory[" + i + "]."+HibernateDatabaseService.HIBERNATE_PROPERTY_Catalog, defaultCatalog);
			properties.setStringValue("factory[" + i + "]."+HibernateDatabaseService.HIBERNATE_PROPERTY_UserName, user);
			properties.setStringValue("factory[" + i + "]."+HibernateDatabaseService.HIBERNATE_PROPERTY_Password, password);

		}
		return properties;
	}

}