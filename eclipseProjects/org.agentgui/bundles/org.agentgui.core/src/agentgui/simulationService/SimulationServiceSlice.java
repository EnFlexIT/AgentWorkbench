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
import jade.core.Service;

import java.util.Hashtable;
import java.util.Vector;

import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.agents.SimulationManagerAgent;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.load.LoadAgentMap.AID_Container;
import agentgui.simulationService.sensoring.ServiceActuator;
import agentgui.simulationService.sensoring.ServiceSensor;
import agentgui.simulationService.transaction.EnvironmentManagerDescription;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * Here the remotely accessible methods of the SimulationService can be found.
 */
public interface SimulationServiceSlice extends Service.Slice {

	// ----------------------------------------------------------
	// --- Horizontal commands of the service -------------------
	// ----------------------------------------------------------
	
	// ----------------------------------------------------------
	// --- Methods to synchronise the time ----------------------
	/** The Constant SERVICE_SYNCH_GET_REMOTE_TIME. */
	static final String SERVICE_SYNCH_GET_REMOTE_TIME = "service-synch-get-remote-time";
	/** The Constant SERVICE_SYNCH_SET_TIME_DIFF. */
	static final String SERVICE_SYNCH_SET_TIME_DIFF = "service-synch-set-time-diff";
	
	/**
	 * Returns the remote time as long.
	 *
	 * @return the remote time
	 * @throws IMTPException the IMTPException
	 */
	public long getRemoteTime() throws IMTPException;
	/**
	 * Sets the remote time difference in milliseconds for synchronised time.
	 *
	 * @param timeDifference the new remote time diff
	 * @throws IMTPException the IMTPException
	 */
	public void setRemoteTimeDiff(long timeDifference) throws IMTPException;
	
	
	
	// ----------------------------------------------------------
	// --- Method on the Manager-Agent --------------------------
	/** The Constant SIM_SET_MANAGER_AGENT. */
	static final String SIM_SET_MANAGER_AGENT = "set-manager";
	/**
	 * Sets the {@link EnvironmentManagerDescription} of the manager agent.
	 *
	 * @param envManager the new manager agent
	 * @throws IMTPException the IMTPException
	 * @see EnvironmentManagerDescription
	 */
	public void setManagerAgent(EnvironmentManagerDescription envManager) throws IMTPException;
	
	
	// ----------------------------------------------------------
	// --- Methods on the EnvironmentModel ----------------------
	/** The Constant SIM_SET_ENVIRONMENT_MODEL_FROM_SETUP. */
	static final String SIM_GET_ENVIRONMENT_MODEL_FROM_SETUP = "sim-set-environment-model-from-setup";
	/** The Constant SIM_SET_ENVIRONMENT_MODEL. */
	static final String SIM_SET_ENVIRONMENT_MODEL = "sim-set-environment-model";
	/** The Constant SIM_STEP_SIMULATION. */
	static final String SIM_STEP_SIMULATION = "sim-step-simulation";
	/** The Constant SIM_SET_ANSWERS_EXPECTED. */
	static final String SIM_SET_ANSWERS_EXPECTED = "sim-set-number-expected";
	/** The Constant SIM_NOTIFY_AGENT. */
	static final String SIM_NOTIFY_AGENT = "sim-notify-agent";
	/** The Constant SIM_NOTIFY_MANAGER. */
	static final String SIM_NOTIFY_MANAGER = "notify-manager";
	/** The Constant SIM_NOTIFY_MANAGER_PUT_AGENT_ANSWERS. */
	static final String SIM_NOTIFY_MANAGER_PUT_AGENT_ANSWERS = "notify-manager-put-agent-answers";
	/** The Constant SIM_SET_ENVIRONMENT_NEXT_PART. */
	static final String SIM_SET_ENVIRONMENT_NEXT_PART = "set-environment-next-part";
	/** The Constant SIM_GET_ENVIRONMENT_NEXT_PARTS. */
	static final String SIM_GET_ENVIRONMENT_NEXT_PARTS = "get-environment-next-parts";
	/** The Constant SIM_RESET_ENVIRONMENT_NEXT_PARTS. */
	static final String SIM_RESET_ENVIRONMENT_NEXT_PARTS = "reset-environment-next-parts";
	
	
	/**
	 * Returns the {@link EnvironmentModel} from the setup within the end user application.
	 *
	 * @return the environment model from the setup
	 * @throws IMTPException the IMTP exception
	 */
	public EnvironmentModel getEnvironmentModelFromSetup() throws IMTPException;
	
	/**
	 * Can be used in order to set and distribute an {@link EnvironmentModel} without
	 * a direct notification to the involved agents.
	 *
	 * @param envModel the current or new EnvironmentModel instance
	 * @param notifySensorAgents true, if the sensor agents should be also notified 
	 * @throws IMTPException the IMTPException
	 * @see EnvironmentModel
	 */
	public void setEnvironmentModel(EnvironmentModel envModel, boolean notifySensorAgents) throws IMTPException;
	
