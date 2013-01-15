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

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlRootElement;

import agentgui.envModel.graph.controller.GraphEnvironmentController;
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
	
	/** The Constant PREFIX_NETWORK_COMPONENT. */
	public final static String PREFIX_NETWORK_COMPONENT = "n"; 
	
	/** Default name for the first DomainSettings. */
	public static final String DEFAULT_DOMAIN_SETTINGS_NAME = "Default Domain";
	/** Default width for edges. */
	public static final float DEFAULT_EDGE_WIDTH = 2;
	/** Default color to be used for edges in the graph */
	public static final Color DEFAULT_EDGE_COLOR = Color.BLACK;
	/** Default color to be used for edges in the graph when highlighted/picked. */
	public static final Color DEFAULT_EDGE_PICKED_COLOR = Color.CYAN; 
	/** Default vertex size of the nodes. */
	public static final Integer DEFAULT_VERTEX_SIZE = 10;
	/** Default color to be used for Vertices in the graph */
	public static final Color DEFAULT_VERTEX_COLOR = Color.RED; 
	/** Default color to be used for Vertices in the graph when highlighted/picked. */
	public static final Color DEFAULT_VERTEX_PICKED_COLOR = Color.YELLOW; 
	/** Default raster size for guide grid. */
	public static final Integer DEFAULT_RASTER_SIZE = 5; 
	 
	public static final String NETWORK_COMPONENT_TYPE_4_CLUSTER = "C L U S T E R !";
	
	/** Default shapes for nodes */
	public static final String SHAPE_ELLIPSE = "Ellipse";
	public static final String SHAPE_RECTANGLE = "Rectangle";
	public static final String SHAPE_ROUND_RECTANGLE = "Round rectangle";
	public static final String SHAPE_REGULAR_POLYGON = "Regular polygon";
	public static final String SHAPE_REGULAR_STAR = "Regular star";
	public static final String SHAPE_IMAGE_SHAPE = "Image shape";
	
	/** Default shapes for a central cluster node */
	public static final String SHAPE_DEFAULT_4_CLUSTER = SHAPE_RECTANGLE;
	
	/** The prefix name for the NetworkComponentAdapter of a GraphNode */
	public static final String GRAPH_NODE_NETWORK_COMPONENT_ADAPTER_PREFIX = "GraphNodeAdapter_";
	
	
	/** The component type settings used in the {@link GraphEnvironmentController} */
	private HashMap<String, ComponentTypeSettings> currentCTS = null;
	/** The available domains used in the {@link GraphEnvironmentController} */
	private HashMap<String, DomainSettings> currentDomainSettings = null;
	
	/** The snap2 grid. */
	private boolean snap2Grid = true;
	/** The snap raster. */
	private double snapRaster = DEFAULT_RASTER_SIZE;

	
	/**
	 * Default constructor
	 */
	public GeneralGraphSettings4MAS() {
	}
	
	/**
	 * Creates a clone of the current instance
	 */
	@Override
	public GeneralGraphSettings4MAS clone() {
		try {
			return (GeneralGraphSettings4MAS) super.clone();
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
		
		HashMap<String, ComponentTypeSettings> copyCTS = this.copyComponentTypeSettings();
		HashMap<String, DomainSettings> copyDS = this.copyDomainSettings();
		
		GeneralGraphSettings4MAS copy = new GeneralGraphSettings4MAS();
		copy.setCurrentCTS(copyCTS);
		copy.setDomainSettings(copyDS);
		return copy;
	}
	
	/**
	 * Copies the component type settings.
	 * @return the hash map
	 */
	private HashMap <String, ComponentTypeSettings> copyComponentTypeSettings() {
		
		HashMap<String, ComponentTypeSettings> copyCtsHash = new HashMap<String, ComponentTypeSettings>();
		
		Iterator<String> ctsIt = this.getCurrentCTS().keySet().iterator();
		while (ctsIt.hasNext()) {
			String element = ctsIt.next();
			ComponentTypeSettings cts = this.currentCTS.get(element); 
			copyCtsHash.put(element, cts.getCopy());
		}
		return copyCtsHash;
	}
	/**
	 * Copies the component type settings.
	 * @return the hash map
	 */
	private HashMap <String, DomainSettings> copyDomainSettings() {
		
		HashMap<String, DomainSettings> copyDomainHash = new HashMap<String, DomainSettings>();
		
		Iterator<String> ctsIt = this.getDomainSettings().keySet().iterator();
		while (ctsIt.hasNext()) {
			String element = ctsIt.next();
			DomainSettings ds = this.currentDomainSettings.get(element); 
			copyDomainHash.put(element, ds.getCopy());
		}
		return copyDomainHash;
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
		if (this.currentDomainSettings.size()==0) {
			DomainSettings ds = new DomainSettings();
			currentDomainSettings.put(DEFAULT_DOMAIN_SETTINGS_NAME, ds);
		}
		if (this.currentDomainSettings.containsKey(DEFAULT_DOMAIN_SETTINGS_NAME)==false) {
			DomainSettings ds = new DomainSettings();
			currentDomainSettings.put(DEFAULT_DOMAIN_SETTINGS_NAME, ds);
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

	/**
	 * Sets the snap2 grid.
	 * @param snap2Grid the new snap2 grid
	 */
	public void setSnap2Grid(boolean snap2Grid) {
		this.snap2Grid = snap2Grid;
	}
	/**
	 * Checks if is snap2 grid.
	 * @return true, if is snap2grid is true
	 */
	public boolean isSnap2Grid() {
		return snap2Grid;
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
