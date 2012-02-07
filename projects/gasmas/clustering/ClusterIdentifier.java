package gasmas.clustering;

import jade.core.ServiceException;

import java.util.HashSet;
import java.util.Set;

import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;

public class ClusterIdentifier {

	private EnvironmentModel environmentModel;
	private NetworkModel baseNetworkModel;
	private SimulationServiceHelper simulationServiceHelper;

	public ClusterIdentifier(EnvironmentModel environmentModel, SimulationServiceHelper simulationServiceHelper) {
		this.environmentModel = environmentModel;
		this.simulationServiceHelper = simulationServiceHelper;
		this.baseNetworkModel = (NetworkModel) environmentModel.getDisplayEnvironment();
	}

	public NetworkModel serach(NetworkModel reducedModel) {
		WeakComponentClusterer<GraphNode, GraphEdge> wcSearch = new WeakComponentClusterer<GraphNode, GraphEdge>();
		Set<Set<GraphNode>> clusterSet = wcSearch.transform(reducedModel.getGraph());
		boolean baseModelChanged = false;
		if (clusterSet.size() > 1) {
			for (Set<GraphNode> graphNodes : clusterSet) {
				if (clusterReplace(reducedModel, reducedModel.getNetworkComponents(graphNodes))) {
					baseModelChanged = true;
				}
			}
		}
		if (baseModelChanged) {
			NetworkModel copy = baseNetworkModel.getCopy();
			baseNetworkModel.setAlternativeNetworkModel(null);
			return copy;
		}
		return reducedModel;
	}

	private boolean clusterReplace(NetworkModel reducedModel, HashSet<NetworkComponent> networkComponents) {
		// ------- Cluster can be only internal NetworkComponents of the NetworkModel
		for (NetworkComponent networkComponent : networkComponents) {
			if (baseNetworkModel.getOuterNetworkComponents().contains(networkComponent)) {
				return false;
			}
		}
		// ------- add the Neighbours to the List because removed are part of the cluster
		networkComponents.addAll(baseNetworkModel.getNeighbourNetworkComponents(networkComponents));
		refrehDisplay(baseNetworkModel.replaceComonentsByCluster(networkComponents));
		return true;
	}

	private void refrehDisplay(ClusterNetworkComponent clusterComponent) {
		this.baseNetworkModel.getAlternativeNetworkModel().put("ClusterComponent " + clusterComponent.getId(), clusterComponent.getClusterNetworkModel());
		this.environmentModel.setDisplayEnvironment(this.baseNetworkModel);

		// --- Put the environment model into the SimulationService -
		// --- in order to make it accessible for the whole agency --
		try {
			simulationServiceHelper.setEnvironmentModel(this.environmentModel, true);

		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
}
