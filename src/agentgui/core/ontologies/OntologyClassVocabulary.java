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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Reads the public fields of an ontology base class, which extends 'jade.content.onto.Ontology'
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OntologyClassVocabulary extends Hashtable<String, String> implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 866517459374218362L;
	
	/**
	 * Initialise the class, which holds the complete vocabulary
	 * of the current ontology inside of a Hashtable.
	 *
	 * @param clazz the clazz
	 */
	public OntologyClassVocabulary (Class<?> clazz) {
		
		if ( clazz == null ) return;
		Hashtable<String, String> unsortedHash = new Hashtable<String, String>();
		
		Field[] publicFields = clazz.getFields();
		for (int i = 0; i < publicFields.length; i++) {
			
			String fieldName = publicFields[i].getName();
			String fieldValue = null;
			
			try {
				fieldValue = publicFields[i].get(clazz).toString();
				unsortedHash.put(fieldName, fieldValue);
				//System.out.println("Name: " + fieldName + ", Value.: "+fieldValue);								
			} catch (IllegalArgumentException e) {
				//e.printStackTrace();
			} catch (IllegalAccessException e) {
				//e.printStackTrace();
			}
			
		}
		this.putAll(unsortedHash);
		this.addAID_Vocabulary();
	}
	
	/**
	 * Because there is no class for the AID inside a single Ontology, some
	 * values related to this must be added manually.
	 */
	private void addAID_Vocabulary() {
		
		String key = "";
		String value = "";
		
		key = "AID";
		value = "aid";
		this.put(key,value);
		
		key = "AID_ADDRESSES";
		value = "addresses";
		this.put(key,value);
		
		key = "AID_RESOLVERS";
		value = "resolvers";
		this.put(key,value);
		
		key = "AID_NAME";
		value = "name";
		this.put(key,value);
	}
	
	/**
	 * Filter the Vocabulary for a special Ontology-Class, to get
	 * the "slots" / internal variables of the class.
	 *
	 * @param selectedNode the selected node
	 * @return The slots of the selected node / OntologyClassTreeObject
	 */
	public Hashtable<String, String> getSlots(DefaultMutableTreeNode selectedNode) {
		Object uObject = selectedNode.getUserObject();
		OntologyClassTreeObject octo = (OntologyClassTreeObject) uObject;		
		return getSlots( octo.getOntologySubClass() );
	}
	
	/**
	 * Filter the Vocabulary for a special Ontology-Class, to get
	 * the "slots" / internal variables of the class.
	 *
	 * @param ontologyClass the ontology class
	 * @return The slots of the selected node / OntologyClassTreeObject
	 */
	public Hashtable<String, String> getSlots(Class<?> ontologyClass) {
		String reference = ontologyClass.getName();
		String filter4Class = reference.substring(reference.lastIndexOf(".")+1);
		return getSlots(filter4Class);
	}
	
	/**
	 * Filter the Vocabulary for a special Ontology-Class, to get
	 * the "slots" / internal variables of the class.
	 *
	 * @param filter4Class the filter4 class
	 * @return The slots of the selected node / OntologyClassTreeObject
	 */
	public Hashtable<String, String> getSlots(String filter4Class) {

		Set<String> set = this.keySet();
	    Iterator<String> itr = set.iterator();
		Hashtable<String, String> resultHT = new Hashtable<String, String>();
		
		String Prefix = filter4Class.toUpperCase();
		//System.out.println("Suche: " + Prefix);
		
	    // --- Run through vocabulary ------------------------------------
		while (itr.hasNext()) {
	    	String key = itr.next();
	    	if (key.startsWith(Prefix)) {
	    		boolean add2Result = false;
	    		if ( key.length() == Prefix.length() ) {
	    			// --- Match, but we are just looking ---------------- 
	    			// --- for slots and not for classes -----------------
	    			add2Result = false;
	    		} else {
	    			// --- Match with something similar ------------------
	    			if ( key.startsWith(Prefix + "_") ) {
	    				add2Result = true;
	    			}	    			
	    		}
	    		// --- If match found, add ------------------------------- 
	    		if ( add2Result == true ) {
		    		//System.out.println(Key + ": " + this.get( Key ));
		    		resultHT.put( key, this.get(key) );
	    		}
	    	}	    	
	    }
		return resultHT;
	}

}
