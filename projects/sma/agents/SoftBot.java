package sma.agents;

import agentgui.physical2Denvironment.MoveToPointBehaviour;
import agentgui.physical2Denvironment.ontology.ActiveObject;
import agentgui.physical2Denvironment.ontology.Physical2DObject;
import agentgui.physical2Denvironment.ontology.Position;
import agentgui.physical2Denvironment.provider.EnvironmentProviderHelper;
import agentgui.physical2Denvironment.provider.EnvironmentProviderService;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;

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
		startBoxExampleBehaviours(this);		
	}
	
	private void startBoxExampleBehaviours(Agent a){
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
			
			// Gesamt-Behaviour
			SequentialBehaviour demoBehaviour = new SequentialBehaviour();
			demoBehaviour.addSubBehaviour(new MoveToPointBehaviour(this, waypoint, self.getMaxSpeed()));	// Wegpunkt 1
			demoBehaviour.addSubBehaviour(new MoveToPointBehaviour(this, objectPos, self.getMaxSpeed()));   // Wegpunkt 2
			
			// Kiste Aufnehmen
			demoBehaviour.addSubBehaviour(new OneShotBehaviour() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = -8054903564662038080L;

				@Override
				public void action() {
					try {
						EnvironmentProviderHelper helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
						boolean success = helper.assignPassiveObject(targetObject.getId(), getLocalName());
					} catch (ServiceException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
			
			demoBehaviour.addSubBehaviour(new MoveToPointBehaviour(this, waypoint, self.getMaxSpeed()));	// Wegpunkt 3
			demoBehaviour.addSubBehaviour(new MoveToPointBehaviour(this, destPos, self.getMaxSpeed()));		// Wegpunkt 4
			
			// Kiste Absetzen
			demoBehaviour.addSubBehaviour(new OneShotBehaviour() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void action() {
					try {
						EnvironmentProviderHelper helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
						helper.releasePassiveObject(targetObject.getId());
					} catch (ServiceException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			demoBehaviour.addSubBehaviour(new MoveToPointBehaviour(this, endPos, self.getMaxSpeed()));		// Wegpunkt 5 = Zielposition
			
			addBehaviour(demoBehaviour);
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
