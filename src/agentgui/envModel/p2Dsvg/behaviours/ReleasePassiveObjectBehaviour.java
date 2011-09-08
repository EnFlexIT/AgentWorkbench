package agentgui.envModel.p2Dsvg.behaviours;

import agentgui.envModel.p2Dsvg.ontology.ActiveObject;
import agentgui.envModel.p2Dsvg.ontology.PassiveObject;
import agentgui.envModel.p2Dsvg.ontology.Physical2DObject;
import agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper;
import agentgui.envModel.p2Dsvg.provider.EnvironmentProviderService;
import agentgui.envModel.p2Dsvg.utils.EnvironmentHelper;
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
