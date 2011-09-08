package agentgui.simulationService.agents;

import jade.core.AID;
import jade.core.ServiceException;
import jade.util.leap.ArrayList;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

import agentgui.envModel.p2Dsvg.ontology.Physical2DEnvironment;
import agentgui.envModel.p2Dsvg.ontology.Physical2DObject;
import agentgui.envModel.p2Dsvg.ontology.PositionUpdate;
import agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper;
import agentgui.envModel.p2Dsvg.provider.EnvironmentProviderService;

public abstract class SimulationManagerAgentPhysical2D extends SimulationManagerAgent implements SimulationManagerInterface {

	private static final long serialVersionUID = 5867692143523662021L;

	protected EnvironmentProviderHelper envHelper = null;
	
	
	@Override
	protected void setup() {
		super.setup();
		// --- get the helper for the EnvironmentService ------------
		try {
			envHelper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
		} catch (ServiceException e) {
			e.printStackTrace();
			this.doDelete();
			return;
		}
	}
	
	/**
	 * If its not a physical Environment you have to overwrite the method
	 * @param agentAnswer
	 * @throws Exception
	 */
	public Object updatePhysical2D(Hashtable<AID,Object> agentAnswer) throws Exception {
	
		if (envHelper==null) {
			throw new Exception("The EnvironmentProviderService is currently not running !");
			
		} else {
			
			Object obj = this.envModel.getDisplayEnvironment();
			if(obj==null) {
				return null;
			}		
			if (obj instanceof Physical2DEnvironment) {
				Physical2DEnvironment world=(Physical2DEnvironment) obj;
				Set<AID> keys = agentAnswer.keySet();
				Object [] newKeys= keys.toArray();
				for(int i=0;i<newKeys.length;i++) {
					AID aid=(AID) newKeys[i];
					Physical2DObject physicalObj = this.getPhysical2DObject(world, aid.getLocalName());
					Object tmpObj = agentAnswer.get(aid);
					PositionUpdate posUpdate = null;
					if (tmpObj instanceof PositionUpdate) {
						posUpdate= (PositionUpdate) tmpObj;
						physicalObj.setPosition(posUpdate.getNewPosition());
						
					} else  {
						throw new Exception("Please overwrite createNewEnvironment because the value of the Hashmap can't be casted to PositionUpdate");
					}
				
				}
				 
			} else {
				throw new Exception("Please overwrite createNewEnvironment because the environment can't be casted to Physical2DEnvironment");	
			}
			
		}
		return null;
	}
	
	/**
	 * Returns the Physical2DObject of a specific id
	 * @param world The displayable representation of the word 
	 * @param id  the ID which is looked for
	 * @return
	 */
	private Physical2DObject getPhysical2DObject(Physical2DEnvironment world,String id)	{
		ArrayList list=(ArrayList) world.getRootPlayground().getChildObjects();
		for(int i=0;i<list.size();i++) {
			Physical2DObject playGround =(Physical2DObject) list.get(i);
			if(playGround.getId().equals(id)) {
				return playGround;
			}
		} 
		return null;
	}
	
	/**
	 * Converts the Hashmap to the specific answer type
	 * @param answer - One single Hashmap with exact one answer of all agents
	 * @return A converted Hashmap 
	 */
	public HashMap<AID,PositionUpdate> convertToPositionUpdateHashmap(Hashtable<AID, Object> answer) {
	
		Set<AID> keys = answer.keySet(); // Let's get the AID
		Object [] newKeys = keys.toArray();
		HashMap<AID, PositionUpdate> result= new HashMap<AID, PositionUpdate> ();
		for(int i=0;i<newKeys.length;i++) {
			
			AID aid = (AID) newKeys[i];			
			Object obj = agentAnswers.get(aid); // Get Answer
		    PositionUpdate posUpdate= (PositionUpdate) obj;
		    result.put(aid, posUpdate);
		}
		return result;
	}
	
	
	/**
	 *  Write the answer into the transaction list of the enviroment service
	 * @param A converted Hashmap of the Agent answers
	 */
	public void fordwardToVisualation(HashMap<AID,PositionUpdate> pos) throws Exception	{
	    
		if (envHelper==null) {
			throw new Exception("The EnvironmentProviderService is currently not running !");
		}

		Set<AID> keys=pos.keySet();
		Object [] newKeys= keys.toArray();
		
		for(int i=0;i<newKeys.length;i++) {
			AID key=(AID) newKeys[i];
			envHelper.stepModel(key, pos.get(key));
		}		
	}


}
