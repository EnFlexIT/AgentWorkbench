package sma.agents;

import mas.environment.ontology.ActiveObject;
import mas.environment.ontology.Position;
import mas.environment.provider.EnvironmentProviderHelper;
import mas.environment.provider.EnvironmentProviderService;
import jade.core.Agent;
import jade.core.ServiceException;

/**
 * Dummy-Implementation for testing the DisplayAgent
 * @author Nils
 *
 */
public class SoftBot extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void setup(){
		try {
			EnvironmentProviderHelper helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
//			Position ownPos = null;
//			
//			if(helper.isEnvSet()){
//				ownPos = helper.getObjectPosition(getLocalName());
//			}
			
			if(helper != null){
				helper.horizontalTest("Services sind doof");
			}
			
//			if(ownPos != null){
//				System.out.println(getLocalName()+": Positionsabruf erfolgreich: "+ownPos.getXPos()+":"+ownPos.getYPos());
//			}else{
//				System.err.println(getLocalName()+": Positionsabruf fehlgeschlagen!");
//			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}		
}
