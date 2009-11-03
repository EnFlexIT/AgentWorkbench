package mas.display;

/**
 * Simple example for a class extending GraphicalAgent
 * Defining agent(type)-specific properties 
 * 
 * @author nils
 *
 */
public class MovingAgent extends PhysicalAgent {
	
	
	public void setup(){
		posX = 20;
		posY = 20;
		speedX = 5;
		speedY = 2;
		Object[] args = getArguments();
		if(args.length>0){
			displayableType = (String) args[0];
		}else{
			displayableType = "A";
		}
		super.setup();
		addBehaviour(new MovingBehaviour(this));
		addBehaviour(new CollisionBehaviour(this));
	}
	
	public void takeDown(){
		super.takeDown();
	}
}
