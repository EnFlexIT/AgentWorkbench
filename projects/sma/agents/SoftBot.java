package sma.agents;

import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import mas.environment.ontology.ActiveObject;
import mas.environment.ontology.Physical2DObject;
import mas.environment.ontology.Position;
import mas.environment.provider.EnvironmentProviderHelper;
import mas.environment.provider.EnvironmentProviderService;
import mas.environment.MoveToPointBehaviour;

/**
 * Dummy-Implementation for testing the DisplayAgent
 * @author Nils
 *
 */
public class SoftBot extends Agent {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1555521768821748185L;

	public void setup(){
				
		try {
			EnvironmentProviderHelper helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
			final Physical2DObject targetObject = helper.getObject("Box2");
			
			ActiveObject self = (ActiveObject) helper.getObject(getLocalName()); 
			
			Position destPos = new Position();
			destPos.setXPos(5f);
			destPos.setYPos(5f);
						
			Position waypoint = new Position();
			waypoint.setXPos(targetObject.getPosition().getXPos());
			waypoint.setYPos(5f);
			
			Position objectPos = new Position();
			objectPos.setXPos(waypoint.getXPos());
			objectPos.setYPos(targetObject.getPosition().getYPos());
			
			Position endPos = new Position();
			endPos.setXPos(10f);
			endPos.setYPos(5f);
			
			
			SequentialBehaviour demoBehaviour = new SequentialBehaviour();
			demoBehaviour.addSubBehaviour(new MoveToPointBehaviour(this, waypoint, self.getMaxSpeed()));
			demoBehaviour.addSubBehaviour(new MoveToPointBehaviour(this, objectPos, self.getMaxSpeed()));
			
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
			
			demoBehaviour.addSubBehaviour(new MoveToPointBehaviour(this, waypoint, self.getMaxSpeed()));
			demoBehaviour.addSubBehaviour(new MoveToPointBehaviour(this, destPos, self.getMaxSpeed()));
			
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
			
			demoBehaviour.addSubBehaviour(new MoveToPointBehaviour(this, endPos, self.getMaxSpeed()));
			
			addBehaviour(demoBehaviour);
			
//			destPos.setXPos(666f);
//			destPos.setYPos(666f);
//			addBehaviour(new MoveToPointBehaviour(this, destPos, self.getMaxSpeed()));
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
