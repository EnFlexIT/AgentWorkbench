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

/**
 * The Class DistributionNode.
 */
public class DistributionNode extends GraphElementPrototype {

	/** The instance of the current DistributionNode */
	private GraphNode distributionNode;
	
	
	/**
	 * Instantiates a new distribution node.
	 */
	public DistributionNode() {
	}
	
	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.prototypes.GraphElementPrototype#addToGraph(edu.uci.ics.jung.graph.Graph)
	 */
	@Override
	public HashSet<GraphElement> addToGraph(NetworkModel networkModel) {
    	
    	Graph<GraphNode, GraphEdge> graph = networkModel.getGraph();
		HashSet<GraphElement> elements = new HashSet<GraphElement>();

		distributionNode = new GraphNode();
		distributionNode.setId(networkModel.nextNodeID());
		graph.addVertex(distributionNode);
	    elements.add(distributionNode);
		return elements;
	}

	/* (non-Javadoc)
	 * @see agentgui.envModel.graph.prototypes.GraphElementPrototype#isDirected()
	 */
	@Override
	public boolean isDirected() {
		return false;
	}

}
