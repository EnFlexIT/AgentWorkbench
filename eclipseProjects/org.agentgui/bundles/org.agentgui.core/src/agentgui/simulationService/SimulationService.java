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
package agentgui.simulationService;

import jade.core.AID;
import jade.core.Agent;
import jade.core.AgentContainer;
import jade.core.BaseService;
import jade.core.Filter;
import jade.core.HorizontalCommand;
import jade.core.IMTPException;
import jade.core.MainContainer;
import jade.core.Node;
import jade.core.Profile;
import jade.core.ProfileException;
import jade.core.Service;
import jade.core.ServiceException;
import jade.core.ServiceHelper;
import jade.core.VerticalCommand;
import jade.core.mobility.AgentMobilityHelper;
import jade.util.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;

import agentgui.core.application.Application;
import agentgui.core.environment.EnvironmentController;
import agentgui.core.project.Project;
import agentgui.simulationService.agents.AbstractDisplayAgent;
import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.agents.SimulationManagerAgent;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;
import agentgui.simulationService.sensoring.ServiceActuator;
import agentgui.simulationService.sensoring.ServiceActuatorManager;
import agentgui.simulationService.sensoring.ServiceSensor;
import agentgui.simulationService.sensoring.ServiceSensorManager;
import agentgui.simulationService.transaction.EnvironmentManagerDescription;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * This is the SimulationService, which provides a list of functionalities for agent based simulations.
 * These are namely:<br>
 * - time synchronization for all involved (remote) container,<br>
 * - methods to stop agents, that are using the Actuator/Sensor relationship between agents and this service,<br>
 * - a method to pause a running simulation, <br>
 * - methods to transport an {@link EnvironmentModel} model to all connected agents in an asynchronous way and<br>
 * - methods to receive changes or notifications from single {@link SimulationAgent}<br>
 * 
 * @see SimulationServiceHelper
 * @see SimulationServiceImpl
 * @see SimulationServiceProxy
 * 
 * @see ServiceActuator
 * @see ServiceSensor
 * @see ServiceActuatorManager
 * @see ServiceSensorManager
 * 
 * @see SimulationAgent
 * @see SimulationManagerAgent
 * 
 * @see EnvironmentModel
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SimulationService extends BaseService {

	public static final String NAME = SimulationServiceHelper.SERVICE_NAME;
	public static final String SERVICE_NODE_DESCRIPTION_FILE = SimulationServiceHelper.SERVICE_NODE_DESCRIPTION_FILE;
	public static final String AID_PROPERTY_CLASSNAME = "_AgentClassName";
	
	
	private AgentContainer myContainer;
	private MainContainer myMainContainer;

	private Filter incFilter;
	private Filter outFilter;
	private ServiceComponent localSlice;
	
	/** --- Variables for the Time-Synchronisation ----------------------------- */
	private long timeMeasureNext = 0;				// --- When was the MainContainerTime last measured 
	private long timeMeasureInterval = 1000*60; 	// --- Measure every 60 seconds 
	private int timeMeasureCountMax = 100;			// --- How often the time-difference should be measured to build an average value?
	private long timeDiff2MainContainer = 0;		// --- Difference between this and the MainContainer-Time
	
	/** --- The Agent who is the Manager / Controller of the Simulation -------- */
	private EnvironmentManagerDescription environmentManagerDescription;
	/** --- Agents which have registered as display agents for the environment - */
	private Vector<AbstractDisplayAgent> displayAgents;
	private HashMap<String, Integer> displayAgentDistribution;
	
	/** --- The List of Agents, which are registered to this service ----------- */ 
	private Hashtable<String, AID> agentList;
	
	/** --- The current EnvironmentModel --------------------------------------- */
	private EnvironmentModel environmentModel;
	/** --- The Actuator for this Service, which can inform registered  
	    	Agents about changes in the Simulation e.g. 'stepTimeModel' -------- */
	private ServiceActuator localServiceActuator;
	private ServiceActuatorManager localServiceActuator4Manager;
	
	/** --- How should an Agent be notified about Environment-Changes? --------- */
	private boolean stepSimulationAsynchronous = true;

	/** --- The next EnvironmentObject-Instance in parts (answers of agents) --- */
	private int environmentInstanceNextPartsExpected;
	private Hashtable<AID, Object> environmentInstanceNextParts;
	private Hashtable<AID, Object> environmentInstanceNextPartsLocal;
	
		
	/* (non-Javadoc)
	 * @see jade.core.BaseService#init(jade.core.AgentContainer, jade.core.Profile)
	 */
	public void init(AgentContainer ac, Profile p) throws ProfileException {
		
		super.init(ac, p);
		
		this.myContainer = ac;
		this.myMainContainer = ac.getMain();	
		// --- Initialise local attributes ----------------  
		this.displayAgents = new Vector<AbstractDisplayAgent>();
		this.displayAgentDistribution = new HashMap<String, Integer>();
		this.agentList = new Hashtable<String, AID>();
		this.localServiceActuator4Manager = new ServiceActuatorManager();
		this.environmentInstanceNextParts = new Hashtable<AID, Object>();
		this.environmentInstanceNextPartsLocal = new Hashtable<AID, Object>();
		// --- Create filters -----------------------------
		this.outFilter = new CommandOutgoingFilter();
		this.incFilter = new CommandIncomingFilter();
		// --- Create local slice -------------------------
		this.localSlice = new ServiceComponent();
		
		if (this.myMainContainer!=null && myLogger.isLoggable(Logger.FINE)) {
			// --- Is !=null, if the Service will start at the Main-Container !!! ----
			this.myLogger.log(Logger.FINE, "Main-Container: " + myMainContainer.toString());
		}
		if (this.myContainer!=null && myLogger.isLoggable(Logger.FINE)) {
			this.myLogger.log(Logger.FINE, "Starting SimulationService: My-Container: " + myContainer.toString());
		}

		// --- Reduce the logging level for Messaging -----
		Logger.getMyLogger("jade.core.messaging.Messaging").setLevel(Level.WARNING);
		Logger.getMyLogger("jade.core.messaging.MessageManager").setLevel(Level.WARNING);
		
	}
	/* (non-Javadoc)
	 * @see jade.core.BaseService#boot(jade.core.Profile)
	 */
	@Override
	public void boot(Profile p) throws ServiceException {
		this.getServiceActuator();
	}
	/* (non-Javadoc)
	 * @see jade.core.BaseService#shutdown()
	 */
	@Override
	public void shutdown() {
		this.displayAgents = null;
		this.displayAgentDistribution = null;
		this.agentList = null;
		this.getServiceActuator().shutdown();
		this.getServiceActuator().interrupt();
		this.setServiceActuator(null);
		this.localServiceActuator4Manager = null;
		this.environmentInstanceNextParts = null;
		this.environmentInstanceNextPartsLocal = null;
		this.environmentModel = null;
	}
	/* (non-Javadoc)
	 * @see jade.core.Service#getName()
	 */
	public String getName() {
		return NAME;
	}
	/* (non-Javadoc)
	 * @see jade.core.BaseService#getHelper(jade.core.Agent)
	 */
	public ServiceHelper getHelper(Agent ag) {
		return new SimulationServiceImpl();
	}
	/* (non-Javadoc)
	 * @see jade.core.BaseService#getCommandFilter(boolean)
	 */
	public Filter getCommandFilter(boolean direction) {
		if(direction == Filter.INCOMING) {
			return incFilter;
		}
		else {
			return outFilter;
		}
	}
	/* (non-Javadoc)
	 * @see jade.core.BaseService#getHorizontalInterface()
	 */
	public Class<?> getHorizontalInterface() {
		return SimulationServiceSlice.class;
	}
	/* (non-Javadoc)
	 * @see jade.core.BaseService#getLocalSlice()
	 */
	public Service.Slice getLocalSlice() {
		return localSlice;
	}
	/**
	 * Rerturns the {@link ServiceActuator} for the local container.
	 * @return the service actuator
	 */
	private ServiceActuator getServiceActuator() {
		if (localServiceActuator==null) {
			localServiceActuator = new ServiceActuator(this.myContainer.getNodeDescriptor().getName());
		}
		return localServiceActuator;
	}
	/**
	 * Sets the service actuator.
	 * @param newServiceActuator the new service actuator
	 */
	private void setServiceActuator(ServiceActuator newServiceActuator) {
		this.localServiceActuator = newServiceActuator;
	}
	
	// --------------------------------------------------------------	
	// ---- Inner-Class 'AgentTimeImpl' ---- Start ------------------
	// --------------------------------------------------------------
	/**
	 * Sub-Class to provide interaction between Agents and this Service.
	 *
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
	 */
	public class SimulationServiceImpl implements SimulationServiceHelper {

		/* (non-Javadoc)
		 * @see jade.core.ServiceHelper#init(jade.core.Agent)
		 */
		public void init(Agent ag) {
			// --- Store the Agent in the agentList -----------------
			agentList.put(ag.getName(), ag.getAID());			
		}
				
		// ----------------------------------------------------------
		// --- Methods for the synchronised time --------------------
		// ----------------------------------------------------------
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#getSynchTimeDifferenceMillis()
		 */
		public long getSynchTimeDifferenceMillis() throws ServiceException {
			return timeDiff2MainContainer;
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#getSynchTimeMillis()
		 */
		public long getSynchTimeMillis() throws ServiceException {
			if (myMainContainer==null) {
				setMainContainerTimeLocally();	
			}			
			return System.currentTimeMillis() + timeDiff2MainContainer;
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#getSynchTimeDate()
		 */
		public Date getSynchTimeDate() throws ServiceException {
			return new Date(this.getSynchTimeMillis());
		}
		
		// ----------------------------------------------------------
		// --- Methods to work on agents ----------------------------
		// ----------------------------------------------------------
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#setAgentMigration(java.util.Vector)
		 */
		public void setAgentMigration(Vector<AID_Container> transferAgents) throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastAgentMigration(transferAgents, slices);
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#stopSimulationAgents()
		 */
		public void stopSimulationAgents() throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastStopSimulationAgents(slices);
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#setPauseSimulation(boolean)
		 */
		public void setPauseSimulation(boolean pause) throws ServiceException {
			// --- block or unblock the simulation ------------
			Service.Slice[] slices = getAllSlices();
			broadcastPauseSimulation(pause, slices);
		}
		
		// ----------------------------------------------------------
		// --- Methods for the Manager-Agent ------------------------
		// ----------------------------------------------------------
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#setManagerAgent(jade.core.AID)
		 */
		public void setManagerAgent(AID agentAddress) throws ServiceException {
			EnvironmentManagerDescription envManager = new EnvironmentManagerDescription(agentAddress, myContainer.here());
			Service.Slice[] slices = getAllSlices();
			broadcastManagerAgent(envManager, slices);
		}		
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#getManagerAgent()
		 */
		public AID getManagerAgent() throws ServiceException {
			if (environmentManagerDescription==null) {
				return null;
			} else {
				return environmentManagerDescription.getAID();	
			}
		}
		
		// ----------------------------------------------------------
		// --- Register, unregister or notify Agents-Sensors --------
		// ----------------------------------------------------------
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#sensorPlugIn(agentgui.simulationService.sensoring.ServiceSensor)
		 */
		public void sensorPlugIn(ServiceSensor sensor) throws ServiceException {
			getServiceActuator().plugIn(sensor);
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#sensorPlugIn(agentgui.simulationService.sensoring.ServiceSensor, boolean)
		 */
		public void sensorPlugIn(ServiceSensor sensor, boolean pasive) throws ServiceException {
			getServiceActuator().plugInPassive(sensor);
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#sensorPlugOut(agentgui.simulationService.sensoring.ServiceSensor)
		 */
		public void sensorPlugOut(ServiceSensor sensor) throws ServiceException {
			getServiceActuator().plugOut(sensor);	
		}		
		
		// ----------------------------------------------------------
		// --- Register, unregister or notify Manager-Sensors -------
		// ----------------------------------------------------------
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#sensorPlugIn4Manager(agentgui.simulationService.sensoring.ServiceSensorManager)
		 */
		public void sensorPlugIn4Manager(ServiceSensorManager sensor) throws ServiceException {
			localServiceActuator4Manager.plugIn(sensor);
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#sensorPlugOut4Manager(agentgui.simulationService.sensoring.ServiceSensorManager)
		 */
		public void sensorPlugOut4Manager(ServiceSensorManager sensor) throws ServiceException {
			localServiceActuator4Manager.plugOut(sensor);	
		}		

		// ----------------------------------------------------------
		// --- Methods for the Simulation ---------------------------
		// ----------------------------------------------------------
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#getStepSimulationAsynchronous()
		 */
		public boolean getStepSimulationAsynchronous() throws ServiceException {
			return stepSimulationAsynchronous;
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#setStepSimulationAsynchronous(boolean)
		 */
		public void setStepSimulationAsynchronous(boolean stepAsynchronous) throws ServiceException {
			stepSimulationAsynchronous = stepAsynchronous;
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#stepSimulation(agentgui.simulationService.environment.EnvironmentModel, int)
		 */
		public void stepSimulation(EnvironmentModel envModel, int answersExpected) throws ServiceException {
			this.stepSimulation(envModel, answersExpected, stepSimulationAsynchronous);
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#stepSimulation(agentgui.simulationService.environment.EnvironmentModel, int, boolean)
		 */
		public void stepSimulation(EnvironmentModel envModel, int answersExpected, boolean aSynchron) throws ServiceException {
			
			// --- Next step for the simulation -----------
			Service.Slice[] slices = getAllSlices();
			if (answersExpected!=environmentInstanceNextPartsExpected) {
				broadcastAnswersExpected(answersExpected, slices);	
			}
			broadcastStepSimulation(envModel, aSynchron, slices);
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#notifySensorAgent(jade.core.AID, agentgui.simulationService.transaction.EnvironmentNotification)
		 */
		public boolean notifySensorAgent(AID agentAID, EnvironmentNotification notification) throws ServiceException {
			return broadcastNotifyAgent(agentAID, notification);	
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#notifyManagerAgent(agentgui.simulationService.transaction.EnvironmentNotification)
		 */
		public boolean notifyManagerAgent(EnvironmentNotification notification) throws ServiceException {
			return broadcastNotifyManager(notification);	
		}
		
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#getEnvironmentModelFromSetup()
		 */
		@Override
		public EnvironmentModel getEnvironmentModelFromSetup() throws ServiceException {
			return mainGetEnvironmentModelFromSetup();
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#getEnvironmentModel()
		 */
		public EnvironmentModel getEnvironmentModel() throws ServiceException {
			return environmentModel;
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#setEnvironmentModel(agentgui.simulationService.environment.EnvironmentModel)
		 */
		public void setEnvironmentModel(EnvironmentModel envModel) throws ServiceException {
			this.setEnvironmentModel(envModel, false);
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#setEnvironmentModel(agentgui.simulationService.environment.EnvironmentModel, boolean)
		 */
		public void setEnvironmentModel(EnvironmentModel envModel, boolean notifySensorAgents) throws ServiceException {
			broadcastSetEnvironmentModel(envModel, notifySensorAgents, getAllSlices());
		}
		
		// ----------------------------------------------------------
		// --- EnvironmentModel of the next simulation step ---------
		// ----------------------------------------------------------
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#setEnvironmentInstanceNextPart(jade.core.AID, java.lang.Object)
		 */
		public void setEnvironmentInstanceNextPart(AID fromAgent, Object nextPart) throws ServiceException {

			synchronized (environmentInstanceNextPartsLocal) {
				// --- Put single changes into the local store until ---- 
				// --- the expected number of answers is not reached ----
				if (nextPart==null) {
					environmentInstanceNextPartsLocal.put(fromAgent, "null");
				} else {
					environmentInstanceNextPartsLocal.put(fromAgent, nextPart);
				}
				
				// --- If the expected number of answers came back to ---
				// --- the service, broadcast it to every other node ----
				//System.out.println(fromAgent.getLocalName() + ": " + environmentInstanceNextPartsLocal.size() + " - " + getServiceActuator().getNoOfSimulationAnswersExpected());
				if (environmentInstanceNextPartsLocal.size() >= getServiceActuator().getNoOfSimulationAnswersExpected()) {
					mainSetEnvironmentInstanceNextPart(environmentInstanceNextPartsLocal);	
					environmentInstanceNextPartsLocal = new Hashtable<AID, Object>();
				}
			}
			
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#getEnvironmentInstanceNextParts()
		 */
		public Hashtable<AID, Object> getEnvironmentInstanceNextParts() throws ServiceException {
			return mainGetEnvironmentInstanceNextParts();
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#resetEnvironmentInstanceNextParts()
		 */
		public void resetEnvironmentInstanceNextParts() throws ServiceException {
			mainResetEnvironmentInstanceNextParts();
		}
		
		// ----------------------------------------------------------
		// --- Methods for the Display Agents -----------------------
		// ----------------------------------------------------------
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#displayAgentRegister(agentgui.simulationService.agents.AbstractDisplayAgent)
		 */
		@Override
		public void displayAgentRegister(AbstractDisplayAgent displayAgent) throws ServiceException {
			synchronized (displayAgents) {
				displayAgents.add(displayAgent);
				broadcastDisplayAgentContainerUnRegistration(true);
			}
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#displayAgentUnregister(jade.core.AID)
		 */
		@Override
		public void displayAgentUnregister(AbstractDisplayAgent displayAgent) throws ServiceException {
			synchronized (displayAgents) {
				displayAgents.remove(displayAgent);
				broadcastDisplayAgentContainerUnRegistration(false);
			}
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#displayAgentSetEnvironmentModel(agentgui.simulationService.environment.EnvironmentModel)
		 */
		@Override
		public void displayAgentSetEnvironmentModel(EnvironmentModel envModel) throws ServiceException {
			broadcastDisplayAgentSetEnvironmentModel(envModel);
		}
		/* (non-Javadoc)
		 * @see agentgui.simulationService.SimulationServiceHelper#displayAgentNotification(agentgui.simulationService.transaction.EnvironmentNotification)
		 */
		@Override
		public void displayAgentNotification(EnvironmentNotification notification) throws ServiceException {
			broadcastDisplayAgentNotification(notification);
		}

	}
	// --------------------------------------------------------------	
	// ---- Inner-Class 'AgentTimeImpl' ---- End --------------------
	// --------------------------------------------------------------

	
	
	/**
	 * Broadcast the EnvironmentManagerDescription of the Simulation-Manager Agent to all Slices.
	 *
	 * @param envManager EnvironmentManagerDescription
	 * @param slices the array of slices
	 * @throws ServiceException the service exception
	 * @see EnvironmentManagerDescription
	 */
	private void broadcastManagerAgent(EnvironmentManagerDescription envManager, Service.Slice[] slices) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending current TimeModel!");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Sending current TimeModel to " + sliceName);
				}
				slice.setManagerAgent(envManager);
				
			} catch(Throwable t) {
				myLogger.log(Logger.WARNING, "Error propagating current TimeModel to slice  " + sliceName, t);
			}
		}
	}
	
	/**
	 * Sends the local next parts of the environment-model to the Main-Container.
	 *
	 * @param nextPartsLocal the Hashtable of local environment changes, coming from different agents
	 * @throws ServiceException the service exception
	 */
	private void mainSetEnvironmentInstanceNextPart(Hashtable<AID, Object> nextPartsLocal) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending agent-answer of environment-change to Main-Container!");
		}
		String sliceName = null;
		try {
			SimulationServiceSlice slice = (SimulationServiceSlice) getSlice(MAIN_SLICE);
			sliceName = slice.getNode().getName();
			if (myLogger.isLoggable(Logger.FINER)) {
				myLogger.log(Logger.FINER, "Sending agent-answer of environment-change to " + sliceName);
			}
			slice.setEnvironmentInstanceNextPart(nextPartsLocal);
			
		} catch(Throwable t) {
			myLogger.log(Logger.WARNING, "Error while sending agent-answer of environment-change to slice  " + sliceName, t);
		}
	}	
	
	/**
	 * This method returns the complete environment-model-changes from the Main-Container.
	 *
	 * @return the hashtable
	 * @throws ServiceException the service exception
	 */
	private Hashtable<AID, Object> mainGetEnvironmentInstanceNextParts() throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Try to get new environment-part from Main-Container!");
		}
		String sliceName = null;
		try {
			SimulationServiceSlice slice = (SimulationServiceSlice) getSlice(MAIN_SLICE);
			sliceName = slice.getNode().getName();
			if (myLogger.isLoggable(Logger.FINER)) {
				myLogger.log(Logger.FINER, "Try to get new environment-parts from " + sliceName);
			}
			return slice.getEnvironmentInstanceNextParts();
			
		} catch(Throwable t) {
			myLogger.log(Logger.WARNING, "Error while trying to get new environment-parts from slice  " + sliceName, t);
		}
		return null;
	}	
	
	/**
	 * This method resets the hash with the single environment-model-changes.
	 *
	 * @throws ServiceException the service exception
	 */
	private void mainResetEnvironmentInstanceNextParts() throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending reset of environment-change-hash to Main-Container!");
		}
		String sliceName = null;
		try {
			SimulationServiceSlice slice = (SimulationServiceSlice) getSlice(MAIN_SLICE);
			sliceName = slice.getNode().getName();
			if (myLogger.isLoggable(Logger.FINER)) {
				myLogger.log(Logger.FINER, "Sending reset of environment-change-hash to " + sliceName);
			}
			slice.resetEnvironmentInstanceNextParts();
			
		} catch(Throwable t) {
			myLogger.log(Logger.WARNING, "Error while sending reset of environment-change-hash to slice  " + sliceName, t);
		}
	}	
	
	/**
	 * Returns the {@link EnvironmentModel}, configured in the setup of the end user application.
	 *
	 * @throws ServiceException the service exception
	 */
	private EnvironmentModel mainGetEnvironmentModelFromSetup() throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending request for environment model to Main-Container!");
		}
		String sliceName = null;
		try {
			SimulationServiceSlice slice = (SimulationServiceSlice) getSlice(MAIN_SLICE);
			sliceName = slice.getNode().getName();
			if (myLogger.isLoggable(Logger.FINER)) {
				myLogger.log(Logger.FINER, "Sending request for environment model to " + sliceName);
			}
			return slice.getEnvironmentModelFromSetup();
			
		} catch(Throwable t) {
			myLogger.log(Logger.WARNING, "Error while sending request for environment model  to slice  " + sliceName, t);
		}
		return null;
	}
	/**
	 * Broadcasts the current EnvironmentModel to all slices.
	 *
	 * @param envModel the EnvironmentModel
	 * @param notifySensorAgents true, if the sensor agents should be also notified 
	 * @param slices the slices
	 * @throws ServiceException the service exception
	 * @see EnvironmentModel
	 */
	private void broadcastSetEnvironmentModel(EnvironmentModel envModel, boolean notifySensorAgents, Service.Slice[] slices) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending new EnvironmentModel!");
		}
		for (int i = 0; i < slices.length; i++) {
			SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
			String sliceName = slice.getNode().getName();
			if (myLogger.isLoggable(Logger.FINER)) {
				myLogger.log(Logger.FINER, "Sending new EnvironmentModel to " + sliceName);
			}
			if (slice.getNode().getName().equals(this.myContainer.getNodeDescriptor().getName())==true) {
				this.environmentModel = envModel;
				if (notifySensorAgents==true) {
					this.getServiceActuator().notifySensors(envModel, this.stepSimulationAsynchronous);
				}
			} else {
				this.setEnvironmentModel2Slice(slice, envModel, notifySensorAgents);	
			}
		}
	}

	/**
	 * Broadcasts to all agents that the simulation steps forward by using their {@link ServiceSensor} and 
	 * putting the new {@link EnvironmentModel} in there.
	 *
	 * @param envModel the EnvironmentModel
	 * @param aSynchron true, if asynchronous
	 * @param slices the slices
	 * @throws ServiceException the service exception
	 * @see EnvironmentModel
	 */
	private void broadcastStepSimulation(EnvironmentModel envModel, boolean aSynchron, Service.Slice[] slices) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending new EnvironmentModel + step simulation!");
		}
		for (int i = 0; i < slices.length; i++) {
			SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
			String sliceName = slice.getNode().getName();
			if (myLogger.isLoggable(Logger.FINER)) {
				myLogger.log(Logger.FINER, "Sending new EnvironmentModel + step simulation to " + sliceName);
			}
			try {
				if (slice.getNode().getName().equals(this.myContainer.getNodeDescriptor().getName())==true) {
					this.environmentModel = envModel;
					this.getServiceActuator().notifySensors(envModel, aSynchron);
				} else {
					slice.stepSimulation(envModel, aSynchron);
				}
				
			} catch (IMTPException err) {
				myLogger.log(Logger.WARNING, "Error while sending the new EnvironmentModel + step simulation to slice " + sliceName, err);
			}
		}
	}
	
	/**
	 * Broadcast the number of agent answers that are expected for single simulation step.
	 *
	 * @param answersExpected the answers expected
	 * @param slices the slices
	 * @throws ServiceException the service exception
	 */
	private void broadcastAnswersExpected(int answersExpected, Service.Slice[] slices) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending number of expected agent answers!");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Sending number of expected agent answers to " + sliceName);
				}
				slice.setAnswersExpected(answersExpected);
				
			} catch(Throwable t) {
				myLogger.log(Logger.WARNING, "Error while sending number of expected agent answers to slice " + sliceName, t);
			}
		}
	}

	/**
	 * Sends the current set of agent answers to the manager agent of the environment.
	 *
	 * @param agentAnswers the agent answers
	 * @throws ServiceException the service exception
	 */
	private void broadcastNotifyManagerPutAgentAnswers(final Hashtable<AID, Object> agentAnswers) throws ServiceException {
		
		Service.Slice[] slices = getAllSlices();		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending agent answers to manager!");
		}
		for (int i = 0; i < slices.length; i++) {
			
			SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
			String sliceName = slice.getNode().getName();
			if (sliceName.equals(environmentManagerDescription.getLocation().getName())==true) {
				try {
					if (myLogger.isLoggable(Logger.FINER)) {
						myLogger.log(Logger.FINER, "Sending agent answers to manager at lice " + sliceName);
					}
					slice.notifyManagerPutAgentAnswers(agentAnswers);
					
				} catch(Throwable t) {
					myLogger.log(Logger.WARNING, "Error while sending agent answers to slice " + sliceName, t);
				}
			}
		}
	}
	
	/**
	 * Broadcast a single notification object to a specific agent by using its ServiceSensor.
	 *
	 * @param agentAID the agent aid
	 * @param notification the notification
	 * @param slices the slices
	 * @return true, if successful
	 * @throws ServiceException the service exception
	 */
	private boolean broadcastNotifyAgent(AID agentAID, EnvironmentNotification notification) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending notfication to agent '" + agentAID.getLocalName() + "'!");
		}
		// --- First try to find the agent locally ------------------
		boolean notified = this.getServiceActuator().notifySensorAgent(agentAID, notification);
		if (notified==true) {
			// --- Found locally - done! ----------------------------
			return notified;
			
		} else {
			// --- Try to find the agent in remote container --------
			Service.Slice[] slices = getAllSlices();
			for (int i = 0; i < slices.length; i++) {
				String sliceName = null;
				try {
					SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
					sliceName = slice.getNode().getName();
					if (myLogger.isLoggable(Logger.FINER)) {
						myLogger.log(Logger.FINER, "Try sending notfication to agent '" + agentAID.getLocalName() + "' at " + sliceName + "!");
					}
					notified = slice.notifyAgent(agentAID, notification);
					if (notified==true) {
						return notified;	
					}
					
				} catch(Throwable t) {
					myLogger.log(Logger.WARNING, "Error while sending a notification to agent '" + agentAID.getLocalName() + "' at slice " + sliceName, t);
				}
			}
		}
		return false;
	}
	
	/**
	 * Sends a notification to the manager agent of the environment.
	 *
	 * @param notification the notification
	 * @return true, if successful
	 * @throws ServiceException the service exception
	 */
	private boolean broadcastNotifyManager(EnvironmentNotification notification) throws ServiceException {
		
		Service.Slice[] slices = getAllSlices();		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending notification to manager!");
		}
		for (int i = 0; i < slices.length; i++) {
			
			SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
			String sliceName = slice.getNode().getName();
			if (sliceName.equals(environmentManagerDescription.getLocation().getName())==true) {
				try {
					if (myLogger.isLoggable(Logger.FINER)) {
						myLogger.log(Logger.FINER, "Sending notification to manager at lice " + sliceName);
					}
					return slice.notifyManager(notification);
					
				} catch(Throwable t) {
					myLogger.log(Logger.WARNING, "Error while sending notification to Manager at slice " + sliceName, t);
				}
			}
		}
		return false;
	}
	
	/**
	 * This method will broadcast that the current simulation should pause or not.
	 *
	 * @param pauseSimulation the pause simulation
	 * @param slices the slices
	 * @throws ServiceException the service exception
	 */
	private void broadcastPauseSimulation(boolean pauseSimulation, Service.Slice[] slices) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Broadcasting 'pause simulation'-message!");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Try sending 'pause simulation'-message to " + sliceName + "!");
				}
				slice.setPauseSimulation(pauseSimulation);
				
			} catch(Throwable t) {
				myLogger.log(Logger.WARNING, "Error while sending 'pause simulation'-message to slice " + sliceName, t);
			}
		}
	}
	
	/**
	 * Broadcast the new locations to the agents.
	 *
	 * @param transferAgents the agents to migrate 
	 * @param slices the slices
	 * @throws ServiceException the service exception
	 * @see AID_Container
	 */
	private void broadcastAgentMigration(Vector<AID_Container> transferAgents, Service.Slice[] slices) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending migration notification to agents!");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Sending migration notification to agents at " + sliceName);
				}
				slice.setAgentMigration(transferAgents);
				
			} catch(Throwable t) {
				myLogger.log(Logger.WARNING, "Error while sending migration notification to agents at slice " + sliceName, t);
			}
		}
	}

	/**
	 * Stops the simulation by invoking the doDelete method at all agents which are
	 * extending the class 'agentgui.simulationService.agents.SimulationAgent' and
	 * which are 'connected to a service actuator
	 *
	 * @param slices the slices
	 * @throws ServiceException the service exception
	 */
	private void broadcastStopSimulationAgents(Service.Slice[] slices) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Try to stop simulation-agents!");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Try to stop simulation-agents on " + sliceName );
				}
				slice.stopSimulationAgents();
				
			} catch(Throwable t) {
				myLogger.log(Logger.WARNING, "Error while try to get Location-Object from " + sliceName, t);
			}
		}	
	}
	
	/**
	 * Broadcast that this container is host for a display agent and does the (un-)registration in each container.
	 *
	 * @param isRegisterContainer set true, if you want to register the local container as a host for a display agent, otherwise false
	 * @throws ServiceException the service exception
	 */
	private void broadcastDisplayAgentContainerUnRegistration(boolean isRegisterContainer) throws ServiceException {
		
		String containerName = this.myContainer.getNodeDescriptor().getName();
		String action = "Register";
		if (isRegisterContainer==false) {
			action = "Unregister";
		}
				
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, action + " container as host fo a DisplayAgent!");
		}
		Service.Slice[] slices = getAllSlices();
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, action + " container as host fo a DisplayAgent at slice " + sliceName);
				}
				slice.doDisplayAgentContainerUnRegister(containerName, isRegisterContainer);
				
			} catch(Throwable t) {
				myLogger.log(Logger.WARNING, "Error while " + action + " container as host fo a DisplayAgent at slice " + sliceName, t);
			}	
		}
	}
	/**
	 * Sets a new EnvironmentModel to all registered DisplayAgents.
	 * @param notification the notification
	 * @throws ServiceException the service exception
	 */
	private void broadcastDisplayAgentSetEnvironmentModel(EnvironmentModel envModel) throws ServiceException {
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending EnvironmentModel to DisplayAgents!");
		}
		
		Set<String> displayContainer = displayAgentDistribution.keySet();
		Service.Slice[] slices = getAllSlices();
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				// --- Only send if a DisplayAgent is known at this node ------
				if (displayContainer.contains(sliceName)==true) {
					if (myLogger.isLoggable(Logger.FINER)) {
						myLogger.log(Logger.FINER, "Sending EnvironmentModel to DisplayAgents at slice " + sliceName);
					}
					slice.displayAgentSetEnvironmentModel(envModel);
				}
				
			} catch(Throwable t) {
				myLogger.log(Logger.WARNING, "Error while sending EnvironmentModel to DisplayAgents at slice " + sliceName, t);
			}	
		}
	}
	/**
	 * Sends a new Notification to all registered DisplayAgents.
	 * @param notification the notification
	 * @throws ServiceException the service exception
	 */
	private void broadcastDisplayAgentNotification(EnvironmentNotification notification) throws ServiceException {
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending DisplayAgentNotification to DisplayAgents!");
		}
		
		if (displayAgentDistribution!=null) {
			// --- Distribute the notifications -----------------------------------------
			Set<String> displayContainer = displayAgentDistribution.keySet();
			Service.Slice[] slices = getAllSlices();
			for (int i = 0; i < slices.length; i++) {
				String sliceName = null;
				try {
					SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
					sliceName = slice.getNode().getName();
					// --- Only send if a DisplayAgent is known at this node ------------
					if (displayContainer.contains(sliceName)==true) {
						if (myLogger.isLoggable(Logger.FINER)) {
							myLogger.log(Logger.FINER, "Sending display notification to slice " + sliceName);
						}
						slice.displayAgentNotification(notification);
					}
					
				} catch(Throwable t) {
					myLogger.log(Logger.WARNING, "Error while sending display notification to slice " + sliceName, t);
				}	
			}			
		}
	}
	
	
	// --------------------------------------------------------------	
	// ---- Inner-Class 'ServiceComponent' ---- Start ---------------
	// --------------------------------------------------------------
	/**
	 * Inner class ServiceComponent. Will receive Commands, which 
	 * are coming from the SimulationServiceProxy 
	 */
	private class ServiceComponent implements Service.Slice {
		
		private static final long serialVersionUID = 1776886375724997808L;

		/* (non-Javadoc)
		 * @see jade.core.Service.Slice#getService()
		 */
		public Service getService() {
			return SimulationService.this;
		}
		/* (non-Javadoc)
		 * @see jade.core.Service.Slice#getNode()
		 */
		public Node getNode() throws ServiceException {
			try {
				return SimulationService.this.getLocalNode();
			}
			catch(IMTPException imtpe) {
				throw new ServiceException("Error retrieving local node", imtpe);
			}
		}
		/* (non-Javadoc)
		 * @see jade.core.Service.Slice#serve(jade.core.HorizontalCommand)
		 */
		public VerticalCommand serve(HorizontalCommand cmd) {
			
			try {
				if (cmd==null) return null;
				//if ( ! cmd.getService().equals(NAME) ) return null;
				
				String cmdName = cmd.getName();
				Object[] params = cmd.getParams();
				
				//System.out.println( "=> ServiceComponent " + cmdName);
				if (cmdName.equals(SimulationServiceSlice.SERVICE_SYNCH_GET_REMOTE_TIME)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering Remote-Time-Request");
					}					
					cmd.setReturnValue(getLocalTime());
					
				} else if (cmdName.equals(SimulationServiceSlice.SERVICE_SYNCH_SET_TIME_DIFF)) {
					long timeDifference = (Long) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Setting Time-Difference to Main-Platform");
					}					
					setPlatformTimeDiff(timeDifference);
					
				} else if (cmdName.equals(SimulationServiceSlice.SIM_SET_MANAGER_AGENT)) {
					EnvironmentManagerDescription envManager = (EnvironmentManagerDescription) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received AID of the Agent-Manager");
					}	
					setManagerAgent(envManager);
					
				} else if (cmdName.equals(SimulationServiceSlice.SIM_GET_ENVIRONMENT_MODEL_FROM_SETUP)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received request for environment model from setup");
					}
					EnvironmentModel envModel = getEnvironmentModelFromSetup(); 
					cmd.setReturnValue(envModel);
					
				} else if (cmdName.equals(SimulationServiceSlice.SIM_SET_ENVIRONMENT_MODEL)) {
					EnvironmentModel envModel = (EnvironmentModel) params[0];
					boolean notifySensorAgents = (Boolean) params[1];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received new environment model");
					}	
					setEnvironmentModel(envModel, notifySensorAgents);
					
				} else if (cmdName.equals(SimulationServiceSlice.SIM_STEP_SIMULATION)) {
					EnvironmentModel envModel = (EnvironmentModel) params[0];
					boolean aSynchron = (Boolean) params[1];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received 'Step Simulation'");
					}	
					stepSimulation(envModel, aSynchron);
					
				} else if (cmdName.equals(SimulationServiceSlice.SIM_SET_ANSWERS_EXPECTED)) {
					int answersExpected = (Integer) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received number of expected agent answers'");
					}	
					setEnvironmentInstanceNextPartsExpected(answersExpected);
					
				} else if (cmdName.equals(SimulationServiceSlice.SIM_PAUSE_SIMULATION)) {
					boolean pause = (Boolean) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Getting 'pause-simulation'-message");
					}	
					setPauseSimulation(pause);
					
				} else if (cmdName.equals(SimulationServiceSlice.SIM_NOTIFY_AGENT)) {
					AID agentAID = (AID) params[0];
					EnvironmentNotification notification = (EnvironmentNotification) params[1];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received 'Notify Agent for '" + agentAID.getLocalName() + "'");
					}	
					cmd.setReturnValue(notifySensorAgent(agentAID, notification));
					
				} else if (cmdName.equals(SimulationServiceSlice.SIM_NOTIFY_MANAGER)) {
					EnvironmentNotification notification = (EnvironmentNotification) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Getting parts for the next environment model");
					}	
					cmd.setReturnValue(notifyManagerAgent(notification));
					
				}  else if (cmdName.equals(SimulationServiceSlice.SIM_NOTIFY_MANAGER_PUT_AGENT_ANSWERS)) {
					@SuppressWarnings("unchecked")
					Hashtable<AID, Object> allAgentAnswers = (Hashtable<AID, Object>) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Getting parts for the next environment model");
					}	
					notifyManagerAgentPutAgentAnswers(allAgentAnswers);
					
				} else if (cmdName.equals(SimulationServiceSlice.SIM_SET_ENVIRONMENT_NEXT_PART)) {
					@SuppressWarnings("unchecked")
					Hashtable<AID, Object> nextPartsLocal = (Hashtable<AID, Object>) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Getting parts for the next environment model");
					}	
					setEnvironmentInstanceNextPart(nextPartsLocal);		
					
				} else if (cmdName.equals(SimulationServiceSlice.SIM_GET_ENVIRONMENT_NEXT_PARTS)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering request for the next parts of the environment-model" );
					}	
					cmd.setReturnValue(getEnvironmentInstanceNextParts());
					
				} else if (cmdName.equals(SimulationServiceSlice.SIM_RESET_ENVIRONMENT_NEXT_PARTS)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Reseting next parts of the environment-model" );
					}	
					resetEnvironmentInstanceNextParts();	
					
				} else if (cmdName.equals(SimulationServiceSlice.SERVICE_STOP_SIMULATION_AGENTS)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Stoping simulation agents in this container");
					}
					stopSimulationAgents();
					
				} else if (cmdName.equals(SimulationServiceSlice.SERVICE_SET_AGENT_MIGRATION)) {
					@SuppressWarnings("unchecked")
					Vector<AID_Container> transferAgents = (Vector<AID_Container>) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Getting info about agent migration");
					}
					setAgentMigration(transferAgents);
					
				} else if (cmdName.equals(SimulationServiceSlice.SERVICE_DISPLAY_CONTAINER_UN_REGISTRATION)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received display container registration");
					}
					String containerName = (String) params[0];
					Boolean isRegisterContainer = (boolean) params[1];
					doDisplayAgentContainerUnRegister(containerName, isRegisterContainer);
					
				} else if (cmdName.equals(SimulationServiceSlice.SERVICE_DISPLAY_AGENT_SET_ENVIRONMENT_MODEL)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received EnvironmentModel for DisplayAgents");
					}
					EnvironmentModel envModel = (EnvironmentModel) params[0];
					displayAgentSetEnvironmentModel(envModel);
					
				} else if (cmdName.equals(SimulationServiceSlice.SERVICE_DISPLAY_AGENT_NOTIFICATION)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received DisplayAgentNotification");
					}
					EnvironmentNotification notification = (EnvironmentNotification) params[0];
					displayAgentNotification(notification);

				} else if (cmdName.equals(LoadServiceSlice.SERVICE_GET_AID_LIST)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering request for the Agents in this container");
					}
					cmd.setReturnValue(getListOfAgents());
					
				} else if (cmdName.equals(LoadServiceSlice.SERVICE_GET_AID_LIST_SENSOR)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering request for the Agents with sensors in this container");
					}
					cmd.setReturnValue(getListOfAgentsWithSensors());
				}

			} catch (Throwable t) {
				cmd.setReturnValue(t);
			}
			return null;
		}
		
		// -----------------------------------------------------------------
		// --- The real functions for the Service Component --- Start ------ 
		// -----------------------------------------------------------------
		/**
		 * Gets the local time.
		 * @return the local time
		 */
		private long getLocalTime() {
			return System.currentTimeMillis();
		}
		/**
		 * Sets the time difference compared to the Main-Container.
		 * @param timeDifference the time difference
		 */
		private void setPlatformTimeDiff(long timeDifference) {
			timeDiff2MainContainer = timeDifference;	
		}
		
		/**
		 * Sets the EnvironmentManagerDescription of the manager agent.
		 * @param envManager the new manager agent
		 */
		private void setManagerAgent(EnvironmentManagerDescription envManager) {
			environmentManagerDescription = envManager;
		}
		
		/**
		 * Returns an {@link EnvironmentModel} copy from the setup, if available.
		 * @return the environment model from setup
		 */
		private EnvironmentModel getEnvironmentModelFromSetup() {

			EnvironmentModel envModel = null;
			Project currProject = Application.getProjectFocused();
			if (currProject!=null) {
				// --- Get the environment model from the controller ----------
				EnvironmentController envController = currProject.getEnvironmentController();
				if (envController!=null) {
					EnvironmentModel envModelTmp = envController.getEnvironmentModel();
					if (envModelTmp!=null) {
						envModel = envModelTmp.getCopy();
					}
				}
			}
			return envModel;
		}
		/**
		 * Sets the environment model.
		 *
		 * @param newEnvironmentModel the new environment model
		 * @param notifySensorAgents true, if the sensor agents should be also notified 
		 */
		private void setEnvironmentModel(EnvironmentModel newEnvironmentModel, boolean notifySensorAgents) {
			environmentModel = newEnvironmentModel;
			if (notifySensorAgents==true) {
				getServiceActuator().notifySensors(environmentModel, stepSimulationAsynchronous);				
			}
		}
		/**
		 * Steps the simulation locally by putting the new EnvironmentModel to the connected agents.
		 *
		 * @param newEnvironmentModel the new environment model
		 * @param aSynchron true, if step is asynchronously
		 */
		private void stepSimulation(EnvironmentModel newEnvironmentModel, boolean aSynchron) {
			environmentModel = newEnvironmentModel;
			getServiceActuator().notifySensors(newEnvironmentModel, aSynchron);
		} 
		/**
		 * Notifies the manager agent about the agent answers.
		 *
		 * @param allAgentAnswers the all agent answers
		 */
		private void notifyManagerAgentPutAgentAnswers(Hashtable<AID, Object> allAgentAnswers) {
			localServiceActuator4Manager.putAgentAnswers(environmentManagerDescription.getAID(), allAgentAnswers, true);
		}
		/**
		 * Sends a notification to the manager agent.
		 *
		 * @param notification the notification
		 * @return true, if successful
		 */
		private boolean notifyManagerAgent(EnvironmentNotification notification) {
			return localServiceActuator4Manager.notifyManager(environmentManagerDescription.getAID(), notification);
		}		
		/**
		 * Sends a notification to a specified sensor agent.
		 *
		 * @param agentAID the agent aid
		 * @param notification the notification
		 * @return true, if successful
		 */
		private boolean notifySensorAgent(AID agentAID, EnvironmentNotification notification){
			return getServiceActuator().notifySensorAgent(agentAID, notification);
		}
		
		/**
		 * Sets the number of environment changes expected within one simulation step.
		 *
		 * @param answersExpected the number of answers expected
		 */
		private void setEnvironmentInstanceNextPartsExpected(int answersExpected) {
			environmentInstanceNextPartsExpected=answersExpected;
		}	
		/**
		 * Will receive the environment changes from a different (remote) container. 
		 * If the answers are complete, it will notify the manager agent.
		 *
		 * @param nextPartsLocal the Hashtable of changes in the environment
		 */
		private void setEnvironmentInstanceNextPart(Hashtable<AID, Object> nextPartsLocal) {
			environmentInstanceNextParts.putAll(nextPartsLocal);
			if (environmentInstanceNextParts.size()>=environmentInstanceNextPartsExpected) {
				// --- We are in the Main-Container and got the expected ---  
				// --- number of answers for a single simulation step	 ---
				try {
					broadcastNotifyManagerPutAgentAnswers(environmentInstanceNextParts);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		}
		/**
		 * Provides the environment changes as a Hashtable<AID, Object>
		 *
		 * @return the environment changes for the next simulation step
		 */
		private Hashtable<AID, Object> getEnvironmentInstanceNextParts() {
			return environmentInstanceNextParts;
		}
		/**
		 * Will reset the local set of environment changes to a new and empty Hashtable.
		 */
		private void resetEnvironmentInstanceNextParts() {
			environmentInstanceNextParts = new Hashtable<AID, Object>();
			environmentInstanceNextPartsLocal = new Hashtable<AID, Object>();
		}
		
		/**
		 * Stop simulation agents.
		 */
		private void stopSimulationAgents() {
			getServiceActuator().notifySensorAgentsDoDelete();
		}
		
		/**
		 * Sets the pause of the simulation.
		 * @param pause true, if the simulation should pause
		 */
		private void setPauseSimulation(boolean pause) {
			getServiceActuator().notifySensorPauseSimulation(pause);
			localServiceActuator4Manager.notifyManagerPauseSimulation(pause);
		}
		
		/**
		 * Sets the agent migration.
		 * @param transferAgents the new agent migration
		 */
		private void setAgentMigration(Vector<AID_Container> transferAgents) {
			getServiceActuator().setMigration(transferAgents);
		}
		
		/**
		 * Returns the list of agents running in this container. Additionally the 
		 * class name of the agent will be resolved. 
		 * @return the AID list of the local agents
		 */
		private AID[] getListOfAgents() {
			
			AID[] localAgentAIDs = myContainer.agentNames();
			try {
			
				// --- Add user defined slot value for the class name ---------
				for (int i = 0; i < localAgentAIDs.length; i++) {
					if (localAgentAIDs[i].getAllUserDefinedSlot().get(AID_PROPERTY_CLASSNAME)==null) {
						Agent agent = myContainer.acquireLocalAgent(localAgentAIDs[i]);
						if (agent!=null) {
							localAgentAIDs[i].addUserDefinedSlot(AID_PROPERTY_CLASSNAME, agent.getClass().getName());
							myContainer.releaseLocalAgent(localAgentAIDs[i]);
						}
					}
				}
				
			} catch (Exception ex) {
				// --- Simply continue in case of errors ---------------------- 
			}
			return localAgentAIDs;
		}
		
		/**
		 * Returns the list of agents, which are connected through the {@link ServiceActuator}.
		 * @return the list of agents with sensors
		 */
		private AID[] getListOfAgentsWithSensors() {
			return getServiceActuator().getSensorAgents();
		}
		
		/**
		 * Does the (un-)registration of a container that hosts a dislay agent.
		 *
		 * @param containerName the container name
		 * @param isRegisterContainer the is register container
		 */
		private void doDisplayAgentContainerUnRegister(String containerName, boolean isRegisterContainer) {

			Integer noOfDAs = displayAgentDistribution.get(containerName);
			if (isRegisterContainer==true) {
				// --------------------------------------------------
				// --- Register display agent container -------------
				// --------------------------------------------------
				if (noOfDAs==null) {
					displayAgentDistribution.put(containerName, 1);
				} else {
					displayAgentDistribution.put(containerName, noOfDAs+1);
				}
				
			} else {
				// --------------------------------------------------
				// --- Unregister display agent container -----------
				// --------------------------------------------------
				if (noOfDAs!=null) {
					if (noOfDAs<=1) {
						displayAgentDistribution.remove(containerName);
					} else {
						displayAgentDistribution.put(containerName, noOfDAs-1);
					}
				}
			}
		}
		/**
		 * Sets a new EnvironmentModel to all registered DisplayAgents.
		 * @param notification the EnvironmentNotification
		 */
		public void displayAgentSetEnvironmentModel(EnvironmentModel envModel) {
			synchronized (displayAgents) {
				if (displayAgents!=null) {
					for (AbstractDisplayAgent displayAgent : displayAgents) {
						getServiceActuator().notifySensor(envModel, displayAgent.getAID());
					}
				}
			}
		}
		/**
		 * Sets a new Notification to all registered DisplayAgents.
		 * @param notification the EnvironmentNotification
		 */
		public void displayAgentNotification(EnvironmentNotification notification) {
			synchronized (displayAgents) {
				for (AbstractDisplayAgent displayAgent : displayAgents) {
					getServiceActuator().notifySensorAgent(displayAgent.getAID(), notification);
				}
			}
		}
		// -----------------------------------------------------------------
		// --- The real functions for the Service Component --- Stop ------- 
		// -----------------------------------------------------------------

		
	} 
	// --------------------------------------------------------------	
	// ---- Inner-Class 'ServiceComponent' ---- End -----------------
	// --------------------------------------------------------------
	
	
	// --------------------------------------------------------------	
	// ---- Inner-Class 'CommandOutgoingFilter' ---- Start ----------
	// --------------------------------------------------------------
	/**
	 * Inner class CommandOutgoingFilter.
	 */
	private class CommandOutgoingFilter extends Filter {
		
		/**
		 * Instantiates a new command outgoing filter.
		 */
		public CommandOutgoingFilter() {
			super();
			//setPreferredPosition(2);  // Before the Messaging (encoding) filter and the security related ones
		}
		/* (non-Javadoc)
		 * @see jade.core.Filter#accept(jade.core.VerticalCommand)
		 */
		public final boolean accept(VerticalCommand cmd) {
			
			if (cmd==null) return true;

			String cmdName = cmd.getName();
			//System.out.println( "=> out " + cmdName + " - " + cmd.getService() + " - " + cmd.getService().getClass() );	

			if (cmdName.equals(AgentMobilityHelper.INFORM_MOVED)) {
				Object[] params = cmd.getParams();
				@SuppressWarnings("unused")
				AID aid = (AID) params[0];
//				ContainerID id = (ContainerID) params[1];
//				localServiceActuator.setAgentMigrated(aid);
			}
			// Never veto other commands
			return true;
		}
	}
	// --------------------------------------------------------------	
	// ---- Inner-Class 'CommandOutgoingFilter' ---- End ------------
	// --------------------------------------------------------------


	// --------------------------------------------------------------	
	// ---- Inner-Class 'CommandIncomingFilter' ---- Start ----------
	// --------------------------------------------------------------
	/**
	 * Inner class CommandIncomingFilter.
	 */
	private class CommandIncomingFilter extends Filter {
		
		/* (non-Javadoc)
		 * @see jade.core.Filter#accept(jade.core.VerticalCommand)
		 */
		public boolean accept(VerticalCommand cmd) {
			
			if (cmd==null) return true;
			String cmdName = cmd.getName();
			//System.out.println( "=> in " + cmdName + " - " + cmd.getService());
			
			if (myMainContainer != null) {
				if (cmdName.equals(Service.NEW_SLICE)) {
					// --- If the new slice is a SimulationServiceSlice, notify it about the current state ---
					handleNewSlice(cmd);
				}
				
			} else {
				if (cmdName.equals(Service.REATTACHED)) {
					// The Main lost all information related to this container --> Notify it again
					
				}
			}
			// Never veto a Command
			return true;
		}
	} 
	// --------------------------------------------------------------	
	// ---- Inner-Class 'CommandIncomingFilter' ---- End ------------
	// --------------------------------------------------------------

	/**
	 * If the new slice is a SimulationServiceSlice notify it about the current state.
	 *
	 * @param cmd the VerticalCommand
	 */
	private void handleNewSlice(VerticalCommand cmd) {
		
		if (cmd.getService().equals(NAME)) {
			// --- We ARE in the Main-Container !!! ----------------------------------------
			Object[] params = cmd.getParams();
			String newSliceName = (String) params[0];
			try {
				// --- Be sure to get the new (fresh) slice --> Bypass the service cache ---
				SimulationServiceSlice newSlice = (SimulationServiceSlice) getFreshSlice(newSliceName);
				// --- Set remote ManagerAgent, TimeModel,EnvironmentInstance --------------
				newSlice.setManagerAgent(environmentManagerDescription);
				// --- Set the current environment model to the new slice ------------------
				this.setEnvironmentModel2Slice(newSlice, environmentModel, false);
				// --- Synchronise the time ------------------------------------------------
				this.synchroniseTimeOfSlice(newSlice);
				
			} catch (Throwable t) {
				myLogger.log(Logger.WARNING, "Error notifying new slice '"+newSliceName+"' about current SimulationService-State", t);
			}
		}
	}

	/**
	 * Sets the environment model to the specified slice.
	 *
	 * @param simSlice the SimulationServiceSlice
	 * @param envModel the EnvironmentModel
	 * @param notifySensorAgents true, if the sensor agents should be also notified 
	 * 
	 * @return the environment model
	 * @throws ServiceException the service exception
	 */
	private void setEnvironmentModel2Slice(SimulationServiceSlice simSlice, EnvironmentModel envModel, boolean notifySensorAgents) throws ServiceException {
		try {
			simSlice.setEnvironmentModel(envModel, notifySensorAgents);
		} catch (IMTPException err) {
			myLogger.log(Logger.WARNING, "Error while sending the new EnvironmentModel to slice " + simSlice.getNode().getName(), err);
		}
	}
	
	/**
	 * This method will synchronise the time between Main-Container and Remote-Container.
	 * It is used only from a Main-Container.
	 * 
	 * @param slice the slice
	 */
	private void synchroniseTimeOfSlice(SimulationServiceSlice slice) {
		
		int countMax = timeMeasureCountMax;
		long locTime1Milli = 0;
		long remTimeMilli = 0;

		long measureTime1Nano = 0;
		long measureTime2Nano = 0;
		
		double timeDiffAccumulate = 0;
		
		try {
	
			for (int i=0; i<countMax; i++) {
				// --- Measure local time and ask for the remote time ---
				locTime1Milli = System.currentTimeMillis();			// --- milliseconds ---
				measureTime1Nano = System.nanoTime();					// --- nanoseconds ---
				remTimeMilli = slice.getRemoteTime();				// --- milliseconds ---
				measureTime2Nano = System.nanoTime();					// --- nanoseconds ---
				
				timeDiffAccumulate += getContainerTimeDifference(locTime1Milli, remTimeMilli, measureTime1Nano, measureTime2Nano);

			}
			slice.setRemoteTimeDiff(Math.round(timeDiffAccumulate / countMax));	
			
		} catch (IMTPException e) {
			e.printStackTrace();
		}
	}
	/**
	 * This Method returns the time difference between this and the remote node
	 * by using the local-time and the time to get the remote-time.
	 *
	 * @param locTime1Milli the local time in milliseconds
	 * @param remTimeMilli the remote time in milliseconds
	 * @param measureTime1Nano the time where the measurement STARTED in nanoseconds
	 * @param measureTime2Nano the time where the measurement STOPED in nanoseconds
	 * @return the container time difference
	 */
	private double getContainerTimeDifference(long locTime1Milli, long remTimeMilli, long measureTime1Nano, long measureTime2Nano) {
		
		// --- Calculate the correction value of the remote time
		long measureTimeNanoCorrect = (measureTime2Nano - measureTime1Nano)/2;
		double measureTimeMilliCorrect = measureTimeNanoCorrect * Math.pow(10, -6);;
		// --- Correct the measured remote time -----------------
		double remTimeMilliCorrect = remTimeMilli - measureTimeMilliCorrect;
		// --- Calculate the time Difference between ------------
		// --- this and the remote platform 		 ------------
		return locTime1Milli - remTimeMilliCorrect;
	}
	/**
	 * This method will synchronise the local time with the time in the Main-Container.
	 */
	private void setMainContainerTimeLocally() {
		
		if (System.currentTimeMillis() >= this.timeMeasureNext ) {
			// --- Set next measure time ---------------------------------
			this.timeMeasureNext = System.currentTimeMillis() + this.timeMeasureInterval;
			
			try {
				// --- Get the Main-Container slice ----------------------
				SimulationServiceSlice ssl = (SimulationServiceSlice) getSlice(MAIN_SLICE);
				// --- Measure time-difference to Main-Container ---------
				double remTimeMilliDiffAccumulate = 0;
				for (int i=0; i<this.timeMeasureCountMax; i++) {
					// --- Measure local time and ask for remote time ----
					long locTime1Milli = System.currentTimeMillis();
					long locTime1Nano = System.nanoTime();										// --- nano-seconds ---
					long remTimeMilli = ssl.getRemoteTime(); 
					long locTime2Nano = System.nanoTime();										// --- nano-seconds ---
					
					remTimeMilliDiffAccumulate += getContainerTimeDifference(locTime1Milli, remTimeMilli, locTime1Nano, locTime2Nano);
				}
				// --- Set local value for time difference ---------------
				this.timeDiff2MainContainer = Math.round(remTimeMilliDiffAccumulate / this.timeMeasureCountMax) * (-1);	

			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (IMTPException e) {
				e.printStackTrace();
			}
		} 		
	}
	
		
}
