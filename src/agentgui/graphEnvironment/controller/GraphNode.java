package agentgui.graphEnvironment.controller;

import jade.content.Concept;

public class GraphNode {
	private String id;
	
	private Concept OntologyObject;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the ontologyObject
	 */
	public Concept getOntologyObject() {
		return OntologyObject;
	}

	/**
	 * @param ontologyObject the ontologyObject to set
	 */
	public void setOntologyObject(Concept ontologyObject) {
		OntologyObject = ontologyObject;
	}

	public GraphNode(String id, Concept ontologyObject) {
		super();
		this.id = id;
		OntologyObject = ontologyObject;
	}
	
	
}
