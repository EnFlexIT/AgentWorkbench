package contmas.behaviours;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Vector;

import mas.display.DisplayConstants;
import mas.display.DisplayableAgent;
import mas.display.ontology.Movement;
import mas.display.ontology.Position;
import mas.display.ontology.Speed;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class SendPositionUpdateBehaviour extends OneShotBehaviour {
	/**
	 * The destination
	 */
	private Position destPos;

	/**
	 * The acting agent
	 */
	private DisplayableAgent agent;

	private String svgId;
	
	/**
	 * Constructor: Proxy Mode, one DisplayableAgent speaking for several agents. SVG element id, StartPos and speed specified by parameter
	 * @param svgId ID attribute of the SVG element representing the moving agent
	 * @param a DisplayableAgent representing the proxy
	 * @param startPos The agents current position
	 * @param destPos The agents destination position
	 * @param speed The agents speed
	 */
	public SendPositionUpdateBehaviour(String svgId, DisplayableAgent a, Position destPos){
		super((Agent) a);
		this.svgId = svgId;
		this.agent = a;
		this.destPos = destPos;
	}
	
	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action(){
		try {
			
			Movement movement = new Movement();
			movement.setSvgId(svgId);
			movement.addSteps(destPos);
			
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
}
