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

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Vector;

import agentgui.envModel.graph.GraphGlobals;
import agentgui.envModel.graph.networkModel.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkModel;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * A graph / network element with a star arrangement. Specially designed for a Cluster. 
 * This Prototype works already with concrete NetworkModel and with real GraphElements
 * 
 * @author David Pachula
 */
public class ClusterGraphElement extends StarGraphElement {

	private GraphNode centralGraphNode;

	/**
	 * Instantiates a new ClusterGraphElement. 
	 * Simple constructor needed for reflective instantiation 
	 */
	public ClusterGraphElement() {
		this.setType(GeneralGraphSettings4MAS.NETWORK_COMPONENT_TYPE_4_CLUSTER);
	}
	/**
	 * Instantiates a new cluster graph element.
	 * 
	 * @param outerNodes the outer nodes
	 * @param id the id
	 */
	public ClusterGraphElement(Vector<GraphNode> outerNodes, String id) {
		this.outerNodes = outerNodes;
		this.id = id;
		this.setType(GeneralGraphSettings4MAS.NETWORK_COMPONENT_TYPE_4_CLUSTER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see agentgui.envModel.graph.prototypes.GraphElementPrototype#addToGraph(edu.uci.ics.jung.graph.Graph)
	 */
	@Override
	public HashSet<GraphElement> addToGraph(NetworkModel networkModel) {
    	
    	Graph<GraphNode, GraphEdge> graph = networkModel.getGraph();
		HashSet<GraphElement> elements = new HashSet<GraphElement>();
		
		// --- Add central Node -------------------------------------
		this.centralGraphNode = new GraphNode();
		this.centralGraphNode.setId(networkModel.nextNodeID());
		graph.addVertex(this.centralGraphNode);
		elements.add(this.centralGraphNode);
		
		// --- Add Edges --------------------------------------------
		int counter = 0;
		for (GraphNode outerNode : outerNodes) {
			// --- Add Edge -----------------------------------------
			GraphEdge edge = new GraphEdge(id + "_" + counter++, getType());
			graph.addEdge(edge, this.centralGraphNode, outerNode, EdgeType.UNDIRECTED);
			elements.add(edge);
		}
		
		// --- Set position of central GraphNode --------------------
		Rectangle2D rectangle = GraphGlobals.getGraphSpreadDimension(this.outerNodes);
		this.centralGraphNode.setPosition(new Point2D.Double(rectangle.getCenterX(), rectangle.getCenterY()));

		elements.addAll(this.outerNodes);
		return elements;
	}
	
	/**
	 * Returns the central graph node if available.
	 * @return the central graph node if available
	 */
	public GraphNode getCentralGraphNode() {
		return this.centralGraphNode;
	}
	
}
