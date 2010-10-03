package sma.agents;

import jade.core.Agent;
import jade.core.ServiceException;
import mas.environment.ontology.Position;
import mas.environment.MoveToPointBehaviour;
import mas.environment.Physical2DAgent;
import mas.environment.ontology.Movement;

/**
 * Dummy-Implementation for testing the DisplayAgent
 * @author Nils
 *
 */
public class SoftBot extends Physical2DAgent {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1555521768821748185L;

	public void setup(){
		super.setup();
		if(myEnvironmentObject != null){
			System.out.println(getLocalName()+": Umgebungsobjekt gefunden:");
			System.out.println("- Position: "+myEnvironmentObject.getPosition().getXPos()+":"+myEnvironmentObject.getPosition().getYPos());
			System.out.println("- Groesse: "+myEnvironmentObject.getSize().getWidth()+"x"+myEnvironmentObject.getSize().getHeight());
		}
		Position destPos = new Position();
		destPos.setXPos(30);
		destPos.setYPos(50);
		
		try {
			addBehaviour(new MoveToPointBehaviour( this, myEnvironmentObject.getPosition(), destPos, myEnvironmentObject.getMaxSpeed()));
		} catch (ServiceException e) {
			System.err.println(getLocalName()+": Could not start MoveToPointBehaviour, error retrieving EnvironmentProviderService!");
		}
	}
	
}
