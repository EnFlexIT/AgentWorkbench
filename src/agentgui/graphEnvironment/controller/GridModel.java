package agentgui.graphEnvironment.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import edu.uci.ics.jung.graph.Graph;
import gasmas.ontology.GridComponent;
import gasmas.ontology.GridLink;

/**
 * Wrapper class encapsulating the JUNG graph and adding features
 * @author Nils
 *
 */
public class GridModel {
	/**
	 * The graph representing the grid
	 */
	private Graph<GridComponent, GridLink> grid;
	/**
	 * HashMap providing component access by ID
	 */
	private HashMap<String, GridComponent> components;
	
	/**
	 * Constructor
	 * @param grid The graph that will be wrapped
	 */
	public GridModel(Graph<GridComponent, GridLink> grid){
		this.grid = grid;
		this.buildHashmap();
	}
	
	/**
	 * This method builds the HashMap
	 */
	private void buildHashmap(){
		components = new HashMap<String, GridComponent>();
		Iterator<GridComponent> iter = (Iterator<GridComponent>) this.grid.getVertices().iterator();
		while(iter.hasNext()){
			GridComponent comp = iter.next();
			components.put(comp.getId(), comp);
		}
	}
	
	/**
	 * This method gets a grid component / graph node by its' ID
	 * @param id The ID of the component
	 * @return The component
	 */
	public GridComponent getComponent(String id){
		return components.get(id);
	}
	
	public Collection getComponents(){
		return grid.getVertices();
	}
			
}
