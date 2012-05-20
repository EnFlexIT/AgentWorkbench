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
package gasmas.clustering.behaviours;

import gasmas.clustering.analyse.ClusterIdentifier;
import gasmas.clustering.analyse.PathSearchBotRunner;
import gasmas.clustering.analyse.PathSerachBotCycleAnalyser;
import gasmas.clustering.analyse.Subgraph;
import jade.core.Agent;

import java.util.Date;

import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;

/**
 * The Class PathCircleClusteringBehaviour.
 */
public class CycleClusteringBehaviour extends ClusteringBehaviour {

	/** The Constant STEPS. */
	private static final int STEPS = 50;


	public CycleClusteringBehaviour(Agent agent, NetworkModel networkModel) {
		super(agent, networkModel);
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action() {
		analyseClusters();
	}

	/**
	 * Analyse clusters.
	 */
	public void analyseClusters() {
		Date begin = new Date();
		System.out.println("Begin CircleClusteringBehaviour for " + myAgent.getLocalName() + " " + begin.getTime());

		Subgraph subgraph = startPathAnalysis(networkModel);
		if (subgraph != null) {
			NetworkModel copyNetworkModel = getClusterNM();
			ClusterNetworkComponent clusterNetworkComponent = copyNetworkModel.replaceComponentsByCluster(subgraph.getNetworkComponents(copyNetworkModel));
			coalitionBehaviour.checkSuggestedCluster(clusterNetworkComponent, true);
		}
		Date end = new Date();
		System.out.println("End CircleClusteringBehaviour for " + myAgent.getLocalName() + " " + end.getTime() + " Duration: " + (end.getTime() - begin.getTime()));
	}

	/**
	 * Start path analysis.
	 *
	 * @param newNetworkModel the new network model
	 * @return the subgraph
	 */
	private Subgraph startPathAnalysis(NetworkModel newNetworkModel) {
		PathSerachBotCycleAnalyser pathSerachBotCircleAnalyser = new PathSearchBotRunner().runBotsAndGetPathSerachBotCircleAnalyser(newNetworkModel, coalitionBehaviour.getThisNetworkComponent().getId(),
				CycleClusteringBehaviour.STEPS);
		return pathSerachBotCircleAnalyser.getBestSubgraph();
	}

	@Override
	public void analyseClusters(NetworkModel networkModel, ClusterIdentifier clusterIdentifier) {
		// Dummy
	}
}
