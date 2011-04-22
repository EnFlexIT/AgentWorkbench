package agentgui.graphEnvironment.prototypes;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

import agentgui.core.application.Language;
import agentgui.graphEnvironment.environmentModel.GraphEdge;
import agentgui.graphEnvironment.environmentModel.GraphElement;
import agentgui.graphEnvironment.environmentModel.GraphNode;

/**
 * A graph / network element with three connection points and direct
 * connections between each two of them. Represented by a triangle 
 * consisting of three nodes and three edges.
 * @author Nils
 *
 */
public class TriangeGraphElement extends GraphElementPrototype {
	/**
	 * The GraphElementPrototype's connection points
	 */
	private Vector<GraphNode> nodes;
	
	public TriangeGraphElement(){
		nodes = new Vector<GraphNode>();
	}
	@Override
	public HashSet<GraphElement> addToGraph(Graph<GraphNode, GraphEdge> graph) {
		this.graph = graph;
		// Create nodes and edges
		GraphNode n1 = new GraphNode();
		n1.setId("PP"+(nodeCounter++));
		GraphNode n2 = new GraphNode();
		n2.setId("PP"+(nodeCounter++));
		GraphNode n3 = new GraphNode();
		n3.setId("PP"+(nodeCounter++));
		GraphEdge e1 = new GraphEdge(getId()+"_1", getType());
		GraphEdge e2 = new GraphEdge(getId()+"_2", getType());
		GraphEdge e3 = new GraphEdge(getId()+"_3", getType());
		
		// Add them to the graph
		graph.addVertex(n1);
		graph.addVertex(n2);
		graph.addVertex(n3);
		graph.addEdge(e1, n1, n2, EdgeType.UNDIRECTED);
		graph.addEdge(e2, n1, n3, EdgeType.UNDIRECTED);
		graph.addEdge(e3, n2, n3, EdgeType.UNDIRECTED);
		
		// Add the nodes to this GraphElementPrototypes node list
		nodes.add(n1);
		nodes.add(n2);
		nodes.add(n3);
		
		// Create a HashSet containing the nodes and edge and return it
		HashSet<GraphElement> elements = new HashSet<GraphElement>();
		elements.add(n1);
		elements.add(n2);
		elements.add(n3);
		elements.add(e1);
		elements.add(e2);
		elements.add(e3);
		return elements;
	}

	@Override
	public HashSet<GraphElement> addAfter(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype predecessor) {
		this.graph = graph;
		GraphNode n1 = predecessor.getFreeExit();
		if(n1 != null){
			// Create nodes and edges
			GraphNode n2 = new GraphNode();
			n2.setId("PP"+(nodeCounter++));
			GraphNode n3 = new GraphNode();
			n3.setId("PP"+(nodeCounter++));
			GraphEdge e1 = new GraphEdge(getId()+"_1", getType());
			GraphEdge e2 = new GraphEdge(getId()+"_2", getType());
			GraphEdge e3 = new GraphEdge(getId()+"_3", getType());
			
			// Add them to the graph
			graph.addVertex(n2);
			graph.addVertex(n3);
			graph.addEdge(e1, n1, n2, EdgeType.UNDIRECTED);
			graph.addEdge(e2, n1, n3, EdgeType.UNDIRECTED);
			graph.addEdge(e3, n2, n3, EdgeType.UNDIRECTED);
			
			// Add the nodes to this GraphElementPrototypes node list
			nodes.add(n1);
			nodes.add(n2);
			nodes.add(n3);
			
			// Create a HashSet containing the nodes and edge and return it
			HashSet<GraphElement> elements = new HashSet<GraphElement>();
			elements.add(n1);
			elements.add(n2);
			elements.add(n3);
			elements.add(e1);
			elements.add(e2);
			elements.add(e3);
			return elements;
		}else{
			System.err.println(Language.translate("Fehler beim Einfügen von Komponente "+getId()+" : Kein freier Anschlusspunkt an Vorgänger ")+predecessor.getId());
			return null;
		}
	}

	@Override
	public HashSet<GraphElement> addBefore(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype successor) {
		this.graph = graph;
		GraphNode n1 = successor.getFreeEntry();
		if(n1 != null){
			// Create nodes and edges
			GraphNode n2 = new GraphNode();
			n2.setId("PP"+(nodeCounter++));
			GraphNode n3 = new GraphNode();
			n3.setId("PP"+(nodeCounter++));
			GraphEdge e1 = new GraphEdge(getId()+"_1", getType());
			GraphEdge e2 = new GraphEdge(getId()+"_2", getType());
			GraphEdge e3 = new GraphEdge(getId()+"_3", getType());
			
			// Add them to the graph
			graph.addVertex(n2);
			graph.addVertex(n3);
			graph.addEdge(e1, n1, n2, EdgeType.UNDIRECTED);
			graph.addEdge(e2, n1, n3, EdgeType.UNDIRECTED);
			graph.addEdge(e3, n2, n3, EdgeType.UNDIRECTED);
			
			// Add the nodes to this GraphElementPrototypes node list
			nodes.add(n1);
			nodes.add(n2);
			nodes.add(n3);
			
			// Create a HashSet containing the nodes and edge and return it
			HashSet<GraphElement> elements = new HashSet<GraphElement>();
			elements.add(n1);
			elements.add(n2);
			elements.add(n3);
			elements.add(e1);
			elements.add(e2);
			elements.add(e3);
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
		GraphNode n1 = predecessor.getFreeExit();
		GraphNode n2 = successor.getFreeEntry();
		if(n1 != null && n2 != null){
			// Create node and edges
			GraphNode n3 = new GraphNode();
			n3.setId("PP"+(nodeCounter++));
			GraphEdge e1 = new GraphEdge(getId()+"_1", getType());
			GraphEdge e2 = new GraphEdge(getId()+"_2", getType());
			GraphEdge e3 = new GraphEdge(getId()+"_3", getType());
			
			// Add them to the graph
			graph.addVertex(n3);
			graph.addEdge(e1, n1, n2, EdgeType.UNDIRECTED);
			graph.addEdge(e2, n1, n3, EdgeType.UNDIRECTED);
			graph.addEdge(e3, n2, n3, EdgeType.UNDIRECTED);
			
			// Add the nodes to this GraphElementPrototypes node list
			nodes.add(n1);
			nodes.add(n2);
			nodes.add(n3);
			
			// Create a HashSet containing the nodes and edge and return it
			HashSet<GraphElement> elements = new HashSet<GraphElement>();
			elements.add(n1);
			elements.add(n2);
			elements.add(n3);
			elements.add(e1);
			elements.add(e2);
			elements.add(e3);
			return elements;
		}else{
			if(n1 == null){
				System.err.println(Language.translate("Fehler beim Einfügen von Komponente "+getId()+" : Kein freier Anschlusspunkt an Vorgänger ")+predecessor.getId());
			}
			if(n2 == null){
				System.err.println(Language.translate("Fehler beim Einfügen von Komponente "+getId()+" : Kein freier Anschlusspunkt an Nachfolger")+successor.getId());
			}
			return null;
		}
	}

	@Override
	public GraphNode getFreeEntry() {
		Iterator<GraphNode> iter = nodes.iterator();
		while(iter.hasNext()){
			GraphNode node = iter.next();
			if(graph.getNeighborCount(node) < 3){
				return node;
			}
		}
		return null;
	}

	@Override
	public GraphNode getFreeExit() {
		// Undirected GraphElement => no distinction between entry and exit necessary
		return getFreeEntry();
	}
	@Override
	public boolean isDirected() {
		return false;
	}
}
