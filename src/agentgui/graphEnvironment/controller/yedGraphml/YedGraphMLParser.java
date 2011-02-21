package agentgui.graphEnvironment.controller.yedGraphml;

import jade.content.Concept;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

import org.apache.commons.collections15.Transformer;

import agentgui.core.application.Language;
import agentgui.graphEnvironment.controller.GraphEdge;
import agentgui.graphEnvironment.controller.GraphNode;

import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.graphml.EdgeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMLReader2;
import edu.uci.ics.jung.io.graphml.GraphMetadata;
import edu.uci.ics.jung.io.graphml.HyperEdgeMetadata;
import edu.uci.ics.jung.io.graphml.NodeMetadata;

public class YedGraphMLParser {
	
	private HashMap<String, String> ontoClasses = null;
	
	// Transformer object generating the graph
	Transformer<GraphMetadata, SparseGraph<GraphNode, GraphEdge>> graphTransformer = new Transformer<GraphMetadata, SparseGraph<GraphNode, GraphEdge>>() {

		@Override
		public SparseGraph<GraphNode, GraphEdge> transform(GraphMetadata gmd) {
			return new SparseGraph<GraphNode, GraphEdge>();
		}
	};
	
	// Transformer object generating nodes
	Transformer<NodeMetadata, GraphNode> nodeTransformer = new Transformer<NodeMetadata, GraphNode>() {

		@Override
		public GraphNode transform(NodeMetadata nmd) {
			Concept newNode = null;
			String type = nmd.getProperty("d5");	// The data field from yED
			String className = ontoClasses.get(type);
			if(className != null){
				try {
					Class<?> ontoClass = Class.forName(className);
					newNode = (Concept) ontoClass.newInstance();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return new GraphNode(nmd.getId(), newNode);
		}
	};
	
	// Transformer object generating edges
	Transformer<EdgeMetadata, GraphEdge> edgeTransformer = new Transformer<EdgeMetadata, GraphEdge>() {

		@Override
		public GraphEdge transform(EdgeMetadata emd) {
//			emd.setDirected(false);
			GraphEdge newEdge = new GraphEdge();
//			System.out.println("Processing link from node "+emd.getSource()+" to node "+emd.getTarget());
			return newEdge;
		}
	};
	
	// Needed to satisfy GraphMLReader2 constructor requirements, doing nothing
	Transformer<HyperEdgeMetadata, GraphEdge> hyperEdgeTransformer = new Transformer<HyperEdgeMetadata, GraphEdge>() {

		@Override
		public GraphEdge transform(HyperEdgeMetadata hmd) {
			return null;
		}
	};
	
	public YedGraphMLParser(HashMap<String, String> ontoClasses){
		this.ontoClasses = ontoClasses;
	}
	
	SparseGraph<GraphNode, GraphEdge> getGraph(File graphMLFile){
		
		SparseGraph<GraphNode, GraphEdge> graph = null;
		if(graphMLFile.exists()){
			try {
				FileReader fr = new FileReader(graphMLFile);
				GraphMLReader2<SparseGraph<GraphNode, GraphEdge>, GraphNode, GraphEdge> graphReader = new GraphMLReader2<SparseGraph<GraphNode,GraphEdge>, GraphNode, GraphEdge>(fr, graphTransformer, nodeTransformer, edgeTransformer, hyperEdgeTransformer);
				graph = graphReader.readGraph();
			} catch (FileNotFoundException e) {
				System.err.println(Language.translate("GraphML-Datei "+graphMLFile.getName()+"nicht gefunden!"));
			} catch (GraphIOException e) {
				System.err.println(Language.translate("Fehler beim Lesen des Graphen"));
			}
		}
		
		return graph;
	}
	
	

}
