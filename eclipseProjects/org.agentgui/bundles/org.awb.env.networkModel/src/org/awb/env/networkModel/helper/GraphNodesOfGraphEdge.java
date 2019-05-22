package org.awb.env.networkModel.helper;

import java.util.ArrayList;
import java.util.List;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphElement;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.NetworkModel;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * The Class GraphNodesOfGraphEdge determines the start and end GraphNode of a 
 * NetworkComonent that consist of a simple (and possibly directed) GraphEdge with 
 * the help of the specified parameter.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class GraphNodesOfGraphEdge {

	private GraphEdge graphEdge;
	private GraphNode graphNodeStart;
	private GraphNode graphNodeEnd;
	
	
	/**
	 * Instantiates a new graph edge positions.
	 *
	 * @param networkModel the network model
	 * @param netComp the net NetworkComponent
	 */
	public GraphNodesOfGraphEdge(NetworkModel networkModel, NetworkComponent netComp) {
		List<GraphElement> graphElementList = new ArrayList<>(networkModel.getGraphElementsOfNetworkComponent(netComp, new GraphEdge(null, null))) ;
		if (graphElementList.size()==1) {
			GraphEdge graphEdge = (GraphEdge) graphElementList.get(0);
			this.setGraphNodes(networkModel.getGraph(), graphEdge);
		}
	}
	/**
	 * Instantiates a new graph nodes of graph edge.
	 *
	 * @param graph the graph
	 * @param graphEdge the graph edge
	 */
	public GraphNodesOfGraphEdge(Graph<GraphNode, GraphEdge> graph, GraphEdge graphEdge) {
		this.setGraphNodes(graph, graphEdge);
	}
	
	/**
	 * Sets the graph nodes.
	 *
	 * @param graph the graph
	 * @param graphEdge the graph edge
	 */
	private void setGraphNodes(Graph<GraphNode, GraphEdge> graph, GraphEdge graphEdge) {
		
		if (graph==null || graphEdge==null) return;
		
		if (graph.getEdgeType(graphEdge)==EdgeType.DIRECTED) {
			this.graphNodeStart = graph.getSource(graphEdge);
			this.graphNodeEnd   = graph.getDest(graphEdge);
		} else {
			Pair<GraphNode> nodePair = graph.getEndpoints(graphEdge);
			this.graphNodeStart = nodePair.getFirst();
			this.graphNodeEnd   = nodePair.getSecond();
		}
		this.graphEdge = graphEdge;
	}
	
	/**
	 * Returns the graph edge.
	 * @return the graph edge
	 */
	public GraphEdge getGraphEdge() {
		return graphEdge;
	}
	/**
	 * Returns the start graph node.
	 * @return the graph node start
	 */
	public GraphNode getGraphNodeStart() {
		return graphNodeStart;
	}
	/**
	 * Returns the end graph node.
	 * @return the graph node end
	 */
	public GraphNode getGraphNodeEnd() {
		return graphNodeEnd;
	}
}
