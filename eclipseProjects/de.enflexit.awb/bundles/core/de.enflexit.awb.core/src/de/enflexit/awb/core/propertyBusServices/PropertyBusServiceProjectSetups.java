package de.enflexit.awb.core.propertyBusServices;

import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.project.setup.SimulationSetups;
import de.enflexit.common.properties.Properties;
import de.enflexit.common.properties.PropertyMessage;
import de.enflexit.common.properties.bus.ApplicationPropertyBus;
import de.enflexit.common.properties.bus.PropertyBusService;

/**
 * The Class PropertyBusServiceProjectSetups can be used to get all available
 * project setups for any number of projects through the {@link ApplicationPropertyBus}.
 * To achieve this, the desired projects have to be specified in brackets after the 
 * performative like this: Project.setups[game_of_life, graphtest,...].
 *
 * @author Daniel Bormann - EnFlex.IT GmbH
 */
public class PropertyBusServiceProjectSetups implements PropertyBusService {

	/* (non-Javadoc)
	* @see de.enflexit.common.properties.bus.PropertyBusService#getPerformative()
	*/
	@Override
	public String getPerformative() {
		return "PROJECT.SETUPS";
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

		if (arguments == null) {
			return properties;
		}
		String[] projects = arguments.split(",");
		for (int i = 0; i < projects.length; i++) {
			Project project = Project.load(projects[i].trim());
			// TODO hide pop-ups when project can't be loaded
			
			if (project != null) {
				SimulationSetups setups = project.getSimulationSetups();
				int setupCounter = 0;
				for (String setup : setups.keySet()) {
					properties.setStringValue("project["+i+"]"+"setup["+setupCounter+"]", setup);
					setupCounter++;
				}
				
			} else {
				// TODO The get endpoint can't handle property messages and is not setup to deal with errors. How to deal with this problem?
				properties.setPropertyMessage(PropertyMessage.MessageType.Error, properties.getPropertyMessage()+"project["+i+"] could not be found");
			}
		}
		
		return properties;
	}

}