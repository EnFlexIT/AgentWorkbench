package mas.service.sensoring;

import jade.core.Location;
import mas.service.agents.SimulationAgent;
import mas.service.environment.EnvironmentModel;

public class ServiceSensor {

	protected SimulationAgent myAgent;
	
	public ServiceSensor(SimulationAgent agent) {
		myAgent = agent;		
	}
	
	public void putEnvironmentModel(EnvironmentModel environmentModel, boolean aSynchron) {
		myAgent.setEnvironmentModel(environmentModel, aSynchron);
	}
	
	public void putMigrationInfo(Location newLocation) {
		myAgent.setMigration(newLocation);
	}
	
}
