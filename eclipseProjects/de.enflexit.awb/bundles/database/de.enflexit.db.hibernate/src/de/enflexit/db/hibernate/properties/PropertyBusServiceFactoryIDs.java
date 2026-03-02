package de.enflexit.db.hibernate.properties;

import java.util.List;

import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.bus.PropertyBusService;
import de.enflexit.db.hibernate.HibernateUtilities;

/**
 * The Class PropertyBusServiceFactoryIDs.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class PropertyBusServiceFactoryIDs implements PropertyBusService {

	/* (non-Javadoc)
	 * @see de.enflexit.common.properties.bus.PropertyBusService#getPerformative()
	 */
	@Override
	public String getPerformative() {
		return "DB.FACTORIES";
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.properties.bus.PropertyBusService#getProperties(de.enflexit.common.properties.Properties)
	 */
	@Override
	public Properties getProperties(Properties properties) {

		if (properties==null) properties = new Properties();
		
		List<String> factoryList = HibernateUtilities.getSessionFactoryIDList();
		for (int i = 0; i < factoryList.size(); i++) {
			String factoryID = factoryList.get(i);
			properties.setStringValue("factory[" + i + "]", factoryID);
			
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
