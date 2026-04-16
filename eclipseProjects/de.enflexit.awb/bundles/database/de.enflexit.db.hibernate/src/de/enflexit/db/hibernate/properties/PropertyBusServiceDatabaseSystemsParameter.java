package de.enflexit.db.hibernate.properties;

import java.util.List;

import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.bus.PropertyBusService;
import de.enflexit.db.hibernate.HibernateDatabaseService;
import de.enflexit.db.hibernate.HibernateUtilities;

/**
 * The Class PropertyBusServiceDatabaseSystems.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PropertyBusServiceDatabaseSystemsParameter implements PropertyBusService {

	/* (non-Javadoc)
	 * @see de.enflexit.common.properties.bus.PropertyBusService#getPerformative()
	 */
	@Override
	public String getPerformative() {
		return "DB.SYSTEMS.PARAMETER";
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getProperties(de.enflexit.common.properties.Properties, java.lang.String)
	*/
	@Override
	public Properties getProperties(Properties properties, String arguments) {

		if (properties==null) properties = new Properties();
		
		List<String> dbNameList = HibernateUtilities.getDatabaseSystemList();
		for (int i = 0; i < dbNameList.size(); i++) {
			String dbName = dbNameList.get(i);
			properties.setStringValue("dbSystem[" + i + "]", dbName);
			
			// --- Get the database service --------------------------------
			HibernateDatabaseService dbService = HibernateUtilities.getDatabaseService(dbName);
			
			java.util.Properties defaultProperties = dbService.getHibernateDefaultPropertySettings();
			
			for (Object keyObject : defaultProperties.keySet()) {
				String key = (String) keyObject;
				String defaultValue = (String) defaultProperties.get(key);
				
				if (key.toLowerCase().equals("port")==true) {
					properties.setIntegerValue("dbSystem[" + i + "]." + key, Integer.parseInt(defaultValue));	
				} else {
					properties.setStringValue("dbSystem[" + i + "]." + key, defaultValue);
				}
			}
		}
		return properties;
	}
	
	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getProperties(de.enflexit.common.properties.Properties, java.lang.String)
	*/
	@Override
	public boolean setProperties(Properties properties, String arguments) {
		return false;
	}

}
