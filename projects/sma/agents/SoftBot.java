package sma.agents;

import agentgui.physical2Denvironment.behaviours.MoveToPointBehaviour;
import agentgui.physical2Denvironment.behaviours.ReleasePassiveObjectBehaviour;
import agentgui.physical2Denvironment.behaviours.TakePassiveObjectBehaviour;
import agentgui.physical2Denvironment.ontology.ActiveObject;
import agentgui.physical2Denvironment.ontology.Physical2DObject;
import agentgui.physical2Denvironment.ontology.Position;
import agentgui.physical2Denvironment.provider.EnvironmentProviderHelper;
import agentgui.physical2Denvironment.provider.EnvironmentProviderService;
import jade.core.Agent;
import jade.core.ServiceException;
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
			
			// Container behaviour
			SequentialBehaviour demoBehaviour = new SequentialBehaviour();
			
			// Movement
			demoBehaviour.addSubBehaviour(new MoveToPointBehaviour(this, waypoint, self.getMaxSpeed()));	// Waypoint 1
			demoBehaviour.addSubBehaviour(new MoveToPointBehaviour(this, objectPos, self.getMaxSpeed()));   // Waypoint 2
			
			// Pick up box
			demoBehaviour.addSubBehaviour(new TakePassiveObjectBehaviour("Box2"));
			
			// Movement
			demoBehaviour.addSubBehaviour(new MoveToPointBehaviour(this, waypoint, self.getMaxSpeed()));	// Waypoint 3
			demoBehaviour.addSubBehaviour(new MoveToPointBehaviour(this, destPos, self.getMaxSpeed()));		// Waypoint 4
			
			// Release box
			demoBehaviour.addSubBehaviour(new ReleasePassiveObjectBehaviour("Box2"));
			
			// Movement
			demoBehaviour.addSubBehaviour(new MoveToPointBehaviour(this, endPos, self.getMaxSpeed()));		// Waypoint 5
			
			// Start behaviour
			addBehaviour(demoBehaviour);
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
