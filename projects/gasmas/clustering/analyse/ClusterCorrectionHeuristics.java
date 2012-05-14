/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package gasmas.clustering.analyse;

import java.util.ArrayList;
import java.util.Vector;

import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;

/**
 * The Class ClusterCorrection.
 */
public class ClusterCorrectionHeuristics {

	/** The base network model. */
	private ClusterIdentifier clusterIdentifier;

	/**
	 * Instantiates a new cluster correction.
	 *
	 * @param networkModel the network model
	 */
	public ClusterCorrectionHeuristics(ClusterIdentifier clusterIdentifier) {
		this.clusterIdentifier = clusterIdentifier;
	}

	/**
	 * Check network.
	 *
	 * @param networkModel the network model
	 * @param name the name
	 */
	public void checkNetwork(NetworkModel networkModel, String name) {
		for (ClusterNetworkComponent clusterNetworkComponent : networkModel.getClusterComponents()) {
			// check if cluster is not already integrated into another
			if (networkModel.getNetworkComponent(clusterNetworkComponent.getId()) != null) {
				checkNetwork(clusterNetworkComponent.getClusterNetworkModel(), clusterNetworkComponent.getId());
				checkCluster(clusterNetworkComponent, networkModel, name);
			}
		}
	}

	/**
	 * Checks Clusters based on heuristics if they're really good clsuters
	 *
	 * @param clusterNetworkComponent the cluster network component
	 * @param networkModel the network model
	 * @param name the name
	 */
	private void checkCluster(ClusterNetworkComponent clusterNetworkComponent, NetworkModel networkModel, String name) {
		ClusterNetworkComponent cnc = multipleConnectinsOnABranch(clusterNetworkComponent, networkModel);
		if (cnc != null) {
			networkModel.mergeClusters(clusterNetworkComponent, cnc);
			// restart check for new build cluster
			checkNetwork(clusterNetworkComponent.getClusterNetworkModel(), clusterNetworkComponent.getId());
		} else if (!checkAmountConnections(clusterNetworkComponent) || !new PathSearchBotRunner().checkClusterCircle(clusterNetworkComponent)) {
			if (!mergeTwoCluster(clusterNetworkComponent, networkModel)) {
				networkModel.replaceClusterByComponents(clusterNetworkComponent);
			}
		}
	}

	private ClusterNetworkComponent multipleConnectinsOnABranch(ClusterNetworkComponent clusterNetworkComponent, NetworkModel networkModel) {
		ArrayList<ClusterNetworkComponent> clusters = multipleConnectionsBetweenClusters(clusterNetworkComponent, networkModel);
		ArrayList<String> criticalComponents = clusterIdentifier.getCriticalBranchConnectionComponents(clusterNetworkComponent);
		if (criticalComponents != null) {
			for (String criticalNCID : criticalComponents) {
				NetworkComponent networkComponent = clusterNetworkComponent.getClusterNetworkModel().getNetworkComponent(criticalNCID);
				if (networkComponent != null) {
					Vector<GraphNode> nodes = clusterNetworkComponent.getClusterNetworkModel().getNodesFromNetworkComponent(networkComponent);
					int counter = 0;
					for (ClusterNetworkComponent otherCluster : clusters) {
						for (GraphNode graphNode : nodes) {
							if (otherCluster.getClusterNetworkModel().getGraphElement(graphNode.getId()) != null) {
								counter++;
							}
						}
						if (counter > 1) {
							return otherCluster;
						}
					}
				}
			}
		}
		return null;
	}

	private ArrayList<ClusterNetworkComponent> extractClusterNetworkComponents(Vector<NetworkComponent> networkComponents) {
		ArrayList<ClusterNetworkComponent> clusters = new ArrayList<ClusterNetworkComponent>();
		for (NetworkComponent networkComponent : networkComponents) {
			if (networkComponent instanceof ClusterNetworkComponent) {
				clusters.add((ClusterNetworkComponent) networkComponent);
			}
		}
		return clusters;
	}

	private ArrayList<ClusterNetworkComponent> multipleConnectionsBetweenClusters(ClusterNetworkComponent clusterNetworkComponent, NetworkModel networkModel) {
		Vector<NetworkComponent> connectionsToNCs = networkModel.getNeighbourNetworkComponents(clusterNetworkComponent);
		ArrayList<ClusterNetworkComponent> clusters = extractClusterNetworkComponents(connectionsToNCs);
		ArrayList<ClusterNetworkComponent> multipleConnectionClusters = new ArrayList<ClusterNetworkComponent>();
		for (ClusterNetworkComponent cluster : clusters) {
			int counter = 0;
			for (NetworkComponent networkComponent : clusters) {
				if (cluster == networkComponent) {
					counter++;
				}
				if (counter > 1) {
					multipleConnectionClusters.add(cluster);
					break;
				}
			}
		}
		return multipleConnectionClusters;
	}

	/**
	 * Merge two cluster on the first multi Connection
	 *
	 * @param clusterNetworkComponent the cluster network component
	 * @param networkModel the network model
	 * @return true, if successful
	 */
	private boolean mergeTwoCluster(ClusterNetworkComponent clusterNetworkComponent, NetworkModel networkModel) {
		ArrayList<ClusterNetworkComponent> clusters = multipleConnectionsBetweenClusters(clusterNetworkComponent, networkModel);
		if (clusters.size() > 0) {
			networkModel.mergeClusters(clusters.get(0), clusterNetworkComponent);
			return true;
		}
		return false;
	}

	/**
	 * Check amount connections.
	 *
	 * @param clusterNetworkComponent the cluster network component
	 * @return true, if successful
	 */
	private boolean checkAmountConnections(ClusterNetworkComponent clusterNetworkComponent) {
		if (clusterNetworkComponent.getConnectionNetworkComponents().size() > 4) {
			return false;
		}
		return true;
	}
}
