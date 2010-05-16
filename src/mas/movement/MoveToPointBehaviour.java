package mas.movement;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Vector;

import mas.display.DisplayConstants;
import mas.display.DisplayableAgent;

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
	 * The destination
	 */
	private Position destPos;
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
	
	public static final int DEST_REACHED = 1;
	public static final int DEST_NOT_REACHED = 0;
	
	private String svgId;
	
	/**
	 * Constructor: SVG element id == localName, one DisplayableAgent per Agent, one destPos. Speed and startPos taken from the DisplayableAgent 
	 * @param a DisplayableAgent representing the agent to be moved
	 * @param destPos Destination position
	 */
	public MoveToPointBehaviour(DisplayableAgent a, Position destPos) {
		this(a.getAID().getLocalName(), a, destPos);
	}
	
	/**
	 * Constructor: SVG element id specified by parameter , one DisplayableAgent per Agent, one destPos. Speed and startPos taken from the DisplayableAgent
	 * @param svgId ID attribute of the SVG element representing the moving agent
	 * @param a DisplayableAgent representing the agent to be moved
	 * @param destPos Destination position
	 */
	public MoveToPointBehaviour(String svgId, DisplayableAgent a, Position destPos){
		super((Agent) a, DisplayConstants.PERIOD);
		this.svgId = svgId;
		this.agent = a;
		this.destPos = destPos;
		this.waypoints = new Vector<Position>();
		this.waypoints.add(destPos);
		this.speed = a.getCurrentSpeed().getSpeed();
		this.startPos = a.getPosition();
	}
	
	/**
	 * Constructor: SVG element id == localName, one DisplayableAgent per Agent, several waypoints, destPos == last waypoint. Speed and startPos taken from the DisplayableAgent 
	 * @param a DisplayableAgent representing the agent to be moved
	 * @param waypoints The waypoints
	 */
	public MoveToPointBehaviour(DisplayableAgent a, Vector<Position> waypoints){
		this(a.getAID().getLocalName(), a, waypoints);
	}
	
	/**
	 * Constructor: SVG element id == localName, one DisplayableAgent per Agent, several waypoints, destPos == last waypoint. Speed and startPos taken from the DisplayableAgent
	 * @param svgId ID attribute of the SVG element representing the moving agent
	 * @param a DisplayableAgent representing the agent to be moved
	 * @param waypoints The waypoints
	 */
	public MoveToPointBehaviour(String svgId, DisplayableAgent a, Vector<Position> waypoints){
		super((Agent) a, DisplayConstants.PERIOD);
		this.svgId = svgId;
		this.agent = a;
		this.waypoints = waypoints;
		this.destPos = this.waypoints.lastElement();
		this.speed = a.getCurrentSpeed().getSpeed();
		this.startPos = a.getPosition();
	}
	
	/**
	 * Constructor: Proxy Mode, one DisplayableAgent speaking for several agents. StartPos, destPos and speed specified by parameter, SVG element id == DisplayableAgent's local Name 
	 * @param a DisplayableAgent representing the proxy
	 * @param startPos The agents current position
	 * @param destPos The agents destination position
	 * @param speed The agents speed
	 */
	public MoveToPointBehaviour(DisplayableAgent a, Position startPos, Position destPos, Speed speed) {
		this(a.getAID().getLocalName(), a, startPos, destPos, speed);
	}
	
	/**
	 * Constructor: Proxy Mode, one DisplayableAgent speaking for several agents. SVG element id, StartPos and speed specified by parameter
	 * @param svgId ID attribute of the SVG element representing the moving agent
	 * @param a DisplayableAgent representing the proxy
	 * @param startPos The agents current position
	 * @param destPos The agents destination position
	 * @param speed The agents speed
	 */
	public MoveToPointBehaviour(String svgId, DisplayableAgent a, Position startPos, Position destPos, Speed speed){
		super((Agent) a, DisplayConstants.PERIOD);
		this.svgId = svgId;
		this.agent = a;
		this.destPos = destPos;
		this.waypoints = new Vector<Position>();
		this.waypoints.add(destPos);
		this.speed = speed.getSpeed();
		this.startPos = startPos;
	}
	
	/**
	 * Constructor: Proxy Mode, one DisplayableAgent speaking for several agents. Several waypoints, destPos == last waypoint. StartPos and speed specified by parameter, SVG element id == DisplayableAgent's local name
	 * @param a DisplayableAgent representing the proxy
	 * @param startPos The agents current position
	 * @param waypoints The waypoints
	 * @param speed The agents speed
	 */
	public MoveToPointBehaviour(DisplayableAgent a, Position startPos, Vector<Position> waypoints, Speed speed){
		this(a.getAID().getLocalName(), a, startPos, waypoints, speed);
	}
	
	/**
	 * Constructor: Proxy Mode, one DisplayableAgent speaking for several agents. Several waypoints, destPos == last waypoint. SVG element id, StartPos and speed specified by parameter
	 * @param svgId svgId ID attribute of the SVG element representing the moving agent
	 * @param a DisplayableAgent representing the proxy
	 * @param startPos The agents current position
	 * @param waypoints The waypoints
	 * @param speed The agents speed
	 */
	public MoveToPointBehaviour(String svgId, DisplayableAgent a, Position startPos, Vector<Position> waypoints, Speed speed){
		super((Agent) a, DisplayConstants.PERIOD);
		this.svgId = svgId;
		this.agent = a;
		this.waypoints = waypoints;
		this.destPos = this.waypoints.lastElement();
		this.speed = speed.getSpeed();
		this.startPos = startPos;
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
			movement.setSvgId(svgId);
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
			agent.setMoving(true);
//			System.out.println(myAgent.getLocalName()+": Message sent.");
			
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public int onEnd(){
		agent.setMoving(false);
		if(agent.getPosition().getX() == destPos.getX()	&& agent.getPosition().getY() == destPos.getY()){
			return DEST_REACHED;
		}else{
			return DEST_NOT_REACHED;
		}
	}
	
	/**
	 * Calculates the agents position for each step on it's way between from and to
	 * @param from The start position for this movement (part)
	 * @param to The destination for this movement (part)
	 * @return Vector containing Position instances for each step
	 */
	private Vector<Position> calculateSteps(Position from, Position to){
		
		Vector<Position> steps = new Vector<Position>();
		
		// Calculate distance and speed
		Point2D.Float start = new Point2D.Float(from.getX(), from.getY());
		Point2D.Float dest = new Point2D.Float(to.getX(), to.getY());
		
		double distance = dest.distance(start);
		
		System.out.println("Distance: "+distance+" meters");
		float speed = this.speed * (DisplayConstants.PERIOD / 1000f);
		System.out.println("Speed: "+this.speed+" meters / s = "+speed+" meters / "+DisplayConstants.PERIOD+" ms");
				
		double distX = dest.getX() - start.getX();
		double distY = dest.getY() - start.getY();
		
		double factor = (float) (distance / speed);
		
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
