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

import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkModel;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * A simple directed graph / network element with two connection points, represented by two nodes and a directed edge.
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 * 
 */
public class DirectedSimpleGraphElement extends GraphElementPrototype {
    
    private GraphNode entry;
    private GraphNode exit;

    /* (non-Javadoc)
     * @see agentgui.envModel.graph.prototypes.GraphElementPrototype#addToGraph(edu.uci.ics.jung.graph.Graph)
     */
    @Override
    public HashSet<GraphElement> addToGraph(NetworkModel networkModel) {
    	
    	Graph<GraphNode, GraphEdge> graph = networkModel.getGraph();
	
		// Create nodes and edge
		entry = new GraphNode();
		entry.setId(networkModel.nextNodeID());
		graph.addVertex(entry);
		
		exit = new GraphNode();
		exit.setId(networkModel.nextNodeID());
		graph.addVertex(exit);
		
		GraphEdge e = new GraphEdge(getId(), getType());
		graph.addEdge(e, entry, exit, EdgeType.DIRECTED);
	
		// Create a HashSet containing the nodes and edge ant return it
		HashSet<GraphElement> elements = new HashSet<GraphElement>();
		elements.add(e);
		elements.add(entry);
		elements.add(exit);
		return elements;
    }

    @Override
    public boolean isDirected() {
    	return true;
    }

}
