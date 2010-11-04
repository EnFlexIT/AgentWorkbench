package agentgui.core.ontologies;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


import agentgui.core.application.Application;
import agentgui.core.reflection.ReflectClassFiles;

public class OntologyClassTree extends DefaultTreeModel implements Serializable {

	/**
	 * This class represents the TreeModel for the Ontology of
	 * the current Project 
	 */
	private static final long serialVersionUID = 1L;

	private OntologyClass currOntoClass;
	private DefaultMutableTreeNode CurrentNode;
	private DefaultMutableTreeNode ParentNode;
	private DefaultMutableTreeNode RootNode;
	private DefaultMutableTreeNode ConceptNode;
	private DefaultMutableTreeNode AidNode;
	private DefaultMutableTreeNode AActionNode;
	private DefaultMutableTreeNode PredicateNode;
	
	private String SearchIN;
	
	// --------------------------------------------------------------------------
	// --- "jade.content.onto.Ontology" - Basisklasse der Ontologie			  ---
	// --- "jade.content.Concept"		- 1. mögliches Interface einer Klasse ---
	// --- "jade.content.AgentAction"	- 2. mögliches Interface einer Klasse ---
	// --------------------------------------------------------------------------
	private final static String BaseClassOnto = "jade.content.onto.Ontology";
	private final static String BaseClassConcept = "jade.content.Concept";
	private final static String BaseClassAID = "jade.core.AID";
	private final static String BaseClassPredicate = "jade.content.Predicate";
	private final static String BaseClassAAction = "jade.content.AgentAction";
	private final static String BaseClassObject = "java.lang.Object";
	// --------------------------------------------------------------------------
	
