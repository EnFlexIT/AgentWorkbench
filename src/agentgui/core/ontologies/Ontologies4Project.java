package agentgui.core.ontologies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import agentgui.core.application.Application;
import agentgui.core.application.Project;

/**
 * This class is used for the management of the used Ontologies inside a project.
 * It can add or remove further (Sub)-Ontologies in order to reduce the
 * effort of building your own complex ontology
 * 
 * 
 */

public class Ontologies4Project extends HashMap<String, OntologyClass> {

	
	private static final long serialVersionUID = 2055269152886396404L;
	
	private Project currProject;
	private DefaultTreeModel projectOntologyTree = null;
	private boolean hasErrors = false;
	private ArrayList<String> errorStack = new ArrayList<String>();
		
	/**
	 * Constructor of the Class
	 * @param project
	 */
	public Ontologies4Project(Project project) {
		currProject = project;	
		this.setOntologyTree();
	}
	
	/**
	 * This method sets the whole TreeModel for the project ontology, which
	 * can consist of one or more Sub-Ontologies
	 * @return DefaultTreeModel
	 */
	private void setOntologyTree() {
		
		Vector<String> subOntologiesCorrected = new Vector<String>();
		
		// --- Run through Sub-Ontologies -----------------------
		Iterator<String> it = currProject.subOntologies.iterator();
		while (it.hasNext()) {
			// --- Get reference of current Sub-Ontology --------
			String subOntologyReference = it.next();	
			// --- Build OntologyClass-Object for Ontology ------
			OntologyClass onCla = new OntologyClass(currProject, subOntologyReference);
			// --- Remember this class localy -------------------
			this.put(onCla.getOntologyMainClass(), onCla);
			subOntologiesCorrected.add(onCla.getOntologyMainClass());
		}
		// ------------------------------------------------------------------------------------
		// --- Correct the Vector of the Sub-Ontologies inside the Project-Class            ---
		// --- because of the possible missing Main-Class inside the Ontology-Reference.    ---
		// --- E.g. it is possible to point to an Ontology by just using 'contmas.ontology' ---
		// ------------------------------------------------------------------------------------
		currProject.subOntologies = subOntologiesCorrected;

		// --- Build the OntologyTree (DefaultTreeModel) --------
		this.buildOntologyTree();
	}
	
	/**
	 * This Method builds up the DefaultTreeModel for the project ontology 
	 */
	private void buildOntologyTree() {
		
		// --- get Root-Node for the whole project ontology -----
		OntologyClassTreeObject root4ProjektOntology = new OntologyClassTreeObject(null, "Project ontology");
		DefaultMutableTreeNode RootNode = new DefaultMutableTreeNode( root4ProjektOntology );
		
		// --- Create Tree-Object with it -----------------------
		projectOntologyTree = new DefaultTreeModel(RootNode);
		
		// --- Create new ErrorStack ----------------------------
		this.errorStack = new ArrayList<String>();
		
		// --- Run through Sub-Ontologies -----------------------
		Iterator<String> it = currProject.subOntologies.iterator();
		while (it.hasNext()) {
			// --- Get reference of current Sub-Ontology --------
			String subOntologyReference = it.next();	
			// --- Build SubTree for current SubOntology --------
			OntologyClass onCla = this.get(subOntologyReference);
			DefaultMutableTreeNode subRootNode = null;
			if (onCla==null) {
				String errText = "ERROR: " + subOntologyReference;
				OntologyClassTreeObject errSubRootNode = new OntologyClassTreeObject(null, errText);
				subRootNode = new DefaultMutableTreeNode( errSubRootNode );
			} else {
				subRootNode = (DefaultMutableTreeNode) onCla.getOntologyTree().getRoot();
			}
			RootNode.add(subRootNode);
			// --- Fehler in Sub-Ontologie? ---------------------
			if ( onCla.ontologyErrorStack.equalsIgnoreCase("")==false ) {
				this.errorStack.add(onCla.ontologyErrorStack); 
			}
		}

		// --- Error-Handling for ConceptNames(), ActionNames() and PredicateNames() ---
		new ErrorCheck(this);
		if (this.hasErrors) {
			System.err.println( this.getErrorStack() );
		}
		
	}
	/**
	 * This method returns the whole TreeModel for the project ontology, which
	 * can consist of one or more Sub-Ontologies
	 * @return DefaultTreeModel
	 */
	public DefaultTreeModel getOntologyTree() {
		return projectOntologyTree;
	}	
	
