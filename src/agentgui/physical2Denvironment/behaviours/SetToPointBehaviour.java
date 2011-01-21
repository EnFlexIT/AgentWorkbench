package agentgui.physical2Denvironment.behaviours;

import agentgui.physical2Denvironment.ontology.ActiveObject;
import agentgui.physical2Denvironment.ontology.Movement;
import agentgui.physical2Denvironment.ontology.Position;
import agentgui.physical2Denvironment.provider.EnvironmentProviderHelper;
import agentgui.physical2Denvironment.provider.EnvironmentProviderService;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;

public class SetToPointBehaviour  extends OneShotBehaviour{
	Position pos = null;
	private EnvironmentProviderHelper helper=null;
	
	public SetToPointBehaviour(Agent a, Position newPosition)
	{
	   super(a);
	   this.pos=newPosition;
		try {
			this.helper = (EnvironmentProviderHelper) myAgent.getHelper(EnvironmentProviderService.SERVICE_NAME);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void action() {
		ActiveObject self = (ActiveObject) helper.getObject(myAgent.getLocalName());
		self.setPosition(this.pos);
		Movement movement = new Movement();
		movement.setXPosChange(0.0f);
		movement.setYPosChange(0.0f);
		this.helper.setMovement(myAgent.getLocalName(), movement);
		
		
		
	}

}
