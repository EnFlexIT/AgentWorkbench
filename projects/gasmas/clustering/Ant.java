package gasmas.clustering;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;

public class Ant {
	private static Random random = new Random();

	private ArrayList<String> path = new ArrayList<String>();
	private boolean active = true;

	public Ant(String startComponentID) {
		path.add(startComponentID);
	}

	public Ant(NetworkComponent networkComponent) {
		path.add(networkComponent.getId());
	}

	/**
	 * Ant goes to the next NetworkComponent returns NetworkComponentID or null if already Home or End reached
	 * 
	 * @param networkModel
	 * @return boolean  Home/End arrived
	 */
	public String run(NetworkModel networkModel) {
		if (!active) {
			return null;
		}
		NetworkComponent lastNC = networkModel.getNetworkComponent(path.get(path.size() - 1));
		Vector<NetworkComponent> nextNCs = networkModel.getNeighbourNetworkComponents(lastNC);
		// don't go back
		if (path.size() > 1) {
			lastNC = networkModel.getNetworkComponent(path.get(path.size() - 2));
			for (NetworkComponent networkComponent : new ArrayList<NetworkComponent>(nextNCs)) {
				if (networkComponent.equals(lastNC)) {
					nextNCs.remove(networkComponent);
				}
			}
		}
		switch (nextNCs.size()) {
		case 0:
			active = false;
			return null;
		case 1:
			path.add(nextNCs.get(0).getId());
			break;

		default:
			double possibility = 1.0 / nextNCs.size();
			int next = (int) Math.floor(random.nextDouble() / possibility);
			path.add(nextNCs.get(next).getId());
		}
		if (path.get(path.size() - 1).equals(path.get(0))) {
			active = false;
			return null;
		}
		return path.get(path.size() - 1);
	}

	/**
	 * Ant still running?
	 * @return boolean running
	 */
	public boolean getActive() {
		return active;
	}

	public ArrayList<String> getPath() {
		return path;
	}
}
