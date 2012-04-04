package gasmas.clustering.randomWalk;

import gasmas.clustering.ClusterIdentifier;
import jade.core.ServiceException;
import jade.core.behaviours.SimpleBehaviour;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;

public class PathAnalyseClusteringBehaviour extends SimpleBehaviour {

	private static final int STEPS = 500;

	private EnvironmentModel environmentModel;
	private NetworkModel networkModel;
	private NetworkComponent thisNetworkComponent;
	private SimulationServiceHelper simulationServiceHelper;

	public PathAnalyseClusteringBehaviour(EnvironmentModel environmentModel, NetworkComponent thisNetworkComponent) {
		this.environmentModel = environmentModel;
		this.networkModel = (NetworkModel) environmentModel.getDisplayEnvironment();
		this.thisNetworkComponent = thisNetworkComponent;
	}

	@Override
	public void action() {
		try {
			simulationServiceHelper = (SimulationServiceHelper) myAgent.getHelper(SimulationService.NAME);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		ClusterIdentifier clusterIdentifier = new ClusterIdentifier();
		NetworkModel copyNetworkModel = networkModel.getCopy();
		copyNetworkModel.setAlternativeNetworkModel(null);
		this.networkModel.getAlternativeNetworkModel().put("ClusteredModel", copyNetworkModel);
		try {
			simulationServiceHelper.setEnvironmentModel(this.environmentModel, true);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		System.out.println("Begin Ant Cluster Analysis");
		analyseClusters(copyNetworkModel, clusterIdentifier);
	}

	public void analyseClusters(NetworkModel networkModel, ClusterIdentifier clusterIdentifier) {
		NetworkModel newNetworkModel = networkModel.getCopy();
		while (newNetworkModel != null && newNetworkModel.getNetworkComponent(thisNetworkComponent.getId()) != null) {
			newNetworkModel = clusterIdentifier.search(startPathAnalysis(newNetworkModel), networkModel);
		}
	}

	private NetworkModel startPathAnalysis(NetworkModel newNetworkModel) {
		PathSearchBotDistributionMatrix antDistributionMatrix = new PathSearchBotRunner().runBotsAndGetDistributionMatrix(newNetworkModel, thisNetworkComponent.getId(),
				PathAnalyseClusteringBehaviour.STEPS);
		String x = antDistributionMatrix.findFrequentPathComponent();
		NetworkComponent networkComponent = newNetworkModel.getNetworkComponent(x);
		newNetworkModel.removeNetworkComponent(networkComponent);
		return newNetworkModel;
	}

	@Override
	public boolean done() {
		return true;
	}

}
