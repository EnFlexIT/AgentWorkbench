package mas.onto;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import application.Project;
import application.reflection.ReflectClassFiles;

public class OntologyClassTree extends DefaultTreeModel implements Serializable {

	/**
	 * This class represents the TreeModel for the Ontology of
	 * the current Project 
	 */
	private static final long serialVersionUID = 1L;

	private Project CurrProject;
	private DefaultMutableTreeNode RootNode;
	private DefaultMutableTreeNode CurrentNode;
	private DefaultMutableTreeNode ParentNode;
	private DefaultMutableTreeNode ConceptNode;
	private DefaultMutableTreeNode AidNode;
	private DefaultMutableTreeNode AActionNode;
	
	private String SearchIN;
	
	// --------------------------------------------------------------------------
	// --- "jade.content.onto.Ontology" - Basisklasse der Ontologie			  ---
	// --- "jade.content.Concept"		- 1. mögliches Interface einer Klasse ---
	// --- "jade.content.AgentAction"	- 2. mögliches Interface einer Klasse ---
	// --------------------------------------------------------------------------
	private final static String BaseClassOnto = "jade.content.onto.Ontology";
	private final static String BaseClassConcept = "jade.content.Concept";
	private final static String BaseClassAID = "jade.core.AID";
	private final static String BaseClassAAction = "jade.content.AgentAction";
	private final static String BaseClassObject = "java.lang.Object";
	// --------------------------------------------------------------------------
	
