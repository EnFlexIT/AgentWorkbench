package mas.display;

import java.io.IOException;
import java.io.Serializable;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.ls.LSSerializer;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

/**
 * Abstract parent class for agents having a "physical" position for being displayed by a display agent 
 *  
 * @author nils
 *
 */
public abstract class PhysicalAgent extends Agent {
	public static final String svgNs="http://www.w3.org/2000/svg";
	
	MovingAgent self;
	DFAgentDescription[] displayAgents = null;
	
	int posX;
	int posY;
	int speedX;
	int speedY;
	
	// Type of displayable agent
	String displayableType = null;
	
	
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
			registrationRequest.setContent(displayableType+","+posX+","+posY);
			registrationRequest.setConversationId("register");
			send(registrationRequest);
		}
		registerAsDisplayable();
	}
	
	public void takeDown(){
		
		ACLMessage deregistrationRequest=new ACLMessage(ACLMessage.REQUEST);
		for(int i=0;i<displayAgents.length;i++)
			deregistrationRequest.addReceiver(displayAgents[i].getName());
		deregistrationRequest.setConversationId("deregister");
		send(deregistrationRequest);
		
	}
	
	/**
	 * Register at the DF as a displayable agent
	 */
	private void registerAsDisplayable(){
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("displayable");
		sd.setName("displayable-"+displayableType);
		dfd.addServices(sd);
		try{
			DFService.register(this, dfd);
		}catch (FIPAException fe){
			fe.printStackTrace();
		}
	}
}

