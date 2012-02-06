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

import java.util.List;

import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;
import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.graph.Graph;

/**
 * The Class EdgeBetweenessBehaviour. Based on the alreay implemented EdgeBetweenessClusterer of the Jung 2.0 Framework. Some changes to fullfill the needs of network clustering
 */
public class EdgeBetweenessBehaviour extends SimpleBehaviour {

	private static final long serialVersionUID = -1944492299919314055L;

	/** The environment model. */
	private NetworkModel networkModel;
	private EnvironmentModel environmentModel;

	/**
	 * Instantiates a new edge betweeness behaviour.
	 * 
	 * @param environmentModel the environment model
	 */
	public EdgeBetweenessBehaviour(EnvironmentModel environmentModel) {
		this.environmentModel = environmentModel;
		this.networkModel = (NetworkModel) environmentModel.getDisplayEnvironment();
	}

	/**
	 * action to cluster the network
	 * 
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action() {

		System.out.println("Begin Edge Betweness Cluster Analysis");
		NetworkModel newNetModel = removeComponent(networkModel.getCopy());
		for (int i = 0; i < 25; i++) {
			newNetModel = removeComponent(newNetModel);
		}

	}

	/**
	 * identifies a removable Edge with the Jung EdgeBetwenessClusterer
	 * 
	 * @param graph
	 * @return
	 */

	private GraphEdge removeEdge(Graph<GraphNode, GraphEdge> graph) {
		EdgeBetweennessClusterer<GraphNode, GraphEdge> edgeBetweennessClusterer = new EdgeBetweennessClusterer<GraphNode, GraphEdge>(1);
		edgeBetweennessClusterer.transform(graph);
		List<GraphEdge> edges = edgeBetweennessClusterer.getEdgesRemoved();
		if (edges.size() < 1) {
			return null;
		}
		return edges.get(0);
	}

	/**
	 * removes a component from the copy of the network model
	 * 
	 * @param networkModel
	 */
	private NetworkModel removeComponent(NetworkModel networkModel) {

		NetworkModel workingCopyNetworkModel = networkModel.getCopy();
		GraphEdge edge = removeEdge(workingCopyNetworkModel.getGraph());
		if (edge == null) {
			return networkModel;
		}
		workingCopyNetworkModel.removeNetworkComponent(workingCopyNetworkModel.getNetworkComponent(edge));

		this.networkModel.getAlternativeNetworkModel().put("EdgeBetweeness", workingCopyNetworkModel);
		this.environmentModel.setDisplayEnvironment(this.networkModel);

		// --- Put the environment model into the SimulationService -
		// --- in order to make it accessible for the whole agency --
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) myAgent.getHelper(SimulationService.NAME);
			simHelper.setEnvironmentModel(this.environmentModel, true);

		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return workingCopyNetworkModel;
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
