package de.enflexit.awb.simulation.sensoring;

import jade.core.AID;

import java.util.Hashtable;

import de.enflexit.awb.core.simulation.agents.SimulationManagerAgent;
import de.enflexit.awb.simulation.agents.SimulationAgent;
import de.enflexit.awb.simulation.transaction.EnvironmentNotification;

/**
 * This is the class for the so called ServiceSensorManager, which is an attribute 
 * of the {@link SimulationManagerAgent}. It transfers information about environment 
 * changes caused by {@link SimulationAgent} and notifications in relation to the 
 * environment.  
 * 
 * @see SimulationManagerAgent 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ServiceSensorManager {

	protected SimulationManagerAgent myManager;
	
	/**
	 * Instantiates a new service sensor manager.
	 * @param agent the agent
	 */
	public ServiceSensorManager(SimulationManagerAgent agent) {
		myManager = agent;		
	}
	
	/**
	 * Puts the agent answers to the manager.
	 *
	 * @param agentAnswers the agent answers
	 * @param aSynchron true, if synchronously
	 */
	public void putAgentAnswers(Hashtable<AID, Object> agentAnswers, boolean aSynchron) {
		myManager.putAgentAnswers(agentAnswers, aSynchron);
	}
	
	/**
	 * Notifies a manager agent about things that happen in the environment.
	 * @param notification the notification
	 */
	public void notifyManager(EnvironmentNotification notification) {
		myManager.setManagerNotification(notification);
	}

	/**
	 * Notifies the manager to pause the simulation.
	 * @param isPauseSimulation the is pause simulation
	 */
	public void notifyPauseSimulation(boolean isPauseSimulation) {
		myManager.setPauseSimulation(isPauseSimulation);
	}
	
}
