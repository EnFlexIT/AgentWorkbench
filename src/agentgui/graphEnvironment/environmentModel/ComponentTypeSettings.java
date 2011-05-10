package agentgui.graphEnvironment.environmentModel;

public class ComponentTypeSettings {
	private String agentClass;
	private String graphPrototype;
	
	public ComponentTypeSettings(){
		super();
	}
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
