package de.enflexit.db.hibernate.properties;

import java.util.List;

import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.bus.PropertyBusService;
import de.enflexit.db.hibernate.HibernateUtilities;

/**
 * The Class PropertyBusServiceDatabaseSystems.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PropertyBusServiceDatabaseSystems implements PropertyBusService {

	/* (non-Javadoc)
	 * @see de.enflexit.common.properties.bus.PropertyBusService#getPerformative()
	 */
	@Override
	public String getPerformative() {
		return "DB.SYSTEMS";
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.properties.bus.PropertyBusService#getProperties(de.enflexit.common.properties.Properties)
	 */
	@Override
	public Properties getProperties(Properties properties) {

		if (properties==null) properties = new Properties();
		
		List<String> dbNameList = HibernateUtilities.getDatabaseSystemList();
		for (int i = 0; i < dbNameList.size(); i++) {
			String dbName = dbNameList.get(i);
			properties.setStringValue("dbSystem[" + i + "]", dbName);
			
		}
		return properties;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.properties.bus.PropertyBusService#setProperties(de.enflexit.common.properties.Properties)
	 */
	@Override
	public boolean setProperties(Properties properties) {
		return false;
	}

}
