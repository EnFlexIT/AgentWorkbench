package mas.projects.sma.agents;

import java.util.Collection;
import java.util.Iterator;

import mas.environment.AgentObject;
import mas.environment.ObstacleObject;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;

/**
 * Dummy-Implementierung zum Testen des Displayagent
 * @author Nils
 *
 */
public class SoftBot extends Agent {

	private int width;
	private int height;
	private int posX;
	private int posY;
	private int speedX = 5;
	private int speedY = 2;
	private int envWidth;
	private int envHeight;
	
	private Collection<ObstacleObject> obstacles = null;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void setup(){
		// Initialize with values from environment definition
		Object[] args = getArguments();
		if(args[0] != null && args[0] instanceof AgentObject){
			AgentObject self = (AgentObject) args[0];
			this.width = self.getWidth();
			this.height = self.getHeight();
			this.posX = self.getPosX();
			this.posY = self.getPosY();
			this.envHeight = self.getPlayground().getHeight();
			this.envWidth = self.getPlayground().getWidth();
			this.obstacles = self.getPlayground().getObstacles();
			this.addBehaviour(new MoveExampleBehaviour(this, 50));
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
			Iterator<ObstacleObject> iter = obstacles.iterator();
			while(iter.hasNext()){
				ObstacleObject oo = iter.next();
				
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
	
	
}