	public boolean hasError() {
		return hasErrors;
	}
	/**
	 * @return the errorStack
	 */
	public String getErrorStack() {
		String errString = "";
		for (int i = 0; i < errorStack.size(); i++) {
			errString += errorStack.get(i) + "\n";
		}
		return errString;
	}

	/**
	 * Adds a Sub-Ontology to the current project 
	 * @param ontoRef
	 */
	public void addSubOntology (String newSubOntology) {
		OntologyClass onCla = new OntologyClass(currProject, newSubOntology);
		if ( currProject.subOntologies.contains(onCla.getOntologyMainClass())==false) {
			currProject.subOntologies.add(onCla.getOntologyMainClass());	
		}		
		this.put(onCla.getOntologyMainClass(), onCla);
		this.buildOntologyTree();
	}
	/**
	 * Removes a Sub-Ontologie from the current Project Ontology
	 * @param ontoRef
	 */
	public void removeSubOntology (String removableSubOntology) {
		currProject.subOntologies.remove(removableSubOntology);
		this.remove(removableSubOntology);
		this.buildOntologyTree();
	}
	
	/**
	 * This Method filters all available Ontologies 
	 * by the already used Project-Ontologies and 
	 * returns the remaining classes as Vector<String> 
	 * @return
	 */
	public Vector<Class<?>> getAllNoneUsedOntologies() {
		
		Vector<Class<?>> allOntos = Application.ClassDetector.getOntologieClasse(false);
		Vector<Class<?>> filteredOntos = new Vector<Class<?>>();
		for (int i =0; i<allOntos.size(); i++) {
			Class<?> currClass = allOntos.get(i);
			String currClassName = currClass.getName();
			if ( this.get(currClassName)==null && currClassName.endsWith(Application.RunInfo.getFileNameProjectOntology())==false ) {
				filteredOntos.add(currClass);
			}
		}
		return filteredOntos;
	}
	
	/**
	 * This Method returns the 'TreeNode' for class to 
	 * search, which is a part of the Project-Ontologie
	 * @param classReference2Search
	 * @return
	 */
	public DefaultMutableTreeNode getClassTreeNode(String classReference2Search) {
		
		OntologyClass onCla = null;
		String searchSrcPackage = classReference2Search.substring(0, classReference2Search.lastIndexOf("."));
		String searchSrcClass = classReference2Search;
		
		if (classReference2Search.equals(OntologyClassTree.BaseClassAID)) {
			// --- Just get the first ontology we can get ----------------
			String subOntoMainClas = currProject.subOntologies.get(0);
			onCla = this.get(subOntoMainClas);
			searchSrcClass = "AID";
			
		} else {
			// --- Get the designated Ontologieclass ---------------------
			Iterator<String> it = currProject.subOntologies.iterator();
			while (it.hasNext()) {
				String subOntoMainClas = it.next();	
				onCla = this.get(subOntoMainClas);
				String subOntoSrcPack = onCla.getOntologySourcePackage();
				if ( subOntoSrcPack.equalsIgnoreCase(searchSrcPackage) ) {
					break;
				} else {
					onCla = null;
				}
			}
			// --- Nothing found? By, by... ------------------------------
			if ( onCla == null ) {
				return null;
			}	
		}
		
		// --- Something found! Get DefaultTableModel for Slots -----
		OntologyClassTree onClaTree = onCla.getOntologyTree();
		DefaultMutableTreeNode tn = onClaTree.getTreeNode(searchSrcClass);
		return tn;
	}
	
