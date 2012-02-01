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

package agentgui.envModel.graph.prototypes;

import java.util.ArrayList;
import java.util.HashSet;

import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkModel;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * A graph / network element with a star arrangement. Specialy designed for a Cluster
 * 
 * @author David Pachula
 */
public class ClusterGraphElement extends GraphElementPrototype {

    /**
     * The vector of outernodes which forms the corners of the element.
     */
    ArrayList<GraphNode> outerNodes;

    public ClusterGraphElement(ArrayList<GraphNode> outerNodes, String id) {
	this.outerNodes = outerNodes;
	this.id = id;
    }

    public HashSet<String> addToGraph(NetworkModel networkModel) {
	HashSet<String> elements = new HashSet<String>();
	// add central Node
	GraphNode centralNode = new GraphNode();
	centralNode.setId(networkModel.nextNodeID());
	networkModel.getGraph().addVertex(centralNode);
	elements.add(centralNode.getId());
	// add Edges
	int counter = -1;
	for (GraphNode graphNode : outerNodes) {
	    GraphEdge edge = new GraphEdge(id + "_" + counter++, getType());
	    graph.addEdge(edge, centralNode, graphNode, EdgeType.UNDIRECTED);
	    elements.add(edge.getId());
	}
	return elements;
    }

    @Override
    public HashSet<GraphElement> addAfter(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype predecessor) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public HashSet<GraphElement> addBefore(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype successor) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public HashSet<GraphElement> addBetween(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype predecessor, GraphElementPrototype successor) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public GraphNode getFreeEntry() {
	for (GraphNode node : outerNodes) {
	    if (graph.getNeighborCount(node) < 2) {
		return node;
	    }
	}
	return null;
    }

    @Override
    public GraphNode getFreeExit() {
	return getFreeEntry();
    }

    @Override
    public boolean isDirected() {
	return false;
    }

    @Override
    public HashSet<GraphElement> addToGraph(Graph<GraphNode, GraphEdge> graph) {
	// TODO Auto-generated method stub
	return null;
    }
}
