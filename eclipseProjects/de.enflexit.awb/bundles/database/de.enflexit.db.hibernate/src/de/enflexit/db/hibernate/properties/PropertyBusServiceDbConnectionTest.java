package de.enflexit.db.hibernate.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import de.enflexit.common.SecurityPolicies;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.PropertyMessage;
import de.enflexit.common.properties.bus.PropertyBusService;
import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.HibernateUtilities;

/**
 * The Class PropertyBusServiceDbConnectionTest can be used to test a database connection through the
 * {@link} ApplicationPropertyBus.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class PropertyBusServiceDbConnectionTest implements PropertyBusService {

	public static String DBSYSTEM = "dbSystem";
	
	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getPerformative()
	*/
	@Override
	public String getPerformative() {

		return "DB.CONN.TEST";
	}

	@Override
	public boolean setProperties(Properties properties) {
		
		// --- If properties aren't valid, don't test the connection and return false -----------------------
		if (this.hasValidProperties(properties)== false) return false;

		// --- Get the property values ----------------------------------------------------------------------
		String dbSystem = properties.getStringValue(DBSYSTEM);
		String driver = HibernateUtilities.getDatabaseService(dbSystem).getDriverClassName();
		String url = properties.getStringValue(HibernateDatabaseService.HIBERNATE_PROPERTY_URL);
		String catalog = properties.getStringValue(HibernateDatabaseService.HIBERNATE_PROPERTY_Catalog);
		String user = properties.getStringValue(HibernateDatabaseService.HIBERNATE_PROPERTY_UserName);
		String password = properties.getStringValue(HibernateDatabaseService.HIBERNATE_PROPERTY_Password);
		
		// --- Prepare the hibernate database settings to test the connection with --------------------------
		java.util.Properties hibernateDbs = new java.util.Properties();
		hibernateDbs.put(HibernateDatabaseService.HIBERNATE_PROPERTY_DriverClass, driver);
		hibernateDbs.put(HibernateDatabaseService.HIBERNATE_PROPERTY_URL, url);
		hibernateDbs.put(HibernateDatabaseService.HIBERNATE_PROPERTY_Catalog, catalog);
		hibernateDbs.put(HibernateDatabaseService.HIBERNATE_PROPERTY_UserName, user);
		hibernateDbs.put(HibernateDatabaseService.HIBERNATE_PROPERTY_Password, password);

		// --- Test the connection --------------------------------------------------------------------------
		Vector<String> userMessage = new Vector<>();
		boolean isTestSuccessful = HibernateUtilities.getDatabaseServiceByDriverClassName(driver).isDatabaseAccessible(hibernateDbs, userMessage, true);
		
		// --- Add a message depending on the result --------------------------------------------------------
		if (isTestSuccessful == false) {
			properties.setPropertyMessage(PropertyMessage.MessageType.Info, "Connection test failed.");
		} else {
			properties.setPropertyMessage(PropertyMessage.MessageType.Info, "Connection test successful.");
		}
		
		return isTestSuccessful;
	}		
	
	/**
	 * Checks for valid properties.
	 *
	 * @param properties2check the properties 2 check
	 * @return true, if successful
	 */
	private boolean hasValidProperties(Properties properties2check) {

		List<String> invalidValues = new ArrayList<>();
		
		// --- Check the property values and add them to invalidValues if invalid ---------------------------
		String dbSystem = properties2check.getStringValue(DBSYSTEM);
		if (dbSystem == null || dbSystem.isBlank() == true) {
			invalidValues.add("Database System is missing");
		}
		
		String driver = properties2check.getStringValue(HibernateDatabaseService.HIBERNATE_PROPERTY_DriverClass);
		if (driver == null || driver.isBlank()) {
			invalidValues.add("Driver class is missing");
		}
		
		String driverForDbSystem = HibernateUtilities.getDatabaseService(dbSystem).getDriverClassName();
		if (!driver.equals(driverForDbSystem)) {
			invalidValues.add("The specified driver does not match the database system");
		}
		
		String url = properties2check.getStringValue(HibernateDatabaseService.HIBERNATE_PROPERTY_URL);
		if (url == null || url.isBlank() == true) {
			invalidValues.add("URL is missing");
		}
		
		String catalog = properties2check.getStringValue(HibernateDatabaseService.HIBERNATE_PROPERTY_Catalog);
		if (catalog == null || catalog.isBlank() == true) {
			invalidValues.add("Catalog is missing");
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
		
		return null;
	}

}
