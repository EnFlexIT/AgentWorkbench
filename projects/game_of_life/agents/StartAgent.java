package game_of_life.agents;

import jade.core.Agent;
import application.Application;


public class StartAgent extends Agent{

	@Override
	protected void setup() {
		
		//number of Agents to be started
		  int nbRow = 30;
		  int nbCol = 30;
		  
		//stored in an object which will be transfered to loadDistributor agent
		  Object [] obj = new Object[2];
		  obj[0] = nbRow;
		  obj[1] = nbCol;
		  
		
			//Start LoadDistributor Agent
			Application.JadePlatform.jadeAgentStart("AgentControler",
					"game_of_life.agents.SimulationServiceControllerAgent", obj);
	}
	
	
	@Override
	protected void takeDown() {
		// TODO Auto-generated method stub
		super.takeDown();
	}
}