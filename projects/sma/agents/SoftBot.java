package sma.agents;

import java.util.Vector;

import sma.ontology.Position;
import sma.ontology.Speed;
import mas.movement.MoveToPointBehaviour;
import mas.movement.MovingAgent;

/**
 * Dummy-Implementation for testing the DisplayAgent
 * @author Nils
 *
 */
public class SoftBot extends MovingAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void setup(){
		super.setup();
		
		this.self.setCurrentSpeed(new Speed());
		this.self.getCurrentSpeed().setSpeed(1.0f);
		
		Vector<Position> wps = new Vector<Position>();
		
		Position pos1 = new Position();
		pos1.setX(20);
		pos1.setY(30);
		
		Position pos2 = new Position();
		pos2.setX(35);
		pos2.setY(30);
		
		Position pos3 = new Position();
		pos3.setX(50);
		pos3.setY(2);
		
		wps.add(0, pos1);
		wps.add(1, pos2);
		wps.add(2, pos3);
		
		// Temporäre Lösung des Start-Timing-Problems
		this.doWait(1000);
		
		addBehaviour(new MoveToPointBehaviour(this, wps, this.getCurrentSpeed()));
	}		
}
