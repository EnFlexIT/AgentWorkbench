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

import agentgui.envModel.graph.prototypes.GraphElementPrototype;

/**
 * This class stores the component type settings for a network component type 
 * 
 * @see GraphElementPrototype
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 * @author Satyadeep 
 *
 */
public class ComponentTypeSettings implements Serializable {
	
	private static final long serialVersionUID = 2456632300628196922L;
	
	/** The domain in which this instance represents this component type */
	private String domain;
	/** The agent class representing this component type */
	private String agentClass;
	/** The GraphElementPrototype class representing this component type */
	private String graphPrototype;
	/** The image icon which will be displayed on the component graph edges. */
	private float edgeWidth = 2;
	/** The image icon which will be displayed on the component graph edges. */
	private String edgeImage = null;
	/** The color which will be displayed on the component graph edges. */
	private String color = null;
	/** Indicates if a label is visible or not. */
	private boolean showLabel = true;
	
	/** The vertex size, used only for the component type settings of the key "node". */
	private Integer vertexSize = null;
	/** The vertex size, used only for the component type settings of the key "node". */
	private boolean snap2Grid= false;
	/** The vertex size, used only for the component type settings of the key "node". */
	private double snapRaster = 0.0;

	/**
	 * Default Constructor
	 */
	public ComponentTypeSettings(){
		super();
	}
	/**
	 * Constructor
	 * @param agentClass The agent class name
	 * @param graphPrototype The GraphElementPrototype class name
	 * @param edgeImage  The path to the image icon to be used for the component
	 * @param color The color to be used for the component edges in RGB integer representation.
	 */
	public ComponentTypeSettings(String agentClass, String graphPrototype, String edgeImage, String color) {
		super();
		this.agentClass = agentClass;
		this.graphPrototype = graphPrototype;
		this.edgeImage = edgeImage;
		this.color = color;
	}
	
	/**
	 * Sets the domain.
	 * @param domain the new domain
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}
	/**
	 * Gets the domain.
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}
	
	/**
	 * Returns the agent class
	 * @return the agentClass
	 */
	public String getAgentClass() {
		return agentClass;
	}
	/**
	 * 
	 * @param agentClass the agentClass to set
	 */
	public void setAgentClass(String agentClass) {
		this.agentClass = agentClass;
	}
	/**
	 * @return the graphPrototype
	 */
	public String getGraphPrototype() {
		return graphPrototype;
	}
	/**
	 * @param graphPrototype the graphPrototype to set
	 */
	public void setGraphPrototype(String graphPrototype) {
		this.graphPrototype = graphPrototype;
	}
	
	/**
	 * Gets the edge width.
	 * @return the edge width
	 */
	public float getEdgeWidth() {
		return edgeWidth;
	}
	/**
	 * Sets the edge width.
	 * @param edgeWidth the new edge width
	 */
	public void setEdgeWidth(float edgeWidth) {
		this.edgeWidth = edgeWidth;
	}
	
	/**
	 * Returns the path to the edge image icon
	 * @return the edgeImage
	 */
	public String getEdgeImage(){
		return edgeImage;
	}	
	/**
	 * Sets the path to the component edge image icon
	 */
	public void setEdgeImage(String edgeImage){
		this.edgeImage = edgeImage;
	}
	
	/**
	 * Returns the color of the component edges.
	 * @return the color as a string in RGB integer representation.
	 */
	public String getColor(){
		return color;
	}	
	/**
	 * Sets the color of the component edges.
	 * @param color as string in RGB integer representation.
	 */
	public void setColor(String color){
		this.color = color;
	}
	
	/**
	 * @param showLable the showLable to set
	 */
	public void setShowLabel(boolean showLable) {
		this.showLabel = showLable;
	}
	/**
	 * @return the showLable
	 */
	public boolean isShowLabel() {
		return showLabel;
	}
	
	/**
	 * Returns the vertex size of the node.
	 * Used only for the component type settings of the key "node".
	 * @return the vertex size as String. 
	 */
	public Integer getVertexSize(){
		return this.vertexSize;
	}
	/**
	 * Sets the vertex size of the node.
	 * Used only for the component type settings of the key "node".
	 * @param vertexSize
	 */
	public void setVertexSize(Integer vertexSize){
		this.vertexSize = vertexSize;
	}
	
	/**
	 * Checks if is snap2 grid.
	 * @return true, if is snap2 grid
	 */
	public boolean isSnap2Grid() {
		return snap2Grid;
	}
	/**
	 * Sets the snap2 grid.
	 * @param snap2Grid the new snap2 grid
	 */
	public void setSnap2Grid(boolean snap2Grid) {
		this.snap2Grid = snap2Grid;
	}

	/**
	 * Sets the snap raster.
	 * @param snapRaster the new snap raster
	 */
	public void setSnapRaster(double snapRaster) {
		this.snapRaster = snapRaster;
	}
	/**
	 * Gets the snap raster.
	 * @return the snap raster
	 */
	public double getSnapRaster() {
		return snapRaster;
	}
	

}
