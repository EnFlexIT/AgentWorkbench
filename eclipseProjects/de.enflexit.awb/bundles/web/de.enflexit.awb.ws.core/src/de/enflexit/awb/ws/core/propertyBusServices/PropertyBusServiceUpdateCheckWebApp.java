package de.enflexit.awb.ws.core.propertyBusServices;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.enflexit.awb.ws.core.util.UpdateCheckCoordinatorWebApp;
import de.enflexit.awb.ws.core.util.UpdateCheckStatusWebApp;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.bus.PropertyBusService;

/**
 * The Class PropertyBusServiceUpdateCheckFrontend.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class PropertyBusServiceUpdateCheckWebApp implements PropertyBusService {

	private static final String PENDING = "updatecheck.frontend.ispending";
	private static final String ISUPDATEAVAILABLE = "updatecheck.frontend.isavailable";
	private static final String LASTCHECK = "updatecheck.frontend.lastcheck";
	private static final String NEW_VERSION = "updatecheck.frontend.version";
	private static final String CURRENT_VERSION = "updatecheck.frontend.currentversion";
	
	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getPerformative()
	*/
	@Override
	public String getPerformative() {
		return "UPDATE.FRONTEND.CHECK";
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
		
		UpdateCheckCoordinatorWebApp.getInstance().triggerCheck();
		UpdateCheckStatusWebApp status = UpdateCheckCoordinatorWebApp.getInstance().getUpdateCheckStatusWebApp();
		if (status.isPending()) {
			properties.setBooleanValue(PENDING, true);
			return properties;
		}
		properties.setBooleanValue(ISUPDATEAVAILABLE, status.isUpdateAvailable());
		properties.setStringValue(NEW_VERSION, status.getNewVersion());
		properties.setStringValue(CURRENT_VERSION, status.getCurrentVersion());
		properties.setStringValue(LASTCHECK, new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date(status.getLastCheck())));
		
		return properties;
	}

}