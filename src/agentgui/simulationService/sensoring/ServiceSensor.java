package agentgui.simulationService.sensoring;

import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.environment.EnvironmentModel;
import jade.core.Location;

public class ServiceSensor {

	protected SimulationAgent myAgent;
	
	public ServiceSensor(SimulationAgent agent) {
		myAgent = agent;		
	}
	
	public void putEnvironmentModel(EnvironmentModel environmentModel, boolean aSynchron) {
		myAgent.setEnvironmentModel(environmentModel, aSynchron);
	}
	
	public void notifyAgent(Object notification, boolean aSynchron) {
		myAgent.setNotification(notification, aSynchron);
	}

	public void doDelete() {
		myAgent.doDelete();
	}
	
	public void putMigrationInfo(Location newLocation) {
		myAgent.setMigration(newLocation);
	}

	
}
