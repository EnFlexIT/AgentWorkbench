package mas.onto;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import application.reflection.ReflectClass;
import application.reflection.ReflectClass.Slot;


public class OntologyClassTreeObject extends Object {

	/**
	 * The instances of this class represents the UserObjects
	 * which are located in the Nodes of the OntologyTree 
	 */
	private OntologyClass Ontology = null;
	private Class<?> OntoClass = null;
	private String ObjectTitle = null;
	
	/**
	 * Defines the Class belonging to the represented node
	 * @param Clazz
	 */
	public OntologyClassTreeObject (OntologyClass onto, Class<?> Clazz) {
		Ontology = onto;
		OntoClass = Clazz;
	}
	/**
	 * In case, that there is no class, a node can be
	 * represented by a simple String. E.g. in case of
	 * 'Concept' or 'AgentAction' there is no class
	 * related to this node. 
	 * @param NodeTitle
	 */
	public OntologyClassTreeObject (OntologyClass onto, String NodeTitle) {
		Ontology = onto;
		ObjectTitle = NodeTitle;
	}
	/**
	 * returns the title of this node object
	 */
	public String toString() {
		if ( OntoClass == null) {
			if ( ObjectTitle == null ) {
				return "-";	
			} else {
				return ObjectTitle;
			}			
		} else {
			if ( ObjectTitle == null ) {
				return getClassTextSimple();
			} else {
				return ObjectTitle;	
			}
		}
	}
	/**
	 * Returns the simple class name from a class-reference
	 * @param Reference
	 * @return String
	 */
	public String getClassTextSimple() {
		String Reference = OntoClass.getName();
		return Reference.substring( Reference.lastIndexOf(".")+1 );
	}
	/**
	 * returns the Class of the ontology node
	 * @return
	 */
	public Class<?> getOntoClass() {
		return OntoClass;
	}
	/**
	 * Allows to set the node title, even if there is a 
	 * concrete class (with reference and title)
	 * @param OntologyTitle
	 */
	public void setObjectTitle(String OntologyTitle){
		ObjectTitle = OntologyTitle;
	}
	/**
	 * This returns the 'DefaultTableModel' for a single
	 * class out of the ontology-classes 
	 * @return
	 */
	public DefaultTableModel getTableModel4Slot() {
		
		// --- TableModel aufbauen ----------------------------------
		DefaultTableModel tm4s = new DefaultTableModel();
		
		// --- Spaltenünerschriften einstellen ----------------------
		tm4s.addColumn("Name");
		tm4s.addColumn("Cardinality");
		tm4s.addColumn("Type");
		tm4s.addColumn("Other Facets");		

		if ( OntoClass == null ) {
			return tm4s;
		}
		
		// --- Nach den entsprechenden Slots im Vokabular filtern ---
		Hashtable<String, String> OntoSlotHash = Ontology.OntologieVocabulary.getSlots( OntoClass );
		ReflectClass RefCla = new ReflectClass( OntoClass, OntoSlotHash );
		
		Vector<String> v = new Vector<String>( OntoSlotHash.keySet() );
	    Collections.sort(v);
	    Iterator<String> it = v.iterator();
	    while (it.hasNext()) {
	    	
	    	// --- Wort/Slot  der Ontologie ermitteln --------------- 
	    	String Key = it.next();
	    	String Word = OntoSlotHash.get(Key);
	    	
	    	// --- Slot untersuchen ... -----------------------------
	    	Slot CurrSlot = RefCla.getSlot(Word);
	    	
	    	// --- Zeile an TableModel anfügen ----------------------	    	
	    	Vector<String> rowData = new Vector<String>(); 
	    	rowData.add( Word );
	    	rowData.add( CurrSlot.Cardinality );
	    	rowData.add( CurrSlot.VarType );
	    	rowData.add( CurrSlot.OtherFacts );
	    	tm4s.addRow(rowData);
	    }		
		return tm4s;		
	}
}
