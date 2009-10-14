package mas.display;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Simple example class for moving agents
 * 
 * @author nils
 *
 */
public class MovingAgent extends Agent {
	MovingAgent self;
	AID displayAgent;
	// Default values, used if no arguments are specified
	int width=20;
	int height=20;
	int xPos=20;
	int yPos=20;
	int xSpeed=5;
	int ySpeed=2;
	String color="red";
	
	public void setup(){
		// Setting agent properties
		Object[] args=getArguments();
		if(args.length==7){
			width=Integer.parseInt((String)args[0]);
			height=Integer.parseInt((String)args[1]);
			xPos=Integer.parseInt((String)args[2]);
			yPos=Integer.parseInt((String)args[3]);
			xSpeed=Integer.parseInt((String)args[4]);
			ySpeed=Integer.parseInt((String)args[5]);
			color=(String)args[6];
		}
		System.out.println(args.length);
		
		
		self=this;
		// Finding display service
		DFAgentDescription template=new DFAgentDescription();
		DFAgentDescription[] result=null;
		ServiceDescription sd=new ServiceDescription();
		sd.setType("Display Service");
		template.addServices(sd);
		try{
			result=DFService.search(this, template);
		}catch(FIPAException fe){
			fe.printStackTrace();
		}
		if(result.length>0){		// If found, register at the display service
			displayAgent=result[0].getName();		// Always using first
			System.out.println("DisplayAgent "+displayAgent.getLocalName()+" found");		
			ACLMessage registerMsg=new ACLMessage(ACLMessage.REQUEST);
			registerMsg.addReceiver(displayAgent);
			registerMsg.setContent(width+","+height+","+xPos+","+yPos+","+color);
			registerMsg.setConversationId("register");
			send(registerMsg);
		}else{
			System.out.println("No display agent available");			
		}
		addBehaviour(new MovingBehaviour(this));
		addBehaviour(new CollisionBehaviour(this));
		
	}
	
	public void takeDown(){
		if(displayAgent!=null){
			ACLMessage deregMessage=new ACLMessage(ACLMessage.REQUEST);
			deregMessage.addReceiver(displayAgent);
			deregMessage.setConversationId("deregister");
			send(deregMessage);
		}
	}
}
