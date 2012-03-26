package gasmas.clustering;

import gasmas.clustering.randomWalk.Ant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;

public class ClusterCorrection {

	private NetworkModel baseNetworkModel;

	public ClusterCorrection(NetworkModel networkModel) {
		this.baseNetworkModel = networkModel;
	}

	public void checkNetwork(NetworkModel networkModel, String name) {
		for (ClusterNetworkComponent clusterNetworkComponent : networkModel.getClusterComponents()) {
			checkNetwork(clusterNetworkComponent.getClusterNetworkModel(), clusterNetworkComponent.getId());
			checkCluster(clusterNetworkComponent, networkModel, name);
		}
	}

	private void checkCluster(ClusterNetworkComponent clusterNetworkComponent, NetworkModel networkModel, String name) {
		if (!checkAmountConnections(clusterNetworkComponent) || !checkClusterCircle(clusterNetworkComponent)) {
			if (!mergeTwoCluster(clusterNetworkComponent, networkModel)) {
				networkModel.replaceClusterByComponents(clusterNetworkComponent);
				baseNetworkModel.getAlternativeNetworkModel().put(name, networkModel);
			}
		}
 else {
			baseNetworkModel.getAlternativeNetworkModel().put(clusterNetworkComponent.getId(), clusterNetworkComponent.getClusterNetworkModel());
		}
	}

	private boolean mergeTwoCluster(ClusterNetworkComponent clusterNetworkComponent, NetworkModel networkModel) {
		Vector<NetworkComponent> connectionsToNCs = networkModel.getNeighbourNetworkComponents(clusterNetworkComponent);
		ArrayList<ClusterNetworkComponent> clusters = new ArrayList<ClusterNetworkComponent>();
		for (NetworkComponent networkComponent : connectionsToNCs) {
			if (networkComponent instanceof ClusterNetworkComponent) {
				clusters.add((ClusterNetworkComponent) networkComponent);
			}
		}
		for (ClusterNetworkComponent cluster : clusters) {
			int counter = 0;
			for (NetworkComponent networkComponent : connectionsToNCs) {
				if (cluster == networkComponent) {
					counter++;
				}
				if (counter > 1) {
					networkModel.mergeClusters(cluster, clusterNetworkComponent);
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkAmountConnections(ClusterNetworkComponent clusterNetworkComponent) {
		if (clusterNetworkComponent.getConnectionNetworkComponents().size() > 4) {
			return false;
		}
		return true;
	}

	private boolean checkClusterCircle(ClusterNetworkComponent clusterNetworkComponent) {

		HashSet<Ant> ants = new HashSet<Ant>();
		ants.add(new Ant(clusterNetworkComponent.getClusterNetworkModel(), clusterNetworkComponent.getConnectionNetworkComponents().get(0).getId()));
		boolean active = true;
		ArrayList<Ant> nextRunAnts = new ArrayList<Ant>(ants);
		while (active) {
			ArrayList<Ant> runAnts = new ArrayList<Ant>(nextRunAnts);
			nextRunAnts = new ArrayList<Ant>();
			for (Ant ant : runAnts) {
				nextRunAnts.addAll(ant.run());
			}
			if (nextRunAnts.size() == 0) {
				active = false;
			}
			ants.addAll(nextRunAnts);
			for (Ant ant : ants) {
				if (ant.isCircle()) {
					return true;
				}
			}
		}
		return false;
	}

}
