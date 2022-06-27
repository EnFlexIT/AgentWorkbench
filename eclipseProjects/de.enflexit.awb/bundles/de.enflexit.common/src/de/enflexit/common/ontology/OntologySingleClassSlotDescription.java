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
package de.enflexit.common.ontology;

import java.lang.reflect.Method;
import java.util.Hashtable;

/**
 * This class represents a single slot description, which is used for the
 * list of slots in {@link OntologySingleClassDescription}.
 * 
 * @see OntologySingleClassDescription
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OntologySingleClassSlotDescription {

	private String slotName = null;
	private String slotCardinality = null;
	private boolean slotCardinalityIsMultiple = false;
	private String slotVarType = null;
	private String slotVarTypeCorrected = null;
	private String slotOtherFacts = null;

	private Hashtable<String, Method> methodList = null;
	
	/**
	 * Default Constructor of this class.
	 */
	public OntologySingleClassSlotDescription() {
	}	
	
	/**
	 * Constructor of this class.
	 *
	 * @param currSlotName the current slot name
	 * @param currSlotCardinality the current slot cardinality
	 * @param currSlotVarType the current slot variable type
	 * @param otherFacts the other facts
	 */
	public OntologySingleClassSlotDescription(String currSlotName, String currSlotCardinality, String currSlotVarType, String otherFacts) {
		this.slotName = currSlotName;
		this.slotCardinality = currSlotCardinality;
		if (this.slotCardinality.equalsIgnoreCase("multiple")) {
			this.slotCardinalityIsMultiple = true;
		} else {
			this.slotCardinalityIsMultiple = false;
		}		
		this.setSlotVarType(currSlotVarType);
		this.slotOtherFacts = otherFacts;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {

		if (object instanceof OntologySingleClassSlotDescription) {
			OntologySingleClassSlotDescription compare = (OntologySingleClassSlotDescription) object;
			if (this.slotName.equals(compare.slotName)==false) {
				return false;
			}
			if (this.slotCardinality.equals(compare.slotCardinality)==false) {
				return false;
			}
			if (this.slotVarType.equals(compare.slotVarType)==false) {
				return false;
			}
			if (this.methodList.equals(compare.methodList)==false) {
				return false;
			}
			return true;
		} else {
			return false;	
		}
	}
	
	/**
	 * Gets the slot name.
	 * @return the slotName
	 */
	public String getSlotName() {
		return slotName;
	}
	/**
	 * Sets the slot name.
	 * @param slotName the slotName to set
	 */
	public void setSlotName(String slotName) {
		this.slotName = slotName;
	}

	/**
	 * Gets the slot cardinality.
	 * @return the slotCardinality
	 */
	public String getSlotCardinality() {
		return slotCardinality;
	}
	/**
	 * Sets the slot cardinality.
	 * @param slotCardinality the slotCardinality to set
	 */
	public void setSlotCardinality(String slotCardinality) {
		this.slotCardinality = slotCardinality;
		if (this.slotCardinality.equalsIgnoreCase("multiple")) {
			this.slotCardinalityIsMultiple = true;
		} else {
			this.slotCardinalityIsMultiple = false;
		}	
	}

	/**
	 * Checks if is slot cardinality is multiple.
	 * @return the slotCardinalityIsMultiple
	 */
	public boolean isSlotCardinalityIsMultiple() {
		return slotCardinalityIsMultiple;
	}
	/**
	 * Sets the slot cardinality is multiple.
	 * @param slotCardinalityIsMultiple the slotCardinalityIsMultiple to set
	 */
	public void setSlotCardinalityIsMultiple(boolean slotCardinalityIsMultiple) {
		this.slotCardinalityIsMultiple = slotCardinalityIsMultiple;
		if (slotCardinalityIsMultiple==true) {
			this.slotCardinality = "multiple";
		} else {
			this.slotCardinality = "single";
		}
	}

	/**
	 * Gets the slot variable type.
	 * @return the slotVarType
	 */
	public String getSlotVarType() {
		return slotVarType;
	}
	/**
	 * Sets the slot variable type.
	 * @param slotVarType the slotVarType to set
	 */
	public void setSlotVarType(String slotVarType) {
		
		this.slotVarType = slotVarType;
		
		String checkSlotVarType = slotVarType;
		if(checkSlotVarType.matches("Instance of (.)*")){
			checkSlotVarType = checkSlotVarType.substring(12);	
		}
		if (checkSlotVarType.equalsIgnoreCase("aid")) {
			checkSlotVarType = OntologyClassTree.BaseClassAID;
		}
		this.slotVarTypeCorrected = checkSlotVarType;
	}

	/**
	 * Sets the slot var type corrected.
	 * @param slotVarTypeCorrected the new slot var type corrected
	 */
	public void setSlotVarTypeCorrected(String slotVarTypeCorrected) {
		this.slotVarTypeCorrected = slotVarTypeCorrected;
	}
	/**
	 * Gets the slot var type corrected.
	 * @return the slot var type corrected
	 */
	public String getSlotVarTypeCorrected() {
		return slotVarTypeCorrected;
	}

	/**
	 * Gets the slot other facts.
	 * @return the slotOtherFacts
	 */
	public String getSlotOtherFacts() {
		return slotOtherFacts;
	}
	/**
	 * Sets the slot other facts.
	 * @param slotOtherFacts the slotOtherFacts to set
	 */
	public void setSlotOtherFacts(String slotOtherFacts) {
		this.slotOtherFacts = slotOtherFacts;
	}
	
	/**
	 * Sets the slot method list.
	 * @param methodList the methodList to set
	 */
	public void setSlotMethodList(Hashtable<String, Method> methodList) {
		this.methodList = methodList;
	}
	/**
	 * Gets the slot method list.
	 * @return the methodList
	 */
	public Hashtable<String, Method> getSlotMethodList() {
		return methodList;
	}
	
}
