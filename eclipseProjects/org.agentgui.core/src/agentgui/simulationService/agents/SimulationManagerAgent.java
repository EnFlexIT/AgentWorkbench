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
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;

import java.util.Hashtable;
import java.util.Vector;

import agentgui.core.application.Application;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.project.Project;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.AbstractEnvironmentModel;
import agentgui.simulationService.environment.DisplaytEnvironmentModel;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.sensoring.ServiceActuatorManager;
import agentgui.simulationService.sensoring.ServiceSensorManager;
import agentgui.simulationService.time.TimeModel;
import agentgui.simulationService.transaction.DisplayAgentNotification;
import agentgui.simulationService.transaction.EnvironmentNotification;

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
	protected SimulationServiceHelper simHelper = null;
	/** The ServiceSensorManager for this agent. */
	protected ServiceSensorManager mySensor = null;
	
	/** The environment model which contains an abstract and a displayable environment model as well as a time model */
	protected EnvironmentModel myEnvironmentModel = new EnvironmentModel();
	
	/** The answers/next state of all involved agents. */
	protected Hashtable<AID, Object> agentAnswers = null;

	/** The CyclicSimulationBehavior. */
	private CyclicSimulationBehaviour simBehaviour = null;
	/** The CyclicNotificationHandler for incoming notification. */
	private CyclicNotificationHandler notifyHandler = null;
	/** The notifications, which arrived at this agent . */
	private Vector<EnvironmentNotification> notifications = new Vector<EnvironmentNotification>();
	
	

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
		
		// --- Get the initial EnvironmentModel from Agent.GUI setup ----------
		this.setEnvironmentModel(this.getEnvironmentModelFromSetup());

		this.sensorPlugIn();
		this.addNotificationHandler();
	}
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#takeDown()
	 */
	@Override
	protected void takeDown() {
		this.removeNotificationHandler();
		this.sensorPlugOut();
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
		this.removeBehaviour(this.simBehaviour);
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
			doSingleSimulationSequennce();
			block();
		}
	}
	
	
	
	/**
	 * This method is used for initialising the simulation during the .setup()-method of the agent.
	 * Here the environment model (see class agentgui.simulationService.environment.EnvironmentModel)
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
	public abstract void doSingleSimulationSequennce();
	
	/**
	 * This method has to be called if the next simulation step can be executed.
	 */
	public void doNextSimulationStep() {
		this.simBehaviour.restart();
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
			proceedAgentAnswers(aa);
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
		synchronized (this.notifications) {
			this.notifications.add(notification);	
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
		}
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
	
	/**
	 * Returns the Vector of current EnvironmentNotification's.
	 * @return the notifications
	 */
	public Vector<EnvironmentNotification> getNotifications() {
		return notifications;
	}
	/**
	 * Sets the notifications.
	 * @param notifications the new notifications
	 */
	public void setNotifications(Vector<EnvironmentNotification> notifications) {
		this.notifications = notifications;
	}

}
