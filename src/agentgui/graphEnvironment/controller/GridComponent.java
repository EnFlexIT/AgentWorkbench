package agentgui.graphEnvironment.controller;

public class GridComponent {
	private String agentID;
	private String type;

	public String getAgentID() {
		return agentID;
	}
	
	public String getType(){
		return this.type;
	}

	public GridComponent(String agentID, String type) {
		super();
		this.agentID = agentID;
		this.type = type;
	}
}
