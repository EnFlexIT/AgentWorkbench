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

import jade.core.behaviours.SimpleBehaviour;

import java.util.List;

import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.environment.EnvironmentModel;
import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.graph.Graph;

/**
 * The Class EdgeBetweenessBehaviour. Based on the alreay implemented EdgeBetweenessClusterer of the Jung 2.0 Framework. Some changes to fullfill the needs of network clustering
 */
public class EdgeBetweenessBehaviour extends SimpleBehaviour {

    /** The environment model. */
    private NetworkModel networkModel;

    /**
     * Instantiates a new edge betweeness behaviour.
     * 
     * @param environmentModel the environment model
     */
    public EdgeBetweenessBehaviour(EnvironmentModel environmentModel) {
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
	removeComponent(networkModel);
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
     * TODO: this has to run recursive
     * 
     * @param networkModel
     */
    private void removeComponent(NetworkModel networkModel) {
	NetworkModel workingCopyNetworkModel = networkModel.getCopy();
	GraphEdge edge = removeEdge(workingCopyNetworkModel.getGraph());
	if (edge == null) {
	    return;
	}
	workingCopyNetworkModel.removeNetworkComponent(workingCopyNetworkModel.getNetworkComponent(edge));
	System.out.println(workingCopyNetworkModel.getNetworkComponent(edge));

	networkModel.getAlternativeNetworkModel().put("Test", workingCopyNetworkModel);
	System.out.println("Z");

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
