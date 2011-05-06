package agentgui.simulationService.sensoring;

import jade.core.AID;

import java.util.Hashtable;

import agentgui.simulationService.agents.SimulationManagerAgent;
import agentgui.simulationService.transaction.EnvironmentNotification;

public class ServiceSensorManager {

	protected SimulationManagerAgent myManager;
	
	public ServiceSensorManager(SimulationManagerAgent agent) {
		myManager = agent;		
	}
	
	public void putAgentAnswers(Hashtable<AID, Object> agentAnswers, boolean aSynchron) {
		myManager.putAgentAnswers(agentAnswers, aSynchron);
	}
	
	public void notifyManager(EnvironmentNotification notification) {
		myManager.setManagerNotification(notification);
	}
	
}
