package agentgui.physical2Denvironment.behaviours;

import agentgui.physical2Denvironment.ontology.ActiveObject;
import agentgui.physical2Denvironment.ontology.PassiveObject;
import agentgui.physical2Denvironment.ontology.Physical2DObject;
import agentgui.physical2Denvironment.provider.EnvironmentProviderHelper;
import agentgui.physical2Denvironment.provider.EnvironmentProviderService;
import agentgui.physical2Denvironment.utils.EnvironmentHelper;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;

public class ReleasePassiveObjectBehaviour extends OneShotBehaviour {
	
	private String objectID;

	public ReleasePassiveObjectBehaviour(String objectID) {
		this.objectID = objectID;
	}

	@Override
	public void action() {
		try {
			EnvironmentProviderHelper helper = (EnvironmentProviderHelper) myAgent.getHelper(EnvironmentProviderService.SERVICE_NAME);
			Physical2DObject object = helper.getObject(objectID);
			if(object instanceof PassiveObject && ((PassiveObject)object).getControllingObjectID().equals(myAgent.getLocalName())){
				helper.releasePassiveObject(objectID);
			}else{
				// spaeter
			}
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
