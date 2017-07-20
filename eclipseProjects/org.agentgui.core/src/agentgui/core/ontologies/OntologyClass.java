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
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import agentgui.core.classLoadService.ClassLoadServiceUtility;
import jade.content.onto.Ontology;

/**
 * This class holds detailed information of a single ontology, given by its class reference.<br>
 * Furthermore, it creates the needed nodes for the tree, which displays all elements. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OntologyClass implements Serializable {

	private static final long serialVersionUID = 1142137843893954757L;

	private Class<? extends Ontology> ontologyClass;
	
	private String currOntologySrcPackage;
	private String currOntologyMainClass;
	private String currOntologyName;
	
	public OntologyClassTree projectOntologieTree;
	public OntologyClassVocabulary ontologieVocabulary;
	public List<String> ontologyConceptNames;
	public List<String> ontologyAgentActionNames;
	public List<String> ontologyPredicateNames;
	
	public String ontologyErrorStack = "";
	private boolean correctTreeBuild;
	
	
	/**
	 * Constructor of this class.
	 * As 'ontologyReference' two arguments can be used:
	 * a) the reference to the main-class of an known ontology
	 * b) the source-package of an ontology e. g. 'myproject.ontology'
	 *
	 * @param ontologyReference the ontology reference
	 */
	public OntologyClass(String ontologyReference) {
		
		this.currOntologyMainClass = ontologyReference;
		
		// --------------------------------------------------------------------
		// --- Is this the Reference to the Main-class of the ontology? -------
		if (this.getOntologyClass(this.currOntologyMainClass)!=null) {
			// --- Yes, it is -------------------------
			this.currOntologySrcPackage = this.currOntologyMainClass.substring(0, this.currOntologyMainClass.lastIndexOf("."));
		} else {
			// --- No it isn't ------------------------
			this.currOntologyMainClass = null;
			this.currOntologySrcPackage = ontologyReference;
		}
		// --------------------------------------------------------------------
		// --- Build the TreeModel for this ontology --------------------------
		this.setCorrectTreeBuild(this.setOntologyTree());
 
		// --------------------------------------------------------------------
		// --- Set Informations for Concepts, AgentActions and Predicates -----
		this.setOntologyDetailInfo();
	}
	
	/**
	 * Checks if is correct tree build.
	 * @return true, if is correct tree build
	 */
	public boolean isCorrectTreeBuild() {
		return correctTreeBuild;
	}
	/**
	 * Sets the correct tree build.
	 * @param correctTreeBuild the new correct tree build
	 */
	public void setCorrectTreeBuild(boolean correctTreeBuild) {
		this.correctTreeBuild = correctTreeBuild;
	}
	
	
	/**
	 * Returns the ontology class, if the specified ontology reference is valid.
	 * @return the ontology class
	 */
	public Class<? extends Ontology> getOntologyClass() {
		if (ontologyClass==null) {
			ontologyClass = this.getOntologyClass(this.getOntologyMainClass());
		}
		return ontologyClass;
	}
	/**
	 * Returns the ontology class, if the specified ontology reference is valid.
	 * @param ontologyReference the ontology reference
	 * @return the ontology class
	 */
	@SuppressWarnings("unchecked")
	private Class<? extends Ontology> getOntologyClass(String ontologyReference) {
		Class<? extends Ontology> ontoClass = null;
		if (ontologyReference!=null && ontologyReference.equals("")==false) {
			try {
				Class<?> classLoded = ClassLoadServiceUtility.forName(ontologyReference);
				ontoClass = (Class<? extends Ontology>) classLoded;
				
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
			}	
		}
		return ontoClass;
	}
	
	/**
	 * Creates the OntologyTree of the current Ontology given it's Main-Class.
	 * @return true, if successful
	 */
	private boolean setOntologyTree() {
		OntologyClassTreeObject octo = new OntologyClassTreeObject(this, "Root");
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(octo);
		try {
			this.projectOntologieTree = new OntologyClassTree(rootNode, this, currOntologySrcPackage);	
			return true;
			
		} catch (Exception ex) {
			System.err.println("Error while creating the ontology tree for " + this.currOntologySrcPackage + ":");
			ex.printStackTrace();
		}
		return false;		
	}
	
	/**
	 * This Method set the public fields 'ontologyConceptNames', 'ontologyActionNames'
	 * and 'ontologyPredicateNames', which are List<String> - Types.
	 */
	@SuppressWarnings("unchecked")
	private void setOntologyDetailInfo() {
		Ontology onto = getOntologyInstance();
		if (onto!=null) {
			ontologyConceptNames     = onto.getConceptNames();
			ontologyAgentActionNames = onto.getActionNames();
			ontologyPredicateNames   = onto.getPredicateNames();
		}
	}
	
	/**
	 * This Method returns the instance of the current Ontology-Class.
	 *
	 * @return The instance of the ontology
	 */
	public Ontology getOntologyInstance() {
		
		Ontology ontology = null;
		// --- Try to get an instance of the current Ontology ---------
		if(currOntologyMainClass!=null){
			try {
				ontology = ClassLoadServiceUtility.getOntologyInstance(this.currOntologyMainClass);
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return ontology;
	}
	
	/**
	 * Provides the @link OntologyClassTree - Object.
	 * @return the projectOntologieTree
	 */
	public OntologyClassTree getOntologyTree() {
		if (projectOntologieTree==null ) {
			this.setOntologyTree();	
		}
		return projectOntologieTree;
	}	
	
	/**
	 * Sets the ontology vocabulary.
	 * @param Class the new ontology vocabulary
	 */
	public void setOntologyVocabulary(Class<?> Class) {
		ontologieVocabulary = new OntologyClassVocabulary(Class);
	}
	/**
	 * Provides a value from the ontology vocabulary, given by its public static name.
	 * @param key The public static name
	 * @return The Value of the public static variable
	 */
	public String getVocabularyValue(String key) {
		return ontologieVocabulary.get(key);
	}

	/**
	 * Sets the ontology main class.
	 * @param currOntoMainClass the currOntoMainClass to set
	 */
	public void setOntologyMainClass(String currOntoMainClass) {
		this.currOntologyMainClass = currOntoMainClass;
	}
	/**
	 * Gets the ontology main class.
	 * @return the currOntoMainClass
	 */
	public String getOntologyMainClass() {
		return currOntologyMainClass;
	}

	/**
	 * Sets the ontology source package.
	 * @param ontologySourcePackage the currOntologySrcPackage to set
	 */
	public void setOntologySourcePackage(String ontologySourcePackage) {
		this.currOntologySrcPackage = ontologySourcePackage;
	}
	/**
	 * Gets the ontology source package.
	 * @return the currOntologySrcPackage
	 */
	public String getOntologySourcePackage() {
		return currOntologySrcPackage;
	}
	
	/**
	 * Sets the ontology name.
	 * @param currOntoName the currOntoName to set
	 */
	public void setOntologyName(String currOntoName) {
		this.currOntologyName = currOntoName;
	}
	/**
	 * Gets the ontology name.
	 * @return the currOntoName
	 */
	public String getOntologyName() {
		return currOntologyName;
	}
	
	
}
