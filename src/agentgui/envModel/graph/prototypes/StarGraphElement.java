package agentgui.envModel.graph.prototypes;

import java.util.HashSet;
import java.util.Vector;

import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.GraphNode;
import edu.uci.ics.jung.graph.Graph;

public abstract class StarGraphElement extends GraphElementPrototype {

	/** The vector of outer nodes that forms the corners of the element. */
	protected Vector<GraphNode> outerNodes = new Vector<GraphNode>();

	protected Integer n;

	public StarGraphElement() {
		super();
	}

	@Override
	public HashSet<GraphElement> addAfter(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype predecessor) {
		return null;
	}

	@Override
	public HashSet<GraphElement> addBefore(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype successor) {
		return null;
	}

	@Override
	public HashSet<GraphElement> addBetween(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype predecessor, GraphElementPrototype successor) {
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

	public Integer getN() {
		return n;
	}

}
