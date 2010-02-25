package mas.onto;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import application.Project;
import application.reflection.Reflect;

public class OntologyClassTree extends DefaultTreeModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DefaultMutableTreeNode RootNode;
	private DefaultMutableTreeNode CurrentNode;
	private DefaultMutableTreeNode ParentNode;
	private DefaultMutableTreeNode ConceptNode;
	private DefaultMutableTreeNode AActionNode;
	
	private String SearchIN;

	// --------------------------------------------------------------------------
	// --- "jade.content.onto.Ontology" - Basisklasse der Ontologie			  ---
	// --- "jade.content.Concept"		- 1. mögliches Interface einer Klasse ---
	// --- "jade.content.AgentAction"	- 2. mögliches Interface einer Klasse ---
	// --------------------------------------------------------------------------
	private final static String BaseClassOnto = "jade.content.onto.Ontology";
	private final static String BaseClassConcept = "jade.content.Concept";
	private final static String BaseClassAAction = "jade.content.AgentAction";
	private final static String BaseClassObject = "java.lang.Object";
	// --------------------------------------------------------------------------
	
	public OntologyClassTree(DefaultMutableTreeNode root, Project CurrPro) {
		super(root);		
		// --- Basisstruktur erstellen -------------------------
		RootNode = root;
		
		ConceptNode = new DefaultMutableTreeNode( "Concept" );
		RootNode.add( ConceptNode );		

		AActionNode = new DefaultMutableTreeNode( "AgentAction" );
		ConceptNode.add( AActionNode );		
		
		// --- Suchverzeichnis festlegen ----------------------
		SearchIN = CurrPro.getProjectFolder()+".ontology";
		
		// --- Auslesen der class-Files ------------------------
		Reflect ProOnto = new Reflect(SearchIN); 		
		Iterator<String> ClLi = ProOnto.listIterator();
		
		// --- Klassen untersuchen -----------------------------
		Class<?> Cla = null;
		Class<?>[] ClaInt = null;
		Class<?> ClaPare = null;
		String ClaRef = null, ClaRefPrev = null;
		String ClaName, ClaIntName, ClaPareName;
		boolean ClaIsBaseOnto, ClaIsConcept, ClaIsAACtion;
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
	        	ClaName = Cla.getName();	
	        	// --- Superclass ------------------------------
	        	ClaPare = Cla.getSuperclass();
	        	ClaPareName = ClaPare.getName();
	        	// --- Interfaces der Klasse ermitteln ---------
	        	ClaInt = Cla.getInterfaces();
	        	//System.out.println( "=> Name: " + ClaName + " => Super: " + ClaPareName );
	        	// --- Default ---------------------------------
	        	ClaIsBaseOnto = false;
		    	ClaIsConcept = false;
				ClaIsAACtion = false;
	        	// --- Parent-Class untersuchen ----------------
	        	if ( ClaPareName == BaseClassOnto ) {
	        		// --- Found: BaseClass of Ontology ----   
	        		ClaIsBaseOnto = true;	        		
	        	} else if ( ClaPareName == BaseClassObject ) {
	        		// --- Found: Normal Object -------------
	        		// --- Serach Interfaces ----------------
	        		// --- => Concept oder AgentAction? -----
	        		for (int i = 0; i < ClaInt.length; i++) {
		    			ClaIntName = ClaInt[i].getName();
		    			//System.out.println( ClaIntName );
		    			if ( ClaIntName == BaseClassConcept ) {
		    				ClaIsConcept = true;
		    				break;
		    			} else if ( ClaIntName == BaseClassAAction ) {
		    				ClaIsAACtion = true;
		    				break;
		    			}
		    		}
	        	} else {
	        		// --- Found: Chld-Object ----------------
	        	}
	        	
	        	// ---------------------------------------------
	        	// --- Aktuelle Klasse in Tree darstellen ------
	        	CurrentNode = new DefaultMutableTreeNode( new OntologyClassTreeObject(Cla) );
	        	if ( ClaIsBaseOnto == true ) {
	        		// --- Root-Node umbennennen ---------------
	        		RootNode.setUserObject( getClassTextSimple( ClaName ) );
	        	} else if ( ClaIsConcept == true ) {
		    		ConceptNode.add( CurrentNode );	
	        	} else if ( ClaIsAACtion == true ){
	        		AActionNode.add( CurrentNode );
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
	private DefaultMutableTreeNode getTreeNode( String Reference ) {
		
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
	
	/**
	 * Returns the simple class name from a class-reference
	 * @param Reference
	 * @return
	 */
	private String getClassTextSimple( String Reference ) {
		return Reference.substring( Reference.lastIndexOf(".")+1 );
	}

	
}
