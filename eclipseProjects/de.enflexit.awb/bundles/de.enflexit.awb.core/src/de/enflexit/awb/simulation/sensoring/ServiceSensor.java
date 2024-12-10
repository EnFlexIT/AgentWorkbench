package de.enflexit.awb.simulation.sensoring;

import de.enflexit.awb.simulation.agents.SimulationAgent;
import de.enflexit.awb.simulation.environment.EnvironmentModel;
import de.enflexit.awb.simulation.transaction.EnvironmentNotification;
import jade.core.Location;

/**
 * This is the class for the so called ServiceSensor, which is an attribute of
 * the {@link SimulationAgent}. It transfers information about new environment 
 * models, notifications in relation to the environment, the location where an
 * agent has to migrate and that a agent will be shut down.  
 * 
 * @see SimulationAgent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ServiceSensor {

	private ServiceSensorInterface myServiceSensor;
	
	/**
	 * Instantiates a new service sensor.
	 * @param serviceSensor the service sensor
	 */
	public ServiceSensor(ServiceSensorInterface serviceSensor) {
		myServiceSensor = serviceSensor;		
	}
	
	/**
	 * Gets the current service sensor.
	 * @return the service sensor
	 */
	public ServiceSensorInterface getServiceSensor() {
		return myServiceSensor;
	}
	
	/**
	 * Puts a new environment model to the agent.
	 * @param environmentModel the environment model
	 * @param aSynchron true, if synchronously
	 */
	public void putEnvironmentModel(EnvironmentModel environmentModel, boolean aSynchron) {
		myServiceSensor.setEnvironmentModel(environmentModel, aSynchron);
	}
	
	/**
	 * Notifies an agent about things, taht happen in the environment.
	 * @param notification the EnvironmentNotification
	 */
	public void notifyAgent(EnvironmentNotification notification) {
		myServiceSensor.setNotification(notification);
	}

	/**
	 * Does the <code>doDelete()</code> method of the agent.
	 */
	public void doDelete() {
		myServiceSensor.doDelete();
	}
	
	/**
	 * Sets that the simulation is to be paused or not.
	 * @param isPauseSimulation the new pause simulation
	 */
	public void setPauseSimulation(boolean isPauseSimulation ) {
		myServiceSensor.setPauseSimulation(isPauseSimulation);
	}
	
	/**
	 * Puts the Location to the agent, where the agent has to migrate to.
	 * @param newLocation the new location
	 */
	public void putMigrationLocation(Location newLocation) {
		myServiceSensor.setMigration(newLocation);
	}

	
}
