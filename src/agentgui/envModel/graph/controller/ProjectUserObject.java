/**
 * 
 */
package agentgui.envModel.graph.controller;

import java.util.HashMap;

import agentgui.envModel.graph.networkModel.ComponentTypeSettings;
import jade.util.leap.Serializable;

/**
 * A custom user object encapsulating the required objects which can be placed in the Project object. 
 * You can add more attributes in this class if required, but be careful to cast and use the user object properly.
 * 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 *
 */
public class ProjectUserObject implements Serializable{
	private static final long serialVersionUID = 7425147528482747232L;
	/**
	 * The component type settings used in the {@link GraphEnvironmentController}
	 */
	private HashMap<String, ComponentTypeSettings> currentCTS = null;

	/**
	 * Default constructor
	 */
	public ProjectUserObject() {
		currentCTS = new HashMap<String, ComponentTypeSettings>();
	}

	/**
	 * Get the component type settings hashmap 
	 * @return the currentCTS
	 */
	public HashMap<String, ComponentTypeSettings> getCurrentCTS() {
		return currentCTS;
	}

	/**
	 * Set the component type settings hashmap
	 * @param currentCTS the currentCTS to set
	 */
	public void setCurrentCTS(HashMap<String, ComponentTypeSettings> currentCTS) {
		this.currentCTS = currentCTS;
	}
}
