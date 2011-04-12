package agentgui.graphEnvironment.controller;

public class GraphElementSettings {
	private String name;
	private String agentClass;
	private String graphPrototype;
	
	public GraphElementSettings(){
		super();
	}
	public GraphElementSettings(String name, String agentClass,
			String graphPrototype) {
		super();
		this.name = name;
		this.agentClass = agentClass;
		this.graphPrototype = graphPrototype;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
