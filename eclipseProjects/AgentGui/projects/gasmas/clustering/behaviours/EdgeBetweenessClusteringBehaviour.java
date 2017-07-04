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
package gasmas.clustering.behaviours;

import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.graph.Graph;
import gasmas.clustering.analyse.ClusterIdentifier;
import jade.core.Agent;

import java.util.List;

import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;

/**
 * The Class EdgeBetweenessBehaviour. Based on the alreay implemented EdgeBetweenessClusterer of the Jung 2.0 Framework. Some changes to fullfill the needs of network clustering
 */
public class EdgeBetweenessClusteringBehaviour extends ClusteringBehaviour {

	/** The Constant edgeBetweneenessRuns. */
	private static final int edgeBetweneenessRuns = 1;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1944492299919314055L;

	/**
	 * Instantiates a new edge betweeness clustering behaviour.
	 *
	 * @param agent the agent
	 * @param networkModel the network model
	 */
	public EdgeBetweenessClusteringBehaviour(Agent agent, NetworkModel networkModel) {
		super(agent, networkModel);
	}

	/* (non-Javadoc)
	 * @see gasmas.clustering.behaviours.ClusteringBehaviour#analyseClusters(agentgui.envModel.graph.networkModel.NetworkModel, gasmas.clustering.analyse.ClusterIdentifier)
	 */
	@Override
	public void analyseClusters(NetworkModel networkModel, ClusterIdentifier clusterIdentifier) {
		NetworkModel newNetworkModel = networkModel.getCopy();
		while (newNetworkModel != null) {
			newNetworkModel = clusterIdentifier.search(removeComponent(newNetworkModel), networkModel);
		}
	}

	/**
	 * identifies a removable Edge with the Jung EdgeBetwenessClusterer.
	 *
	 * @param graph the graph
	 * @return the list
	 */

	private List<GraphEdge> removeEdge(Graph<GraphNode, GraphEdge> graph) {
		EdgeBetweennessClusterer<GraphNode, GraphEdge> edgeBetweennessClusterer = new EdgeBetweennessClusterer<GraphNode, GraphEdge>(EdgeBetweenessClusteringBehaviour.edgeBetweneenessRuns);
		edgeBetweennessClusterer.transform(graph);
		List<GraphEdge> edges = edgeBetweennessClusterer.getEdgesRemoved();
		if (edges.size() < 1) {
			return null;
		}
		return edges;
	}

	/**
	 * removes a component from the copy of the network model.
	 *
	 * @param networkModel the network model
	 * @return the network model
	 */
	private NetworkModel removeComponent(NetworkModel networkModel) {
		List<GraphEdge> edges = removeEdge(networkModel.getGraph());
		if (edges == null) {
			return networkModel;
		}
		for (GraphEdge edge : edges) {
			NetworkComponent networkComponent = networkModel.getNetworkComponent(edge);
			if (networkComponent != null) {
				networkModel.removeNetworkComponent(networkModel.getNetworkComponent(edge));
			}
		}
		return networkModel;
	}
}
