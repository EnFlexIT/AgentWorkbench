package agentgui.simulationService.agents;

import agentgui.simulationService.environment.EnvironmentModel;

public interface SimulationManagerInterface {

	/**
	 *  This method is used for initialising the simulation during the .setup()-method of the agent.
	 *  Here the enviroment model (see class agentgui.simulationService.environment.EnvironmentModel) 
	 *  should be set.
	 */
	public EnvironmentModel getInitialEnvironmentModel();
	
	/**
	 * 	The logic of the simulation is implemented here. It's highly recommended to use 
	 *  the provided methods for implementing the logic.
	 */
	public abstract void doSingleSimulationSequennce();
	
	
}
