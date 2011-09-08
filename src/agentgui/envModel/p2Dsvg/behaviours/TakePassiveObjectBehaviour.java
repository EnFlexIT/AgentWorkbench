package agentgui.envModel.p2Dsvg.behaviours;

import agentgui.envModel.p2Dsvg.ontology.PassiveObject;
import agentgui.envModel.p2Dsvg.ontology.Physical2DObject;
import agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper;
import agentgui.envModel.p2Dsvg.provider.EnvironmentProviderService;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;

public class TakePassiveObjectBehaviour extends OneShotBehaviour {
	
	private String objectID;
	
	public TakePassiveObjectBehaviour(String objectID){
		this.objectID = objectID;
	}

	@Override
	public void action() {
		try {
			EnvironmentProviderHelper helper = (EnvironmentProviderHelper) myAgent.getHelper(EnvironmentProviderService.SERVICE_NAME);
			// Get object from the EPS
			Physical2DObject object = helper.getObject(objectID);
			// Check type
			if(object instanceof PassiveObject){
				// take
				helper.assignPassiveObject(objectID, myAgent.getLocalName());
			}else{
				// Spaeter
			}
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
