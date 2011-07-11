/**
 * 
 */
package agentgui.graphEnvironment.controller;

import java.util.HashMap;

import agentgui.graphEnvironment.networkModel.ComponentTypeSettings;
import jade.util.leap.Serializable;

/**
 * @author Satyadeep
 *
 */
public class ProjectUserObject implements Serializable{
	private static final long serialVersionUID = 7425147528482747232L;
	/**
	 * The component type settings used in the {@link GraphEnvironmentController}
	 */
	private HashMap<String, ComponentTypeSettings> currentCTS = null;

	/**
	 * @param currentCTS
	 */
	public ProjectUserObject() {
		currentCTS = new HashMap<String, ComponentTypeSettings>();
	}

	/**
	 * @return the currentCTS
	 */
	public HashMap<String, ComponentTypeSettings> getCurrentCTS() {
		return currentCTS;
	}

	/**
	 * @param currentCTS the currentCTS to set
	 */
	public void setCurrentCTS(HashMap<String, ComponentTypeSettings> currentCTS) {
		this.currentCTS = currentCTS;
	}
}
