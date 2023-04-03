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

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.ui.GraphEnvironmentControllerGUI;
import org.awb.env.networkModel.controller.ui.toolbar.CustomToolbarComponentDescription;

import agentgui.core.application.Language;
import jade.util.leap.Serializable;

/**
 * The Class GeneralGraphSettings4MAS describes the general settings and the components for a 
 * {@link NetworkModel} and it's visualization within the {@link GraphEnvironmentControllerGUI}.
 *
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
@XmlRootElement
public class GeneralGraphSettings4MAS implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 7425147528482747232L;
	
	/** The Constant PREFIX_NETWORK_COMPONENT. */
	public final static String PREFIX_NETWORK_COMPONENT = "n"; 
	/** The prefix name for the NetworkComponentAdapter of a GraphNode */
	public static final String GRAPH_NODE_NETWORK_COMPONENT_ADAPTER_PREFIX = "GraphNodeAdapter_";
	/** The NetworkComponent type name for a cluster component representation */
	public static final String NETWORK_COMPONENT_TYPE_4_CLUSTER = "C L U S T E R !";
	
	
	/** Default name for the first DomainSettings. */
	public static final String DEFAULT_DOMAIN_SETTINGS_NAME = "Default Domain";
	/** Default name for the first LayoutSettings. */
	public static final String DEFAULT_LAYOUT_SETTINGS_NAME = "Default Layout";
	
	
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

	
	/** Basic color for image icons. If using dynamic colorization, this color will be replaced */
	public static final Color IMAGE_ICON_COLORIZE_BASE_COLOR = new Color(179, 179, 179);
	
	
	/** Default shapes for nodes */
	public static final String SHAPE_ELLIPSE = "Ellipse";
	public static final String SHAPE_RECTANGLE = "Rectangle";
	public static final String SHAPE_ROUND_RECTANGLE = "Round rectangle";
	public static final String SHAPE_REGULAR_POLYGON = "Regular polygon";
	public static final String SHAPE_REGULAR_STAR = "Regular star";
	public static final String SHAPE_IMAGE_SHAPE = "Image shape";
	
	/** Default shapes for a central cluster node */
	public static final String SHAPE_DEFAULT_4_CLUSTER = SHAPE_RECTANGLE;
	
	
	/** The enumeration for component sorting options */
	public enum ComponentSorting {
		Alphanumeric("Alphanumeric"),
		Alphabetical("Alphabetical");
		
		private final String displayText;
		private ComponentSorting(String displayText) {
			this.displayText = displayText;
		}
		/* (non-Javadoc)
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return Language.translate(displayText, Language.EN);
		}
	}
	
	
	
	
	/** The available domains used in the {@link GraphEnvironmentController} */
	private TreeMap<String, DomainSettings> currentDomainSettings;
	/** The component type settings used in the {@link GraphEnvironmentController} */
	private TreeMap<String, ComponentTypeSettings> currentCTS;
	/** The layout setting to use in the visualization */
	private TreeMap<String, LayoutSettings> currentLayoutSettings;
	
	private ComponentSorting componentSorting = ComponentSorting.Alphanumeric;
	
	/** 
	 * The current vector of custom BasicGraphGuiCustomJBottonDescription. 
	 **/
	@XmlTransient
	private Vector<CustomToolbarComponentDescription> customToolbarComponentDescriptions;
	
	
	/**
	 * Default constructor
	 */
	public GeneralGraphSettings4MAS() {	}
	
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
			// --- Compare LayoutSettings -----------------
			if (isEqual==true) {
				isEqual = this.hasEqualLayoutSettings(ggsToCompare);
			}
			// --- Compare 'componentSorting' -------------
			if (isEqual==true) {
				isEqual = (ggsToCompare.getComponentSorting()==this.getComponentSorting());
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
				if (dsLocal.equals(ds2Comp)==false) return false;
			}
		}
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
				if (ctsLocal.equals(cts2Comp)==false) return false;
			}
		}
		return isEqual;
	}

	/**
	 * Checks for equal layout settings.
	 *
	 * @param ggsToCompare the GeneralGraphSettings4MAS to compare
	 * @return true, if successful
	 */
	public boolean hasEqualLayoutSettings(GeneralGraphSettings4MAS ggsToCompare) {
		
		boolean isEqual = true;
		TreeMap<String, LayoutSettings> lsTreeMapToCompare = ggsToCompare.getLayoutSettings();
		isEqual = (lsTreeMapToCompare.size()==this.getCurrentCTS().size());
		if (isEqual==true) {
			// --- Compare each element in the TreeMap ----
			Vector<String> keyVector = new Vector<String>(lsTreeMapToCompare.keySet());
			for (int i = 0; i < keyVector.size(); i++) {
				String key = keyVector.get(i);
				LayoutSettings ls2Comp = lsTreeMapToCompare.get(key);
				LayoutSettings lsLocal = this.getLayoutSettings().get(key);
				if (lsLocal==null) return false;
				if (lsLocal.equals(ls2Comp)==false) return false;
			}
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
			
			if (file.exists()==false) {
				// --- Create file, if it does not exists -----------
				GeneralGraphSettings4MAS.save(file, new GeneralGraphSettings4MAS());
			}
			
			// -- Create context and read the file ------------------
			JAXBContext context = JAXBContext.newInstance(GeneralGraphSettings4MAS.class);
		    Unmarshaller unmarsh = context.createUnmarshaller();

		    FileReader componentReader = new FileReader(file);
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
		copy.setCurrentCTS(this.getComponentTypeSettingsCopy());
		copy.setDomainSettings(this.getDomainSettingsCopy());
		copy.setLayoutSettings(this.getLayoutSettingsCopy());
		copy.setComponentSorting(this.getComponentSorting());
		copy.setCustomToolbarComponentDescriptions(this.copyCustomToolbarComponentDescription());
		return copy;
	}
	/**
	 * Copies the component type settings.
	 * @return the hash map
	 */
	private TreeMap <String, DomainSettings> getDomainSettingsCopy() {
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
	 * Copies the component type settings.
	 * @return the hash map
	 */
	private TreeMap <String, ComponentTypeSettings> getComponentTypeSettingsCopy() {
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
	private TreeMap <String, LayoutSettings> getLayoutSettingsCopy() {
		TreeMap<String, LayoutSettings> copyLayoutMap = new TreeMap<String, LayoutSettings>();
		Iterator<String> ctsIt = this.getLayoutSettings().keySet().iterator();
		while (ctsIt.hasNext()) {
			String element = ctsIt.next();
			LayoutSettings ls = this.currentLayoutSettings.get(element); 
			copyLayoutMap.put(element, ls.getCopy());
		}
		return copyLayoutMap;
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
			currentCTS = new TreeMap<>();
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
		if (this.currentDomainSettings.size()==0 || this.currentDomainSettings.containsKey(DEFAULT_DOMAIN_SETTINGS_NAME)==false) {
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
	 * Generates a unique layout ID.
	 * @return the string
	 */
	public static String generateLayoutID() {
		
		String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		StringBuilder builder = new StringBuilder();
		int count = 10 ;
		while (count--!=0) {
			int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}
	/**
	 * Returns the current layout settings.
	 * @return the layout settings
	 */
	public TreeMap<String, LayoutSettings> getLayoutSettings() {
		if (currentLayoutSettings==null) {
			currentLayoutSettings = new TreeMap<>();
		}
		if (this.containsDefaultLayout()==false) {
			LayoutSettings ls = new LayoutSettings();
			ls.setLayoutName(DEFAULT_LAYOUT_SETTINGS_NAME);
			currentLayoutSettings.put(generateLayoutID(), ls);
		}
		return currentLayoutSettings;
	}
	/**
	 * Sets the layout settings.
	 * @param layoutSettings the layout settings
	 */
	public void setLayoutSettings(TreeMap<String, LayoutSettings> layoutSettings) {
		this.currentLayoutSettings = layoutSettings;
	}
	/**
	 * Check, if the current LayouSettings contains the default layout.
	 * @return true, if the DefaultLayout is available
	 */
	private boolean containsDefaultLayout() {
		List<LayoutSettings> layoutSettingList = new ArrayList<>(this.currentLayoutSettings.values());
		for (int i = 0; i < layoutSettingList.size(); i++) {
			LayoutSettings lsWork = layoutSettingList.get(i);
			if (lsWork.getLayoutName().equals(DEFAULT_LAYOUT_SETTINGS_NAME)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Returns the layout id derived from the layout name.
	 *
	 * @param layoutName the layout name
	 * @return the layout id that corresponds to the layout name or <code>null</code>
	 */
	public String getLayoutIdByLayoutName(String layoutName) {
		
		List<String> layoutIDs = new ArrayList<>(this.getLayoutSettings().keySet());
		for (int i = 0; i < layoutIDs.size(); i++) {
			String layoutIdWork = layoutIDs.get(i);
			LayoutSettings lsWork = this.getLayoutSettings().get(layoutIdWork);
			if (lsWork.getLayoutName().equals(layoutName)) {
				return layoutIdWork;
			}
		}
		return null;
	}

	
	
	/**
	 * Sets the component sorting.
	 * @param componentSorting the new component sorting
	 */
	public void setComponentSorting(ComponentSorting componentSorting) {
		this.componentSorting = componentSorting;
	}
	/**
	 * Gets the component sorting.
	 * @return the component sorting
	 */
	public ComponentSorting getComponentSorting() {
		return componentSorting;
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
