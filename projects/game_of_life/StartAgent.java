package game_of_life;

import game_of_life.gui.GameOfLifeGUI;
import game_of_life.gui.GameOfLifeObject;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import application.Application;
import jade.core.Agent;

public class StartAgent extends Agent{

	@Override
	protected void setup() {
		
		 //Start test Containers
		Application.JadePlatform.jadeContainerCreate("SubPlatformB");
		Application.JadePlatform.jadeContainerCreate("SubPlatformC");
	
		
		//number of Agents to be started
		  int nbRow = 10;
		  int nbCol = 10;
		  
		//stored in an object which will be transfered to loadDistributor agent
		  Object [] obj = new Object[2];
		  obj[0] = nbRow;
		  obj[1] = nbCol;
		  
		 try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			//Start LoadDistributor Agent
			Application.JadePlatform.jadeAgentStart("LoadDistributor",
					"game_of_life.agent_distributor.loadDistributorAgent", obj);
	}
	
	
	@Override
	protected void takeDown() {
		// TODO Auto-generated method stub
		super.takeDown();
	}
}
