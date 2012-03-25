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
package gasmas.clustering;

import jade.core.ServiceException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;

/**
 * The Class ClusterIdentifier.
 */
public class ClusterIdentifier {

	/** The environment model. */
	private EnvironmentModel environmentModel;

	/** The base network model. */
	private NetworkModel baseNetworkModel;

	/** The simulation service helper. */
	private SimulationServiceHelper simulationServiceHelper;

	/**
	 * Instantiates a new cluster identifier.
	 *
	 * @param environmentModel the environment model
	 * @param simulationServiceHelper the simulation service helper
	 */
	public ClusterIdentifier(EnvironmentModel environmentModel, SimulationServiceHelper simulationServiceHelper) {
		this.environmentModel = environmentModel;
		this.simulationServiceHelper = simulationServiceHelper;
		this.baseNetworkModel = (NetworkModel) environmentModel.getDisplayEnvironment();
	}

	/**
	 * Seraches for possible Clusters.
	 *
	 * @param reducedModel the reduced model
	 * @return the network model
	 */
	public NetworkModel search(NetworkModel reducedModel, NetworkModel networkModel) {
		WeakComponentClusterer<GraphNode, GraphEdge> wcSearch = new WeakComponentClusterer<GraphNode, GraphEdge>();
		Set<Set<GraphNode>> clusterSet = wcSearch.transform(reducedModel.getGraph());
		boolean clustersToSmall = false;
		boolean baseModelChanged = false;
		if (clusterSet.size() > 1) {
			clustersToSmall = true;
			for (Set<GraphNode> graphNodes : clusterSet) {
				if (graphNodes.size() > 3) {
					clustersToSmall = false;
					if (clusterReplace(graphNodes, reducedModel, networkModel)) {
						baseModelChanged = true;
					}
				}
			}
		}
		if (baseModelChanged) {
			NetworkModel clusteredModel = networkModel.getCopy();
			clusteredModel.setAlternativeNetworkModel(null);
			return clusteredModel;
		}
		return clustersToSmall ? null : reducedModel;
	}

	/**
	 * Cluster replace only for inner Clusters
	 *
	 * @param reducedModel the reduced model
	 * @param networkComponents the network components
	 * @return true, if successful
	 */
	private boolean clusterReplace(Set<GraphNode> graphNodes, NetworkModel reducedModel, NetworkModel networkModel) {
		HashSet<NetworkComponent> networkComponents = networkModel.getNetworkComponents(graphNodes);
		// ------- Cluster can be only internal NetworkComponents of the NetworkModel
		for (NetworkComponent networkComponent : networkComponents) {
			for (String outerNetworkComponentID : networkModel.getOuterNetworkComponentIDs()) {
				if (outerNetworkComponentID.equals(networkComponent.getId())) {
					return false;
				}
			}
		}
		checkBranches(networkComponents, getConnectionComponents(networkComponents, reducedModel.getNetworkComponents(graphNodes), networkModel), networkModel);
		refrehDisplay(networkModel.replaceComponentsByCluster(networkComponents));
		return true;
	}

	private void checkBranches(HashSet<NetworkComponent> networkComponents, ArrayList<NetworkComponent> connectionComponents, NetworkModel networkModel) {
		ArrayList<NetworkComponent> coreComponents = new ArrayList<NetworkComponent>(networkComponents);
		coreComponents.removeAll(connectionComponents);

		HashSet<GraphNode> clusterComponentsNodes = new HashSet<GraphNode>();
		for (NetworkComponent networkComponent : coreComponents) {
			clusterComponentsNodes.addAll(networkModel.getNodesFromNetworkComponent(networkComponent));
		}

		for (NetworkComponent networkComponent : connectionComponents) {
			int counter = 0;
			Vector<GraphNode> componentsNodes = networkModel.getNodesFromNetworkComponent(networkComponent);
			for (GraphNode graphNode : componentsNodes) {
				if (clusterComponentsNodes.contains(graphNode)) {
					counter++;
				}
			}
			if (counter < componentsNodes.size() / 2) {
				networkComponents.remove(networkComponent);
			}
		}
	}

	private ArrayList<NetworkComponent> getConnectionComponents(HashSet<NetworkComponent> networkComponents, HashSet<NetworkComponent> coreComponents,NetworkModel networkModel) {
		ArrayList<NetworkComponent> connectionComponents = new ArrayList<NetworkComponent>(networkComponents);
		for (NetworkComponent networkComponent : coreComponents) {
			connectionComponents.remove(networkModel.getNetworkComponent(networkComponent.getId()));
		}
		return connectionComponents;
	}

	/**
	 * Refresh display.
	 *
	 * @param clusterComponent the cluster component
	 */
	private void refrehDisplay(ClusterNetworkComponent clusterComponent) {
		this.baseNetworkModel.getAlternativeNetworkModel().put(clusterComponent.getId(), clusterComponent.getClusterNetworkModel());
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
