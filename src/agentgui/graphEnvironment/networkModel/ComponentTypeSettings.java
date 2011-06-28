package agentgui.graphEnvironment.networkModel;

import agentgui.graphEnvironment.prototypes.GraphElementPrototype;

/**
 * This class stores the component type settings for a network component type 
 * 
 * @see GraphElementPrototype
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 * @author Satyadeep 
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
	 * The color which will be displayed on the component graph edges.
	 */
	private String color;
	
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
	 * @param edgeImage  The path to the image icon to be used for the component
	 * @param color The color to be used for the component edges in RGB integer representation.
	 */
	public ComponentTypeSettings(String agentClass, String graphPrototype, String edgeImage, String color) {
		super();
		this.agentClass = agentClass;
		this.graphPrototype = graphPrototype;
		this.edgeImage = edgeImage;
		this.color = color;
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
	/**
	 * Returns the color of the component edges.
	 * @return the color as a string in RGB integer representation.
	 */
	public String getColor(){
		return color;
	}	
	/**
	 * Sets the color of the component edges.
	 * @param color as string in RGB integer representation.
	 */
	public void setColor(String color){
		this.color = color;
	}
	
	
}
