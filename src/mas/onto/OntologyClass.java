package mas.onto;

import java.io.Serializable;

import javax.swing.tree.DefaultMutableTreeNode;

import application.Project;

public class OntologyClass extends Object implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Project CurrProject; 
	
	private OntologyClassTree ProjectOntologieTree;
	public OntologyClassVocabulary OntologieVocabulary;

	
	public OntologyClass(Project CurrPro) {
		CurrProject = CurrPro;
	}
	
	/**
	 * @return the projectOntologieTree
	 */
	public OntologyClassTree getOntologyTree() {
		if ( ProjectOntologieTree == null ) {
			OntologyClassTreeObject octo = new OntologyClassTreeObject(this, "Root");
			DefaultMutableTreeNode RootNode = new DefaultMutableTreeNode( octo );
			ProjectOntologieTree = new OntologyClassTree( RootNode, CurrProject );			
		}
		return ProjectOntologieTree;
	}	
	/**
	 * 
	 * @param Class, should be the ontology base class which 
	 * extends jade.content.onto.Ontology
	 */
	public void setOntologyVocabulary(Class<?> Class) {
		OntologieVocabulary = new OntologyClassVocabulary(Class);
	}
	public String getOntologyWord(String Key) {
		return OntologieVocabulary.get(Key);
	}
	
	
}
