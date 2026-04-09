package de.enflexit.db.hibernate.properties;

import java.util.ArrayList;
import java.util.List;

import de.enflexit.common.SecurityPolicies;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.PropertyMessage;
import de.enflexit.common.properties.bus.PropertyBusService;
import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.connection.DatabaseConnectionManager;
import de.enflexit.db.hibernate.connection.GeneralDatabaseSettings;

/**
 * The Class PropertyBusServiceGeneralConnection.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class PropertyBusServiceGeneralConnection implements PropertyBusService {
	
	public static final String USE_FOR_EVERY_CONNECTION = "useForEveryFactory";
	public static final String DB_SYSTEM = "dbSystem";
	

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getPerformative()
	*/
	@Override
	public String getPerformative() {

		return "DB.CONN.GENERAL";
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#setProperties(de.enflexit.common.properties.Properties)
	*/
	@Override
	public boolean setProperties(Properties properties) {

		// --- If properties aren't valid, don't apply them and return false --------------------------------
		if (this.hasValidProperties(properties)== false) return false;
		
		// --- Get the new property values ------------------------------------------------------------------
		Boolean useForEveryDB = properties.getBooleanValue(USE_FOR_EVERY_CONNECTION);
		String dbSystem = properties.getStringValue(DB_SYSTEM);
		String driver = properties.getStringValue(HibernateDatabaseService.HIBERNATE_PROPERTY_DriverClass);
		String url = properties.getStringValue(HibernateDatabaseService.HIBERNATE_PROPERTY_URL);
		String defaultCatalog = properties.getStringValue(HibernateDatabaseService.HIBERNATE_PROPERTY_Catalog);
		String user = properties.getStringValue(HibernateDatabaseService.HIBERNATE_PROPERTY_UserName);
		String password = properties.getStringValue(HibernateDatabaseService.HIBERNATE_PROPERTY_Password);
		
		// --- Put new values into a java.util.Property instance --------------------------------------------
		java.util.Properties hibernateDbs = new java.util.Properties();
		hibernateDbs.put(DB_SYSTEM, dbSystem);
		hibernateDbs.put(HibernateDatabaseService.HIBERNATE_PROPERTY_DriverClass, driver);
		hibernateDbs.put(HibernateDatabaseService.HIBERNATE_PROPERTY_URL, url);
		hibernateDbs.put(HibernateDatabaseService.HIBERNATE_PROPERTY_Catalog, defaultCatalog);
		hibernateDbs.put(HibernateDatabaseService.HIBERNATE_PROPERTY_UserName, user);
		hibernateDbs.put(HibernateDatabaseService.HIBERNATE_PROPERTY_Password, password);
		
		GeneralDatabaseSettings gdbSettings = new GeneralDatabaseSettings();
		gdbSettings.setUseForEveryFactory(useForEveryDB);
		gdbSettings.setHibernateDatabaseSettings(hibernateDbs);

		
		// --- Try to save the new settings and return the result -------------------------------------------
		return DatabaseConnectionManager.getInstance().saveGeneralDatabaseSettings(gdbSettings);
	}

	/**
	 * Checks for valid properties. Adds an error message to the properties if invalid values are found.
	 *
	 * @param properties2check the properties to check
	 * @return true, if successful
	 */
	private boolean hasValidProperties(Properties properties2check) {

		List<String> invalidValues = new ArrayList<>();
		
		// --- Check the property values and add them to invalidValues if invalid ---------------------------
		Boolean useForEveryDB = properties2check.getBooleanValue(USE_FOR_EVERY_CONNECTION);
		if (useForEveryDB == null) {
			invalidValues.add(USE_FOR_EVERY_CONNECTION + " is null");
		}

		String dbSystem = properties2check.getStringValue(DB_SYSTEM);
		if (dbSystem == null || dbSystem.isBlank()) {
			invalidValues.add(DB_SYSTEM + " is empty");
		}

		String user = properties2check.getStringValue(HibernateDatabaseService.HIBERNATE_PROPERTY_UserName);
		String errorUser = SecurityPolicies.getUserNameError(user);
		if (errorUser != null) {
			invalidValues.add(errorUser);
		}

		String password = properties2check.getStringValue(HibernateDatabaseService.HIBERNATE_PROPERTY_Password);
		String errorPswd = SecurityPolicies.getPasswordError(password);
		if (errorPswd != null) {
			invalidValues.add(errorPswd);
		}

		// --- If invalid values were found, add an error message to the properties -------------------------
		if (invalidValues.size() > 0) {
			properties2check.setPropertyMessage(PropertyMessage.MessageType.Error, String.join(", ", invalidValues));
			return false;
		}
		return true;
	}

	
	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getProperties(de.enflexit.common.properties.Properties)
	*/
	@Override
	public Properties getProperties(Properties properties) {
		
		if (properties == null) properties = new Properties();
		
		// --- Get database settings and set the current db system ------------------------------------------
		GeneralDatabaseSettings databaseSettings = DatabaseConnectionManager.getInstance().getGeneralDatabaseSettings();
		String dbSystem = databaseSettings.getDatabaseSystemName();
		properties.setStringValue(DB_SYSTEM, dbSystem);
		properties.setBooleanValue(USE_FOR_EVERY_CONNECTION, databaseSettings.isUseForEveryFactory());

		// --- Iterate over hibernate database settings to extract keys and values --------------------------
		java.util.Properties hibernateDbs = databaseSettings.getHibernateDatabaseSettings();
		
		for (Object keyObject : hibernateDbs.keySet()) {
			String key = (String) keyObject;
			String value = (String) hibernateDbs.get(key);

			// --- Set property value -----------------------------------------------------------------------
				properties.setStringValue(key, value);
		}
		return properties;
	}

}