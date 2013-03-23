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

import jade.content.onto.Ontology;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This class holds detailed information of a single ontology, given by its class reference.<br>
 * Furthermore it creates the needed nodes for the tree, which displays all elements. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class OntologyClass extends Object implements Serializable {

	private static final long serialVersionUID = 1142137843893954757L;

	private String currOntologySrcPackage = null;
	private String currOntologyMainClass  = null;
	private String currOntologyName 	  = null;
	
	public OntologyClassTree projectOntologieTree = null;
	public OntologyClassVocabulary ontologieVocabulary;
	public List<String> ontologyConceptNames;
	public List<String> ontologyAgentActionNames;
	public List<String> ontologyPredicateNames;
	public String ontologyErrorStack = "";
	
	/**
	 * Constructor of this class.
	 * As 'ontologyReference' two arguments can be used:
	 * a) the reference to the main-class of an known ontology
	 * b) the source-package of an ontology e. g. 'myproject.ontology'
	 *
	 * @param ontologyReference the ontology reference
	 */
	public OntologyClass(String ontologyReference) {

		// --------------------------------------------------------------------
		// --- Is this the Reference to the Main-class of the ontology? -------
		if (referenceIsClass(ontologyReference)==true) {
			// --- Yes, it is -------------------------
			currOntologyMainClass  = ontologyReference;
			currOntologySrcPackage = currOntologyMainClass.substring(0, currOntologyMainClass.lastIndexOf("."));
		} else {
			// --- No it isn't ------------------------
			currOntologyMainClass  = null;
			currOntologySrcPackage = ontologyReference;
		}
		// --------------------------------------------------------------------
		// --- Build the TreeModel for this ontology --------------------------
		this.setOntologyTree();		
 
		// --------------------------------------------------------------------
		// --- Set Informations for Concepts, AgentActions and Predicates -----
		this.setOntologyDetailInfo();
	}

	/**
	 * Checks if the given Ontology is a class or not.
	 *
	 * @param ontologyReference the ontology reference
	 * @return true, if the reference is a valid class
	 */
	private boolean referenceIsClass(String ontologyReference) {
		try {
			@SuppressWarnings("unused")
			Class<?> cl = Class.forName(ontologyReference);
			return true;
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Creates the OntologyTree of the current Ontology given it's Main-Class.
	 */
	private void setOntologyTree() {
		OntologyClassTreeObject octo = new OntologyClassTreeObject(this, "Root");
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode( octo );
		try {
			this.projectOntologieTree = new OntologyClassTree(rootNode, this, currOntologySrcPackage);	
			
		} catch (Exception ex) {
			System.err.println("Error while creating the ontology tree for " + this.currOntologySrcPackage + ":");
			ex.printStackTrace();
		}
					
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
				Class<?> currOntoClass = Class.forName(currOntologyMainClass);
				Method method = currOntoClass.getMethod("getInstance", new Class[0]);
				ontology = (Ontology) method.invoke(currOntoClass, new Object[0]);
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
	 *
	 * @return the projectOntologieTree
	 */
	public OntologyClassTree getOntologyTree() {
		if ( projectOntologieTree == null ) {
			this.setOntologyTree();	
		}
		return projectOntologieTree;
	}	
	
	/**
	 * Sets the ontology vocabulary.
	 *
	 * @param Class the new ontology vocabulary
	 */
	public void setOntologyVocabulary(Class<?> Class) {
		ontologieVocabulary = new OntologyClassVocabulary(Class);
	}
	
	/**
	 * Provides a value from the ontology vocabulary, given by its public static name.
	 *
	 * @param key The public static name
	 * @return The Value of the public static variable
	 */
	public String getVocabularyValue(String key) {
		return ontologieVocabulary.get(key);
	}

	/**
	 * Sets the ontology main class.
	 *
	 * @param currOntoMainClass the currOntoMainClass to set
	 */
	public void setOntologyMainClass(String currOntoMainClass) {
		this.currOntologyMainClass = currOntoMainClass;
	}
	
	/**
	 * Gets the ontology main class.
	 *
	 * @return the currOntoMainClass
	 */
	public String getOntologyMainClass() {
		return currOntologyMainClass;
	}

	/**
	 * Sets the ontology source package.
	 *
	 * @param ontologySourcePackage the currOntologySrcPackage to set
	 */
	public void setOntologySourcePackage(String ontologySourcePackage) {
		this.currOntologySrcPackage = ontologySourcePackage;
	}
	
	/**
	 * Gets the ontology source package.
	 *
	 * @return the currOntologySrcPackage
	 */
	public String getOntologySourcePackage() {
		return currOntologySrcPackage;
	}
	
	/**
	 * Sets the ontology name.
	 *
	 * @param currOntoName the currOntoName to set
	 */
	public void setOntologyName(String currOntoName) {
		this.currOntologyName = currOntoName;
	}
	
	/**
	 * Gets the ontology name.
	 *
	 * @return the currOntoName
	 */
	public String getOntologyName() {
		return currOntologyName;
	}
	
	
}
