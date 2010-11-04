package agentgui.core.ontologies;

import jade.content.onto.Ontology;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

public class OntologyClass extends Object implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String currOntologySrcPackage = null;
	private String currOntologyMainClass  = null;
	private String currOntologyName 	  = null;
	
	public OntologyClassTree ProjectOntologieTree = null;
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
	 * @param ontologyReference
	 */
	public OntologyClass(String ontologyReference) {

		// --------------------------------------------------------------------
		// --- Is this the Reference to the Main-class of the ontology? -------
		if ( referenceIsClass(ontologyReference)==true ) {
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
	 * @param ontologyReference
	 * @return
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
	 * Creates the OntologyTree of the current Ontology given it's Main-Class 
	 */
	private void setOntologyTree() {
		OntologyClassTreeObject octo = new OntologyClassTreeObject(this, "Root");
		DefaultMutableTreeNode RootNode = new DefaultMutableTreeNode( octo );
		ProjectOntologieTree = new OntologyClassTree( RootNode, this, currOntologySrcPackage );			
	}
	/**
	 * this Method set the public fields 'ontologyConceptNames', 'ontologyActionNames'
	 * and 'ontologyPredicateNames', which are List<String> - Types 
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
	 * This Method returns the instance of the current Ontology-Class
	 * @return 
	 */
	private Ontology getOntologyInstance() {
		
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
	 * @return the projectOntologieTree 
	 */
	public OntologyClassTree getOntologyTree() {
		if ( ProjectOntologieTree == null ) {
			this.setOntologyTree();	
		}
		return ProjectOntologieTree;
	}	
	
	/**
	 * @param Class, should be the ontology base class which 
	 * extends jade.content.onto.Ontology
	 */
	public void setOntologyVocabulary(Class<?> Class) {
		ontologieVocabulary = new OntologyClassVocabulary(Class);
	}
	public String getOntologyWord(String Key) {
		return ontologieVocabulary.get(Key);
	}

	/**
	 * @param currOntoMainClass the currOntoMainClass to set
	 */
	public void setOntologyMainClass(String currOntoMainClass) {
		this.currOntologyMainClass = currOntoMainClass;
	}
	/**
	 * @return the currOntoMainClass
	 */
	public String getOntologyMainClass() {
		return currOntologyMainClass;
	}

	/**
	 * @param ontologySourcePackage the currOntologySrcPackage to set
	 */
	public void setOntologySourcePackage(String ontologySourcePackage) {
		this.currOntologySrcPackage = ontologySourcePackage;
	}
	/**
	 * @return the currOntologySrcPackage
	 */
	public String getOntologySourcePackage() {
		return currOntologySrcPackage;
	}
	
	/**
	 * @param currOntoName the currOntoName to set
	 */
	public void setOntologyName(String currOntoName) {
		this.currOntologyName = currOntoName;
	}
	/**
	 * @return the currOntoName
	 */
	public String getOntologyName() {
		return currOntologyName;
	}
	
	
}
