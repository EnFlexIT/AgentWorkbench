package gol.plugin;

import de.enflexit.awb.core.environment.EnvironmentController;
import de.enflexit.awb.core.environment.EnvironmentType;
import de.enflexit.language.Language;
import gol.environment.DisplayAgent;
import gol.environment.SquaredEnvironmentController;
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
		
		String envKey = "Squared";
		String envDisplayName = "Discrete squares environment";
		String envDisplayNameLanguage = Language.EN;
		Class<? extends EnvironmentController> envControllerClass = SquaredEnvironmentController.class;
		Class<? extends Agent> displayAgentClass = DisplayAgent.class;
		return new EnvironmentType(envKey, envDisplayName, envDisplayNameLanguage, envControllerClass, displayAgentClass);
	}

}
