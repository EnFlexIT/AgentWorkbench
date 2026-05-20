package de.enflexit.awb.core.propertyBusServices;


import java.text.SimpleDateFormat;
import java.util.Date;
import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.update.UpdateCheckCoordinator;
import de.enflexit.awb.core.update.UpdateCheckStatus;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.bus.PropertyBusService;

/**
 * The Class PropertyBusServiceUpdateBackend.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class PropertyBusServiceUpdateCheckBackend implements PropertyBusService{
	

	private static final String PENDING = "updatecheck.backend.ispending";
	private static final String ISUPDATEAVAILABLE = "updatecheck.backend.isavailable";
	private static final String LASTCHECK = "updatecheck.backend.lastcheck";
	private static final String VERSION = "updatecheck.backend.version";

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getPerformative()
	*/
	@Override
	public String getPerformative() {
		return "UPDATE.CHECK";
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
		if (arguments == null) return properties;
		
		boolean forceNewCheck = arguments.equals("true") ? true : false;
		UpdateCheckCoordinator.getInstance().triggerCheck(forceNewCheck);
		UpdateCheckStatus status = UpdateCheckCoordinator.getInstance().getStatus();
		if (status.isPending()) {
			properties.setBooleanValue(PENDING, true);
			return properties;
		}
		properties.setBooleanValue(ISUPDATEAVAILABLE, status.isUpdateAvailable());
		// TODO Implement Version data
		properties.setStringValue(LASTCHECK, new SimpleDateFormat("dd.MM.yy HH:mm").format(new Date(status.getLastCheck())));
		
		Application.getGlobalInfo().setUpdateDateLastChecked(status.getLastCheck());
		
		return properties;
	}

}