	public OntologyClassTree(DefaultMutableTreeNode root, OntologyClass ontoClass, String ontologieSourcePackage) {
		super(root);
		currOntoClass = ontoClass;
		
		String logMsgOnto = "";
		String logMsgTitleOnto = "Ontology - Error !";
		int baseClassOntoCount = 0; 
		boolean logMsgShow = false;
		
		// --- Suchverzeichnis festlegen ----------------------
		SearchIN = ontologieSourcePackage;
		// --- Basisstruktur erstellen -------------------------
		RootNode = root;
		
		// --- Den Knoten 'Concept' hinzufügen -----------------
		ConceptNode = new DefaultMutableTreeNode( new OntologyClassTreeObject(currOntoClass, "Concept") );
		RootNode.add( ConceptNode );
		
		// --- Den Knoten AID 'Agent-Identifier' hinzufügen ----
		try {
			Class<?> AIDClass = Class.forName(BaseClassAID);
			AidNode = new DefaultMutableTreeNode( new OntologyClassTreeObject(currOntoClass, AIDClass) );			
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			System.out.println( "Could not find AID-Class !!!" );
			AidNode = new DefaultMutableTreeNode( new OntologyClassTreeObject(currOntoClass, "AID") );
		}
		ConceptNode.add( AidNode );
		
		// --- Den Knoten AgentAction hinzufügen ---------------
		AActionNode = new DefaultMutableTreeNode( new OntologyClassTreeObject(currOntoClass, "AgentAction") );
		ConceptNode.add( AActionNode );		
		
		// --- Den Knoten Predicate hinzufügen -----------------
		PredicateNode = new DefaultMutableTreeNode( new OntologyClassTreeObject(currOntoClass, "Predicate") );
		RootNode.add( PredicateNode );
		
		
		// --- Auslesen der class-Files ------------------------
		ArrayList<String> ProOnto = new ReflectClassFiles(SearchIN);

		// --- Klassen untersuchen -----------------------------
		Class<?> Cla = null;
		Class<?>[] ClaInt = null;
		Class<?> ClaPare = null;
		String ClaRef = null;
		String ClaIntName, ClaPareName;
		boolean ClaIsBaseOnto, ClaIsConcept, ClaIsAAction, ClaIsAID, ClaIsPredicate;
		int ClLiIndex = 0, LastClLiIndexResetAtSize = -1;
		
		while ( ProOnto.size() > 0 ) {
	    	// --- Referenz auf die Klasse ---------------------
	    	if (ClLiIndex > ProOnto.size()-1 ) {
	    		ClLiIndex=0;
	    	}
			ClaRef = ProOnto.get(ClLiIndex);
	    	//System.out.println( "Größe: " + ProOnto.size() + " - " + ClLiIndex + " => " + ClaRef );
	        try {
	        	// ---------------------------------------------
	        	// --- Klassen anfassen ------------------------	        	
	        	Cla = Class.forName( ClaRef );
	        	// ------------------------------------------------------------
        		// --- Die aktuelle Klasse ist nichts verbotenes --------------	        		
	        	// ------------------------------------------------------------
	        	if ( Cla.isInterface() == true ) {
	        		ProOnto.remove(ClaRef);
	        	} else {
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
			    	ClaIsPredicate = false;
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
			    			} else if ( ClaIntName == BaseClassPredicate ) {
			    				ClaIsPredicate = true;
			    				break;
			    			}
			    		}
		        	} else {
		        		// --- Found: Child-Object ----------------
		        	}
		        	
			    	// ---------------------------------------------
		        	// --- UserObject für Knoten erstellen --------
			    	OntologyClassTreeObject userObject = new OntologyClassTreeObject(currOntoClass, Cla); 
			    	userObject.setIsConcept(ClaIsConcept);
			    	userObject.setIsAgentAction(ClaIsAAction);
			    	userObject.setIsPredicate(ClaIsPredicate);
			    	
			    	// ---------------------------------------------
		        	// --- Aktuelle Klasse in Tree darstellen ------
		        	CurrentNode = new DefaultMutableTreeNode( userObject );
		        	if ( ClaIsBaseOnto == true ) {
		        		// --- ggf. die Klasse 'AgentGUIProjectOntology' ignorieren? ---
		        		if (Cla.getName().toLowerCase().endsWith(Application.RunInfo.getFileNameProjectOntology().toLowerCase())==false) {
			        		baseClassOntoCount++;
		        			logMsgOnto += "Found " + baseClassOntoCount + ". Class for Ontology which inherites from '" + ClaPareName + "':\n";
		        			logMsgOnto += "=> " + Cla.getName() + "\n";
			        		if ( baseClassOntoCount>1 ) {
			        			logMsgShow = true;		        			
			        		} else {
				        		// --- Vokabular der Ontologie auslesen ----
				        		// --- und im Project-Objekt bereitstellen -
				        		currOntoClass.setOntologyVocabulary(Cla);
				        		currOntoClass.setOntologyName(currOntoClass.getOntologyWord("ONTOLOGY_NAME"));
				        		currOntoClass.setOntologyMainClass(Cla.getName());
				        		// --- Root-Object neu belegen -------------
				        		OntologyClassTreeObject OCTO = new OntologyClassTreeObject(currOntoClass, Cla);
				        		OCTO.setObjectTitle(currOntoClass.getOntologyName());
				        		RootNode.setUserObject(OCTO);  
			        		}
		        		}
		        		ProOnto.remove(ClaRef);
		        	} else if ( ClaIsConcept == true ) {
			    		ConceptNode.add( CurrentNode );
			    		ProOnto.remove(ClaRef);
		        	} else if ( ClaIsAAction == true ){
		        		AActionNode.add( CurrentNode );
		        		ProOnto.remove(ClaRef);
		        	} else if ( ClaIsAID == true ){
		        		AidNode.add( CurrentNode );
		        		ProOnto.remove(ClaRef);
		        	} else if ( ClaIsPredicate == true ){
		        		PredicateNode.add( CurrentNode );
		        		ProOnto.remove(ClaRef);
		        	} else {
		        		// --- An den entsprechenden Knoten hängen -	        		
		        		ParentNode = getTreeNode( ClaPareName );
		        		if ( !(ParentNode == null) ) {
		        			ParentNode.add( CurrentNode );
		        			ProOnto.remove(ClaRef);
		        		} else {
		        			// Falls kein Knoten gefunden wurde, ist 
		        			// dieser ggf. noch nicht erstellt worden
		        			// => Hinten einsortieren 
		        			ProOnto.remove( ClaRef );
		        			ProOnto.add( ClaRef );	        	
		        			
		        			ClLiIndex++;
		        			if ( ClLiIndex>0 && ProOnto.size()==ClLiIndex ) {
		        				ClLiIndex = 0;
		        				if (ProOnto.size()==LastClLiIndexResetAtSize) {
		        					break;
		        				}		        				
		        				LastClLiIndexResetAtSize = ProOnto.size();
		        			}
		        		}
		        		// -----------------------------------------
		        	}   	        		
	        	}
	        	// ------------------------------------------------------------
        		// --- Die aktuelle Klasse ist nichts verbotenes --------------
	        	// ------------------------------------------------------------	        		
			} catch (ClassNotFoundException ErrCl) {
				// --- Fehlerfall ------------------------------ 
				ErrCl.printStackTrace();
			}
	      } // --- end while ---		
	    
		if ( logMsgShow == true ) {
			// --- If an error occures, show the message ----------------------	
			String MsgHead = Application.RunInfo.AppTitel() + ": " +  logMsgTitleOnto;
			String MsgText = ontologieSourcePackage + ":\n" + logMsgOnto;			

			// --- Send Message to user of AgentGUI --------------------------- 
			JOptionPane.showInternalMessageDialog( Application.MainWindow.getContentPane(), MsgText, MsgHead, JOptionPane.ERROR_MESSAGE);
			
			// --- Add the Error to the current ontology class ----------------
			currOntoClass.ontologyErrorStack += MsgText;
		}
		
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
