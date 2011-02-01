package agentgui.simulationService.agents;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import edu.uci.ics.jung.algorithms.util.DiscreteDistribution;



import agentgui.physical2Denvironment.ontology.Physical2DObject;
import agentgui.physical2Denvironment.ontology.Physical2DEnvironment;
import agentgui.physical2Denvironment.ontology.PositionUpdate;
import agentgui.physical2Denvironment.provider.EnvironmentProviderHelper;
import agentgui.physical2Denvironment.provider.EnvironmentProviderService;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.time.TimeModel;
import agentgui.simulationService.time.TimeModelDiscrete;
import agentgui.simulationService.time.TimeModelStroke;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;

public abstract class SimulationsManager extends Agent {
	
	private EnvironmentModel env = null;
	private boolean running=false;
	protected SimulationServiceHelper simHelper = null;
	private AID aid= null;
	protected EnvironmentProviderHelper envHelper = null; 
	protected Hashtable<AID, Object> agentAnswers = null;
	protected int numberOfAgents=0;
	
	
	
	
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



	public EnvironmentModel getEnvironmentModel() {
		return env;
	}

	public void setEnvironmentModel(EnvironmentModel env) {
		this.env = env;
		try
		{
		simHelper.setEnvironmentModel(env);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public TimeModel getTimeModel() {
		return this.env.getTimeModel();
	}

	public void setTimeModel(TimeModel timeModel) {
		this.env.setTimeModel(timeModel);
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
	
	
	
	public abstract void initSimulation();
	public abstract void simulationLogic();
	public void createNewEnvironment(Hashtable<AID,Object> agentAnswer) throws Exception // If its not a physical Environment you have to overwrite the method
	{
	
		Object obj=env.getDisplayEnvironment();
		if(obj==null)
		{
			
			System.out.println("Display Enviroment ist null!");
		}
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
				 throw new Exception("Please overwrite createNewEnvironment because the value of the Hashmap can't be casted to PositionUpdate");
				
				}
			
			}
			
				if(this.env.getTimeModel() instanceof TimeModelDiscrete)
				{
				
					TimeModelDiscrete tm=(TimeModelDiscrete) this.env.getTimeModel(); 
					tm.step();
					this.env.setTimeModel(tm);
				
				}
				else
				{
					TimeModelStroke ts= (TimeModelStroke) this.env.getTimeModel(); 
					ts.step();
					this.env.setTimeModel(ts);
			   }
			 
		}
		else
		{
			 throw new Exception("Please overwrite createNewEnvironment because the environment can't be casted to Physical2DEnvironment");	
		}
		
	
	}
	

	
	private Physical2DObject getPhysical2DObject(Physical2DEnvironment world,String id)
	{
	   
		   Iterator<Physical2DObject> it=world.getRootPlayground().getAllChildObjects();
		   while(it.hasNext())
		   {
			   Physical2DObject playGround = it.next();
		   
		   		if(playGround.getId().equals(id))
		   		{
			   return playGround;
		   		}
		   } 
		   return null;
	}
		   
		   
	public HashMap<AID,PositionUpdate>  convertToPositionUpdateHashmap(Hashtable<AID, Object> answer)
	{
		Enumeration<AID> keys = answer.keys(); // Let's get the AID
		HashMap<AID, PositionUpdate> result= new HashMap<AID, PositionUpdate> ();
		while (keys.hasMoreElements())
		{
			AID aid = keys.nextElement();
			
			Object obj = agentAnswers.get(aid); // Get Answer
		    PositionUpdate posUpdate= (PositionUpdate) obj;
		    result.put(aid, posUpdate);
		}
		return result;
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
