package sma.agents;

import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import mas.environment.ontology.Physical2DObject;
import mas.environment.ontology.Position;
import mas.environment.provider.EnvironmentProviderHelper;
import mas.environment.provider.EnvironmentProviderService;
import mas.environment.MoveToPointBehaviour;
import mas.environment.Physical2DAgent;

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
				
		try {
			final Physical2DObject targetObject = getEnvironmentObject("Box2");
			
			Position wp1 = new Position();
			wp1.setXPos(5f);
			wp1.setYPos(5f);
			
			Position wp2 = new Position();
			wp2.setXPos(targetObject.getPosition().getXPos());
			wp2.setYPos(wp1.getYPos());
			
			Position wp3 = new Position();
			wp3.setXPos(wp2.getXPos());
			wp3.setYPos(targetObject.getPosition().getYPos());
			
			Position finalPos = new Position();
			finalPos.setXPos(10f);
			finalPos.setYPos(5f);
			
			
			SequentialBehaviour demoBehaviour = new SequentialBehaviour();
			demoBehaviour.addSubBehaviour(new MoveToPointBehaviour(this, wp1, wp2, myEnvironmentObject.getMaxSpeed()));
			demoBehaviour.addSubBehaviour(new MoveToPointBehaviour(this, wp2, wp3, myEnvironmentObject.getMaxSpeed()));
			
			demoBehaviour.addSubBehaviour(new OneShotBehaviour() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = -8054903564662038080L;

				@Override
				public void action() {
					try {
						EnvironmentProviderHelper helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
						boolean success = helper.takeObject(targetObject.getId(), getLocalName());
						if(success){
							System.out.println("Testausgabe: "+getLocalName()+" nimmt Objekt "+targetObject.getId()+" auf.");
						}else{
							System.err.println("Testausgabe: "+getLocalName()+" Fehler beim Aufnehmen von Objekt "+targetObject.getId());
						}
					} catch (ServiceException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
			
			demoBehaviour.addSubBehaviour(new MoveToPointBehaviour(this, wp3, wp2, myEnvironmentObject.getMaxSpeed()));
			demoBehaviour.addSubBehaviour(new MoveToPointBehaviour(this, wp2, wp1, myEnvironmentObject.getMaxSpeed()));
			
			demoBehaviour.addSubBehaviour(new OneShotBehaviour() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void action() {
					try {
						EnvironmentProviderHelper helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
						helper.putObject(targetObject.getId());
						System.out.println("Testausgabe: "+getLocalName()+" setzt Objekt "+targetObject.getId()+" ab.");
					} catch (ServiceException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			demoBehaviour.addSubBehaviour(new MoveToPointBehaviour(this, wp1, finalPos, myEnvironmentObject.getMaxSpeed()));
			
			addBehaviour(demoBehaviour);
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
