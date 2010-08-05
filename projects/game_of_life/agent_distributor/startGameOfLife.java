package game_of_life.agent_distributor;

import application.Application;
import jade.core.Agent;

public class startGameOfLife extends Agent{

	@Override
	protected void setup() {
		
		 // ----- test Containers -----------------------------------------------
		//Application.JadePlatform.jadeContainerCreate("MainPlatformA");
		//Application.JadePlatform.jadeContainerCreate("SubPlatformB");
		//Application.JadePlatform.jadeContainerCreate("SubPlatformC");
		
		// ------ number of Agents to be started ---------------------------------
		// ------ only equal coordinates are accepted by the system (e.g 4*4)!!!!
		  int nbRow = 40;
		  int nbCol = 40;
		  
		// --- arguments for agents ----------------------------------------------
		  Object [] obj = new Object[2];
		  obj[0] = nbRow;
		  obj[1] = nbCol;
			
			//Start LoadDistributor Agent
			Application.JadePlatform.jadeAgentStart("AgentControler",
					"mas.agentsNew.SimulationServiceControllerAgent", obj);
	}
	
}
