package agentgui.graphEnvironment.environmentModel;

import java.util.HashSet;

//import jade.content.Concept;

/**
 * This class represents a component of the modeled network. It contains
 * its' ontology representation, its' GraphElementPrototype, the nodes and
 * edges representing it in the environment graph and an ID for easier access. 
 * @author Nils
 *
 */
public class NetworkComponent {
	/**
	 * The NetworkComponent's ID
	 */
	private String id;
	/**
	 * The NetworkComponent's type
	 */
	private String type;
//	/**
//	 * The NetworkComponent's ontology representation
//	 */
//	private Concept ontologyRepresentation;
	/**
	 * The IDs of the nodes and edges that are part of this NetworkComponent
	 */
	private HashSet<String> graphElementIDs;
	/**
	 * Specifies if the NetworkComponent is directed or undirected
	 */
	private boolean directed;
	/**
	 * The NetworkComponent's GraphElementPrototype class name
	 */
	private String prototypeClassName;
	/**
	 * The ontology object instance representing this component, serialized as a base64 encoded String for saving via JAXB
	 */
	private String encodedOntologyRepresentation;
	/**
	 * Default constructor
	 */
	public NetworkComponent(){
		graphElementIDs = new HashSet<String>();
	}
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
//	/**
//	 * @return the ontologyRepresentation
//	 */
//	public Concept getOntologyRepresentation() {
//		return ontologyRepresentation;
//	}
//	/**
//	 * @param ontologyRepresentation the ontologyRepresentation to set
//	 */
//	public void setOntologyRepresentation(Concept ontologyRepresentation) {
//		this.ontologyRepresentation = ontologyRepresentation;
//	}
	/**
	 * @return the graphElements
	 */
	public HashSet<String> getGraphElementIDs() {
		return graphElementIDs;
	}
	/**
	 * @param graphElementIDs the graphElements to set
	 */
	public void setGraphElementIDs(HashSet<String> graphElementIDs) {
		this.graphElementIDs = graphElementIDs;
	}
	/**
	 * @return the directed
	 */
	public boolean isDirected() {
		return directed;
	}
	/**
	 * @param directed the directed to set
	 */
	public void setDirected(boolean directed) {
		this.directed = directed;
	}
	/**
	 * @return the prototypeClassName
	 */
	public String getPrototypeClassName() {
		return prototypeClassName;
	}
	/**
	 * @param prototypeClassName the prototypeClassName to set
	 */
	public void setPrototypeClassName(String prototypeClassName) {
		this.prototypeClassName = prototypeClassName;
	}
	/**
	 * @return the encodedOntologyRepresentation
	 */
	public String getEncodedOntologyRepresentation() {
		return encodedOntologyRepresentation;
	}
	/**
	 * @param encodedOntologyRepresentation the encodedOntologyRepresentation to set
	 */
	public void setEncodedOntologyRepresentation(
			String encodedOntologyRepresentation) {
		this.encodedOntologyRepresentation = encodedOntologyRepresentation;
	}
	
}
