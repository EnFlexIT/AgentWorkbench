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
package org.awb.env.networkModel.persistence;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.maps.MapSettings;

/**
 * This class describes the structure of the NetowkModel's XML file.
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NetworkModelFileContent", propOrder = {
    "layoutID",
    "mapSettingsList",
    "networkComponentList"
})
public class NetworkModelFileContent {

	private String layoutID;
	private TreeMap<String, MapSettings> mapSettingsList;
	
	private TreeMap<String, NetworkComponent> networkComponentList;
	
	/**
	 * Default constructor, required for JAXB.
	 */
	public NetworkModelFileContent() { }

	
	/**
	 * Returns the ID of the used layout.
	 * @return the layout used
	 */
	public String getLayoutID() {
		return layoutID;
	}
	/**
	 * Sets the ID of the used layout.
	 * @param newLayoutID the new layout used
	 */
	public void setLayoutID(String newLayoutID) {
		this.layoutID = newLayoutID;
	}
	
	/**
	 * Returns the tree map of layout-dependent {@link MapSettings}.
	 * @return the map settings tree map
	 */
	public TreeMap<String, MapSettings> getMapSettingsList() {
		return mapSettingsList;
	}
	/**
	 * Sets the tree map of {@link MapSettings} .
	 * @param mapSettingsTreeMap the map settings tree map
	 */
	public void setMapSettingsList(TreeMap<String, MapSettings> mapSettingsTreeMap) {
		this.mapSettingsList = mapSettingsTreeMap;
	}
	
	/**
	 * Return the NetworkComponent list.
	 * @return the networkComponentList
	 */
	public TreeMap<String, NetworkComponent> getNetworkComponentList() {
		return networkComponentList;
	}
	/**
	 * Sets the NetworkComponent list.
	 * @param networkComponentList the networkComponentList to set
	 */
	public void setNetworkComponentList(TreeMap<String, NetworkComponent> networkComponentList) {
		this.networkComponentList = networkComponentList;
	}

	/**
	 * Save the current NetworkModelFileContent to the specified XML file.
	 *
	 * @param xmlFile the component file
	 * @return true, if successful
	 */
	public boolean save(File xmlFile) {
		return save(xmlFile, this);
	}
	
	
	// ------------------------------------------------------------------------
	// --- From here, we have static access methods for save and load --------- 
	// ------------------------------------------------------------------------
	/**
	 * Save the NetworkModelFileContent to a XML file.
	 *
	 * @param xmlFile the component file
	 * @param fileContent the NetworkModelFileContent
	 * @return true, if successful
	 */
	public static boolean save(File xmlFile, NetworkModelFileContent fileContent) {
		
		boolean success = false;
		FileWriter componentFileWriter = null;
		try {
			componentFileWriter = new FileWriter(xmlFile);
			JAXBContext context = JAXBContext.newInstance(NetworkModelFileContent.class);
			Marshaller marsh = context.createMarshaller();
			marsh.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marsh.marshal(fileContent, componentFileWriter);
			success = true;
			
		} catch (IOException | JAXBException e) {
			System.err.println("[" +  NetworkModelFileContent.class.getSimpleName() + "] Error while saving NetworkModel file!");
			e.printStackTrace();
		} finally {
			if (componentFileWriter!=null) {
				try {
					componentFileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return success;
	}

	/**
	 * Load the NetworkModelFileContent from a XML file.
	 *
	 * @param xmlFile the components file
	 * @return true, if successful
	 */
	public static NetworkModelFileContent load(File xmlFile) {
		return load(xmlFile, true);
	}
	/**
	 * Load the NetworkModelFileContent from a XML file.
	 *
	 * @param xmlFile the components file
	 * @param isPrintException set true if you want to print exceptions
	 * @return true, if successful
	 */
	public static NetworkModelFileContent load(File xmlFile, boolean isPrintException) {
		
		NetworkModelFileContent fileContent = null;
		if (xmlFile.exists()) {
		
			FileReader componentReader = null;
			try {
				componentReader = new FileReader(xmlFile);

				JAXBContext context = JAXBContext.newInstance(NetworkModelFileContent.class);
				Unmarshaller unmarsh = context.createUnmarshaller();
				fileContent = (NetworkModelFileContent) unmarsh.unmarshal(componentReader);
				
			} catch (JAXBException | IOException ex) {
				if (isPrintException) ex.printStackTrace();
			} finally {
				try {
					componentReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return fileContent;
	}

}
