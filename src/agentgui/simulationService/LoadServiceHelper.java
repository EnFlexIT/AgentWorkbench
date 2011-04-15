package agentgui.simulationService;

import jade.core.Location;
import jade.core.ServiceException;
import jade.core.ServiceHelper;

import java.util.Hashtable;
import java.util.Vector;

import agentgui.simulationService.load.LoadAgentMap;
import agentgui.simulationService.load.LoadThresholdLevels;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;
import agentgui.simulationService.load.LoadInformation.Container2Wait4;
import agentgui.simulationService.load.LoadInformation.NodeDescription;
import agentgui.simulationService.ontology.ClientRemoteContainerReply;
import agentgui.simulationService.ontology.PlatformLoad;
import agentgui.simulationService.ontology.RemoteContainerConfig;


/**
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public interface LoadServiceHelper extends ServiceHelper {

	public static final String SERVICE_NAME = "agentgui.simulationService.LoadService";
	
	public static final String SERVICE_NODE_DESCRIPTION_FILE = "AgentGUINode.bin";
	
	// --- Methods for agent and container handling -----------------
	public boolean startAgent(String nickName, String agentClassName, Object[] args, String containerName) throws ServiceException;
	
	public String startNewRemoteContainer() throws ServiceException;
	public String startNewRemoteContainer(boolean preventUsageOfAlreadyUsedComputers) throws ServiceException;
	public String startNewRemoteContainer(RemoteContainerConfig remoteConfig, boolean preventUsageOfAlreadyUsedComputers) throws ServiceException;
	public RemoteContainerConfig getDefaultRemoteContainerConfig() throws ServiceException;
	public RemoteContainerConfig getDefaultRemoteContainerConfig(boolean preventUsageOfAlreadyUsedComputers) throws ServiceException;
	public Container2Wait4 startNewRemoteContainerStaus(String containerName) throws ServiceException;
	
	// --- Methods for the load balancing ---------------------------
	public Vector<String> getContainerQueue() throws ServiceException;
	public double getAvgCycleTime() throws ServiceException;
	public void setCycleStartTimeStamp() throws ServiceException;
	
	public void setThresholdLevels(LoadThresholdLevels currThresholdLevels) throws ServiceException;
	
	public Hashtable<String, PlatformLoad> getContainerLoads() throws ServiceException;
	public PlatformLoad getContainerLoad(String containerName) throws ServiceException;
	
	public Hashtable<String, Location> getContainerLocations() throws ServiceException;
	public Location getContainerLocation(String containerName) throws ServiceException;
	
	public void setAndSaveCRCReplyLocal(ClientRemoteContainerReply crcReply) throws ServiceException;
	public ClientRemoteContainerReply getLocalCRCReply() throws ServiceException;

	public void putContainerDescription(ClientRemoteContainerReply crcReply) throws ServiceException;
	public Hashtable<String, NodeDescription> getContainerDescriptions() throws ServiceException;
	public NodeDescription getContainerDescription(String containerName) throws ServiceException;
	
	public LoadAgentMap getAgentMap() throws ServiceException;

	public void setAgentMigration(Vector<AID_Container> transferAgents) throws ServiceException;
	
	
}
