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

import jade.core.ServiceException;
import jade.core.behaviours.SimpleBehaviour;

import java.util.ArrayList;
import java.util.HashSet;

import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;

/**
 * The Class PathCircleClusteringBehaviour.
 */
public class PathCircleClusteringBehaviour extends SimpleBehaviour {

	/** The Constant STEPS. */
	private static final int STEPS = 100;

	/** The environment model. */
	private EnvironmentModel environmentModel;

	/** The network model. */
	private NetworkModel networkModel;

	/** The this network component. */
	private NetworkComponent thisNetworkComponent;

	/** The simulation service helper. */
	private SimulationServiceHelper simulationServiceHelper;

	/**
	 * Instantiates a new path circle clustering behaviour.
	 *
	 * @param environmentModel the environment model
	 * @param thisNetworkComponent the this network component
	 */
	public PathCircleClusteringBehaviour(EnvironmentModel environmentModel, NetworkComponent thisNetworkComponent) {
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
		System.out.println("Begin Ant Circle Cluster Analysis");
		analyseClusters();
		System.out.println("End Ant Circle Cluster Analysis");
	}

	/**
	 * Analyse clusters.
	 */
	public void analyseClusters() {
		Subgraph subgraph = startPathAnalysis(networkModel);
		NetworkModel copyNetworkModel = networkModel.getCopy();
		copyNetworkModel.setAlternativeNetworkModel(null);
		ClusterNetworkComponent clusterNetworkComponent = copyNetworkModel.replaceComponentsByCluster(subgraph.getNetworkComponents(copyNetworkModel));
		this.networkModel.getAlternativeNetworkModel().put("ClusteredModel", copyNetworkModel);
		this.networkModel.getAlternativeNetworkModel().put(clusterNetworkComponent.getId(), clusterNetworkComponent.getClusterNetworkModel());
		try {
			simulationServiceHelper.setEnvironmentModel(this.environmentModel, true);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Start path analysis.
	 *
	 * @param newNetworkModel the new network model
	 * @return the subgraph
	 */
	private Subgraph startPathAnalysis(NetworkModel newNetworkModel) {
		HashSet<Ant> ants = runAnts(newNetworkModel);
		System.out.println("Start Analysing Circle; Ants: " + ants.size());
		AntCircleAnalyser antCircleAnalyser = new AntCircleAnalyser(new ArrayList<Ant>(ants), newNetworkModel);
		System.out.println("End Analysing Circle");
		return antCircleAnalyser.getBestSubgraph();
	}

	/**
	 * Does Random Walk with Ants
	 *
	 * @param newNetworkModel the new network model
	 * @return the hash set
	 */
	private HashSet<Ant> runAnts(NetworkModel newNetworkModel) {
		HashSet<Ant> ants = new HashSet<Ant>();
		ants.add(new Ant(newNetworkModel, thisNetworkComponent.getId()));
		ArrayList<Ant> nextRunAnts = new ArrayList<Ant>(ants);
		for (int step = 0; step < PathCircleClusteringBehaviour.STEPS; step++) {
			ArrayList<Ant> runAnts = new ArrayList<Ant>(nextRunAnts);
			nextRunAnts = new ArrayList<Ant>();
			for (Ant ant : runAnts) {
				nextRunAnts.addAll(ant.run());
			}
			if (nextRunAnts.size() == 0) {
				break;
			}
			ants.addAll(nextRunAnts);
		}
		return ants;
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#done()
	 */
	@Override
	public boolean done() {
		return true;
	}

}
