package agentgui.simulationService.sensoring;

import jade.core.AID;

import java.util.Hashtable;
import java.util.Vector;

import agentgui.simulationService.transaction.EnvironmentNotification;


public class ServiceActuatorManager {

	private Vector<ServiceSensorManager> serviceSensors = new Vector<ServiceSensorManager>();
	
	/**
	 * Opportunity for the agents to plugIn to this service
	 * @param currSensor
	 */
	public void plugIn(ServiceSensorManager currSensor) {
		serviceSensors.addElement(currSensor);		
	}
	/**
	 * Method to plugOut from this actuator
	 * @param currSensor
	 */
	public void plugOut(ServiceSensorManager currSensor) {
		serviceSensors.removeElement(currSensor);
	}
	
	/**
	 * This method returns the ServiceSensor-Instance identified by the AID of the agent
	 * @param aid
	 * @return
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
	 * It can be either used to do this asynchron or synchron, but it. 
	 * is highly recommended to do this asynchron, so that the agencie 
	 * can act parallel and not sequential. 
	 * @param currEnvironmentModel
	 * @param aSynchron
	 */
	public void putAgentAnswers(AID aid, Hashtable<AID, Object> agentAnswers, boolean aSynchron) {
		ServiceSensorManager sensor = getSensor(aid);
		if (sensor!=null) {
			sensor.putAgentAnswers(agentAnswers, aSynchron);
		}		
	}

	/**
	 * 
	 * @param agentAID
	 * @param notification
	 * @param aSynchron
	 * @return
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
	
}
