package agentgui.simulationService.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
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

	private EnvironmentModel envModel = new EnvironmentModel();
	private boolean running=false;
	protected SimulationServiceHelper simHelper = null;
	protected EnvironmentProviderHelper envHelper = null; 
	protected Hashtable<AID, Object> agentAnswers = null;
	protected int numberOfAgents=0;
	
	/**
	 * 
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
	 * 
	 */
	public abstract void initSimulation();
	
	/**
	 * 
	 */
	public abstract void simulationLogic();
	/**
	 * 
	 * @param agentAnswer
	 */
	public abstract void createNewEnvironment(Hashtable<AID,Object> agentAnswer);

	/**
	 * 
	 * @return
	 */
	public EnvironmentModel getEnvironmentModel() {
		return envModel;
	}
	/**
	 * Where?
	 * @param envModel
	 */
	public void setEnvironmentModelPrivate(EnvironmentModel environmentModel) {
		this.envModel = environmentModel;
	}
	/**
	 * Where?
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
	 * 
	 * @return
	 */
	public TimeModel getTimeModel() {
		return this.envModel.getTimeModel();
	}

	/**
	 * 
	 * @param timeModel
	 */
	public void setTimeModelPrivate(TimeModel timeModel) {
		this.envModel.setTimeModel(timeModel);
	}
	/**
	 * 
	 * @param timeModel
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
	 * 
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
	 * 
	 */
	public void pauseSimulation() {
		running = false;
	}
	/**
	 * 
	 */
	public void continueSimulation() {
		running = true;
	}
	
	/**
	 * 
	 * @param numberOfAgents
	 * @return
	 * @throws ServiceException
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
			System.out.println("Display Enviroment is null!");
		}
		
		if (obj instanceof Physical2DEnvironment) {
			
			Physical2DEnvironment world=(Physical2DEnvironment) obj;
			Set<AID> keys = agentAnswer.keySet();
			Iterator<AID> it = keys.iterator();
			while(it.hasNext()) {
				
				AID aid=it.next();
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
	 * 
	 * @param world
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Physical2DObject getPhysical2DObject(Physical2DEnvironment world,String id)	{
	
		Iterator<Physical2DObject> it = world.getRootPlayground().getAllChildObjects();
		while(it.hasNext()) {
			Physical2DObject playGround = it.next();
			if(playGround.getId().equals(id)) {
				return playGround;
			}
		} 
		return null;
	}
		   
		   
	public HashMap<AID,PositionUpdate> convertToPositionUpdateHashmap(Hashtable<AID, Object> answer) {
		
		Enumeration<AID> keys = answer.keys(); // Let's get the AID
		HashMap<AID, PositionUpdate> result= new HashMap<AID, PositionUpdate> ();
		while (keys.hasMoreElements()) {
			
			AID aid = keys.nextElement();
			
			Object obj = agentAnswers.get(aid); // Get Answer
		    PositionUpdate posUpdate= (PositionUpdate) obj;
		    result.put(aid, posUpdate);
		}
		return result;
	}
	
	
	
	public void fordwardToVisualation(HashMap<AID,PositionUpdate> pos)	{
	    
		HashSet<Physical2DObject> movingObjects=envHelper.getCurrentlyMovingObjects();
	    // Clear map
		movingObjects.clear();
		Set<AID> keys=pos.keySet();
		Iterator<AID> it= keys.iterator();
		while(it.hasNext())
		{
			AID aid=it.next();
			Physical2DObject obj=envHelper.getObject(aid.getLocalName());
			obj.setPosition(pos.get(aid).getNewPosition());
			movingObjects.add(obj);
		}
		
	}	
		
	/**
	 *  
	 * @author Tim Lewen
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
