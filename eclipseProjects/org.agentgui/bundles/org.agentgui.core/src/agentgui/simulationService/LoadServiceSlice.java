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
import jade.core.IMTPException;
import jade.core.Location;
import jade.core.Service;

import java.util.Vector;

import agentgui.simulationService.load.LoadThresholdLevels;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;
import agentgui.simulationService.load.LoadInformation.Container2Wait4;
import agentgui.simulationService.load.threading.ThreadProtocol;
import agentgui.simulationService.ontology.ClientRemoteContainerReply;
import agentgui.simulationService.ontology.PlatformLoad;
import agentgui.simulationService.ontology.RemoteContainerConfig;
import agentgui.simulationService.sensoring.ServiceActuator;


/**
 * Here the remotely accessible methods of the LoadService can be found.
 */
public interface LoadServiceSlice extends Service.Slice {

	// ----------------------------------------------------------
	// --- Horizontal commands of the service -------------------
	// ----------------------------------------------------------
	
	// ----------------------------------------------------------
	// --- Methods to handle new remote container --------------- 
	/** The Constant SERVICE_START_AGENT. */
	static final String SERVICE_START_AGENT = "start-agent";
	
	/** The Constant SERVICE_START_NEW_REMOTE_CONTAINER. */
	static final String SERVICE_START_NEW_REMOTE_CONTAINER = "start-new-remote-container";
	
	/** The Constant SERVICE_SET_DEFAULTS_4_REMOTE_CONTAINER_CONFIG. */
	static final String SERVICE_SET_DEFAULTS_4_REMOTE_CONTAINER_CONFIG = "service-set-defaults-4-remote-container-config";
	
	/** The Constant SERVICE_GET_AUTO_REMOTE_CONTAINER_CONFIG. */
	static final String SERVICE_GET_AUTO_REMOTE_CONTAINER_CONFIG = "get-auto-remote-container-config";
	
	/** The Constant SERVICE_GET_NEW_CONTAINER_2_WAIT_4_STATUS. */
	static final String SERVICE_GET_NEW_CONTAINER_2_WAIT_4_STATUS = "get-new-container-2-wait-4-status";
	
	/**
	 * Start an agent.
	 *
	 * @param nickName the local name of the agent
	 * @param agentClassName the agent class 
	 * @param args the serializable Object array of the start start arguments
	 * @return true, if successful
	 * @throws IMTPException the IMTPException
	 */
	public boolean startAgent(String nickName, String agentClassName, Object[] args) throws IMTPException;
	
	/**
	 * Start new remote container.
	 *
	 * @param remoteConfig the RemoteContainerConfig
	 * @return the name of the remote container  
	 * @throws IMTPException the IMTPException
	 */
	public String startNewRemoteContainer(RemoteContainerConfig remoteConfig) throws IMTPException;
	
	/**
	 * Sets the defaults for a remote container configuration.
	 *
	 * @param remoteConfig the ontology instance for the default remote container configuration
	 * @throws IMTPException the iMTP exception
	 */
	public void setDefaults4RemoteContainerConfig(RemoteContainerConfig remoteConfig) throws IMTPException;
	
	/**
	 * Provides the Default remote container configuration.
	 *
	 * @return the default RemoteContainerConfig
	 * @throws IMTPException the IMTPException
	 */
	public RemoteContainerConfig getAutoRemoteContainerConfig() throws IMTPException;
	
	/**
	 * Returns the current status of the requested new container.
	 *
	 * @param containerName2Wait4 the new containers name
	 * @return current status of the requested new remote container
	 * @throws IMTPException the IMTPException
	 * @see Container2Wait4
	 */
	public Container2Wait4 getNewContainer2Wait4Status(String containerName2Wait4) throws IMTPException;
	
	
	
	// ----------------------------------------------------------
	// --- Methods to deal with the container description ------- 
	/** The Constant SERVICE_PUT_CONTAINER_DESCRIPTION. */
	static final String SERVICE_PUT_CONTAINER_DESCRIPTION = "service-container-description-put";
	
	/** The Constant SERVICE_GET_CONTAINER_DESCRIPTION. */
	static final String SERVICE_GET_CONTAINER_DESCRIPTION = "service-container-description-get";
	
