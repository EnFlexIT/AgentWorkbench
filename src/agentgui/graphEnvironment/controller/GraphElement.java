package agentgui.graphEnvironment.controller;

import jade.content.Concept;

public abstract class GraphElement {
	
	private String id = null;
	
	private Concept ontologyRepresentation = null;

	String getId() {
		return id;
	}

	void setId(String id) {
		this.id = id;
	}

	Concept getOntologyRepresentation() {
		return ontologyRepresentation;
	}

	void setOntologyRepresentation(Concept ontologyRepresentation) {
		this.ontologyRepresentation = ontologyRepresentation;
	}

}
