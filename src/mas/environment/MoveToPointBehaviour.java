package mas.environment;

import mas.environment.ontology.Movement;
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
	
	private float xPosDiff, yPosDiff;
	
	private Movement movement;
	
	private boolean lastStep = false;
	
	EnvironmentProviderHelper helper;

	public MoveToPointBehaviour(Agent a, Position currPos, Position destPos, float speed) throws ServiceException{
		super(a, PERIOD);
		
		try {
			helper = (EnvironmentProviderHelper) myAgent.getHelper(EnvironmentProviderService.SERVICE_NAME);
		} catch (ServiceException e) {
			throw e;
		}
		this.destPos = destPos;
		xPosDiff = destPos.getXPos() - currPos.getXPos();
		yPosDiff = destPos.getYPos() - currPos.getYPos();
		float dist = (float) Math.sqrt(xPosDiff*xPosDiff + yPosDiff*yPosDiff);
		float seconds = dist / speed;
		
		movement = new Movement();
		movement.setXPosChange(xPosDiff / seconds);
		movement.setYPosChange(yPosDiff / seconds);
		System.out.println("Testausgabe: Bewegung");
		System.out.println("- X: "+movement.getXPosChange()+", Y: "+movement.getYPosChange());
		System.out.println("- Geschwindigkeit: "+movement.getSpeed());
		
		
		helper.setMovement(myAgent.getLocalName(), movement);
	}

	@Override
	protected void onTick() {
		Position currPos = helper.getObject(myAgent.getLocalName()).getPosition();
		System.out.println("Testausgabe: "+myAgent.getLocalName()+" - Position "+currPos.getXPos()+"x"+currPos.getYPos());
		if(lastStep){
			Movement stop = new Movement();
			stop.setXPosChange(0);
			stop.setYPosChange(0);
			helper.setMovement(myAgent.getLocalName(), stop);
			System.out.println("Testausgabe: "+myAgent.getLocalName()+" Zielposition erreicht.");
			this.stop();
		}else if(xPosDiff <= (movement.getXPosChange() / 1000 * PERIOD) && yPosDiff <= (movement.getYPosChange() / 1000 * PERIOD)){
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
