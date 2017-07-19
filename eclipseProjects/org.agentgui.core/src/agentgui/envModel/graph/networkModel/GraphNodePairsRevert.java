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
package agentgui.envModel.graph.networkModel;

/**
 * The Class GraphNodePairsRevert is use in the class {@link GraphNodePairs}
 * as reverting information for merging and splitting.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class GraphNodePairsRevert {

	private GraphNode graphNode = null; 
	private GraphEdge graphedge = null;
	
	/**
	 * Instantiates a new graph node pairs revert.
	 *
	 * @param graphNode the graph node
	 * @param graphEdge the graph edge
	 */
	public GraphNodePairsRevert(GraphNode graphNode, GraphEdge graphEdge) {
		this.graphNode = graphNode; 
		this.graphedge = graphEdge;
	}

	/**
	 * Gets the graph node.
	 * @return the graph node
	 */
	public GraphNode getGraphNode() {
		return graphNode;
	}
	/**
	 * Sets the graph node.
	 * @param graphNode the new graph node
	 */
	public void setGraphNode(GraphNode graphNode) {
		this.graphNode = graphNode;
	}

	/**
	 * Gets the {@link GraphEdge}.
	 * @return the {@link GraphEdge}
	 */
	public GraphEdge getGraphEdge() {
		return graphedge;
	}
	
	/**
	 * Sets the {@link GraphEdge}.
	 * @param graphEdge the new {@link GraphEdge}
	 */
	public void setGraphEdge(GraphEdge graphEdge) {
		this.graphedge = graphEdge;
	}

}
