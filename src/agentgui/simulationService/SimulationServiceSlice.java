package agentgui.simulationService;

import jade.core.AID;
import jade.core.IMTPException;
import jade.core.Location;
import jade.core.Service;

import java.util.Hashtable;
import java.util.Vector;

import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.load.LoadThresholdLevels;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;
import agentgui.simulationService.load.LoadInformation.Container2Wait4;
import agentgui.simulationService.ontology.ClientRemoteContainerReply;
import agentgui.simulationService.ontology.PlatformLoad;
import agentgui.simulationService.ontology.RemoteContainerConfig;


public interface SimulationServiceSlice extends Service.Slice {

	// ----------------------------------------------------------
	// --- Horizontal commands of the service -------------------
	// ----------------------------------------------------------
	
	// ----------------------------------------------------------
	// --- Methods to synchronise the time ----------------------
	static final String SERVICE_SYNCH_GET_REMOTE_TIME = "service-synch-get-remote-time";
	static final String SERVICE_SYNCH_SET_TIME_DIFF = "service-synch-set-time-diff";
	
	public long getRemoteTime() throws IMTPException;
	public void setRemoteTimeDiff(long timeDifference) throws IMTPException;
	
	
	// ----------------------------------------------------------
	// --- Methods on the Manager-Agent -------------------------
	static final String SIM_SET_MANAGER_AGENT = "set-manager";
	static final String SIM_GET_MANAGER_AGENT = "get-manager";
	
	public void setManagerAgent(AID agentAddress) throws IMTPException;
	public AID getManagerAgent() throws IMTPException;
	
	
	// ----------------------------------------------------------
	// --- Methods on the EnvironmentModel ----------------------
	static final String SIM_SET_ENVIRONMENT_MODEL = "sim-set-environment-model";
	static final String SIM_STEP_SIMULATION = "sim-step-simulation";
	static final String SIM_NOTIFY_AGENT = "sim-notify-agent";
	static final String SIM_SET_ENVIRONMENT_NEXT_PART = "set-environment-next-part";
	static final String SIM_GET_ENVIRONMENT_NEXT_PARTS = "get-environment-next-parts";
	static final String SIM_RESET_ENVIRONMENT_NEXT_PARTS = "reset-environment-next-parts";
	
	public void setEnvironmentModel(EnvironmentModel envModel) throws IMTPException;
	public void stepSimulation(EnvironmentModel envModel, boolean aSynchron) throws IMTPException;
	public boolean notifyAgent(AID agentAID, Object notification, boolean aSynchron) throws IMTPException;
	
	public void setEnvironmentInstanceNextPart(Hashtable<AID, Object> nextPartsLocal) throws IMTPException;
	public Hashtable<AID, Object> getEnvironmentInstanceNextParts() throws IMTPException;
	public void resetEnvironmentInstanceNextParts() throws IMTPException;
	
	// ----------------------------------------------------------
	// --- Methods to handle new remote container --------------- 
	static final String SERVICE_START_AGENT = "start-agent";
	static final String SERVICE_STOP_SIMULATION_AGENTS = "stop-simulation-agents";
	static final String SERVICE_START_NEW_REMOTE_CONTAINER = "start-new-remote-container";
	static final String SERVICE_GET_DEFAULT_REMOTE_CONTAINER_CONFIG = "get-default-remote-container-config";
	static final String SERVICE_GET_NEW_CONTAINER_2_WAIT_4_STATUS = "get-new-container-2-wait-4-status";
	
	public boolean startAgent(String nickName, String agentClassName, Object[] args) throws IMTPException;
	public void stopSimulationAgents() throws IMTPException;
	
	public String startNewRemoteContainer(RemoteContainerConfig remoteConfig, boolean preventUsageOfAlreadyUsedComputers) throws IMTPException;
	public RemoteContainerConfig getDefaultRemoteContainerConfig(boolean preventUsageOfAlreadyUsedComputers) throws IMTPException;
	public Container2Wait4 getNewContainer2Wait4Status(String containerName2Wait4) throws IMTPException;
	
	// ----------------------------------------------------------
	// --- Methods to deal with the container description ------- 
	static final String SERVICE_PUT_CONTAINER_DESCRIPTION = "service-container-description-put";
	static final String SERVICE_GET_CONTAINER_DESCRIPTION = "service-container-description-get";
	
	public void putContainerDescription(ClientRemoteContainerReply crcReply) throws IMTPException;
	public ClientRemoteContainerReply getCRCReply() throws IMTPException;

	// ----------------------------------------------------------
	// --- Methods for load-informations of all containers
	static final String SERVICE_GET_LOCATION = "get-location";
	static final String SERVICE_SET_THRESHOLD_LEVEL = "set-threshold-level";
	static final String SERVICE_MEASURE_LOAD = "measure-Load";
	static final String SERVICE_GET_AID_LIST = "get-aid-list";
	static final String SERVICE_GET_AID_LIST_SENSOR = "get-aid-list-sensor";
	static final String SERVICE_SET_AGENT_MIGRATION = "set-agent-migration";
	
	public Location getLocation() throws IMTPException;
	public void setThresholdLevels(LoadThresholdLevels thresholdLevels) throws IMTPException;
	public PlatformLoad measureLoad() throws IMTPException;
	public AID[] getAIDList() throws IMTPException;
	public AID[] getAIDListSensorAgents() throws IMTPException;
	public void setAgentMigration(Vector<AID_Container> transferAgents) throws IMTPException;
	
	
}
