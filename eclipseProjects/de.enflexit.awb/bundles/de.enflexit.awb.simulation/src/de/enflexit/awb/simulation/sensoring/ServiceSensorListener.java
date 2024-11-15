package de.enflexit.awb.simulation.sensoring;

import java.io.Serializable;

import de.enflexit.awb.simulation.agents.SimulationAgent;
import de.enflexit.awb.simulation.behaviour.SimulationServiceBehaviour;
import de.enflexit.awb.simulation.transaction.EnvironmentNotification;

/**
 * This Sensor Listener can be used with the {@link SimulationAgent} or the {@link SimulationServiceBehaviour} class
 * and enables to 
 * 
 * @see SimulationAgent#addSimulationServiceListener(ServiceSensorListener)
 * @see SimulationAgent#removeSimulationServiceListener(ServiceSensorListener)
 * or
 * @see SimulationServiceBehaviour#addSimulationServiceListener(ServiceSensorListener)
 * @see SimulationServiceBehaviour#removeSimulationServiceListener(ServiceSensorListener)
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface ServiceSensorListener extends Serializable {

	/**
	 * Will be invoked, if the environment (model) changed.
	 */
	public void onEnvironmentStimulus();

	/**
	 * If an environment notification arrives the Agent, this method will be invoked.
	 *
	 * @param notification the notification
	 * @return the environment notification
	 */
	public EnvironmentNotification onEnvironmentNotification(EnvironmentNotification notification);
	
	
}
