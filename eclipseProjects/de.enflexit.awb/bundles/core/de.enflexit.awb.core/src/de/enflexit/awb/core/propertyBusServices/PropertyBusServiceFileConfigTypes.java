package de.enflexit.awb.core.propertyBusServices;

import java.util.List;

import de.enflexit.common.ServiceFinder;
import de.enflexit.common.fileConfiguration.FileConfigurationService;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.bus.PropertyBusService;

/**
 * The Class PropertyBusServiceFileConfigTypes returns the performative of all currently
 * known {@link FileConfigurationService}.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class PropertyBusServiceFileConfigTypes implements PropertyBusService{

	public static final String CONFIGURATION_TYPE = "configurationtype[X]";
	
	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getPerformative()
	*/
	@Override
	public String getPerformative() {
		return "File.Configuration";
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
		
		// --- Find all FileConfigurationServices ---------------------------------------
		List<FileConfigurationService> fileConfigurationServices = ServiceFinder.findServices(FileConfigurationService.class);
		
		// --- Set results --------------------------------------------------------------
		for (int serviceCounter = 0; serviceCounter < fileConfigurationServices.size(); serviceCounter++) {
			properties.setStringValue(CONFIGURATION_TYPE.replace("X", serviceCounter+""), fileConfigurationServices.get(serviceCounter).getPerformative());
		}
		
		return properties;
	}

}