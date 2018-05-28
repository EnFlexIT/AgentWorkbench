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
import jade.core.GenericCommand;
import jade.core.IMTPException;
import jade.core.Location;
import jade.core.Node;
import jade.core.ServiceException;
import jade.core.SliceProxy;

import java.util.Vector;

import agentgui.simulationService.load.LoadThresholdLevels;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;
import agentgui.simulationService.load.LoadInformation.Container2Wait4;
import agentgui.simulationService.load.threading.ThreadProtocol;
import agentgui.simulationService.ontology.ClientRemoteContainerReply;
import agentgui.simulationService.ontology.PlatformLoad;
import agentgui.simulationService.ontology.RemoteContainerConfig;


/**
 * This class provides the reals functionalities for the {@link LoadServiceSlice}.
 */
public class LoadServiceProxy extends SliceProxy implements LoadServiceSlice {

	private static final long serialVersionUID = -7016240061703852319L;

	// ----------------------------------------------------------
	// --- Method to get the Load-Informations of all ----------- 
	// --- containers ----------------------------- S T A R T ---
	// ----------------------------------------------------------
	/* (non-Javadoc)
	 * @see agentgui.simulationService.LoadServiceSlice#startAgent(java.lang.String, java.lang.String, java.lang.Object[])
	 */
	@Override
	public boolean startAgent(String nickName, String agentClassName, Object[] args) throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SERVICE_START_AGENT, LoadService.NAME, null);
			cmd.addParam(nickName);
			cmd.addParam(agentClassName);
			cmd.addParam(args);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (Boolean) result;
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.LoadServiceSlice#startNewRemoteContainer(agentgui.simulationService.ontology.RemoteContainerConfig, boolean)
	 */
	@Override
	public String startNewRemoteContainer(RemoteContainerConfig remoteConfig) throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_START_NEW_REMOTE_CONTAINER, LoadService.NAME, null);
			cmd.addParam(remoteConfig);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (String) result;
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.LoadServiceSlice#setDefaults4RemoteContainerConfig(agentgui.simulationService.ontology.RemoteContainerConfig)
	 */
	@Override
	public void setDefaults4RemoteContainerConfig(RemoteContainerConfig remoteConfig) throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_SET_DEFAULTS_4_REMOTE_CONTAINER_CONFIG, LoadService.NAME, null);
			cmd.addParam(remoteConfig);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.LoadServiceSlice#getDefaultRemoteContainerConfig(boolean)
	 */
	@Override
	public RemoteContainerConfig getAutoRemoteContainerConfig() throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SERVICE_GET_AUTO_REMOTE_CONTAINER_CONFIG, LoadService.NAME, null);
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (RemoteContainerConfig) result;
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.LoadServiceSlice#getNewContainer2Wait4Status(java.lang.String)
	 */
	@Override
	public Container2Wait4 getNewContainer2Wait4Status(String containerName2Wait4) throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_GET_NEW_CONTAINER_2_WAIT_4_STATUS, LoadService.NAME, null);
			cmd.addParam(containerName2Wait4);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (Container2Wait4) result;
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.LoadServiceSlice#getLocation()
	 */
	@Override
	public Location getLocation() throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_GET_LOCATION, LoadService.NAME, null);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			Location loc = (Location) result;
			return loc;
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.LoadServiceSlice#setThresholdLevels(agentgui.simulationService.load.LoadThresholdLevels)
	 */
	@Override
	public void setThresholdLevels(LoadThresholdLevels thresholdLevels) throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SERVICE_SET_THRESHOLD_LEVEL, LoadService.NAME, null);
			cmd.addParam(thresholdLevels);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}		
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.LoadServiceSlice#measureLoad()
	 */
	@Override
	public PlatformLoad measureLoad() throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SERVICE_MEASURE_LOAD, LoadService.NAME, null);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (PlatformLoad) result;
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.LoadServiceSlice#getAIDList()
	 */
	@Override
	public AID[] getAIDList() throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SERVICE_GET_AID_LIST, SimulationService.NAME, null);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if ((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (AID[]) result;
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.LoadServiceSlice#getAIDListSensorAgents()
	 */
	@Override
	public AID[] getAIDListSensorAgents() throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SERVICE_GET_AID_LIST_SENSOR, SimulationService.NAME, null);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (AID[]) result;
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.LoadServiceSlice#setAgentMigration(java.util.Vector)
	 */
	@Override
	public void setAgentMigration(Vector<AID_Container> transferAgents) throws IMTPException {
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_SET_AGENT_MIGRATION, LoadService.NAME, null);
			cmd.addParam(transferAgents);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}			
	}
	// ----------------------------------------------------------
	// --- Method to get the Load-Informations of all ----------- 
	// --- containers ----------------------------- E N D -------
	// ----------------------------------------------------------

	/* (non-Javadoc)
	 * @see agentgui.simulationService.LoadServiceSlice#putContainerDescription(agentgui.simulationService.ontology.ClientRemoteContainerReply)
	 */
	@Override
	public void putContainerDescription(ClientRemoteContainerReply crcReply) throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_PUT_CONTAINER_DESCRIPTION, LoadService.NAME, null);
			cmd.addParam(crcReply);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}			
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.LoadServiceSlice#getCRCReply()
	 */
	@Override
	public ClientRemoteContainerReply getCRCReply() throws IMTPException {
	
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_GET_CONTAINER_DESCRIPTION, LoadService.NAME, null);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (ClientRemoteContainerReply) result;
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}

	/* (non-Javadoc)
	 * @see agentgui.simulationService.LoadServiceSlice#requestThreadMeasurement(long)
	 */
	@Override
	public void requestThreadMeasurement(long timestamp) throws IMTPException {
	
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_THREAD_MEASUREMENT_REQUEST, LoadService.NAME, null);
			cmd.addParam(timestamp);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}			
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}

	/* (non-Javadoc)
	 * @see agentgui.simulationService.LoadServiceSlice#putThreadProtocol(agentgui.simulationService.load.threading.ThreadProtocol)
	 */
	@Override
	public void putThreadProtocol(ThreadProtocol threadProtocol) throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_THREAD_MEASUREMENT_PUT, LoadService.NAME, null);
			cmd.addParam(threadProtocol);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if ((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}			
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}

	/* (non-Javadoc)
	 * @see agentgui.simulationService.LoadServiceSlice#mainRequestAvailableMachines()
	 */
	@Override
	public void mainRequestAvailableMachines() throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_REQUEST_AVAILABLE_MACHINES, LoadService.NAME, null);
			
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}			
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	
}
