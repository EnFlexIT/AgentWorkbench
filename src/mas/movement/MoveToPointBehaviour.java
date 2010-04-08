package mas.movement;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Vector;

import mas.display.DisplayableAgent;
import mas.environment.DisplayConstants;

import sma.ontology.Movement;
import sma.ontology.Position;
import sma.ontology.Speed;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Movement to a destination point
 * @author Nils
 *
 */
public class MoveToPointBehaviour extends TickerBehaviour {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Position to move to
	 */
	private Position destPos;
	/**
	 * The agent's current position
	 */
	private Position startPos;
	/**
	 * The agent's current speed
	 */
	float speed;
	/**
	 * The agent's current speed in direction x
	 */
	private double speedX;
	/**
	 * The agent's current speed in direction y
	 */
	private double speedY;
	/**
	 * The acting agent
	 */
	private DisplayableAgent agent;
	/**
	 * The steps 
	 */
	private Vector<Position> steps;
	
	private Iterator<Position> stepIterator;

	/**
	 * Constructor 	
	 * @param a	The acting agent
	 * @param destPos The position the agent shall move to
	 * @param speed The agents speed
	 */
	public MoveToPointBehaviour(DisplayableAgent a, Position destPos, Speed speed) {
		super((Agent) a, DisplayConstants.PERIOD);
		this.agent = a;
		this.destPos = destPos;
		this.speed = speed.getSpeed();
		this.startPos = agent.getPosition();		
	}
		
	public void onStart(){
		
		Point2D.Float start = new Point2D.Float(startPos.getX(), startPos.getY());
		Point2D.Float dest = new Point2D.Float(destPos.getX(), destPos.getY());
		
		double distance = dest.distance(start);
		
		double distX = destPos.getX() - startPos.getX();
		double distY = destPos.getY() - startPos.getY();
		
		double factor = distance / speed;
		
		speedX = distX / factor;
		speedY = distY / factor;
		
		this.steps = calculateSteps();
		this.stepIterator = steps.iterator();		
		
		try {
			Movement movement = new Movement();
			for(int i=0; i<steps.size(); i++){
				movement.addSteps(steps.get(i));
			}
			
			Action act = new Action();
			act.setActor(myAgent.getAID());
			act.setAction(movement);
			
			ACLMessage movementInfo = new ACLMessage(ACLMessage.INFORM);
			movementInfo.addReceiver(agent.getUpdateReceiver());
			movementInfo.setLanguage(agent.getDisplayCodec().getName());
			movementInfo.setOntology(agent.getDisplayOntology().getName());
			myAgent.getContentManager().fillContent(movementInfo, act);
			myAgent.send(movementInfo);
			
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	private Vector<Position> calculateSteps(){
		Vector<Position> steps = new Vector<Position>();
		
		float posX = startPos.getX();
		float posY = startPos.getY();		
		
		while( posX != destPos.getX() && posY != destPos.getY()){
			
			float distX = destPos.getX() - posX;
			float distY = destPos.getY() - posY;
			
			if(Math.abs(distX) > Math.abs(speedX)){
				posX += speedX;
			}else{
				posX += distX;
			}
			
			if(Math.abs(distY) > Math.abs(speedX)){
				posY += speedY;
			}else{
				posY += distY;
			}
			
			Position step = new Position();
			step.setX(posX);
			step.setY(posY);
			
			steps.add(step);
		}
		
		return steps;
	}

	@Override
	protected void onTick() {
		if(stepIterator.hasNext()){
			Position step = stepIterator.next();
			agent.setPosition(step);
//			System.out.println("Position: "+step.getX()+":"+step.getY());
		}else{
//			System.out.println("Sie haben Ihr Ziel erreicht!");
			stop();
		}
	}

}
