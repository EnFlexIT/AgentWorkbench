/**
 * 
 */
package mas.service;

import jade.core.AID;
import jade.core.Location;
import jade.core.ServiceException;
import jade.core.ServiceHelper;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import mas.service.distribution.ontology.ClientRemoteContainerReply;
import mas.service.distribution.ontology.PlatformLoad;
import mas.service.distribution.ontology.RemoteContainerConfig;
import mas.service.environment.EnvironmentModel;
import mas.service.load.LoadAgentMap;
import mas.service.load.LoadAgentMap.AID_Container;
import mas.service.load.LoadInformation.Container2Wait4;
import mas.service.load.LoadInformation.NodeDescription;
import mas.service.sensoring.ServiceSensor;

/**
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public interface SimulationServiceHelper extends ServiceHelper {

	public static final String SERVICE_NAME = "mas.service.SimulationService";
	
	public static final String SERVICE_NODE_DESCRIPTION_FILE = "AgentGUINode.bin";
	
	// --- Methods for the synchronised time --------------------
	public long getSynchTimeDifferenceMillis() throws ServiceException;
	public long getSynchTimeMillis() throws ServiceException;
	public Date getSynchTimeDate() throws ServiceException;

	
	// --- Methods for the load balancing ---------------------------
	public String startNewRemoteContainer() throws ServiceException;
	public String startNewRemoteContainer(RemoteContainerConfig remoteConfig) throws ServiceException;
	public RemoteContainerConfig getDefaultRemoteContainerConfig() throws ServiceException;
	public Container2Wait4 startNewRemoteContainerStaus(String containerName) throws ServiceException;
	
	public Vector<String> getContainerQueue() throws ServiceException;
	
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
	
	// --- Methods for simulations ----------------------------------
	public void setManagerAgent(AID agentAddress) throws ServiceException; 
	public AID getManagerAgent() throws ServiceException;
	
	public void sensorPlugIn(ServiceSensor sensor) throws ServiceException;
	public void sensorPlugOut(ServiceSensor sensor) throws ServiceException;
	
	public void setStepSimulationAsynchronous(boolean stepAsynchronous) throws ServiceException;
	public boolean getStepSimulationAsynchronous() throws ServiceException;
	
	public void stepSimulation(EnvironmentModel envModel) throws ServiceException;
	public void stepSimulation(EnvironmentModel envModel, boolean aSynchron) throws ServiceException;

	public void setEnvironmentModel(EnvironmentModel envModel) throws ServiceException;
	public EnvironmentModel getEnvironmentModel() throws ServiceException;
	
	public void setEnvironmentInstanceNextPart(AID fromAgent, Object nextPart) throws ServiceException;
	public void resetEnvironmentInstanceNextParts() throws ServiceException;
	public Hashtable<AID, Object> getEnvironmentInstanceNextParts() throws ServiceException;
	
}
