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
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;

/**
 * The Class ShortestPathClusteringBehaviour.
 */
public class ShortestPathClusteringBehaviour extends SimpleBehaviour {

	private static final long serialVersionUID = -2788112416597375066L;

	/** The network model. */
	private NetworkModel networkModel;

	private EnvironmentModel environmentModel;

	/** The this network component. */
	private NetworkComponent thisNetworkComponent;

	private SimulationServiceHelper simulationServiceHelper;

	/**
	 * Instantiates a new shortest path clustering behaviour.
	 * 
	 * @param environmentModel the environment model
	 * @param networkManagerAgent the network manager agent
	 * @param thisNetworkComponent the this network component
	 */
	public ShortestPathClusteringBehaviour(EnvironmentModel environmentModel, NetworkComponent thisNetworkComponent) {
		this.environmentModel = environmentModel;
		this.networkModel = (NetworkModel) environmentModel.getDisplayEnvironment();
		this.thisNetworkComponent = thisNetworkComponent;
	}

	/*
	 * (non-Javadoc)
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
		ClusterIdentifier clusterIdentifier = new ClusterIdentifier();
		NetworkModel clusterNM = networkModel.getCopy();
		clusterNM.setAlternativeNetworkModel(null);
		this.networkModel.getAlternativeNetworkModel().put("ClusteredModel", clusterNM);
		analyseClusters(clusterNM, clusterIdentifier);
	}

	public void analyseClusters(NetworkModel networkModel, ClusterIdentifier clusterIdentifier) {
		NetworkModel copyNetworkModel = networkModel.getCopy();
		copyNetworkModel.setAlternativeNetworkModel(null);
		while (copyNetworkModel != null) {
			HashSet<NetworkComponent> mostFrequent = findMostFrequentNetworkComponent(findShortestPaths(copyNetworkModel), ActiveNetworkComponents.getActiveNetworkComponents(copyNetworkModel));
			if (mostFrequent.size() == 1 && mostFrequent.iterator().next() == null) {
				copyNetworkModel = null;
			} else {
				for (NetworkComponent networkComponent : mostFrequent) {
					if (networkComponent != null) {
						copyNetworkModel.removeNetworkComponent(networkComponent);
					}
					this.networkModel.getAlternativeNetworkModel().put("EdgeBetweeness" + thisNetworkComponent.getId(), copyNetworkModel);
					this.environmentModel.setDisplayEnvironment(this.networkModel);
					try {
						simulationServiceHelper.setEnvironmentModel(this.environmentModel, true);
					} catch (ServiceException e) {
						e.printStackTrace();
					}
				}
				copyNetworkModel = clusterIdentifier.search(copyNetworkModel, networkModel);
			}
		}
	}

	/**
	 * Find shortest path.
	 */
	private ArrayList<NetworkPath> findShortestPaths(NetworkModel workingCopyNetworkModel) {
		ArrayList<NetworkComponent> activeNCs = ActiveNetworkComponents.getActiveNetworkComponents(workingCopyNetworkModel);
		ArrayList<NetworkPath> paths = new ArrayList<NetworkPath>();
		for (int c1 = 0; c1 < activeNCs.size(); c1++) {
			for (int c2 = c1 + 1; c2 < activeNCs.size(); c2++) {
				if (workingCopyNetworkModel.getNetworkComponent(activeNCs.get(c1).getId()) != null && workingCopyNetworkModel.getNetworkComponent(activeNCs.get(c2).getId()) != null) {
					paths.add(findNetworkPath(activeNCs.get(c1), activeNCs.get(c2), workingCopyNetworkModel));
				}
			}
		}
		return paths;
	}

	/**
	 * Find network path.
	 * 
	 * @param networkComponent the network component
	 * @param workingCopyNetworkModel the working copy network model
	 * @return the network path
	 */
	private NetworkPath findNetworkPath(NetworkComponent fromNC, NetworkComponent toNC, NetworkModel workingCopyNetworkModel) {
		DijkstraShortestPath<GraphNode, GraphEdge> dijkstraShortestPath = new DijkstraShortestPath<GraphNode, GraphEdge>(workingCopyNetworkModel.getGraph());
		Vector<GraphNode> nodes = workingCopyNetworkModel.getNodesFromNetworkComponent(toNC);
		Vector<GraphNode> nodesOfGoal = workingCopyNetworkModel.getNodesFromNetworkComponent(fromNC);
		List<GraphEdge> path = dijkstraShortestPath.getPath(nodes.iterator().next(), nodesOfGoal.iterator().next());
		return new NetworkPath(workingCopyNetworkModel, path);
	}

	/**
	 * Find most frequent network component.
	 * 
	 * @return the network component
	 */
	public HashSet<NetworkComponent> findMostFrequentNetworkComponent(ArrayList<NetworkPath> paths, ArrayList<NetworkComponent> aNCs) {
		HashMap<NetworkComponent, Integer> componentCounter = new HashMap<NetworkComponent, Integer>();
		for (NetworkPath networkPath : paths) {
			for (NetworkComponent networkComponent : networkPath.getPath()) {
				componentCounter.put(networkComponent, componentCounter.get(networkComponent) == null ? new Integer(1) : new Integer(componentCounter.get(networkComponent).intValue() + 1));
			}
		}
		int max = 0;
		NetworkComponent networkComponent = null;
		for (Entry<NetworkComponent, Integer> entry : componentCounter.entrySet()) {
			if (entry.getValue().intValue() > max && !isActiveNC(entry.getKey(), aNCs)) {
				max = entry.getValue().intValue();
				networkComponent = entry.getKey();
			}
		}
		HashSet<NetworkComponent> maxNetworkComponents = new HashSet<NetworkComponent>();
		maxNetworkComponents.add(networkComponent);
		for (Entry<NetworkComponent, Integer> entry : componentCounter.entrySet()) {
			if (entry.getValue().intValue() == max && !isActiveNC(entry.getKey(), aNCs)) {
				maxNetworkComponents.add(entry.getKey());
			}
		}
		return maxNetworkComponents;
	}

	private boolean isActiveNC(NetworkComponent networkComponent, ArrayList<NetworkComponent> activeNCs) {
		for (NetworkComponent aNC : activeNCs) {
			if (aNC.getId().equals(networkComponent.getId())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jade.core.behaviours.Behaviour#done()
	 */
	@Override
	public boolean done() {
		return true;
	}
}
