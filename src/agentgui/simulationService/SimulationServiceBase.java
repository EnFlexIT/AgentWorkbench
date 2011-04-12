package agentgui.simulationService;

import jade.core.AID;
import jade.core.BaseService;
import jade.core.Location;
import jade.core.Service;
import jade.core.ServiceException;
import jade.util.Logger;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.load.LoadInformation;
import agentgui.simulationService.load.LoadThresholdLevels;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;
import agentgui.simulationService.load.LoadInformation.Container2Wait4;
import agentgui.simulationService.ontology.ClientRemoteContainerReply;
import agentgui.simulationService.ontology.PlatformLoad;
import agentgui.simulationService.ontology.RemoteContainerConfig;
import agentgui.simulationService.sensoring.ServiceActuator;
import agentgui.simulationService.transaction.TransactionMap;

public abstract class SimulationServiceBase extends BaseService {

	// --- This is the Object with all necessary informations about ----------- 
	// --- this slice, which are needed by AgentGUI					-----------
	protected String myContainerMTPurl = null;
	protected ClientRemoteContainerReply myCRCReply = null; 
	public static boolean currentlyWritingFile = false;  
	
	// --- Variables for the Time-Synchronisation -----------------------------
	protected long timeMeasureNext = 0;				// --- When was the MainContainerTime last measured 
	protected long timeMeasureInterval = 1000*5; 	// --- measure every 5 seconds 
	protected int timeMeasureCountMax = 100;		// --- How often the time-difference should be measured to build an average value?
	protected long timeDiff2MainContainer = 0;		// --- Difference between this and the MainContainer-Time
	
	// --- The Agent who is the Manager / Controller of the Simulation --------
	protected AID managerAgent = null;
	// --- The List of Agents, which are registered to this service ----------- 
	protected Hashtable<String,AID> agentList = new Hashtable<String,AID>();
	
	// --- Pause the current simulation ---------------------------------------
	protected boolean pauseSimulation = false; 
	
	// --- The TransactionMap of the simulation -------------------------------
	protected TransactionMap transactionMap = new TransactionMap();
	// --- The current EnvironmentModel ---------------------------------------
	protected EnvironmentModel environmentModel = null;
	// --- The Actuator for this Service, which can inform registered --------- 
	// --- Agents about changes in the Simulation e.g. 'stepTimeModel' --------
	protected ServiceActuator localServiceActuator = new ServiceActuator();
	// --- How should an Agent be notified about Environment-Changes? ---------
	protected boolean stepSimulationAsynchronous = true;
	// --- Default value for starting remote-container ------------------------
	protected boolean DEFAULT_preventUsageOfAlreadyUsedComputers = true;
	// --- The next EnvironmentObject-Instance in parts (answers of agents) ---
	protected Hashtable<AID, Object> environmentInstanceNextParts = new Hashtable<AID, Object>();
	protected Hashtable<AID, Object> environmentInstanceNextPartsLocal = new Hashtable<AID, Object>();
	
