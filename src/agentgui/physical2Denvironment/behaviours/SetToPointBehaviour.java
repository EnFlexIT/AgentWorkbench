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
	Position old = null;
	private EnvironmentProviderHelper helper=null;
	
	public SetToPointBehaviour(Agent a, Position newPosition,Position oldPosition)
	{
	   super(a);
	   this.pos=newPosition;
	   this.old=oldPosition;
	  
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
		movement.setXPosChange(Math.abs(this.old.getXPos()-this.pos.getXPos()));
		movement.setYPosChange(Math.abs(this.old.getYPos()-this.pos.getYPos()));
		this.helper.setMovement(myAgent.getLocalName(), movement);
		
		
		
		
	}

}
