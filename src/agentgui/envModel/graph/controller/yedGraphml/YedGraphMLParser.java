/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.envModel.graph.controller.yedGraphml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.apache.commons.collections15.Transformer;

import agentgui.core.application.Language;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.graphml.EdgeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMLReader2;
import edu.uci.ics.jung.io.graphml.GraphMetadata;
import edu.uci.ics.jung.io.graphml.HyperEdgeMetadata;
import edu.uci.ics.jung.io.graphml.NodeMetadata;
/**
 * Helper class for YedGraphMLFileImporter, converting the yEd graph to a temporary JUNG graph, which is then processed by the YedGraphMLFileImporter.
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 */
public class YedGraphMLParser {
	
	int edgeCount = 0;

	Transformer<GraphMetadata, SparseGraph<TempNode, Object>> graphTransformer = new Transformer<GraphMetadata, SparseGraph<TempNode,Object>>() {

		@Override
		public SparseGraph<TempNode, Object> transform(GraphMetadata gmd) {
			return new SparseGraph<TempNode, Object>();
		}
	};
	
	Transformer<NodeMetadata, TempNode> nodeTransformer = new Transformer<NodeMetadata, TempNode>() {

		@Override
		public TempNode transform(NodeMetadata nmd) {
			String id = nmd.getId();
			String type = nmd.getProperty("d5");	// yEd Properties data field
			return new TempNode(id, type);
		}
	};
	
	Transformer<EdgeMetadata, Object> edgeTransformer = new Transformer<EdgeMetadata, Object>() {

		@Override
		public Object transform(EdgeMetadata emd) {
			emd.setDirected(true);
			return new Integer(edgeCount++);
		}
	};
	
	Transformer<HyperEdgeMetadata, Object> hyperedgeTransformer = new Transformer<HyperEdgeMetadata, Object>() {

		@Override
		public Object transform(HyperEdgeMetadata arg0) {
			return null;
		}
	};
	
	Graph<TempNode, Object> getGraph(File graphFile){
		Graph<TempNode, Object> graph = null;
		if(graphFile.exists()){
			try {
				FileReader fr = new FileReader(graphFile);
				GraphMLReader2<SparseGraph<TempNode, Object>, TempNode, Object> graphReader = new GraphMLReader2<SparseGraph<TempNode,Object>, TempNode, Object>(fr, graphTransformer, nodeTransformer, edgeTransformer, hyperedgeTransformer);
				graph = graphReader.readGraph();
			} catch (FileNotFoundException e) {
				System.err.println(Language.translate("GraphML-Datei "+graphFile.getName()+"nicht gefunden!"));
			} catch (GraphIOException e) {
				System.err.println(Language.translate("Fehler beim Lesen des Graphen"));
			}
		}
		return graph;
	}
}
