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

import java.util.HashSet;

import org.apache.commons.text.StringEscapeUtils;
import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphEdgeShapeConfiguration;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.adapter.dataModel.AbstractDataModelStorageHandler;

import com.google.common.base.Function;

import edu.uci.ics.jung.io.GraphMLWriter;

/**
 * {@link GraphMLWriter} extension for writing graph-based environment models to a GraphML file
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class GraphModelWriter extends GraphMLWriter<GraphNode, GraphEdge>{
	
	private static final String GraphML_NewLine = System.getProperty("line.separator");
	private static final String GraphML_VectorBrackets = "[conent]";
	
	protected static final String KEY_POSITION_PROPERTY = "pos";
	protected static final String KEY_POSITION_TREE_MAP = "positionTreeMap";
	protected static final String KEY_DATA_MODEL_STORAGE_SETTINGS = "dataModelStorageSettings";
	protected static final String KEY_DATA_MODEL_BASE64_PROPERTY = "dataModelVectorBase64Encoded";
	
	protected static final String KEY_EDGE_SHAPE_CONFIGUARATION = "edgeShapeConfiguration";
	protected static final String KEY_EDGE_SHAPE_CONFIGUARATION_TREE_MAP = "edgeShapeConfigurationTreeMap";
	
	
	private HashSet<String> allowedLayoutIDs;

	
	/**
	 * Instantiates a new graph model writer.
	 * @param allowedLayoutIDs the allowed layout ID's
	 */
	public GraphModelWriter(HashSet<String> allowedLayoutIDs) {
		this.allowedLayoutIDs = allowedLayoutIDs;
		this.initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		this.setVertexIDs(new Function<GraphNode, String>() {
			@Override
			public String apply(GraphNode graphNode) {
				return StringEscapeUtils.escapeHtml4(graphNode.getId());
			}
		});
		this.addVertexData(KEY_POSITION_PROPERTY, "position", "", new Function<GraphNode, String>() {
			@Override
			public String apply(GraphNode graphNode) {
				return graphNode.getCoordinate().serialize();
			}
		});
		this.addVertexData(KEY_POSITION_TREE_MAP, "Position TreeMap", "", new Function<GraphNode, String>() {
			@Override
			public String apply(GraphNode graphNode) {
				return graphNode.getPositionTreeMapAsString(GraphModelWriter.this.allowedLayoutIDs);
			}
		});
		this.addVertexData(KEY_DATA_MODEL_STORAGE_SETTINGS, "Data model storage settings", "", new Function<GraphNode, String>() {
			@Override
			public String apply(GraphNode graphNode) {
				return AbstractDataModelStorageHandler.getDataModelStorageSettingsAsString(graphNode);
			}
		});
		this.addVertexData(KEY_DATA_MODEL_BASE64_PROPERTY, "Base64 encoded individual data model", "", new Function<GraphNode, String>() {
			@Override
			public String apply(GraphNode graphNode) {
				String dmBase64StringSave = null;
				if (graphNode.getDataModelBase64() != null) {
					for (String dmBase64String : graphNode.getDataModelBase64()) {
						dmBase64String = GraphML_VectorBrackets.replace("conent", dmBase64String);
						if (dmBase64StringSave == null) {
							dmBase64StringSave = dmBase64String;
						} else {
							dmBase64StringSave = dmBase64StringSave + GraphML_NewLine + dmBase64String;
						}
					}
				}
				if (dmBase64StringSave != null) {
					dmBase64StringSave = GraphML_NewLine + dmBase64StringSave + GraphML_NewLine;
				}
				return dmBase64StringSave;
			}
		});
		
		
		this.setEdgeIDs(new Function<GraphEdge, String>() {
			@Override
			public String apply(GraphEdge graphEdge) {
				return StringEscapeUtils.escapeHtml4(graphEdge.getId());
			}
		});
		this.setEdgeDescriptions(new Function<GraphEdge, String>() {
			@Override
			public String apply(GraphEdge graphEdge) {
				return graphEdge.getComponentType();
			}
		});
		this.addEdgeData(KEY_EDGE_SHAPE_CONFIGUARATION, "Edge-Configuration", "", new Function<GraphEdge, String>() {
			@Override
			public String apply(GraphEdge graphEdge) {
				if (graphEdge.getEdgeShapeConfiguration()!=null) {
					return graphEdge.getEdgeShapeConfiguration().getConfigurationAsString(); 
				}
				return null;
			}
		});
		this.addEdgeData(KEY_EDGE_SHAPE_CONFIGUARATION_TREE_MAP, "Edge-Configuration TreeMap", "", new Function<GraphEdge, String>() {
			@Override
			public String apply(GraphEdge graphEdge) {
				return GraphEdgeShapeConfiguration.getGraphEdgeShapeConfigurationTreeMapAsString(graphEdge.getEdgeShapeConfigurationTreeMap(), GraphModelWriter.this.allowedLayoutIDs);
			}
		});
		
	}
	
}
