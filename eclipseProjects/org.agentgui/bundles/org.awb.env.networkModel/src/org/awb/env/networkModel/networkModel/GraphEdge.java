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
package org.awb.env.networkModel.networkModel;

/**
 * This class represents a graph edge in an environment model of the type graph / network.
 * 
 * @see GraphNode
 * @see GraphElement
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 */
public class GraphEdge extends GraphElement {

	private static final long serialVersionUID = 1043215558300713661L;

	private String componentType;
	private GraphEdgeShapeConfiguration<?> edgeShapeConfiguration;
	
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
	 * @see org.awb.env.networkModel.networkModel.GraphElement#getCopy()
	 */
	@Override
	public GraphEdge getCopy() {
		
		GraphEdge edgceCopy = new GraphEdge(this.getId(), this.getComponentType());
		edgceCopy.setEdgeShapeConfiguration(this.getEdgeShapeConfiguration().getCopy());
		
		if (this.graphElementLayout!=null) {
			edgceCopy.setGraphElementLayout(this.graphElementLayout.getCopy(edgceCopy));	
		}
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
	public GraphEdgeShapeConfiguration<?> getEdgeShapeConfiguration() {
		return edgeShapeConfiguration;
	}
	/**
	 * Sets the edge shape configuration for the corresponding edge shape.
	 * @param edgeShapeConfiguration the new edge shape configuration
	 */
	public void setEdgeShapeConfiguration(GraphEdgeShapeConfiguration<?> edgeShapeConfiguration) {
		this.edgeShapeConfiguration = edgeShapeConfiguration;
	}
	
}
