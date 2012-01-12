package gasmas.agents.components;

import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.environment.EnvironmentModel;

public class CompressorAgent extends SimulationAgent {

	private static final long serialVersionUID = 2792727750579482552L;

	private NetworkModel myNetworkModel = null;	
	
	@Override
	protected void setup() {
		
		super.setup();

		while(this.myEnvironmentModel==null) {
			
			try {
				SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
				EnvironmentModel envModel =  simHelper.getEnvironmentModel();
				if (envModel!=null) {
					this.myEnvironmentModel = envModel;
					break;
				}
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			
			
		}
		
		this.myNetworkModel = (NetworkModel) this.myEnvironmentModel.getDisplayEnvironment();

		this.addBehaviour(new ClusterBeha(myNetworkModel.getCopy()));
		
	}
	
	
	class ClusterBeha extends OneShotBehaviour {

		private NetworkModel myNetworkModel = null;	
		
		public ClusterBeha(NetworkModel networkModel) {
			myNetworkModel = networkModel;
		}
		
		@Override
		public void action() {

			myNetworkModel.getEdges();
			
			
		}
		
	}
	
	
	
	
	
	
}
