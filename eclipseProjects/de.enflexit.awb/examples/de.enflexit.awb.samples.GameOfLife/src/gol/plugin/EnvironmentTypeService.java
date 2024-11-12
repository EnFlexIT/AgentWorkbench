package gol.plugin;

import de.enflexit.language.Language;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.environment.EnvironmentType;
import gol.environment.DisplayAgent;
import gol.environment.SquaredEnvironmentController;
import jade.core.Agent;

/**
 * The Class EnvironmentTypeService.
 */
public class EnvironmentTypeService implements agentgui.core.environment.EnvironmentTypeService {

	/* (non-Javadoc)
	 * @see agentgui.core.environment.EnvironmentTypeService#getEnvironmentType()
	 */
	@Override
	public EnvironmentType getEnvironmentType() {
		
		String envKey = "Squared";
		String envDisplayName = "Discrete squares environment";
		String envDisplayNameLanguage = Language.EN;
		Class<? extends EnvironmentController> envControllerClass = SquaredEnvironmentController.class;
		Class<? extends Agent> displayAgentClass = DisplayAgent.class;
		return new EnvironmentType(envKey, envDisplayName, envDisplayNameLanguage, envControllerClass, displayAgentClass);
	}

}