	/**
	 * This Method returns the 'OntologyClassTreeObject' for class to 
	 * search, which is a part of the Project-Ontologie
	 * @param classReference2Search
	 * @return
	 */
	public OntologyClassTreeObject getClassTreeObject(String classReference2Search) {
		
		DefaultMutableTreeNode tn = this.getClassTreeNode(classReference2Search);
		if (tn==null) {
			return null;
		} else {
			OntologyClassTreeObject userObject = (OntologyClassTreeObject) tn.getUserObject();
			return userObject;	
		}
	}
	
	/**
	 * This Method returns a DefaultTableModel for a   
	 * given class inside of the project-ontology 
	 * @param classReference2Search
	 * @return DefaultTableModel
	 */
	public DefaultTableModel getSlots4Class(String classReference2Search) {
		OntologyClassTreeObject userObject = this.getClassTreeObject(classReference2Search);
		if (userObject== null) {
			return null;
		} else {
			return userObject.getTableModel4Slot();			
		}
	}

	/**
	 * This Method returns a ArrayList of Slot-Descriptions for a   
	 * given class inside of the project-ontology 
	 * @param classReference2Search
	 * @return DefaultTableModel
	 */
	public OntologySingleClassDescription getSlots4ClassAsObject(String classReference2Search) {
		OntologyClassTreeObject userObject = this.getClassTreeObject(classReference2Search);
		if (userObject== null) {
			return null;
		} else {
			return userObject.getClassDescription();			
		}
	}
	
	// --- Start SubClass -------------------------------------------
	/**
	 * Checks the configuration of the current project ontology.
	 * In detail, this method looks for intersections between
	 * all Ontology -Concepts, -AgentActions and -Predicates.
	 * 
	 * @author Christian Derksen - DAWIS - ICB - University Duisburg-Essen
	 */
	private class ErrorCheck {

		private Ontologies4Project currProjectOntology;
		
		private ArrayList<String> mergerConcepts = new ArrayList<String>();
		private ArrayList<String> mergerAgentActions =  new ArrayList<String>();
		private ArrayList<String> mergerPredicates =  new ArrayList<String>();
		private ArrayList<String> mergerClasses = new ArrayList<String>();
		
		private ArrayList<String> mergerConceptsUnique = new ArrayList<String>();
		private ArrayList<String> mergerAgentActionsUnique =  new ArrayList<String>();
		private ArrayList<String> mergerPredicatesUnique =  new ArrayList<String>();
		private ArrayList<String> mergerClassesUnique = new ArrayList<String>();
		
		private ArrayList<String> duplicateConcepts =  new ArrayList<String>();
		private ArrayList<String> duplicateAgentActions=  new ArrayList<String>();
		private ArrayList<String> duplicatePredicates =  new ArrayList<String>();
		private ArrayList<String> duplicateClasses =  new ArrayList<String>();
		
		/**
		 * Constructor of this class
		 * @param projectOntology
		 */
		public ErrorCheck(Ontologies4Project projectOntology) {
			
			currProjectOntology = projectOntology;
			if (currProjectOntology.size()>1) {
				// --- Merge Concepts, AgentActioons and Predicates to one Array each ---
				this.setMergedUniqueAndDuplicateArrays();
				// --- Check for duplicates ---------------------------------------------
				if ( duplicateConcepts.size() > 0 || duplicateAgentActions.size() > 0 || duplicatePredicates.size() > 0 || duplicateClasses.size() > 0 ) {
					hasErrors = true;
				}
				// --- Analyse the found intersections ----------------------------------
				if ( hasErrors==true ) {
					for (int i = 0; i < duplicateClasses.size(); i++) {
						// --- Where is this class used in ------------------------------ 
						String usedIn = getWhereClassIsUsed(duplicateClasses.get(i));
						errorStack.add(usedIn);						
					}
				}
				// --- Done -------------------------------------------------------------
			}
		}
		
		
		/**
		 * This method searches inside the sub-ontologies for the given 
		 * class name and returns it use as an Information-String
		 * @param classname
		 * @return
		 */
		private String getWhereClassIsUsed(String classname) {
			
			String usedIn = "";
			Collection<String> Keys = currProjectOntology.keySet();
			for (Iterator<String> iterator = Keys.iterator(); iterator.hasNext();) {
				
				String Key = iterator.next();
				OntologyClass onCla = currProjectOntology.get(Key);
				
				DefaultMutableTreeNode node = onCla.getOntologyTree().getTreeNode(classname);
				if ( node!=null) {
					// --- UserObject aus dem entsprechenden Node-Object analysieren ---- 
					OntologyClassTreeObject userObject = (OntologyClassTreeObject) node.getUserObject();
					// --- Text ergänzen ------------------------------------------------ 
					if (usedIn.equalsIgnoreCase("")==false) {
						usedIn += " AND ";
					}
					usedIn += userObject.getClassReference() + " ";
					if (userObject.isConcept()) {
						usedIn += "[Concept]";
					} else if (userObject.isAgentAction()) {
						usedIn += "[AgentAction, Concept]";
					} else if (userObject.isPredicate()) {
						usedIn += "[Predicate]";
					} 
				}
			}
			return "'" + classname + "' is used in: " + usedIn;
		}
		
