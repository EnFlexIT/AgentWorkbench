package mas.movement;

import java.awt.geom.Point2D;
import java.util.Collection;
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
	 * The agent's current position
	 */
	private Position startPos;
	/**
	 * The agent's current speed
	 */
	float speed;
	/**
	 * The acting agent
	 */
	private DisplayableAgent agent;
	/**
	 * Waypoints to follow
	 */
	private Vector<Position> waypoints;
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
		this.waypoints = new Vector<Position>();
		this.waypoints.add(destPos);
		this.speed = speed.getSpeed();
		this.startPos = agent.getPosition();		
	}
	
	public MoveToPointBehaviour(DisplayableAgent a, Vector<Position> waypoints, Speed speed){
		super((Agent) a, DisplayConstants.PERIOD);
		this.agent = a;
		this.waypoints = waypoints;
		this.speed = speed.getSpeed();
		this.startPos = agent.getPosition();
	}
		
	public void onStart(){
		
		Iterator<Position> wpIterator = waypoints.iterator();
		Position current = startPos;
		
		this.steps = new Vector<Position>();
		while(wpIterator.hasNext()){
			Position next = wpIterator.next();
			steps.addAll(calculateSteps(current, next));
			current = next;
		}
		
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
	
	
	
	private Vector<Position> calculateSteps(Position from, Position to){
		
		Vector<Position> steps = new Vector<Position>();
		
		System.out.println("Calculating steps from "+
				from.getX()+":"+from.getY()+
				" to "+to.getX()+":"+to.getY());
		
		// Calculate distance and speed
		Point2D.Float start = new Point2D.Float(from.getX(), from.getY());
		Point2D.Float dest = new Point2D.Float(to.getX(), to.getY());
		
		double distance = dest.distance(start);
		
		double distX = dest.getX() - start.getX();
		double distY = dest.getY() - start.getY();
		
		double factor = (float) (distance / this.speed);
		
		float speedX = (float) (distX / factor);
		float speedY = (float) (distY / factor);
		
		// Calculate steps
		float posX = from.getX();
		float posY = from.getY();
		
		while(!(posX == dest.getX() && posY == dest.getY()) ){
			if(Math.abs(distX) > Math.abs(speedX)){
				posX += speedX;
			}else{
				posX = to.getX();
			}
			
			if(Math.abs(distY) > Math.abs(speedY)){
				posY += speedY;
			}else{
				posY = to.getY();
			}
			
			Position step = new Position();
			step.setX(posX);
			step.setY(posY);
			steps.add(step);
			
			distX = to.getX() - posX;
			distY = to.getY() - posY;
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
