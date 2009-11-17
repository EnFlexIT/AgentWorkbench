package mas.display;

import java.awt.Dimension;
import java.awt.Point;

import jade.core.Agent;
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
	
	public void setup(){
		// Create and initialize DisplayableData object
		posData = new DisplayableData(this);
		posData.setPosition(new Point(50,30));
		posData.setAgentSize(new Dimension(30,10));
		posData.setPlaygroundSize(new Dimension (300,300));
		posData.setAgentType(this.getClass().getSimpleName());
		
		addBehaviour(new RegisterBehaviour());
			
//		MessageTemplate mt = jade.proto.SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE);
//		responder = new SubscriptionResponder(this, mt);
		

//		addBehaviour(new MovingBehaviour(this));
//		addBehaviour(new CollisionBehaviour(this));
		
	}
	
	public void takeDown(){
		try{
			DFService.deregister(this);
		}catch (FIPAException fe){
			fe.printStackTrace();
		}
	}

	@Override
	public String getAgentType() {
		return this.posData.getAgentType();
	}

	@Override
	public Point getPosition() {
		return this.posData.getPosition();
	}

	@Override
	public Dimension getSize() {
		return this.posData.getAgentSize();
	}
}
