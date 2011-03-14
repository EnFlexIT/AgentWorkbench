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
	private Graph<PropagationPoint, GridComponent> graph;
	/**
	 * HashMap providing access to the grid components based on the component's agentID
	 */
	private HashMap<String, GridComponent> components;
	/**
	 * Internal counter, used for generating sequential CheckPoint indexes 
	 */
	private int ppCounter = 0;
	/**
	 * Default constructor
	 */
	public GridModel(){
		this.graph = new SparseGraph<PropagationPoint, GridComponent>();
		this.components = new HashMap<String, GridComponent>();
	}
	/**
	 * This method adds a simple grid component (i.e. one represented by only one edge) to the grid, without any connection to other components.
	 * @param component The simple component to add
	 */
	public void addSimpleComponent(GridComponent component){
		PropagationPoint pp1 = new PropagationPoint(ppCounter++);
		PropagationPoint pp2 = new PropagationPoint(ppCounter++);
		graph.addVertex(pp1);
		graph.addVertex(pp2);
		graph.addEdge(component, pp1, pp2, EdgeType.DIRECTED);
		components.put(component.getAgentID(), component);
	}
	/**
	 * This method adds a simple grid component (i.e. one represented by only one edge) to the grid. It will be placed before the component specified by the given ID.
	 * @param component The simple component to add
	 * @param successorID The ID of the successor component
	 */
	public void addSimpleComponentBefore(GridComponent component, String successorID){
		
		PropagationPoint pp2 = graph.getSource(components.get(successorID));
		if(pp2 != null){
			PropagationPoint pp1 = new PropagationPoint(ppCounter++);
			graph.addVertex(pp1);
			graph.addEdge(component, pp1, pp2, EdgeType.DIRECTED);
			components.put(component.getAgentID(), component);
		}else{
			System.err.println(Language.translate("Nachfolger-Knoten")+" "+successorID+" "+Language.translate("nicht gefunden!"));
		}
	}
	/**
	 * This method adds a simple grid component (i.e. one represented by only one edge) to the grid. It will be placed after the component specified by the given ID.
	 * @param component The simple component to add
	 * @param predecessorID The ID of the predecessor component
	 */
	public void addSimpleComponentAfter(GridComponent component, String predecessorID){
		PropagationPoint pp1 = graph.getDest(components.get(predecessorID));
		if(pp1 != null){
			PropagationPoint pp2 = new PropagationPoint(ppCounter++);
			graph.addVertex(pp2);
			graph.addEdge(component, pp1, pp2, EdgeType.DIRECTED);
			components.put(component.getAgentID(), component);
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
	public void addSimpleComponentBetween(GridComponent component, String predecessorID, String successorID){
		PropagationPoint pp1 = graph.getDest(components.get(predecessorID));
		PropagationPoint pp2 = graph.getSource(components.get(successorID));
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
		Iterator<GridComponent> components = graph.getEdges().iterator();
		while(components.hasNext()){
			GridComponent component = components.next();
			if(!component.getType().equals("compressor")){
				PropagationPoint pp1 = graph.getSource(component);
				PropagationPoint pp2 = graph.getDest(component);
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
	public GridComponent getComponent(String id){
		return components.get(id);
	}
	/**
	 * Returns a list of all GridComponents
	 * @return The list
	 */
	public Collection<GridComponent> getComponents() {
		return graph.getEdges();
	}
	Graph<PropagationPoint, GridComponent> getGraph() {
		return graph;
	}
	void setGraph(Graph<PropagationPoint, GridComponent> graph) {
		this.graph = graph;
	}
}
