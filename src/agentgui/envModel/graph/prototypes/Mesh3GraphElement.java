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

import java.util.HashSet;
import java.util.Vector;

import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.GraphNode;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * A graph / network element with a mesh arrangement. Extend this class for implementing 'n' vertex mesh graph element prototypes.
 * 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 */
public class Mesh3GraphElement extends GraphElementPrototype {
    /**
     * The number of connection points
     */
    private Integer n = null;

    /**
     * The vector of nodes which form the connection points
     */
    Vector<GraphNode> nodes;

    /**
     * Default constructor with 3 corners
     */
    public Mesh3GraphElement() {
	super();
	n = 3;
	nodes = new Vector<GraphNode>();
    }

    /**
     * Constructor for creating the Mesh prototype with 'n' connection points
     * 
     * @param n the number of connection points
     */
    public Mesh3GraphElement(Integer n) {
	super();
	if (n >= 3) {
	    this.n = n;
	    nodes = new Vector<GraphNode>();
	} else {
	    throw new GraphElementPrototypeException("Number of connection points should be greater than 3");
	}
    }

    @Override
    public HashSet<GraphElement> addToGraph(Graph<GraphNode, GraphEdge> graph) {
	// check if n is set
	if (n != null) {
	    this.graph = graph;
	    // Create a HashSet for the nodes and edges
	    HashSet<GraphElement> elements = new HashSet<GraphElement>();

	    // Creating nodes
	    for (int i = 0; i < n; i++) {
		// Create the node and add to the vector
		GraphNode node = new GraphNode();
		node.setId(GraphNode.GRAPH_NODE_PREFIX + (nodeCounter++));
		graph.addVertex(node);
		nodes.add(node);
		elements.add(node);
	    }

	    // Creating edges
	    int edgeCount = 0;
	    for (int i = 0; i < n; i++) {
		for (int j = i + 1; j < n; j++) {
		    // Creating edge
		    GraphEdge edge = new GraphEdge(getId() + "_" + edgeCount, getType());

		    // Adding to the graph
		    graph.addEdge(edge, nodes.get(i), nodes.get(j), EdgeType.UNDIRECTED);
		    elements.add(edge);
		    edgeCount++;
		}
	    }
	    return elements;
	} else {
	    throw new GraphElementPrototypeException("Number of connection points (n) is null");
	}
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
	// TODO
	// Iterator<GraphNode> iter = outerNodes.iterator();
	// while(iter.hasNext()){
	// GraphNode node = iter.next();
	// if(graph.getNeighborCount(node) < 2){
	// return node;
	// }
	// }
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

    /**
     * @return the number of corners
     */
    public Integer getN() {
	return n;
    }

    /**
     * Set the number of corners
     * 
     * @param n the number of corners
     */
    public void setN(Integer n) {
	this.n = n;
    }
}
