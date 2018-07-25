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
package agentgui.envModel.graph.networkModel;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Vector;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.io.graphml.EdgeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMLReader2;
import edu.uci.ics.jung.io.graphml.GraphMetadata;
import edu.uci.ics.jung.io.graphml.HyperEdgeMetadata;
import edu.uci.ics.jung.io.graphml.NodeMetadata;

/**
 * {@link GraphMLReader2} extension for reading graph-based environment models from a GraphML file.
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class GraphModelReader extends GraphMLReader2<Graph<GraphNode, GraphEdge>, GraphNode, GraphEdge>{
	
	private static final String KEY_POSITION_PROPERTY = "pos";
	private static final String KEY_DATA_MODEL_BASE64_PROPERTY = "dataModelVectorBase64Encoded";
	
	/**
	 * Instantiates a new graph model reader.
	 *
	 * @param graphMLFile the graph ML file
	 */
	public GraphModelReader(File graphMLFile) {
		super(getFileReader(graphMLFile), getGraphTransformerInternal(), getVertexTransformerInternal(), getEdgeTransformerInternal(), getHyperEdgeTransformerInternal());
	}
	
	/**
	 * Gets the file reader.
	 *
	 * @param graphMlFile the graph ml file
	 * @return the file reader
	 */
	private static FileReader getFileReader(File graphMlFile) {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(graphMlFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fileReader;
	}
	
	/**
	 * Gets the graph transformer internal.
	 *
	 * @return the graph transformer internal
	 */
	private static Transformer<GraphMetadata, Graph<GraphNode, GraphEdge>> getGraphTransformerInternal() {
		Transformer<GraphMetadata, Graph<GraphNode, GraphEdge>> graphTransformer = new Transformer<GraphMetadata, Graph<GraphNode, GraphEdge>>() {
		@Override
		public SparseGraph<GraphNode, GraphEdge> transform(GraphMetadata gmd) {
			return new SparseGraph<GraphNode, GraphEdge>();
		}};
		return graphTransformer;
	} 
	
	/**
	 * Gets the vertex transformer internal.
	 * @return the vertex transformer internal
	 */
	private static Transformer<NodeMetadata, GraphNode> getVertexTransformerInternal() {
		Transformer<NodeMetadata, GraphNode> nodeTransformer = new Transformer<NodeMetadata, GraphNode>() {
			@Override
			public GraphNode transform(NodeMetadata nmd) {

				// --- Create GraphNode instance and set ID ---------
				GraphNode graphNode = new GraphNode();
				graphNode.setId(nmd.getId());

				// --- Load the individual data model ---------------
				String dmBase64StringSaved = nmd.getProperty(KEY_DATA_MODEL_BASE64_PROPERTY);
				if (dmBase64StringSaved != null) {
					Vector<String> base64Vector = new Vector<String>();
					while (dmBase64StringSaved.contains("]")) {
						int cutAtOpen = dmBase64StringSaved.indexOf("[") + 1;
						int cutAtClose = dmBase64StringSaved.indexOf("]");
						String singleString = dmBase64StringSaved.substring(cutAtOpen, cutAtClose);
						base64Vector.add(singleString);
						dmBase64StringSaved = dmBase64StringSaved.substring(cutAtClose + 1);
					}
					if (base64Vector.size() > 0) {
						graphNode.setDataModelBase64(base64Vector);
					}
				}

				// --- Set the position of the node -----------------
				Point2D pos = null;
				String posString = nmd.getProperty(KEY_POSITION_PROPERTY);
				if (posString != null) {
					String[] coords = posString.split(":");
					if (coords.length == 2) {
						double xPos = Double.parseDouble(coords[0]);
						double yPos = Double.parseDouble(coords[1]);
						pos = new Point2D.Double(xPos, yPos);
					}
				}
				if (pos == null) {
					System.err.println("Keine Position definiert für Knoten " + nmd.getId());
					pos = new Point2D.Double(0, 0);
				}
				graphNode.setPosition(pos);
				return graphNode;
			}
		};
		return nodeTransformer;
	}
	
	/**
	 * Gets the edge transformer internal.
	 * @return the edge transformer internal
	 */
	private static Transformer<EdgeMetadata, GraphEdge> getEdgeTransformerInternal() {
		Transformer<EdgeMetadata, GraphEdge> edgeTransformer = new Transformer<EdgeMetadata, GraphEdge>() {
			@Override
			public GraphEdge transform(EdgeMetadata emd) {
				return new GraphEdge(emd.getId(), emd.getDescription());
			}
		};
		return edgeTransformer;
	}
	
	/**
	 * Gets the hyper edge transformer internal.
	 * @return the hyper edge transformer internal
	 */
	private static Transformer<HyperEdgeMetadata, GraphEdge> getHyperEdgeTransformerInternal() {
		Transformer<HyperEdgeMetadata, GraphEdge> hyperEdgeTransformer = new Transformer<HyperEdgeMetadata, GraphEdge>() {
			@Override
			public GraphEdge transform(HyperEdgeMetadata arg0) {
				return null;
			}
		};
		return hyperEdgeTransformer;
	}

}
