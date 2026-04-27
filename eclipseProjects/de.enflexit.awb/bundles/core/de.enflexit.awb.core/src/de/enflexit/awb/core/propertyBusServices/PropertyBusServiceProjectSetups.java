package de.enflexit.awb.core.propertyBusServices;

import java.io.File;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.core.project.setup.SimulationSetups;
import de.enflexit.common.properties.Properties;
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

		if (arguments == null) return properties;
		
		String projectFolder = Application.getGlobalInfo().getPathProjects();
		if (projectFolder == null) {
			return properties;
		}
		
		// --- Split the arguments ----------------------------------------------------------------
		String[] projects = arguments.split(",");
		for (int i = 0; i < projects.length; i++) {
			// --- Prepare the file path to the next project and load -----------------------------
			File projectPath = new File(projectFolder+projects[i].trim());
			Project project = null;
			if (projectPath != null && projectPath.exists() == true) {
				project = Project.loadProjectXml(projectPath);
			}
			
			// --- If successful, get the available setups and set the results --------------------
			if (project != null) {
				SimulationSetups setups = project.getSimulationSetups();
				int setupCounter = 0;
				for (String setup : setups.keySet()) {
					properties.setStringValue("project["+i+"]."+"setup["+setupCounter+"]", setup);
					setupCounter++;
				}
			}
		}
		return properties;
	}
}