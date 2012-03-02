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
package gasmas.clustering;

import jade.core.ServiceException;
import jade.core.behaviours.SimpleBehaviour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;

/**
 * The Class EdgeBetweenessBehaviour. Based on the alreay implemented EdgeBetweenessClusterer of the Jung 2.0 Framework. Some changes to fullfill the needs of network clustering
 */
public class AntClusteringBehaviour extends SimpleBehaviour {

	private static final int STEPS = 10;
	private static final int ANTS = 1000;
	private static final long serialVersionUID = -1944492299919314055L;

	/** The environment model. */
	private NetworkModel networkModel;
	private EnvironmentModel environmentModel;
	private SimulationServiceHelper simulationServiceHelper;

	/** The this network component. */
	private NetworkComponent thisNetworkComponent;

	/**
	 * Instantiates a new edge betweeness behaviour.
	 * 
	 * @param environmentModel the environment model
	 */
	public AntClusteringBehaviour(EnvironmentModel environmentModel, NetworkComponent thisNetworkComponent) {
		this.environmentModel = environmentModel;
		this.networkModel = (NetworkModel) environmentModel.getDisplayEnvironment();
		this.thisNetworkComponent = thisNetworkComponent;
	}

	/**
	 * action to cluster the network
	 * 
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action() {
		try {
			simulationServiceHelper = (SimulationServiceHelper) myAgent.getHelper(SimulationService.NAME);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		ClusterIdentifier clusterIdentifier = new ClusterIdentifier(environmentModel, simulationServiceHelper);
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

	public void analyseClusters(NetworkModel networkModel, ClusterIdentifier clusterIdentifier) {
		NetworkModel newNetworkModel = networkModel.getCopy();
		// while (newNetworkModel != null) {
		newNetworkModel = clusterIdentifier.search(startAntAnalysis(newNetworkModel), networkModel);
		// }
	}

	private NetworkModel startAntAnalysis(NetworkModel networkModel) {
		ArrayList<Ant> ants = new ArrayList<Ant>();
		for (int i = 0; i < ANTS; i++) {
			ants.add(new Ant(thisNetworkComponent));
		}
		for (int step = 0; step < AntClusteringBehaviour.STEPS; step++) {
			for (Ant ant : ants) {
				ant.run(networkModel);
			}
		}
		findBorderNetworkCompoennts(new AntDistributionMatrix(ants));
		return networkModel;
	}

	private void findBorderNetworkCompoennts(AntDistributionMatrix distributionMatrix) {
		HashMap<String, ArrayList<Integer>> distribution = distributionMatrix.getDistribution();
		for (Entry<String, ArrayList<Integer>> entry : distribution.entrySet()) {
			System.out.printf("%-4s :", entry.getKey());
			for (Integer integer : entry.getValue()) {
				System.out.printf("%6d", integer);
			}
			System.out.println();
		}
	}

	/**
	 * removes a component from the copy of the network model
	 * 
	 * @param networkModel
	 */
	private NetworkModel removeComponents(NetworkModel networkModel, ArrayList<String> networkCompoenntsIDs) {
		for (String networkComponentID : networkCompoenntsIDs) {
			NetworkComponent networkComponent = networkModel.getNetworkComponent(networkComponentID);
			if (networkComponent != null) {
				networkModel.removeNetworkComponent(networkComponent);
			}
		}

		this.networkModel.getAlternativeNetworkModel().put("EdgeBetweeness", networkModel);
		this.environmentModel.setDisplayEnvironment(this.networkModel);
		try {
			simulationServiceHelper.setEnvironmentModel(this.environmentModel, true);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return networkModel;
	}

	/**
	 * @see jade.core.behaviours.Behaviour#done()
	 * 
	 *      return if the Behaviour is finished or remains active
	 */
	@Override
	public boolean done() {
		return true;
	}

}
