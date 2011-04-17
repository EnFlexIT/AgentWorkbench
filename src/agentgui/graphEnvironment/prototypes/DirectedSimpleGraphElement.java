package agentgui.graphEnvironment.prototypes;

import agentgui.core.application.Language;
import agentgui.graphEnvironment.environmentModel.GraphEdge;
import agentgui.graphEnvironment.environmentModel.GraphNode;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

public class DirectedSimpleGraphElement extends GraphElementPrototype {
	
	private GraphNode entry;
	
	private GraphNode exit;

	@Override
	public boolean addToGraph(Graph<GraphNode, GraphEdge> graph) {
		this.graph = graph;
		entry = new GraphNode();
		entry.setId("PP"+(nodeCounter++));
		exit = new GraphNode();
		exit.setId("PP"+(nodeCounter++));
		GraphEdge e = new GraphEdge(getId(), getType());
		
		graph.addVertex(entry);
		graph.addVertex(exit);
		graph.addEdge(e, entry, exit, EdgeType.DIRECTED);
		
		return true;
	}

	@Override
	public boolean addAfter(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype predecessor) {
		this.graph = graph;
		entry = predecessor.getFreeEntry();
		if(entry != null){
			exit = new GraphNode();
			exit.setId("PP"+(nodeCounter++));
			graph.addVertex(exit);
			graph.addEdge(new GraphEdge(getId(), getType()), entry, exit, EdgeType.DIRECTED);
			return true;
		}else{
			System.err.println(Language.translate("Fehler beim Einfügen von Komponente "+getId()+" : Kein freier Anschlusspunkt an Vorgänger ")+predecessor.getId());
			return false;
		}
		
	}

	@Override
	public boolean addBefore(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype successor) {
		this.graph = graph;
		exit = successor.getFreeExit();
		if(exit != null){
			entry = new GraphNode();
			entry.setId("PP"+(nodeCounter++));
			graph.addVertex(entry);
			graph.addEdge(new GraphEdge(getId(), getType()), entry, exit, EdgeType.DIRECTED);
			return true;
		}else{
			System.err.println(Language.translate("Fehler beim Einfügen von Komponente "+getId()+" : Kein freier Anschlusspunkt an Nachfolger")+successor.getId());
			return false;
		}
	}

	@Override
	public boolean addBetween(Graph<GraphNode, GraphEdge> graph, GraphElementPrototype predecessor, GraphElementPrototype successor) {
		this.graph = graph;
		entry = predecessor.getFreeExit();
		exit = successor.getFreeEntry();
		if(entry != null && exit != null){
			graph.addEdge(new GraphEdge(getId(), getType()), entry, exit, EdgeType.DIRECTED);
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

}
