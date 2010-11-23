package agentgui.simulationService;

import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.AgentContainer;
import jade.core.BaseService;
import jade.core.ContainerID;
import jade.core.Filter;
import jade.core.HorizontalCommand;
import jade.core.IMTPException;
import jade.core.Location;
import jade.core.MainContainer;
import jade.core.NameClashException;
import jade.core.Node;
import jade.core.NotFoundException;
import jade.core.Profile;
import jade.core.ProfileException;
import jade.core.Service;
import jade.core.ServiceDescriptor;
import jade.core.ServiceException;
import jade.core.ServiceHelper;
import jade.core.VerticalCommand;
import jade.core.management.AgentManagementSlice;
import jade.core.messaging.MessagingSlice;
import jade.core.mobility.AgentMobilityHelper;
import jade.lang.acl.ACLMessage;
import jade.security.JADESecurityException;
import jade.util.Logger;
import jade.util.ObjectManager;
import jade.util.leap.ArrayList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import agentgui.core.application.Application;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.load.LoadAgentMap;
import agentgui.simulationService.load.LoadInformation;
import agentgui.simulationService.load.LoadMeasureSigar;
import agentgui.simulationService.load.LoadMeasureThread;
import agentgui.simulationService.load.LoadThresholdLevels;
import agentgui.simulationService.load.LoadUnits;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;
import agentgui.simulationService.load.LoadInformation.Container2Wait4;
import agentgui.simulationService.load.LoadInformation.NodeDescription;
import agentgui.simulationService.ontology.AgentGUI_DistributionOntology;
import agentgui.simulationService.ontology.BenchmarkResult;
import agentgui.simulationService.ontology.ClientRemoteContainerReply;
import agentgui.simulationService.ontology.ClientRemoteContainerRequest;
import agentgui.simulationService.ontology.OSInfo;
import agentgui.simulationService.ontology.PlatformAddress;
import agentgui.simulationService.ontology.PlatformLoad;
import agentgui.simulationService.ontology.PlatformPerformance;
import agentgui.simulationService.ontology.RemoteContainerConfig;
import agentgui.simulationService.sensoring.ServiceActuator;
import agentgui.simulationService.sensoring.ServiceSensor;
import agentgui.simulationService.transaction.TransactionMap;


