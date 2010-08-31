package game_of_life.agents;

import jade.core.Agent;
import application.Application;

public class StartAgent extends Agent{

	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		
		//number of Agents to be started
		  int nbRow =10;
		  int nbCol =10;
		  
		//stored in an object which will be transfered to loadDistributor agent
		  Object [] obj = new Object[2];
		  obj[0] = nbRow;
		  obj[1] = nbCol;
		  
		 try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Start LoadDistributor Agent
			Application.JadePlatform.jadeAgentStart("AgentControler",
					"game_of_life.agents.SimulationServiceControllerAgent", obj);
		}
	
	@Override
	protected void takeDown() {
		super.takeDown();
	}
}