package mas.agents;

import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import application.Project;

public class AgentConfiguration extends Hashtable<String, String> {
	
	/**
	 * This Class represent the mappings of all project agents 
	 * to a specific starting configuration, given by a part of 
	 * the current project ontology  
	 */
	private static final long serialVersionUID = 1L;
	private static final boolean AllowReferenceDuplicates = true;
	
	private Project currProject; 
	private String refsString;
	private References refsObject;
	
	
	public AgentConfiguration(Project project) {
		currProject = project;
	}
	public Vector<String> getListData( String agentReference ) {
		if ( agentReference != null ) {
			refsString = this.get( agentReference );
			refsObject = new References( refsString );
			return refsObject.getVectorNumbered();
		}
		else {
			return null;
		}
	}
	public void addReference( String agentReference, String ontoReference ) {
		if ( agentReference != null && ontoReference != null ){
			refsString = this.get( agentReference );
			refsObject = new References( refsString );
			refsObject.add( ontoReference );
			this.update(agentReference, refsObject.toString() );
		}
	}
	public void removeReference( String agentReference, String ontoReference ) {
		if ( agentReference != null && ontoReference != null ){
			refsString = this.get( agentReference );
			refsObject = new References( refsString );
			refsObject.remove( ontoReference );
			this.update(agentReference, refsObject.toString() );
		}
	}
	public void removeReference( String agentReference, Integer positionReference ) {
		if ( agentReference != null && positionReference != null ){
			refsString = this.get( agentReference );
			refsObject = new References( refsString );
			refsObject.remove( positionReference );
			this.update(agentReference, refsObject.toString() );
		}
	}
	public void removeReferencesComplete( String agentReference ) {
		if ( agentReference!= null ) {
			this.remove(agentReference);	
			currProject.updateAgentReferences();
		}
	}
	private void update( String hashKey, String hashValue ) {
		this.remove(hashKey);
		if ( hashValue!=null ) {
			this.put( hashKey, hashValue );	
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