	// --- The Load-Information Array of all slices ---------------------------
	protected LoadInformation loadInfo = new LoadInformation(); 
	
	
	/**
	 * Broadcast the AID of the Simulation-Manager Agent to all Slices
	 * @param timeModel 
	 * @param slices
	 * @throws ServiceException
	 */
	protected void broadcastManagerAgent(AID agentAddress, Service.Slice[] slices) throws ServiceException {
		
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
	 * Sends the local next parts of the environment-model to the Main-Container 
	 * @param fromAgent
	 * @param nextPart
	 * @throws ServiceException
	 */
	protected void mainSetEnvironmentInstanceNextPart(Hashtable<AID, Object> nextPartsLocal) throws ServiceException {
		
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
	protected Hashtable<AID, Object> mainGetEnvironmentInstanceNextParts() throws ServiceException {
		
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
	protected void mainResetEnvironmentInstanceNextParts() throws ServiceException {
		
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
	protected void broadcastSetEnvironmentModel(EnvironmentModel envModel, Service.Slice[] slices) throws ServiceException {
		
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
	protected void broadcastStepSimulation(EnvironmentModel envModel, boolean aSynchron, Service.Slice[] slices) throws ServiceException {
		
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
	 * Broadcast a single notification object to a specific agent by using its ServiceSensor
	 * @param agentAID
	 * @param notification
	 * @param aSynchron
	 * @param slices
	 * @throws ServiceException
	 */
	protected boolean broadcastNotifyAgent(AID agentAID, Object notification, boolean aSynchron, Service.Slice[] slices) throws ServiceException {
		
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
				return slice.notifyAgent(agentAID, notification, aSynchron);
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error while sending a notification to agent '" + agentAID.getLocalName() + "' at slice " + sliceName, t);
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
	protected void broadcastPauseSimulation(boolean pauseSimulation, Service.Slice[] slices) throws ServiceException {
		
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
	protected void broadcastAgentMigration(Vector<AID_Container> transferAgents, Service.Slice[] slices) throws ServiceException {
		
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
	 * This mehtods starts an agent on an designate (remote) container
	 * @param nickName
	 * @param agentClass
	 * @param args
	 * @param containerName
	 * @return
	 * @throws ServiceException
	 */
	protected boolean broadcastStartAgent (String nickName, String agentClassName, Object[] args, String containerName) throws ServiceException {
			
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Try to start agent '" + nickName + "' on container " + containerName + "!");
		}
		String sliceName = null;
		try {
			SimulationServiceSlice slice = (SimulationServiceSlice) getSlice(containerName);
			sliceName = slice.getNode().getName();
			if (myLogger.isLoggable(Logger.FINER)) {
				myLogger.log(Logger.FINER, "Start agent '" + nickName + "' on container " + sliceName + "");
			}
			return slice.startAgent(nickName, agentClassName, args);
		}
		catch(Throwable t) {
			// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
			myLogger.log(Logger.WARNING, "Error while trying to get the default remote container configuration from " + sliceName, t);
		}
		return false;
	}
	
	/**
	 * Stops the simulation by invoking the doDelete method at all agents which are 
	 * extending the class 'agentgui.simulationService.agents.SimulationAgent' and 
	 * which are 'connected to a service actuator  
	 * @param slices
	 * @throws ServiceException
	 */
	protected void broadcastStopSimulationAgents(Service.Slice[] slices) throws ServiceException {
		
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
	
	/**
	 * This Methods returns the default Remote-Container-Configuration, coming from the Main-Container
	 * @param slices
	 * @return
	 * @throws ServiceException
	 */
	protected RemoteContainerConfig broadcastGetDefaultRemoteContainerConfig(boolean preventUsageOfAlreadyUsedComputers) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Start request for the default remote container configuration!");
		}
		String sliceName = null;
		try {
			SimulationServiceSlice slice = (SimulationServiceSlice) getSlice(MAIN_SLICE);
			sliceName = slice.getNode().getName();
			if (myLogger.isLoggable(Logger.FINER)) {
				myLogger.log(Logger.FINER, "Start request for the default remote container configuration at container (" + sliceName + ")");
			}
			return slice.getDefaultRemoteContainerConfig(preventUsageOfAlreadyUsedComputers);
		}
		catch(Throwable t) {
			// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
			myLogger.log(Logger.WARNING, "Error while trying to get the default remote container configuration from " + sliceName, t);
		}
		return null;
	}
	
	/**
	 * Broadcast to start a new remote-container for this platform to the Main-Container 
	 * @param slices
	 * @throws ServiceException
	 */
	protected String broadcastStartNewRemoteContainer(RemoteContainerConfig remoteConfig, boolean preventUsageOfAlreadyUsedComputers) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Start a new remote container!");
		}
		String sliceName = null;
		try {
			SimulationServiceSlice slice = (SimulationServiceSlice) getSlice(MAIN_SLICE);
			sliceName = slice.getNode().getName();
			if (myLogger.isLoggable(Logger.FINER)) {
				myLogger.log(Logger.FINER, "Try to start a new remote container (" + sliceName + ")");
			}
			return slice.startNewRemoteContainer(remoteConfig, preventUsageOfAlreadyUsedComputers);
		}
		catch(Throwable t) {
			// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
			myLogger.log(Logger.WARNING, "Error while starting a new remote-container from " + sliceName, t);
		}
		return null;
	}
	
	/**
	 * Broadcast to start a new remote-container for this platform 
	 * @param slices
	 * @throws ServiceException
	 */
	protected Container2Wait4 broadcastGetNewContainer2Wait4Status(String containerName2Wait4) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Start a new remote container!");
		}
		String sliceName = null;
		try {
			SimulationServiceSlice slice = (SimulationServiceSlice) getSlice(MAIN_SLICE);
			sliceName = slice.getNode().getName();
			if (myLogger.isLoggable(Logger.FINER)) {
				myLogger.log(Logger.FINER, "Try to start a new remote container (" + sliceName + ")");
			}
			return slice.getNewContainer2Wait4Status(containerName2Wait4);
		}
		catch(Throwable t) {
			// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
			myLogger.log(Logger.WARNING, "Error while starting a new remote-container from " + sliceName, t);
		}
		return null;
	}
	
	/**
	 * Broadcast to start a new remote-container for this platform 
	 * @param slices
	 * @throws ServiceException
	 */
	protected void broadcastGetContainerLocation(Service.Slice[] slices) throws ServiceException {
		
		loadInfo.containerLocations.clear();
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Try to get Location-Informations!");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Try to get Location-Object for " + sliceName );
				}
				Location cLoc = slice.getLocation();
				loadInfo.containerLocations.put(sliceName, cLoc);
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error while try to get Location-Object from " + sliceName, t);
			}
		}	
	}
	
	/**
	 * Broadcast informtion's of the remote-container (OS etc.) to all remote-container of this platform 
	 * @param slices
	 * @throws ServiceException
	 */
	protected void broadcastPutContainerDescription(Service.Slice[] slices, ClientRemoteContainerReply crcReply) throws ServiceException {
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Sending remote container Information!");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Try sending remote container Information to " + sliceName );
				}
				slice.putContainerDescription(crcReply);
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error while try to send container information to " + sliceName, t);
			}
		}	
	}
	
	/**
	 * Broadcast the set of threshold levels to all container
	 * @param slices
	 * @throws ServiceException
	 */
	protected void broadcastThresholdLevels(Service.Slice[] slices, LoadThresholdLevels thresholdLevels) throws ServiceException {
		
		loadInfo.containerLoads.clear();
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Try to set threshold level to all Containers !");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Try to set threshold level to " + sliceName);
				}
				slice.setThresholdLevels(thresholdLevels);
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error while try to set threshold level to slice " + sliceName, t);
			}
		}		
	}
	
	/**
	 * 'Broadcast' (or receive) all Informations about the containers load
	 * @param slices
	 * @throws ServiceException
	 */
	protected void broadcastMeasureLoad(Service.Slice[] slices) throws ServiceException {
		
		loadInfo.containerLoads.clear();
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Try to get Load-Information from all Containers !");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Try to get Load-Information of " + sliceName);
				}
				PlatformLoad pl = slice.measureLoad();
				loadInfo.containerLoads.put(sliceName, pl);
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error while executing 'MeasureLoad' on slice " + sliceName, t);
			}
		}		
	}
	
	/**
	 * 'Broadcast' (or receive) the list of all agents in a container
	 * @param slices
	 * @throws ServiceException
	 */
	protected void broadcastGetAIDList(Service.Slice[] slices) throws ServiceException {
		
		loadInfo.resetAIDs4Container();
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Try to get AID's from all Containers !");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Try to get AID's from " + sliceName);
				}
				AID[] aid = slice.getAIDList();
				loadInfo.putAIDs4Container(sliceName, aid);
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error while trying to get AID's from " + sliceName, t);
			}
		}
		loadInfo.countAIDs4Container();
	}
	
	/**
	 * 'Broadcast' (or receive) the list of all agents in a container with a registered sensor
	 * @param slices
	 * @throws ServiceException
	 */
	protected void broadcastGetAIDListSensorAgents(Service.Slice[] slices) throws ServiceException {
		
		loadInfo.sensorAgents = new Vector<AID>();
		
		if (myLogger.isLoggable(Logger.CONFIG)) {
			myLogger.log(Logger.CONFIG, "Try to get Sensor-AID's from all Containers !");
		}
		for (int i = 0; i < slices.length; i++) {
			String sliceName = null;
			try {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				sliceName = slice.getNode().getName();
				if (myLogger.isLoggable(Logger.FINER)) {
					myLogger.log(Logger.FINER, "Try to get Sensor-AID's from " + sliceName);
				}
				AID[] aidList = slice.getAIDListSensorAgents();
				loadInfo.sensorAgents.addAll( new Vector<AID>(Arrays.asList(aidList)) );
			}
			catch(Throwable t) {
				// NOTE that slices are always retrieved from the main and not from the cache --> No need to retry in case of failure 
				myLogger.log(Logger.WARNING, "Error while trying to get Sensor-AID's from " + sliceName, t);
			}
		}
	}
	
}
