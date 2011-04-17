package agentgui.graphEnvironment.environmentModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;

/**
 * This class encapsulates a JUNG graph representing a grid, with edges representing the grid components.
 * @author Nils
 *
 */
public class GridModel {
	/**
	 * The JUNG graph.
	 */
	private Graph<GraphNode, GraphEdge> graph;
	/**
	 * HashMap providing access to the grid components based on the component's agentID
	 */
	private HashMap<String, GraphEdge> components;
	/**
	 * Default constructor
	 */
	public GridModel(){
		this.graph = new SparseGraph<GraphNode, GraphEdge>();
		this.components = new HashMap<String, GraphEdge>();
	}
	/**
	 * Returns the GridComponent with the given ID, or null if not found.
	 * @param id The ID to look for
	 * @return The GridComponent
	 */
	public GraphEdge getComponent(String id){
		return components.get(id);
	}
	/**
	 * Returns a list of all GridComponents
	 * @return The list
	 */
	public Collection<GraphEdge> getComponents() {
		return graph.getEdges();
	}
	public Graph<GraphNode, GraphEdge> getGraph() {
		return graph;
	}
	public void setGraph(Graph<GraphNode, GraphEdge> graph) {
		this.graph = graph;
		
		// Create HashMap of components
		this.components = new HashMap<String, GraphEdge>();
		Iterator<GraphEdge> componentIterator = graph.getEdges().iterator();
		while(componentIterator.hasNext()){
			GraphEdge component = componentIterator.next();
			components.put(component.id(), component);
		}
	}
}