	/**
	 * Steps a simulation by using the current {@link EnvironmentModel} and the number of expected changes / answers.
	 *
	 * @param envModel the current or new EnvironmentModel
	 * @param aSynchron true, if the notification has to be done asynchronously
	 * @throws IMTPException the iMTP exception
	 * @see EnvironmentModel
	 */
	public void stepSimulation(EnvironmentModel envModel, boolean aSynchron) throws IMTPException;
	
	/**
	 * Sets the number of changes/answers expected from the simulation cycle.
	 *
	 * @param answersExpected the number of answers expected
	 * @throws IMTPException the IMTPException
	 */
	public void setAnswersExpected(int answersExpected) throws IMTPException;
	
	/**
	 * Notifies a specified sensor agent by using an EnvironmentNotification.
	 *
	 * @param agentAID the agent AID
	 * @param notification the notification
	 * @return true, if successful
	 * @throws IMTPException the IMTPException
	 * 
	 * @see ServiceSensor
	 * @see ServiceActuator
	 * @see SimulationAgent
	 * @see EnvironmentNotification
	 */
	public boolean notifyAgent(AID agentAID, EnvironmentNotification notification) throws IMTPException;
	
	/**
	 * Notifies a SimulationManager by using an EnvironmentNotification.
	 *
	 * @param notification the notification
	 * @return true, if successful
	 * @throws IMTPException the IMTPException
	 * @see SimulationManagerAgent
	 * @see EnvironmentNotification
	 */
	public boolean notifyManager(EnvironmentNotification notification) throws IMTPException;
	
	/**
	 * Sends the current set of agent answers to the manager agent of the environment.
	 *
	 * @param allAgentAnswers the Hashtable of all agent answers
	 * @throws IMTPException the IMTPException
	 */
	public void notifyManagerPutAgentAnswers(Hashtable<AID, Object> allAgentAnswers) throws IMTPException;
	
	/**
	 * Sends the local next parts of the environment-model to the Main-Container.
	 *
	 * @param nextPartsLocal the Hashtable of local environment changes, coming from different agents
	 * @throws IMTPException the IMTPException
	 */
	public void setEnvironmentInstanceNextPart(Hashtable<AID, Object> nextPartsLocal) throws IMTPException;
	
	/**
	 * This method returns the complete environment-model-changes from the Main-Container.
	 *
	 * @return the environment instance next parts
	 * @throws IMTPException the IMTPException
	 */
	public Hashtable<AID, Object> getEnvironmentInstanceNextParts() throws IMTPException;
	
	/**
	 * This method resets the hash with the single environment-model-changes.
	 *
	 * @throws IMTPException the IMTPException
	 */
	public void resetEnvironmentInstanceNextParts() throws IMTPException;
	
	
	// ----------------------------------------------------------
	// --- Methods to work on agents ----------------------------
	/** The Constant SERVICE_STOP_SIMULATION_AGENTS. */
	static final String SERVICE_STOP_SIMULATION_AGENTS = "stop-simulation-agents";
	/** The Constant SIM_PAUSE_SIMULATION. */
	static final String SIM_PAUSE_SIMULATION = "sim-pause";
	/** The Constant SERVICE_SET_AGENT_MIGRATION. */
	static final String SERVICE_SET_AGENT_MIGRATION = "set-agent-migration";
	
	/**
	 * Stops the simulation agents.
	 *
	 * @throws IMTPException the IMTPException
	 */
	public void stopSimulationAgents() throws IMTPException;
	/**
	 * Sets to pause the simulation.
	 *
	 * @param pauseSimulation the new pause simulation
	 * @throws IMTPException the IMTPException
	 */
	public void setPauseSimulation(boolean pauseSimulation) throws IMTPException;
	
	/**
	 * Sets the new locations to the agents.
	 *
	 * @param transferAgents the agents to migrate
	 * @throws IMTPException the IMTPException
	 * @see AID_Container
	 */
	public void setAgentMigration(Vector<AID_Container> transferAgents) throws IMTPException;
	
	// ----------------------------------------------------------
	// --- Methods for updating display agents ------------------
	/** The Constant SERVICE_DISPLAY_AGENT_NOTIFY. */
	static final String SERVICE_DISPLAY_AGENT_SET_ENVIRONMENT_MODEL = "service-display-agent-set-environment-model";
	static final String SERVICE_DISPLAY_AGENT_NOTIFICATION = "service-display-agent-notification";
	static final String SERVICE_DISPLAY_CONTAINER_UN_REGISTRATION = "service-display-container-un-registration";
	
	/**
	 * Notifies all registered DisplayAgents about a new EnvironmentModel.
	 * @param envModel the {@link EnvironmentModel}
	 * @throws IMTPException the iMTP exception
	 */
	public void displayAgentSetEnvironmentModel(EnvironmentModel envModel) throws IMTPException;
	/**
	 * Notifies all registered DisplayAgents.
	 * @param notification the notification
	 * @throws IMTPException the iMTP exception
	 */
	public void displayAgentNotification(EnvironmentNotification notification) throws IMTPException;
	
	
	/**
	 * Does the (un-)registration for a display agent in a container .
	 *
	 * @param containerName the container name
	 * @param isRegisterContainer the is register container
	 * @throws IMTPException the IMTP exception
	 */
	public void doDisplayAgentContainerUnRegister(String containerName, boolean isRegisterContainer) throws IMTPException;

	
}
