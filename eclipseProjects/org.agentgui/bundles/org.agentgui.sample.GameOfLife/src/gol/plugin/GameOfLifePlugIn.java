package gol.plugin;

import gol.environment.DisplayAgent;
import gol.environment.SquaredEnvironmentController;
import jade.core.Agent;
import agentgui.core.application.Language;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.environment.EnvironmentType;
import agentgui.core.plugin.PlugIn;
import agentgui.core.project.Project;

public class GameOfLifePlugIn extends PlugIn {

	private String myPlugInName = "Game of Life PlugIn";
	
	public GameOfLifePlugIn(Project currProject) {
		super(currProject);
	}

	@Override
	public String getName() {
		return myPlugInName;
	}

	@Override
	public void onPlugIn() {
		super.onPlugIn();
		
		// --- Register your own environment model to Agent.GUI -----
		this.addEnvironmentType(this.getMyEnvironmentType());
		
	}
	
	/**
	 * Returns my personal EnvironmentType.
	 * @return the my environment type
	 */
	private EnvironmentType getMyEnvironmentType() {
		
		String key = "Squared";
		String displayName = "Discrete squares environment";
		String displayNameLanguage = Language.EN;
		Class<? extends EnvironmentController> controllerClass = SquaredEnvironmentController.class;
		Class<? extends Agent> agentClass = DisplayAgent.class;
		
		EnvironmentType myEnvType = new EnvironmentType(key, displayName, displayNameLanguage, controllerClass, agentClass);
		return myEnvType;
	}
	
	
	
}