		/**
		 * This Method creates the merged and unique ArrayLists<String>
		 * for the comparison of Concept, AgentActions and Predicates
		 */
		private void setMergedUniqueAndDuplicateArrays() {
			
			// --- Run through all sub-ontologies of this project -------------
			Collection<String> Keys = currProjectOntology.keySet();
			for (Iterator<String> iterator = Keys.iterator(); iterator.hasNext();) {
				String Key = iterator.next();
				OntologyClass subOnto = currProjectOntology.get(Key);
				if(subOnto.ontologyConceptNames!=null){
					mergerConcepts.addAll(subOnto.ontologyConceptNames);	
				}
				if(subOnto.ontologyAgentActionNames!=null){
					mergerAgentActions.addAll(subOnto.ontologyAgentActionNames);
				}
				if(subOnto.ontologyPredicateNames!=null){
					mergerPredicates.addAll(subOnto.ontologyPredicateNames);
				}
			}
			
			// --- Merge elements which can be classes in an Ontology ---------
			mergerClasses.addAll(mergerConcepts);
			mergerClasses.addAll(mergerPredicates);
			
			// --- Remove all duplicate-enmtries from the Lists ---------------
			mergerConceptsUnique = removeDuplicatedEntries(mergerConcepts);
			mergerAgentActionsUnique = removeDuplicatedEntries(mergerAgentActions);
			mergerPredicatesUnique = removeDuplicatedEntries(mergerPredicates);
			mergerClassesUnique = removeDuplicatedEntries(mergerClasses);
			
			duplicateConcepts = getDuplicateEntries(mergerConcepts, mergerConceptsUnique);
			duplicateAgentActions = getDuplicateEntries(mergerAgentActions, mergerAgentActionsUnique);
			duplicatePredicates = getDuplicateEntries(mergerPredicates, mergerPredicatesUnique);
			duplicateClasses = getDuplicateEntries(mergerClasses, mergerClassesUnique);
		}
		/**
		 * This Method returns a ArrayList<String> without duplicate-entries
		 * @param arrayList
		 * @return
		 */
		private ArrayList<String> removeDuplicatedEntries(ArrayList<String> arrayList) {
		    HashSet<String> hashSet = new HashSet<String>(arrayList);
		    ArrayList<String> arrLi = new ArrayList<String>();
		    arrLi.addAll(hashSet);
			return arrLi;
		}
		/**
		 * Method looks for the duplicate entry inside the merged Array
		 * @param mergedArray
		 * @param mergedArrayUnique
		 * @return
		 */
		private ArrayList<String> getDuplicateEntries(ArrayList<String> mergedArray, ArrayList<String> mergedArrayUnique ) {
			ArrayList<String> returnList = new ArrayList<String>();
			for (int i = 0; i < mergedArrayUnique.size(); i++) {
				String string = mergedArrayUnique.get(i);
				if ( mergedArray.indexOf(string) != mergedArray.lastIndexOf(string) ) {
					returnList.add(string);
				}
			};			
			return returnList;
		}
		
		
	} // --- End SubClass -------------------------------------------
	
		
}
