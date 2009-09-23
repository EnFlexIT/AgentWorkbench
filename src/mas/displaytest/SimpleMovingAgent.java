package mas.displaytest;

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
 * To change default values, all 7 arguments have to be specified in the folowwing order:
 * 
 * width, height, initial pos x, initial pos y, initial speed x, initial speed y, color 
 * 
 * @author nils
 *
 */
public class SimpleMovingAgent extends Agent {
	SimpleMovingAgent self;
	AID DisplayAgent;
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
		ServiceDescription sd=new ServiceDescription();
		sd.setType("Display Service");
		template.addServices(sd);
		try{
			DisplayAgent=DFService.search(this, template)[0].getName();
		}catch(FIPAException fe){
			fe.printStackTrace();
		}
		System.out.println("DisplayAgent "+DisplayAgent.getLocalName()+" gefunden");
		
		// Registering at the display service
		ACLMessage registerMsg=new ACLMessage(ACLMessage.REQUEST);
		registerMsg.addReceiver(DisplayAgent);
		registerMsg.setContent(width+","+height+","+xPos+","+yPos+","+color);
		registerMsg.setConversationId("register");
		send(registerMsg);
		addBehaviour(new MovingBehaviour());
		addBehaviour(new CollisionBehaviour());
	}
	
	public void takeDown(){
		ACLMessage deregMessage=new ACLMessage(ACLMessage.REQUEST);
		deregMessage.addReceiver(DisplayAgent);
		deregMessage.setConversationId("deregister");
		send(deregMessage);
	}
	
	private class MovingBehaviour extends TickerBehaviour{

		public MovingBehaviour() {
			super(self, 50);			
		}

		@Override
		protected void onTick() {
			xPos+=xSpeed;
			yPos+=ySpeed;
			ACLMessage movingMsg=new ACLMessage(ACLMessage.REQUEST);
			movingMsg.addReceiver(DisplayAgent);
			movingMsg.setConversationId("move");
			movingMsg.setContent(xPos+","+yPos);
			send(movingMsg);
		}
		
	}
	
	private class CollisionBehaviour extends CyclicBehaviour{

		@Override
		public void action() {
			MessageTemplate mt=MessageTemplate.and(
					MessageTemplate.MatchPerformative(ACLMessage.INFORM),
					MessageTemplate.MatchConversationId("collision"));
			ACLMessage collisionMsg=receive(mt);
			if(collisionMsg!=null){
				switch(Integer.parseInt(collisionMsg.getContent())){
					case 1: xSpeed=-xSpeed; break;
					case 2: ySpeed=-ySpeed; break;
					case 3: xSpeed=-xSpeed; ySpeed=-ySpeed;
				}
			}else{
				block();
			}
			
		}
		
	}

}
