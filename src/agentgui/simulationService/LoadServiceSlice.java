package agentgui.simulationService;

import jade.core.AID;
import jade.core.IMTPException;
import jade.core.Location;
import jade.core.Service;

import java.util.Vector;

import agentgui.simulationService.load.LoadThresholdLevels;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;
import agentgui.simulationService.load.LoadInformation.Container2Wait4;
import agentgui.simulationService.ontology.ClientRemoteContainerReply;
import agentgui.simulationService.ontology.PlatformLoad;
import agentgui.simulationService.ontology.RemoteContainerConfig;


public interface LoadServiceSlice extends Service.Slice {

	// ----------------------------------------------------------
	// --- Horizontal commands of the service -------------------
	// ----------------------------------------------------------
	
	// ----------------------------------------------------------
	// --- Methods to handle new remote container --------------- 
	static final String SERVICE_START_AGENT = "start-agent";
	static final String SERVICE_START_NEW_REMOTE_CONTAINER = "start-new-remote-container";
	static final String SERVICE_GET_DEFAULT_REMOTE_CONTAINER_CONFIG = "get-default-remote-container-config";
	static final String SERVICE_GET_NEW_CONTAINER_2_WAIT_4_STATUS = "get-new-container-2-wait-4-status";
	
	public boolean startAgent(String nickName, String agentClassName, Object[] args) throws IMTPException;
	
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
