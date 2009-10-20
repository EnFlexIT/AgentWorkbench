package mas.display;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

/**
 * Abstract parent class for graphical agents
 * Specifying properties shared by all graphical agents
 *  
 * @author nils
 *
 */
public abstract class GraphicalAgent extends Agent {
	MovingAgent self;
	DFAgentDescription[] displayAgents = null;
	// Default values, used if no arguments are specified
	int width=20;
	int height=20;
	int posX=20;
	int posY=20;
	int speedX=5;
	int speedY=2;
	String color="red";
	
	public void setup(){
		// Ask DF for DisplayAgents
		DFAgentDescription template=new DFAgentDescription();
		ServiceDescription sd=new ServiceDescription();
		sd.setType("Display Service");
		template.addServices(sd);
		try{
			displayAgents=DFService.search(this, template);
		}catch(FIPAException fe){
			fe.printStackTrace();
		}
		if(displayAgents!=null){
			ACLMessage registrationRequest = new ACLMessage(ACLMessage.REQUEST);
			for(int i=0; i<displayAgents.length; i++)
				registrationRequest.addReceiver(displayAgents[i].getName());
			registrationRequest.setContent(width+","+height+","+posX+","+posY+","+color);
			registrationRequest.setConversationId("register");
			send(registrationRequest);
		}		
	}
	
	public void takeDown(){
		
		ACLMessage deregistrationRequest=new ACLMessage(ACLMessage.REQUEST);
		for(int i=0;i<displayAgents.length;i++)
			deregistrationRequest.addReceiver(displayAgents[i].getName());
		deregistrationRequest.setConversationId("deregister");
		send(deregistrationRequest);
		
	}
}

