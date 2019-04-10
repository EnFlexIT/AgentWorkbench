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
package org.awb.env.networkModel.networkModel;

import java.util.List;

import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * The Class GraphEdgeConnection is used in order to evaluate the
 * connected GraphNodes and NetworkComponents of a specified GraphEdge.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class GraphEdgeConnection {

	private GraphEdge graphEdge;
	
	private GraphNode graphNode1;
	private NetworkComponent externalNetworkComponent1;
	
	private GraphNode graphNode2;
	private NetworkComponent externalNetworkComponent2;
	
	private boolean fixedDirected = false;
	
	/**
	 * Instantiates a new graph edge connection.
	 *
	 * @param networkModel the network model
	 * @param networkComponent the network component
	 * @param graphEdge the graph edge
	 */
	public GraphEdgeConnection(NetworkModel networkModel, NetworkComponent networkComponent, GraphEdge graphEdge) {
	
		this.graphEdge = graphEdge;
		
		EdgeType edgeType = networkModel.getGraph().getEdgeType(this.graphEdge);
		
		if (networkComponent.isDirected() && edgeType==EdgeType.DIRECTED) {
			this.fixedDirected = true;	
			this.graphNode1 = networkModel.getGraph().getSource(this.graphEdge);
			this.graphNode2 = networkModel.getGraph().getDest(this.graphEdge);
			
		} else {
			this.fixedDirected = false;
			Pair<GraphNode> nodePair = networkModel.getGraph().getEndpoints(this.graphEdge);
			this.graphNode1 = nodePair.getFirst();
			this.graphNode2 = nodePair.getSecond();
		}
		
		List<NetworkComponent> netComps = null; 
		
		netComps = networkModel.getNetworkComponents(this.graphNode1);
		netComps.remove(networkComponent);
		if (netComps.size()>0) {
			this.externalNetworkComponent1 = netComps.iterator().next();	
		}
		
		netComps = networkModel.getNetworkComponents(this.graphNode2);
		netComps.remove(networkComponent);
		if (netComps.size()>0) {
			this.externalNetworkComponent2 = netComps.iterator().next();	
		}
	}

	/**
	 * Gets the graph edge.
	 * @return the graphEdge
	 */
	public GraphEdge getGraphEdge() {
		return graphEdge;
	}
	/**
	 * Gets the graph node1.
	 * @return the graphNode1
	 */
	public GraphNode getGraphNode1() {
		return graphNode1;
	}
	/**
	 * Gets the graph node2.
	 * @return the graphNode2
	 */
	public GraphNode getGraphNode2() {
		return graphNode2;
	}
	/**
	 * Gets the network component1.
	 * @return the networkComponent1
	 */
	public NetworkComponent getExternalNetworkComponent1() {
		return externalNetworkComponent1;
	}
	/**
	 * Gets the network component2.
	 * @return the networkComponent2
	 */
	public NetworkComponent getExternalNetworkComponent2() {
		return externalNetworkComponent2;
	}

	/**
	 * Checks if is fixed directed.
	 * @return true, if is fixed directed
	 */
	public boolean isFixedDirected() {
		return fixedDirected;
	}
	
}
