package de.enflexit.db.hibernate.properties;

import java.util.List;

import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.PropertyValue;
import de.enflexit.common.properties.bus.PropertyBusService;
import de.enflexit.db.hibernate.HibernateUtilities;
import de.enflexit.db.hibernate.SessionFactoryMonitor;
import de.enflexit.db.hibernate.SessionFactoryMonitor.SessionFactoryState;

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
		
		// --- Collect description of SessionFactoryState -----------
		String[] sfsArray = new String[SessionFactoryState.values().length];
		int s = 0;
		for (SessionFactoryState sfs : SessionFactoryState.values()) {
			sfsArray[s] = sfs.name() + " (" + sfs.getDescription() + ")";
			s++;
		}
		
		// ----------------------------------------------------------
		// --- Fill result properties -------------------------------
		// ----------------------------------------------------------
		List<String> factoryList = HibernateUtilities.getSessionFactoryIDList();
		for (int i = 0; i < factoryList.size(); i++) {
			String factoryID = factoryList.get(i);
			properties.setStringValue("factory[" + i + "]", factoryID);
			
			// --- Add SessionFactoryState and descriptions ---------
			SessionFactoryMonitor sfMon = HibernateUtilities.getSessionFactoryMonitor(factoryID);
			PropertyValue pValue = new PropertyValue(sfMon.getSessionFactoryState().name());
			pValue.setValueOptions(sfsArray);
			pValue.setValueOptionsOnly(true);
			properties.setValue("factory[" + i + "].state", pValue);
			
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
