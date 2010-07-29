package mas.service;

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
import jade.core.management.AgentManagementSlice;
import jade.util.Logger;

import java.util.Date;
import java.util.Hashtable;
import java.util.Observer;

import mas.service.environment.EnvironmentModel;
import mas.service.sensoring.ServiceActuator;
import mas.service.time.TimeModel;
import mas.service.transaction.TransactionMap;

/**
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public class SimulationService extends BaseService {

	public static final String NAME = SimulationServiceHelper.SERVICE_NAME;
	
	public static final String SERVICE_UPDATE_TIME_MODEL = SimulationServiceHelper.SERVICE_UPDATE_TIME_MODEL;
	public static final String SERVICE_UPDATE_TIME_STEP = SimulationServiceHelper.SERVICE_UPDATE_TIME_STEP;
	public static final String SERVICE_UPDATE_ENVIRONMENT = SimulationServiceHelper.SERVICE_UPDATE_ENVIRONMENT;
	public static final String SERVICE_UPDATE_SIMULATION = SimulationServiceHelper.SERVICE_UPDATE_SIMULATION;
	
	private AgentContainer myContainer;
	private MainContainer myMainContainer;
	
	private Filter incFilter;
	private Filter outFilter;
	private ServiceComponent localSlice;
	
	// --- The List of Agents, which are registered to this service ----------- 
	private Hashtable<String,AID> agentList = new Hashtable<String,AID>();
	
	// --- The Agent who is the Manager / Controller of the Simulation --------
	private AID managerAgent = null;
	// --- The current TimeModel ----------------------------------------------
	private TimeModel timeModel = null;
	// --- The current EnvironmentObject Instance -----------------------------
	private Object environmentInstance = null;
	
	// --- The current EnvironmentModel ---------------------------------------
	private EnvironmentModel environmentModel = null;
	// --- The TransactionMap of the simulation -------------------------------
	private TransactionMap transactionMap = new TransactionMap();
	// --- The Counter of the Simulation  -------------------------------------
	private Long simulationCounter = new Long(0);

	// --- The Actuator for this Sevice, which can inform registered ---------- 
	// --- Agents about changes in the Simulation e.g. 'stepTimeModel' --------
	private ServiceActuator localServiceActuator = new ServiceActuator();
	
	
	public void init(AgentContainer ac, Profile p) throws ProfileException {
		
		super.init(ac, p);
		myContainer = ac;
		myMainContainer = ac.getMain();
		// Create filters
		outFilter = new CommandOutgoingFilter();
		incFilter = new CommandIncomingFilter();
		// Create local slice
		localSlice = new ServiceComponent();
		
		if (myContainer!=null) {
			if (myLogger.isLoggable(Logger.FINE)) {
				myLogger.log(Logger.FINE, "Starting SimulationService: My-Container: " + myContainer.toString());
			}
		}
		if (myMainContainer!=null) {
			if (myLogger.isLoggable(Logger.FINE)) {
				myLogger.log(Logger.FINE, "Main-Container: " + myMainContainer.toString());
			}
		}
	}
	public void boot(Profile p) throws ServiceException {
		super.boot(p);
	}
	public String getName() {
		return NAME;
	}
	public ServiceHelper getHelper (Agent ag) {
		return new SimulationServiceImpl();
	}
	public Filter getCommandFilter(boolean direction) {
		if(direction == Filter.INCOMING) {
			return incFilter;
		}
		else {
			return outFilter;
		}
	}
	public Class<?> getHorizontalInterface() {
		return SimulationServiceSlice.class;
	}
	/**
	 * Retrieve the locally installed slice of this service.
	 */
	public Service.Slice getLocalSlice() {
		return localSlice;
	}

	
	// --------------------------------------------------------------	
	// ---- Inner-Class 'AgentTimeImpl' ---- Start ------------------
	// --------------------------------------------------------------
	/**
	 * Sub-Class to provide interaction between Agents and this Service
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
	 */
	public class SimulationServiceImpl implements SimulationServiceHelper {

		private static final long serialVersionUID = 5741448121178289099L;

		public void init(Agent ag) {
			// --- Store the Agent in the agentList -----------------
			agentList.put(ag.getName(), ag.getAID());			
		}
		
		// ----------------------------------------------------------
		// --- Method to notify all Semsors about changes -----------
		public void notifySensors(String event) throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastNotifySensors(event, slices);
		}
		
		// ----------------------------------------------------------
		// --- Methods to register and unregister Agents-Sensors ----
		public void addSensor(Agent agentWithSensor) throws ServiceException {
			localServiceActuator.addObserver((Observer) agentWithSensor);
		}
		public void deleteSensor(Agent agentWithSensor) throws ServiceException {
			localServiceActuator.deleteObserver((Observer) agentWithSensor);	
		}		
		
		// ----------------------------------------------------------
		// --- Methods for the Manager-Agent ------------------------
		public void setManagerAgent(AID agentAddress) throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastManagerAgent(agentAddress, slices);
		}		
		public AID getManagerAgent() throws ServiceException {
			return managerAgent;
		}
		
		// ----------------------------------------------------------
		// --- Methods for the Simulation ---------------------------
		public void stepSimulation(Object envObjectInstance) throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			this.setEnvironmentInstance(envObjectInstance);
			this.stepTimeModel();
			broadcastNotifySensors(SERVICE_UPDATE_SIMULATION, slices);
		}
		
		// ----------------------------------------------------------
		// --- Methods on the TimeModel -----------------------------
		public void setTimeModel(TimeModel newTimeModel) throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastSetTimeModel(newTimeModel, slices);
			broadcastNotifySensors(SERVICE_UPDATE_TIME_MODEL, slices);			
		}
		public TimeModel getTimeModel() throws ServiceException {
			return timeModel;
		}
		public void stepTimeModel() throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastStepTimeModel(slices);
			broadcastNotifySensors(SERVICE_UPDATE_TIME_STEP, slices);
		}		
		
		// ----------------------------------------------------------
		// --- Methods on the EnvironmentModel ----------------------
		public void setEnvironmentInstance(Object envObjectInstance) throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastEnvironmentInstance(envObjectInstance, slices);
			broadcastNotifySensors(SERVICE_UPDATE_ENVIRONMENT, slices);
		}
		public Object getEnvironmentInstance() throws ServiceException {
			return environmentInstance;
		}
		
		// ----------------------------------------------------------
		// --- Methods on the real Time of the Main-Container -------
		public Date getTimeOfMainContainerAsDate() {
			Date nowDate = new Date();
			return nowDate;
		}
		public Long getTimeOfMainContainerAsLong() {
			return System.currentTimeMillis();
		}

	}
	// --------------------------------------------------------------	
	// ---- Inner-Class 'AgentTimeImpl' ---- End --------------------
	// --------------------------------------------------------------
	

	/**
	 * Broadcast the AID of the Simulation-Manager Agent to all Slices
	 * @param timeModel 
	 * @param slices
	 * @throws ServiceException
	 */
	private void broadcastManagerAgent(AID agentAddress, Service.Slice[] slices) throws ServiceException {
		
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
				slice.setManagerAgent(agentAddress);
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error propagating current TimeModel to slice  " + sliceName, t);
			}
		}
	}
	
	/**
	 * Broadcast the current TimeModel to all Slices
	 * @param timeModel 
	 * @param slices
	 * @throws ServiceException
	 */
	private void broadcastSetTimeModel(TimeModel timeModel, Service.Slice[] slices) throws ServiceException {
		
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
				slice.setTimeModel(timeModel);
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error propagating current TimeModel to slice  " + sliceName, t);
			}
		}
	}
	
	/**
	 * Broadcast to all Slices to step to the next Point in Time
	 * @param slices
	 * @throws ServiceException
	 */
	private void broadcastStepTimeModel(Service.Slice[] slices) throws ServiceException {
		
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
				slice.stepTimeModel();
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error propagating current TimeModel to slice  " + sliceName, t);
			}
		}
	}
	
	/**
	 * Broadcast the EnvironmentInstance to all Slices
	 * @param envObjectInstance 
	 * @param slices
	 * @throws ServiceException
	 */
	private void broadcastEnvironmentInstance(Object envObjectInstance, Service.Slice[] slices) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending current Environment-Update!");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Sending current Environment-Update to " + sliceName);
				}
				slice.setEnvironmentInstance(envObjectInstance);
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error propagating current Environment-Update to slice  " + sliceName, t);
			}
		}
	}
	
	/**
	 * Broadcast that all agents have to informed about changes through his ServiceSensor
	 * @param envObjectInstance 
	 * @param slices
	 * @throws ServiceException
	 */
	private void broadcastNotifySensors(String topicWhichChanged, Service.Slice[] slices) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending 'Notify Sensor' for " + topicWhichChanged + "!");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Sending 'Notify Sensor' for " + topicWhichChanged + " to " + sliceName);
				}
				slice.notifySensors(topicWhichChanged);
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error propagating 'Notify Sensor' for " + topicWhichChanged + " to slice " + sliceName, t);
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

		public Service getService() {
			return SimulationService.this;
		}
		
		public Node getNode() throws ServiceException {
			try {
				return SimulationService.this.getLocalNode();
			}
			catch(IMTPException imtpe) {
				throw new ServiceException("Error retrieving local node", imtpe);
			}
		}
		
		public VerticalCommand serve(HorizontalCommand cmd) {
			
			try {
				if (cmd==null) return null;
				//if ( ! cmd.getService().equals(NAME) ) return null;
				
				String cmdName = cmd.getName();
				Object[] params = cmd.getParams();
				
				//System.out.println( "=> ServiceComponent " + cmdName);
				if (cmdName.equals(SimulationServiceSlice.SIM_SET_MANAGER_AGENT)) {
					AID managerAdress = (AID) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received AID of the Agent-Manager");
					}	
					setManagerAgent(managerAdress);
				}
				else if (cmdName.equals(SimulationServiceSlice.SIM_GET_MANAGER_AGENT)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering Management-Agent-Request");
					}					
					cmd.setReturnValue(getManagerAgent());
				}
				
				else if (cmdName.equals(SimulationServiceSlice.SIM_SET_TIMEMODEL)) {
					TimeModel ntm = (TimeModel) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received TimeModel");
					}					
					setTimeModel(ntm);
				}
				else if(cmdName.equals(SimulationServiceSlice.SIM_GET_TIMEMODEL)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering TimeModel-Request");
					}					
					cmd.setReturnValue(getTimeModel());
				} 
				else if (cmdName.equals(SimulationServiceSlice.SIM_STEP_TIMEMODEL)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Step the TimeModel");
					}					
					stepTimeModel();				
				}
				
				else if (cmdName.equals(SimulationServiceSlice.SIM_SET_ENVIRONMENT)) {
					Object envObj = (Object) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received Environment-Instance");
					}	
					setEnvironmentInstance(envObj);
				}
				else if (cmdName.equals(SimulationServiceSlice.SIM_GET_ENVIRONMENT)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering Request for Environment-Instance");
					}	
					cmd.setReturnValue(getEnvironmentInstance());
				}
				
				else if (cmdName.equals(SimulationServiceSlice.SERVICE_UPDATE_TIME_MODEL) || 
						 cmdName.equals(SimulationServiceSlice.SERVICE_UPDATE_TIME_STEP) ||
						 cmdName.equals(SimulationServiceSlice.SERVICE_UPDATE_ENVIRONMENT) ||
						 cmdName.equals(SimulationServiceSlice.SERVICE_UPDATE_SIMULATION)
						) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received 'Notfy Sensor': " + cmdName);
					}	
					notifySensors(cmdName);
				}
				
			}
			catch (Throwable t) {
				cmd.setReturnValue(t);
			}
			return null;
			
		}
		
		private void setManagerAgent(AID agentAddress) {
			managerAgent = agentAddress;
		}
		private AID getManagerAgent() {
			return managerAgent;
		}
		
		private void setTimeModel(TimeModel newTimeModel) {
			timeModel = newTimeModel;
		}
		private TimeModel getTimeModel() {
			return timeModel;
		}
		private void stepTimeModel() {
			timeModel.step(timeModel);	
		}
		
		private void setEnvironmentInstance(Object envObjectInstance) {
			environmentInstance = envObjectInstance;
		}
		private Object getEnvironmentInstance() {
			return environmentInstance;
		}
		
		private void notifySensors(String topicWhichChanged) {
			localServiceActuator.setChangedAndNotify(topicWhichChanged);
		}
		
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
		public CommandOutgoingFilter() {
			super();
			//setPreferredPosition(2);  // Before the Messaging (encoding) filter and the security related ones
		}
		public final boolean accept(VerticalCommand cmd) {
			
			if (cmd==null) return true;
			//if ( ! cmd.getService().equals(NAME) ) return true;

			String name = cmd.getName();
			//System.out.println( "=> out " + name + " - " + cmd.getService());
			if (name.equals("nothing yet")) {
				// --- Here is nothing to do yet ---------------------

				// Veto the original SEND_MESSAGE command
				return false;
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
		
		public boolean accept(VerticalCommand cmd) {
			
			if (cmd==null) return true;
			String cmdName = cmd.getName();
			//if ( ! name.equals(NAME) ) return true;
			
			//System.out.println( "=> in " + name + " - " + cmd.getService());

			if (myMainContainer != null) {
				if (cmdName.equals(AgentManagementSlice.INFORM_KILLED)) {
					// If the dead agent was registered to some topic, deregister it
					//handleInformKilled(cmd);
				} else if (cmdName.equals(Service.DEAD_SLICE)) {
					// --- If the slice is a SimulationServiceSlice, do something ---
					
				} else if (cmdName.equals(Service.NEW_SLICE)) {
					// --- If the new slice is a SimulationServiceSlice, notify it about the current state ---
					handleNewSlice(cmd);
				}
			}
			else {
				if (cmdName.equals(Service.REATTACHED)) {
					// The Main lost all information related to this container --> Notify it again
					//handleReattached(cmd);
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
	 * If the new slice is a SimulationServiceSlice notify it about the current state
	 */
	private void handleNewSlice(VerticalCommand cmd) {
		
		if (cmd.getService().equals(NAME)) {
			Object[] params = cmd.getParams();
			String newSliceName = (String) params[0];
			try {
				// --- Be sure to get the new (fresh) slice --> Bypass the service cache ---
				SimulationServiceSlice newSlice = (SimulationServiceSlice) getFreshSlice(newSliceName);
				// --- set remote TimeModel ------------------------------------------------
				newSlice.setManagerAgent(managerAgent);
				newSlice.setTimeModel(timeModel);			
				newSlice.setEnvironmentInstance(environmentInstance);				
			}
			catch (Throwable t) {
				myLogger.log(Logger.WARNING, "Error notifying new slice "+newSliceName+" about current SimulationService-State", t);
			}
		}
	}
	
	
}
