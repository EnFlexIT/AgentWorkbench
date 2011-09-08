package agentgui.envModel.p2Dsvg.behaviours;


import java.util.Iterator;

import agentgui.core.application.Language;
import agentgui.envModel.p2Dsvg.ontology.ActiveObject;
import agentgui.envModel.p2Dsvg.ontology.Movement;
import agentgui.envModel.p2Dsvg.ontology.PassiveObject;
import agentgui.envModel.p2Dsvg.ontology.Physical2DObject;
import agentgui.envModel.p2Dsvg.ontology.PlaygroundObject;
import agentgui.envModel.p2Dsvg.ontology.Position;
import agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper;
import agentgui.envModel.p2Dsvg.provider.EnvironmentProviderService;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;

/**
 * This behaviour implements an agent's movement to a destination point in a straight line 
 * @author Nils
 *
 */
public class MoveToPointBehaviour extends TickerBehaviour {
	/**
	 * Time between to position checks. Should match the EnvironmentProviderAgent's period 
	 */
	private static int PERIOD = 100;
	
	public static boolean IS_USED=false;

	/**
	 * Automatically generated serialVersionUID
	 */
	private static final long serialVersionUID = -918169252452095480L;
	/**
	 * The destination position
	 */
	private Position destPos;
	/**
	 * The speed
	 */
	private float speed;
	/**
	 * The distance to the destination position in x and y direction
	 */
	private float xPosDiff, yPosDiff;
	/**
	 * Is the current step the first one?
	 */
	private boolean firstStep = true;
	/**
	 * Is the current step the last one?
	 */
	private boolean lastStep = false;
	/**
	 * An EnvironmentProviderHelper instance
	 */
	private EnvironmentProviderHelper helper;
	
	/**
	 * Constructor
	 * @param a The agent executing the MoveToPointBehaviour
	 * @param destPos The destination position
	 * @param speed The speed to move with
	 * @throws ServiceException Error accessing the EnvironmentProviderService
	 */
	public MoveToPointBehaviour(Agent a, Position destPos, float speed) throws ServiceException{
		super(a, PERIOD);
		IS_USED=true;
		System.out.println("Call Move to Point");
		try {
			this.destPos = destPos;
			this.speed = speed;
			
			// Get the EnvironmentProviderHelper 
			this.helper = (EnvironmentProviderHelper) myAgent.getHelper(EnvironmentProviderService.SERVICE_NAME);
			
			// Check if the destination position in inside the agent's parent playground
			if(!checkPlayground()){
				System.err.println(myAgent.getLocalName()+": "+Language.translate("Fehler: Zielposition auﬂerhalb des Parent Playground!"));
				this.stop();
			}
			
		} catch (ServiceException e) {
			throw e;
		}
	}
	
	/**
	 * This method is executed every PERIOD milliseconds
	 */
	@Override
	protected void onTick() {
		/**
		 * Get the agent's ActiveObject from the 
		 */
		ActiveObject self = (ActiveObject) helper.getObject(myAgent.getLocalName());
		
		// The first step
		if(firstStep){
		
			// Initialize distances in x and y direction 
			xPosDiff = destPos.getXPos() - self.getPosition().getXPos();
			yPosDiff = destPos.getYPos() - self.getPosition().getYPos();
			
			// Calculate distance between start and destination Position 
			float dist = (float) Math.sqrt(xPosDiff*xPosDiff + yPosDiff*yPosDiff);
		
			
			// Calculate required time
			float seconds = dist / speed;
		
			// Create Movement instance
			Movement movement = new Movement();
			movement.setXPosChange(xPosDiff / seconds);
			movement.setYPosChange(yPosDiff / seconds);
		
			// Start moving
			helper.setMovement(myAgent.getLocalName(), movement);
			firstStep = false;
		}else{
			
			Position currPos = self.getPosition();	// Get the agent's current 
			
//			if(checkCollisions()){		// Check for collisions
//				Movement stop = new Movement();
//				stop.setXPosChange(0);
//				stop.setYPosChange(0);
//				helper.setMovement(myAgent.getLocalName(), stop);	// Stop moving
//				this.stop();		// Stop the MoveToPointBehaviour
//			}
			
			if(lastStep){
				
				Movement stop = new Movement();
				stop.setXPosChange(0);
				stop.setYPosChange(0);
				helper.setMovement(myAgent.getLocalName(), stop);	// Stop moving
				this.stop();		// Stop the MoveToPointBehaviour
				
				// If distance <= speed, -> destination position will be reached in the next step
			}else if(Math.abs(xPosDiff) <= (Math.abs(self.getMovement().getXPosChange()) / 1000 * PERIOD) + 0.5 && Math.abs(yPosDiff) <= (Math.abs(self.getMovement().getYPosChange()) / 1000 * PERIOD) + 0.5){
				Movement lastStep = new Movement();
				lastStep.setXPosChange(xPosDiff);
				lastStep.setYPosChange(yPosDiff);
				System.out.println("Fast da!");
				helper.setMovement(myAgent.getLocalName(), lastStep);		// Slow down if necessary
				this.lastStep = true;		// Stop in the next step
			}else{
				// Refresh distances
		
				xPosDiff = destPos.getXPos() - currPos.getXPos();
				yPosDiff = destPos.getYPos() - currPos.getYPos();
			
			}
		}
		
	}
	
	/**
	 * Checks if the destination position is inside the agents parent playground
	 * @return True or false
	 */
	/**
	 * @return
	 */
	private boolean checkPlayground(){
		// Get the agent's Physical2DObject 
		
		Physical2DObject self = helper.getObject(myAgent.getLocalName());
		
		// Create a new Physical2DObject with the agent's size at the destination position
		Physical2DObject selfAtDestPos = new Physical2DObject();
		System.out.println("AgentName:"+myAgent.getLocalName());
		selfAtDestPos.setSize(self.getSize());
		selfAtDestPos.setPosition(destPos);
		
		// Get the agent's parent playground
		Physical2DObject playground = helper.getObject(self.getParentPlaygroundID());
		
		// Check if the playground contains the object at the destination position
		return playground.objectContains(selfAtDestPos);
	}
	
	/**
	 * @return Collision with other Physical2DObjects?
	 */
	@SuppressWarnings("unchecked")
	private boolean checkCollisions(){
		// Get the agent's Physical2DObject
		Physical2DObject self = helper.getObject(myAgent.getLocalName());
		// Get the agent's parent playground
		PlaygroundObject parentPG = (PlaygroundObject) helper.getObject(self.getParentPlaygroundID());
			
		// Check for collisions with every Physical2DObject inside the playground
		Iterator<Physical2DObject> pgObjects = parentPG.getAllChildObjects();
		while(pgObjects.hasNext()){
			Physical2DObject object = pgObjects.next();
			if( (!object.getId().equals(self.getId()))			// Object can't collide with itself 
					&& ! (object instanceof PassiveObject) 		// Collisions with PassiveObjects disabled
					&& self.objectIntersects(object)			// Check for collision
			){
				System.err.println(Language.translate("Kollision mit Objekt "+object.getId()));
				return true;
			}
		}
		return false;
	}


}
