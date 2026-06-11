package de.enflexit.awb.core.propertyBusServices;


import java.text.SimpleDateFormat;
import java.util.Date;

import de.enflexit.awb.core.update.UpdateCheckCoordinatorBackend;
import de.enflexit.awb.core.update.UpdateCheckStatusBackend;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.bus.PropertyBusService;

/**
 * The Class PropertyBusServiceUpdateBackend.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class PropertyBusServiceUpdateCheckBackend implements PropertyBusService{
	
	private static final String PENDING = "updatecheck.backend.ispending";
	private static final String IS_UPDATE_AVAILABLE = "updatecheck.backend.isavailable";
	private static final String LAST_CHECK = "updatecheck.backend.lastcheck";

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getPerformative()
	*/
	@Override
	public String getPerformative() {
		return "UPDATE.BACKEND.CHECK";
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#setProperties(de.enflexit.common.properties.Properties, java.lang.String)
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
		
		UpdateCheckCoordinatorBackend.getInstance().triggerCheck();
		UpdateCheckStatusBackend status = UpdateCheckCoordinatorBackend.getInstance().getUpdateCheckStatusBackend();
		if (status.isPending()) {
			properties.setBooleanValue(PENDING, true);
			return properties;
		}
		properties.setBooleanValue(IS_UPDATE_AVAILABLE, status.isUpdateAvailable());
		properties.setStringValue(LAST_CHECK, new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date(status.getLastCheck())));
				
		return properties;
	}

}