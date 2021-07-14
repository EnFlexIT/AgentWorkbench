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
package org.awb.env.networkModel.persistence;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.text.StringEscapeUtils;
import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphEdgeShapeConfiguration;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.adapter.dataModel.AbstractDataModelStorageHandler;
import org.awb.env.networkModel.positioning.GraphNodePositionFactory;

import de.enflexit.geography.coordinates.AbstractCoordinate;
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
	

	/**
	 * Instantiates a new graph model reader.
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
				graphNode.setId(StringEscapeUtils.unescapeHtml4(nmd.getId()));

				// --- Load the individual data model ---------------
				String dmBase64StringSaved = nmd.getProperty(GraphModelWriter.KEY_DATA_MODEL_BASE64_PROPERTY);
				if (dmBase64StringSaved!=null) {
					Vector<String> base64Vector = new Vector<String>();
					while (dmBase64StringSaved.contains("]")) {
						int cutAtOpen = dmBase64StringSaved.indexOf("[") + 1;
						int cutAtClose = dmBase64StringSaved.indexOf("]");
						String singleString = dmBase64StringSaved.substring(cutAtOpen, cutAtClose);
						base64Vector.add(singleString);
						dmBase64StringSaved = dmBase64StringSaved.substring(cutAtClose + 1);
					}
					if (base64Vector.size()>0) {
						graphNode.setDataModelBase64(base64Vector);
					}
				}

				// --- Load data model storage settings -------------
				String storageSettings = nmd.getProperty(GraphModelWriter.KEY_DATA_MODEL_STORAGE_SETTINGS);
				TreeMap<String, String> dataModelStorageSettings = AbstractDataModelStorageHandler.getDataModelStorageSettingsFromString(storageSettings);
				graphNode.setDataModelStorageSettings(dataModelStorageSettings);
				
				// --- Set the position of the node -----------------
				String posString = nmd.getProperty(GraphModelWriter.KEY_POSITION_PROPERTY);
				AbstractCoordinate coordinate = GraphNodePositionFactory.getCoordinateFromString(posString);
				if (coordinate == null) {
					System.err.println("Keine Position definiert für Knoten " + nmd.getId());
					coordinate = GraphNodePositionFactory.convertToCoordinate(new Point2D.Double(0, 0));
				}
				graphNode.setCoordinate(coordinate);
				
				// --- Set the position TreeMap ---------------------
				String posTreeMapString = nmd.getProperty(GraphModelWriter.KEY_POSITION_TREE_MAP);
				graphNode.setPositionTreeMapFromString(posTreeMapString);
				
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
				
				// --- Create GraphEdge and set ID and type ---------
				GraphEdge graphEdge = new GraphEdge(StringEscapeUtils.unescapeHtml4(emd.getId()), emd.getDescription());
				
				// --- Get the edge shape configuration -------------
				String shapeConfigString = emd.getProperty(GraphModelWriter.KEY_EDGE_SHAPE_CONFIGUARATION);
				graphEdge.setEdgeShapeConfiguration(GraphEdgeShapeConfiguration.getGraphEdgeShapeConfiguration(graphEdge, shapeConfigString));
				
				// --- Get the edge shape configuration TreeMap -----
				String shapeConfigTreeMapString = emd.getProperty(GraphModelWriter.KEY_EDGE_SHAPE_CONFIGUARATION_TREE_MAP);
				graphEdge.setEdgeShapeConfigurationTreeMap(GraphEdgeShapeConfiguration.getGraphEdgeShapeConfigurationTreeMap(graphEdge, shapeConfigTreeMapString));
				
				return graphEdge;
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
			public GraphEdge transform(HyperEdgeMetadata hyperEdgeMetadata) {
				return null;
			}
		};
		return hyperEdgeTransformer;
	}

}
