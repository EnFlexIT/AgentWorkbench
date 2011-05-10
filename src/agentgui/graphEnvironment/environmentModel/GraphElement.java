package agentgui.graphEnvironment.environmentModel;
/**
 * Abstract super class for nodes and edges in an environment model of the type graph / network.
 * @author Nils
 *
 */
public abstract class GraphElement {
	/**
	 * Used for identification of a specific GraphElement instance
	 */
	protected String id = null;

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

}
