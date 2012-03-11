package gasmas.clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Vector;

import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;

public class Ant {
	private static Random random = new Random();
	private static boolean clone = false;

	private ArrayList<String> path = new ArrayList<String>();
	private HashMap<String, ArrayList<String>> altrnativePaths = new HashMap<String, ArrayList<String>>();
	private boolean active = true;

	public Ant(String startComponentID) {
		path.add(startComponentID);
	}

	public Ant(NetworkComponent networkComponent) {
		path.add(networkComponent.getId());
	}

	public Ant(Ant ant) {
		path = new ArrayList<String>(ant.getPath());
		for (Entry<String, ArrayList<String>> entry : ant.getAlternativePaths().entrySet()) {
			altrnativePaths.put(entry.getKey(), new ArrayList<String>(entry.getValue()));
		}
		active = ant.getActive();
	}

	public void run(int no, Vector<NetworkComponent> nextNCs) {
		path.add(nextNCs.get(no).getId());
		addAlternativePaths(nextNCs.get(no), nextNCs);
	}

	private Vector<NetworkComponent> getNextsNCs(NetworkModel networkModel, String componentID) {
		Vector<NetworkComponent> nextNCs = networkModel.getNeighbourNetworkComponents(networkModel.getNetworkComponent(componentID));
		// don't go back
		if (path.size() > 1) {
			ArrayList<NetworkComponent> networkComponents = new ArrayList<NetworkComponent>();
			if (Ant.clone) {
				networkComponents = getPathNetworkComponents(networkModel);
			} else {
				networkComponents.add(networkModel.getNetworkComponent(path.get(path.size() - 2)));
			}
			removePreviousNCs(networkComponents, nextNCs);
		}
		return nextNCs;
	}

	private ArrayList<NetworkComponent> getPathNetworkComponents(NetworkModel networkModel) {
		ArrayList<NetworkComponent> networkComponents = new ArrayList<NetworkComponent>();
		for (String componentID : path) {
			networkComponents.add(networkModel.getNetworkComponent(componentID));
		}
		return networkComponents;
	}

	private void removePreviousNCs(ArrayList<NetworkComponent> toRemove, Vector<NetworkComponent> nextNCs) {
		for (NetworkComponent networkComponent : new ArrayList<NetworkComponent>(nextNCs)) {
			if (toRemove.contains(networkComponent)) {
				nextNCs.remove(networkComponent);
			}
		}
	}

	/**
	 * Ant goes to the next NetworkComponent returns NetworkComponentID or null if already Home or End reached
	 * 
	 * @param networkModel
	 * @return boolean  Home/End arrived
	 */
	public ArrayList<Ant> run(NetworkModel networkModel) {
		ArrayList<Ant> activeAnts = new ArrayList<Ant>();
		if (!active) {
			return activeAnts;
		}
		Vector<NetworkComponent> nextNCs = getNextsNCs(networkModel, path.get(path.size() - 1));
		switch (nextNCs.size()) {
		case 0:
			active = false;
			return activeAnts;
		case 1:
			path.add(nextNCs.get(0).getId());
			activeAnts.add(this);
			break;

		default:
			double possibility = 1.0 / nextNCs.size();
			int next = (int) Math.floor(random.nextDouble() / possibility);
			// run Alternative Paths
			if (Ant.clone) {
				for (int i = 0; i < nextNCs.size(); i++) {
					if (i != next) {
						Ant ant = new Ant(this);
						ant.run(i, new Vector<NetworkComponent>(nextNCs));
						activeAnts.add(ant);
					}
				}
			}
			path.add(nextNCs.get(next).getId());
			this.addAlternativePaths(nextNCs.get(next), nextNCs);
			activeAnts.add(this);
		}
		if (path.get(path.size() - 1).equals(path.get(0))) {
			active = false;
			activeAnts.remove(this);
		}
		return activeAnts;
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

	public HashMap<String, ArrayList<String>> getAlternativePaths() {
		return altrnativePaths;
	}

	/**
	 * Adds Alterbtive Paths to a List 
	 * @param networkComponent
	 * @param networkComponents
	 */
	private void addAlternativePaths(NetworkComponent networkComponent, Vector<NetworkComponent> networkComponents) {
		networkComponents.remove(networkComponent);
		ArrayList<String> listNCIDs = new ArrayList<String>();
		for (NetworkComponent netComponent : networkComponents) {
			listNCIDs.add(netComponent.getId());
		}
		altrnativePaths.put(path.get(path.size() - 2), listNCIDs);
	}

	public static void setClone(boolean clone) {
		Ant.clone = clone;
	}
}
