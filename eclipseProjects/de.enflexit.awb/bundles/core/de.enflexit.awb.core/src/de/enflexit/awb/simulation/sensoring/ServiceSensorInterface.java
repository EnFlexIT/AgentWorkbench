package de.enflexit.awb.simulation.sensoring;

import de.enflexit.awb.simulation.SimulationService;
import de.enflexit.awb.simulation.agents.SimulationAgent;
import de.enflexit.awb.simulation.behaviour.SimulationServiceBehaviour;
import de.enflexit.awb.simulation.environment.EnvironmentModel;
import de.enflexit.awb.simulation.transaction.EnvironmentNotification;
import jade.core.AID;
import jade.core.Location;

/**
 * This interface is the super class for connections to the {@link ServiceSensor}.
 * It is used in the {@link SimulationAgent} or in the {@link SimulationServiceBehaviour} 
 * in order to connect to the {@link SimulationService} and its sensor system.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface ServiceSensorInterface {

	/**
	 * This method will be used by the ServiceActuator (class) to inform
	 * this agent about its new migration location.
	 *
	 * @param newLocation the new Location for the migration
	 */
	public abstract void setMigration(Location newLocation);

	/**
	 * This method will be used by the ServiceActuator (class) to inform
	 * that the simulation is paused or not.
	 *
	 * @param isPauseSimulation the new pause simulation
	 */
	public abstract void setPauseSimulation(boolean isPauseSimulation);
	
	/**
	 * This method will be used by the {@link ServiceActuator} to inform
	 * this agent about changes in the environment. It can be either used
	 * to do this asynchronously or synchronously. It is highly recommended 
	 * to do this asynchronously, so that the agency can act parallel and not
	 * sequentially.
	 *
	 * @param envModel the current or new EnvironmentModel
	 * @param aSynchron true, if this should be done asynchronously
	 */
	public abstract void setEnvironmentModel(EnvironmentModel envModel, boolean aSynchron);

	/**
	 * This method can be invoked from the simulation service, if
	 * a notification for the manager has to be delivered.
	 *
	 * @param notification the new notification
	 */
	public abstract void setNotification(EnvironmentNotification notification);

	/**
	 * Will delete/kill the current agent.
	 */
	public abstract void doDelete();

	/**
	 * Returns the AID of the current Agent.
	 * @return the AID
	 */
	public abstract AID getAID();
	
}