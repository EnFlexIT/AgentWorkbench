package agentgui.graphEnvironment.controller;

public class GraphEdge extends GraphElement{
	private String id;
	private String type;

	public String id() {
		return id;
	}
	
	public String getType(){
		return this.type;
	}

	public GraphEdge(String id, String type) {
		super();
		this.id = id;
		this.type = type;
	}
}
