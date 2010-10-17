package mas.environment;

//import java.util.Iterator;

import java.util.Iterator;

import application.Language;
import mas.environment.ontology.ActiveObject;
import mas.environment.ontology.Movement;
import mas.environment.ontology.Physical2DObject;
import mas.environment.ontology.PlaygroundObject;
//import mas.environment.ontology.PlaygroundObject;
import mas.environment.ontology.Position;
import mas.environment.provider.EnvironmentProviderHelper;
import mas.environment.provider.EnvironmentProviderService;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;

public class MoveToPointBehaviour extends TickerBehaviour {
	
	private static int PERIOD = 100;

	/**
	 * 
	 */
	private static final long serialVersionUID = -918169252452095480L;
	
	private Position destPos;
	
	private float speed;
	
	private float xPosDiff, yPosDiff;
	
	private Movement movement;
	
	private boolean firstStep = true;
	
	private boolean lastStep = false;
	
	EnvironmentProviderHelper helper;
	
	ActiveObject self = null;

	public MoveToPointBehaviour(Agent a, Position destPos, float speed) throws ServiceException{
		super(a, PERIOD);
		
		try {
			this.destPos = destPos;
			this.speed = speed;
			this.helper = (EnvironmentProviderHelper) myAgent.getHelper(EnvironmentProviderService.SERVICE_NAME);
			
			if(!checkPlayground()){
				System.err.println(myAgent.getLocalName()+": "+Language.translate("Fehler: Zielposition auﬂerhalb des Parent Playground!"));
				this.stop();
			}
			
		} catch (ServiceException e) {
			throw e;
		}
		
		
	}

	@Override
	protected void onTick() {
		
		if(firstStep){
			self = (ActiveObject) helper.getObject(myAgent.getLocalName());
			System.out.println("Testausgabe: "+myAgent.getLocalName()+" bewegt sich von "
					+self.getPosition().getXPos()+":"+self.getPosition().getYPos()+
					" nach "+destPos.getXPos()+":"+destPos.getYPos());
			xPosDiff = destPos.getXPos() - self.getPosition().getXPos();
			yPosDiff = destPos.getYPos() - self.getPosition().getYPos();
			float dist = (float) Math.sqrt(xPosDiff*xPosDiff + yPosDiff*yPosDiff);
			float seconds = dist / speed;
			
			movement = new Movement();
			movement.setXPosChange(xPosDiff / seconds);
			movement.setYPosChange(yPosDiff / seconds);
			
			helper.setMovement(myAgent.getLocalName(), movement);
			firstStep = false;
		}else{
			Position currPos = self.getPosition();
			checkCollisions();
			if(lastStep){
				Movement stop = new Movement();
				stop.setXPosChange(0);
				stop.setYPosChange(0);
				helper.setMovement(myAgent.getLocalName(), stop);
				System.out.println("Testausgabe: "+myAgent.getLocalName()+" Zielposition "+ destPos.getXPos()+":"+destPos.getYPos()+" erreicht.");
				Physical2DObject self = helper.getObject(myAgent.getLocalName());
				System.out.println("- aktuelle Position des Agenten: "+self.getPosition().getXPos()+":"+self.getPosition().getYPos());
				this.stop();
			}else if(Math.abs(xPosDiff) <= (Math.abs(movement.getXPosChange()) / 1000 * PERIOD) + 0.5 && Math.abs(yPosDiff) <= (Math.abs(movement.getYPosChange()) / 1000 * PERIOD) + 0.5){
				Movement lastStep = new Movement();
				lastStep.setXPosChange(xPosDiff);
				lastStep.setYPosChange(yPosDiff);
				helper.setMovement(myAgent.getLocalName(), lastStep);
				this.lastStep = true;
			}else{
				xPosDiff = destPos.getXPos() - currPos.getXPos();
				yPosDiff = destPos.getYPos() - currPos.getYPos();
			}
		}
		
	}
	
	/**
	 * Checks if the destination position is in the same playground
	 * @return True or false
	 */
	private boolean checkPlayground(){
		// Get the Physical2DObject representing the agent 
		Physical2DObject self = helper.getObject(myAgent.getLocalName());
		
		// Create a new Physical2DObject with the agent's size at the destination position
		Physical2DObject selfAtDestPos = new Physical2DObject();
		selfAtDestPos.setSize(self.getSize());
		selfAtDestPos.setPosition(destPos);
		
		// Get the agent's parent playground
		Physical2DObject playground = helper.getObject(self.getParentPlaygroundID());
		
		// Check if the playground contains the object at the destination position
		return playground.objectContains(selfAtDestPos);
	}
	
	/**
	 * Checks if the agent collides with another object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean checkCollisions(){
		Physical2DObject self = helper.getObject(myAgent.getLocalName());
		PlaygroundObject parentPG = (PlaygroundObject) helper.getObject(self.getParentPlaygroundID());
		Iterator<Physical2DObject> pgObjects = parentPG.getAllChildObjects();
		
		Physical2DObject object;
		
		while(pgObjects.hasNext()){
			object = pgObjects.next();
			if( (!object.getId().equals(self.getId())) && (self.objectIntersects(object))){
				System.out.println("Testausgabe: Kollision mit "+object.getId());
				System.out.println("Self:   "+self.getPosition().getXPos()+":"+self.getPosition().getYPos()+", "+self.getSize().getWidth()+"x"+self.getSize().getHeight());
				System.out.println("Object: "+object.getPosition().getXPos()+":"+object.getPosition().getYPos()+", "+object.getSize().getWidth()+"x"+object.getSize().getHeight());
				return true;
			}
		}
		return false;
	}

}
