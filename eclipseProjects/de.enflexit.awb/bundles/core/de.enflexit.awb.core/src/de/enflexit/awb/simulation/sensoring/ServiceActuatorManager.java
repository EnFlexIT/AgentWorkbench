package de.enflexit.awb.simulation.sensoring;

import jade.core.AID;
import jade.core.Location;

import java.util.Hashtable;
import java.util.Vector;

import de.enflexit.awb.simulation.SimulationService;
import de.enflexit.awb.simulation.agents.SimulationAgent;
import de.enflexit.awb.simulation.agents.SimulationManagerAgent;
import de.enflexit.awb.simulation.transaction.EnvironmentNotification;

/**
 * This is the class for an actuator of the {@link SimulationService}, which is able to 
 * inform all connected {@link SimulationManagerAgent} with an integrated {@link ServiceSensorManager} 
 * about the environment changes caused by {@link SimulationAgent} (sometimes called agentAnswers). 
 * 
 * @see SimulationService
 * @see SimulationManagerAgent
 * @see ServiceSensorManager
 * @see EnvironmentNotification
 * @see Location
 *  
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ServiceActuatorManager {

	private Vector<ServiceSensorManager> serviceSensors = new Vector<ServiceSensorManager>();
	
	/**
	 * Method for agents to plug-in to this actuator.
	 * @param currSensor the ServiceSensorManager to plug-in
	 */
	public void plugIn(ServiceSensorManager currSensor) {
		serviceSensors.addElement(currSensor);		
	}
	/**
	 * Method to plug-out from this actuator
	 * @param currSensor the ServiceSensorManager to plug-out
	 */
	public void plugOut(ServiceSensorManager currSensor) {
		serviceSensors.removeElement(currSensor);
	}
	
	/**
	 * This method returns the ServiceSensor-Instance identified by the AID of the agent.
	 *
	 * @param aid the AID of the agent
	 * @return the ServiceSensorManager
	 */
	public ServiceSensorManager getSensor(AID aid) {
		
		Object[] arrLocal = serviceSensors.toArray();
		for (int i = arrLocal.length-1; i>=0; i--) {
			ServiceSensorManager sensor = (ServiceSensorManager)arrLocal[i];
			AID sensorAgentAID = sensor.myManager.getAID();
			if (sensorAgentAID.equals(aid)) {
				return sensor;				
			}
		}
		return null;
	}
		
	/**
	 * This method informs the manager agent about the answers of all involved agents.
	 * It can be either used to do this asynchronously or asynchronously, but it.
	 * is highly recommended to do this asynchronously, so that the agents
	 * can act parallel and not sequential.
	 *
	 * @param managerAID the AID of the manager agent
	 * @param agentAnswers the agent answers
	 * @param aSynchron true, if this should be don asynchronously
	 */
	public void putAgentAnswers(AID managerAID, Hashtable<AID, Object> agentAnswers, boolean aSynchron) {
		ServiceSensorManager sensor = this.getSensor(managerAID);
		if (sensor!=null) {
			sensor.putAgentAnswers(agentAnswers, aSynchron);
		}		
	}

	/**
	 * Notify manager.
	 *
	 * @param managerAID the AID of the manager 
	 * @param notification the EnvironmentNotification
	 * @return true, if successful
	 */
	public boolean notifyManager(AID managerAID, EnvironmentNotification notification) {
		
		ServiceSensorManager sensor = getSensor(managerAID);
		if (sensor==null) {
			// --- Manager/Sensor was NOT found ---
			return false;
		} else {
			// --- Manager/Sensor was found -------
			sensor.notifyManager(notification);
			return true;
		}		
	}
	/**
	 * Notify that simulation has to be paused or not.
	 * @param isPauseSimulation the is pause simulation
	 */
	public void notifyManagerPauseSimulation(final boolean isPauseSimulation) {
		Object[] arrLocal = serviceSensors.toArray();
		for (int i = arrLocal.length-1; i>=0; i--) {
			((ServiceSensorManager)arrLocal[i]).notifyPauseSimulation(isPauseSimulation);
		}
	}
	
}
