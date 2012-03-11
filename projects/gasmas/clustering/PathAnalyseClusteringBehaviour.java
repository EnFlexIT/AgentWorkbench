package gasmas.clustering;

import jade.core.ServiceException;
import jade.core.behaviours.SimpleBehaviour;

import java.util.ArrayList;
import java.util.HashSet;

import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;

public class PathAnalyseClusteringBehaviour extends SimpleBehaviour {

	private static final int STEPS = 1000;

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
		ClusterIdentifier clusterIdentifier = new ClusterIdentifier(environmentModel, simulationServiceHelper);
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
		while (newNetworkModel != null) {
			newNetworkModel = clusterIdentifier.search(startPathAnalysis(newNetworkModel), networkModel);
		}
	}

	private NetworkModel startPathAnalysis(NetworkModel newNetworkModel) {
		Ant.setClone(true);
		HashSet<Ant> ants = new HashSet<Ant>();
		ants.add(new Ant(thisNetworkComponent));
		ArrayList<Ant> nextRunAnts = new ArrayList<Ant>(ants);
		for (int step = 0; step < PathAnalyseClusteringBehaviour.STEPS; step++) {
			ArrayList<Ant> runAnts = new ArrayList<Ant>(nextRunAnts);
			nextRunAnts = new ArrayList<Ant>();
			for (Ant ant : runAnts) {
				nextRunAnts.addAll(ant.run(newNetworkModel));
			}
			ants.addAll(nextRunAnts);
		}
		AntDistributionMatrix antDistributionMatrix = new AntDistributionMatrix(new ArrayList<Ant>(ants));
		String x = antDistributionMatrix.findFrequentPathComponent();
		System.out.println(x);
		NetworkComponent networkComponent = newNetworkModel.getNetworkComponent(x);
		newNetworkModel.removeNetworkComponent(networkComponent);
		return newNetworkModel;
	}

	@Override
	public boolean done() {
		return true;
	}

}
