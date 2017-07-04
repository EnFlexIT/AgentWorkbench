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

import java.util.HashSet;


/**
 * The Class GraphNodePair can be utilized as a reminder for couples 
 * of GraphNodes that are used for merging or splitting actions.<br>
 * Because nodes can be connected with more than one other component
 * the second slot is designed as Hash.<br><br>
 * The revertInfo can be used in order to reconstruct a merged 
 * configuration.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class GraphNodePairs {

	private GraphNode graphNode1 = null;
	private HashSet<GraphNode> graphNode2Hash = null;
	private HashSet<GraphNodePairsRevert> revertInfos = null;

	
	
	/**
	 * Constructor that allows a one to one merge of GraphNodes.
	 *
	 * @param node1 the first GraphNode
	 * @param node2 the second GraphNode 
	 */
	public GraphNodePairs(GraphNode node1, GraphNode node2) {
		this.graphNode1 = node1;
		this.graphNode2Hash = new HashSet<GraphNode>();
		this.graphNode2Hash.add(node2);
	}
	/**
	 * Constructor that allows a one to many merge of GraphNodes.
	 *
	 * @param node1 the first GraphNode
	 * @param pairNodes the {@link HashSet} that describes the GraphNodes to merge with the first one
	 */
	public GraphNodePairs(GraphNode node1, HashSet<GraphNode> pairNodes) {
		this.graphNode1 = node1;
		this.graphNode2Hash = pairNodes;
	}

	/**
	 * Returns the first GraphNode.
	 * @return the first GraphNode
	 */
	public GraphNode getGraphNode1() {
		return graphNode1;
	}
	/**
	 * Sets the first GraphNode.
	 * @param graphNode1 the new first GraphNode
	 */
	public void setGraphNode1(GraphNode graphNode1) {
		this.graphNode1 = graphNode1;
	}

	/**
	 * Returns the Hash of the second GraphNode's.
	 * @return the second GraphNode
	 */
	public HashSet<GraphNode> getGraphNode2Hash() {
		return graphNode2Hash;
	}
	/**
	 * Sets the second Hash of the second GraphNode's.
	 * @param graphNode2Hash the Hash with the second GraphNodes
	 */
	public void setGraphNode2Hash(HashSet<GraphNode> graphNode2Hash) {
		this.graphNode2Hash = graphNode2Hash;
	}

	/**
	 * Returns the revert info as configurations of GraphNodes and GraphEdges.
	 * @return the revertInfos
	 */
	public HashSet<GraphNodePairsRevert> getRevertInfos() {
		return revertInfos;
	}
	/**
	 * Sets the revert info.
	 * @param revertInfos the revertInfo to set
	 */
	public void setRevertInfos(HashSet<GraphNodePairsRevert> revertInfos) {
		this.revertInfos = revertInfos;
	}
	
}
