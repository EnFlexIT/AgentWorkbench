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
package org.awb.env.networkModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;

import org.awb.env.networkModel.helper.GraphEdgeDirection;
import org.awb.env.networkModel.prototypes.AbstractGraphElementPrototype;

import de.enflexit.common.SerialClone;

/**
 * This class represents a component of the modeled network. It may contains it's ontology representation, it's 
 * {@link AbstractGraphElementPrototype}, the nodes and edges representing it in the environment graph and an
 * ID for easier access.
 * 
 * @see NetworkModel
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
@XmlType(name = "NetworkComponent", propOrder = {
    "id",
    "alternativeIDs",
    "type",
    "graphElementIDs",
    "edgeDirections",
    "dataModelStorageSettings",
    "dataModelBase64"
})
public class NetworkComponent implements DataModelNetworkElement, Serializable, Comparable<NetworkComponent> {

	private static final long serialVersionUID = 537431665305238609L;

	/** The NetworkComponent's ID. */
	protected String id;
	
	/** Alternative ID's, freely to be used depending on the application context */
	@XmlElementWrapper(name = "alternativeIDs")
	@XmlElement(name="alternativeID")
	protected TreeMap<String, String> alternativeIDs;
	
	/** The NetworkComponent's type. */
	protected String type;
	/** The IDs of the nodes and edges that are part of this NetworkComponent. */
	protected HashSet<String> graphElementIDs = new HashSet<String>();
	/** The direction(s) of the edge(s). */
	protected HashMap<String, GraphEdgeDirection> edgeDirections;
	
	/**	The data model for this NetworkComponent.*/
	@XmlTransient
	protected Object dataModel;
	
	@XmlElementWrapper(name = "dataModelStorageSettings")
	@XmlElement(name="dataModelStorageSetting")
	protected TreeMap<String, String> dataModelStorageSettings;
	
	/** The data model for this NetworkComponent encoded as Base64 String*/
	@XmlElementWrapper(name = "dataModelsBase64")
	@XmlElement(name="dataModelBase64")
	protected Vector<String> dataModelBase64;
	
	
	/**
	 * Instantiates a new network component.
	 * Depreciated, but available because of the JAXB context
	 */
	@Deprecated
	public NetworkComponent() { }
	
	/**
	 * Instantiates a new network component.
	 *
	 * @param id the id
	 * @param type the type
	 * @param graphElements the graph elements
	 */
	public NetworkComponent(String id, String type, HashSet<GraphElement> graphElements) {
		this.setId(id);
		this.setType(type);
		this.setGraphElements(graphElements);
	}
	
	/**
	 * Gets the id.
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * Sets the id and changes graphEleemntIDs
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the TreeMap of alternative ID's.
	 * @return the alternative ID's
	 */
	@XmlTransient
	public TreeMap<String, String> getAlternativeIDs() {
		if (alternativeIDs==null) {
			alternativeIDs = new TreeMap<>();
		}
		return alternativeIDs;
	}
	/**
	 * Sets the alternative ID's. TreeMap
	 * @param alternativeIDs the alternative ID's
	 */
	public void setAlternativeIDs(TreeMap<String, String> alternativeIDs) {
		this.alternativeIDs = alternativeIDs;
	}
	/**
	 * Returns a string description of the alternative IDs .
	 *
	 * @param lineSeparator the line separator to be used
	 * @return the description of alternative IDs
	 */
	public String getAlternativeIDsDescription(String lineSeparator) {
		
		if (this.alternativeIDs==null || this.alternativeIDs.size()==0) return null;
		
		String altIDDesc = "";
		
		String lineSeparatorToUse = lineSeparator;
		if (lineSeparatorToUse==null || lineSeparatorToUse.isBlank()) lineSeparatorToUse = ", ";
		
		List<String> altIDKeyList = new ArrayList<>(this.alternativeIDs.keySet());
		for (int i = 0; i < altIDKeyList.size(); i++) {
			String altIDKey = altIDKeyList.get(i);
			String altIDValue = this.alternativeIDs.get(altIDKey);
			altIDDesc += altIDKey + ": " + altIDValue;
			if (i < (altIDKeyList.size()-1)) {
				altIDDesc += lineSeparatorToUse;
			}
		}
		return altIDDesc;
	}
	
	/**
	 * Puts an alternative ID to the current NetworkComponent.
	 *
	 * @param idKey the id of the key
	 * @param idValue the id value
	 * @return the string, possibly overwritten 
	 */
	public String putAlternativeID(String idKey, String idValue) {
		if (idKey==null) return null;
		return this.getAlternativeIDs().put(idKey, idValue);
	}
	/**
	 * Return the alternative ID for the specified key.
	 *
	 * @param idKey the id key
	 * @return the alternative ID
	 */
	public String getAlternativeID(String idKey) {
		if (idKey==null) return null;
		return this.getAlternativeIDs().get(idKey);
	}
	/**
	 * Removes the alternative ID for the specified key.
	 *
	 * @param idKey the id key
	 * @return the removed alternative ID
	 */
	public String removeAlternativeID(String idKey) {
		if (idKey==null) return null;
		return this.getAlternativeIDs().remove(idKey);
	}
	
	/**
	 * Returns all alternative ID's (the values) as a list.
	 * @return the alternative ID list
	 */
	public List<String> getAlternativeIDList() {
		return new ArrayList<>(this.getAlternativeIDs().values());
	}
	/**
	 * Removes the all alternative IDs.
	 * @return the removed tree map of alternative ID's
	 */
	public TreeMap<String, String> removeAllAlternativeIDs() {
		TreeMap<String, String> oldAltIDs = this.getAlternativeIDs();
		this.setAlternativeIDs(null);
		return oldAltIDs;
	}
	
	
	/**
	 * Gets the type.
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * Sets the type.
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Returns the graph element IDs present in the component as a HashSet.
	 * @return the graphElements
	 */
	public HashSet<String> getGraphElementIDs() {
		return graphElementIDs;
	}
	/**
	 * Sets the graph element IDs as a HashSet.
	 * @param graphElementIDs the graphElements to set
	 */
	public void setGraphElementIDs(HashSet<String> graphElementIDs) {
		this.graphElementIDs = graphElementIDs;
	}
	/**
	 * Sets the id HashSet elements IDs by extracting the IDs from an GraphElement HashSet.
	 * @param graphElements the new graph elements
	 */
	public void setGraphElements(HashSet<GraphElement> graphElements) {
		if (graphElements==null) {
			graphElementIDs = null;
		} else {
			HashSet<String> newGraphElementIDs = new HashSet<String>();
			for (GraphElement graphElement : graphElements) {
				newGraphElementIDs.add(graphElement.getId());
			}
			graphElementIDs = newGraphElementIDs;	
		}
	}

	/**
	 * Returns the current edge directions.
	 * @return the edgeDirections
	 */
	public HashMap<String, GraphEdgeDirection> getEdgeDirections() {
		return edgeDirections;
	}
	/**
	 * Sets the edge directions.
	 * @param edgeDirections the edgeDirections to set
	 */
	public void setEdgeDirections(HashMap<String, GraphEdgeDirection> edgeDirections) {
		this.edgeDirections = edgeDirections;
	}

	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.DataModelNetworkElement#getDataModel()
	 */
	@Override
	@XmlTransient
	public Object getDataModel() {
		return dataModel;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.DataModelNetworkElement#setDataModel(java.lang.Object)
	 */
	@Override
	public void setDataModel(Object dataModel) {
		this.dataModel = dataModel;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.DataModelNetworkElement#getDataModelStorageSettings()
	 */
	@Override
	@XmlTransient
	public TreeMap<String, String> getDataModelStorageSettings() {
		return dataModelStorageSettings;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.DataModelNetworkElement#setDataModelStorageSettings(java.util.TreeMap)
	 */
	@Override
	public void setDataModelStorageSettings(TreeMap<String, String> dataModelStorageSettings) {
		this.dataModelStorageSettings = dataModelStorageSettings;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.DataModelNetworkElement#getDataModelBase64()
	 */
	@Override
	@XmlTransient
	public Vector<String> getDataModelBase64() {
		return dataModelBase64;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.DataModelNetworkElement#setDataModelBase64(java.util.Vector)
	 */
	@Override
	public void setDataModelBase64(Vector<String> dataModelBase64) {
		this.dataModelBase64 = dataModelBase64;
	}	

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getId() + " - " + this.getType();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(NetworkComponent ncToComp) {
		
		// --- Check the pure string case first ------------- 
		String n1NumberString = this.getId().replaceAll("\\D+","");
		String n2NumberString = ncToComp.getId().replaceAll("\\D+","");
		if (n1NumberString.isEmpty() && n2NumberString.isEmpty()) {
			return this.getId().compareTo(ncToComp.getId());
		}
		
		n1NumberString = this.ensureValidLongValueString(n1NumberString);
		n2NumberString = this.ensureValidLongValueString(n2NumberString);
		
		// --- In case of available numbers -----------------
		if (n1NumberString==null || n1NumberString.isEmpty()==true) n1NumberString = "0";		
		if (n2NumberString==null || n2NumberString.isEmpty()==true) n2NumberString = "0";
		try {
			Long n1 = Long.parseLong(n1NumberString);
			Long n2 = Long.parseLong(n2NumberString);
			return n1.compareTo(n2);
		} catch (Exception ex) {}
		
		return this.getId().compareTo(ncToComp.getId());
	}
	
	/**
	 * This method makes sure the provided numeric String is actually a valid long value, 
	 * by and making sure it has no more than 18 places (Long.MAX_VALUE has 19).
	 * @param numberString the number string
	 * @return the string
	 */
	private String ensureValidLongValueString(String numberString) {
		
		if (numberString.length()>18) {
			// If longer than 18 chars, only use the last 18 
			int endIndex = numberString.length();
			int startIndex = endIndex-18;
			return numberString.substring(startIndex, endIndex);
		} else {
			return numberString;
		}
	}

	/**
	 * Returns a copy of the current NetworkComponent.
	 * @return the copy
	 */
	public NetworkComponent getCopy() {
		return this.getCopy(true);
	}
	/**
	 * Returns a copy of the current NetworkComponent.
	 * @param isIncludeDataModel the indicator include or exclude the current data model
	 * @return the copy
	 */
	public NetworkComponent getCopy(boolean isIncludeDataModel) {
		NetworkComponent copy = null;	
		try {
			if (isIncludeDataModel==true) {
				// --- Include the data model -------------
				copy = SerialClone.clone(this);
			} else {
				// --- Exclude the data model -------------
				synchronized (this) {
					Object dataModel = this.getDataModel();
					this.setDataModel(null);
					copy = SerialClone.clone(this);
					this.setDataModel(dataModel);
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return copy;
	}

}
