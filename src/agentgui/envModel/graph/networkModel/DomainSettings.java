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

import java.io.Serializable;

import agentgui.envModel.graph.controller.GeneralGraphSettings4MAS;

/**
 * The Class DomainSettings.
 */
public class DomainSettings implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 469880829860930598L;
	
	private String ontologyClass = null;
	private Integer vertexSize = GeneralGraphSettings4MAS.DEFAULT_VERTEX_SIZE; 
	private String vertexColor = String.valueOf(GeneralGraphSettings4MAS.DEFAULT_VERTEX_COLOR.getRGB());
	private String vertexColorPicked = String.valueOf(GeneralGraphSettings4MAS.DEFAULT_VERTEX_PICKED_COLOR.getRGB());
	private boolean showLabel = true;
	private String clusterShape = GeneralGraphSettings4MAS.SHAPE_DEFAULT_4_CLUSTER; 
	
	/**
	 * Instantiates a new domain settings.
	 */
	public DomainSettings() {
	}
	
	/**
	 * Gets the ontology class.
	 * @return the ontologyClass
	 */
	public String getOntologyClass() {
		return ontologyClass;
	}
	/**
	 * Sets the ontology class.
	 * @param ontologyClass the ontologyClass to set
	 */
	public void setOntologyClass(String ontologyClass) {
		this.ontologyClass = ontologyClass;
	}

	/**
	 * Gets the vertex size.
	 * @return the vertexSize
	 */
	public Integer getVertexSize() {
		return vertexSize;
	}
	/**
	 * Sets the vertex size.
	 * @param vertexSize the vertexSize to set
	 */
	public void setVertexSize(Integer vertexSize) {
		this.vertexSize = vertexSize;
	}

	/**
	 * Gets the vertex color.
	 * @return the vertexColor
	 */
	public String getVertexColor() {
		return vertexColor;
	}
	/**
	 * Sets the vertex color.
	 * @param vertexColor the vertexColor to set
	 */
	public void setVertexColor(String vertexColor) {
		this.vertexColor = vertexColor;
	}

	/**
	 * Gets the vertex color if vertex is picked.
	 * @return the vertexColor
	 */
	public String getVertexColorPicked() {
		return vertexColorPicked;
	}
	/**
	 * Sets the vertex color for a picked vertex.
	 * @param vertexColor the vertexColor to set
	 */
	public void setVertexColorPicked(String vertexColorPicked) {
		this.vertexColorPicked = vertexColorPicked;
	}
	
	/**
	 * Checks if is show label.
	 * @return the showLabel
	 */
	public boolean isShowLabel() {
		return showLabel;
	}
	/**
	 * Sets the show label.
	 * @param showLabel the showLabel to set
	 */
	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
	}

	/**
	 * Gets the cluster shape.
	 * @return the cluster shape
	 */
	public String getClusterShape() {
		return clusterShape;
	}
	/**
	 * Sets the cluster shape.
	 * @param clusterShape the new cluster shape
	 */
	public void setClusterShape(String clusterShape) {
		this.clusterShape = clusterShape;
	}
	
}
