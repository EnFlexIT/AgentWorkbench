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
package de.enflexit.common.ontology;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import de.enflexit.common.bundleEvaluation.PackageClasses;
import de.enflexit.common.classLoadService.BaseClassLoadServiceUtility;

/**
 * This class represents the DefaultTreeModel for a single 
 * ontology, which can be used for example in projects.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OntologyClassTree extends DefaultTreeModel implements Serializable {

	private static final long serialVersionUID = 719806666309626657L;
	
	// --------------------------------------------------------------------------
	// --- "jade.content.onto.Ontology" - Base class of the ontology          ---
	// --- "jade.content.Concept"		- 1st possible Interface of a class   ---
	// --- "jade.content.AgentAction"	- 2nd possible Interface of a class   ---
	// --------------------------------------------------------------------------
	private final static String BaseClassObject = "java.lang.Object";
	
	private final static String BaseClassOnto = "jade.content.onto.Ontology";
	private final static String BaseClassConcept = "jade.content.Concept";
	private final static String BaseClassPredicate = "jade.content.Predicate";
	private final static String BaseClassAAction = "jade.content.AgentAction";
	public final static String BaseClassAID = "jade.core.AID";
	// --------------------------------------------------------------------------

	private OntologyClass currOntoClass;
	private String searchInPackage;
	
	private DefaultMutableTreeNode rootNode;
	private DefaultMutableTreeNode conceptNode;
	private DefaultMutableTreeNode aActionNode;
	private DefaultMutableTreeNode aidNode;
	private DefaultMutableTreeNode predicateNode;
	
	private String logMsgOnto = "";
	private String logMsgTitleOnto = "Ontology - Error !";
	private int baseClassOntoCount = 0; 
	private boolean logMsgShow = false;

	
	/**
	 * Constructor of this class
	 * @param root The root node where further nodes can be added
	 * @param ontoClass The current instance of OntologyClass
	 * @param ontologieSourcePackage The reference to the package of the ontology 
	 */ 
	public OntologyClassTree(DefaultMutableTreeNode root, OntologyClass ontoClass, String ontologieSourcePackage) {
		
		super(root);
		this.currOntoClass = ontoClass;
		this.searchInPackage = ontologieSourcePackage;
		this.rootNode = root;
		
		// --- Add the default nodes for an ontology ----------
		this.addDefaultNodes();

		// --- Add the nodes from the given ontology -----------
		this.addOntologyNodes();
	    
		// --- Did we had an error here? -----------------------
		if (logMsgShow==true ) {
			// --- If an error occurs, show the message --------	
			String MsgHead = OntologyVisualisationConfiguration.getApplicationTitle() + ": " +  logMsgTitleOnto;
			String MsgText = ontologieSourcePackage + ":\n" + logMsgOnto;			

			// --- Send Message to user of AgentGUI ------------ 
			JOptionPane.showInternalMessageDialog(OntologyVisualisationConfiguration.getOwnerWindow(), MsgText, MsgHead, JOptionPane.ERROR_MESSAGE);
			
			// --- Add the Error to the current ontology class -
			currOntoClass.ontologyErrorStack += MsgText;
		}
		
	}
	
	/**
	 * Adds the default nodes for 'Concept', 'AID', 'AgentAction' and 'Predicate'.
	 */
	private void addDefaultNodes() {
		
		// --- Add node 'Concept'--------------------------
		this.conceptNode = new DefaultMutableTreeNode( new OntologyClassTreeObject(currOntoClass, "Concept") );
		this.rootNode.add(conceptNode);
		
		// --- Add node AID 'Agent-Identifier' ------------
		try {
			Class<?> AIDClass = BaseClassLoadServiceUtility.forName(BaseClassAID);
			this.aidNode = new DefaultMutableTreeNode(new OntologyClassTreeObject(currOntoClass, AIDClass));			
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			System.out.println( "Could not find AID-Class !!!" );
			this.aidNode = new DefaultMutableTreeNode(new OntologyClassTreeObject(currOntoClass, "AID"));
		}
		this.conceptNode.add(aidNode);
		
		// --- Add node 'AgentAction' ---------------------
		this.aActionNode = new DefaultMutableTreeNode(new OntologyClassTreeObject(currOntoClass, "AgentAction"));
		this.conceptNode.add(aActionNode);		

		// --- Add node 'Predicate' -----------------------
		this.predicateNode = new DefaultMutableTreeNode(new OntologyClassTreeObject(this.currOntoClass, "Predicate"));
		this.rootNode.add(predicateNode);
		
	}
	
	/**
	 * Adds the nodes from the current ontology .
	 */
	private void addOntologyNodes() {
	
		int clazzListIndex = 0; 
		int lastClLiIndexResetAtSize = -1;

		// --- Get the class-files from the package ---------------------------
		ArrayList<String> ontologyClassList = new PackageClasses(this.currOntoClass.getOntologyClass());
		while (ontologyClassList.size()>0) {
	    	
	    	if (clazzListIndex > ontologyClassList.size()-1 ) {
	    		clazzListIndex=0;
	    	}

	    	// ----------------------------------------------------------------
	    	// --- Work on the current class and its description --------------
	    	String currentClass = ontologyClassList.get(clazzListIndex);
	    	OntologyClassTreeObject ontoClassTreeObj = this.getNewTreeNodeUserObject(currentClass);

	    	// ----------------------------------------------------------------
	    	// --- Work on the OntologyClassTreeObject, if available ---------- 
	    	if (ontoClassTreeObj==null) {
				ontologyClassList.remove(currentClass);
			
			} else {
				// --- Add a new node to the ontology tree --------------------
				DefaultMutableTreeNode currentNode = new DefaultMutableTreeNode(ontoClassTreeObj);
	        	if (ontoClassTreeObj.isBaseOntology()==true) {
	        		// --- Ignore 'AgentGUIProjectOntology' ? -----------------
	        		baseClassOntoCount++;
        			logMsgOnto += "Found " + baseClassOntoCount + ". Class for Ontology which inherites from '" + ontoClassTreeObj.getParentOntologySubClass().getName() + "':\n";
        			logMsgOnto += "=> " + ontoClassTreeObj.getClassReference() + "\n";
	        		if (baseClassOntoCount>1) {
	        			logMsgShow = true;		        			
	        		} else {
		        		// --- Read ontology vocabulary -------------------
		        		currOntoClass.setOntologyVocabulary(ontoClassTreeObj.getOntologySubClass());
		        		currOntoClass.setOntologyName(currOntoClass.getVocabularyValue("ONTOLOGY_NAME"));
		        		currOntoClass.setOntologyMainClass(ontoClassTreeObj.getClassReference());
		        		// --- Reset the root object ----------------------
		        		OntologyClassTreeObject octo = new OntologyClassTreeObject(currOntoClass, ontoClassTreeObj.getOntologySubClass());
		        		octo.setObjectTitle(currOntoClass.getOntologyName());
		        		rootNode.setUserObject(octo);  
	        		}
	        		ontologyClassList.remove(currentClass);
	        		
	        	} else if (ontoClassTreeObj.isConcept()==true) {
		    		conceptNode.add(currentNode);
		    		ontologyClassList.remove(currentClass);
		    		
	        	} else if (ontoClassTreeObj.isAgentAction()==true){
	        		aActionNode.add(currentNode);
	        		ontologyClassList.remove(currentClass);
	        		
	        	} else if (ontoClassTreeObj.isAID()==true){
	        		// --- Remind the parent Node -----------------------------
        			OntologyClassTreeObject parentOntologyClassTreeObject = (OntologyClassTreeObject) aidNode.getUserObject();
        			ontoClassTreeObj.setParentOntologyClassTreeObject(parentOntologyClassTreeObject);
        			currentNode.setUserObject(ontoClassTreeObj);
        			// --- Add to the parent node -----------------------------
        			aidNode.add(currentNode);
	        		ontologyClassList.remove(currentClass);
	        		
	        	} else if (ontoClassTreeObj.isPredicate()==true){
	        		predicateNode.add( currentNode );
	        		ontologyClassList.remove(currentClass);
	        		
	        	} else {
			    	// --- Get parent node Object ---------------------------
	        		DefaultMutableTreeNode parentNode = getTreeNode(ontoClassTreeObj.getParentOntologySubClass().getName());
	        		// --- Add to the appropriated parent node --------------       		
	        		if (parentNode!=null) {
	        			// --------------------------------------------------
	        			// --- Remind the parent node -----------------------
	        			// --------------------------------------------------
	        			OntologyClassTreeObject parentOntologyClassTreeObject = (OntologyClassTreeObject) parentNode.getUserObject();
	        			ontoClassTreeObj.setParentOntologyClassTreeObject(parentOntologyClassTreeObject);
	        			currentNode.setUserObject(ontoClassTreeObj);
	        			// --- Add to the parent node -----------------------
	        			parentNode.add( currentNode );
	        			ontologyClassList.remove(currentClass);
	        			
	        		} else {
	        			// --------------------------------------------------
	        			// --- If no parent node was found, it was maybe ----
	        			// --- not yet be created => Queue at the end    ----
	        			// --------------------------------------------------
	        			ontologyClassList.remove(currentClass);
	        			ontologyClassList.add(currentClass);	        	
	        			
	        			clazzListIndex++;
	        			if ( clazzListIndex>0 && ontologyClassList.size()==clazzListIndex ) {
	        				clazzListIndex = 0;
	        				if (ontologyClassList.size()==lastClLiIndexResetAtSize) {
	        					break;
	        				}		        				
	        				lastClLiIndexResetAtSize = ontologyClassList.size();
	        			}
	        		}
	        		// ------------------------------------------------------
	        		// --------------------------------------------------------
    		
	        	}
			}
			
	      } // --- end while ---	
		
	}

	
	/**
	 * Returns a new tree node user object depending on the given class.
	 *
	 * @param className the class name
	 * @return the new tree node user object
	 */
	private OntologyClassTreeObject getNewTreeNodeUserObject(String className) {
		
    	try {
    		// --- Inspect the given class ---------------------
    		Class<?> clazz = BaseClassLoadServiceUtility.forName(className);
    		// --- Return object for Protege 3.3.1 -------------
    		return this.getNewTreeNodeUserObject(clazz);
	    	
    	} catch (ClassNotFoundException | NoClassDefFoundError cnfe) {
    		cnfe.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets the new tree node user object 
	 *
	 * @param clazz the current class
	 * @return the new tree node user object 
	 */
	private OntologyClassTreeObject getNewTreeNodeUserObject(Class<?> clazz) {
	
		Class<?> clazzParent = null;
		Class<?>[] classInterfaces = null;
		String clazzInterfaceName= null;
		
		boolean clazzIsBaseOnto = false;
    	boolean clazzIsConcept = false;
    	boolean clazzIsAction = false;
    	boolean clazzIsAID = false;
    	boolean clazzIsPredicate = false;
    	
    	
    	// --- Remove default package info, if it occurs --    	
    	if (clazz.getSimpleName().equals("package-info")) return null;
    	
		// --- Separate super class -----------------------
    	classInterfaces = clazz.getInterfaces();
    	if (clazz.isInterface()==true) {
    		// --------------------------------------------
    		// --- For Protege 3.4 ------------------------
    		// --------------------------------------------
    		clazzParent = classInterfaces[0];
    		// --- Found: Normal Object --------------------
    		// --- Search Interfaces -----------------------
    		// --- => Concept, AID or AgentAction? ---------
    		for (int i = 0; i < classInterfaces.length; i++) {
    			clazzInterfaceName = classInterfaces[i].getName();
    			if (clazzInterfaceName.equals(BaseClassConcept)) {
    				clazzIsConcept = true;
    				break;
    			} else if (clazzInterfaceName.equals(BaseClassAAction)) {
    				clazzIsAction = true;
    				break;
    			} else if (clazzInterfaceName.equals(BaseClassPredicate)) {
    				clazzIsPredicate = true;
    				break;
    			}
    		}

    	} else {
    		// --------------------------------------------
    		// --- For Protege 3.3.1 ----------------------
    		// --------------------------------------------
    		clazzParent = clazz.getSuperclass();
    		// --- evaluate Parent-Class ---------------
    		if (clazzParent.getName().equals(BaseClassOnto)) {
        		// --- Found: BaseClass of Ontology -------
        		clazzIsBaseOnto = true;	       
        		
    		} else if (clazzParent.getName().equals(BaseClassAID)) {
    			// --- Found: BaseClass of AID ------------
    			clazzIsAID = true;
    			
        	} else if (clazzParent.getName().equals(BaseClassObject)) {
        		// --- Found: Normal Object ---------------
        		// --- Search Interfaces ------------------
        		// --- => Concept, AID or AgentAction? ----
        		for (int i = 0; i < classInterfaces.length; i++) {
        			clazzInterfaceName = classInterfaces[i].getName();
        			if (clazzInterfaceName.equals(BaseClassConcept)) {
        				clazzIsConcept = true;
        				break;
        			} else if (clazzInterfaceName.equals(BaseClassAAction)) {
        				clazzIsAction = true;
        				break;
        			} else if (clazzInterfaceName.equals(BaseClassPredicate)) {
        				clazzIsPredicate = true;
        				break;
        			}
        		}
        	} 
        	
    	}
    	
    	// --- define userObject for the node -------------
    	OntologyClassTreeObject ontoClassTreeUserObject = new OntologyClassTreeObject(this.currOntoClass, clazz); 
    	ontoClassTreeUserObject.setParentOntologySubClass(clazzParent);
    	ontoClassTreeUserObject.setIsBaseOntology(clazzIsBaseOnto);
    	ontoClassTreeUserObject.setIsAID(clazzIsAID);
    	ontoClassTreeUserObject.setIsConcept(clazzIsConcept);
    	ontoClassTreeUserObject.setIsAgentAction(clazzIsAction);
    	ontoClassTreeUserObject.setIsPredicate(clazzIsPredicate);
		return ontoClassTreeUserObject;
		
	}
	
	
	/**
	 * Returns the TreeNode requested by the reference 
	 * @param reference
	 * @return The found DefaultMutableTreeNode
	 */
	public DefaultMutableTreeNode getTreeNode(String reference) {
		
		if (reference==null) return null;

		DefaultMutableTreeNode nodeFound = null;
		String searchFor = reference.replace(searchInPackage + "." , "");
		
		for (Enumeration<?> enu = this.rootNode.breadthFirstEnumeration(); enu.hasMoreElements();) {
			DefaultMutableTreeNode currNode = (DefaultMutableTreeNode) enu.nextElement();
			String currNodeText = currNode.getUserObject().toString(); 
			if (currNodeText.equals(searchFor)) {				
				nodeFound = currNode;
				break;
			} 
		}
		return nodeFound;
	}

}
