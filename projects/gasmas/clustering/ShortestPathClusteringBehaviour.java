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

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import gasmas.agents.manager.NetworkManagerAgent;
import jade.core.behaviours.SimpleBehaviour;

import java.util.List;

import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.environment.EnvironmentModel;

/**
 * The Class ShortestPathClusteringBehaviour.
 */
public class ShortestPathClusteringBehaviour extends SimpleBehaviour {

    /** The network model. */
    private NetworkModel networkModel;

    /** The network manager agent. */
    private NetworkManagerAgent networkManagerAgent;

    /** The this network component. */
    private NetworkComponent thisNetworkComponent;

    /** The shortest path blackboard. */
    private ShortestPathBlackboard shortestPathBlackboard;

    /**
     * Instantiates a new shortest path clustering behaviour.
     * 
     * @param environmentModel the environment model
     * @param networkManagerAgent the network manager agent
     * @param thisNetworkComponent the this network component
     */
    public ShortestPathClusteringBehaviour(EnvironmentModel environmentModel, NetworkManagerAgent networkManagerAgent, NetworkComponent thisNetworkComponent) {
	this.networkModel = (NetworkModel) environmentModel.getDisplayEnvironment();
	this.networkManagerAgent = networkManagerAgent;
	this.thisNetworkComponent = thisNetworkComponent;
	this.shortestPathBlackboard = networkManagerAgent.getShortestPathBlackboard();
    }

    /*
     * (non-Javadoc)
     * 
     * @see jade.core.behaviours.Behaviour#action()
     */
    @Override
    public void action() {
	findShortestPath();
    }

    /**
     * Find shortest path.
     */
    private void findShortestPath() {
	NetworkModel workingCopyNetworkModel = networkModel.getCopy();

	for (NetworkComponent networkComponent : networkManagerAgent.getActiveNetworkComponents()) {
	    if (networkComponent != thisNetworkComponent) {
		if (!shortestPathBlackboard.contains(networkComponent, thisNetworkComponent)) {
		    findNetworkPath(networkComponent, workingCopyNetworkModel);
		}
	    }
	}
    }

    /**
     * Find network path.
     * 
     * @param networkComponent the network component
     * @param workingCopyNetworkModel the working copy network model
     * @return the network path
     */
    private NetworkPath findNetworkPath(NetworkComponent networkComponent, NetworkModel workingCopyNetworkModel) {
	DijkstraShortestPath<GraphNode, GraphEdge> dijkstraShortestPath = new DijkstraShortestPath<GraphNode, GraphEdge>(workingCopyNetworkModel.getGraph());
	List<GraphEdge> path = dijkstraShortestPath.getPath(workingCopyNetworkModel.getANodeFromNetworkComponent(thisNetworkComponent),
		workingCopyNetworkModel.getANodeFromNetworkComponent(networkComponent));
	return new NetworkPath(workingCopyNetworkModel, path);
    }

    /*
     * (non-Javadoc)
     * 
     * @see jade.core.behaviours.Behaviour#done()
     */
    @Override
    public boolean done() {
	// TODO Auto-generated method stub
	return false;
    }
}
