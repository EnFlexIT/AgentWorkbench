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
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ComponentTypeSettings implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 2456632300628196922L;
	
	/** The domain in which this instance represents this component type */
	private String domain;
	/** The agent class representing this component type */
	private String agentClass;
	/** The GraphElementPrototype class representing this component type */
	private String graphPrototype;
	/** The adapter class that can extend functionalities for a NetworkComponent  */
	private String adapterClass;
	/** The image icon which will be displayed on the component graph edges. */
	private float edgeWidth = 2;
	/** The image icon which will be displayed on the component graph edges. */
	private String edgeImage = null;
	/** The color which will be displayed on the component graph edges. */
	private String color = null;
	/** Indicates if a label is visible or not. */
	private boolean showLabel = true;
	
	
	/**
	 * Default Constructor
	 */
	public ComponentTypeSettings(){
		super();
	}

	/**
	 * Returns a copy of the current instance.
	 * @return the copy
	 */
	public ComponentTypeSettings getCopy() {
		
		ComponentTypeSettings copy = new ComponentTypeSettings();
		if (domain!=null) {
			copy.setDomain(new String (domain));	
		}
		if (agentClass!=null) {
			copy.setAgentClass(new String(agentClass));	
		}
		if (graphPrototype!=null) {
			copy.setGraphPrototype(new String(graphPrototype));	
		}
		if (adapterClass!=null) {
			copy.setAdapterClass(new String(adapterClass));	
		}
		if (color!=null) {
			copy.setColor(new String(color));	
		}
		if (edgeImage!=null) {
			copy.setEdgeImage(new String(edgeImage));	
		}
		copy.setEdgeWidth(edgeWidth);
		copy.setShowLabel(showLabel);
		return copy;
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
		if (domain == null) {
			domain = GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME;
		}
		if (domain.equals("")) {
			domain = GeneralGraphSettings4MAS.DEFAULT_DOMAIN_SETTINGS_NAME;
		}
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
	 * Gets the graph prototype.
	 * @return the graphPrototype
	 */
	public String getGraphPrototype() {
		return graphPrototype;
	}
	/**
	 * Sets the graph prototype.
	 * @param graphPrototype the graphPrototype to set
	 */
	public void setGraphPrototype(String graphPrototype) {
		this.graphPrototype = graphPrototype;
	}
	
	/**
	 * Sets the adapter class.
	 * @param adapterClass the new adapter class
	 */
	public void setAdapterClass(String adapterClass) {
		this.adapterClass = adapterClass;
	}
	/**
	 * Returns the adapter class for the current NetworkComponent.
	 * @return the adapter class
	 */
	public String getAdapterClass() {
		return adapterClass;
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
	 * Sets the show label.
	 * @param showLabel the new show label
	 */
	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
	}
	/**
	 * Checks if is show label.
	 * @return true, if the label has to be displayed
	 */
	public boolean isShowLabel() {
		return showLabel;
	}
	
	
}
