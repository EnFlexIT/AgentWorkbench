package agentgui.graphEnvironment.prototypes;

import jade.content.Concept;

/**
 * Abstract super class for GraphElementPrototypes.
 * A graph element defines how a component in a graph/network environment 
 * will be represented in graph nodes and edges.  
 * @author Nils
 *
 */
public abstract class GraphElementPrototype {
	private Concept ontologyRepresentation;

	/**
	 * @return the ontologyRepresentation
	 */
	public Concept getOntologyRepresentation() {
		return ontologyRepresentation;
	}

	/**
	 * @param ontologyRepresentation the ontologyRepresentation to set
	 */
	public void setOntologyRepresentation(Concept ontologyRepresentation) {
		this.ontologyRepresentation = ontologyRepresentation;
	}
}
