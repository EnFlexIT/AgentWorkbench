package agentgui.simulationService;

import jade.core.AID;
import jade.core.GenericCommand;
import jade.core.IMTPException;
import jade.core.Location;
import jade.core.Node;
import jade.core.ServiceException;
import jade.core.SliceProxy;

import java.util.Hashtable;
import java.util.Vector;

import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.load.LoadThresholdLevels;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;
import agentgui.simulationService.load.LoadInformation.Container2Wait4;
import agentgui.simulationService.ontology.ClientRemoteContainerReply;
import agentgui.simulationService.ontology.PlatformLoad;
import agentgui.simulationService.ontology.RemoteContainerConfig;


public class SimulationServiceProxy extends SliceProxy implements SimulationServiceSlice {

	private static final long serialVersionUID = -7016240061703852319L;

	// ----------------------------------------------------------
	// --- Methods to synchronise the Time --- S T A R T --------
	// ----------------------------------------------------------
	@Override
	public long getRemoteTime() throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_SYNCH_GET_REMOTE_TIME, SimulationService.NAME, null);
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (Long) result;
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	@Override
	public void setRemoteTimeDiff(long timeDifference) throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SERVICE_SYNCH_SET_TIME_DIFF, SimulationService.NAME, null);
			cmd.addParam(timeDifference);
			
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
	// --- Methods to synchronise the Time --- S T O P ----------
	// ----------------------------------------------------------

	
	// ----------------------------------------------------------
	// --- Methods on the Manager-Agent --- S T A R T -----------
	// ----------------------------------------------------------
	public void setManagerAgent(AID agentAddress) throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SIM_SET_MANAGER_AGENT, SimulationService.NAME, null);
			cmd.addParam(agentAddress);
			
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
	@Override
	public AID getManagerAgent() throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SIM_GET_MANAGER_AGENT, SimulationService.NAME, null);
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			return (AID) result;
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	// ----------------------------------------------------------
	// --- Methods on the Manager-Agent --- S T O P -------------
	// ----------------------------------------------------------
	@Override
	public void setEnvironmentModel(EnvironmentModel envModel) throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SIM_SET_ENVIRONMENT_MODEL, SimulationService.NAME, null);
			cmd.addParam(envModel);
			
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
	@Override
	public void stepSimulation(EnvironmentModel envModel, boolean aSynchron) throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SIM_STEP_SIMULATION, SimulationService.NAME, null);
			cmd.addParam(envModel);
			cmd.addParam(aSynchron);
			
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
	@Override
	public void setEnvironmentInstanceNextPart(Hashtable<AID, Object> nextPartsLocal) throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SIM_SET_ENVIRONMENT_NEXT_PART, SimulationService.NAME, null);
			cmd.addParam(nextPartsLocal);
			
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
	@SuppressWarnings("unchecked")
	@Override
	public Hashtable<AID, Object> getEnvironmentInstanceNextParts() throws IMTPException {
		try {
			GenericCommand cmd = new GenericCommand(SIM_GET_ENVIRONMENT_NEXT_PARTS, SimulationService.NAME, null);
			Node n = getNode();
			Object result = n.accept(cmd);
			if((result != null) && (result instanceof Throwable)) {
				if(result instanceof IMTPException) {
					throw (IMTPException)result;
				} else {
					throw new IMTPException("An undeclared exception was thrown", (Throwable)result);
				}
			}
			Hashtable<AID, Object> nextParts = (Hashtable<AID, Object>) result;
			return nextParts;
		}
		catch(ServiceException se) {
			throw new IMTPException("Unable to access remote node", se);
		}
	}
	@Override
	public void resetEnvironmentInstanceNextParts() throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SIM_RESET_ENVIRONMENT_NEXT_PARTS, SimulationService.NAME, null);
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
	// --- Methods on the Environment --- E N D -----------------
	// ----------------------------------------------------------

	
	// ----------------------------------------------------------
	// --- Method to get the Load-Informations of all ----------- 
	// --- containers ----------------------------- S T A R T ---
	// ----------------------------------------------------------
	@Override
	public boolean startAgent(String nickName, String agentClassName, Object[] args) throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SERVICE_START_AGENT, SimulationService.NAME, null);
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
	@Override
	public void stopSimulationAgents() throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_STOP_SIMULATION_AGENTS, SimulationService.NAME, null);
			
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
	@Override
	public String startNewRemoteContainer(RemoteContainerConfig remoteConfig, boolean preventUsageOfAlreadyUsedComputers ) throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_START_NEW_REMOTE_CONTAINER, SimulationService.NAME, null);
			cmd.addParam(remoteConfig);
			cmd.addParam(preventUsageOfAlreadyUsedComputers);
			
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
	@Override
	public RemoteContainerConfig getDefaultRemoteContainerConfig(boolean preventUsageOfAlreadyUsedComputers) throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SERVICE_GET_DEFAULT_REMOTE_CONTAINER_CONFIG, SimulationService.NAME, null);
			cmd.addParam(preventUsageOfAlreadyUsedComputers);
			
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
	@Override
	public Container2Wait4 getNewContainer2Wait4Status(String containerName2Wait4) throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_GET_NEW_CONTAINER_2_WAIT_4_STATUS, SimulationService.NAME, null);
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
	@Override
	public Location getLocation() throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_GET_LOCATION, SimulationService.NAME, null);
			
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
	@Override
	public void setThresholdLevels(LoadThresholdLevels thresholdLevels) throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SERVICE_SET_THRESHOLD_LEVEL, SimulationService.NAME, null);
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
	@Override
	public PlatformLoad measureLoad() throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SERVICE_MEASURE_LOAD, SimulationService.NAME, null);
			
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
	@Override
	public AID[] getAIDList() throws IMTPException {

		try {
			GenericCommand cmd = new GenericCommand(SERVICE_GET_AID_LIST, SimulationService.NAME, null);
			
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
	@Override
	public void setAgentMigration(Vector<AID_Container> transferAgents) throws IMTPException {
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_SET_AGENT_MIGRATION, SimulationService.NAME, null);
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

	@Override
	public void putContainerDescription(ClientRemoteContainerReply crcReply) throws IMTPException {
		
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_PUT_CONTAINER_DESCRIPTION, SimulationService.NAME, null);
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
	
	@Override
	public ClientRemoteContainerReply getCRCReply() throws IMTPException {
	
		try {
			GenericCommand cmd = new GenericCommand(SERVICE_GET_CONTAINER_DESCRIPTION, SimulationService.NAME, null);
			
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
	
}
