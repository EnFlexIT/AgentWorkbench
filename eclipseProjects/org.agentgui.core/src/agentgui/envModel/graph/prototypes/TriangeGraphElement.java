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
import agentgui.envModel.graph.networkModel.NetworkModel;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * A graph / network element with three connection points and direct connections between each two of them. Represented by a triangle consisting of three nodes and three edges.
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 */
public class TriangeGraphElement extends GraphElementPrototype {
    /**
     * The GraphElementPrototype's connection points
     */
    private Vector<GraphNode> nodes;

    public TriangeGraphElement() {
    	nodes = new Vector<GraphNode>();
    }

    @Override
    public HashSet<GraphElement> addToGraph(NetworkModel networkModel) {
    	
    	Graph<GraphNode, GraphEdge> graph = networkModel.getGraph();
    	
		// Create nodes and edges
		GraphNode n1 = new GraphNode();
		n1.setId(networkModel.nextNodeID());
		graph.addVertex(n1);
		
		GraphNode n2 = new GraphNode();
		n2.setId(networkModel.nextNodeID());
		graph.addVertex(n2);
		
		GraphNode n3 = new GraphNode();
		n3.setId(networkModel.nextNodeID());
		graph.addVertex(n3);
		
		
		GraphEdge e1 = new GraphEdge(getId() + "_1", getType());
		GraphEdge e2 = new GraphEdge(getId() + "_2", getType());
		GraphEdge e3 = new GraphEdge(getId() + "_3", getType());
	
		graph.addEdge(e1, n1, n2, EdgeType.UNDIRECTED);
		graph.addEdge(e2, n1, n3, EdgeType.UNDIRECTED);
		graph.addEdge(e3, n2, n3, EdgeType.UNDIRECTED);
	
		// Add the nodes to this GraphElementPrototypes node list
		nodes.add(n1);
		nodes.add(n2);
		nodes.add(n3);
	
		// Create a HashSet containing the nodes and edge and return it
		HashSet<GraphElement> elements = new HashSet<GraphElement>();
		elements.add(n1);
		elements.add(n2);
		elements.add(n3);
		elements.add(e1);
		elements.add(e2);
		elements.add(e3);
		return elements;
    }

    @Override
    public boolean isDirected() {
    	return false;
    }
}
