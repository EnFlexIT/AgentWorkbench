package agentgui.simulationService;

import jade.core.AID;
import jade.core.ServiceException;
import jade.core.ServiceHelper;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;
import agentgui.simulationService.sensoring.ServiceSensor;


/**
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public interface SimulationServiceHelper extends ServiceHelper {

	public static final String SERVICE_NAME = "agentgui.simulationService.SimulationService";
	
	public static final String SERVICE_NODE_DESCRIPTION_FILE = "AgentGUINode.bin";
	
	// --- Methods for the synchronised time ------------------------
	public long getSynchTimeDifferenceMillis() throws ServiceException;
	public long getSynchTimeMillis() throws ServiceException;
	public Date getSynchTimeDate() throws ServiceException;
	
	// --- Methods for agent and container handling -----------------
	public void stopSimulationAgents() throws ServiceException; 
	
	// --- Methods for the load balancing ---------------------------
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

	public boolean notifySensorAgent(AID agentAID, Object notification) throws ServiceException;
	public boolean notifySensorAgent(AID agentAID, Object notification, boolean aSynchron) throws ServiceException;
	
	public void setPauseSimulation(boolean pauseSimulation) throws ServiceException;
	
	public void setEnvironmentModel(EnvironmentModel envModel) throws ServiceException;
	public EnvironmentModel getEnvironmentModel() throws ServiceException;
	
	public void setEnvironmentInstanceNextPart(AID fromAgent, Object nextPart) throws ServiceException;
	public void resetEnvironmentInstanceNextParts() throws ServiceException;
	public Hashtable<AID, Object> getEnvironmentInstanceNextParts() throws ServiceException;
	
}
