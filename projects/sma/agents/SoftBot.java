package sma.agents;

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
		this.self.getCurrentSpeed().setSpeed(10.0f);
		
		Position dest = new Position();
		dest.setX(500);
		dest.setY(50);
		
		addBehaviour(new MoveToPointBehaviour(this, dest, this.getCurrentSpeed()));
	}		
}
