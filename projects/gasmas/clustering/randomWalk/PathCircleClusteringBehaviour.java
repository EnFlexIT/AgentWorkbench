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
package gasmas.clustering.randomWalk;

import gasmas.clustering.ClusteringBehaviour;
import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.environment.EnvironmentModel;

/**
 * The Class PathCircleClusteringBehaviour.
 */
public class PathCircleClusteringBehaviour extends ClusteringBehaviour {

	/** The Constant STEPS. */
	private static final int STEPS = 70;

	/** The network model. */
	private NetworkModel networkModel;

	/**
	 * Instantiates a new path circle clustering behaviour.
	 *
	 * @param environmentModel the environment model
	 */
	public PathCircleClusteringBehaviour(EnvironmentModel environmentModel) {
		this.networkModel = (NetworkModel) environmentModel.getDisplayEnvironment();
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action() {
		// System.out.println("Begin Ant Circle Cluster Analysis " + new Date());
		analyseClusters();
		// System.out.println("End Ant Circle Cluster Analysis " + new Date());
	}

	/**
	 * Analyse clusters.
	 */
	public void analyseClusters() {
		Subgraph subgraph = startPathAnalysis(networkModel);
		NetworkModel copyNetworkModel = networkModel.getCopy();
		copyNetworkModel.setAlternativeNetworkModel(null);
		ClusterNetworkComponent clusterNetworkComponent = copyNetworkModel.replaceComponentsByCluster(subgraph.getNetworkComponents(copyNetworkModel));
		coalitionBehaviour.checkSuggestedCluster(clusterNetworkComponent);
	}

	/**
	 * Start path analysis.
	 *
	 * @param newNetworkModel the new network model
	 * @return the subgraph
	 */
	private Subgraph startPathAnalysis(NetworkModel newNetworkModel) {
		// System.out.println("Start Analysing Circle");
		PathSerachBotCycleAnalyser pathSerachBotCircleAnalyser = new PathSearchBotRunner().runBotsAndGetPathSerachBotCircleAnalyser(newNetworkModel, coalitionBehaviour.getThisNetworkComponent().getId(),
				PathCircleClusteringBehaviour.STEPS);
		System.out.println(coalitionBehaviour.getThisNetworkComponent().getId() + "-" + pathSerachBotCircleAnalyser.getBestSubgraph());
		return pathSerachBotCircleAnalyser.getBestSubgraph();
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#done()
	 */
	@Override
	public boolean done() {
		return true;
	}
}
