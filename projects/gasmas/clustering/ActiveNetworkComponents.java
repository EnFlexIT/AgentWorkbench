package gasmas.clustering;

import java.util.ArrayList;

import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;

public class ActiveNetworkComponents {

	/** The active network component agent class prefix. */
	private static final String activeNetworkComponentAgentClassPrefix = "gasmas.agents.components.";

	/** The active network component agent classes. */
	private final static String[] activeNetworkComponentAgentClasses = new String[] { "CompressorAgent", "EntryAgent", "ExitAgent", "StorageAgent" };

	
	
	/**
	 * Identify active components.
	 */
	public static ArrayList<NetworkComponent> getActiveNetworkComponents(NetworkModel networkModel) {
		
		ArrayList<NetworkComponent> activeNetworkComponents = new ArrayList<NetworkComponent>();
		for (NetworkComponent networkComponent : networkModel.getNetworkComponents().values()) {
			String agentClassName = networkComponent.getAgentClassName();
			if (agentClassName != null) {
				for (String activeAgentClassType : activeNetworkComponentAgentClasses) {
					if (agentClassName.equals(activeNetworkComponentAgentClassPrefix + activeAgentClassType)) {
						activeNetworkComponents.add(networkComponent);
					}
				}
			}
		}
		return activeNetworkComponents;
	}


	
}
