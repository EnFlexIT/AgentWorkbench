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

import jade.core.Location;
import jade.core.ServiceException;
import jade.core.ServiceHelper;

import java.util.Hashtable;
import java.util.Vector;

import agentgui.simulationService.agents.LoadMeasureAgent;
import agentgui.simulationService.load.LoadAgentMap;
import agentgui.simulationService.load.LoadThresholdLevels;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;
import agentgui.simulationService.load.LoadInformation.Container2Wait4;
import agentgui.simulationService.load.LoadInformation.NodeDescription;
import agentgui.simulationService.ontology.ClientAvailableMachinesReply;
import agentgui.simulationService.ontology.ClientRemoteContainerReply;
import agentgui.simulationService.ontology.PlatformLoad;
import agentgui.simulationService.ontology.RemoteContainerConfig;

/**
 * Here, the accessible methods of the LoadService can be found.  
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public interface LoadServiceHelper extends ServiceHelper {

	/** The name of this service. */
	public static final String SERVICE_NAME = "agentgui.simulationService.LoadService";
	
	
	// --- Methods for agent and container handling -----------------
	/**
	 * Start an agent on a specified location.
	 *
	 * @param agentName the local name for the agent
	 * @param agentClassName the class of the agent
	 * @param args the serializable Object array for the start arguments
	 * @param containerName the name of the container, where the agent should start
	 * @return true, if successful
	 * @throws ServiceException the service exception
	 */
	public boolean startAgent(String agentName, String agentClassName, Object[] args, String containerName) throws ServiceException;
	
	/**
	 * Will start a new remote container.
	 *
	 * @return the name of the new container
	 * @throws ServiceException the service exception
	 */
	public String startNewRemoteContainer() throws ServiceException;
	
	/**
	 * Will start a new remote container.
	 *
	 * @param remoteConfig the remote container configuration
	 * @return the name of the new container
	 * @throws ServiceException the service exception
	 */
	public String startNewRemoteContainer(RemoteContainerConfig remoteConfig) throws ServiceException;
	
	/**
	 * Provides the default remote container configuration.
	 *
	 * @return the default remote container configuration
	 * @throws ServiceException the service exception
	 */
	public RemoteContainerConfig getAutoRemoteContainerConfig() throws ServiceException;
	
	/**
	 * Sets the default remote container configuration.
	 *
	 * @param remoteContainerConfig the new default remote container configuration
	 * @throws ServiceException the service exception
	 */
	public void setDefaults4RemoteContainerConfig(RemoteContainerConfig remoteContainerConfig) throws ServiceException;
	
	/**
	 * Returns the current status of the requested new container.
	 *
	 * @param containerName the new containers name
	 * @return current status of the requested new remote container
	 * @throws ServiceException the service exception
	 * @see Container2Wait4
	 */
	public Container2Wait4 startNewRemoteContainerStaus(String containerName) throws ServiceException;
	
	
	// --- Methods for the load balancing ---------------------------
	/**
	 * Returns the list of all available container.
	 *
	 * @return the container queue
	 * @throws ServiceException the service exception
	 */
	public Vector<String> getContainerNames() throws ServiceException;
	
	/**
	 * Provides the average cycle time for a simulation.
	 *
	 * @return the average cycle time fo a simulation
	 * @throws ServiceException the service exception
	 * @see #setSimulationCycleStartTimeStamp()
	 */
	public double getAvgCycleTime() throws ServiceException;
	
	/**
	 * Sets System.currentTimeMillis() as an indicator for the start of an new simulations cycle.
	 *
	 * @throws ServiceException the service exception
	 * @see #getAvgCycleTime()
	 */
	public void setSimulationCycleStartTimeStamp() throws ServiceException;
	
	/**
	 * Sets the threshold levels for all involved container.
	 *
	 * @param currThresholdLevels the new threshold levels
	 * @throws ServiceException the service exception
	 */
	public void setThresholdLevels(LoadThresholdLevels currThresholdLevels) throws ServiceException;
	
	/**
	 * Returns the load of all connected container.
	 *
	 * @return the container loads
	 * @throws ServiceException the service exception
	 */
	public Hashtable<String, PlatformLoad> getContainerLoadHash() throws ServiceException;
	
	/**
	 * Returns the load of a specified container.
	 *
	 * @param containerName the container name
	 * @return the container load
	 * @throws ServiceException the service exception
	 */
	public PlatformLoad getContainerLoad(String containerName) throws ServiceException;
	
	/**
	 * Provides the container locations of all connected container.
	 *
	 * @return the container locations
	 * @throws ServiceException the service exception
	 */
	public Hashtable<String, Location> getContainerLocations() throws ServiceException;
	
	/**
	 * Gets the container location of a specified container.
	 *
	 * @param containerName the container name
	 * @return the container location
	 * @throws ServiceException the service exception
	 */
	public Location getContainerLocation(String containerName) throws ServiceException;
	
	/**
	 * Sets and save the local ClientRemoteContainerReply.
	 *
	 * @param crcReply the ClientRemoteContainerReply to save
	 * @throws ServiceException the service exception
	 */
	public void setAndSaveCRCReplyLocal(ClientRemoteContainerReply crcReply) throws ServiceException;
	
	/**
	 * Returns the locally saved ClientRemoteContainerReply.
	 *
	 * @return the local ClientRemoteContainerReply
	 * @throws ServiceException the service exception
	 */
	public ClientRemoteContainerReply getLocalCRCReply() throws ServiceException;

	/**
	 * Broadcasts a container description to all involved node, if the ClientRemoteContainerReply is valid.
	 *
	 * @param crcReply the crc reply
	 * @throws ServiceException the service exception
	 */
	public void putContainerDescription(ClientRemoteContainerReply crcReply) throws ServiceException;
	
	/**
	 * Gets the NodeDescription of all involved nodes.
	 *
	 * @return the container descriptions
	 * @throws ServiceException the service exception
	 * @see NodeDescription
	 */
	public Hashtable<String, NodeDescription> getContainerDescriptions() throws ServiceException;
	
	/**
	 * Gets the NodeDescription of a specified container.
	 *
	 * @param containerName the container name
	 * @return the container description
	 * @throws ServiceException the service exception
	 * @see NodeDescription
	 */
	public NodeDescription getContainerDescription(String containerName) throws ServiceException;
	
	/**
	 * Returns the current LoadAgentMap.
	 *
	 * @return the current LoadAgentMap
	 * @throws ServiceException the service exception
	 * @see LoadAgentMap
	 */
	public LoadAgentMap getLoadAgentMap() throws ServiceException;

	/**
	 * Sets the agent migration.
	 *
	 * @param transferAgents the agents, which has to be migrated
	 * @throws ServiceException the service exception
	 */
	public void setAgentMigration(Vector<AID_Container> transferAgents) throws ServiceException;

	/**
	 * Requests all involved JVMs to do the thread measurements.
	 *
	 * @param loadMeasureAgent the load measure agent
	 * @throws ServiceException the service exception
	 */
	public void requestThreadMeasurements(LoadMeasureAgent loadMeasureAgent) throws ServiceException;
	
	
	/**
	 * Requests at the main container for the available machines.
	 *
	 * @throws ServiceException the service exception
	 */
	public void requestAvailableMachines() throws ServiceException;
	
	/**
	 * Puts the available machines to the load information.
	 *
	 * @param availableMachines the available machines
	 * @throws ServiceException the service exception
	 */
	public void putAvailableMachines(ClientAvailableMachinesReply availableMachines) throws ServiceException;
	
	/**
	 * Gets the available machines.
	 *
	 * @return the available machines
	 * @throws ServiceException the service exception
	 */
	public ClientAvailableMachinesReply getAvailableMachines() throws ServiceException;
	
}
