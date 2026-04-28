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

	public final static String FACTORY_FACTORYID="factory[X].factoryID";
	public final static String FACTORY_DBSYSTEM="factory[X].dbSystem";
	public final static String FACTORY_HIBERNATE_CONNECTION_DRIVER_CLASS="factory[X].hibernate.connection.driver_class";
	public final static String FACTORY_HIBERNATE_CONNECTION_URL="factory[X].hibernate.connection.url";
	public final static String FACTORY_HIBERNATE_DEFAULT_CATALOG="factory[X].hibernate.default_catalog";
	public final static String FACTORY_HIBERNATE_CONNECTION_USERNAME="factory[X].hibernate.connection.username";
	public final static String FACTORY_HIBERNATE_CONNECTION_PASSWORD="factory[X].hibernate.connection.password";

	@Override
	public String getPerformative() {
		
		return "DB.CONN.FACTORY.GET";
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getProperties(de.enflexit.common.properties.Properties, java.lang.String)
	*/
	@Override
	public boolean setProperties(Properties properties, String arguments) {

		return false;
	}


	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getProperties(de.enflexit.common.properties.Properties, java.lang.String)
	*/	
	@Override
	public Properties getProperties(Properties properties, String arguments) {

		if (properties == null) properties = new Properties();

		List<String> factoryList = HibernateUtilities.getSessionFactoryIDList();
		// --- Iterate over list of known factories ---------------------------------------------------------
		for (int factoryCounter = 0; factoryCounter < factoryList.size(); factoryCounter++) {
			String factoryID = factoryList.get(factoryCounter);
			
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
			properties.setStringValue(FACTORY_FACTORYID.replace("X", String.valueOf(factoryCounter)) , factoryID);
			properties.setStringValue(FACTORY_DBSYSTEM.replace("X", String.valueOf(factoryCounter)), dbSystem);
			properties.setStringValue(FACTORY_HIBERNATE_CONNECTION_DRIVER_CLASS.replace("X", String.valueOf(factoryCounter)), driver);
			properties.setStringValue(FACTORY_HIBERNATE_CONNECTION_URL.replace("X", String.valueOf(factoryCounter)), url);
			properties.setStringValue(FACTORY_HIBERNATE_DEFAULT_CATALOG.replace("X", String.valueOf(factoryCounter)), defaultCatalog);
			properties.setStringValue(FACTORY_HIBERNATE_CONNECTION_USERNAME.replace("X", String.valueOf(factoryCounter)), user);
			properties.setStringValue(FACTORY_HIBERNATE_CONNECTION_PASSWORD.replace("X", String.valueOf(factoryCounter)), password);

		}
		return properties;
	}

}