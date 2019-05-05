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
package org.awb.env.networkModel;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

/**
 * This class represents a graph edge in an environment model of the type graph / network.
 * 
 * @see GraphNode
 * @see GraphElement
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 */
public class GraphEdge extends GraphElement {

	private static final long serialVersionUID = 1043215558300713661L;

	private static final String EDGE_SHAPE_CONFIGURATION_SEPERATOR = ";";
	
	private String componentType;
	private GraphEdgeShapeConfiguration<? extends Shape> edgeShapeConfiguration;
	private TreeMap<String, GraphEdgeShapeConfiguration<? extends Shape>> edgeShapeConfigurationTreeMap;
	
	
	/**
	 * Constructor.
	 * 
	 * @param id The id of this GraphEdge
	 * @param componentType The component type identifier for this GraphEdge
	 */
	public GraphEdge(String id, String componentType) {
		super();
		this.id = id;
		this.componentType = componentType;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.GraphElement#getCopy()
	 */
	@Override
	public GraphEdge getCopy() {
		GraphEdge edgceCopy = new GraphEdge(this.id, this.componentType);
		if (this.edgeShapeConfiguration!=null) edgceCopy.setEdgeShapeConfiguration(this.edgeShapeConfiguration.getCopy());
		if (this.graphElementLayout!=null)     edgceCopy.setGraphElementLayout(this.graphElementLayout.getCopy(edgceCopy));	
		return edgceCopy;
	}

	/**
	 * Gets the component type.
	 * @return the componentType
	 */
	public String getComponentType() {
		return componentType;
	}
	/**
	 * Sets the component type.
	 * @param componentType the componentType to set
	 */
	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	
	/**
	 * Returns the edge shape configuration for the current edge shape.
	 * @return the edge shape configuration
	 */
	public GraphEdgeShapeConfiguration<? extends Shape> getEdgeShapeConfiguration() {
		return edgeShapeConfiguration;
	}
	/**
	 * Sets the edge shape configuration for the corresponding edge shape.
	 * @param edgeShapeConfiguration the new edge shape configuration
	 */
	public void setEdgeShapeConfiguration(GraphEdgeShapeConfiguration<?> edgeShapeConfiguration) {
		this.edgeShapeConfiguration = edgeShapeConfiguration;
	}

	/**
	 * Returns the edge shape configuration tree map that distinguishes between different layouts.
	 * @return the edge configuration tree map
	 */
	public TreeMap<String, GraphEdgeShapeConfiguration<? extends Shape>> getEdgeShapeConfigurationTreeMap() {
		if (edgeShapeConfigurationTreeMap==null) {
			edgeShapeConfigurationTreeMap = new TreeMap<>();
		}
		return edgeShapeConfigurationTreeMap;
	}
	/**
	 * Sets the edge shape configuration tree map.
	 * @param edgeShapeConfigurationTreeMap the edge configuration tree map
	 */
	public void setEdgeShapeConfigurationTreeMap(TreeMap<String, GraphEdgeShapeConfiguration<? extends Shape>> edgeConfigurationTreeMap) {
		this.edgeShapeConfigurationTreeMap = edgeConfigurationTreeMap;
	}
	
	/**
	 * Gets the edge shape configuration tree map as string.
	 *
	 * @param allowedLayoutIDs the allowed layout I ds
	 * @return the edge configuration tree map as string
	 */
	public String getEdgeShapeConfigurationTreeMapAsString(HashSet<String> allowedLayoutIDs) {
		
		String config = null;
		if (this.getEdgeShapeConfigurationTreeMap().size()==0) {
			// --- Set to null to reduce RAM load ---------
			this.setEdgeShapeConfigurationTreeMap(null);
			
		} else {
			// --- Convert to String ----------------------
			List<String> keys = new ArrayList<>(this.getEdgeShapeConfigurationTreeMap().keySet()); 
			Collections.sort(keys);
			for (int i = 0; i < keys.size(); i++) {
				String layoutID = keys.get(i);
				if (allowedLayoutIDs.contains(layoutID)==false) continue;
				
				GraphEdgeShapeConfiguration<? extends Shape> shapeConfig = this.getEdgeShapeConfigurationTreeMap().get(layoutID);
				if (shapeConfig!=null) {
					String singleConfig = layoutID + ":=" + shapeConfig.getConfigurationAsString();
					if (config==null) {
						config = singleConfig;
					} else {
						config = config + EDGE_SHAPE_CONFIGURATION_SEPERATOR  + singleConfig;
					}
				}
			}
			
		}
		return config;
	}
	/**
	 * Sets the edge shape configuration tree map from the specified string.
	 * @param treeMapAsString the new edge configuration tree map from string
	 */
	public void setEdgeShapeConfigurationTreeMapFromString(String treeMapAsString) {
		
		if (treeMapAsString==null || treeMapAsString.isEmpty()) return;

		String[] layoutShapeConfigurations = treeMapAsString.split(EDGE_SHAPE_CONFIGURATION_SEPERATOR);
		for (int i = 0; i < layoutShapeConfigurations.length; i++) {
			
			String[] layoutConfigurationPair = layoutShapeConfigurations[i].split(":=");
			if (layoutConfigurationPair.length==2) {
				String layoutID = layoutConfigurationPair[0];
				GraphEdgeShapeConfiguration<? extends Shape> shapeConfig = GraphEdgeShapeConfiguration.getGraphEdgeShapeConfiguration(layoutConfigurationPair[1]);
				if (layoutID!=null && layoutID.isEmpty()==false && shapeConfig!=null) {
					this.getEdgeShapeConfigurationTreeMap().put(layoutID, shapeConfig);
				}
			}
		}
	}
	
}
