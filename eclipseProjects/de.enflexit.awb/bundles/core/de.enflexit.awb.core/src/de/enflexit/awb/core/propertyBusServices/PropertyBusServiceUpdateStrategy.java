package de.enflexit.awb.core.propertyBusServices;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.update.AWBUpdater;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.bus.PropertyBusService;

/**
 * The Class PropertyBusServiceUpdateStrategy.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class PropertyBusServiceUpdateStrategy implements PropertyBusService {

	public static final String UPDATE_STRATEGY = "update.strategy";
	public static final String AUTOMATIC = "automatic";
	public static final String ASKUSER = "askuser";

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getPerformative()
	*/
	@Override
	public String getPerformative() {
		return "UPDATE.STRATEGY";
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#setProperties(de.enflexit.common.properties.Properties, java.lang.String)
	*/
	@Override
	public boolean setProperties(Properties properties, String arguments) {
		switch (properties.getStringValue(UPDATE_STRATEGY)) {
		case "automatic":
			Application.getGlobalInfo().setUpdateAutoConfiguration(AWBUpdater.UPDATE_MODE_AUTOMATIC);
			Application.getGlobalInfo().doSaveConfiguration();
			break;
		case "askuser":
			Application.getGlobalInfo().setUpdateAutoConfiguration(AWBUpdater.UPDATE_MODE_ASK);
			Application.getGlobalInfo().doSaveConfiguration();
			break;
		default:
			return false;
		}
		
		return true;
	}

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getProperties(de.enflexit.common.properties.Properties, java.lang.String)
	*/
	@Override
	public Properties getProperties(Properties properties, String arguments) {
		
		if (properties == null) properties = new Properties();
		String updateStrat = Application.getGlobalInfo().getUpdateAutoConfiguration() == AWBUpdater.UPDATE_MODE_AUTOMATIC? AUTOMATIC: ASKUSER;
		properties.setStringValue(UPDATE_STRATEGY, updateStrat);
		
		return properties;
	}

}
