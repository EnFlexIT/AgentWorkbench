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

import agentgui.envModel.graph.controller.BasicGraphGui;
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

	/**
	 * Instantiates a new cluster graph element.
	 * 
	 * @param outerNodes the outer nodes
	 * @param id the id
	 */
	public ClusterGraphElement(Vector<GraphNode> outerNodes, String id) {
		this.outerNodes = outerNodes;
		this.id = id;
		this.setType("Cluster");
	}

	/**
	 * Adds the to graph new Nodes for the ClusteComponent
	 * 
	 * @param networkModel the network model
	 * @return the hash set
	 */
	public HashSet<GraphElement> addToGraph(NetworkModel networkModel) {
		HashSet<GraphElement> elements = new HashSet<GraphElement>(outerNodes);
		// add central Node
		GraphNode centralNode = new GraphNode();
		centralNode.setId(networkModel.nextNodeID());
		Rectangle2D rectangle = BasicGraphGui.getVerticesSpreadDimension(outerNodes);
		double x = (rectangle.getMaxX() + rectangle.getMinX()) / 2 - 100;
		double y = (rectangle.getMaxY() + rectangle.getMinY()) / 2;
		centralNode.setPosition(new Point2D.Double(x, y));
		networkModel.getGraph().addVertex(centralNode);
		elements.add(centralNode);
		// add Edges
		int counter = 0;
		for (GraphNode graphNode : outerNodes) {
			GraphEdge edge = new GraphEdge(id + "_" + counter++, getType());
			networkModel.getGraph().addEdge(edge, centralNode, graphNode, EdgeType.UNDIRECTED);
			elements.add(edge);
		}
		return elements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see agentgui.envModel.graph.prototypes.GraphElementPrototype#addToGraph(edu.uci.ics.jung.graph.Graph)
	 */
	@Override
	public HashSet<GraphElement> addToGraph(Graph<GraphNode, GraphEdge> graph) {
		return null;
	}
}
