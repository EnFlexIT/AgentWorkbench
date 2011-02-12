package agentgui.core.ontologies;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;

public class OntologyClassVocabulary extends Hashtable<String, String> implements Serializable {

	/**
	 * Reads the public fields of the ontology base class 
	 * which extends "jade.content.onto.Ontology"
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * Initialise the class, which holds the complete vocabulary
	 * of the current Ontology inside of a Hashtable
	 * @param Clazz
	 */
	public OntologyClassVocabulary (Class<?> Clazz) {
		
		if ( Clazz == null ) return;
		Hashtable<String, String> UnsortedHash = new Hashtable<String, String>();
		
		Field[] publicFields = Clazz.getFields();
		for (int i = 0; i < publicFields.length; i++) {
			
			String fieldName = publicFields[i].getName();
			String fieldValue = null;
			
			try {
				fieldValue = publicFields[i].get( Clazz ).toString();
				UnsortedHash.put(fieldName, fieldValue);
				//System.out.println("Name: " + fieldName + ", Value.: "+fieldValue);								
			} catch (IllegalArgumentException e) {
				//e.printStackTrace();
			} catch (IllegalAccessException e) {
				//e.printStackTrace();
			}
			
			
		}
		this.putAll(UnsortedHash);
		this.addAID_Vocabulary();
	}
	/**
	 * Because there is no class for the AID inside a single Ontology, some
	 * values related to this must be added manually
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
	 * the "slots" / internal variables of the class
	 * @param Node
	 * @return
	 */
	public Hashtable<String, String> getSlots( DefaultMutableTreeNode Node  ) {
		Object UObject = Node.getUserObject();
		OntologyClassTreeObject OCTO = (OntologyClassTreeObject) UObject;		
		return getSlots( OCTO.getOntologySubClass() );
	}
	/**
	 * Filter the Vocabulary for a special Ontology-Class, to get
	 * the "slots" / internal variables of the class
	 * @param Node
	 * @return
	 */
	public Hashtable<String, String> getSlots(Class<?> OntoClass) {
		String Reference = OntoClass.getName();
		String FilterFor = Reference.substring( Reference.lastIndexOf(".")+1 );
		return getSlots(FilterFor);
	}
	/**
	 * Filter the Vocabulary for a special Ontology-Class, to get
	 * the "slots" / internal variables of the class
	 * @param Node
	 * @return
	 */
	public Hashtable<String, String> getSlots( String FilterFor  ) {

		String Key;
		Set<String> set = this.keySet();
	    Iterator<String> itr = set.iterator();
		Hashtable<String, String> ResultHT = new Hashtable<String, String>();
		
		String Prefix = FilterFor.toUpperCase();
		//System.out.println("Suche: " + Prefix);
		
	    // --- Vokabular durchlaufen ------------------------------------------
		while (itr.hasNext()) {
	    	Key = itr.next();
	    	if (Key.startsWith(Prefix)) {
	    		boolean add2Result = false;
	    		if ( Key.length() == Prefix.length() ) {
	    			// --- Treffer, aber es werden nur Slots gesucht ----- 
	    			// --- und nicht die Klasse selbst  ------------------
	    			add2Result = false;
	    		} else {
	    			// --- ggf. ein gleich startendes Wort gefunden ------
	    			if ( Key.startsWith(Prefix + "_") ) {
	    				add2Result = true;
	    			}	    			
	    		}
	    		// --- Bei Treffer anfügen ------------------------------- 
	    		if ( add2Result == true ) {
		    		//System.out.println(Key + ": " + this.get( Key ));
		    		ResultHT.put( Key, this.get(Key) );
	    		}
	    	}	    	
	    }
		return ResultHT;
	}

}
