package agentgui.envModel.graph.controller.yedGraphml;

/**
 * This class is only needed to temporary represent yEd GraphML Nodes during the import process
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 *
 */
public class TempNode {
	private String id;
	private String type;
	
	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public TempNode(String id, String type) {
		super();
		this.id = id;
		this.type = type;
	}
	
}
