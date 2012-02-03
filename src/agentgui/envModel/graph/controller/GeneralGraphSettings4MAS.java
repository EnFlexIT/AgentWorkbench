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
package agentgui.envModel.graph.controller;

import java.awt.Color;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;

import agentgui.envModel.graph.networkModel.ComponentTypeSettings;
import agentgui.envModel.graph.networkModel.DomainSettings;
import jade.util.leap.Serializable;

/**
 * A custom user object encapsulating the required objects which can be placed in the Project object. 
 * You can add more attributes in this class if required, but be careful to cast and use the user object properly.
 * 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 */
@XmlRootElement
public class GeneralGraphSettings4MAS implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 7425147528482747232L;
	
	/** Default width for edges. */
	public static float DEFAULT_EDGE_WIDTH = 2;
	/** Default raster size for guide grid. */
	public static Integer DEFAULT_RASTER_SIZE = 20; 
	/** Default vertex size of the nodes. */
	public static Integer DEFAULT_VERTEX_SIZE = 10; 
	/** Default color to be used for edges in the graph when highlighted/picked. */
	public static Color DEFAULT_EDGE_PICKED_COLOR = Color.CYAN; 
	/** Default color to be used for edges in the graph */
	public static Color DEFAULT_EDGE_COLOR = Color.BLACK; 
	/** Default color to be used for Vertices in the graph when highlighted/picked. */
	public static Color DEFAULT_VERTEX_PICKED_COLOR = Color.YELLOW; 
	/** Default color to be used for Vertices in the graph */
	public static Color DEFAULT_VERTEX_COLOR = Color.RED; 
	
	/** The component type settings used in the {@link GraphEnvironmentController} */
	private HashMap<String, ComponentTypeSettings> currentCTS = null;
	/** The available domains used in the {@link GraphEnvironmentController} */
	private HashMap<String, DomainSettings> currentDomainSettings = null;

	
	/**
	 * Default constructor
	 */
	public GeneralGraphSettings4MAS() {
	}
	
	/**
	 * Creates a clone of the current instance
	 */
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Gets a copy from the current instance.
	 * @return the copy
	 */
	public GeneralGraphSettings4MAS getCopy() {
		
		HashMap<String, ComponentTypeSettings> copyCTS = new HashMap<String, ComponentTypeSettings>(this.currentCTS);
		HashMap<String, DomainSettings> copyDS = new HashMap<String, DomainSettings>(this.currentDomainSettings);
		
		GeneralGraphSettings4MAS copy = new GeneralGraphSettings4MAS();
		copy.setCurrentCTS(copyCTS);
		copy.setDomainSettings(copyDS);
		return copy;
	}
	
	/**
	 * Get the component type settings HashMap 
	 * @return the currentCTS
	 */
	public HashMap<String, ComponentTypeSettings> getCurrentCTS() {
		if (currentCTS==null) {
			currentCTS = new HashMap<String, ComponentTypeSettings>();
		}
		return currentCTS;
	}
	/**
	 * Set the component type settings HashMap
	 * @param currentCTS the currentCTS to set
	 */
	public void setCurrentCTS(HashMap<String, ComponentTypeSettings> currentCTS) {
		this.currentCTS = currentCTS;
	}

	/**
	 * Gets the domain settings.
	 * @return the currentDomainSettings
	 */
	public HashMap<String, DomainSettings> getDomainSettings() {
		if (currentDomainSettings==null) {
			currentDomainSettings = new HashMap<String, DomainSettings>();
		}
		return currentDomainSettings;
	}
	/**
	 * Sets the domain settings.
	 * @param domainSettings the domain settings
	 */
	public void setDomainSettings(HashMap<String, DomainSettings> domainSettings) {
		this.currentDomainSettings = domainSettings;
	}
	
}
