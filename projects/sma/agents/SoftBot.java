package sma.agents;

import java.util.Vector;

import mas.display.ontology.Position;
import mas.display.ontology.Speed;
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
		
		Speed max = new Speed();
		max.setSpeed(20.0f);
		
		this.setMaxSpeed(max);
				
		Vector<Position> wps = new Vector<Position>();
		
		Position pos1 = new Position();
		pos1.setX(20);
		pos1.setY(3);
		
		Position pos2 = new Position();
		pos2.setX(20);
		pos2.setY(22);
		
		Position pos3 = new Position();
		pos3.setX(53);
		pos3.setY(22);
		
		Position pos4 = new Position();
		pos4.setX(53);
		pos4.setY(10);
		
		Position pos5 = new Position();
		pos5.setX(33);
		pos5.setY(10);
		
		Position pos6 = new Position();
		pos6.setX(33);
		pos6.setY(3);
		
		wps.add(0, pos1);
		wps.add(1, pos2);
		wps.add(2, pos3);
		wps.add(3, pos4);
		wps.add(4, pos5);
		wps.add(5, pos6);
		
		// Temporäre Lösung des Start-Timing-Problems
		this.doWait(1000);
		
		addBehaviour(new MoveToPointBehaviour(this, wps));
	}		
}
