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

public class ControlValveAgent extends GenericNetworkAgent {

	private static final long serialVersionUID = 5137325765165987781L;

	@Override
	protected void setup() {

		super.setup();

//		startCoalitionBehaviour();
	}

	private void startCoalitionBehaviour() {
		ClusteringBehaviour clusteringBehaviour = new CycleClusteringBehaviour(this, myNetworkModel);
		this.addBehaviour(new CoalitionBehaviour(this, myEnvironmentModel, myNetworkModel, clusteringBehaviour));
	}
}
