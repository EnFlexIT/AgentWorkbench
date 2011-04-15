package agentgui.simulationService;

import jade.core.AID;
import jade.core.IMTPException;
import jade.core.Service;

import java.util.Hashtable;
import java.util.Vector;

import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;


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
	static final String SIM_PAUSE_SIMULATION = "sim-pause";
	static final String SIM_SET_ENVIRONMENT_NEXT_PART = "set-environment-next-part";
	static final String SIM_GET_ENVIRONMENT_NEXT_PARTS = "get-environment-next-parts";
	static final String SIM_RESET_ENVIRONMENT_NEXT_PARTS = "reset-environment-next-parts";
	
	public void setEnvironmentModel(EnvironmentModel envModel) throws IMTPException;
	public void stepSimulation(EnvironmentModel envModel, boolean aSynchron) throws IMTPException;
	public boolean notifyAgent(AID agentAID, Object notification, boolean aSynchron) throws IMTPException;
	public void setPauseSimulation(boolean pauseSimulation) throws IMTPException;
	
	public void setEnvironmentInstanceNextPart(Hashtable<AID, Object> nextPartsLocal) throws IMTPException;
	public Hashtable<AID, Object> getEnvironmentInstanceNextParts() throws IMTPException;
	public void resetEnvironmentInstanceNextParts() throws IMTPException;
	
	// ----------------------------------------------------------
	// --- Methods to handle new remote container --------------- 
	static final String SERVICE_STOP_SIMULATION_AGENTS = "stop-simulation-agents";
	public void stopSimulationAgents() throws IMTPException;
	
	// ----------------------------------------------------------
	// --- Methods for load-informations of all containers
	static final String SERVICE_SET_AGENT_MIGRATION = "set-agent-migration";
	public void setAgentMigration(Vector<AID_Container> transferAgents) throws IMTPException;
	
	
}
