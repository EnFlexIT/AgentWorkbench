/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.simulationService.agents;

import jade.core.AID;
import jade.core.ServiceException;
import jade.util.leap.ArrayList;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

import agentgui.envModel.p2Dsvg.controller.Physical2DEnvironmentController;
import agentgui.envModel.p2Dsvg.controller.Physical2DEnvironmentControllerGUI;
import agentgui.envModel.p2Dsvg.ontology.Physical2DEnvironment;
import agentgui.envModel.p2Dsvg.ontology.Physical2DObject;
import agentgui.envModel.p2Dsvg.ontology.PositionUpdate;
import agentgui.envModel.p2Dsvg.provider.EnvironmentProviderHelper;
import agentgui.envModel.p2Dsvg.provider.EnvironmentProviderService;

/**
 * This type of simulation manager should be extended in case that the 'continous2Denvironment' is used.
 * 
 * @see Physical2DEnvironmentControllerGUI
 * @see Physical2DEnvironmentController
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class SimulationManagerAgentPhysical2D extends SimulationManagerAgent {

	private static final long serialVersionUID = 5867692143523662021L;

	/** The EnvironmentProviderHelper. */
	protected EnvironmentProviderHelper envHelper = null;
	
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationManagerAgent#setup()
	 */
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
	 * If its not a physical Environment you have to overwrite the method.
	 *
	 * @param agentAnswer the answers of the simulation agents
	 * @return the object
	 * @throws Exception the Exception
	 */
	public Object updatePhysical2D(Hashtable<AID,Object> agentAnswer) throws Exception {
	
		if (envHelper==null) {
			throw new Exception("The EnvironmentProviderService is currently not running !");
			
		} else {
			
			Object obj = this.myEnvironmentModel.getDisplayEnvironment();
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
	 * Returns the Physical2DObject of the specified identifier.
	 *
	 * @param world The displayable representation of the world
	 * @param id the ID, which is looked for
	 * @return the Physical2DObject object
	 */
	private Physical2DObject getPhysical2DObject(Physical2DEnvironment world, String id)	{
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
	 * Converts the Hashtable of the agent answers to a HashMap.
	 *
	 * @param answers one single Hashtable with exact one answer of all agents
	 * @return The converted HashMap
	 */
	public HashMap<AID, PositionUpdate> convertToPositionUpdateHashmap(Hashtable<AID, Object> answers) {
	
		Set<AID> keys = answers.keySet(); // Let's get the AID
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
	 * Write the answer into the transaction list of the environment service.
	 *
	 * @param positionUpdates the position updates
	 * @throws Exception the Exception
	 */
	public void fordwardToVisualation(HashMap<AID,PositionUpdate> positionUpdates) throws Exception	{
	    
		if (envHelper==null) {
			throw new Exception("The EnvironmentProviderService is currently not running !");
		}

		Set<AID> keys    = positionUpdates.keySet();
		Object[] newKeys = keys.toArray();
		
		for(int i=0;i<newKeys.length;i++) {
			AID key=(AID) newKeys[i];
			envHelper.stepModel(key, positionUpdates.get(key));
		}		
	}


}
