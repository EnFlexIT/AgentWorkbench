package de.enflexit.awb.simulation;

import jade.core.AID;
import jade.core.ServiceException;
import jade.core.ServiceHelper;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import de.enflexit.awb.simulation.agents.AbstractDisplayAgent;
import de.enflexit.awb.simulation.agents.SimulationAgent;
import de.enflexit.awb.simulation.environment.EnvironmentModel;
import de.enflexit.awb.simulation.load.LoadAgentMap.AID_Container;
import de.enflexit.awb.simulation.sensoring.ServiceActuator;
import de.enflexit.awb.simulation.sensoring.ServiceSensor;
import de.enflexit.awb.simulation.sensoring.ServiceSensorManager;
import de.enflexit.awb.simulation.transaction.EnvironmentNotification;


/**
 * Here, the accessible methods of the SimulationService can be found.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public interface SimulationServiceHelper extends ServiceHelper {

	public static final String SERVICE_NAME = SimulationService.class.getName();
	public static final String SERVICE_NODE_DESCRIPTION_FILE = "AWB-Node.bin";
	
	// --- Methods for the synchronized time ------------------------
	/**
	 * Returns the difference in milliseconds for synchronized time.
	 *
	 * @return the time difference milliseconds for synchronized time
	 * @throws ServiceException the ServiceException
	 */
	public long getSynchTimeDifferenceMillis() throws ServiceException;
	
	/**
	 * Returns the synchronized time in milliseconds.
	 *
	 * @return the synchronized time in milliseconds
	 * @throws ServiceException the ServiceException
	 */
	public long getSynchTimeMillis() throws ServiceException;
	/**
	 * Returns the synchronized time as date.
	 *
	 * @return the synchronized time date
	 * @throws ServiceException the ServiceException
	 */
	public Date getSynchTimeDate() throws ServiceException;
	
	
	// --- Methods for agent and container handling -----------------
	/**
	 * Stops all agents, which are connected to the SimulationService via the Actuator/Sensor relationship .
	 *
	 * @throws ServiceException the ServiceException
	 */
	public void stopSimulationAgents() throws ServiceException; 
	
	/**
	 * Sets the pause of the simulation.
	 *
	 * @param pauseSimulation the new pause simulation
	 * @throws ServiceException the ServiceException
	 */
	public void setPauseSimulation(boolean pauseSimulation) throws ServiceException;
	
	
	// --- Methods for the load balancing ---------------------------
	/**
	 * Sets the agents to migrate to a different location/container.
	 *
	 * @param transferAgents the new agent migration
	 * @throws ServiceException the ServiceException
	 * @see AID_Container
	 */
	public void setAgentMigration(Vector<AID_Container> transferAgents) throws ServiceException;
	
	// --- Methods for simulations ----------------------------------
	/**
	 * Sets the manager agent of the simulation/environment.
	 *
	 * @param agentAddress the new manager agent
	 * @throws ServiceException the ServiceException
	 */
	public void setManagerAgent(AID agentAddress) throws ServiceException;
	
	/**
	 * Returns the manager agent for the simulation/environment.
	 *
	 * @return the manager agent
	 * @throws ServiceException the ServiceException
	 */
	public AID getManagerAgent() throws ServiceException;
		
	/**
	 * Allows to plug in a {@link ServiceSensor} into the SimulationService.
	 *
	 * @param sensor the {@link ServiceSensor} to plug in
	 * @throws ServiceException the ServiceException
	 */
	public void sensorPlugIn(ServiceSensor sensor) throws ServiceException;
	
	/**
	 * Allows to plug in a {@link ServiceSensor} into the SimulationService.
	 *
	 * @param sensor the {@link ServiceSensor} to plug in
	 * @param passive true, if the sensor is just listening to the actuator, but that there is no answer to expect  
	 * @throws ServiceException the ServiceException
	 */
	public void sensorPlugIn(ServiceSensor sensor, boolean passive) throws ServiceException;
	
	/**
	 * Allows to plug out a {@link ServiceSensor} into the SimulationService.
	 *
	 * @param sensor the {@link ServiceSensor} to plug out
	 * @throws ServiceException the ServiceException
	 */
	public void sensorPlugOut(ServiceSensor sensor) throws ServiceException;
	
	/**
	 * Allows to plug in a {@link ServiceSensorManager} into the SimulationService.
	 *
	 * @param sensor the {@link ServiceSensorManager} to plug in
	 * @throws ServiceException the ServiceException
	 */
	public void sensorPlugIn4Manager(ServiceSensorManager sensor) throws ServiceException;
	
	/**
	 * Allows to plug out a {@link ServiceSensorManager} into the SimulationService.
	 *
	 * @param sensor the {@link ServiceSensorManager} to plug out
	 * @throws ServiceException the ServiceException
	 */
	public void sensorPlugOut4Manager(ServiceSensorManager sensor) throws ServiceException;
	
	/**
	 * Sets to step a simulation by notify all involved agents in a asynchronous way.
	 *
	 * @param stepAsynchronous true, if the notification has to be done asynchronously
	 * @throws ServiceException the ServiceException
	 */
	public void setStepSimulationAsynchronous(boolean stepAsynchronous) throws ServiceException;
	
	/**
	 * Returns the information, if the notification about environment changes has to be done asynchronously.
	 *
	 * @return true, if asynchronously notification is set
	 * @throws ServiceException the ServiceException
	 */
	public boolean getStepSimulationAsynchronous() throws ServiceException;
	
	/**
	 * Steps a simulation by using the current {@link EnvironmentModel} and the number of expected changes / answers.
	 *
	 * @param envModel the current or new EnvironmentModel
	 * @param answersExpected the changes / answers expected from the simulation agents
	 * @throws ServiceException the ServiceException
	 * @see EnvironmentModel
	 */
	public void stepSimulation(EnvironmentModel envModel, int answersExpected) throws ServiceException;
	
	/**
	 * Steps a simulation by using the current {@link EnvironmentModel} and the number of expected changes / answers.
	 *
	 * @param envModel the current or new EnvironmentModel
	 * @param answersExpected the changes / answers expected from the simulation agents
	 * @param aSynchron true, if the notification has to be done asynchronously
	 * @throws ServiceException the ServiceException
	 * @see EnvironmentModel
	 */
	public void stepSimulation(EnvironmentModel envModel, int answersExpected, boolean aSynchron) throws ServiceException;

	/**
	 * Notifies a specified sensor agent by using an EnvironmentNotification.
	 *
	 * @param agentAID the agent AID
	 * @param notification the notification
	 * @return true, if successful
	 * @throws ServiceException the ServiceException
	 * @see ServiceSensor
	 * @see ServiceActuator
	 * @see SimulationAgent
	 * @see EnvironmentNotification
	 */
	public boolean notifySensorAgent(AID agentAID, EnvironmentNotification notification) throws ServiceException;
	
	/**
	 * Notifies a SimulationManager by using an EnvironmentNotification.
	 *
	 * @param notification the notification
	 * @return true, if successful
	 * @throws ServiceException the ServiceException
	 * @see SimulationManagerAgent
	 * @see EnvironmentNotification
	 */
	public boolean notifyManagerAgent(EnvironmentNotification notification) throws ServiceException;
	
	
	/**
	 * Returns the {@link EnvironmentModel} from the setup of the end user application.
	 *
	 * @return the environment model from setup
	 * @throws ServiceException the service exception
	 */
	public EnvironmentModel getEnvironmentModelFromSetup() throws ServiceException;

	/**
	 * Returns the current the {@link EnvironmentModel}.
	 *
	 * @return the environment model
	 * @throws ServiceException the ServiceException
	 * @see EnvironmentModel
	 */
	public EnvironmentModel getEnvironmentModel() throws ServiceException;
	
	/**
	 * Can be used in order to set and distribute an {@link EnvironmentModel} without 
	 * a direct notification to the involved agents.
	 *
	 * @param envModel the current or new EnvironmentModel instance
	 * @throws ServiceException the ServiceException
	 * @see EnvironmentModel
	 */
	public void setEnvironmentModel(EnvironmentModel envModel) throws ServiceException;
	
	/**
	 * Can be used in order to set and distribute an {@link EnvironmentModel} without
	 * a direct notification to the involved agents.
	 *
	 * @param envModel the current or new EnvironmentModel instance
	 * @param notifySensorAgents true, if the sensor agents should be also notified 
	 * @throws ServiceException the ServiceException
	 * @see EnvironmentModel
	 */
	public void setEnvironmentModel(EnvironmentModel envModel, boolean notifySensorAgents) throws ServiceException;

	
	/**
	 * This method can be used by a {@link SimulationAgent} in order to set their part
	 * of the environment change.
	 *
	 * @param fromAgent the AID of the agent
	 * @param nextPart the agents part for the next environment
	 * @throws ServiceException the ServiceException
	 * @see SimulationAgent
	 */
	public void setEnvironmentInstanceNextPart(AID fromAgent, Object nextPart) throws ServiceException;
	
	/**
	 * This method will reset the Hashtable of environment changes for the next environment instance.
	 *
	 * @throws ServiceException the ServiceException
	 */
	public void resetEnvironmentInstanceNextParts() throws ServiceException;
	
	/**
	 * Returns the parts for the next environment instances, coming from all container and all simulating agents {e.g. @link SimulationAgent}.
	 *
	 * @return the environment instance next parts as pair container name to HashTable of agent answers
	 * @throws ServiceException the ServiceException
	 */
	public Hashtable<String, Hashtable<AID, Object>> getEnvironmentInstanceNextParts() throws ServiceException;
	
	/**
	 * Returns the parts for the next environment instances that already arrived at the MainContainer.
	 *
	 * @return the environment instance next parts
	 * @throws ServiceException the ServiceException
	 */
	public Hashtable<AID, Object> getEnvironmentInstanceNextPartsFromMain() throws ServiceException;
	
	/**
	 * Registers a DisplayAgent at the SimulationService.
	 *
	 * @see AbstractDisplayAgent
	 * @see SimulationService
	 * 
	 * @param displayAgent the instance of the display agent
	 * @throws ServiceException the service exception
	 */
	public void displayAgentRegister(AbstractDisplayAgent<?> displayAgent) throws ServiceException;

	/**
	 * Unregisters a DisplayAgent at the SimulationService.
	 *
	 * @see AbstractDisplayAgent
	 * @see SimulationService
	 * 
	 * @param displayAgent the instance of the display agent
	 * @throws ServiceException the service exception
	 */
	public void displayAgentUnregister(AbstractDisplayAgent<?> displayAgent) throws ServiceException;
	
	/**
	 * Notifies all registered DisplayAgents about a new EnvironmentModel.
	 *
	 * @see AbstractDisplayAgent
	 * @param envModel the EnvironmentModel 
	 * @throws ServiceException the service exception
	 */
	public void displayAgentSetEnvironmentModel(EnvironmentModel envModel) throws ServiceException;
	
	/**
	 * Notifies all registered DisplayAgents.
	 *
	 * @param notification the EnvironmentNotification
	 * @throws ServiceException the service exception
	 */
	public void displayAgentNotification(EnvironmentNotification notification) throws ServiceException;
	
	
}
