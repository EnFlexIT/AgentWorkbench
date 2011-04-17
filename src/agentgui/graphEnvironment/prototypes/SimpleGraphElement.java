package agentgui.graphEnvironment.prototypes;

import java.util.Iterator;
import java.util.Vector;

import agentgui.core.application.Language;
import agentgui.graphEnvironment.environmentModel.GraphEdge;
import agentgui.graphEnvironment.environmentModel.GraphNode;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * A simple graph / network element with two connection points,
 * represented by two nodes and an edge. 
 * @author Nils
 *
 */
public class SimpleGraphElement extends GraphElementPrototype {
	/**
	 * The GraphElementPrototype's connection points
	 */
	private Vector<GraphNode> nodes;
	
	public SimpleGraphElement(){
		nodes = new Vector<GraphNode>();
	}

	@Override
	public boolean addToGraph(Graph<GraphNode, GraphEdge> graph) {
		this.graph = graph;
		
		GraphNode entry = new GraphNode();
		entry.setId("PP"+(nodeCounter++));
		GraphNode exit = new GraphNode();
		exit.setId("PP"+(nodeCounter++));
		
		graph.addVertex(entry);
		graph.addVertex(exit);
		graph.addEdge(new GraphEdge(getId(), getType()), entry, exit, EdgeType.UNDIRECTED);
		
		nodes.add(entry);
		nodes.add(exit);
		
		return true;
	}

	@Override
	public boolean addAfter(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype predecessor) {
		this.graph = graph;
		GraphNode entry = predecessor.getFreeExit();
		if(entry != null){
			GraphNode exit = new GraphNode();
			exit.setId("PP"+(nodeCounter++));
			graph.addVertex(exit);
			graph.addEdge(new GraphEdge(getId(), getType()), entry, exit, EdgeType.UNDIRECTED);
			
			nodes.add(entry);
			nodes.add(exit);
			
			return true;
		}else{
			System.err.println(Language.translate("Fehler beim Einfügen von Komponente "+getId()+" : Kein freier Anschlusspunkt an Vorgänger ")+predecessor.getId());
			return false;
		}
	}

	@Override
	public boolean addBefore(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype successor) {
		this.graph = graph;
		GraphNode exit = successor.getFreeEntry();
		if(exit != null){
			GraphNode entry = new GraphNode();
			entry.setId("PP"+(nodeCounter++));
			graph.addVertex(entry);
			graph.addEdge(new GraphEdge(getId(), getType()), entry, exit, EdgeType.UNDIRECTED);
			
			nodes.add(entry);
			nodes.add(exit);
			
			return true;
		}else{
			System.err.println(Language.translate("Fehler beim Einfügen von Komponente "+getId()+" : Kein freier Anschlusspunkt an Nachfolger")+successor.getId());
			return false;
		}
	}

	@Override
	public boolean addBetween(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype predecessor, GraphElementPrototype successor) {
		this.graph = graph;
		GraphNode entry = predecessor.getFreeExit();
		GraphNode exit = successor.getFreeEntry();
		if(entry != null && exit != null){
			graph.addEdge(new GraphEdge(getId(), getType()), entry, exit, EdgeType.UNDIRECTED);
			
			nodes.add(entry);
			nodes.add(exit);
			
			return true;
		}else{
			if(entry == null){
				System.err.println(Language.translate("Fehler beim Einfügen von Komponente "+getId()+" : Kein freier Anschlusspunkt an Vorgänger ")+predecessor.getId());
			}
			if(exit == null){
				System.err.println(Language.translate("Fehler beim Einfügen von Komponente "+getId()+" : Kein freier Anschlusspunkt an Nachfolger")+successor.getId());
			}
			return false;
		}
	}

	@Override
	public GraphNode getFreeEntry() {
		Iterator<GraphNode> iter = nodes.iterator();
		while(iter.hasNext()){
			GraphNode node = iter.next();
			if(graph.getNeighborCount(node) < 2){
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

}
