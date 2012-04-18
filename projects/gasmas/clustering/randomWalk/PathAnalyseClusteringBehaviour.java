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

import gasmas.clustering.ClusterIdentifier;
import jade.core.ServiceException;
import jade.core.behaviours.SimpleBehaviour;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;

/**
 * The Class PathAnalyseClusteringBehaviour.
 */
public class PathAnalyseClusteringBehaviour extends SimpleBehaviour {

	/** The Constant STEPS. */
	private static final int STEPS = 500;

	/** The environment model. */
	private EnvironmentModel environmentModel;

	/** The network model. */
	private NetworkModel networkModel;

	/** The this network component. */
	private NetworkComponent thisNetworkComponent;

	/** The simulation service helper. */
	private SimulationServiceHelper simulationServiceHelper;

	/**
	 * Instantiates a new path analyse clustering behaviour.
	 *
	 * @param environmentModel the environment model
	 * @param thisNetworkComponent the this network component
	 */
	public PathAnalyseClusteringBehaviour(EnvironmentModel environmentModel, NetworkComponent thisNetworkComponent) {
		this.environmentModel = environmentModel;
		this.networkModel = (NetworkModel) environmentModel.getDisplayEnvironment();
		this.thisNetworkComponent = thisNetworkComponent;
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action() {
		try {
			simulationServiceHelper = (SimulationServiceHelper) myAgent.getHelper(SimulationService.NAME);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		ClusterIdentifier clusterIdentifier = new ClusterIdentifier();
		NetworkModel copyNetworkModel = networkModel.getCopy();
		copyNetworkModel.setAlternativeNetworkModel(null);
		this.networkModel.getAlternativeNetworkModel().put("ClusteredModel", copyNetworkModel);
		try {
			simulationServiceHelper.setEnvironmentModel(this.environmentModel, true);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		System.out.println("Begin Ant Cluster Analysis");
		analyseClusters(copyNetworkModel, clusterIdentifier);
	}

	/**
	 * Analyse clusters.
	 *
	 * @param networkModel the network model
	 * @param clusterIdentifier the cluster identifier
	 */
	public void analyseClusters(NetworkModel networkModel, ClusterIdentifier clusterIdentifier) {
		NetworkModel newNetworkModel = networkModel.getCopy();
		while (newNetworkModel != null && newNetworkModel.getNetworkComponent(thisNetworkComponent.getId()) != null) {
			newNetworkModel = clusterIdentifier.search(startPathAnalysis(newNetworkModel), networkModel);
		}
	}

	/**
	 * Start path analysis.
	 *
	 * @param newNetworkModel the new network model
	 * @return the network model
	 */
	private NetworkModel startPathAnalysis(NetworkModel newNetworkModel) {
		PathSearchBotDistributionMatrix antDistributionMatrix = new PathSearchBotRunner().runBotsAndGetDistributionMatrix(newNetworkModel, thisNetworkComponent.getId(),
				PathAnalyseClusteringBehaviour.STEPS);
		String x = antDistributionMatrix.findFrequentPathComponent();
		NetworkComponent networkComponent = newNetworkModel.getNetworkComponent(x);
		newNetworkModel.removeNetworkComponent(networkComponent);
		return newNetworkModel;
	}

	@Override
	public boolean done() {
		return true;
	}
}