	/**
	 * Sends a container description to the node.
	 *
	 * @param crcReply the ClientRemoteContainerReply
	 * @throws IMTPException the IMTPException
	 */
	public void putContainerDescription(ClientRemoteContainerReply crcReply) throws IMTPException;
	
	/**
	 * Gets the remotely available ClientRemoteContainerReply.
	 *
	 * @return the ClientRemoteContainerReply of the romote container 
	 * @throws IMTPException the IMTPException
	 */
	public ClientRemoteContainerReply getCRCReply() throws IMTPException;

	
	
	// ----------------------------------------------------------
	// --- Methods for load-informations of all containers ------
	/** The Constant SERVICE_GET_LOCATION. */
	static final String SERVICE_GET_LOCATION = "get-location";
	
	/** The Constant SERVICE_SET_THRESHOLD_LEVEL. */
	static final String SERVICE_SET_THRESHOLD_LEVEL = "set-threshold-level";
	
	/** The Constant SERVICE_MEASURE_LOAD. */
	static final String SERVICE_MEASURE_LOAD = "measure-Load";
	
	/** The Constant SERVICE_GET_AID_LIST. */
	static final String SERVICE_GET_AID_LIST = "get-aid-list";
	
	/** The Constant SERVICE_GET_AID_LIST_SENSOR. */
	static final String SERVICE_GET_AID_LIST_SENSOR = "get-aid-list-sensor";
	
	/** The Constant SERVICE_SET_AGENT_MIGRATION. */
	static final String SERVICE_SET_AGENT_MIGRATION = "set-agent-migration";
	
	/**
	 * Gets the Location of a container.
	 *
	 * @return the location
	 * @throws IMTPException the IMTPException
	 * @see Location
	 */
	public Location getLocation() throws IMTPException;
	
	/**
	 * Sets the threshold levels at a remote container.
	 *
	 * @param thresholdLevels the new threshold levels
	 * @throws IMTPException the IMTPException
	 * @see LoadThresholdLevels
	 */
	public void setThresholdLevels(LoadThresholdLevels thresholdLevels) throws IMTPException;
	
	/**
	 * Will measure the load of a (remote) container.
	 *
	 * @return the platform load
	 * @throws IMTPException the IMTPException
	 */
	public PlatformLoad measureLoad() throws IMTPException;
	
	/**
	 * Gets the array of all available agents in a (remote) container.
	 *
	 * @return the list of AID's of the (remote) container
	 * @throws IMTPException the IMTPException
	 */
	public AID[] getAIDList() throws IMTPException;
	
	/**
	 * Gets the array of all available agents in a (remote) container, which are connected to the SimulationService.
	 *
	 * @return the aID list sensor agents
	 * @throws IMTPException the IMTPException
	 * @see SimulationService
	 * @see ServiceActuator
	 */
	public AID[] getAIDListSensorAgents() throws IMTPException;
	
	/**
	 * Sets the Vector of agents, which have to migrate to a different container/location.
	 *
	 * @param transferAgents the new agent migration
	 * @throws IMTPException the IMTPException
	 */
	public void setAgentMigration(Vector<AID_Container> transferAgents) throws IMTPException;

	
	
	// ----------------------------------------------------------
	// --- Methods for thread measurements ----------------------
	/** The Constant SERVICE_REQUEST_THREAD_MEASUREMENT. */
	static final String SERVICE_THREAD_MEASUREMENT_REQUEST = "requestThreadMeasurement";
	static final String SERVICE_THREAD_MEASUREMENT_PUT = "putThreadMeasurement";
	
	/**
	 * Requests the thread measurement from the contacted container.
	 * @throws IMTPException the IMTP exception
	 */
	public void requestThreadMeasurement(long timestamp) throws IMTPException;

	/**
	 * Put thread protocol.
	 *
	 * @param threadProtocol the thread protocol
	 * @throws IMTPException the IMTP exception
	 */
	public void putThreadProtocol(ThreadProtocol threadProtocol) throws IMTPException;

	
	// ----------------------------------------------------------
	// --- Methods for available macines ------------------------
	static final String SERVICE_REQUEST_AVAILABLE_MACHINES= "requestAvailableMachines";
	
	/**
	 * Main request available machines.
	 * @throws IMTPException the IMTP exception
	 */
	public void mainRequestAvailableMachines() throws IMTPException;

}
