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
package org.awb.env.networkModel.settings;

import java.io.Serializable;

import org.awb.env.networkModel.prototypes.AbstractGraphElementPrototype;

import de.enflexit.common.StringHelper;

/**
 * This class stores the component type settings for a network component type 
 * 
 * @see AbstractGraphElementPrototype
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ComponentTypeSettings implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 2456632300628196922L;
	
	private String domain;
	private String agentClass;
	private String graphPrototype;
	private String adapterClass;
	private float edgeWidth = 2;
	private String edgeImage = null;
	private String color = null;
	private boolean showLabel = true;
	
	
	/**
	 * Default Constructor
	 */
	public ComponentTypeSettings(){ }

	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compareObject) {
		
		if (compareObject==null) return false;
		if (! (compareObject instanceof ComponentTypeSettings)) return false;
		
		ComponentTypeSettings cts2 = (ComponentTypeSettings) compareObject;
		
		boolean isEqual = true;
		if (isEqual==true) isEqual = StringHelper.isEqualString(this.getDomain(), cts2.getDomain());
		if (isEqual==true) isEqual = StringHelper.isEqualString(this.getAgentClass(), cts2.getAgentClass());
		if (isEqual==true) isEqual = StringHelper.isEqualString(this.getGraphPrototype(), cts2.getGraphPrototype());
		if (isEqual==true) isEqual = StringHelper.isEqualString(this.getAdapterClass(), cts2.getAdapterClass());
		if (isEqual==true) isEqual = (this.getEdgeWidth()==cts2.getEdgeWidth());
		if (isEqual==true) isEqual = StringHelper.isEqualString(this.getEdgeImage(), cts2.getEdgeImage());
		if (isEqual==true) isEqual = StringHelper.isEqualString(this.getColor(), cts2.getColor());
		if (isEqual==true) isEqual = (this.isShowLabel()==cts2.isShowLabel());
		return isEqual;
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
		// --- Consider changes due to the bundle refactoring -------
		if (graphPrototype!=null && graphPrototype.startsWith("agentgui.envModel.graph.prototypes.")==true) {
			graphPrototype = graphPrototype.replace("agentgui.envModel.graph.prototypes.", "org.awb.env.networkModel.prototypes.");
		}
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
	 * Returns the adapter class for the current NetworkComponent.
	 * @return the adapter class
	 */
	public String getAdapterClass() {
		// --- Consider changes due to the bundle refactoring -------
		if (adapterClass!=null && adapterClass.startsWith("agentgui.envModel.graph.networkModel.")==true) {
			adapterClass = adapterClass.replace("agentgui.envModel.graph.networkModel.", "org.awb.env.networkModel.adapter.");
		}
		return adapterClass;
	}
	/**
	 * Sets the adapter class.
	 * @param adapterClass the new adapter class
	 */
	public void setAdapterClass(String adapterClass) {
		this.adapterClass = adapterClass;
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
	 * Sets the path to the component edge image icon.
	 * @param edgeImage the new edge image
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
