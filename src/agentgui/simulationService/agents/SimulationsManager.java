package agentgui.simulationService.agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.apache.batik.apps.svgbrowser.JSVGViewerFrame.PlayAction;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

import agentgui.physical2Denvironment.ontology.Physical2DObject;
import agentgui.physical2Denvironment.ontology.Physical2DEnvironment;
import agentgui.physical2Denvironment.ontology.PlaygroundObject;
import agentgui.physical2Denvironment.ontology.PositionUpdate;
import agentgui.physical2Denvironment.provider.EnvironmentProviderHelper;
import agentgui.physical2Denvironment.provider.EnvironmentProviderService;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.time.TimeModel;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;

public abstract class SimulationsManager extends Agent {
	
	private EnvironmentModel env = null;
	private TimeModel timeModel = null;
	private boolean running=false;
	private SimulationServiceHelper simHelper = null;
	private AID aid= null;
	private EnvironmentProviderHelper envHelper = null; 
	  protected void setup() 
	  {
		  try
		  {
		  envHelper=(EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
		  simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
		  }
		  catch(ServiceException e)
		  {
			  e.printStackTrace();
		  }
		  initSimulation();
		  addBehaviour(new SimulationBehavior());
	  }
	
	

	public AID getAid() {
		return aid;
	}



	public void setAid(AID aid) throws Exception {
		this.aid = aid;
		simHelper.setManagerAgent(aid);
	}



	public EnvironmentModel getEnv() {
		return env;
	}

	public void setEnv(EnvironmentModel env) {
		this.env = env;
	}

	public TimeModel getTimeModel() {
		return timeModel;
	}

	public void setTimeModel(TimeModel timeModel) {
		this.timeModel = timeModel;
	}
	
	public void stepSimulation() throws Exception
	{
		simHelper.stepSimulation(env);
	}
	
	public void resetEnvironmentInstanceNextParts() throws Exception
	{
		simHelper.resetEnvironmentInstanceNextParts();
	}
	
	public void pauseSimulation()
	{
	
		running=false;
	}
	
	public void continueSimulation()
	{
		
		running=true;
	}
	
	public  Hashtable<AID, Object> waitForAgentAnswer(int numberOfAgents) throws ServiceException
	{
		while (simHelper.getEnvironmentInstanceNextParts().size()!=numberOfAgents)
		{
		  doWait(100);
		}
		
		
		return simHelper.getEnvironmentInstanceNextParts();

	}
	
	
	
	abstract void initSimulation();
	abstract void simulationLogic();
	public void createNewEnviroment(HashMap<AID,Object> agentAnswer) throws Exception // If its not a physical Eviroment you have to overwrite the method
	{
	
		Object obj=env.getDisplayEnvironment();
		if(obj instanceof Physical2DEnvironment)
		{
			Physical2DEnvironment world=(Physical2DEnvironment) obj;
			Set<AID> keys=agentAnswer.keySet();
			Iterator<AID> it= keys.iterator();
		 
			while(it.hasNext())
			{
				AID aid=it.next();
				Physical2DObject physicalObj=this.getPhysical2DObject(world, aid.getLocalName());
				Object tmpObj = agentAnswer.get(aid);
				PositionUpdate posUpdate = null;
				if(tmpObj instanceof PositionUpdate)
				{
					posUpdate= (PositionUpdate) tmpObj;
					physicalObj.setPosition(posUpdate.getNewPosition());
				}
				else
				{
				 throw new Exception("Please overwrite createNewEvironment because the value of the Hashmap can't be casted to PositionUpdate");
				
				}
			
			}
		}
		else
		{
			 throw new Exception("Please overwrite createNewEvironment because the enviroment can't be casted to Physical2DEnviroment");	
		}
		
	
	}
	
	private Physical2DObject getPhysical2DObject(Physical2DEnvironment world,String id)
	{
	
		   Iterator<PlaygroundObject> it=world.getRootPlayground().getAllChildObjects();
		   while(it.hasNext())
		   {
		   PlaygroundObject playGround = it.next();
		   
		   		if(playGround.getId().equals(id))
		   		{
			   return playGround;
		   		}
		   } 
		   return null;
	}
		   
		   
	
	
	
	
	public void fordwardToVisualation(HashMap<AID,PositionUpdate> pos)
	{
	    
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
		
		
	private class SimulationBehavior extends CyclicBehaviour
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			if(running)
			{
			simulationLogic();
			}
			
		}
		
	}
	
}
