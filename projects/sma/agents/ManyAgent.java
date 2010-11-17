package sma.agents;

import agentgui.physical2Denvironment.MoveToPointBehaviour;
import agentgui.physical2Denvironment.ontology.Position;
import jade.core.Agent;
import jade.core.ServiceException;

public class ManyAgent extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setup(){
		Position destPos = new Position();
		destPos.setXPos(30);
		destPos.setYPos(20);
		
		try {
			addBehaviour(new MoveToPointBehaviour(this, destPos, 10.0f));
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
