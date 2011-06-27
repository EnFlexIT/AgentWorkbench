package agentgui.graphEnvironment.networkModel;

import agentgui.graphEnvironment.prototypes.GraphElementPrototype;

/**
 * This class stores the component type settings for a network component type 
 * 
 * @see GraphElementPrototype
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 *
 */
public class ComponentTypeSettings {
	/**
	 * The agent class representing this component type
	 */
	private String agentClass;
	/**
	 * The GraphElementPrototype class representing this component type
	 */
	private String graphPrototype;
	
	/**
	 * The image icon which will be displayed on the component graph edges.
	 */
	private String edgeImage;
	
	/**
	 * Default Constructor
	 */
	public ComponentTypeSettings(){
		super();
	}
	/**
	 * Constructor
	 * @param agentClass The agent class name
	 * @param graphPrototype The GraphElementPrototype class name
	 */
	public ComponentTypeSettings(String agentClass, String graphPrototype, String edgeImage) {
		super();
		this.agentClass = agentClass;
		this.graphPrototype = graphPrototype;
		this.edgeImage = edgeImage;
	}
	/**
	 * Returns the agent class
	 * @return the agentClass
	 */
	public String getAgentClass() {
		return agentClass;
	}
	/**
	 * 
	 * @param agentClass the agentClass to set
	 */
	public void setAgentClass(String agentClass) {
		this.agentClass = agentClass;
	}
	/**
	 * @return the graphPrototype
	 */
	public String getGraphPrototype() {
		return graphPrototype;
	}
	/**
	 * @param graphPrototype the graphPrototype to set
	 */
	public void setGraphPrototype(String graphPrototype) {
		this.graphPrototype = graphPrototype;
	}
	/**
	 * Returns the path to the edge image icon
	 * @return the edgeImage
	 */
	public String getEdgeImage(){
		return edgeImage;
	}	
	/**
	 * Sets the path to the component edge image icon
	 */
	public void setEdgeImage(String edgeImage){
		this.edgeImage = edgeImage;
	}
	
}
