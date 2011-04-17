package agentgui.graphEnvironment.environmentModel;

import jade.content.Concept;

public abstract class GraphElement {
	
	private String id = null;
	
	private Concept ontologyRepresentation = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Concept getOntologyRepresentation() {
		return ontologyRepresentation;
	}

	public void setOntologyRepresentation(Concept ontologyRepresentation) {
		this.ontologyRepresentation = ontologyRepresentation;
	}

}
