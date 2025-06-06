package org.awb.env.networkModel.controller;

import org.awb.env.networkModel.visualisation.DisplayAgent;

import de.enflexit.awb.core.environment.EnvironmentController;
import de.enflexit.awb.core.environment.EnvironmentType;
import de.enflexit.language.Language;
import jade.core.Agent;

/**
 * The Class EnvironmentTypeService.
 */
public class EnvironmentTypeService implements de.enflexit.awb.core.environment.EnvironmentTypeService {

	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentTypeService#getEnvironmentType()
	 */
	@Override
	public EnvironmentType getEnvironmentType() {
		// --- Definition of the Graph and Network Model environment type -----
		String envKey = "gridEnvironment";
		String envDisplayName = "Graph bzw. Netzwerk";
		String envDisplayNameLanguage = Language.DE;
		Class<? extends EnvironmentController> envControllerClass = GraphEnvironmentController.class;
		Class<? extends Agent> displayAgentClass = DisplayAgent.class;
		return new EnvironmentType(envKey, envDisplayName, envDisplayNameLanguage, envControllerClass, displayAgentClass);
	}

}
