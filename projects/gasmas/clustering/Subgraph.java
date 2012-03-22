package gasmas.clustering;

import java.util.ArrayList;
import java.util.HashSet;

import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;

public class Subgraph {
	private ArrayList<String> networkComponents;
	private ArrayList<String> interfaceNetworkComponents;

	public Subgraph(ArrayList<String> networkComponents, ArrayList<String> interfaceNetworkComponents) {
		this.networkComponents = networkComponents;
		this.interfaceNetworkComponents = interfaceNetworkComponents;
	}

	public ArrayList<String> getInterfaceNetworkComponents() {
		return interfaceNetworkComponents;
	}

	public ArrayList<String> getNetworkComponents() {
		return networkComponents;
	}

	public HashSet<NetworkComponent> getNetworkComponents(NetworkModel networkModel) {
		HashSet<NetworkComponent> networkComponents = new HashSet<NetworkComponent>();
		for (String networkComponentID : this.networkComponents) {
			networkComponents.add(networkModel.getNetworkComponent(networkComponentID));
		}
		return networkComponents;
	}

	@Override
	public String toString() {
		String string = "";
		for (String networkComponentID : networkComponents) {
			string += networkComponentID + ";";
		}
		string += "   Interfaces:";
		for (String networkComponentID : interfaceNetworkComponents) {
			string += networkComponentID + ";";
		}
		return string;
	}
}
