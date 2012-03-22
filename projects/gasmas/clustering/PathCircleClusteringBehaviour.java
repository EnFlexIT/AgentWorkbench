package gasmas.clustering;

import jade.core.ServiceException;
import jade.core.behaviours.SimpleBehaviour;

import java.util.ArrayList;
import java.util.HashSet;

import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;

public class PathCircleClusteringBehaviour extends SimpleBehaviour {

	private static final int STEPS = 100;

	private EnvironmentModel environmentModel;
	private NetworkModel networkModel;
	private NetworkComponent thisNetworkComponent;
	private SimulationServiceHelper simulationServiceHelper;

	public PathCircleClusteringBehaviour(EnvironmentModel environmentModel, NetworkComponent thisNetworkComponent) {
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
		System.out.println("Begin Ant Circle Cluster Analysis");
		analyseClusters();
		System.out.println("End Ant Circle Cluster Analysis");
	}

	public void analyseClusters() {
		Subgraph subgraph = startPathAnalysis(networkModel);
		NetworkModel copyNetworkModel = networkModel.getCopy();
		copyNetworkModel.setAlternativeNetworkModel(null);
		ClusterNetworkComponent clusterNetworkComponent = copyNetworkModel.replaceComponentsByCluster(subgraph.getNetworkComponents(copyNetworkModel));
		this.networkModel.getAlternativeNetworkModel().put("ClusteredModel", copyNetworkModel);
		this.networkModel.getAlternativeNetworkModel().put(clusterNetworkComponent.getId(), clusterNetworkComponent.getClusterNetworkModel());
		try {
			simulationServiceHelper.setEnvironmentModel(this.environmentModel, true);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	private Subgraph startPathAnalysis(NetworkModel newNetworkModel) {
		HashSet<Ant> ants = new HashSet<Ant>();
		ants.add(new Ant(newNetworkModel, thisNetworkComponent.getId()));
		ArrayList<Ant> nextRunAnts = new ArrayList<Ant>(ants);
		for (int step = 0; step < PathCircleClusteringBehaviour.STEPS; step++) {
			ArrayList<Ant> runAnts = new ArrayList<Ant>(nextRunAnts);
			nextRunAnts = new ArrayList<Ant>();
			for (Ant ant : runAnts) {
				nextRunAnts.addAll(ant.run());
			}
			if (nextRunAnts.size() == 0) {
				break;
			}
			ants.addAll(nextRunAnts);
		}
		System.out.println("Start Analysing Circle; Ants: " + ants.size());
		AntCircleAnalyser antCircleAnalyser = new AntCircleAnalyser(new ArrayList<Ant>(ants), newNetworkModel);
		System.out.println("End Analysing Circle");
		return antCircleAnalyser.getBestSubgraph();
	}

	@Override
	public boolean done() {
		return true;
	}

}
