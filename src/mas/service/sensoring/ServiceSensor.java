package mas.service.sensoring;

import mas.service.SimulationAgent;
import mas.service.environment.EnvironmentModel;

public class ServiceSensor {

	private SimulationAgent myAgent;
	
	public ServiceSensor(SimulationAgent agent) {
		myAgent = agent;		
	}
	
	public void putEnvironmentModel(EnvironmentModel environmentModel, boolean aSynchron) {
		myAgent.setEnvironmentModel(environmentModel, aSynchron);
	}
	
	
}
