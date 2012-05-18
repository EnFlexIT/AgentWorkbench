package gasmas.agents.components;

import gasmas.clustering.behaviours.ClusteringBehaviour;
import gasmas.clustering.behaviours.CycleClusteringBehaviour;
import gasmas.clustering.coalitions.ClusterNACoalitionBehaviour;
import gasmas.clustering.coalitions.CoalitionBehaviour;
import jade.core.ServiceException;
import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.agents.SimulationAgent;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.transaction.EnvironmentNotification;

public class ClusterNetworkAgent extends SimulationAgent {

	public static final String CLUSTER_AGENT_Prefix = "C_";
	private static int clusterIDCounter = 0;
	
	private ClusterNetworkComponent clusterNetworkComponent;

	private ClusterNACoalitionBehaviour clusterNACoalitionBehaviour;

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
		clusterNACoalitionBehaviour = new ClusterNACoalitionBehaviour(this, clusterNetworkComponent);
		this.addBehaviour(clusterNACoalitionBehaviour);
	}

	public static int getFreeID() {
		return clusterIDCounter++;
	}

	public void setClusterNetworkComponent(ClusterNetworkComponent clusterNetworkComponent) {
		this.clusterNetworkComponent=clusterNetworkComponent;
	}

	/**
	 * This method will be executed if a ManagerNotification arrives this agent.
	 *
	 * @param notification the notification
	 */
	@Override
	protected void onEnvironmentNotification(EnvironmentNotification notification) {
		if (notification.getNotification() instanceof ClusterNetworkComponent) {
			clusterNetworkComponent = (ClusterNetworkComponent) notification.getNotification();
			startCoalitionBehaviour();
		}
	};

	private void startCoalitionBehaviour() {
		NetworkModel clusteredNM = getClusteredModel().getCopy();
		ClusteringBehaviour clusteringBehaviour = new CycleClusteringBehaviour(this, clusteredNM);
		this.addBehaviour(new CoalitionBehaviour(this, myEnvironmentModel, clusteredNM, clusteringBehaviour));
	}

	private NetworkModel getClusteredModel() {
		NetworkModel networkModel = (NetworkModel) myEnvironmentModel.getDisplayEnvironment();
		NetworkModel clusteredNM = networkModel.getAlternativeNetworkModel().get(ClusteringBehaviour.CLUSTER_NETWORK_MODL_NAME);
		return clusteredNM;
	}
}
