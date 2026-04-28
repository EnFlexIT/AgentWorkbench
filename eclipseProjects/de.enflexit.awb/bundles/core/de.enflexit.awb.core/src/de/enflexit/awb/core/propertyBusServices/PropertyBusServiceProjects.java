package de.enflexit.awb.core.propertyBusServices;

import de.enflexit.awb.core.Application;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.bus.ApplicationPropertyBus;
import de.enflexit.common.properties.bus.PropertyBusService;

/**
 * The Class PropertyBusServiceProjects can be used to get all available
 * project names through the {@link ApplicationPropertyBus}.
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class PropertyBusServiceProjects implements PropertyBusService {
	
	public final static String PROJECT= "project[X]";

	@Override
	public String getPerformative() {
		return "PROJECTS";
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
		
		int projectCounter = 0;
		String[] projectFolders = Application.getGlobalInfo().getProjectSubDirectories();
		if (projectFolders == null) {
			return properties;
		}
		for (String project : projectFolders) {
			properties.setStringValue(PROJECT.replace("X", String.valueOf(projectCounter)), project);
			projectCounter++;
		}
		
		return properties;
	}

}