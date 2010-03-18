package sma.agents;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import mas.environment.Old_AgentObject;
import mas.environment.Old_ObstacleObject;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Dummy-Implementierung zum Testen des Displayagent
 * @author Nils
 *
 */
public class SoftBot extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int width;
	private int height;
	private int posX;
	private int posY;
	private int speedX = 5;
	private int speedY = 2;
	private int envWidth;
	private int envHeight;
	
	private Collection<Old_ObstacleObject> obstacles = null;
	
	private HashMap<String, Old_AgentObject> agents = null;
	
	public void setup(){
		// Initialize with values from environment definition
		Object[] args = getArguments();
		if(args[0] != null && args[0] instanceof Old_AgentObject){
			Old_AgentObject self = (Old_AgentObject) args[0];
			this.width = self.getWidth();
			this.height = self.getHeight();
			this.posX = self.getPosX();
			this.posY = self.getPosY();
			this.envHeight = self.getParentPlayground().getHeight();
			this.envWidth = self.getParentPlayground().getWidth();
			this.obstacles = self.getParentPlayground().getObstacles();
			this.agents = self.getParentPlayground().getAgents();
			this.addBehaviour(new MoveExampleBehaviour(this, 50));
			try {
				TopicManagementHelper tmh = (TopicManagementHelper) getHelper(TopicManagementHelper.SERVICE_NAME);
				AID positionTopic = tmh.createTopic("position");
				tmh.register(positionTopic);
				addBehaviour(new PosUpdateBehaviour(MessageTemplate.MatchTopic(positionTopic)));
			
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * 
	 */
	public void takeDown(){
		TopicManagementHelper tmh;
		try {
			tmh = (TopicManagementHelper) getHelper(TopicManagementHelper.SERVICE_NAME);
			AID positionTopic = tmh.createTopic("position");
			tmh.deregister(positionTopic);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	class MoveExampleBehaviour extends TickerBehaviour{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		TopicManagementHelper tmh = null;
		AID positionTopic = null;

		public MoveExampleBehaviour(Agent a, long period) {
			super(a, period);
			
			try {
				tmh = (TopicManagementHelper) a.getHelper(TopicManagementHelper.SERVICE_NAME);
				
				
				positionTopic = tmh.createTopic("position");
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}

		@Override
		protected void onTick() {
			int oldX = posX;
			int oldY = posY;
			boolean collX = false;
			boolean collY = false;
			
			posX += speedX;
			posY += speedY;
			if(posX < 0 || posX+width >= envWidth){
				collX = true;
			}
			if(posY <= 0 || posY+height >= envHeight){
				collY = true;
			}
			Iterator<Old_ObstacleObject> iter = obstacles.iterator();
			while(iter.hasNext()){
				Old_ObstacleObject oo = iter.next();
				
				// X Richtung
				if(oldY+height>oo.getPosY()&&oldY<oo.getPosY()+oo.getHeight()){
					if(posX+width>oo.getPosX()&&posX<oo.getPosX()+oo.getWidth()
							&&!(oldX+width>oo.getPosX()&&oldX<oo.getPosX()+oo.getWidth()))
						collX = true;
				}
				// Y Richtung
				if(oldX+width>oo.getPosX()&&oldX<oo.getPosX()+oo.getWidth()){
					if(posY+height>oo.getPosY()&&posY<oo.getPosY()+oo.getHeight()
							&&!(oldY+height>oo.getPosY()&&oldY<oo.getPosY()+oo.getHeight()))
						collY = true;
				}				
			}
			
			Iterator<Old_AgentObject> aIter = agents.values().iterator();
			while(aIter.hasNext()){
				Old_AgentObject ao = aIter.next();
				
				// X Richtung
				if(oldY+height>ao.getPosY()&&oldY<ao.getPosY()+ao.getHeight()){
					if(posX+width>ao.getPosX()&&posX<ao.getPosX()+ao.getWidth()
							&&!(oldX+width>ao.getPosX()&&oldX<ao.getPosX()+ao.getWidth())){
						collX = true;
						System.out.println("Agent collision");
					}
						
				}
				// Y Richtung
				if(oldX+width>ao.getPosX()&&oldX<ao.getPosX()+ao.getWidth()){
					if(posY+height>ao.getPosY()&&posY<ao.getPosY()+ao.getHeight()
							&&!(oldY+height>ao.getPosY()&&oldY<ao.getPosY()+ao.getHeight())){
						collY = true;
						System.out.println("Agent collision");
					}
						
				}				
			}
			
			if(collX){
				speedX = -speedX;
			}
			if(collY){
				speedY = -speedY;
			}
			
			
			ACLMessage posUpdate = new ACLMessage(ACLMessage.INFORM);
			
			posUpdate.addReceiver(positionTopic);
			posUpdate.setContent(posX+","+posY);		
			myAgent.send(posUpdate);
			
		}
		
		public int onEnd(){
			ACLMessage bye = new ACLMessage(ACLMessage.INFORM);
			bye.addReceiver(positionTopic);
			bye.setContent("bye");
			myAgent.send(bye);
			return 0;
		}
		
	}
	
	class PosUpdateBehaviour extends CyclicBehaviour{
		
		/**
		 * Empfängt Positionsupdates anderer Agenten
		 */
		private static final long serialVersionUID = 1L;
		MessageTemplate positionTemplate;
		
		public PosUpdateBehaviour(MessageTemplate mt){
			this.positionTemplate = mt;
		}

		@Override
		public void action() {
			ACLMessage posUpdate = receive(positionTemplate);
			if(posUpdate != null){
				String sender = posUpdate.getSender().getLocalName();
				String content = posUpdate.getContent();
				if(content.equals("bye")){
					// Agent wurde beendet -> entferne aus HashMap
					agents.remove(sender);
				}else{
					String[] pos = content.split(",");
					Old_AgentObject agent = agents.get(sender);
					agent.setPosX(Integer.parseInt(pos[0]));
					agent.setPosY(Integer.parseInt(pos[1]));					
				}
				
			}else{
				block();
			}
				
			
		}
		
	}
	
	
}