/**
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public class SimulationService extends BaseService {

	public static final String NAME = SimulationServiceHelper.SERVICE_NAME;
	public static final String SERVICE_NODE_DESCRIPTION_FILE = SimulationServiceHelper.SERVICE_NODE_DESCRIPTION_FILE;
	
	private AgentContainer myContainer;
	private MainContainer myMainContainer;

	private Filter incFilter;
	private Filter outFilter;
	private ServiceComponent localSlice;
	
	// --- This is the Object with all necessary informations about ----------- 
	// --- this slice, which are needed by AgentGUI					-----------
	private String myContainerMTPurl = null;
	private ClientRemoteContainerReply myCRCReply = null; 
	public static boolean currentlyWritingFile = false;  
	
	// --- Variables for the Time-Synchronisation -----------------------------
	private long timeMeasureNext = 0;				// --- When was the MainContainerTime last measured 
	private long timeMeasureInterval = 1000*5; 		// --- measure every 5 seconds 
	private int timeMeasureCountMax = 100;			// --- How often the time-difference should be measured to build an average value?
	private long timeDiff2MainContainer = 0;		// --- Difference between this and the MainContainer-Time
	
	// --- The Agent who is the Manager / Controller of the Simulation --------
	private AID managerAgent = null;
	// --- The List of Agents, which are registered to this service ----------- 
	private Hashtable<String,AID> agentList = new Hashtable<String,AID>();
	
	// --- The TransactionMap of the simulation -------------------------------
	private TransactionMap transactionMap = new TransactionMap();
	// --- The current EnvironmentModel ---------------------------------------
	private EnvironmentModel environmentModel = null;
	// --- The Actuator for this Service, which can inform registered --------- 
	// --- Agents about changes in the Simulation e.g. 'stepTimeModel' --------
	private ServiceActuator localServiceActuator = new ServiceActuator();
	// --- How should an Agent be notified about Environment-Changes? ---------
	private boolean stepSimulationAsynchronous = true;
	// --- Default value for starting remote-container ------------------------
	private boolean DEFAULT_preventUsageOfAlreadyUsedComputers = true;
	// --- The next EnvironmentObject-Instance in parts (answers of agents) ---
	private Hashtable<AID, Object> environmentInstanceNextParts = new Hashtable<AID, Object>();
	private Hashtable<AID, Object> environmentInstanceNextPartsLocal = new Hashtable<AID, Object>();
	
	// --- The Load-Information Array of all slices ---------------------------
	private LoadInformation loadInfo = new LoadInformation(); 
	
	
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
		// --- Start the Load-Measurements on this Node ---
		new LoadMeasureThread().start();  
		
		// --- Reduce the logging level for Messaging -----
		Logger.getMyLogger("jade.core.messaging.Messaging").setLevel(Level.WARNING);
		Logger.getMyLogger("jade.core.messaging.MessageManager").setLevel(Level.WARNING);
		
	}
	public void boot(Profile p) throws ServiceException {
		super.boot(p);
		if (myMainContainer==null) {
			setLocalCRCReply(true);
		}
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

		public boolean startAgent(String nickName, String agentClassName, Object[] args, String containerName) throws ServiceException {
			return broadcastStartAgent(nickName, agentClassName, args, containerName);
		}
		public void stopSimulationAgents() throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastStopSimulationAgents(slices);
		}
		
		// ----------------------------------------------------------
		// --- Methods to start a new remote-container -------------- 
		public RemoteContainerConfig getDefaultRemoteContainerConfig() throws ServiceException {
			return this.getDefaultRemoteContainerConfig(DEFAULT_preventUsageOfAlreadyUsedComputers);
		}
		public RemoteContainerConfig getDefaultRemoteContainerConfig(boolean preventUsageOfAlreadyUsedComputers) throws ServiceException {
			return broadcastGetDefaultRemoteContainerConfig(preventUsageOfAlreadyUsedComputers);
		}
		
		public String startNewRemoteContainer() throws ServiceException {
			return this.startNewRemoteContainer(null, DEFAULT_preventUsageOfAlreadyUsedComputers);
		}
		public String startNewRemoteContainer(boolean preventUsageOfAlreadyUsedComputers) throws ServiceException {
			return this.startNewRemoteContainer(null, preventUsageOfAlreadyUsedComputers);
		}
		public String startNewRemoteContainer(RemoteContainerConfig remoteConfig, boolean preventUsageOfAlreadyUsedComputers) throws ServiceException {
			return broadcastStartNewRemoteContainer(remoteConfig, preventUsageOfAlreadyUsedComputers);
		}
		
		public Container2Wait4 startNewRemoteContainerStaus(String containerName) throws ServiceException {
			return broadcastGetNewContainer2Wait4Status(containerName);
		}
		
		// ----------------------------------------------------------
		// --- Methods to set the local description of this node ----
		// --- which is stored in the file 'AgentGUINode.bin'    ----
		public ClientRemoteContainerReply getLocalCRCReply() throws ServiceException {
			return myCRCReply;
		}
		public void setAndSaveCRCReplyLocal(ClientRemoteContainerReply crcReply) throws ServiceException {
			myCRCReply = crcReply;
			saveCRCReply(myCRCReply);
		}

		// ----------------------------------------------------------
		// --- Methods for container info about OS, benchmark etc. -- 
		public void putContainerDescription(ClientRemoteContainerReply crcReply) throws ServiceException {
			if (crcReply.getRemoteAddress()==null && crcReply.getRemoteOS()==null && crcReply.getRemotePerformance()==null && crcReply.getRemoteBenchmarkResult()==null) {
				// --- RemoteContainerRequest WAS NOT successful ----
				loadInfo.setNewContainerCancelled(crcReply.getRemoteContainerName());
			} else {
				Service.Slice[] slices = getAllSlices();
				broadcastPutContainerDescription(slices, crcReply);	
			}
		}
		public Hashtable<String, NodeDescription> getContainerDescriptions() throws ServiceException {
			return loadInfo.containerDescription;
		}
		public NodeDescription getContainerDescription(String containerName) throws ServiceException {
			return loadInfo.containerDescription.get(containerName);
		}
		
		// ----------------------------------------------------------
		// --- Method for getting Location-Objects ------------------ 
		public Hashtable<String, Location> getContainerLocations() throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastGetContainerLocation(slices);
			return loadInfo.containerLocations;
		}
		public Location getContainerLocation(String containerName) throws ServiceException {
			this.getContainerLocations();
			return loadInfo.containerLocations.get(containerName);
		}
		
		// ----------------------------------------------------------
		// --- Method to get the Load-Informations of all containers 
		public void setThresholdLevels(LoadThresholdLevels thresholdLevels) throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastThresholdLevels(slices, thresholdLevels);
		}
		public Hashtable<String, PlatformLoad> getContainerLoads() throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastMeasureLoad(slices);
			return loadInfo.containerLoads;
		}
		public PlatformLoad getContainerLoad(String containerName) throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastMeasureLoad(slices);
			return loadInfo.containerLoads.get(containerName);
		}
		
		public Vector<String> getContainerQueue() throws ServiceException {
			return loadInfo.containerQueue;
		}
		
		// ----------------------------------------------------------
		// --- Method to get positions Agents at this platform ------ 
		public LoadAgentMap getAgentMap() throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastGetAIDListSensorAgents(slices);
			broadcastGetAIDList(slices);
			return loadInfo.agentLocations;
		}
		
		// ----------------------------------------------------------
		// --- Method to set the agent migration --------------------
		public void setAgentMigration(Vector<AID_Container> transferAgents) throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastAgentMigration(transferAgents, slices);
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
		// --- Register, unregister or notify Agents-Sensors --------
		public void sensorPlugIn(ServiceSensor sensor) throws ServiceException {
			localServiceActuator.plugIn(sensor);
		}
		public void sensorPlugOut(ServiceSensor sensor) throws ServiceException {
			localServiceActuator.plugOut(sensor);	
		}		
		
		// ----------------------------------------------------------
		// --- Methods for the Simulation ---------------------------
		public boolean getStepSimulationAsynchronous() throws ServiceException {
			return stepSimulationAsynchronous;
		}
		public void setStepSimulationAsynchronous(boolean stepAsynchronous) throws ServiceException {
			stepSimulationAsynchronous = stepAsynchronous;
		}

		public void stepSimulation(EnvironmentModel envModel) throws ServiceException {
			this.stepSimulation(envModel, stepSimulationAsynchronous);
		}
		public void stepSimulation(EnvironmentModel envModel, boolean aSynchron) throws ServiceException {
			Service.Slice[] slices = getAllSlices();
			broadcastStepSimulation(envModel, aSynchron, slices);
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
	 * This mehtods starts an agent on an designate (remote) container
	 * @param nickName
	 * @param agentClass
	 * @param args
	 * @param containerName
	 * @return
	 * @throws ServiceException
	 */
	private boolean broadcastStartAgent (String nickName, String agentClassName, Object[] args, String containerName) throws ServiceException {
			
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
	
	/**
	 * This Methods returns the default Remote-Container-Configuration, coming from the Main-Container
	 * @param slices
	 * @return
	 * @throws ServiceException
	 */
	private RemoteContainerConfig broadcastGetDefaultRemoteContainerConfig(boolean preventUsageOfAlreadyUsedComputers) throws ServiceException {
		
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
	private String broadcastStartNewRemoteContainer(RemoteContainerConfig remoteConfig, boolean preventUsageOfAlreadyUsedComputers) throws ServiceException {
		
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
	private Container2Wait4 broadcastGetNewContainer2Wait4Status(String containerName2Wait4) throws ServiceException {
		
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
	private void broadcastGetContainerLocation(Service.Slice[] slices) throws ServiceException {
		
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
	 * <Broadcast informtions of the remote-container (OS etc.) to all remote-container of this platform 
	 * @param slices
	 * @throws ServiceException
	 */
	private void broadcastPutContainerDescription(Service.Slice[] slices, ClientRemoteContainerReply crcReply) throws ServiceException {
		
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
	 * Broadcast the set of threshold levels to all cntainer
	 * @param slices
	 * @throws ServiceException
	 */
	private void broadcastThresholdLevels(Service.Slice[] slices, LoadThresholdLevels thresholdLevels) throws ServiceException {
		
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
	private void broadcastMeasureLoad(Service.Slice[] slices) throws ServiceException {
		
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
	private void broadcastGetAIDList(Service.Slice[] slices) throws ServiceException {
		
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
	private void broadcastGetAIDListSensorAgents(Service.Slice[] slices) throws ServiceException {
		
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
				
				
				else if (cmdName.equals(SimulationServiceSlice.SERVICE_START_AGENT)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Starting a new agent on this platform");
					}
					String nickName = (String) params[0];
					String agentClassName = (String) params[1];
					Object[] args = (Object[]) params[2];
					cmd.setReturnValue( startAgent(nickName, agentClassName, args) );
				}
				else if (cmdName.equals(SimulationServiceSlice.SERVICE_STOP_SIMULATION_AGENTS)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Stoping simulation agents in this container");
					}
					stopSimulationAgents();
				}
				else if (cmdName.equals(SimulationServiceSlice.SERVICE_START_NEW_REMOTE_CONTAINER)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Starting a new remote-container for this platform");
					}
					RemoteContainerConfig remoteConfig = (RemoteContainerConfig) params[0];
					boolean preventUsageOfAlreadyUsedComputers = (Boolean) params[1];
					cmd.setReturnValue(startRemoteContainer(remoteConfig, preventUsageOfAlreadyUsedComputers));
				}
				else if (cmdName.equals(SimulationServiceSlice.SERVICE_GET_DEFAULT_REMOTE_CONTAINER_CONFIG)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering to request for 'get_default_remote_container_config'");
					}
					boolean preventUsageOfAlreadyUsedComputers = (Boolean) params[0];
					cmd.setReturnValue(getDefaultRemoteContainerConfig(preventUsageOfAlreadyUsedComputers));
				}
				else if (cmdName.equals(SimulationServiceSlice.SERVICE_GET_NEW_CONTAINER_2_WAIT_4_STATUS)) {
					String container2Wait4 = (String) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering request for new container status of container '" + container2Wait4 + "'");
					}
					cmd.setReturnValue(getNewContainer2Wait4Status(container2Wait4));
				}
				
				else if (cmdName.equals(SimulationServiceSlice.SERVICE_GET_LOCATION)) {
					cmd.setReturnValue(myContainer.here());
				}

				else if (cmdName.equals(SimulationServiceSlice.SERVICE_SET_THRESHOLD_LEVEL)) {
					LoadThresholdLevels thresholdLevels = (LoadThresholdLevels) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Getting new threshold levels for load");
					}
					setThresholdLevels(thresholdLevels);
				}
				else if (cmdName.equals(SimulationServiceSlice.SERVICE_MEASURE_LOAD)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering request for Container-Load");
					}
					cmd.setReturnValue(measureLoad());
				}
				else if (cmdName.equals(SimulationServiceSlice.SERVICE_GET_AID_LIST)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering request for the Agents in this container");
					}
					cmd.setReturnValue(getListOfAgents());
				}
				else if (cmdName.equals(SimulationServiceSlice.SERVICE_GET_AID_LIST_SENSOR)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering request for the Agents with sensors in this container");
					}
					cmd.setReturnValue(getListOfAgentsWithSensors());
				}

				else if (cmdName.equals(SimulationServiceSlice.SERVICE_SET_AGENT_MIGRATION)) {
					@SuppressWarnings("unchecked")
					Vector<AID_Container> transferAgents = (Vector<AID_Container>) params[0];
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Getting info about agent migration");
					}
					setAgentMigration(transferAgents);
				}
				
				else if (cmdName.equals(SimulationServiceSlice.SERVICE_PUT_CONTAINER_DESCRIPTION)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Putting in container description");
					}
					putContainerDescription((ClientRemoteContainerReply) params[0]);
				}
				else if (cmdName.equals(SimulationServiceSlice.SERVICE_GET_CONTAINER_DESCRIPTION)) {
					if (myLogger.isLoggable(Logger.FINE)) {
						myLogger.log(Logger.FINE, "Answering request for container description");
					}
					cmd.setReturnValue(getContainerDescription());
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
		
		private void setManagerAgent(AID agentAddress) {
			managerAgent = agentAddress;
		}
		private AID getManagerAgent() {
			return managerAgent;
		}
		
		private void setEnvironmentModel(EnvironmentModel newEnvironmentModel) {
			environmentModel = newEnvironmentModel;
		}
		private void stepSimulation(EnvironmentModel newEnvironmentModel, boolean aSynchron) {
			environmentModel = newEnvironmentModel;
			localServiceActuator.notifySensors(newEnvironmentModel, aSynchron);
		}

		private void setEnvironmentInstanceNextPart(Hashtable<AID, Object> nextPartsLocal) {
			environmentInstanceNextParts.putAll(nextPartsLocal);
		}
		private Hashtable<AID, Object> getEnvironmentInstanceNextParts() {
			return environmentInstanceNextParts;
		}
		private void resetEnvironmentInstanceNextParts() {
			environmentInstanceNextParts = new Hashtable<AID, Object>();
			environmentInstanceNextPartsLocal = new Hashtable<AID, Object>();
		}
		
		private boolean startAgent(String nickName, String agentClassName, Object[] args) {
			
			AID agentID = new AID();
			agentID.setLocalName(nickName);
			
			try {
				Agent agent = (Agent) ObjectManager.load(agentClassName, ObjectManager.AGENT_TYPE);
				if (agent == null) {
					agent = (Agent)Class.forName(agentClassName).newInstance();
				}
				agent.setArguments(args);
				myContainer.initAgent(agentID, agent, null, null);
				myContainer.powerUpLocalAgent(agentID);
				
			} catch (IMTPException e) {
				e.printStackTrace();
			} catch (NameClashException e) {
				e.printStackTrace();
			} catch (NotFoundException e) {
				e.printStackTrace();
			} catch (JADESecurityException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return true;
		}
		private void stopSimulationAgents() {
			localServiceActuator.notifySensorAgentsDoDelete();
		}
		private String startRemoteContainer(RemoteContainerConfig remoteConfig, boolean preventUsageOfAlreadyUsedComputers) {
			return sendMsgRemoteContainerRequest(remoteConfig, preventUsageOfAlreadyUsedComputers);
		}
		private RemoteContainerConfig getDefaultRemoteContainerConfig(boolean preventUsageOfAlreadyUsedComputers) {
			return getRemoteContainerConfigDefault(preventUsageOfAlreadyUsedComputers);
		}
		private Container2Wait4 getNewContainer2Wait4Status(String container2Wait4) {
			return loadInfo.getNewContainer2Wait4Status(container2Wait4);
		}
		
		private void setThresholdLevels(LoadThresholdLevels thresholdLevels) {
			LoadMeasureThread.setThresholdLevels(thresholdLevels);
		}
		private PlatformLoad measureLoad() {
			PlatformLoad pl = new PlatformLoad();
			pl.setLoadCPU(LoadMeasureThread.getLoadCPU());
			pl.setLoadMemorySystem(LoadMeasureThread.getLoadMemorySystem());
			pl.setLoadMemoryJVM(LoadMeasureThread.getLoadMemoryJVM());
			pl.setLoadNoThreads(LoadMeasureThread.getLoadNoThreads());
			pl.setLoadExceeded(LoadMeasureThread.getThresholdLevelesExceeded());
			return pl;
		}
		private AID[] getListOfAgents() {
			return myContainer.agentNames();
		}
		private AID[] getListOfAgentsWithSensors() {
			return localServiceActuator.getSensorAgents();
		}
		private void setAgentMigration(Vector<AID_Container> transferAgents) {
			localServiceActuator.setMigration(transferAgents);
		}
		
		private void putContainerDescription(ClientRemoteContainerReply crcReply) {
			loadInfo.putContainerDescription(crcReply);
		}
		private ClientRemoteContainerReply getContainerDescription() {
			return myCRCReply;
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
			
			if (cmdName.equals(MessagingSlice.SET_PLATFORM_ADDRESSES) && myContainerMTPurl==null ) {
				// --- Handle that the MTP-Address was created ------
				Object[] params = cmd.getParams();
				AID aid = (AID)params[0];
				String[] aidArr = aid.getAddressesArray();
				if (aidArr.length!=0) {
					myContainerMTPurl = aidArr[0];
					setLocalCRCReply(false);
				}
				// Veto the original SEND_MESSAGE command, if needed
				// return false;
			} else if (cmdName.equals(AgentManagementSlice.KILL_CONTAINER)) {
				Object[] params = cmd.getParams();
				ContainerID id = (ContainerID) params[0];
				String containerName = id.getName();
				loadInfo.containerLoads.remove(containerName);
				loadInfo.containerLocations.remove(containerName);
			} else if (cmdName.equals(AgentMobilityHelper.INFORM_MOVED)) {
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
				// --- Is this the slice, we have waited for? ------------------------------
				loadInfo.setNewContainerStarted(newSliceName);
				// --- Be sure to get the new (fresh) slice --> Bypass the service cache ---
				SimulationServiceSlice newSlice = (SimulationServiceSlice) getFreshSlice(newSliceName);
				// --- Set remote ManagerAgent, TimeModel,EnvironmentInstance --------------
				newSlice.setThresholdLevels(LoadMeasureThread.getThresholdLevels());
				newSlice.setManagerAgent(managerAgent);
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
	
	/**
	 * This method returns a default configuration for a new remote container
	 * @return
	 */
	private RemoteContainerConfig getRemoteContainerConfigDefault(boolean preventUsageOfAlreadyUsedComputers) {
		
		// --- Variable for the new container name ------------------
		String newContainerPrefix = "remote";
		Integer newContainerNo = 0;
		String newContainerName = null;
		
		// --- Get the local IP-Address -----------------------------
		String myIP = myContainer.getNodeDescriptor().getContainer().getAddress();
		// --- Get the local port of JADE ---------------------------
		String myPort = myContainer.getNodeDescriptor().getContainer().getPort();
	
		// --- Get the List of services started here ----------------
		String myServices = "";
		List<?> services = myContainer.getServiceManager().getLocalServices();
		Iterator<?> it = services.iterator();
		while (it.hasNext()) {
			ServiceDescriptor serviceDesc = (ServiceDescriptor) it.next();
			String service = serviceDesc.getService().getClass().getName() + ";";
			myServices += service;				
		}			
		
		// --- Define the new container name ------------------------
		try {
			Service.Slice[] slices = getAllSlices();
			for (int i = 0; i < slices.length; i++) {
				SimulationServiceSlice slice = (SimulationServiceSlice) slices[i];
				String sliceName = slice.getNode().getName();
				if (sliceName.startsWith(newContainerPrefix)) {
					String endString = sliceName.replace(newContainerPrefix, "");
					Integer endNumber = Integer.parseInt(endString);
					if (endNumber>newContainerNo) {
						newContainerNo = endNumber;
					}
				}
			}	
		} catch (ServiceException errSlices) {
			errSlices.printStackTrace();
		}
		newContainerNo++;
		newContainerName = newContainerPrefix + newContainerNo;
		
		// --- If defined, find external jar-Files for ClassPath ----
		jade.util.leap.List extJars = null;
		if (Application.ProjectCurr!=null) {
			for (String link : Application.ProjectCurr.downloadResources) {
				if (extJars == null) {
					extJars = new ArrayList();
				}
				extJars.add(link);
			}
		}
		
		// --- Find machines, which should be excluded for a remote start -----
		jade.util.leap.List hostExcludeIP = null;
		for (String containerName : loadInfo.containerDescription.keySet()) {
			
			NodeDescription nodeDesc = loadInfo.containerDescription.get(containerName);
			String ip = nodeDesc.getPlAddress().getIp();
			if (hostExcludeIP == null) {
				hostExcludeIP = new ArrayList();
			}
			hostExcludeIP.add(ip);
		}
		
		// --- For the Jade-Logger with love ;-) --------------------
		if (myLogger.isLoggable(Logger.FINE)) {
			myLogger.log(Logger.FINE, "-- Infos to start the remote container ------------");
			myLogger.log(Logger.FINE, "=> Services2Start:   " + myServices);
			myLogger.log(Logger.FINE, "=> NewContainerName: " + newContainerName);
			myLogger.log(Logger.FINE, "=> ThisAddresses:    " + myIP +  " - Port: " + myPort);
		}
		
		// --- Define the 'RemoteContainerConfig' - Object ----------
		RemoteContainerConfig remConf = new RemoteContainerConfig();
		remConf.setJadeServices(myServices);
		remConf.setJadeIsRemoteContainer(true);
		remConf.setJadeHost(myIP);
		remConf.setJadePort(myPort);
		remConf.setJadeContainerName(newContainerName);
		remConf.setJadeShowGUI(true);
		remConf.setJadeJarIncludeList(extJars);
		if (preventUsageOfAlreadyUsedComputers) {
			remConf.setHostExcludeIP(hostExcludeIP);	
		}			
		return remConf;
	}
	
	/**
	 * This method configures and send a ACLMessage to start a new remote-Container
	 * @param remConf, a RemoteContainerConfig-Object
	 */
	private String sendMsgRemoteContainerRequest(RemoteContainerConfig remConf, boolean preventUsageOfAlreadyUsedComputers) {
		
		// --- Get the local Address of JADE ------------------------
		String myPlatformAddress = myContainer.getPlatformID();
		
		// --- If the remote-configuration is null configure it now -
		if (remConf==null) {
			remConf = this.getRemoteContainerConfigDefault(preventUsageOfAlreadyUsedComputers);
		}
		
		// --- Define the AgentAction -------------------------------
		ClientRemoteContainerRequest req = new ClientRemoteContainerRequest();
		req.setRemoteConfig(remConf);
		
		Action act = new Action();
		act.setActor(myContainer.getAMS());
		act.setAction(req);

		// --- Define receiver of the Message ----------------------- 
		AID agentGUIAgent = new AID("server.client" + "@" + myPlatformAddress, AID.ISGUID );
		//mainPlatformAgent.addAddresses(mainPlatform.getHttp4mtp());
		
		// --- Build Message ----------------------------------------
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setSender(myContainer.getAMS());
		msg.addReceiver(agentGUIAgent);		
		msg.setLanguage(new SLCodec().getName());
		msg.setOntology(AgentGUI_DistributionOntology.getInstance().getName());
		try {
			msg.setContentObject(act);
		} catch (IOException errCont) {
			errCont.printStackTrace();
		}

		// --- Send message -----------------------------------------
		myContainer.postMessageToLocalAgent(msg, agentGUIAgent);
		
		// --- Remind, that we're waiting for this container --------
		loadInfo.setNewContainer2Wait4(remConf.getJadeContainerName());
		
		// --- Return -----------------------------------------------
		return remConf.getJadeContainerName();
	}
	
	/**
	 * Here the local ContainerDescription will be stored on disk
	 * @param crcReply
	 */
	private void saveCRCReply(ClientRemoteContainerReply crcReply) {
		
		SimulationService.currentlyWritingFile = true;
		String mySavingPath = SimulationService.SERVICE_NODE_DESCRIPTION_FILE;
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(mySavingPath));
			out.writeObject(crcReply);
			out.flush();
			out.close();
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		SimulationService.currentlyWritingFile = false;
	}
	/**
	 * This method reads the ContainerDescription from the file to
	 * the local object 'myCRCReply'
	 */
	private ClientRemoteContainerReply loadCRCReply() {
		
		ClientRemoteContainerReply crcReply = null;
		String mySavingPath = SimulationService.SERVICE_NODE_DESCRIPTION_FILE;

		// --- Wait until the file-writing process is finished ------ 
		while (SimulationService.currentlyWritingFile==true) {
			try {
				Thread.sleep(50);				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// --- If the file exists, parse it now ---------------------
		if (new File(mySavingPath).exists()) {
			
			try {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(mySavingPath));
				crcReply = (ClientRemoteContainerReply) in.readObject();
				in.close();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			// --- Set informations, which have to be local one -----
			crcReply.setRemoteContainerName(myContainer.getID().getName());
			crcReply.setRemotePID(LoadMeasureThread.getLoadCurrentJVM().getJvmPID());
		}
		return crcReply;
	}

	/**
	 * This method defines the local field 'myCRCReply' which is an instance
	 * of 'ClientRemoteContainerReply' and holds the information about
	 * Performance, BenchmarkResult, Network-Addresses of this container-node
	 * @param loadFile
	 */
	private void setLocalCRCReply(boolean loadFile) {
		
		ClientRemoteContainerReply crcReply = null;
		if (loadFile==true) {
			// --- Load the Descriptions from the local file ----------------------------
			crcReply = loadCRCReply();
		}
		
		if (crcReply==null){
			// --- Build the Descriptions from the running system -----------------------
			
			// --- Get infos about the network connection -----
			InetAddress currAddress = null;
			InetAddress addressLocal = null;
			InetAddress addressLocalAlt = null;
			String hostIP, hostName, port;
			
			try {
				currAddress = InetAddress.getByName(myContainer.getID().getAddress());
				addressLocal = InetAddress.getLocalHost();
				addressLocalAlt = InetAddress.getByName("127.0.0.1");
				if (currAddress.equals(addressLocalAlt)) {
					currAddress = addressLocal;
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			hostIP = currAddress.getHostAddress();
			hostName = currAddress.getHostName();
			port = myContainer.getID().getPort();
			
			// --- Define Platform-Info -----------------------
			PlatformAddress myPlatform = new PlatformAddress();
			myPlatform.setIp(hostIP);
			myPlatform.setUrl(hostName);
			myPlatform.setPort(Integer.parseInt(port));
			myPlatform.setHttp4mtp(myContainerMTPurl);	
					
			// --- Set OS-Informations ------------------------
			OSInfo myOS = new OSInfo();
			myOS.setOs_name(System.getProperty("os.name"));
			myOS.setOs_version(System.getProperty("os.version"));
			myOS.setOs_arch(System.getProperty("os.arch"));
			
			// --- Set the Performance of machine -------------
			LoadMeasureSigar sys = LoadMeasureThread.getLoadCurrent();
			PlatformPerformance myPerformance = new PlatformPerformance();
			myPerformance.setCpu_vendor(sys.getVendor());
			myPerformance.setCpu_model(sys.getModel());
			myPerformance.setCpu_numberOf(sys.getTotalCpu());
			myPerformance.setCpu_speedMhz((int) sys.getMhz());
			myPerformance.setMemory_totalMB((int) LoadUnits.bytes2(sys.getTotalMemory(), LoadUnits.CONVERT2_MEGA_BYTE));
			
			// --- Set the performance (Mflops) of the system -
			float benchValue = 0;
			BenchmarkResult bench = null;
			if (LoadMeasureThread.getCompositeBenchmarkValue()==0) {
				ClientRemoteContainerReply storedCRCreply = loadCRCReply();
				if (storedCRCreply==null) {
					benchValue = 0;
				} else {
					bench = storedCRCreply.getRemoteBenchmarkResult();
					benchValue = bench.getBenchmarkValue();	
				}				
			} else {
				benchValue = LoadMeasureThread.getCompositeBenchmarkValue();
			}
			bench = new BenchmarkResult();
			bench.setBenchmarkValue(benchValue);
			
			// --- Get the PID of this JVM --------------------
			String jvmPID = LoadMeasureThread.getLoadCurrentJVM().getJvmPID();
			
			// --- Finally define this local description ------
			crcReply = new ClientRemoteContainerReply();
			crcReply.setRemoteContainerName(myContainer.getID().getName());
			crcReply.setRemotePID(jvmPID);
			crcReply.setRemoteAddress(myPlatform);
			crcReply.setRemoteOS(myOS);
			crcReply.setRemotePerformance(myPerformance);
			crcReply.setRemoteBenchmarkResult(bench);
			
		}

		// --- Set the local value of the ClientRemoteContainerReply --------------------
		myCRCReply = crcReply;
		
		// --- Broadcast the ClientRemoteContainerReply-Object to all other container ---
		Service.Slice[] slices;
		try {
			slices = getAllSlices();
			broadcastPutContainerDescription(slices, myCRCReply);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}
	
}
