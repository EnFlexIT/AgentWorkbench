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
package agentgui.core.project;

import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;


/**
 * This Class represents the mappings of all project agents to a specific starting 
 * configuration, given through elements of the current project ontology's.
 * 
 * In other words: Every agent can be configured with a set of start arguments
 * (in JADE this can be done by an object array - Object[]).  
 * Now the specific parts of this object array can be selected out of one
 * ontology, which is defined in the current project. 
 *  
 * The first String in this HashMap contains the class reference of the agent
 * and the second String a list of ontology classes, separated by a '|' 
 *  
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class AgentConfiguration extends Hashtable<String, String> {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1826500137791805011L;

	/** The Allow reference duplicates. */
	private boolean AllowReferenceDuplicates = true;
	
	/** The curr project. */
	private Project currProject; 
	
	/** The refs string. */
	private String refsString;
	
	/** The refs object. */
	private References refsObject;
	
	/**
	 * The constructor of this class.
	 *
	 * @param project the project
	 */
	public AgentConfiguration(Project project) {
		currProject = project;
	}
	
	/**
	 * This method will return the references of the start arguments as
	 * enumerated and ordered String-Vector.
	 *
	 * @param agentReference the agent reference
	 * @return Vector<String> enumerated and ordered String-Vector of the start arguments
	 */
	public Vector<String> getListData(String agentReference) {
		if ( agentReference != null ) {
			refsString = this.get(agentReference);
			refsObject = new References(refsString);
			return refsObject.getVectorNumbered();
		}
		else {
			return null;
		}
	}
	
	/**
	 * This method will return the references of the start arguments as
	 * enumerated TreeMap<Integer, String>, where the Integer value keeps
	 * the position of a start argument and the String the reference to
	 * the start arguments.
	 *
	 * @param agentReference the reference to the agent class
	 * @return The enumerated list of start arguments
	 */
	public TreeMap<Integer, String> getReferencesAsTreeMap(String agentReference) {
		if ( agentReference != null ) {
			refsString = this.get( agentReference );
			refsObject = new References( refsString );
			return refsObject.getReferencesTreeMap();
		}
		else {
			return null;
		}	
	}
	
	/**
	 * This method allows to add an ontology-class-reference to a specified agent.
	 *
	 * @param agentClassReference the agent class reference
	 * @param ontoClassReference the onto class reference
	 */
	public void addReference(String agentClassReference, String ontoClassReference) {
		if ( agentClassReference != null && ontoClassReference != null ){
			refsString = this.get( agentClassReference );
			refsObject = new References( refsString );
			refsObject.add( ontoClassReference );
			this.update(agentClassReference, refsObject.toString() );
		}
	}
	
	/**
	 * This method allows to mask an ontology-class-reference with a different name or identifier.
	 *
	 * @param agentClassReference The reference to the agent
	 * @param ontoClassReferenceIndex The index of the start argument we're working on
	 * @param newStringMask The new identifier string for the start argument
	 */
	public void setReferenceMask(String agentClassReference, int ontoClassReferenceIndex, String newStringMask) {
		if ( agentClassReference != null ){
			refsString = this.get(agentClassReference);
			refsObject = new References(refsString);
			refsObject.setMask(ontoClassReferenceIndex, newStringMask);
			this.update(agentClassReference, refsObject.toString() );
		}
	}
	
	/**
	 * This method allows to remove an ontology-class-reference from a specified agent
	 * by using a class reference to a part of an ontology.
	 *
	 * @param agentClassReference the agent class reference
	 * @param ontoClassReference the onto class reference
	 */
	public void removeReference(String agentClassReference, String ontoClassReference) {
		if ( agentClassReference != null && ontoClassReference != null ){
			refsString = this.get( agentClassReference );
			refsObject = new References( refsString );
			refsObject.remove( ontoClassReference );
			this.update(agentClassReference, refsObject.toString() );
		}
	}
	
	/**
	 * This method allows to remove an ontology-class-reference from a specified agent
	 * by using position number of the class reference.
	 *
	 * @param agentClassReference the agent class reference
	 * @param positionOfReference the position of reference
	 */
	public void removeReference(String agentClassReference, Integer positionOfReference) {
		if ( agentClassReference != null && positionOfReference != null ){
			refsString = this.get( agentClassReference );
			refsObject = new References( refsString );
			refsObject.remove( positionOfReference );
			this.update(agentClassReference, refsObject.toString() );
		}
	}
	
	/**
	 * This method will remove all ontology-class-reference from the specified agent.
	 * All start arguments for the agents will be removed
	 *
	 * @param agentClassReference the agent class reference
	 */
	public void removeReferencesComplete(String agentClassReference) {
		if ( agentClassReference!= null ) {
			this.remove(agentClassReference);	
			currProject.updateAgentReferences();
		}
	}
	
	/**
	 * This method will update the current 'value' of 'key', means the
	 * ontology configuration of an agent can be changed directly.
	 *
	 * @param agentsReference the agents reference
	 * @param ontologyReferences the ontology references
	 */
	private void update(String agentsReference, String ontologyReferences) {
		this.remove(agentsReference);
		if ( ontologyReferences!=null ) {
			this.put(agentsReference, ontologyReferences );	
		}
		currProject.updateAgentReferences();
	}
	
	/**
	 * This method can be used in order to change the order of the ontology-start arguments
	 * of an agent. So position 1 can be changed to 2 and so on.
	 *
	 * @param agentReference the agent reference
	 * @param agentConfig The whole configuration of the start arguments as a string
	 * @param agentConfigPos Which start argument should change its position? (not Index)
	 * @param direction +1 or -1
	 * @return successful changed or not?
	 */
	public boolean movePosition(String agentReference, String agentConfig, Integer agentConfigPos, Integer direction ) {
		if ( agentReference != null && agentConfig != null && direction != null ){
			refsString = this.get( agentReference );
			refsObject = new References(refsString);
			refsObject.moveReference(agentConfig, agentConfigPos, direction);
			this.update(agentReference, refsObject.toString() );
			return true;
		} else {
			return false;	
		}
	}

	/**
	 * This is just a debugging method in order to just print out the current configuration.
	 */
	public void printAll2Sysout() {
		
		Vector<String> v = new Vector<String>( this.keySet() );
		Collections.sort(v);
		Iterator<String> it = v.iterator();
		while (it.hasNext()) {
			String agentClass = it.next();
			System.out.println( "Agenten: " + agentClass );
			System.out.println( "References: " + this.get( agentClass ) );
		}
		
	}
	
	// ------------------------------------------------------------------------
	// --- Start Sub-Class ----------------------------------------------------
	// ------------------------------------------------------------------------
	/**
	 * This internal class is used in order to organise the handling of the 
	 * configured ontology-start arguments. 
	 * 
	 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
	 */
	private class References {
		
		/** Organises the references in a list in order to access a reference by its String representation. */
		private TreeMap<String, Integer> refTM;
		
		/** Organises the references in a list in order to access a reference by its position. */
		private TreeMap<Integer, String> refMT;
		
		/**
		 * Constructor of this class.
		 *
		 * @param refString The ontology references as a String separated by '|'
		 */
		public References(String refString) {
			// --- Object und die TreeMaps füllen ------------------------
			refTM = new TreeMap<String, Integer>();
			refMT = new TreeMap<Integer, String>();
			
			if ( !(refString == null) ) {
				String[] refArr = refString.split("\\|");			
				for (int i=0; i<refArr.length; i++) {			
					refMT.put(i+1, refArr[i]);
					refTM.put(refArr[i], i+1);
				}			
			}
		}
		
		/**
		 * This method can be used in order to mask an ontology reference with a different identifier.
		 *
		 * @param ontoClassReferenceIndex the onto class reference index
		 * @param newStringMask the new identifier to set
		 */
		public void setMask(int ontoClassReferenceIndex, String newStringMask) {
			
			String ontoRef 		= this.refMT.get(ontoClassReferenceIndex+1);
			String ontoRefBulk 	= null;
			String ontoRefNew 	= null;
			
			if (ontoRef.contains("]")==true) {
				int cutPos = ontoRef.indexOf("]");
				ontoRefBulk = ontoRef.substring(cutPos+1).trim();
			} else {
				ontoRefBulk = ontoRef;
			}
			
			if (newStringMask!=null && newStringMask.equals("")==false) {
				ontoRefNew = "[" + newStringMask + "] " + ontoRefBulk;	
			} else {
				ontoRefNew = ontoRefBulk;
			}
			
			refTM.remove(ontoRef);
			refTM.put(ontoRefNew, ontoClassReferenceIndex+1);
			refMT.put(ontoClassReferenceIndex+1,ontoRefNew);			
			
		}
		
		/**
		 * Can be used in order to get the whole list of references and its positions.
		 *
		 * @return list with position and its class references
		 */
		public TreeMap<Integer,String> getReferencesTreeMap() {
			return refMT;
		}
		/**
		 * Here the list of ontology references will be returned as Vector.
		 * Every entry consists of the position number and the associated 
		 * ontology reference (e.g. '1: myProject.myOntolog.myOntologyClass').
		 *  
		 * @return numbered list of the start arguments 
		 */
		public Vector<String> getVectorNumbered() {
			Vector<Integer> v = new Vector<Integer>( refMT.keySet() );
			Vector<String> out = new Vector<String>();
			Collections.sort(v);
			Iterator<Integer> it = v.iterator();
			while (it.hasNext()) {
				Integer key = it.next();
				out.add(key + ": " +  refMT.get(key));
			}
			return out;
		}
		
		/**
		 * This method can be used in order to add a new ontology reference
		 * to the set of start arguments.
		 *
		 * @param newReference A class reference to one of your configured ontology's
		 */
		public void add(String newReference) {
			// --- Gibt es die Refernce schon? ---------------------------
			Integer singleRef = refTM.get(newReference);
			if ( singleRef==null || AllowReferenceDuplicates==true ) {
				// --- Falls nicht, dann diese ergänzen ----
				refMT.put(refMT.size()+1, newReference);
				refTM.put(newReference, refTM.size()+1);
			}
		}
		
		/**
		 * This method can be used in order to remove an ontology reference
		 * from the set of start arguments.
		 *
		 * @param reference The class reference to remove
		 */
		public void remove(String reference) {
		
			Vector<Integer> v = new Vector<Integer>( refMT.keySet() );
			Collections.sort(v);
			Iterator<Integer> it = v.iterator();
			while (it.hasNext()) {
				Integer currKey = it.next();
				String currRef = refMT.get(currKey);
				if ( currRef.equalsIgnoreCase(reference) ) {
					this.remove(currKey);
					break;
				}
			}
		}
		
		/**
		 * This method can be used in order to remove an ontology reference
		 * from the set of start arguments.
		 *
		 * @param removePosition The position on which a start argument can be removed
		 */
		public void remove(Integer removePosition) {
			refMT.remove( removePosition );
		}
		/**
		 * This method can be used in order to change the order of the ontology-start arguments
		 * of an agent. So position 1 can be changed to 2 and so on.
		 *  
		 * @param reference The reference to be moved 
		 * @param oldPos The position of the reference  
		 * @param direction The direction in which the element shall move ( +1 | -1 ) 
		 */
		public void moveReference(String reference, Integer oldPos, Integer direction){
			
			Integer newPos = oldPos + direction;
			if ( refMT.size() > 1 ) {
		
				Vector<Integer> v = new Vector<Integer>( refMT.keySet() );
				// --- Sortierreihenfolge je nach Verschieberichtung -----
				if ( direction > 0 ) {
					Collections.sort(v);
				} else {
					Comparator<Integer> comparator = Collections.<Integer>reverseOrder(); 
					Collections.sort( v, comparator ); 
				}
				Iterator<Integer> it = v.iterator();
				while (it.hasNext()) {
					
					Integer currPos = it.next();
					String currRef = refMT.get(currPos);
					// --- the only way is up ------------
					if ( currPos == oldPos && it.hasNext() ) {
						// --- alten Eintrag entfernen ---
						refMT.remove(oldPos);
						// --- nächsten Eintrag holen ----
						currPos = it.next();
						currRef = refMT.get(currPos);
						// --- ebenfalls entfernen -------
						refMT.remove(currPos);
						// --- und an alte Position ------ 
						refMT.put(oldPos, currRef);
						// --- Tausch vollenden ----------
						refMT.put(newPos, reference);
					}
					
				} // -- End While --
			} // -- End if --
		}
		
		/**
		 * returns the current start-argument configuration as a String
		 * as it will be stored in the Agent.GUI configuration file
		 * (e.g. '/yourProjectFolder/agentgui.xml')
		 *
		 * @return the string
		 */
		public String toString() {
			// --- Gibt den Gesamt-String der References zurück ----------
			String refString = "";
			Vector<Integer> v = new Vector<Integer>(refMT.keySet());
			Collections.sort(v);
			Iterator<Integer> it = v.iterator();
			while (it.hasNext()) {
				if ( refString.equalsIgnoreCase("") ) {
					refString = refMT.get(it.next()); 
				} else {
					refString = refString + "|" + refMT.get(it.next());					
				}
			}
			if ( refString.equalsIgnoreCase("") ) {
				return null;
			} else {
				return refString;	
			}
		}
	}
	// ------------------------------------------------------------------------	
	// --- End Sub-Class ------------------------------------------------------
	// ------------------------------------------------------------------------

}
