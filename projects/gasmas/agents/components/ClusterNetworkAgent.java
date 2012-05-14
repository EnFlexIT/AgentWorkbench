package gasmas.agents.components;

import gasmas.clustering.coalitions.ClusterNetworkAgentCoalitionBehaviour;
import jade.core.ServiceException;

import java.util.ArrayList;

import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.environment.EnvironmentModel;

public class ClusterNetworkAgent extends SimulationAgent {

	public static final String CLUSTER_AGENT_Prefix = "C_";
	private static int clusterIDCounter = 0;
	
	private ClusterNetworkComponent clusterNetworkComponent;

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

		clusterNetworkComponent = (ClusterNetworkComponent) this.getArguments()[0];
		@SuppressWarnings("unchecked")
		ArrayList<ClusterNetworkComponent> biggerClusters = (ArrayList<ClusterNetworkComponent>) this.getArguments()[1];
		this.addBehaviour(new ClusterNetworkAgentCoalitionBehaviour(this, myEnvironmentModel, clusterNetworkComponent, biggerClusters));
	}

	public static int getFreeID() {
		return clusterIDCounter++;
	}

	public void setClusterNetworkComponent(ClusterNetworkComponent clusterNetworkComponent) {
		this.clusterNetworkComponent=clusterNetworkComponent;
	}

}
