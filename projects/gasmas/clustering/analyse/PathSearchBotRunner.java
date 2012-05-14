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

import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;

/**
 * The Class PathSearchBotRuner.
 */
public class PathSearchBotRunner {

	/** The active search bots. */
	private ArrayList<PathSearchBot> activeSearchBots = new ArrayList<PathSearchBot>();

	/**
	 * Creates the initial bot.
	 *
	 * @param networkModel the network model
	 * @param startNCID the start ncid
	 */
	private void createInitialBot(NetworkModel networkModel, String startNCID) {
		PathSearchBot bot = new PathSearchBot(networkModel, startNCID);
		bot.setPathSearchBotRunner(this);
		activeSearchBots.add(bot);
	}

	/**
	 * Run bots and gets a distribution matrix.
	 *
	 * @param networkModel the network model
	 * @param startNCID the start ncid
	 * @param steps the steps
	 * @return the path search bot distribution matrix
	 */
	public PathSearchBotDistributionMatrix runBotsAndGetDistributionMatrix(NetworkModel networkModel, String startNCID, int steps) {
		createInitialBot(networkModel, startNCID);
		PathSearchBotDistributionMatrix antDistributionMatrix = new PathSearchBotDistributionMatrix();

		for (int step = 0; step < steps; step++) {
			if (activeSearchBots.size() < 1) {
				break;
			}
			for (PathSearchBot bot : new ArrayList<PathSearchBot>(activeSearchBots)) {
				if (!bot.run())
					activeSearchBots.remove(bot);
				if (!bot.isCycle()) {
					antDistributionMatrix.addAntToDynamicMatrix(bot);
				}
			}
		}
		// and Rest of the running bots
		for (PathSearchBot bot : activeSearchBots) {
			antDistributionMatrix.addAntToDynamicMatrix(bot);
		}
		return antDistributionMatrix;
	}

	/**
	 * Run bots and get path serach bot circle analyser.
	 *
	 * @param networkModel the network model
	 * @param startNCID the start ncid
	 * @param steps the steps
	 * @return the path serach bot circle analyser
	 */
	public PathSerachBotCycleAnalyser runBotsAndGetPathSerachBotCircleAnalyser(NetworkModel networkModel, String startNCID, int steps) {
		createInitialBot(networkModel, startNCID);
		PathSerachBotCycleAnalyser antCircleAnalyser = new PathSerachBotCycleAnalyser(networkModel);

		for (int step = 0; step < steps; step++) {
			if (activeSearchBots.size() < 1) {
				break;
			}
			for (PathSearchBot bot : new ArrayList<PathSearchBot>(activeSearchBots)) {
				if (!bot.run())
					activeSearchBots.remove(bot);
				if (bot.isCycle()) {
					antCircleAnalyser.addPathSearchBotToSubgraphs(bot);
				}
			}
		}
		return antCircleAnalyser;
	}

	/**
	 * Check cluster circle.
	 *
	 * @param clusterNetworkComponent the cluster network component
	 * @return true, if successful
	 */
	public boolean checkClusterCircle(ClusterNetworkComponent clusterNetworkComponent) {
		createInitialBot(clusterNetworkComponent.getClusterNetworkModel(), clusterNetworkComponent.getConnectionNetworkComponents().get(0).getId());

		while (activeSearchBots.size() > 0) {
			for (PathSearchBot bot : new ArrayList<PathSearchBot>(activeSearchBots)) {
				if (!bot.run()) {
					activeSearchBots.remove(bot);
					if (bot.isCycle()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Adds the path search bot to run.
	 *
	 * @param pathSearchBot the path search bot
	 */
	public void addPathSearchBotToRun(PathSearchBot pathSearchBot) {
		activeSearchBots.add(pathSearchBot);
	}
}
