package agentgui.graphEnvironment.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import edu.uci.ics.jung.graph.Graph;

/**
 * Wrapper class encapsulating the JUNG graph and adding features
 * @author Nils
 *
 */
public class GridModel {
	/**
	 * The graph representing the grid
	 */
	private Graph<GraphNode, GraphEdge> grid;
	/**
	 * HashMap providing component access by ID
	 */
	private HashMap<String, GraphNode> components;
	
	/**
	 * Constructor
	 * @param grid The graph that will be wrapped
	 */
	public GridModel(Graph<GraphNode, GraphEdge> grid){
		this.grid = grid;
		this.buildHashmap();
	}
	
	/**
	 * This method builds the HashMap
	 */
	private void buildHashmap(){
		components = new HashMap<String, GraphNode>();
		Iterator<GraphNode> iter = (Iterator<GraphNode>) this.grid.getVertices().iterator();
		while(iter.hasNext()){
			GraphNode comp = iter.next();
			components.put(comp.getId(), comp);
		}
	}
	
	/**
	 * This method gets a grid component / graph node by its' ID
	 * @param id The ID of the component
	 * @return The component
	 */
	public GraphNode getComponent(String id){
		return components.get(id);
	}
	
	@SuppressWarnings("rawtypes")
	public Collection getComponents(){
		return grid.getVertices();
	}
			
}
