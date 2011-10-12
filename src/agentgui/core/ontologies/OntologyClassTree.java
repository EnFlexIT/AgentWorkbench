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
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


import agentgui.core.application.Application;
import agentgui.core.application.Project;
import agentgui.core.reflection.ReflectClassFiles;

/**
 * This class represents the DefaultTreeModel for a single ontology, used in the current project.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OntologyClassTree extends DefaultTreeModel implements Serializable {

	private static final long serialVersionUID = 719806666309626657L;

	private Project currProject;
	private OntologyClass currOntoClass;

	private DefaultMutableTreeNode currentNode;
	private DefaultMutableTreeNode parentNode;
	private DefaultMutableTreeNode rootNode;
	private DefaultMutableTreeNode conceptNode;
	private DefaultMutableTreeNode aidNode;
	private DefaultMutableTreeNode aActionNode;
	private DefaultMutableTreeNode predicateNode;
	
	private String SearchIN;
	
	// --------------------------------------------------------------------------
	// --- "jade.content.onto.Ontology" - Basisklasse der Ontologie			  ---
	// --- "jade.content.Concept"		- 1. mögliches Interface einer Klasse ---
	// --- "jade.content.AgentAction"	- 2. mögliches Interface einer Klasse ---
	// --------------------------------------------------------------------------
	private final static String BaseClassObject = "java.lang.Object";
	
	private final static String BaseClassOnto = "jade.content.onto.Ontology";
	private final static String BaseClassConcept = "jade.content.Concept";
	private final static String BaseClassPredicate = "jade.content.Predicate";
	private final static String BaseClassAAction = "jade.content.AgentAction";
	public final static String BaseClassAID = "jade.core.AID";
	// --------------------------------------------------------------------------
	

	/**
	 * Constructor of this class
	 * @param project The current Project
	 * @param root The root node where further nodes can be added
	 * @param ontoClass The current instance of OntologyClass
	 * @param ontologieSourcePackage The reference to the package of the ontology 
	 */ 
	public OntologyClassTree(Project project, DefaultMutableTreeNode root, OntologyClass ontoClass, String ontologieSourcePackage) {
		
		super(root);
		this.currProject = project;
		this.currOntoClass = ontoClass;
		this.SearchIN = ontologieSourcePackage;
		this.rootNode = root;
		
		String logMsgOnto = "";
		String logMsgTitleOnto = "Ontology - Error !";
		int baseClassOntoCount = 0; 
		boolean logMsgShow = false;
		
		// --- Den Knoten 'Concept' hinzufügen -----------------
		this.conceptNode = new DefaultMutableTreeNode( new OntologyClassTreeObject(currOntoClass, "Concept") );
		this.rootNode.add( conceptNode );
		
		// --- Den Knoten AID 'Agent-Identifier' hinzufügen ----
		try {
			Class<?> AIDClass = Class.forName(BaseClassAID);
			this.aidNode = new DefaultMutableTreeNode( new OntologyClassTreeObject(currOntoClass, AIDClass) );			
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			System.out.println( "Could not find AID-Class !!!" );
			this.aidNode = new DefaultMutableTreeNode( new OntologyClassTreeObject(currOntoClass, "AID") );
		}
		this.conceptNode.add( aidNode );
		
		// --- Den Knoten AgentAction hinzufügen ---------------
		this.aActionNode = new DefaultMutableTreeNode( new OntologyClassTreeObject(currOntoClass, "AgentAction") );
		this.conceptNode.add( aActionNode );		
		
		// --- Den Knoten Predicate hinzufügen -----------------
		this.predicateNode = new DefaultMutableTreeNode( new OntologyClassTreeObject(currOntoClass, "Predicate") );
		this.rootNode.add( predicateNode );
		

		// --- get the class-files from the package ------------
		ArrayList<String> projectOntologyClassList = new ReflectClassFiles(currProject, SearchIN);
		
		// --- investigate classes -----------------------------
		Class<?> Cla = null;
		Class<?>[] ClaInt = null;
		Class<?> ClaPare = null;
		String ClaRef = null;
		String ClaIntName, ClaPareName;
		boolean ClaIsBaseOnto, ClaIsConcept, ClaIsAAction, ClaIsAID, ClaIsPredicate;
		int ClLiIndex = 0, LastClLiIndexResetAtSize = -1;

		// ---------------------------------------------------------------
		// --- run as long as there is a class in the list ---------------
		// ---------------------------------------------------------------
		while ( projectOntologyClassList.size() > 0 ) {
	    	// --- Referenz auf die Klasse ---------------------
	    	if (ClLiIndex > projectOntologyClassList.size()-1 ) {
	    		ClLiIndex=0;
	    	}
			ClaRef = projectOntologyClassList.get(ClLiIndex);
	    	//System.out.println( "Größe: " + ProOnto.size() + " - " + ClLiIndex + " => " + ClaRef );
	        try {
	        	// ---------------------------------------------
	        	// --- Klassen anfassen ------------------------	        	
	        	Cla = Class.forName( ClaRef );
	        	// ------------------------------------------------------------
        		// --- Die aktuelle Klasse ist nichts verbotenes --------------	        		
	        	// ------------------------------------------------------------
	        	if ( Cla.isInterface() == true ) {
	        		projectOntologyClassList.remove(ClaRef);
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
		        		// --- Search Interfaces ----------------
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
		        	// --- define userObject for the node ----------
			    	OntologyClassTreeObject userObject = new OntologyClassTreeObject(currOntoClass, Cla); 
			    	userObject.setIsConcept(ClaIsConcept);
			    	userObject.setIsAgentAction(ClaIsAAction);
			    	userObject.setIsPredicate(ClaIsPredicate);
			    	
			    	// ---------------------------------------------
		        	// --- show current class in the tree ----------
		        	currentNode = new DefaultMutableTreeNode( userObject );
		        	
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
				        		currOntoClass.setOntologyName(currOntoClass.getVocabularyValue("ONTOLOGY_NAME"));
				        		currOntoClass.setOntologyMainClass(Cla.getName());
				        		// --- Root-Object neu belegen -------------
				        		OntologyClassTreeObject OCTO = new OntologyClassTreeObject(currOntoClass, Cla);
				        		OCTO.setObjectTitle(currOntoClass.getOntologyName());
				        		rootNode.setUserObject(OCTO);  
			        		}
		        		}
		        		projectOntologyClassList.remove(ClaRef);
		        	} else if ( ClaIsConcept == true ) {
			    		conceptNode.add( currentNode );
			    		projectOntologyClassList.remove(ClaRef);
		        	} else if ( ClaIsAAction == true ){
		        		aActionNode.add( currentNode );
		        		projectOntologyClassList.remove(ClaRef);
		        	} else if ( ClaIsAID == true ){
		        		// --- Remind the parent Node ---------------
	        			OntologyClassTreeObject parentOntologyClassTreeObject = (OntologyClassTreeObject) aidNode.getUserObject();
	        			userObject.setParentOntologyClassTreeObject(parentOntologyClassTreeObject);
	        			currentNode.setUserObject(userObject);
	        			// --- Add to the parent node ---------------
	        			aidNode.add( currentNode );
		        		projectOntologyClassList.remove(ClaRef);
		        	} else if ( ClaIsPredicate == true ){
		        		predicateNode.add( currentNode );
		        		projectOntologyClassList.remove(ClaRef);
		        	} else {
		        		// -------------------------------------------
				    	// --- get parent node Object ----------------
				    	parentNode = getTreeNode( ClaPareName );
		        		// --- Add to the appropriated node ----------       		
		        		if ( !(parentNode == null) ) {
		        			// --- Remind the parent Node ------------
		        			OntologyClassTreeObject parentOntologyClassTreeObject = (OntologyClassTreeObject) parentNode.getUserObject();
		        			userObject.setParentOntologyClassTreeObject(parentOntologyClassTreeObject);
		        			currentNode.setUserObject(userObject);
		        			// --- Add to the parent node ------------
		        			parentNode.add( currentNode );
		        			projectOntologyClassList.remove(ClaRef);
		        		} else {
		        			// --------------------------------------
		        			// --- if no node was found, it has maybe 
		        			// --- to be created => queue at the end 
		        			// --------------------------------------
		        			projectOntologyClassList.remove( ClaRef );
		        			projectOntologyClassList.add( ClaRef );	        	
		        			
		        			ClLiIndex++;
		        			if ( ClLiIndex>0 && projectOntologyClassList.size()==ClLiIndex ) {
		        				ClLiIndex = 0;
		        				if (projectOntologyClassList.size()==LastClLiIndexResetAtSize) {
		        					break;
		        				}		        				
		        				LastClLiIndexResetAtSize = projectOntologyClassList.size();
		        			}
		        		}
		        		// ------------------------------------------
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
			// --- If an error occurs, show the message -----------------------	
			String MsgHead = Application.RunInfo.getApplicationTitle() + ": " +  logMsgTitleOnto;
			String MsgText = ontologieSourcePackage + ":\n" + logMsgOnto;			

			// --- Send Message to user of AgentGUI --------------------------- 
			JOptionPane.showInternalMessageDialog( Application.MainWindow.getContentPane(), MsgText, MsgHead, JOptionPane.ERROR_MESSAGE);
			
			// --- Add the Error to the current ontology class ----------------
			currOntoClass.ontologyErrorStack += MsgText;
		}
		
	}
	
	/**
	 * Returns the TreeNode requested by the reference 
	 * @param reference
	 * @return The found DefaultMutableTreeNode
	 */
	@SuppressWarnings("unchecked")
	public DefaultMutableTreeNode getTreeNode(String reference) {
		
		DefaultMutableTreeNode nodeFound = null;
		DefaultMutableTreeNode currNode  = null;
		String currNodeText = null;
		
		reference = reference.replace(SearchIN + "." , "");
		
		for (Enumeration<DefaultMutableTreeNode> e = rootNode.breadthFirstEnumeration(); e.hasMoreElements();) {
			currNode = e.nextElement();
			currNodeText = currNode.getUserObject().toString(); 
			if ( currNodeText.equals(reference) ) {				
				nodeFound = currNode;
				break;
			} 
		}
		return nodeFound;
	}
}
