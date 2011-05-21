package agentgui.graphEnvironment.networkModel;
/**
 * This class stores the component type settings for a network component type 
 * @author Nils
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
	 * Constructor
	 */
	public ComponentTypeSettings(){
		super();
	}
	/**
	 * Constructor
	 * @param agentClass The agent class name
	 * @param graphPrototype The GraphElementPrototype class name
	 */
	public ComponentTypeSettings(String agentClass, String graphPrototype) {
		super();
		this.agentClass = agentClass;
		this.graphPrototype = graphPrototype;
	}
	/**
	 * @return the agentClass
	 */
	public String getAgentClass() {
		return agentClass;
	}
	/**
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
	
}
