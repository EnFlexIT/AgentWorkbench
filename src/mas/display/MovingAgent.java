package mas.display;

import java.awt.Dimension;
import java.awt.Point;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.messaging.TopicManagementHelper;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;

/**
 * Simple example for a class extending GraphicalAgent
 * Defining agent(type)-specific properties 
 * 
 * @author nils
 *
 */
public class MovingAgent extends Agent implements DisplayableAgent{
//	SubscriptionResponder responder = null;
	
	DisplayableData posData;
	MovingBehaviour mb;
	
	public void setup(){
		// Create and initialize DisplayableData object
		posData = new DisplayableData();
		posData.setPos(50,30);
		posData.setAgentSize(50,20);
		posData.setPlaygroundSize(300,300);
		posData.setAgentType(this.getClass().getSimpleName());
		
		mb = new MovingBehaviour(this, 20);
		addBehaviour(mb);
			

		
	}
	
	public void takeDown(){
		mb.onEnd();
		
	}

	@Override
	public String getAgentType() {
		return this.posData.getAgentType();
	}

	

	

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return this.posData.getAgentHeight();
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return this.posData.getAgentWidth();
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return this.posData.getX();
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return this.posData.getY();
	}

	@Override
	public void setPos(int x, int y) {
		this.posData.setPos(x, y);		
	}

	@Override
	public int getPlaygroundHeight() {
		// TODO Auto-generated method stub
		return this.posData.getPlaygroundHeight();
	}

	@Override
	public int getPlaygroundWidth() {
		// TODO Auto-generated method stub
		return this.posData.getPlaygroundWidth();
	}
}
