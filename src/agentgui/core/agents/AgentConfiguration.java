/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://sourceforge.net/projects/agentgui/
 * http://www.dawis.wiwi.uni-due.de/ 
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
package agentgui.core.agents;

import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import agentgui.core.application.Project;

/**
 * This Class represents the mappings of all project agents to a specific starting 
 * configuration, given through elements of the current project ontology's.
 * 
 * In other words: Every agent can be configured with a set of start arguments
 * (in JADE this can be done by an object array - Object[]).  
 * Now the specific parts of this object array can be selected out of one
 * ontology, which is defined in this project. 
 *  
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class AgentConfiguration extends Hashtable<String, String> {
	
	private static final long serialVersionUID = -1826500137791805011L;

	private boolean AllowReferenceDuplicates = true;
	
	private Project currProject; 
	private String refsString;
	private References refsObject;
	
	/**
	 * The constructor of this class
	 * @param project
	 */
	public AgentConfiguration(Project project) {
		currProject = project;
	}
	
	/**
	 * This method will return the references of the start arguments as
	 * enumerated and ordered String-Vector   
	 * @param agentReference
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
	 * the start arguments      
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
	 * This method allows to add an ontology-class-reference to a specified agent
	 * 
	 * @param agentClassReference
	 * @param ontoClassReference
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
	 * This method allows to remove an ontology-class-reference from a specified agent
	 * by using a class reference to a part of an ontology
	 * 
	 * @param agentClassReference
	 * @param ontoClassReference
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
	 * by using position number of the class reference
	 * 
	 * @param agentClassReference
	 * @param positionOfReference
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
	 * @param agentClassReference
	 */
	public void removeReferencesComplete(String agentClassReference) {
		if ( agentClassReference!= null ) {
			this.remove(agentClassReference);	
			currProject.updateAgentReferences();
		}
	}
	/**
	 * This method will update the current 'value' of 'key'
	 *  
	 * @param key
	 * @param value
	 */
	private void update(String key, String value) {
		this.remove(key);
		if ( value!=null ) {
			this.put(key, value );	
		}
		currProject.updateAgentReferences();
	}
	public boolean movePosition( String agentReference, String agentConfig, Integer agentConfigPos, Integer direction ) {
		if ( agentReference != null && agentConfig != null && direction != null ){
			refsString = this.get( agentReference );
			refsObject = new References( refsString );
			refsObject.moveReference(agentConfig, agentConfigPos, direction);
			this.update(agentReference, refsObject.toString() );
			return true;
		} else {
			return false;	
		}
	}
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
	public class References {
		
		private TreeMap<String, Integer> refTM;
		private TreeMap<Integer, String> refMT;
		
		public References( String refString ) {
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
		public TreeMap<Integer,String> getReferencesTreeMap() {
			return refMT;
		}
		public Vector<String> getVectorNumbered() {
			Vector<Integer> v = new Vector<Integer>( refMT.keySet() );
			Vector<String> out = new Vector<String>();
			Collections.sort(v);
			Iterator<Integer> it = v.iterator();
			while (it.hasNext()) {
				Integer key = it.next();
				out.add( key + ": " +  refMT.get(key) );
			}
			return out;
		}
		public void add( String newReference ) {
			// --- Gibt es die Refernce schon? ---------------------------
			Integer singleRef = refTM.get(newReference);
			if ( singleRef==null || AllowReferenceDuplicates==true ) {
				// --- Falls nicht, dann diese ergänzen ----
				refMT.put(refMT.size()+1, newReference);
				refTM.put(newReference, refTM.size()+1);
			}
		}
		public void remove( String reference ) {
		
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
		public void remove( Integer removePosition ) {
			refMT.remove( removePosition );
		}
		public void moveReference( String reference, Integer oldPos, Integer direction ){
			
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
		public String toString() {
			// --- Gibt den Gesamt-String der References zurück ----------
			String refString = "";
			Vector<Integer> v = new Vector<Integer>( refMT.keySet() );
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
