package agentgui.graphEnvironment.prototypes;

import java.util.HashSet;

import agentgui.graphEnvironment.environmentModel.GraphEdge;
import agentgui.graphEnvironment.environmentModel.GraphElement;
import agentgui.graphEnvironment.environmentModel.GraphNode;
import edu.uci.ics.jung.graph.Graph;

/**
 * Abstract super class for GraphElementPrototypes.
 * A GraphElementPrototype defines how a component in a graph/network environment will be represented
 * by nodes and edges in the graph defining the environment model. GraphElementPrototypes are used
 * during graph import only. Later the nodes and edges of the GridModel will be stored directly in
 * GraphML.
 *     
 * @author Nils
 *
 */
public abstract class GraphElementPrototype {
	/**
	 * Counting the total Number of nodes for generating unique node IDs 
	 */
	protected static int nodeCounter = 0;
	/**
	 * The id of the element represented by this GraphElementPrototype	
	 */
	protected String id;
	/**
	 * The type of the element represented by this GraphElementPrototype
	 */
	protected String type;
	/**
	 * Reference to the graph instance, required to get graph topology related information on nodes and edges 
	 */
	protected Graph<GraphNode, GraphEdge> graph;

	/**
	 * This method adds a GraphElementPrototype to a JUNG graph, with no connection to other graph elements.
	 * @param graph The JUNG graph
	 * @return A HashSet containing the GraphElements representing this component, or null if adding failed  
	 */
	public abstract HashSet<GraphElement> addToGraph(Graph<GraphNode, GraphEdge> graph);
	/**
	 * This method adds a GraphElementPrototype to a JUNG graph after another element.
	 * @param graph The JUNG graph
	 * @param predecessor The GraphElementPrototype's predecessor
	 * @return A HashSet containing the GraphElements representing this component, or null if adding failed
	 */
	public abstract HashSet<GraphElement> addAfter(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype predecessor);
	/**
	 * This method adds a GraphElementPrototype to a JUNG graph before another element.
	 * @param graph The JUNG graph
	 * @param successor The GraphElementPrototype's successor
	 * @return A HashSet containing the GraphElements representing this component, or null if adding failed
	 */
	public abstract HashSet<GraphElement> addBefore(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype successor);
	/**
	 * This method adds a GraphElementPrototype to a JUNG graph between two other elements.
	 * @param graph The JUNG graph
	 * @param predecessor The GraphElementPrototype's predecessor
	 * @param successor The GraphElementPrototype's successor
	 * @return A HashSet containing the GraphElements representing this component, or null if adding failed
	 */
	public abstract HashSet<GraphElement> addBetween(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype predecessor, GraphElementPrototype successor);
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * This method returns a node in which a predecessor can be connected to this GraphElementPrototype 
	 * @return The node
	 */
	public abstract GraphNode getFreeEntry();
	/**
	 * This method returns a node in which a successor can be connected to this GraphElementPrototype
	 * @return The node
	 */
	public abstract GraphNode getFreeExit();
	/**
	 * @return True if directed, false if undirected
	 */
	public abstract boolean isDirected();
}
