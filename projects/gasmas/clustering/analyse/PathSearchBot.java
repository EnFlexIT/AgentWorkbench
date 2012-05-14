/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package gasmas.clustering.analyse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Vector;

import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;

/**
 * The Class Ant.
 */
public class PathSearchBot {

	/** The network model. */
	private NetworkModel networkModel;
	private PathSearchBotRunner pathSearchBotRuner;

	/** The path. */
	private ArrayList<NetworkComponent> path = new ArrayList<NetworkComponent>();

	/** The altrnative paths. */
	private HashMap<String, ArrayList<String>> altrnativePaths = new HashMap<String, ArrayList<String>>();

	/** The circle. */
	private boolean circle;

	/**
	 * Instantiates a new ant.
	 *
	 * @param networkModel the network model
	 * @param startNC the start nc
	 */
	public PathSearchBot(NetworkModel networkModel, String startNCID) {
		this.networkModel = networkModel;
		path.add(networkModel.getNetworkComponent(startNCID));
	}

	/**
	 * Instantiates a new ant.
	 *
	 * @param bot the ant
	 */
	public PathSearchBot(NetworkModel networkModel, PathSearchBot bot) {
		this.networkModel = networkModel;
		pathSearchBotRuner = bot.getPathBotSearchBotRuner();
		path = new ArrayList<NetworkComponent>(bot.getPathNetworkComponents());
		for (Entry<String, ArrayList<String>> entry : bot.getAlternativePaths().entrySet()) {
			altrnativePaths.put(entry.getKey(), new ArrayList<String>(entry.getValue()));
		}
	}

	/**
	 * Gets the path network components.
	 *
	 * @return the path network components
	 */
	private ArrayList<NetworkComponent> getPathNetworkComponents() {
		return path;
	}

	/**
	 * checks if this is already a circle
	 * 
	 * @return
	 */
	private boolean checkForCycle() {
		if (new HashSet<NetworkComponent>(path).size() < path.size()-1) {
			circle = true;
			return true;
		}
		return false;
	}

	/**
	 * Ant goes to the next NetworkComponent returns NetworkComponentID or null if already Home or End reached.
	 *
	 * @return boolean  Home/End arrived
	 */
	public boolean run() {
		if (checkForCycle()) {
			return false;
		}
		Vector<NetworkComponent> nextNCs = networkModel.getNeighbourNetworkComponents(path.get(path.size() - 1));
		nextNCs = new Vector<NetworkComponent>(new HashSet<NetworkComponent>(nextNCs));
		if (path.size() > 1) {
			nextNCs.remove(path.get(path.size() - 2));
		}
		switch (nextNCs.size()) {
		case 0:
			return false;
		case 1:
			path.add(nextNCs.get(0));
			break;

		default:
			createPathSearchBotClones(nextNCs);
			path.add(nextNCs.get(0));
			this.addAlternativePaths(nextNCs.get(0), nextNCs);
		}
		return true;
	}

	/**
	 * Creates the path search bot clones for other branches
	 *
	 * @param nextNCs other Branch NetworkComponents
	 */
	private void createPathSearchBotClones(Vector<NetworkComponent> nextNCs) {
		for (int i = 1; i < nextNCs.size(); i++) {
			PathSearchBot bot = new PathSearchBot(networkModel, this);
			bot.run(i, new Vector<NetworkComponent>(nextNCs));
			pathSearchBotRuner.addPathSearchBotToRun(bot);
		}
	}

	/**
	 * Run for cloned Ant
	 *
	 * @param no the no
	 * @param nextNCs the next n cs
	 */
	public void run(int no, Vector<NetworkComponent> nextNCs) {
		path.add(nextNCs.get(no));
		addAlternativePaths(nextNCs.get(no), nextNCs);
	}

	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	public ArrayList<String> getPath() {
		ArrayList<String> pathNCIDs = new ArrayList<String>();
		for (NetworkComponent networkComponent : path) {
			pathNCIDs.add(networkComponent.getId());
		}
		return pathNCIDs;
	}

	/**
	 * Gets the alternative paths.
	 *
	 * @return the alternative paths
	 */
	public HashMap<String, ArrayList<String>> getAlternativePaths() {
		return altrnativePaths;
	}

	/**
	 * Checks if is circle.
	 *
	 * @return true, if is circle
	 */
	public boolean isCycle() {
		return circle;
	}

	/**
	 * Adds Alternative Paths to a List.
	 *
	 * @param networkComponent the network component
	 * @param networkComponents the network components
	 */
	private void addAlternativePaths(NetworkComponent networkComponent, Vector<NetworkComponent> networkComponents) {
		networkComponents.remove(networkComponent);
		ArrayList<String> listNCIDs = new ArrayList<String>();
		for (NetworkComponent netComponent : networkComponents) {
			listNCIDs.add(netComponent.getId());
		}
		altrnativePaths.put(path.get(path.size() - 2).getId(), listNCIDs);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String string = "";
		for (int step = 0; step < path.size(); step++) {
			string += path.get(step).getId() + ";";
		}
		return string;
	}

	/**
	 * Gets the all alternative components.
	 *
	 * @return the all alternative components
	 */
	public ArrayList<String> getAllAlternativeComponents() {
		ArrayList<String> alternativesList = new ArrayList<String>();
		for (ArrayList<String> alternatives : altrnativePaths.values()) {
			alternativesList.addAll(alternatives);
		}
		return alternativesList;
	}

	/**
	 * Sets the path search bot runner.
	 *
	 * @param pathSearchBotRuner the new path search bot runner
	 */
	public void setPathSearchBotRunner(PathSearchBotRunner pathSearchBotRuner) {
		this.pathSearchBotRuner = pathSearchBotRuner;
	}

	/**
	 * Gets the path bot search bot runer.
	 *
	 * @return the path bot search bot runer
	 */
	public PathSearchBotRunner getPathBotSearchBotRuner() {
		return pathSearchBotRuner;
	}
}
