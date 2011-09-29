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
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;

import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;
import agentgui.simulationService.sensoring.ServiceActuator;
import agentgui.simulationService.sensoring.ServiceActuatorManager;
import agentgui.simulationService.sensoring.ServiceSensor;
import agentgui.simulationService.sensoring.ServiceSensorManager;
import agentgui.simulationService.transaction.EnvironmentManagerDescription;
import agentgui.simulationService.transaction.EnvironmentNotification;
import agentgui.simulationService.transaction.TransactionMap;

/**
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class SimulationService extends BaseService {

	public static final String NAME = SimulationServiceHelper.SERVICE_NAME;
	public static final String SERVICE_NODE_DESCRIPTION_FILE = SimulationServiceHelper.SERVICE_NODE_DESCRIPTION_FILE;
	
	private AgentContainer myContainer;
	private MainContainer myMainContainer;

	private Filter incFilter;
	private Filter outFilter;
	private ServiceComponent localSlice;
	
	// --- Variables for the Time-Synchronisation -----------------------------
	private long timeMeasureNext = 0;				// --- When was the MainContainerTime last measured 
	private long timeMeasureInterval = 1000*5; 	// --- measure every 5 seconds 
	private int timeMeasureCountMax = 100;		// --- How often the time-difference should be measured to build an average value?
	private long timeDiff2MainContainer = 0;		// --- Difference between this and the MainContainer-Time
	
	// --- The Agent who is the Manager / Controller of the Simulation --------
	private EnvironmentManagerDescription environmentManagerDescription = null;
	//private AID managerAgent = null;
	
	// --- The List of Agents, which are registered to this service ----------- 
	private Hashtable<String,AID> agentList = new Hashtable<String,AID>();
	
	// --- Pause the current simulation ---------------------------------------
	private boolean pauseSimulation = false; 
	
	// --- The TransactionMap of the simulation -------------------------------
	private TransactionMap transactionMap = new TransactionMap();
	// --- The current EnvironmentModel ---------------------------------------
	private EnvironmentModel environmentModel = null;
	// --- The Actuator for this Service, which can inform registered --------- 
	// --- Agents about changes in the Simulation e.g. 'stepTimeModel' --------
	private ServiceActuator localServiceActuator = new ServiceActuator();
	private ServiceActuatorManager localServiceActuator4Manager = new ServiceActuatorManager();
	// --- How should an Agent be notified about Environment-Changes? ---------
	private boolean stepSimulationAsynchronous = true;

	// --- The next EnvironmentObject-Instance in parts (answers of agents) ---
	private int environmentInstanceNextPartsExpected = 0;
	private Hashtable<AID, Object> environmentInstanceNextParts = new Hashtable<AID, Object>();
	private Hashtable<AID, Object> environmentInstanceNextPartsLocal = new Hashtable<AID, Object>();
	
		
	public void init(AgentContainer ac, Profile p) throws ProfileException {
		
		super.init(ac, p);
		myContainer = ac;
		myMainContainer = ac.getMain();		
		// --- Create filters -----------------------------
		outFilter = new CommandOutgoingFilter();
		incFilter = new CommandIncomingFilter();
		// --- Create local slice -------------------------
		localSlice = new ServiceComponent();
		
		if (myContainer!=null) {
			if (myLogger.isLoggable(Logger.FINE)) {
				myLogger.log(Logger.FINE, "Starting SimulationService: My-Container: " + myContainer.toString());
			}
		}
		if (myMainContainer!=null) {
			// --- Is !=null, if the Service will start at the Main-Container !!! ----
			if (myLogger.isLoggable(Logger.FINE)) {
				myLogger.log(Logger.FINE, "Main-Container: " + myMainContainer.toString());
			}
		}
		
		// --- Reduce the logging level for Messaging -----
		Logger.getMyLogger("jade.core.messaging.Messaging").setLevel(Level.WARNING);
		Logger.getMyLogger("jade.core.messaging.MessageManager").setLevel(Level.WARNING);
		
	}
	public void boot(Profile p) throws ServiceException {
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
		// --- Methods for the synchronised time --------------------
		public long getSynchTimeDifferenceMillis() throws ServiceException {
			return timeDiff2MainContainer;
		}
		public long getSynchTimeMillis() throws ServiceException {
			if (myMainContainer==null) {
				requestMainContainerTime();	
			}			
			return System.currentTimeMillis() + timeDiff2MainContainer;
		}
		public Date getSynchTimeDate() throws ServiceException {
			return new Date(this.getSynchTimeMillis());
		}
		

		// ----------------------------------------------------------
		// --- Method to set the agent migration --------------------
		public void setAgentMigration(Vector<AID_Container> transferAgents) throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastAgentMigration(transferAgents, slices);
		}
		public void stopSimulationAgents() throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastStopSimulationAgents(slices);
		}
		
		// ----------------------------------------------------------
		// --- Methods for the Manager-Agent ------------------------
		public void setManagerAgent(AID agentAddress) throws ServiceException {
			
			EnvironmentManagerDescription envManager = new EnvironmentManagerDescription(agentAddress, myContainer.here());
			Service.Slice[] slices = getAllSlices();
			broadcastManagerAgent(envManager, slices);
		}		
		public AID getManagerAgent() throws ServiceException {
			if (environmentManagerDescription==null) {
				return null;
			} else {
				return environmentManagerDescription.getAID();	
			}
		}
		
		// ----------------------------------------------------------
		// --- Register, unregister or notify Agents-Sensors --------
		public void sensorPlugIn(ServiceSensor sensor) throws ServiceException {
			localServiceActuator.plugIn(sensor);
		}
		public void sensorPlugOut(ServiceSensor sensor) throws ServiceException {
			localServiceActuator.plugOut(sensor);	
		}		
		
		// ----------------------------------------------------------
		// --- Register, unregister or notify Manager-Sensors -------
		public void sensorPlugIn4Manager(ServiceSensorManager sensor) throws ServiceException {
			localServiceActuator4Manager.plugIn(sensor);
		}
		public void sensorPlugOut4Manager(ServiceSensorManager sensor) throws ServiceException {
			localServiceActuator4Manager.plugOut(sensor);	
		}		

		// ----------------------------------------------------------
		// --- Methods for the Simulation ---------------------------
		public boolean getStepSimulationAsynchronous() throws ServiceException {
			return stepSimulationAsynchronous;
		}
		public void setStepSimulationAsynchronous(boolean stepAsynchronous) throws ServiceException {
			stepSimulationAsynchronous = stepAsynchronous;
		}

		public void stepSimulation(EnvironmentModel envModel, int answersExpected) throws ServiceException {
			this.stepSimulation(envModel, answersExpected, stepSimulationAsynchronous);
		}
		public void stepSimulation(EnvironmentModel envModel, int answersExpected, boolean aSynchron) throws ServiceException {
			if (pauseSimulation==false) {
				// --- step forward the transaction map -------
				transactionMap.put(environmentModel);
				// --- next step for the simulation -----------
				Service.Slice[] slices = getAllSlices();
				if (answersExpected!=environmentInstanceNextPartsExpected) {
					broadcastAnswersExpected(answersExpected, slices);	
				}
				broadcastStepSimulation(envModel, aSynchron, slices);
			}
		}
		public void setPauseSimulation(boolean pause) throws ServiceException {
			// --- block or unblock the simulation ------------
			Service.Slice[] slices = getAllSlices();
			broadcastPauseSimulation(pause, slices);
			if (pause==false) {
				// --- Reset next parts of the environement ---
				this.resetEnvironmentInstanceNextParts();
				// --- Restart simulation  --------------------
				this.stepSimulation(environmentModel, environmentInstanceNextPartsExpected);
			}
		}
			
		public boolean notifySensorAgent(AID agentAID, EnvironmentNotification notification) throws ServiceException {
			if (pauseSimulation==true) {
				return false;
			} else {
				Service.Slice[] slices = getAllSlices();
				return broadcastNotifyAgent(agentAID, notification, slices);	
			}
		}
		public boolean notifyManagerAgent(EnvironmentNotification notification) throws ServiceException {
			if (pauseSimulation==true) {
				return false;
			} else {
				return broadcastNotifyManager(notification);	
			}
		}
		
		public EnvironmentModel getEnvironmentModel() throws ServiceException {
			return environmentModel;
		}
		public void setEnvironmentModel(EnvironmentModel envModel) throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastSetEnvironmentModel(envModel, slices);
		}
		
		// ----------------------------------------------------------
		// --- EnvironmentModel of the next simulation step ---------
		public void setEnvironmentInstanceNextPart(AID fromAgent, Object nextPart) throws ServiceException {

			synchronized(environmentInstanceNextPartsLocal) {
				// --- Put single changes into the local store until ---- 
				// --- the expected number of answers is not reached ----
				environmentInstanceNextPartsLocal.put(fromAgent, nextPart);
				
				// --- If the expected number of answers came back to ---
				// --- the service, broadcast it to every other node ----
				if (environmentInstanceNextPartsLocal.size() >= localServiceActuator.getNoOfSimulationAnswersExpected()) {
					mainSetEnvironmentInstanceNextPart(environmentInstanceNextPartsLocal);	
					environmentInstanceNextPartsLocal = new Hashtable<AID, Object>();
				}
			}
			
		}
		public Hashtable<AID, Object> getEnvironmentInstanceNextParts() throws ServiceException {
			return mainGetEnvironmentInstanceNextParts();
		}
		public void resetEnvironmentInstanceNextParts() throws ServiceException {
			mainResetEnvironmentInstanceNextParts();
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
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error propagating current TimeModel to slice  " + sliceName, t);
			}
		}
	}
	
	/**
	 * Sends the local next parts of the environment-model to the Main-Container 
	 * @param fromAgent
	 * @param nextPart
	 * @throws ServiceException
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
			slice.setEnvironmentInstanceNextPart(environmentInstanceNextPartsLocal);
		}
		catch(Throwable t) {
			// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
			myLogger.log(Logger.WARNING, "Error while sending agent-answer of environment-change to slice  " + sliceName, t);
		}
	}	
	
	/**
	 * This returns the complete environment-model-changes from the Main-Container
	 * @return
	 * @throws ServiceException
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
		}
		catch(Throwable t) {
			// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
			myLogger.log(Logger.WARNING, "Error while trying to get new environment-parts from slice  " + sliceName, t);
		}
		return null;
	}	
	
	/**
	 * This Method resets the hash with the single environment-model-changes
	 * @throws ServiceException
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
		}
		catch(Throwable t) {
			// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
			myLogger.log(Logger.WARNING, "Error while sending reset of environment-change-hash to slice  " + sliceName, t);
		}
	}	
	
	/**
	 * Broadcasts the current EnvironmentModel to all slices
	 * @param envModel 
	 * @param slices
	 * @throws ServiceException
	 */
	private void broadcastSetEnvironmentModel(EnvironmentModel envModel, Service.Slice[] slices) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending new EnvironmentModel !");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Sending new EnvironmentModel to " + sliceName);
				}
				slice.setEnvironmentModel(envModel);
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error while sending the new EnvironmentModel to slice " + sliceName, t);
			}
		}
	}

	/**
	 * Broadcast that all agents have to informed about changes in the EnvironmentModel through his ServiceSensor
	 * @param envModel 
	 * @param aSynchron
	 * @param slices
	 * @throws ServiceException
	 */
	private void broadcastStepSimulation(EnvironmentModel envModel, boolean aSynchron, Service.Slice[] slices) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending new EnvironmentModel + step simulation!");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Sending new EnvironmentModel + step simulation to " + sliceName);
				}
				slice.stepSimulation(envModel, aSynchron);
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error while sending the new EnvironmentModel + step simulation to slice " + sliceName, t);
			}
		}
	}
	/**
	 * Broadcast the number of agent answers that are expected for single simulation step
	 * @param envModel 
	 * @param aSynchron
	 * @param slices
	 * @throws ServiceException
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
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error while sending number of expected agent answers to slice " + sliceName, t);
			}
		}
	}

	/**
	 * Sends the current set of agent answers to the manager agent of the environment 
	 * @param envModel 
	 * @param aSynchron
	 * @param slices
	 * @throws ServiceException
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
				}
				catch(Throwable t) {
					// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
					myLogger.log(Logger.WARNING, "Error while sending agent answers to slice " + sliceName, t);
				}
			}
		}
	}
	
	/**
	 * Broadcast a single notification object to a specific agent by using its ServiceSensor
	 * @param agentAID
	 * @param notification
	 * @param aSynchron
	 * @param slices
	 * @throws ServiceException
	 */
	private boolean broadcastNotifyAgent(AID agentAID, EnvironmentNotification notification, Service.Slice[] slices) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending notfication to agent '" + agentAID.getLocalName() + "'!");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Try sending notfication to agent '" + agentAID.getLocalName() + "' at " + sliceName + "!");
				}
				boolean notified = slice.notifyAgent(agentAID, notification);
				if (notified==true) {
					return notified;	
				}
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error while sending a notification to agent '" + agentAID.getLocalName() + "' at slice " + sliceName, t);
			}
		}
		return false;
	}
	/**
	 * Sends a notification to the manager agent of the environment 
	 * @param envModel 
	 * @param aSynchron
	 * @param slices
	 * @throws ServiceException
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
				}
				catch(Throwable t) {
					// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
					myLogger.log(Logger.WARNING, "Error while sending notification to Manager at slice " + sliceName, t);
				}
			}
		}
		return false;
	}
	
	/**
	 * This method will broadcast that the current simulation should pause or not
	 * @param pauseSimulation
	 * @param slices
	 * @return
	 * @throws ServiceException
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
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error while sending 'pause simulation'-message to slice " + sliceName, t);
			}
		}
	}
	
	/**
	 * Broadcast the new locations to the agents
	 * @param transferAgents 
	 * @param aSynchron
	 * @param slices
	 * @throws ServiceException
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
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error while sending migration notification to agents at slice " + sliceName, t);
			}
		}
	}

	/**
	 * Stops the simulation by invoking the doDelete method at all agents which are 
	 * extending the class 'agentgui.simulationService.agents.SimulationAgent' and 
	 * which are 'connected to a service actuator  
	 * @param slices
	 * @throws ServiceException
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
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error while try to get Location-Object from " + sliceName, t);
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
				if (cmdName.equals(SimulationServiceSlice.SERVICE_SYNCH_GET_REMOTE_TIME)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering Remote-Time-Request");
					}					
					cmd.setReturnValue(getLocalTime());
				}
				else if (cmdName.equals(SimulationServiceSlice.SERVICE_SYNCH_SET_TIME_DIFF)) {
					long timeDifference = (Long) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Setting Time-Difference to Main-Platform");
					}					
					setPlatformTimeDiff(timeDifference);
				}

				else if (cmdName.equals(SimulationServiceSlice.SIM_SET_MANAGER_AGENT)) {
					EnvironmentManagerDescription envManager = (EnvironmentManagerDescription) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received AID of the Agent-Manager");
					}	
					setManagerAgent(envManager);
				}
				
				else if (cmdName.equals(SimulationServiceSlice.SIM_SET_ENVIRONMENT_MODEL)) {
					EnvironmentModel envModel = (EnvironmentModel) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received 'Step Simulation'");
					}	
					setEnvironmentModel(envModel);
				}
				else if (cmdName.equals(SimulationServiceSlice.SIM_STEP_SIMULATION)) {
					EnvironmentModel envModel = (EnvironmentModel) params[0];
					boolean aSynchron = (Boolean) params[1];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received 'Step Simulation'");
					}	
					stepSimulation(envModel, aSynchron);
				}
				else if (cmdName.equals(SimulationServiceSlice.SIM_SET_ANSWERS_EXPECTED)) {
					int answersExpected = (Integer) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received number of expected agent answers'");
					}	
					setEnvironmentInstanceNextPartsExpected(answersExpected);
				}
				else if (cmdName.equals(SimulationServiceSlice.SIM_PAUSE_SIMULATION)) {
					boolean pause = (Boolean) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Getting 'pause-simulation'-message");
					}	
					setPauseSimulation(pause);					
				} 
				
				else if (cmdName.equals(SimulationServiceSlice.SIM_NOTIFY_AGENT)) {
					AID agentAID = (AID) params[0];
					EnvironmentNotification notification = (EnvironmentNotification) params[1];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Received 'Notify Agent for '" + agentAID.getLocalName() + "'");
					}	
					cmd.setReturnValue(notifySensorAgent(agentAID, notification));
				}
				else if (cmdName.equals(SimulationServiceSlice.SIM_NOTIFY_MANAGER)) {
					EnvironmentNotification notification = (EnvironmentNotification) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Getting parts for the next environment model");
					}	
					cmd.setReturnValue(notifyManagerAgent(notification));									
				} 
				else if (cmdName.equals(SimulationServiceSlice.SIM_NOTIFY_MANAGER_PUT_AGENT_ANSWERS)) {
					@SuppressWarnings("unchecked")
					Hashtable<AID, Object> allAgentAnswers = (Hashtable<AID, Object>) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Getting parts for the next environment model");
					}	
					notifyManagerAgentPutAgentAnswers(allAgentAnswers);									
				} 
				
				else if (cmdName.equals(SimulationServiceSlice.SIM_SET_ENVIRONMENT_NEXT_PART)) {
					@SuppressWarnings("unchecked")
					Hashtable<AID, Object> nextPartsLocal = (Hashtable<AID, Object>) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Getting parts for the next environment model");
					}	
					setEnvironmentInstanceNextPart(nextPartsLocal);					
				} 
				else if (cmdName.equals(SimulationServiceSlice.SIM_GET_ENVIRONMENT_NEXT_PARTS)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering request for the next parts of the environment-model" );
					}	
					cmd.setReturnValue(getEnvironmentInstanceNextParts());					
				}
				else if (cmdName.equals(SimulationServiceSlice.SIM_RESET_ENVIRONMENT_NEXT_PARTS)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Reseting next parts of the environment-model" );
					}	
					resetEnvironmentInstanceNextParts();					
				}
				
				else if (cmdName.equals(SimulationServiceSlice.SERVICE_STOP_SIMULATION_AGENTS)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Stoping simulation agents in this container");
					}
					stopSimulationAgents();
				}
				else if (cmdName.equals(SimulationServiceSlice.SERVICE_SET_AGENT_MIGRATION)) {
					@SuppressWarnings("unchecked")
					Vector<AID_Container> transferAgents = (Vector<AID_Container>) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Getting info about agent migration");
					}
					setAgentMigration(transferAgents);
				}
				else if (cmdName.equals(LoadServiceSlice.SERVICE_GET_AID_LIST)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering request for the Agents in this container");
					}
					cmd.setReturnValue(getListOfAgents());
				}
				else if (cmdName.equals(LoadServiceSlice.SERVICE_GET_AID_LIST_SENSOR)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering request for the Agents with sensors in this container");
					}
					cmd.setReturnValue(getListOfAgentsWithSensors());
				}

			}
			catch (Throwable t) {
				cmd.setReturnValue(t);
			}
			return null;
		}
		
		// -----------------------------------------------------------------
		// --- The real functions for the Service Component --- Start ------ 
		// -----------------------------------------------------------------
		private long getLocalTime() {
			return System.currentTimeMillis();
		}
		private void setPlatformTimeDiff(long timeDifference) {
			timeDiff2MainContainer = timeDifference;	
		}
		
		private void setManagerAgent(EnvironmentManagerDescription envManager) {
			environmentManagerDescription = envManager;
		}
		private void setEnvironmentModel(EnvironmentModel newEnvironmentModel) {
			environmentModel = newEnvironmentModel;
			//TODO localServiceActuator.notifySensors(newEnvironmentModel, stepSimulationAsynchronous );
		}
		private void stepSimulation(EnvironmentModel newEnvironmentModel, boolean aSynchron) {
			environmentModel = newEnvironmentModel;
			localServiceActuator.notifySensors(newEnvironmentModel, aSynchron);
		}
		private void setEnvironmentInstanceNextPartsExpected(int answersExpected) {
			environmentInstanceNextPartsExpected=answersExpected;
		}		
		private void notifyManagerAgentPutAgentAnswers(Hashtable<AID, Object> allAgentAnswers) {
			localServiceActuator4Manager.putAgentAnswers(environmentManagerDescription.getAID(), allAgentAnswers, true);
		}
		private boolean notifyManagerAgent(EnvironmentNotification notification) {
			return localServiceActuator4Manager.notifyManager(environmentManagerDescription.getAID(), notification);
		}		
		private boolean notifySensorAgent(AID agentAID, EnvironmentNotification notification){
			return localServiceActuator.notifySensorAgent(agentAID, notification);
		}
		private void setPauseSimulation(boolean pause) {
			pauseSimulation = pause;
		}
		
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
		private Hashtable<AID, Object> getEnvironmentInstanceNextParts() {
			return environmentInstanceNextParts;
		}
		private void resetEnvironmentInstanceNextParts() {
			environmentInstanceNextParts = new Hashtable<AID, Object>();
			environmentInstanceNextPartsLocal = new Hashtable<AID, Object>();
		}
		
		private void stopSimulationAgents() {
			localServiceActuator.notifySensorAgentsDoDelete();
		}
		private void setAgentMigration(Vector<AID_Container> transferAgents) {
			localServiceActuator.setMigration(transferAgents);
		}
		private AID[] getListOfAgents() {
			return myContainer.agentNames();
		}
		private AID[] getListOfAgentsWithSensors() {
			return localServiceActuator.getSensorAgents();
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
		public CommandOutgoingFilter() {
			super();
			//setPreferredPosition(2);  // Before the Messaging (encoding) filter and the security related ones
		}
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
	 * If the new slice is a SimulationServiceSlice notify it about the current state
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
				newSlice.setEnvironmentModel(environmentModel);	
				// --- Synchronise the time ------------------------------------------------
				this.synchTimeOfSlice(newSlice);
				
			}
			catch (Throwable t) {
				myLogger.log(Logger.WARNING, "Error notifying new slice "+newSliceName+" about current SimulationService-State", t);
			}
		}
	}
	
	/**
	 * This method will synchronise the time between Main-Container and Remote-Container 
	 * @param slice
	 */
	private void synchTimeOfSlice(SimulationServiceSlice slice) {
		
		int countMax = timeMeasureCountMax;
		long locTime1Milli = 0;
		long remTimeMilli = 0;

		long locTime1Nano = 0;
		long locTime2Nano = 0;
		
		double timeDiffAccumulate = 0;
		
		try {
	
			for (int i=0; i<countMax; i++) {
				// --- Measure local time and ask for the remote time ---
				locTime1Milli = System.currentTimeMillis();								// --- milli-seconds ---
				locTime1Nano = System.nanoTime();										// --- nano-seconds ---
				remTimeMilli = slice.getRemoteTime();									// --- milli-seconds ---
				locTime2Nano = System.nanoTime();										// --- nano-seconds ---
				
				timeDiffAccumulate += getContainerTimeDifference(locTime1Milli, remTimeMilli, locTime1Nano, locTime2Nano);

			}
			slice.setRemoteTimeDiff(Math.round(timeDiffAccumulate / countMax));	
			
		} catch (IMTPException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This Method returns the time difference between this and the remote node
	 * by using the local-time and the time to get the remote-time
	 * @param locTime1Milli
	 * @param remTimeMilli
	 * @param locTime1Nano
	 * @param locTime2Nano
	 * @return
	 */
	private double getContainerTimeDifference(long locTime1Milli, long remTimeMilli, long locTime1Nano, long locTime2Nano) {
		
		// --- Calculate the correction value of the remote time
		long measureTimeNanoCorrect = (locTime2Nano - locTime1Nano)/2;
		double measureTimeMilliCorrect = measureTimeNanoCorrect * Math.pow(10, -6);;
		// --- Correct the measured remote time -----------------
		double remTimeMilliCorrect = remTimeMilli - measureTimeMilliCorrect;
		// --- Calculate the time Difference between ------------
		// --- this and the remote platform 		 ------------
		return locTime1Milli - remTimeMilliCorrect;
	}
	/**
	 * This method asks the MainContainer of his local time 
	 * and stores all important informations in this class 
	 * @return
	 */
	private void requestMainContainerTime() {
		
		int counter = timeMeasureCountMax;
		long locTime1Milli = 0;
		long remTimeMilli = 0;
		
		long locTime1Nano = 0;
		long locTime2Nano = 0;
	
		double remTimeMilliDiffAccumulate = 0;
		
		if (System.currentTimeMillis() >= timeMeasureNext ) {
			// --- Balance the Main-Container time-differnece ------------
			timeMeasureNext =  System.currentTimeMillis() + timeMeasureInterval;
			SimulationServiceSlice ssl;
			try {
				ssl = (SimulationServiceSlice) getSlice(MAIN_SLICE);
				
				for (int i=0; i<counter; i++) {
					// --- Measure local time and ask for the remote time ---
					locTime1Milli = System.currentTimeMillis();
					locTime1Nano = System.nanoTime();										// --- nano-seconds ---
					remTimeMilli = ssl.getRemoteTime(); 
					locTime2Nano = System.nanoTime();										// --- nano-seconds ---
					
					remTimeMilliDiffAccumulate += getContainerTimeDifference(locTime1Milli, remTimeMilli, locTime1Nano, locTime2Nano);
				}
				timeDiff2MainContainer = Math.round(remTimeMilliDiffAccumulate/counter) * (-1);	

			} catch (ServiceException e) {
				e.printStackTrace();
			} catch (IMTPException e) {
				e.printStackTrace();
			}
		} 		
	}
	
		
}
