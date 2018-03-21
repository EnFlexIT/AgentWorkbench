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

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.io.GraphMLWriter;

/**
 * {@link GraphMLWriter} extension for writing graph-based environment models to a GraphML file
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class GraphModelWriter extends GraphMLWriter<GraphNode, GraphEdge>{
	
	private static final String GraphML_NewLine = System.getProperty("line.separator");
	private static final String GraphML_VectorBrackets = "[conent]";
	private static final String KEY_POSITION_PROPERTY = "pos";
	private static final String KEY_DATA_MODEL_BASE64_PROPERTY = "dataModelVectorBase64Encoded";

	/**
	 * Instantiates a new graph model writer.
	 */
	public GraphModelWriter() {
		this.initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		this.setEdgeIDs(new Transformer<GraphEdge, String>() {
			@Override
			public String transform(GraphEdge arg0) {
				return arg0.getId();
			}
		});
		this.setEdgeDescriptions(new Transformer<GraphEdge, String>() {
			@Override
			public String transform(GraphEdge graphEdge) {
				return graphEdge.getComponentType();
			}
		});
		this.setVertexIDs(new Transformer<GraphNode, String>() {
			@Override
			public String transform(GraphNode graphNode) {
				return graphNode.getId();
			}
		});
		this.addVertexData(KEY_POSITION_PROPERTY, "position", "", new Transformer<GraphNode, String>() {
			@Override
			public String transform(GraphNode graphNode) {
				String pos = graphNode.getPosition().getX() + ":" + graphNode.getPosition().getY();
				return pos;
			}
		});
		this.addVertexData(KEY_DATA_MODEL_BASE64_PROPERTY, "Base64 encoded individual data model", "", new Transformer<GraphNode, String>() {
			@Override
			public String transform(GraphNode graphNode) {
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
	}
	
}
