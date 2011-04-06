package agentgui.graphEnvironment.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import agentgui.core.application.Language;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;

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
	 * Internal counter, used for generating sequential CheckPoint indexes 
	 */
	private int ppCounter = 0;
	/**
	 * Default constructor
	 */
	public GridModel(){
		this.graph = new SparseGraph<GraphNode, GraphEdge>();
		this.components = new HashMap<String, GraphEdge>();
	}
	/**
	 * This method adds a simple grid component (i.e. one represented by only one edge) to the grid, without any connection to other components.
	 * @param component The simple component to add
	 */
	public void addSimpleComponent(GraphEdge component){
		GraphNode pp1 = new GraphNode();
		pp1.setId("PP"+(ppCounter++));
		
		GraphNode pp2 = new GraphNode();
		pp2.setId("PP"+(ppCounter++));
		graph.addVertex(pp1);
		graph.addVertex(pp2);
		graph.addEdge(component, pp1, pp2, EdgeType.DIRECTED);
		components.put(component.id(), component);
	}
	/**
	 * This method adds a simple grid component (i.e. one represented by only one edge) to the grid. It will be placed before the component specified by the given ID.
	 * @param component The simple component to add
	 * @param successorID The ID of the successor component
	 */
	public void addSimpleComponentBefore(GraphEdge component, String successorID){
		
		GraphNode pp2 = graph.getSource(components.get(successorID));
		if(pp2 != null){
			GraphNode pp1 = new GraphNode();
			pp1.setId("PP"+(ppCounter++));
			graph.addVertex(pp1);
			graph.addEdge(component, pp1, pp2, EdgeType.DIRECTED);
			components.put(component.id(), component);
		}else{
			System.err.println(Language.translate("Nachfolger-Knoten")+" "+successorID+" "+Language.translate("nicht gefunden!"));
		}
	}
	/**
	 * This method adds a simple grid component (i.e. one represented by only one edge) to the grid. It will be placed after the component specified by the given ID.
	 * @param component The simple component to add
	 * @param predecessorID The ID of the predecessor component
	 */
	public void addSimpleComponentAfter(GraphEdge component, String predecessorID){
		GraphNode pp1 = graph.getDest(components.get(predecessorID));
		if(pp1 != null){
			GraphNode pp2 = new GraphNode();
			pp2.setId("PP"+(ppCounter++));
			graph.addVertex(pp2);
			graph.addEdge(component, pp1, pp2, EdgeType.DIRECTED);
			components.put(component.id(), component);
		}else{
			System.err.println(Language.translate("Vorgänger-Knoten")+" "+predecessorID+" "+Language.translate("nicht gefunden!"));
		}
	}
	/**
	 * This method adds a simple grid component (i.e. one represented by only one edge) to the grid. It will be placed between the two components specified by the given IDs.
	 * @param component The simple component to add
	 * @param predecessorID The ID of the predecessor component
	 * @param successorID The ID of the successor component
	 */
	public void addSimpleComponentBetween(GraphEdge component, String predecessorID, String successorID){
		GraphNode pp1 = graph.getDest(components.get(predecessorID));
		GraphNode pp2 = graph.getSource(components.get(successorID));
		if(pp1 != null && pp2 != null){
				graph.addEdge(component, pp1, pp2, EdgeType.DIRECTED);
//			}
		}else{
			if(pp1 == null){
				System.err.println(Language.translate("Vorgänger-Knoten")+" "+predecessorID+" "+Language.translate("nicht gefunden!"));
			}
			if(pp2 == null){
				System.err.println(Language.translate("Nachfolger-Knoten")+" "+successorID+" "+Language.translate("nicht gefunden!"));
			}
		}
	}
	public void fixDirections(){
		Iterator<GraphEdge> components = graph.getEdges().iterator();
		while(components.hasNext()){
			GraphEdge component = components.next();
			if(!component.getType().equals("compressor")){
				GraphNode pp1 = graph.getSource(component);
				GraphNode pp2 = graph.getDest(component);
				graph.removeEdge(component);
				graph.addEdge(component, pp1, pp2, EdgeType.UNDIRECTED);
			}
		}
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
	Graph<GraphNode, GraphEdge> getGraph() {
		return graph;
	}
	void setGraph(Graph<GraphNode, GraphEdge> graph) {
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
