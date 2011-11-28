package gasmas.agents.components;

import jade.core.ServiceException;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.environment.EnvironmentModel;

public class CompressorAgent extends SimulationAgent {

	private static final long serialVersionUID = 2792727750579482552L;

	SimulationServiceHelper simHelper = null;
	
	@Override
	protected void setup() {
		super.setup();

//		while(this.myEnvironmentModel==null) {
//			
//			if (simHelper==null) {
//				try {
//					simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
//					EnvironmentModel envModel =  simHelper.getEnvironmentModel();
//					if (envModel!=null) {
//						this.myEnvironmentModel = envModel;
//						break;
//					}
//				} catch (ServiceException e) {
//					e.printStackTrace();
//				}
//			}
//		}
		
	}
	
}
