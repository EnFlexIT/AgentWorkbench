package de.enflexit.awb.simulation.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;

import java.util.Hashtable;
import java.util.Vector;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.environment.EnvironmentController;
import de.enflexit.awb.core.project.Project;
import de.enflexit.awb.simulation.SimulationService;
import de.enflexit.awb.simulation.SimulationServiceHelper;
import de.enflexit.awb.simulation.environment.AbstractEnvironmentModel;
import de.enflexit.awb.simulation.environment.DisplaytEnvironmentModel;
import de.enflexit.awb.simulation.environment.EnvironmentModel;
import de.enflexit.awb.simulation.environment.time.TimeModel;
import de.enflexit.awb.simulation.sensoring.ServiceActuatorManager;
import de.enflexit.awb.simulation.sensoring.ServiceSensorManager;
import de.enflexit.awb.simulation.transaction.DisplayAgentNotification;
import de.enflexit.awb.simulation.transaction.EnvironmentNotification;

/** 
 * This prototype agent can be used in order to build a tailored manager for a simulation. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class SimulationManagerAgent extends Agent {
	
	private static final long serialVersionUID = -7398714332312572026L;

	/** The debug indicator. If true, some debug information will provided during the execution of some methods (e.g. {@link #getEnvironmentModelFromSetup()}). */
	protected boolean debug = false;
	
	/** The SimulationServiceHelper. */
	protected SimulationServiceHelper simHelper;
	/** The ServiceSensorManager for this agent. */
	protected ServiceSensorManager mySensor;
	
	/** The environment model which contains an abstract and a displayable environment model as well as a time model */
	protected EnvironmentModel myEnvironmentModel;
	
	/** The answers/next state of all involved agents. */
	protected Hashtable<AID, Object> agentAnswers;

	/** The CyclicSimulationBehavior. */
	private CyclicSimulationBehaviour simBehaviour;
	/** The CyclicNotificationHandler for incoming notification. */
	private CyclicNotificationHandler notifyHandler;
	/** The notifications, which arrived at this agent . */
	private Vector<EnvironmentNotification> notifications;
	
	/** Flag to indicate that the simulation manager is about to terminate */ 
	private boolean isDoTerminate;
	

	/**
	 * Mandatory setup()-functionality.
	 */
	@Override
	protected void setup() {		  

		// --- Get the helper for the SimulationService -----------------------
		try {
		  this.simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
		  this.simHelper.setManagerAgent(this.getAID());
		  
		} catch (ServiceException se) {
			  se.printStackTrace();
			  this.doDelete();
			  return;
		}
		
		// --- Get the initial EnvironmentModel from the AWB setup ------------
		this.setEnvironmentModel(this.getEnvironmentModelFromSetup());

		this.sensorPlugIn();
		this.addNotificationHandler();
	}
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#doDelete()
	 */
	@Override
	public void doDelete() {
		this.isDoTerminate = true;
		this.removeSimulationBehaviour();
		this.removeNotificationHandler();
		this.sensorPlugOut();
		super.doDelete();
	}
	
	/**
	 * This Method plugs IN the service sensor.
	 */
	protected void sensorPlugIn() {
		// --- Start the ServiceSensor ------------------------------
		this.mySensor = new ServiceSensorManager(this);
		// --- Register the sensor to the SimulationService ---------
		try {
			this.simHelper.sensorPlugIn4Manager(this.mySensor);	
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This Method plugs OUT the service sensor.
	 */
	protected void sensorPlugOut() {
		// --- plug-out the Sensor ----------------------------------
		try {
			this.simHelper.sensorPlugOut4Manager(this.mySensor);	
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		this.mySensor = null;
	}
	
	/**
	 * This method adds the mandatory CyclicSimulationBehavior to this agent.
	 */
	protected void addSimulationBehaviour() {
		// --- Start the cyclic SimulationBehavior of this manager --
		if (this.simBehaviour==null) {
			this.simBehaviour = new CyclicSimulationBehaviour(this);	
		}		
		this.addBehaviour(this.simBehaviour);
	}
	
	/**
	 * Removes the simulation behaviour.
	 */
	protected void removeSimulationBehaviour() {
		// --- Remove the cyclic SimulationBehavior of this manager --
		if (this.simBehaviour!=null) {
			this.removeBehaviour(this.simBehaviour);
			this.simBehaviour = null;
		}
	}
	
	
	/**
	 * This method adds the core behaviour to the agent which is controlling the 
	 * sequence (cyclic) simulation behaviour
	 *  
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
	private class CyclicSimulationBehaviour extends CyclicBehaviour {
		private static final long serialVersionUID = 7456541169963374884L;
		
		/**
		 * Instantiates a new cyclic simulation behaviour.
		 * @param simulationManagerAgent 
		 */
		public CyclicSimulationBehaviour(SimulationManagerAgent simulationManagerAgent) {
			super(simulationManagerAgent);
		}
		
		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action() {
			SimulationManagerAgent.this.doSingleSimulationSequence();
			this.block();
		}
	}
	
	
	
	/**
	 * This method is used for initializing the simulation during the .setup()-method of the agent.
	 * Here the environment model (see class de.enflexit.awb.simulation.environment.EnvironmentModel)
	 * will be set out of the current Setup.
	 * In case that you want to fill the {@link EnvironmentModel} with your own data, just overwrite this method. 
	 *
	 * @return a copy of the {@link EnvironmentModel}, configured in the setup
	 */
	public EnvironmentModel getEnvironmentModelFromSetup() {
		
		EnvironmentModel envModel = null;
		// --- Try to get the environment model from the project setup -------- 
		try {
			envModel = this.simHelper.getEnvironmentModelFromSetup();
			if (this.debug==true) {
				if (envModel==null) {
					System.err.println(this.getLocalName() + ": Didn't get environment model from SimulationService! - Try local access ...");
				} else {
					System.out.println(this.getLocalName() + ": Got environment model from SimulationService!");
				}	
			}
			
		} catch (ServiceException se) {
			se.printStackTrace();
		}
		
		// --- Backup solution by directly accessing Application --------------
		if (envModel==null) {
			Project currProject = Application.getProjectFocused();
			if (currProject!=null) {
				// --- Get the environment model from the controller ----------
				EnvironmentController envController = currProject.getEnvironmentController();
				if (envController!=null) {
					EnvironmentModel envModelTmp = envController.getEnvironmentModel();
					if (envModelTmp!=null) {
						envModel = envModelTmp.getCopy();
					}
				} else {
					// --- Could not find environment controller --------------
					if (this.debug==true) {
						System.err.println(this.getLocalName() + ": No EnvironmentController coud be found for current project!");	
					}	
				}
			} else {
				// --- No current open project --------------------------------
				if (this.debug==true) {
					System.err.println(this.getLocalName() + ": Didn't get Project instance from Application!");	
				}
			}
		}
		return envModel;
	}
	
	/**
	 * The logic of the simulation is implemented here. It's highly recommended 
	 * to use this methods for implementing the individual logic.
	 */
	public abstract void doSingleSimulationSequence();
	
	/**
	 * This method has to be called if the next simulation step can be executed.
	 */
	public void doNextSimulationStep() {
		if (this.simBehaviour!=null && this.isDoTerminate==false) {
			this.simBehaviour.restart();
		}
	}
	
	/**
	 * Steps the simulation. As a side effect a transition of current environment is written into the transaction list
	 *
	 * @param answersExpected the expected number answers from the involved {@link SimulationAgent}'s 
	 * @throws ServiceException the ServiceException
	 */
	public void stepSimulation(int answersExpected) throws ServiceException {
		this.simHelper.stepSimulation(this.getEnvironmentModel(), answersExpected);
	}
	
	/**
	 * Steps the simulation. As a side effect a transition of current environment is written into the transaction list
	 *
	 * @param environmentModel the new {@link EnvironmentModel} to set
	 * @param answersExpected the expected number answers from the involved {@link SimulationAgent}'s
	 * @throws ServiceException the ServiceException
	 */
	public void stepSimulation(EnvironmentModel environmentModel, int answersExpected) throws ServiceException {
		this.setEnvironmentModel(environmentModel);
		this.simHelper.stepSimulation(this.getEnvironmentModel(), answersExpected);
	}

	/**
	 * Resets the answers of the agents in the simulation service to an empty Hashtable.
	 * @throws ServiceException ServiceException
	 */
	public void resetEnvironmentInstanceNextParts() throws ServiceException {
		this.simHelper.resetEnvironmentInstanceNextParts();
	}
	
	/**
	 * This method will be used by the {@link ServiceActuatorManager} to inform
	 * this manager about agent answers for environment changes. It can be either 
	 * used to do this asynchronously or synchronously. It is highly recommended 
	 * to do this asynchronously, so that the agency can act parallel and not
	 * sequentially.
	 *
	 * @param agentAnswers the agent answers as a Hashtable
	 * @param aSynchron true, if this should be done asynchronously
	 */
	public void putAgentAnswers(Hashtable<AID, Object> agentAnswers, boolean aSynchron) {
		
		if (this.isDoTerminate==true) return;
				
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
	 * 
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
	private class AgentAnswerStimulus extends OneShotBehaviour {
		
		private static final long serialVersionUID = 1441989543791055996L;
		
		private Hashtable<AID, Object> aa = null;
		
		/**
		 * Instantiates a new agent answer stimulus.
		 * @param answersHash the answers hash
		 */
		public AgentAnswerStimulus(Hashtable<AID, Object> answersHash) {
			aa = answersHash;
		}
		
		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action() {
			if (SimulationManagerAgent.this.isDoTerminate==false) {
				proceedAgentAnswers(aa);
			}
		}
	}
	
	/**
	 * This method is called if a stimulus from the outside reached this agent.
	 * It can be overwritten in the child class to act on the agent answers
	 * in order to build the next EnvironmentModel.
	 *
	 * @param agentAnswers the agent answers as a Hashtable
	 */
	protected void proceedAgentAnswers(Hashtable<AID, Object> agentAnswers) {}
	
	/**
	 * Returns the current state of agent answers from the simulation service.
	 *
	 * @param isFromMainOnly the indicator to collect from the main container only, otherwise all container involved will be requested
	 * @return the current state of agent answers
	 */
	public Hashtable<String, Hashtable<AID, Object>> getEnvironmentInstanceNextParts() {
		try {
			return this.simHelper.getEnvironmentInstanceNextParts();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Returns the current state of agent answers from the simulation service.
	 *
	 * @param isFromMainOnly the indicator to collect from the main container only, otherwise all container involved will be requested
	 * @return the current state of agent answers
	 */
	public Hashtable<AID, Object> getEnvironmentInstanceNextPartsFromMain() {
		try {
			return this.simHelper.getEnvironmentInstanceNextPartsFromMain();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * This method can be used to transfer any kind of information to one member of the current environment model.
	 *
	 * @param receiverAID the AID of the receiver
	 * @param notification the notification
	 * @return true, if successful
	 */
	public boolean sendAgentNotification(AID receiverAID, Object notification) {
		boolean send = false;
		EnvironmentNotification myNotification = new EnvironmentNotification(this.getAID(), true, notification);
		try {
			send = this.simHelper.notifySensorAgent(receiverAID, myNotification);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return send;
	}
	
	/**
	 * Notify all AbstractDisplayAgents about environment changes by using the SimulationService.
	 */
	public void sendDisplayAgentNotificationUpdateEnvironmentModel() {
		try {
			this.simHelper.displayAgentSetEnvironmentModel(this.getEnvironmentModel());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Notify display agents about environment changes with concrete messages.
	 * @param displayAgentNotification the display agent message
	 */
	public void sendDisplayAgentNotification(DisplayAgentNotification displayAgentNotification) {
		try {
			EnvironmentNotification notification = new EnvironmentNotification(getAID(), true, displayAgentNotification);
			this.simHelper.displayAgentNotification(notification);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * This method can be invoked from the simulation service, if
	 * a notification for the manager has to be delivered.
	 *
	 * @param notification the new EnvironmentNotification for this manager agent
	 */
	public void setManagerNotification(EnvironmentNotification notification) {
		// --- place the notification into the notification vector -------
		synchronized (this.getNotifications()) {
			this.getNotifications().add(notification);	
		}
		// --- restart the CyclicNotificationHandler ---------------------
		this.notifyHandler.restart();	
			
	}
	/**
	 * This method adds the CyclicNotificationHandler to this agent.
	 */
	private void addNotificationHandler() {
		if (this.notifyHandler==null) {
			this.notifyHandler = new CyclicNotificationHandler();	
		}		
		this.addBehaviour(this.notifyHandler);
	}
	/**
	 * This method removes the CyclicNotificationHandler from this agent.
	 */
	private void removeNotificationHandler() {
		if (this.notifyHandler!=null) {
			this.removeBehaviour(this.notifyHandler);
			this.notifyHandler = null;
			this.getNotifications().clear();
		}
	}

	/**
	 * Returns the Vector of current EnvironmentNotification's.
	 * @return the notifications
	 */
	public Vector<EnvironmentNotification> getNotifications() {
		if (notifications==null) {
			notifications = new Vector<EnvironmentNotification>();
		}
		return notifications;
	}
	
	/**
	 * Extracts and returns a Vector of notifications that match the specified content type class.
	 *
	 * @param contentTypeClass the content type class
	 * @return the vector of notifications found
	 */
	protected Vector<EnvironmentNotification> getNotificationsByConentType(Class<?> contentTypeClass) {
		
		Vector<EnvironmentNotification> contentEnvNotifications = new Vector<>();
		synchronized (this.getNotifications()) {
			// --- Find the corresponding notifications -------------
			for (EnvironmentNotification envNote : this.getNotifications()) {
				if (envNote.getNotification().getClass().equals(contentTypeClass)==true) {
					contentEnvNotifications.add(envNote);
				}
			}
			// --- Remove from notifications ------------------------
			this.getNotifications().removeAll(contentEnvNotifications);
		}
		return contentEnvNotifications;
	}
	
	
	/**
	 * This CyclicBehaviour is used in order to act on the incoming notifications.
	 *  
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
	 */
	private class CyclicNotificationHandler extends CyclicBehaviour {
		private static final long serialVersionUID = 4638681927192305608L;
		
		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action() {
			
			boolean removeFirstElement = false;
					
			// --- Get the first element and work on it -----------------------
			EnvironmentNotification notification = null;
			if (SimulationManagerAgent.this.isDoTerminate==false && SimulationManagerAgent.this.getNotifications().size()!=0) {
				notification = SimulationManagerAgent.this.getNotifications().get(0);
				if (notification==null) {
					SimulationManagerAgent.this.getNotifications().remove(0);
					return;
				}
				SimulationManagerAgent.this.onManagerNotification(notification);
				removeFirstElement = true;				
			}
			
			if (SimulationManagerAgent.this.isDoTerminate==false) {
				// --- Regular operating case ---------------------------------
				// --- => Remove this element and control the notifications ---
				synchronized (SimulationManagerAgent.this.getNotifications()) {
					if (removeFirstElement==true && SimulationManagerAgent.this.getNotifications().size()>0) {
						SimulationManagerAgent.this.getNotifications().remove(notification);
					}
					if (SimulationManagerAgent.this.getNotifications().size()==0) {
						this.block();
					}
				}
			} else {
				// --- termination was called ---------------------------------
				// --- => Simply block this behaviour -------------------------
				this.block();
			}
		}
	}
	
	
	/**
	 * This method will be executed if a ManagerNotification arrives this agent.
	 *
	 * @param notification the EnvironmentNotification for this agent
	 */
	protected void onManagerNotification(EnvironmentNotification notification) {};
	
	
	/**
	 * Notifies the manager to pause or restart the simulation.
	 * @param isPauseSimulation the is pause simulation
	 */
	public abstract void setPauseSimulation(boolean isPauseSimulation);
	
	
	// ----------------------------------------------------------------------
	// --- From here on some simple getter and setter methods are placed ----
	// ----------------------------------------------------------------------
	/**
	 * Provides the environment model.
	 * @return the environment model
	 */
	public EnvironmentModel getEnvironmentModel() {
		return myEnvironmentModel;
	}
	/**
	 * Sets the environment model.
	 * @param environmentModel the new environment model
	 */
	public void setEnvironmentModel(EnvironmentModel environmentModel) {
		this.myEnvironmentModel = environmentModel;
	}
	
	/**
	 * Provides the current time model.
	 * @return the time model
	 */
	public TimeModel getTimeModel() {
		return this.getEnvironmentModel().getTimeModel();
	}
	/**
	 * Sets the current time model.
	 * @param timeModel the new time model
	 */
	public void setTimeModel(TimeModel timeModel) {
		this.getEnvironmentModel().setTimeModel(timeModel);
	}
	
	/**
	 * Returns the current abstract environment model.
	 * @return the abstract environment
	 */
	public AbstractEnvironmentModel getAbstractEnvironment() {
		return this.getEnvironmentModel().getAbstractEnvironment();
	}
	/**
	 * Sets the abstract environment.
	 * @param abstractEnvironment the new abstract environment
	 */
	public void setAbstractEnvironment(AbstractEnvironmentModel abstractEnvironment) {
		this.getEnvironmentModel().setAbstractEnvironment(abstractEnvironment);
	}

	/**
	 * Returns the current display environment.
	 * @return the display environment
	 */
	public DisplaytEnvironmentModel getDisplayEnvironment() {
		return this.getEnvironmentModel().getDisplayEnvironment();
	}
	/**
	 * Sets the display environment.
	 * @param displayEnvironment the new display environment
	 */
	public void setDisplayEnvironment(DisplaytEnvironmentModel displayEnvironment) {
		this.getEnvironmentModel().setDisplayEnvironment(displayEnvironment);
	}

	/**
	 * Gets the agent answers.
	 * @return the agent answers
	 */
	public Hashtable<AID, Object> getAgentAnswers() {
		return agentAnswers;
	}
	/**
	 * Sets the agent answers.
	 * @param agentAnswers the agent answers
	 */
	public void setAgentAnswers(Hashtable<AID, Object> agentAnswers) {
		this.agentAnswers = agentAnswers;
	}
	
}
