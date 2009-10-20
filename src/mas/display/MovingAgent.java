package mas.display;

/**
 * Simple example for a class extending GraphicalAgent
 * Defining agent(type)-specific properties 
 * 
 * @author nils
 *
 */
public class MovingAgent extends GraphicalAgent {
	
	
	public void setup(){
		color="blue";
		super.setup();
		addBehaviour(new MovingBehaviour(this));
		addBehaviour(new CollisionBehaviour(this));
	}
	
	public void takeDown(){
		super.takeDown();
	}
}
