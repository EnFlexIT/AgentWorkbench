package de.enflexit.db.derby.server;

import java.util.ArrayList;
import java.util.List;

import de.enflexit.common.SecurityPolicies;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.PropertyMessage;
import de.enflexit.common.properties.bus.PropertyBusService;

/**
 * This Class is a PropertyBusService implementation for managing the
 * configuration of the Derby Network Server within the application. It is
 * registered under the performative "db.derby.networkserver" and allows other
 * components of the application to retrieve and update the Derby Network Server
 * settings through the {@link de.enflexit.common.properties.bus.ApplicationPropertyBus}.
 * 
 * @author Daniel Bormann
 */
public class PropertyBusServiceDerbyNetworkServer implements PropertyBusService {

	public static final String ISSTARTDERBYNETWORKSERVER = "isStartDerbyNetworkServer";
	public static final String HOSTIP = "host-IP";
	public static final String PORT = "port";
	public static final String USER = "userName";
	public static final String PASSWORD = "password";

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.enflexit.common.properties.bus.PropertyBusService#getPerformative()
	 */
	@Override
	public String getPerformative() {
		return "DB.DERBY.NETWORKSERVER";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.enflexit.common.properties.bus.PropertyBusService#setProperties(de.
	 * enflexit.common.properties.Properties)
	 */
	@Override
	public Properties getProperties(Properties properties) {
		
		if (properties == null) properties = new Properties();
		
		DerbyNetworkServerProperties derbyProperties = new DerbyNetworkServerProperties();
		properties.setBooleanValue(ISSTARTDERBYNETWORKSERVER, derbyProperties.isStartDerbyNetworkServer());
		properties.setStringValue(HOSTIP, derbyProperties.getHost());
		properties.setIntegerValue(PORT, derbyProperties.getPort());
		properties.setStringValue(USER, derbyProperties.getUserName());
		properties.setStringValue(PASSWORD, derbyProperties.getPassword());
		
		return properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.enflexit.common.properties.bus.PropertyBusService#setProperties(de.
	 * enflexit.common.properties.Properties)
	 */
	@Override
	public boolean setProperties(Properties properties) {
		
		// --- if values are invalid, don't apply and return false --------------------------------
		if (this.hasValidProperties(properties) == false) {
			return false;
		}
		// --- Shut down the Derby server to set new properties -----------------------------------
		if (DerbyNetworkServer.isExecuted()) DerbyNetworkServer.terminate();
		
		boolean isStart = properties.getBooleanValue(ISSTARTDERBYNETWORKSERVER);
		String host = properties.getStringValue(HOSTIP);
		Integer port = properties.getIntegerValue(PORT);
		String user = properties.getStringValue(USER);
		String password = properties.getStringValue(PASSWORD);
		
		// --- Set new properties and restart the server if necessary -----------------------------
		DerbyNetworkServerProperties derbyProperties = new DerbyNetworkServerProperties();
		boolean success = derbyProperties.setProperties(isStart, host, port, user, password);
		
		if (isStart == true) DerbyNetworkServer.execute();

		return success;
	}

	/**
	 * Checks for valid properties. Adds an error message to the properties if invalid values are found.
	 *
	 * @param properties2check the properties 2 check
	 * @return true if all required properties have valid values, else false
	 */
	private boolean hasValidProperties(Properties properties2check) {

		List<String> invalidValues = new ArrayList<>();
		
		// --- Check the property values and add them to invalidValues if invalid -----------------
		String host = properties2check.getStringValue(HOSTIP);
		if (host == null || host.isBlank() == true) {
			invalidValues.add("Host is empty.");
		}

		Integer port = properties2check.getIntegerValue(PORT);
		if (port == null || port < 1 || port > 65535) {
			invalidValues.add("Port number is invalid.");
		}

		String user = properties2check.getStringValue(USER);
		String errorUser = SecurityPolicies.getUserNameError(user);
		if (errorUser != null) {
			invalidValues.add(errorUser);
		}

		String password = properties2check.getStringValue(PASSWORD);
		String errorPswd = SecurityPolicies.getPasswordError(password);
		if (errorPswd != null) {
			invalidValues.add(errorPswd);
		}

		// --- If invalid values were found, add an error message to the properties ---------------
		if (invalidValues.size() > 0) {
			properties2check.setPropertyMessage(PropertyMessage.MessageType.Error, String.join(", ", invalidValues));
			return false;
		}
		return true;
	}

}