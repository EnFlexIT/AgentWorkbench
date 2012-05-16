package gasmas.agents.components;

import gasmas.clustering.behaviours.ClusteringBehaviour;
import gasmas.clustering.behaviours.CycleClusteringBehaviour;
import gasmas.clustering.coalitions.CoalitionBehaviour;
import jade.core.ServiceException;
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

		while (this.myEnvironmentModel == null) {

			try {
				SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
				EnvironmentModel envModel = simHelper.getEnvironmentModel();
				if (envModel != null) {
					this.myEnvironmentModel = envModel;
					break;
				}
			} catch (ServiceException e) {
				e.printStackTrace();
			}

		}

		this.myNetworkModel = (NetworkModel) this.myEnvironmentModel.getDisplayEnvironment();

		ClusteringBehaviour clusteringBehaviour = new CycleClusteringBehaviour(this, myNetworkModel);
		this.addBehaviour(new CoalitionBehaviour(this, myEnvironmentModel, myNetworkModel, clusteringBehaviour));
	}
}
