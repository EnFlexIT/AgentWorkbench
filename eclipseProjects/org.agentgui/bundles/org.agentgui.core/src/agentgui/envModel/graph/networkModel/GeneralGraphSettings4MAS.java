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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import agentgui.envModel.graph.controller.CustomToolbarComponentDescription;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import jade.util.leap.Serializable;

/**
 * A custom user object encapsulating the required objects which can be placed in the Project object. 
 * You can add more attributes in this class if required, but be careful to cast and use the user object properly.
 * 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
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
	
	/** Basic color for image icons. If using dynamic colorization, this color will be replaced */
	public static final Color IMAGE_ICON_BASIC_COLOR = new Color(179, 179, 179);
	
	/** Default shapes for nodes */
	public static final String SHAPE_ELLIPSE = "Ellipse";
	public static final String SHAPE_RECTANGLE = "Rectangle";
	public static final String SHAPE_ROUND_RECTANGLE = "Round rectangle";
	public static final String SHAPE_REGULAR_POLYGON = "Regular polygon";
	public static final String SHAPE_REGULAR_STAR = "Regular star";
	public static final String SHAPE_IMAGE_SHAPE = "Image shape";
	
	/** Default shapes for a central cluster node */
	public static final String SHAPE_DEFAULT_4_CLUSTER = SHAPE_RECTANGLE;
	
	/** The list of possible edge shapes */
	public static enum EdgeShape {
		BentLine,
		Box,
		CubicCurve,
		Line,
		Polyline,
		Loop,
		Orthogonal,
		QuadCurve,
		SimpleLoop,
		Wedge
	}
	
	/** The prefix name for the NetworkComponentAdapter of a GraphNode */
	public static final String GRAPH_NODE_NETWORK_COMPONENT_ADAPTER_PREFIX = "GraphNodeAdapter_";
	
	
	/** The available domains used in the {@link GraphEnvironmentController} */
	private TreeMap<String, DomainSettings> currentDomainSettings;
	/** The component type settings used in the {@link GraphEnvironmentController} */
	private TreeMap<String, ComponentTypeSettings> currentCTS;
	
	private boolean snap2Grid = true;
	private double snapRaster = DEFAULT_RASTER_SIZE;
	private EdgeShape edgeShape = EdgeShape.Line;
	
	/** 
	 * The current vector of custom BasicGraphGuiCustomJBottonDescription. 
	 **/
	@XmlTransient
	private Vector<CustomToolbarComponentDescription> customToolbarComponentDescriptions;
	
	
	/**
	 * Default constructor
	 */
	public GeneralGraphSettings4MAS() {
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compareObject) {
		
		if (compareObject==null || (compareObject instanceof GeneralGraphSettings4MAS)==false) return false;

		boolean isEqual = true;
		GeneralGraphSettings4MAS ggsToCompare = (GeneralGraphSettings4MAS) compareObject;
		if (compareObject!=this) {
			// --- Compare Domain settings ----------------
			if (isEqual==true) {
				isEqual = this.hasEqualDomainSettings(ggsToCompare);
			}
			// --- Compare ComponentTypeSettings ----------
			if (isEqual==true) {
				isEqual = this.hasEqualComponentTypeSettings(ggsToCompare);
			}
			// --- Compare 'snap2Grid' --------------------
			if (isEqual==true) {
				isEqual = (ggsToCompare.isSnap2Grid()==this.isSnap2Grid());
			}
			// --- Compare 'snapRaster' -------------------
			if (isEqual==true) {
				isEqual = (ggsToCompare.getSnapRaster()==this.getSnapRaster());
			}
			// --- Compare 'edgeShape' --------------------
			if (isEqual==true) {
				isEqual = (ggsToCompare.getEdgeShape()==this.getEdgeShape());
			}
		}
		return isEqual;
	}
	
	/**
	 * Checks for equal domain settings.
	 *
	 * @param ggsToCompare the GeneralGraphSettings4MAS to compare
	 * @return true, if successful
	 */
	public boolean hasEqualDomainSettings(GeneralGraphSettings4MAS ggsToCompare) {
		
		boolean isEqual = true;
		TreeMap<String, DomainSettings> dsTreeMapToCompare = ggsToCompare.getDomainSettings();
		isEqual = (dsTreeMapToCompare.size()==this.getDomainSettings().size());
		if (isEqual==true) {
			// --- Compare each element in the TreeMap ----
			Vector<String> keyVector = new Vector<String>(dsTreeMapToCompare.keySet());
			for (int i = 0; i < keyVector.size(); i++) {
				String key = keyVector.get(i);
				DomainSettings ds2Comp = dsTreeMapToCompare.get(key);
				DomainSettings dsLocal = this.getDomainSettings().get(key);
				if (dsLocal==null) return false;
				if (isEqualDomainSetting(ds2Comp, dsLocal)==false) return false;
			}
		}
		return isEqual;
	}
	/**
	 * Checks if the both {@link DomainSettings} are equal.
	 * 
	 * @param ds1 the first  DomainSetting
	 * @param ds2 the second DomainSetting
	 * @return true, if is equal DomainSetting
	 */
	public static boolean isEqualDomainSetting(DomainSettings ds1, DomainSettings ds2) {
		
		boolean isEqual = true;
		if (isEqual==true) isEqual = (ds1.isShowLabel()==ds2.isShowLabel());
		if (isEqual==true) isEqual = (ds1.getVertexSize()==ds2.getVertexSize());
		if (isEqual==true) isEqual = isEqualString(ds1.getVertexColor(), ds2.getVertexColor());
		if (isEqual==true) isEqual = isEqualString(ds1.getVertexColorPicked(), ds2.getVertexColorPicked());
		if (isEqual==true) isEqual = isEqualString(ds1.getAdapterClass(), ds2.getAdapterClass());
		if (isEqual==true) isEqual = isEqualString(ds1.getClusterAgent(), ds2.getClusterAgent());
		if (isEqual==true) isEqual = isEqualString(ds1.getClusterShape(), ds2.getClusterShape());
		return isEqual;
	}
	
	/**
	 * Checks for equal component type settings.
	 *
	 * @param ggsToCompare the GeneralGraphSettings4MAS to compare
	 * @return true, if successful
	 */
	public boolean hasEqualComponentTypeSettings(GeneralGraphSettings4MAS ggsToCompare) {
		
		boolean isEqual = true;
		TreeMap<String, ComponentTypeSettings> ctsTreeMapToCompare = ggsToCompare.getCurrentCTS();
		isEqual = (ctsTreeMapToCompare.size()==this.getCurrentCTS().size());
		if (isEqual==true) {
			// --- Compare each element in the TreeMap ----
			Vector<String> keyVector = new Vector<String>(ctsTreeMapToCompare.keySet());
			for (int i = 0; i < keyVector.size(); i++) {
				String key = keyVector.get(i);
				ComponentTypeSettings cts2Comp = ctsTreeMapToCompare.get(key);
				ComponentTypeSettings ctsLocal = this.getCurrentCTS().get(key);
				if (ctsLocal==null) return false;
				if (isEqualComponentTypeSettings(cts2Comp, ctsLocal)==false) return false;
			}
		}
		return isEqual;
	}
	/**
	 * Checks if the both {@link ComponentTypeSettings} are equal.
	 * 
	 * @param cts1 the first ComponentTypeSettings
	 * @param cts2 the second ComponentTypeSettings
	 * @return true, if is equal ComponentTypeSettings
	 */
	public static boolean isEqualComponentTypeSettings(ComponentTypeSettings cts1, ComponentTypeSettings cts2) {
		
		boolean isEqual = true;
		if (isEqual==true) isEqual = isEqualString(cts1.getDomain(), cts2.getDomain());
		if (isEqual==true) isEqual = isEqualString(cts1.getAgentClass(), cts2.getAgentClass());
		if (isEqual==true) isEqual = isEqualString(cts1.getGraphPrototype(), cts2.getGraphPrototype());
		if (isEqual==true) isEqual = isEqualString(cts1.getAdapterClass(), cts2.getAdapterClass());
		if (isEqual==true) isEqual = (cts1.getEdgeWidth()==cts2.getEdgeWidth());
		if (isEqual==true) isEqual = isEqualString(cts1.getEdgeImage(), cts2.getEdgeImage());
		if (isEqual==true) isEqual = isEqualString(cts1.getColor(), cts2.getColor());
		if (isEqual==true) isEqual = (cts1.isShowLabel()==cts2.isShowLabel());
		return isEqual;
	}
	/**
	 * Checks if is equal string setting.
	 *
	 * @param string1 the string 1
	 * @param string2 the string 2
	 * @return true, if is equal string
	 */
	private static boolean isEqualString(String string1, String string2) {
		boolean isEqual = true;
		if (string1==null & string2==null) {
			isEqual = true;
		} else if (string1==null & string2!=null) {
			isEqual = false;
		} else if (string1!=null & string2==null) {
			isEqual = false;
		} else {
			isEqual = string1.equals(string2);
		}
		return isEqual;
	}
	
	/**
	 * Loads the specified file .
	 *
	 * @param file the file to load
	 * @return the general graph settings for the MAS
	 */
	public static GeneralGraphSettings4MAS load(File file) {
		
		GeneralGraphSettings4MAS ggs4MAS = null;
		try {
			
			if (file.exists()==false) GeneralGraphSettings4MAS.save(file, new GeneralGraphSettings4MAS());
			FileReader componentReader = new FileReader(file);
		    
			JAXBContext context = JAXBContext.newInstance(GeneralGraphSettings4MAS.class);
		    Unmarshaller unmarsh = context.createUnmarshaller();
		    ggs4MAS = (GeneralGraphSettings4MAS) unmarsh.unmarshal(componentReader);
		    
		    componentReader.close();
	
		} catch (JAXBException ex) {
		    ex.printStackTrace();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
		return ggs4MAS;
	}
	/**
	 * Saves the specified graph setting into the specified file.
	 * @param file the file to save
	 * @param ggs4MASToSave the actual GeneralGraphSettings4MAS
	 */
	public static void save(File file, GeneralGraphSettings4MAS ggs4MASToSave) {
		
		try {
			
			boolean writeFile = false;
			if (file.exists()==false) {
				writeFile = true;
			} else {
				// --- Compare for equal settings ---------
				GeneralGraphSettings4MAS ggs4MASToCompare = load(file);
				writeFile = (ggs4MASToSave.equals(ggs4MASToCompare)==false);
			}
			
			if (writeFile==true) {
				// --- Write GeneralGraphSettings4MAS ----- 
				FileWriter componentFileWriter = new FileWriter(file);
				
				JAXBContext context = JAXBContext.newInstance(GeneralGraphSettings4MAS.class);
				Marshaller marsh = context.createMarshaller();
				marsh.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				marsh.marshal(ggs4MASToSave, componentFileWriter);
				
				componentFileWriter.close();
			}
			
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (JAXBException e) {
		    e.printStackTrace();
		}
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
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
	 * Returns a deep copy of the current instance.
	 * @return the deep copy of the settings
	 */
	public GeneralGraphSettings4MAS getCopy() {
		GeneralGraphSettings4MAS copy = new GeneralGraphSettings4MAS();
		copy.setCurrentCTS(this.copyComponentTypeSettings());
		copy.setDomainSettings(this.copyDomainSettings());
		copy.setSnap2Grid(this.isSnap2Grid());
		copy.setSnapRaster(this.getSnapRaster());
		copy.setEdgeShape(this.getEdgeShape());
		copy.setCustomToolbarComponentDescriptions(this.copyCustomToolbarComponentDescription());
		return copy;
	}
	
	/**
	 * Copies the component type settings.
	 * @return the hash map
	 */
	private TreeMap <String, ComponentTypeSettings> copyComponentTypeSettings() {
		TreeMap<String, ComponentTypeSettings> copyCtsHash = new TreeMap<String, ComponentTypeSettings>();
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
	private TreeMap <String, DomainSettings> copyDomainSettings() {
		TreeMap<String, DomainSettings> copyDomainHash = new TreeMap<String, DomainSettings>();
		Iterator<String> ctsIt = this.getDomainSettings().keySet().iterator();
		while (ctsIt.hasNext()) {
			String element = ctsIt.next();
			DomainSettings ds = this.currentDomainSettings.get(element); 
			copyDomainHash.put(element, ds.getCopy());
		}
		return copyDomainHash;
	}
	/**
	 * Copy custom toolbar component descriptions.
	 * @return the vector
	 */
	private Vector<CustomToolbarComponentDescription> copyCustomToolbarComponentDescription() {
		Vector<CustomToolbarComponentDescription> copyCTCD = new Vector<>();
		for (CustomToolbarComponentDescription ctcd : this.getCustomToolbarComponentDescriptions()) {
			copyCTCD.add(ctcd.getCopy());
		}
		if (copyCTCD.size()==0) copyCTCD = null;
		return copyCTCD;
	}
	
	/**
	 * Get the component type settings HashMap 
	 * @return the currentCTS
	 */
	public TreeMap<String, ComponentTypeSettings> getCurrentCTS() {
		if (currentCTS==null) {
			currentCTS = new TreeMap<String, ComponentTypeSettings>();
		}
		return currentCTS;
	}
	/**
	 * Set the component type settings HashMap
	 * @param currentCTS the currentCTS to set
	 */
	public void setCurrentCTS(TreeMap<String, ComponentTypeSettings> currentCTS) {
		this.currentCTS = currentCTS;
	}

	/**
	 * Gets the domain settings.
	 * @return the currentDomainSettings
	 */
	public TreeMap<String, DomainSettings> getDomainSettings() {
		if (currentDomainSettings==null) {
			currentDomainSettings = new TreeMap<String, DomainSettings>();
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
	public void setDomainSettings(TreeMap<String, DomainSettings> domainSettings) {
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
	/**
	 * Sets the edge shape.
	 * @param edgeShape the new edge shape
	 */
	public void setEdgeShape(EdgeShape edgeShape) {
		this.edgeShape = edgeShape;
	}
	/**
	 * Gets the edge shape.
	 * @return the edge shape
	 */
	public EdgeShape getEdgeShape() {
		return edgeShape;
	}


	/**
	 * Returns the vector of custom {@link CustomToolbarComponentDescription}.
	 * @return the vector of custom {@link CustomToolbarComponentDescription}.
	 */
	@XmlTransient
	public Vector<CustomToolbarComponentDescription> getCustomToolbarComponentDescriptions() {
		if (customToolbarComponentDescriptions==null) {
			customToolbarComponentDescriptions = new Vector<CustomToolbarComponentDescription>();
		}
		return customToolbarComponentDescriptions;
	}
	/**
	 * Sets the vector of custom {@link CustomToolbarComponentDescription}.
	 * @param newCustomToolbarComponentDescriptions the new vector of custom CustomToolbarComponentDescription
	 */
	public void setCustomToolbarComponentDescriptions(Vector<CustomToolbarComponentDescription> newCustomToolbarComponentDescriptions) {
		this.customToolbarComponentDescriptions = newCustomToolbarComponentDescriptions;
	}
	
}
