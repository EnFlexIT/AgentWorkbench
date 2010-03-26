package sma.agents;

import java.util.Iterator;

import sma.ontology.AbstractObject;
import sma.ontology.AgentObject;
import sma.ontology.DisplayOntology;
import sma.ontology.Movement;
import sma.ontology.PlaygroundObject;
import sma.ontology.Position;
import sma.ontology.Speed;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;

/**
 * Dummy-Implementation for testing the DisplayAgent
 * @author Nils
 *
 */
public class SoftBot extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Ontology object instance of this agent
	 */
	private AgentObject self = null;
	
	/**
	 * Ontology object instance of the playground this agent lives in
	 */
	private PlaygroundObject playground = null;
	
	private AID posTopic = null;
	
	private Codec codec = new SLCodec();
	private Ontology ontology = DisplayOntology.getInstance();
	
	public void setup(){
		
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);
		
		Object[] args = getArguments();
		if(args != null && args.length>0 && args[0] instanceof AgentObject){
			this.self = (AgentObject) args[0];
			this.playground = self.getParent();
			
//			DFAgentDescription template = new DFAgentDescription();
//			ServiceDescription sd = new ServiceDescription();
//			sd.setType("DisplayService");
//			sd.setName("SMA");
//			template.addServices(sd);
//			
//			try {
//				DFAgentDescription[] results = DFService.search(SoftBot.this, template);
//				if(results != null && results.length>0){
//					displayAgent = results[0].getName();
//					System.out.println(getLocalName()+" found DisplayAgent "+displayAgent.getLocalName());
//				}else{
//					System.out.println("No DA found");
//				}
////				System.out.println(getLocalName()+" found DisplayAgent "+displayAgent.getLocalName());
//				
//			} catch (FIPAException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			try {
				TopicManagementHelper tmh = (TopicManagementHelper) getHelper(TopicManagementHelper.SERVICE_NAME);
				this.posTopic = tmh.createTopic("position");
				tmh.register(posTopic);
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			addBehaviour(new MovingBehaviour(this, 100));
			
			System.out.println("Starting "+getLocalName()+" in playground "+this.playground.getId());
		}
	}

	/**
	 * Simple movement example behaviour
	 * @author Nils
	 *
	 */
	private class MovingBehaviour extends TickerBehaviour{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MovingBehaviour(Agent a, long period) {
			super(a, period);
			Speed speed = new Speed();
			speed.setXSpeed(5);
			speed.setYSpeed(2);
			self.setCurrentSpeed(speed);
			// TODO Auto-generated constructor stub
		}
	
		@Override
		protected void onTick() {
			int oldXPos = self.getPosition().getXPos();
			int oldYPos = self.getPosition().getYPos();
			int xSpeed = self.getCurrentSpeed().getXSpeed();
			int ySpeed = self.getCurrentSpeed().getYSpeed();
						
			int newXPos = oldXPos + xSpeed;
			int newYPos = oldYPos + ySpeed;
			
			self.getPosition().setXPos(newXPos);
			self.getPosition().setYPos(newYPos);
			
			checkCollisions();			
			
			Movement movement = new Movement();
			movement.setStartPos(new Position());
			movement.getStartPos().setXPos(oldXPos);
			movement.getStartPos().setYPos(oldYPos);
			movement.setSpeed(self.getCurrentSpeed());
			
			Action act = new Action();
			act.setActor(getAID());
			act.setAction(movement);
			
			ACLMessage movementInfo = new ACLMessage(ACLMessage.INFORM);
			movementInfo.addReceiver(posTopic);
			movementInfo.setLanguage(codec.getName());
			movementInfo.setOntology(ontology.getName());
			
			try {
				getContentManager().fillContent(movementInfo, act);
				send(movementInfo);
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		/**
		 * Checks for collisions, inverting speed if collision detected
		 */
		@SuppressWarnings("unchecked")
		private void checkCollisions(){
			
			boolean collX = false;
			boolean collY = false;
			
			int myXPos = self.getPosition().getXPos();
			int myYPos = self.getPosition().getYPos();
			int myWidth = self.getSize().getWidth();
			int myHeight = self.getSize().getHeight();
			
			int parentXPos = self.getParent().getPosition().getXPos();
			int parentYPos = self.getParent().getPosition().getYPos();
			int parentWidth = self.getParent().getSize().getWidth();
			int parentHeight = self.getParent().getSize().getHeight();
			
			// Check for collisions with playground borders
			if(myXPos <= parentXPos || myXPos+myWidth >= parentXPos + parentWidth){
				collX = true;
			}
			if(myYPos <= parentYPos || myYPos+myHeight >= parentYPos + parentHeight){
				collY = true;
			}
			
			// Check for collisions with other objects
			int oldX = myXPos - self.getCurrentSpeed().getXSpeed();
			int oldY = myYPos - self.getCurrentSpeed().getYSpeed();
			Iterator<AbstractObject> objects = playground.getAllChildObjects();
			while(objects.hasNext()){
				AbstractObject object = objects.next();
				int objectXPos = object.getPosition().getXPos();
				int objectYPos = object.getPosition().getYPos();
				int objectWidth = object.getSize().getWidth();
				int objectHeight = object.getSize().getHeight();
				
				// X direction
				if(oldY+myHeight>objectYPos&&oldY<objectYPos+objectHeight){
					if(myXPos+myWidth>objectXPos&&myXPos<objectXPos+objectWidth
							&&!(oldX+myWidth>objectXPos&&oldX<objectXPos+objectWidth)){
						collX = true;						
					}
						
				}
				// Y direction
				if(oldX+myWidth>objectXPos&&oldX<objectXPos+objectWidth){
					if(myYPos+myHeight>objectYPos&&myYPos<objectYPos+objectHeight
							&&!(oldY+myHeight>objectYPos&&oldY<objectYPos+objectHeight)){
						collY = true;						
					}
						
				}
			}
			
			// Invert speed if collision detected
			if(collX){
				self.getCurrentSpeed().setXSpeed(-self.getCurrentSpeed().getXSpeed());				
			}
			if(collY){
				self.getCurrentSpeed().setYSpeed(-self.getCurrentSpeed().getYSpeed());
			}
		}
			
	}
	
}
