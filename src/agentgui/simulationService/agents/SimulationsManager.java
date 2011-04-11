/**
 * This class provides the basic functionality for implemented simulation
 * 
 */
package agentgui.simulationService.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.util.leap.ArrayList;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

import agentgui.physical2Denvironment.ontology.Physical2DEnvironment;
import agentgui.physical2Denvironment.ontology.Physical2DObject;
import agentgui.physical2Denvironment.ontology.PositionUpdate;
import agentgui.physical2Denvironment.provider.EnvironmentProviderHelper;
import agentgui.physical2Denvironment.provider.EnvironmentProviderService;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.time.TimeModel;

public abstract class SimulationsManager extends Agent {
	
	private static final long serialVersionUID = -7398714332312572026L;

	/**
	 *  The enviromentmodel which contains an abstract and an displayable enviroment and a time model 
	 */
	private EnvironmentModel envModel = new EnvironmentModel();
	private boolean running=false;
	protected SimulationServiceHelper simHelper = null;
	protected EnvironmentProviderHelper envHelper = null; 
	protected Hashtable<AID, Object> agentAnswers = null;
	/**
	 * The expected numbers of answer
	 */
	protected int numberOfAgents=0;
	
	/**
	 *  Entry Point
	 */
	protected void setup() {		  
		try {
		  envHelper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
		  simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
		  simHelper.setManagerAgent(this.getAID());
		  
		} catch(ServiceException e) {
			  e.printStackTrace();
		}		
		this.initSimulation();
		this.addBehaviour(new SimulationBehavior());
	}

	/**
	 *  This method is used for initialising the  simulation, which means that the enviromentmodel would be set there.
	 */
	public abstract void initSimulation();
	
	/**
	 *  The logic of the simulation is implemented here. It's highly recommended to use the provided methods for implementing the logic.
	 */
	public abstract void simulationLogic();
	/**
	 * This method have to create the new state of the environment based on the agent's answer.
	 * @param agentAnswer It's the Hashmap which is returned from the simulation service when waitForAgents is called
	 */
	public abstract void createNewEnvironment(Hashtable<AID,Object> agentAnswer);

	/**
	 *  Returns the environment model
	 * @return Enviromentsmodel
	 */
	public EnvironmentModel getEnvironmentModel() {
		return envModel;
	}
	/**
	 * Sets the  environment only locally.  
	 * @param envModel
	 */
	public void setEnvironmentModelPrivate(EnvironmentModel environmentModel) {
		this.envModel = environmentModel;
	}
	/**
	 *  Sets the  environment and the model is propagated to all nodes.
	 * @param envModel
	 */
	public void setEnvironmentModelDistributed(EnvironmentModel environmentModel) {
		this.envModel = environmentModel;
		try	{
			simHelper.setEnvironmentModel(environmentModel);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *  Returns the current time model
	 * @return the current time model
	 */
	public TimeModel getTimeModel() {
		return this.envModel.getTimeModel();
	}

	/**
	 *  Sets the time model locally
	 * @param timeModel The current time modell
	 */
	public void setTimeModelPrivate(TimeModel timeModel) {
		this.envModel.setTimeModel(timeModel);
	}
	/**
	 *  Sets the current time model and the time model is propagated to all nodes
	 * @param timeModel The current time model
	 */
	public void setTimeModelDistributed(TimeModel timeModel) {
		this.envModel.setTimeModel(timeModel);
		try	{
			simHelper.setEnvironmentModel(this.envModel);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Steps the simulation. As a side effect a transition of current environment is written into the transaction list
	 * @throws Exception
	 */
	public void stepSimulation() throws Exception {
		simHelper.stepSimulation(this.envModel);
	}
	public void stepSimulation(EnvironmentModel environmentModel) throws Exception {
		this.setEnvironmentModelPrivate(environmentModel);
		simHelper.stepSimulation(this.envModel);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void resetEnvironmentInstanceNextParts() throws Exception {
		simHelper.resetEnvironmentInstanceNextParts();
	}
	
	/**
	 *  Pauses the simulation
	 */
	public void pauseSimulation() {
		running = false;
	}
	/**
	 *  Continous the simulation
	 */
	public void continueSimulation() {
		running = true;
	}
	
	/**
	 * 
	 * @param numberOfAgents - If the value us 1 the method waits exactly for one agent
	 * @return The answers of the agent. The key of the hashmap is the AID
	 * @throws ServiceException- Through the distribution it's possible that one service is not available
	 */
	public Hashtable<AID, Object> waitForAgentAnswer(int numberOfAgents) throws ServiceException {
		while (simHelper.getEnvironmentInstanceNextParts().size()!=numberOfAgents) {
		  doWait(100);
		}
		return simHelper.getEnvironmentInstanceNextParts();
	}
	
	
	
	/**
	 * If its not a physical Environment you have to overwrite the method
	 * @param agentAnswer
	 * @throws Exception
	 */
	public Object updatePhysical2D(Hashtable<AID,Object> agentAnswer) throws Exception {
	
		Object obj = envModel.getDisplayEnvironment();
		if(obj==null) {
			return null;
		}		
		if (obj instanceof Physical2DEnvironment) {
			Physical2DEnvironment world=(Physical2DEnvironment) obj;
			Set<AID> keys = agentAnswer.keySet();
			Object [] newKeys= keys.toArray();
			for(int i=0;i<newKeys.length;i++)
			{
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
		
		return null;
	}
	

	/**
	 * Returns the Physical2DObject of a specific id
	 * @param world The displayable representation of the word 
	 * @param id  the ID which is looked for
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Physical2DObject getPhysical2DObject(Physical2DEnvironment world,String id)	
	{
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
	public void fordwardToVisualation(HashMap<AID,PositionUpdate> pos)	{
	    
		Set<AID> keys=pos.keySet();
		Object [] newKeys= keys.toArray();
		
		for(int i=0;i<newKeys.length;i++)
		{
			AID key=(AID) newKeys[i];
			envHelper.stepModel(key, pos.get(key));
			
		}		
	}	
		
	/**
	 *  
	 * @author Tim Lewen
	 *  The simulation logic is running in a cylicBehaviour
	 */
	private class SimulationBehavior extends CyclicBehaviour {

		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			if(running) {
				simulationLogic();
			}
		}
	}
	
}
