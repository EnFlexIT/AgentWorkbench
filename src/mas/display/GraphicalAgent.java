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
 * Abstract parent class for graphical agents
 * Specifying properties shared by all graphical agents
 *  
 * @author nils
 *
 */
public abstract class GraphicalAgent extends Agent {
	public static final String svgNs="http://www.w3.org/2000/svg";
	
	MovingAgent self;
	DFAgentDescription[] displayAgents = null;
	// Default values, used if no arguments are specified
	int width=30;
	int height=30;
	int posX=20;
	int posY=20;
	int speedX=5;
	int speedY=2;
	String color="red";
	
	Element svgRepresentation;
	
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
//			if(this.svgRepresentation == null){
//				this.svgRepresentation = this.createDefaultRepresentation();
//			}
			
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
	
	private Element createDefaultRepresentation(){
		Element rep = SVGDOMImplementation.getDOMImplementation().createDocument(svgNs, "svg", null).createElementNS(svgNs, "rect");
		rep.setAttributeNS(null, "id", this.getLocalName());
		rep.setAttributeNS(null, "x", "30");
		rep.setAttributeNS(null, "y", "30");
		rep.setAttributeNS(null, "width", "30");
		rep.setAttributeNS(null, "height", "30");
		rep.setAttributeNS(null, "style", "fill:blue");
		return rep;
	}
}

