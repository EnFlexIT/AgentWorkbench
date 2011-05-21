package agentgui.graphEnvironment.networkModel;

/**
 * This class represents a graph edge in an environment model of the type graph / network 
 * @author Nils
 *
 */
public class GraphEdge extends GraphElement{
	/**
	 * The type identifier of the network component represented by this edge
	 */
	private String componentType;
	/**
	 * The weight / costs of this edge / component
	 */
	private float weight;
	/**
	 * Constructor
	 * @param id The id of this GraphEdge
	 * @param componentType The component type identifier for this GraphEdge
	 */
	public GraphEdge(String id, String componentType) {
		super();
		this.id = id;
		this.componentType = componentType;
	}
	/**
	 * @return the componentType
	 */
	public String getComponentType() {
		return componentType;
	}
	/**
	 * @param componentType the componentType to set
	 */
	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}
	/**
	 * @return the weight
	 */
	public float getWeight() {
		return weight;
	}
	/**
	 * @param weight the weight to set
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}
}