	public OntologyClassTree(DefaultMutableTreeNode root, Project CurrPro) {
		super(root);		
		CurrProject = CurrPro;
		// --- Basisstruktur erstellen -------------------------
		RootNode = root;
		
		// --- Den Knoten 'Concept' hinzufügen -----------------
		ConceptNode = new DefaultMutableTreeNode( new OntologyClassTreeObject(CurrPro.Ontology, "Concept") );
		RootNode.add( ConceptNode );		

		// --- Den Knoten AID 'Agent-Identifier' hinzufügen ----
		try {
			Class<?> AIDClass = Class.forName(BaseClassAID);
			AidNode = new DefaultMutableTreeNode( new OntologyClassTreeObject(CurrPro.Ontology, AIDClass) );			
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			System.out.println( "Could not find AID-Class !!!" );
			AidNode = new DefaultMutableTreeNode( new OntologyClassTreeObject(CurrPro.Ontology, "AID") );
		}
		ConceptNode.add( AidNode );
		
		// --- Den Knoten AgentAction hinzufügen --------------
		AActionNode = new DefaultMutableTreeNode( new OntologyClassTreeObject(CurrPro.Ontology, "AgentAction") );
		ConceptNode.add( AActionNode );		
		
		// --- Suchverzeichnis festlegen ----------------------
		SearchIN = CurrPro.getProjectFolder()+".ontology";
		
		// --- Auslesen der class-Files ------------------------
		ReflectClassFiles ProOnto = new ReflectClassFiles(SearchIN); 		
		Iterator<String> ClLi = ProOnto.listIterator();
		
		// --- Klassen untersuchen -----------------------------
		Class<?> Cla = null;
		Class<?>[] ClaInt = null;
		Class<?> ClaPare = null;
		String ClaRef = null, ClaRefPrev = null;
		String ClaIntName, ClaPareName;
		boolean ClaIsBaseOnto, ClaIsConcept, ClaIsAAction, ClaIsAID;
		int ClLiCount = 0, ClLiIndex = 0;
		
	    while ( ClLi.hasNext() ) {
	    	// --- Zähler --------------------------------------
	    	ClLiCount++;
	    	ClLiIndex = ClLiCount-1 ;
	    	// --- Referenz auf die Klasse ---------------------
	    	ClaRefPrev = ClaRef ;
	    	ClaRef = ClLi.next();
	    	
	        try {
	        	// ---------------------------------------------
	        	// --- Klassen anfassen ------------------------	        	
	        	Cla = Class.forName( ClaRef );
	        	// --- Superclass ------------------------------
	        	ClaPare = Cla.getSuperclass();
	        	ClaPareName = ClaPare.getName();
	        	// --- Interfaces der Klasse ermitteln ---------
	        	ClaInt = Cla.getInterfaces();
	        	// --- Default ---------------------------------
	        	ClaIsBaseOnto = false;
		    	ClaIsConcept = false;
		    	ClaIsAAction = false;
		    	ClaIsAID = false;
	        	// --- Parent-Class untersuchen ----------------
		    	if ( ClaPareName == BaseClassOnto ) {
	        		// --- Found: BaseClass of Ontology ----   
	        		ClaIsBaseOnto = true;	        		
    			} else if ( ClaPareName == BaseClassAID ) {
    				// --- Found: BaseClass of AID ----------
    				ClaIsAID = true;
		    	} else if ( ClaPareName == BaseClassObject ) {
	        		// --- Found: Normal Object -------------
	        		// --- Serach Interfaces ----------------
	        		// --- => Concept, AID or AgentAction? --
	        		for (int i = 0; i < ClaInt.length; i++) {
		    			ClaIntName = ClaInt[i].getName();
		    			if ( ClaIntName == BaseClassConcept ) {
		    				ClaIsConcept = true;
		    				break;
		    			} else if ( ClaIntName == BaseClassAAction ) {
		    				ClaIsAAction = true;
		    				break;
		    			}
		    		}
	        	} else {
	        		// --- Found: Chld-Object ----------------
	        	}
	        	
	        	// ---------------------------------------------
	        	// --- Aktuelle Klasse in Tree darstellen ------
	        	CurrentNode = new DefaultMutableTreeNode( new OntologyClassTreeObject(CurrPro.Ontology, Cla) );
	        	if ( ClaIsBaseOnto == true ) {
	        		// --- Vokabular der Ontologie auslesen ----
	        		// --- und im Project-Objekt bereitstellen -
	        		CurrProject.Ontology.setOntologyVocabulary( Cla );
	        		// --- Root-Object neu belegen -------------
	        		OntologyClassTreeObject OCTO = new OntologyClassTreeObject(CurrPro.Ontology, Cla);
	        		OCTO.setObjectTitle( CurrProject.Ontology.getOntologyWord("ONTOLOGY_NAME") );
	        		RootNode.setUserObject( OCTO );   
	        	} else if ( ClaIsConcept == true ) {
		    		ConceptNode.add( CurrentNode );	
	        	} else if ( ClaIsAAction == true ){
	        		AActionNode.add( CurrentNode );
	        	} else if ( ClaIsAID == true ){
	        		AidNode.add( CurrentNode );
	        	} else {
	        		// --- An den entsprechenden Knoten hängen -	        		
	        		ParentNode = getTreeNode( ClaPareName );
	        		if ( !(ParentNode == null) ) {
	        			ParentNode.add( CurrentNode );
	        		} else {
	        			// Falls kein Knoten gefunden wurde, ist 
	        			// dieser ggf. noch nicht erstellt worden
	        			// => Hinten einsortieren 
	        			ProOnto.remove( ClLiIndex );
	        			ProOnto.add( ClaRef );	        	
	        			
	        			ClLiCount = 0;	// --- Neu hochzählen ---------
	        			ClLi = ProOnto.listIterator();
	        			if ( ClaRefPrev != null ) {
	        				//------------------------------------------
	        				// Zur vorherigen Refernce laufen ...
		        			while ( ClLi.hasNext() ) {
		        				ClLiCount++;
		        				ClaRef = ClLi.next();		        				
		        				if ( ClaRef.equalsIgnoreCase(ClaRefPrev)) {		        					
		        					break;
		        				}
		        			}
	        				//------------------------------------------
	        			}
	        		}
	        	}      	
			} catch (ClassNotFoundException ErrCl) {
				// --- Fehlerfall ------------------------------ 
				ErrCl.printStackTrace();
			}
	      } // --- end while ---		
	    return;
	}
	
	/**
	 * Returns the Tree-Node requested by the Reference 
	 * @param Reference
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DefaultMutableTreeNode getTreeNode( String Reference ) {
		
		DefaultMutableTreeNode NodeFound = null;
		DefaultMutableTreeNode CurrNode = null;
		Reference = Reference.replace( SearchIN + "." , "");
		String CurrNodeText;
		
		for (Enumeration<DefaultMutableTreeNode> e = RootNode.breadthFirstEnumeration(); e.hasMoreElements();) {
			CurrNode = e.nextElement();
			CurrNodeText = CurrNode.getUserObject().toString(); 
			if ( CurrNodeText.equals(Reference) ) {				
				NodeFound = CurrNode;
				break;
			} 
		}
		return NodeFound;
	}
}
