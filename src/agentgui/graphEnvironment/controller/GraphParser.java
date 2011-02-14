package agentgui.graphEnvironment.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

import org.apache.commons.collections15.Transformer;

import agentgui.core.application.Language;

import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.graphml.EdgeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMLReader2;
import edu.uci.ics.jung.io.graphml.GraphMetadata;
import edu.uci.ics.jung.io.graphml.HyperEdgeMetadata;
import edu.uci.ics.jung.io.graphml.NodeMetadata;
import gasmas.ontology.GridComponent;
import gasmas.ontology.GridLink;

public class GraphParser {
	
	private HashMap<String, String> ontoClasses = null;
	
	// Transformer object generating the graph
	Transformer<GraphMetadata, SparseGraph<GridComponent, GridLink>> graphTransformer = new Transformer<GraphMetadata, SparseGraph<GridComponent, GridLink>>() {

		@Override
		public SparseGraph<GridComponent, GridLink> transform(GraphMetadata gmd) {
			return new SparseGraph<GridComponent, GridLink>();
		}
	};
	
	// Transformer object generating nodes
	Transformer<NodeMetadata, GridComponent> nodeTransformer = new Transformer<NodeMetadata, GridComponent>() {

		@Override
		public GridComponent transform(NodeMetadata nmd) {
			GridComponent newNode = null;
			String type = nmd.getProperty("d5");	// The data field from yED
			String className = ontoClasses.get(type);
			if(className != null){
				try {
					Class<?> ontoClass = Class.forName(className);
					newNode = (GridComponent) ontoClass.newInstance();
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
			if(newNode != null){
				newNode.setId(nmd.getId());
			}
			return newNode;
		}
	};
	
	// Transformer object generating edges
	Transformer<EdgeMetadata, GridLink> edgeTransformer = new Transformer<EdgeMetadata, GridLink>() {

		@Override
		public GridLink transform(EdgeMetadata emd) {
//			emd.setDirected(false);
			GridLink newEdge = new GridLink();
			newEdge.setSourceID(emd.getSource());
			newEdge.setTargetID(emd.getTarget());
//			System.out.println("Processing link from node "+emd.getSource()+" to node "+emd.getTarget());
			return newEdge;
		}
	};
	
	// Needed to satisfy GraphMLReader2 constructor requirements, doing nothing
	Transformer<HyperEdgeMetadata, GridLink> hyperEdgeTransformer = new Transformer<HyperEdgeMetadata, GridLink>() {

		@Override
		public GridLink transform(HyperEdgeMetadata hmd) {
			return null;
		}
	};
	
	public GraphParser(HashMap<String, String> ontoClasses){
		this.ontoClasses = ontoClasses;
	}
	
	SparseGraph<GridComponent, GridLink> getGraph(File graphMLFile){
		
		SparseGraph<GridComponent, GridLink> graph = null;
		if(graphMLFile.exists()){
			try {
				FileReader fr = new FileReader(graphMLFile);
				GraphMLReader2<SparseGraph<GridComponent, GridLink>, GridComponent, GridLink> graphReader = new GraphMLReader2<SparseGraph<GridComponent,GridLink>, GridComponent, GridLink>(fr, graphTransformer, nodeTransformer, edgeTransformer, hyperEdgeTransformer);
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
