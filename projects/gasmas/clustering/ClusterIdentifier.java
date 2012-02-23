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

	private int clusterCounter = 0;

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
					if (clusterReplace(networkModel.getNetworkComponents(graphNodes), networkModel)) {
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
	private boolean clusterReplace(HashSet<NetworkComponent> networkComponents, NetworkModel networkModel) {
		// ------- Cluster can be only internal NetworkComponents of the NetworkModel
		for (NetworkComponent networkComponent : networkComponents) {
			for (String outerNetworkComponentID : networkModel.getOuterNetworkComponentIDs()) {
				if (outerNetworkComponentID.equals(networkComponent.getId())) {
					return false;
				}
			}
		}
		ArrayList<NetworkComponent> clustersComponents = new ArrayList<NetworkComponent>(networkComponents);
		// ------- add the Neighbours to the List because removed are part of the cluster
		networkComponents.addAll(networkModel.getNeighbourNetworkComponents(networkComponents));
		connectionComponents(networkComponents, clustersComponents, networkModel);
		refrehDisplay(networkModel.replaceComponentsByCluster(networkComponents));
		return true;
	}

	/**
	 * Removes Components with more than two outeNodes if more outerNodes are connected to the outside
	 * 
	 * @param networkComponents
	 * @param clustersComponents
	 * @param networkModel
	 */
	private void connectionComponents(HashSet<NetworkComponent> networkComponents, ArrayList<NetworkComponent> clustersComponents, NetworkModel networkModel) {
		ArrayList<NetworkComponent> connectionComponents = new ArrayList<NetworkComponent>(networkComponents);
		connectionComponents.removeAll(clustersComponents);
		HashSet<GraphNode> clusterComponentsNodes = new HashSet<GraphNode>();
		for (NetworkComponent networkComponent : clustersComponents) {
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
		for (NetworkComponent networkComponent : connectionComponents) {
			for (String outerNetworkComponentID : networkModel.getOuterNetworkComponentIDs()) {
				if (outerNetworkComponentID.equals(networkComponent.getId())) {
					networkComponents.remove(networkComponent);
				}
			}
		}
	}

	/**
	 * Refreh display.
	 *
	 * @param clusterComponent the cluster component
	 */
	private void refrehDisplay(ClusterNetworkComponent clusterComponent) {
		this.baseNetworkModel.getAlternativeNetworkModel().put("C" + clusterCounter++ + " " + clusterComponent.getId(), clusterComponent.getClusterNetworkModel());
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
