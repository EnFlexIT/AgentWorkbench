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
package agentgui.core.ontologies;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import agentgui.core.ontologies.gui.Sorter;
import agentgui.core.ontologies.reflection.ReflectClass;
import agentgui.core.ontologies.reflection.ReflectClass.Slot;


/**
 * The instances of this class represents the UserObjects which are located in the Nodes of the OntologyClassTree.
 *
 * @see OntologyClassTree
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OntologyClassTreeObject extends Object {

	private OntologyClass ontologyClass = null;
	private Class<?> ontologySubClass = null;
	private String objectTitle = null;
	
	private Class<?> parentOntologySubClass = null;
	private OntologyClassTreeObject parentOntologyClassTreeObject = null;
	
	
	
	private boolean isBaseOntology = false;
	private boolean isAID = false;
	
	private boolean isAConcept = false;
	private boolean isAAgentAction = false;
	private boolean isAPredicate = false;
	
	
	/**
	 * Defines the Class belonging to the represented node.
	 *
	 * @param ontologyClazz the ontology clazz
	 * @param clazz the clazz
	 */
	public OntologyClassTreeObject (OntologyClass ontologyClazz, Class<?> clazz) {
		ontologyClass = ontologyClazz;
		ontologySubClass = clazz;
	}
	
	/**
	 * In case, that there is no class, a node can be
	 * represented by a simple String. E.g. in case of
	 * 'Concept' or 'AgentAction' there is no class
	 * related to this node.
	 *
	 * @param ontologyClazz the ontology clazz
	 * @param nodeTitle the node title
	 */
	public OntologyClassTreeObject (OntologyClass ontologyClazz, String nodeTitle) {
		ontologyClass = ontologyClazz;
		objectTitle = nodeTitle;
	}
	
	/**
	 * returns the title of this node object.
	 *
	 * @return the string
	 */
	public String toString() {
		if (ontologySubClass==null) {
			if (objectTitle==null) {
				return "-";	
			} else {
				return objectTitle;
			}			
		} else {
			if (objectTitle==null) {
				return getClassTextSimple();
			} else {
				return objectTitle;	
			}
		}
	}
	
	/**
	 * Returns TRUE if the current Instance has an
	 * real Ontology-Class.
	 *
	 * @return boolean
	 */
	public boolean isClass() {
		if (ontologySubClass==null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the simple class name from a class-reference.
	 *
	 * @return String
	 */
	public String getClassTextSimple() {
		String Reference = ontologySubClass.getName();
		return Reference.substring( Reference.lastIndexOf(".")+1 );
	}
	
	/**
	 * Returns the full class-reference of the current ontology-sub-class.
	 *
	 * @return the class reference
	 */
	public String getClassReference() {
		if (ontologySubClass==null) {
			if (ontologyClass==null) {
				return this.toString();
			} else {
				return ontologyClass.getOntologySourcePackage() + "." + this.toString();	
			}
		} else {
			return ontologySubClass.getName();	
		}
	}
	
	/**
	 * This method returns the instance of the OntologyClass which
	 * belongs to the current Tree-Object.
	 * Here, the basic informations about the Ontology are available!
	 *
	 * @return the ontology class
	 */
	public OntologyClass getOntologyClass() {
		return this.ontologyClass;
	}
	
	/**
	 * returns the Class of the current ontology node.
	 *
	 * @return the ontology sub class
	 */
	public Class<?> getOntologySubClass() {
		return ontologySubClass;
	}
	
	/**
	 * Allows to set the node title, even if there is a
	 * concrete class (with reference and title).
	 *
	 * @param ontologyTitle the new object title
	 */
	public void setObjectTitle(String ontologyTitle){
		objectTitle = ontologyTitle;
	}
	
	/**
	 * This returns the 'DefaultTableModel' for a single
	 * class out of the ontology-classes.
	 *
	 * @return the table model4 slot
	 */
	public DefaultTableModel getTableModel4Slot() {
		
		DefaultTableModel tm4s = new DefaultTableModel();
		tm4s.addColumn("Name");
		tm4s.addColumn("Cardinality");
		tm4s.addColumn("Type");
		tm4s.addColumn("Other Facets");		

		if (ontologySubClass == null) {
			return tm4s;
		}
		
		// --- Nach den entsprechenden Slots im Vokabular filtern ---
		Hashtable<String, String> ontoSlotHash = ontologyClass.ontologieVocabulary.getSlots(ontologySubClass);
		ReflectClass reflectedClass = new ReflectClass(ontologySubClass, ontoSlotHash);
		
		Vector<String> v = new Vector<String>( ontoSlotHash.keySet() );
	    Collections.sort(v);
	    Iterator<String> it = v.iterator();
	    while (it.hasNext()) {
	    	
	    	// --- Get Word of the ontology ------------------------- 
	    	String key = it.next();
	    	String word = ontoSlotHash.get(key);
	    	
	    	// --- Get Slot... --------------------------------------
	    	Slot currSlot = reflectedClass.getSlot(word);
	    	
	    	// --- Add table row ------------------------------------	    	
	    	Vector<String> rowData = new Vector<String>(); 
	    	rowData.add(word );
	    	rowData.add(currSlot.Cardinality);
	    	rowData.add(currSlot.VarType);
	    	rowData.add(currSlot.OtherFacts);
	    	tm4s.addRow(rowData);
	    }	
	    
	    // ----------------------------------------------------------
	    // --- Are there slots from the parent Node? ----------------
	    // ----------------------------------------------------------
	    if (parentOntologyClassTreeObject!=null) {
	    	DefaultTableModel subTBmodel = parentOntologyClassTreeObject.getTableModel4Slot();
	    	Vector<?> subDataVector = subTBmodel.getDataVector();
	    	for (int i = 0; i < subDataVector.size(); i++) {
	    		Vector<?> rowVector = (Vector<?>) subDataVector.get(i);
	    		tm4s.addRow(rowVector);
			}
	    	Sorter.sortTableModel(tm4s, 0);
	    }
	    // ----------------------------------------------------------
	    return tm4s;		
	}
	
	/**
	 * This returns the a ArrayList of Slots for a single
	 * class out of the ontology-classes.
	 *
	 * @return the class description
	 */
	public OntologySingleClassDescription getClassDescription() {
		
		OntologySingleClassDescription ocd = null;
		OntologySingleClassSlotDescription osd = null;

		if ( ontologySubClass == null ) {
			return null;
		}
		
		// --- Beschreibungsobjekt initialisieren -------------------
		ocd = new OntologySingleClassDescription();
		ocd.setClazz(ontologySubClass);
		ocd.setClassReference(this.getClassReference()); // Package und Class werden hier automatisch gesetzt
		
		// --- Nach den entsprechenden Slots im Vokabular filtern ---
		Hashtable<String, String> ontoSlotHash = ontologyClass.ontologieVocabulary.getSlots(ontologySubClass);
		ReflectClass reflectedClass = new ReflectClass(ontologySubClass, ontoSlotHash);

		Vector<String> v = new Vector<String>(ontoSlotHash.keySet());
	    Collections.sort(v);
	    Iterator<String> it = v.iterator();
	    while (it.hasNext()) {
	    	
	    	// --- Wort/Slot der Ontologie ermitteln --------------- 
	    	String key = it.next();
	    	String word = ontoSlotHash.get(key);
	    	
	    	// --- Slot untersuchen ... -----------------------------
	    	Slot currSlot = reflectedClass.getSlot(word);
	    	
	    	// --- Objekt v. Typ 'OntologySlotDescription' erzeugen -	    	
	    	osd = new OntologySingleClassSlotDescription();
	    	osd.setSlotName(word);
	    	osd.setSlotCardinality(currSlot.Cardinality);
	    	osd.setSlotVarType(currSlot.VarType);
	    	osd.setSlotOtherFacts(currSlot.OtherFacts);
	    	osd.setSlotMethodList(currSlot.MethodList);
	    	
	    	ocd.getArrayList4SlotDescriptions().add(osd);
	    }		
	    
	    // ----------------------------------------------------------
	    // --- Are there slots from the parent Node? ----------------
	    // ----------------------------------------------------------
	    if (parentOntologyClassTreeObject!=null) {
	    	OntologySingleClassDescription subOCD = parentOntologyClassTreeObject.getClassDescription();
	    	ocd.getArrayList4SlotDescriptions().addAll(subOCD.getArrayList4SlotDescriptions());
	    	Sorter.sortSlotDescriptionArray(ocd.getArrayList4SlotDescriptions());
	    }
	    // ----------------------------------------------------------
		return ocd;
	}
	
	/**
	 * Sets the checks if is base ontology.
	 * @param isBaseOntology the new checks if is base ontology
	 */
	public void setIsBaseOntology(boolean isBaseOntology) {
		this.isBaseOntology = isBaseOntology;
	}
	/**
	 * Checks if is base ontology.
	 * @return true, if is base ontology
	 */
	public boolean isBaseOntology() {
		return isBaseOntology;
	}

	/**
	 * Sets the checks if is aid.
	 * @param isAID the new checks if is aid
	 */
	public void setIsAID(boolean isAID) {
		this.isAID = isAID;
	}
	/**
	 * Checks if is a AID.
	 * @return true, if is aid
	 */
	public boolean isAID() {
		return isAID;
	}

	/**
	 * Sets the checks if is concept.
	 * @param isConcept the new checks if is concept
	 */
	public void setIsConcept(boolean isConcept) {
		this.isAConcept = isConcept;
	}
	/**
	 * Checks if is concept.
	 * @return the isAConcept
	 */
	public boolean isConcept() {
		return isAConcept;
	}
	
	/**
	 * Sets the checks if is agent action.
	 * @param isAgentAction the new checks if is agent action
	 */
	public void setIsAgentAction(boolean isAgentAction) {
		this.isAAgentAction = isAgentAction;
	}
	/**
	 * Checks if is agent action.
	 * @return the isAAgentAction
	 */
	public boolean isAgentAction() {
		return isAAgentAction;
	}
	
	/**
	 * Sets the checks if is predicate.
	 * @param isPredicate the new checks if is predicate
	 */
	public void setIsPredicate(boolean isPredicate) {
		this.isAPredicate = isPredicate;
	}
	/**
	 * Checks if is predicate.
	 * @return the isAPredicate
	 */
	public boolean isPredicate() {
		return isAPredicate;
	}
	
	/**
	 * Sets the parent ontology class tree object.
	 * @param parentOntologyClassTreeObject the parentOntologyClassTreeObject to set
	 */
	public void setParentOntologyClassTreeObject(OntologyClassTreeObject parentOntologyClassTreeObject) {
		this.parentOntologyClassTreeObject = parentOntologyClassTreeObject;
	}
	/**
	 * Gets the parent ontology class tree object.
	 * @return the parentOntologyClassTreeObject
	 */
	public OntologyClassTreeObject getParentOntologyClassTreeObject() {
		return parentOntologyClassTreeObject;
	}
	
	/**
	 * Gets the parent ontology sub class.
	 * @return the parent ontology sub class
	 */
	public Class<?> getParentOntologySubClass() {
		return parentOntologySubClass;
	}
	/**
	 * Sets the parent ontology sub class.
	 * @param parentOntologySubClass the new parent ontology sub class
	 */
	public void setParentOntologySubClass(Class<?> parentOntologySubClass) {
		this.parentOntologySubClass = parentOntologySubClass;
	}
	
}
