package agentgui.envModel.graph.prototypes;

import java.util.HashSet;

import agentgui.core.application.Language;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.GraphNode;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
/**
 * A simple directed graph / network element with two connection points,
 * represented by two nodes and a directed edge.
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 *
 */
public class DirectedSimpleGraphElement extends GraphElementPrototype {
	/**
	 * The node where the edge comes from
	 */
	private GraphNode entry;
	/**
	 * The node where the edge goes to
	 */
	private GraphNode exit;

	@Override
	public HashSet<GraphElement> addToGraph(Graph<GraphNode, GraphEdge> graph) {
		this.graph = graph;
		
		// Create nodes and edge
		entry = new GraphNode();
		entry.setId("PP"+(nodeCounter++));
		exit = new GraphNode();
		exit.setId("PP"+(nodeCounter++));
		GraphEdge e = new GraphEdge(getId(), getType());
		
		// Add them to the graph
		graph.addVertex(entry);
		graph.addVertex(exit);
		graph.addEdge(e, entry, exit, EdgeType.DIRECTED);
		
		// Create a HashSet containing the nodes and edge ant return it
		HashSet<GraphElement> elements = new HashSet<GraphElement>();
		elements.add(e);
		elements.add(entry);
		elements.add(exit);
		return elements;
	}

	@Override
	public HashSet<GraphElement> addAfter(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype predecessor) {
		this.graph = graph;
		// Get the predecessor node
		entry = predecessor.getFreeEntry();
		if(entry != null){
			// Create successor node and edge
			exit = new GraphNode();
			exit.setId("PP"+(nodeCounter++));
			GraphEdge e = new GraphEdge(getId(), getType());
			
			// Add them to the graph
			graph.addVertex(exit);
			graph.addEdge(e, entry, exit, EdgeType.DIRECTED);
			
			// Create a HashSet containing the nodes and edge and return it
			HashSet<GraphElement> elements = new HashSet<GraphElement>();
			elements.add(e);
			elements.add(entry);
			elements.add(exit);
			return elements;
		}else{
			System.err.println(Language.translate("Fehler beim Einfügen von Komponente "+getId()+" : Kein freier Anschlusspunkt an Vorgänger ")+predecessor.getId());
			return null;
		}
		
	}

	@Override
	public HashSet<GraphElement> addBefore(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype successor) {
		this.graph = graph;
		// Get the successor node
		exit = successor.getFreeExit();
		if(exit != null){
			// Create predecessor node and edge
			entry = new GraphNode();
			entry.setId("PP"+(nodeCounter++));
			GraphEdge e = new GraphEdge(getId(), getType());
			
			// Add them to the graph
			graph.addVertex(entry);
			graph.addEdge(e, entry, exit, EdgeType.DIRECTED);
			
			// Create a HashSet containing the nodes and edge and return it
			HashSet<GraphElement> elements = new HashSet<GraphElement>();
			elements.add(e);
			elements.add(entry);
			elements.add(exit);
			return elements;
		}else{
			System.err.println(Language.translate("Fehler beim Einfügen von Komponente "+getId()+" : Kein freier Anschlusspunkt an Nachfolger")+successor.getId());
			return null;
		}
	}

	@Override
	public HashSet<GraphElement> addBetween(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype predecessor, GraphElementPrototype successor) {
		this.graph = graph;
		// Find predecessor and successor node
		entry = predecessor.getFreeExit();
		exit = successor.getFreeEntry();
		if(entry != null && exit != null){
			// Create the edge and add it to the graph
			GraphEdge e = new GraphEdge(getId(), getType());
			graph.addEdge(e, entry, exit, EdgeType.DIRECTED);
			
			// Create a HashSet containing the nodes and edge and return it
			HashSet<GraphElement> elements = new HashSet<GraphElement>();
			elements.add(e);
			elements.add(entry);
			elements.add(exit);
			return elements;
		}else{
			if(entry == null){
				System.err.println(Language.translate("Fehler beim Einfügen von Komponente "+getId()+" : Kein freier Anschlusspunkt an Vorgänger ")+predecessor.getId());
			}
			if(exit == null){
				System.err.println(Language.translate("Fehler beim Einfügen von Komponente "+getId()+" : Kein freier Anschlusspunkt an Nachfolger")+successor.getId());
			}
			return null;
		}
	}

	@Override
	public GraphNode getFreeEntry() {
		// Only one incoming edge is possible
		if(graph.getInEdges(entry).size() == 0){
			return entry;
		}else{
			return null;
		}
	}

	@Override
	public GraphNode getFreeExit() {
		// Only one outgoing edge is possible
		if(graph.getOutEdges(exit).size() == 0){
			return exit;
		}else{
			return null;
		}
	}

	@Override
	public boolean isDirected() {
		return true;
	}

}
