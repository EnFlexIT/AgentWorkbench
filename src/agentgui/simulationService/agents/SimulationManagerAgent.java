/**
 * This class provides the basic functionality for implemented simulation
 * 
 */
package agentgui.simulationService.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;

import java.util.Hashtable;

import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.time.TimeModel;

public abstract class SimulationManagerAgent extends Agent implements SimulationManagerInterface {
	
	private static final long serialVersionUID = -7398714332312572026L;

	protected SimulationServiceHelper simHelper = null;
	
	/**
	 *  The environment model which contains an abstract and   
	 *  a displayable enviroment model as well as a time model 
	 */
	protected EnvironmentModel envModel = new EnvironmentModel();
	protected Hashtable<AID, Object> agentAnswers = null;

	private CyclicSimulationBehavior cSimBehavior = null;
	
	/**
	 *  Mandatory setup()-functionallity
	 */
	protected void setup() {		  
		// --- get the helper for the SimulationService -------------
		try {
		  simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
		  simHelper.setManagerAgent(this.getAID());
		  
		} catch(ServiceException e) {
			  e.printStackTrace();
			  this.doDelete();
			  return;
		}		
	}
	
	/**
	 * This method ads the mandatory CyclicSimulationBehavior to this agent
	 */
	protected void addCyclicSimiulationBehavior() {
		// --- Start the cyclic SimulationBehavior of this manager --
		if (this.cSimBehavior==null) {
			this.cSimBehavior = new CyclicSimulationBehavior();	
		}		
		this.addBehaviour(this.cSimBehavior);
	}
	protected void removeCyclicSimiulationBehavior() {
		// --- Remove the cyclic SimulationBehavior of this manager --
		this.removeBehaviour(this.cSimBehavior);
	}
	
	/**
	 * This method adds the core behavior to the agent which is controlling the 
	 * sequence (cyclic) simulation behavior
	 *    
	 * @author Tim Lewen, Christian Derksen 
	 */
	private class CyclicSimulationBehavior extends CyclicBehaviour {

		private static final long serialVersionUID = 1L;

		public CyclicSimulationBehavior() {
			// --- Get the initial Environment Model locally ------------
			setEnvironmentModel(getInitialEnvironmentModel());
		}
		
		@Override
		public void action() {
			doSingleSimulationSequennce();
		}
	}
	
	protected EnvironmentModel getEnvironmentModel() {
		return envModel;
	}
	protected void setEnvironmentModel(EnvironmentModel environmentModel) {
		this.envModel = environmentModel;
	}
	
	protected TimeModel getTimeModel() {
		return this.envModel.getTimeModel();
	}
	protected void setTimeModel(TimeModel timeModel) {
		this.envModel.setTimeModel(timeModel);
	}
	
	protected Object getAbstractEnvironment() {
		return this.envModel.getAbstractEnvironment();
	}
	protected void setAbstractEnvironment(Object abstractEnvironment) {
		this.envModel.setAbstractEnvironment(abstractEnvironment);
	}

	protected Object getDisplayEnvironment() {
		return this.envModel.getDisplayEnvironment();
	}
	protected void setDisplayEnvironment(Object displayEnvironment) {
		this.envModel.setDisplayEnvironment(displayEnvironment);
	}
	
	
	/**
	 * Steps the simulation. As a side effect a transition of current environment is written into the transaction list
	 * @throws Exception
	 */
	protected void stepSimulation() throws ServiceException {
		simHelper.stepSimulation(this.envModel);
	}
	protected void stepSimulation(EnvironmentModel environmentModel) throws ServiceException {
		this.setEnvironmentModel(environmentModel);
		simHelper.stepSimulation(this.envModel);
	}

	/**
	 * @return the agentAnswers
	 */
	protected Hashtable<AID, Object> getAgentAnswers() {
		return agentAnswers;
	}
	/**
	 * @param agentAnswers the agentAnswers to set
	 */
	protected void setAgentAnswers(Hashtable<AID, Object> agentAnswers) {
		this.agentAnswers = agentAnswers;
	}

	/**
	 * 
	 * @param numberOfAgents - If the value us 1 the method waits exactly for one agent
	 * @return The answers of the agent. The key of the hashmap is the AID
	 * @throws ServiceException- Through the distribution it's possible that one service is not available
	 */
	protected void waitForAgentAnswers(int numberOfAgentAnswersExpected) throws ServiceException {
		while (simHelper.getEnvironmentInstanceNextParts().size()!=numberOfAgentAnswersExpected) {
		  doWait(5);
		}
		this.setAgentAnswers(simHelper.getEnvironmentInstanceNextParts());
	}
	
	/**
	 * Resets the answers of the agents in the simulation service to an empty Hashmap
	 * @throws Exception
	 */
	protected void resetEnvironmentInstanceNextParts() throws ServiceException {
		simHelper.resetEnvironmentInstanceNextParts();
	}
	

	
	
	
}
