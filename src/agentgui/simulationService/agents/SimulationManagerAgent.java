/**
 * This class provides the basic functionality for implemented simulation
 * 
 */
package agentgui.simulationService.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;

import java.util.Hashtable;
import java.util.Vector;

import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.sensoring.ServiceSensorManager;
import agentgui.simulationService.time.TimeModel;
import agentgui.simulationService.transaction.EnvironmentNotification;

public abstract class SimulationManagerAgent extends Agent implements SimulationManagerInterface {
	
	private static final long serialVersionUID = -7398714332312572026L;

	protected SimulationServiceHelper simHelper = null;
	protected ServiceSensorManager mySensor = null;
	
	/**
	 *  The environment model which contains an abstract and   
	 *  a displayable enviroment model as well as a time model 
	 */
	protected EnvironmentModel envModel = new EnvironmentModel();
	protected Hashtable<AID, Object> agentAnswers = null;

	private CyclicSimulationBehavior simBehavior = null;

	private CyclicNotificationHandler notifyHandler = null;
	private Vector<EnvironmentNotification> notifications = new Vector<EnvironmentNotification>();
	
	/**
	 *  Mandatory setup()-functionality
	 */
	@Override
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
		this.sensorPlugIn();
		this.addNotificationHandler();
	}
	@Override
	protected void takeDown() {
		this.removeNotificationHandler();
		this.sensorPlugOut();
	}
	
	/**
	 * This Method plugs IN the service sensor
	 */
	protected void sensorPlugIn() {
		// --- Start the ServiceSensor ------------------------------
		mySensor = new ServiceSensorManager(this);
		// --- Register the sensor to the SimulationService ---------
		try {
			simHelper.sensorPlugIn4Manager(mySensor);	
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	/**
	 * This Method plugs OUT the service sensor
	 */
	protected void sensorPlugOut() {
		// --- plug-out the Sensor ----------------------------------
		try {
			simHelper.sensorPlugOut4Manager(mySensor);	
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		mySensor = null;
	}
	
	/**
	 * This method adds the mandatory CyclicSimulationBehavior to this agent
	 */
	protected void addSimulationBehavior() {
		// --- Start the cyclic SimulationBehavior of this manager --
		if (this.simBehavior==null) {
			this.simBehavior = new CyclicSimulationBehavior();	
		}		
		this.addBehaviour(this.simBehavior);
	}
	protected void removeSimulationBehavior() {
		// --- Remove the cyclic SimulationBehavior of this manager --
		this.removeBehaviour(this.simBehavior);
	}
	
	/**
	 * This method adds the core behavior to the agent which is controlling the 
	 * sequence (cyclic) simulation behavior
	 *    
	 * @author Christian Derksen 
	 */
	private class CyclicSimulationBehavior extends CyclicBehaviour {
		private static final long serialVersionUID = 7456541169963374884L;
		public CyclicSimulationBehavior() {
			// --- Get the initial Environment Model locally ------------
			if (getEnvironmentModel().isEmpty()) {
				setEnvironmentModel(getInitialEnvironmentModel());	
			}
		}
		@Override
		public void action() {
			doSingleSimulationSequennce();
			block();
		}
	}
	
	/**
	 * This method has to be called if the next simulation step can be executed
	 */
	protected void doNextSimulationStep() {
		this.simBehavior.restart();
	}
	
	/**
	 * Steps the simulation. As a side effect a transition of current environment is written into the transaction list
	 * @throws Exception
	 */
	protected void stepSimulation(int answersExpected) throws ServiceException {
		simHelper.stepSimulation(this.envModel, answersExpected);
	}
	protected void stepSimulation(EnvironmentModel environmentModel, int answersExpected) throws ServiceException {
		this.setEnvironmentModel(environmentModel);
		simHelper.stepSimulation(this.envModel, answersExpected);
	}

	/**
	 * Resets the answers of the agents in the simulation service to an empty Hashmap
	 * @throws Exception
	 */
	protected void resetEnvironmentInstanceNextParts() throws ServiceException {
		simHelper.resetEnvironmentInstanceNextParts();
	}
	
	/**
	 * This method will be used by the ServiceActuatorManager (class) to inform
	 * this manager about the agent answers for environment changes. It can be either 
	 * used to do this asynchron or synchron. It is highly recommended to do
	 * this asynchron, so that the agencie can act parallel and not sequently.
	 *  
	 * @param agentAnswers
	 * @param aSynchron
	 */
	public void putAgentAnswers(Hashtable<AID, Object> agentAnswers, boolean aSynchron) {
		this.setAgentAnswers(agentAnswers);
		if (aSynchron==true) {
			this.addBehaviour(new AgentAnswerStimulus(agentAnswers));	
		} else {
			this.proceedAgentAnswers(agentAnswers);	
		}		
	}
	/**
	 * This Behaviour is used to stimulate the manager from the outside 
	 * in a asynchronous way.
	 * @author derksen
	 */
	private class AgentAnswerStimulus extends OneShotBehaviour {
		private static final long serialVersionUID = 1441989543791055996L;
		private Hashtable<AID, Object> aa = null;
		public AgentAnswerStimulus(Hashtable<AID, Object> answersHash) {
			aa = answersHash;
		}
		@Override
		public void action() {
			proceedAgentAnswers(aa);
		}
	}
	/**
	 * This method is called if a stimulus from the outside reached this agent.
	 * It can be overwritten in the child class to act on the agent answers
	 * in order to build the next EnvironmentModel. 
	 */
	protected void proceedAgentAnswers(Hashtable<AID, Object> agentAnswers) {}
	
	
	/**
	 * This method can be used to transfer any kind of information to one member of the current environment model 
	 * @param receiverAID
	 * @param notification
	 * @return
	 */
	protected boolean sendAgentNotification(AID receiverAID, Object notification) {
		boolean send = false;
		EnvironmentNotification myNotification = new EnvironmentNotification(this.getAID(), true, notification);
		try {
			send = simHelper.notifySensorAgent(receiverAID, myNotification);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return send;
	}
	
	/**
	 * This method can be invoked from the simulation service, if 
	 * a notification for the manager has to be delivered 
	 * @param notification
	 * @param aSynchron
	 */
	public void setManagerNotification(EnvironmentNotification notification) {
		// --- place the notification into the notification vector -------
		synchronized (this.notifications) {
			this.notifications.add(notification);	
		}
		// --- restart the CyclicNotificationHandler ---------------------
		notifyHandler.restart();	
			
	}
	/**
	 * This method adds the CyclicNotificationHandler to this agent
	 */
	private void addNotificationHandler() {
		if (this.notifyHandler==null) {
			this.notifyHandler = new CyclicNotificationHandler();	
		}		
		this.addBehaviour(this.notifyHandler);
	}
	/**
	 * This method removes the CyclicNotificationHandler from this agent
	 */
	private void removeNotificationHandler() {
		this.removeBehaviour(this.notifyHandler);
	}

	private class CyclicNotificationHandler extends CyclicBehaviour {
		private static final long serialVersionUID = 4638681927192305608L;
		@Override
		public void action() {
			
			boolean removeFirstElement = false;
			
			// --- Get the first element and work on it ------------------
			if (notifications.size()!=0) {
				EnvironmentNotification notification = notifications.get(0);
				onManagerNotification(notification);
				removeFirstElement = true;				
			}
			
			// --- remove this element and control the notifications -----
			synchronized (notifications) {
				if (removeFirstElement==true) {
					notifications.remove(0);
				}
				if (notifications.size()==0) {
					block();
				}
			}
		}
	}
	/**
	 * This mehtod will be executed if a ManagerNotification arrives this agent
	 * @param notification
	 */
	protected void onManagerNotification(EnvironmentNotification notification) {};
	
	
	
	
	// ----------------------------------------------------------------------
	// --- From here on some simple getter and setter methods are placed ----
	// ----------------------------------------------------------------------
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

	protected Hashtable<AID, Object> getAgentAnswers() {
		return agentAnswers;
	}
	protected void setAgentAnswers(Hashtable<AID, Object> agentAnswers) {
		this.agentAnswers = agentAnswers;
	}
	
	public Vector<EnvironmentNotification> getNotifications() {
		return notifications;
	}
	public void setNotifications(Vector<EnvironmentNotification> notifications) {
		this.notifications = notifications;
	}
	
}
