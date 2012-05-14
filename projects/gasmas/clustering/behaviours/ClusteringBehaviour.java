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

import gasmas.clustering.analyse.ClusterCorrectionHeuristics;
import gasmas.clustering.analyse.ClusterIdentifier;
import gasmas.clustering.coalitions.CoalitionBehaviour;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

import java.util.ArrayList;

import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;

/**
 * The Class ClusteringBehaviour.
 */
abstract public class ClusteringBehaviour extends OneShotBehaviour {

	/** The Constant CLUSTER_NETWORK_MODL_NAME. */
	public static final String CLUSTER_NETWORK_MODL_NAME = "ClusterdNM";

	/** The coalition behaviour. */
	protected CoalitionBehaviour coalitionBehaviour;

	/** The network model. */
	protected NetworkModel networkModel;

	/**
	 * Instantiates a new path circle clustering behaviour.
	 *
	 * @param environmentModel the environment model
	 */
	public ClusteringBehaviour(Agent agent, NetworkModel networkModel) {
		myAgent = agent;
		this.networkModel = networkModel;
	}

	public void setNetworkModel(NetworkModel networkModel) {
		this.networkModel = networkModel;
	}

	/**
	 * Sets the coalition behaviours.
	 *
	 * @param coalitionBehaviour the new coalition behaviours
	 */
	public void setCoalitionBehaviours(CoalitionBehaviour coalitionBehaviour) {
		this.coalitionBehaviour = coalitionBehaviour;
	}

	/**
	 * Gets the coalition behaviour.
	 *
	 * @return the coalition behaviour
	 */
	public CoalitionBehaviour getCoalitionBehaviour() {
		return coalitionBehaviour;
	}

	/**
	 * Removes the sub clusters.
	 *
	 * @param clusterNetworkComponent the cluster network component
	 */
	protected void removeSubClusters(ClusterNetworkComponent clusterNetworkComponent) {
		for (NetworkComponent networkComponent : new ArrayList<NetworkComponent>(clusterNetworkComponent.getClusterNetworkModel().getNetworkComponents().values())) {
			if (networkComponent instanceof ClusterNetworkComponent) {
				removeSubClusters((ClusterNetworkComponent) networkComponent);
				clusterNetworkComponent.getClusterNetworkModel().replaceClusterByComponents((ClusterNetworkComponent) networkComponent);
			}
		}
	}

	/**
	 * Correct and set cluster.
	 *
	 * @param clusteredNM the clustered nm
	 * @param clusterIdentifier the cluster identifier
	 */
	protected void correctAndSetCluster(NetworkModel clusteredNM, ClusterIdentifier clusterIdentifier) {
		ClusterCorrectionHeuristics clusterCorrection = new ClusterCorrectionHeuristics(clusterIdentifier);
		clusterCorrection.checkNetwork(clusteredNM, "ClusteredModel");
		ClusterNetworkComponent clusterNetworkComponent = findCluster(clusteredNM);
		if (clusterNetworkComponent != null) {
			removeSubClusters(clusterNetworkComponent);
			coalitionBehaviour.checkSuggestedCluster(clusterNetworkComponent,true);
		}
	}

	/**
	 * Gets the cluster nm.
	 *
	 * @return the cluster nm
	 */
	protected NetworkModel getClusterNM() {
		NetworkModel clusteredNM = networkModel.getCopy();
		clusteredNM.setAlternativeNetworkModel(null);
		return clusteredNM;
	}

	/**
	 * Find networkModel component.
	 *
	 * @param networkModel the network model
	 * @return the cluster network component
	 */
	protected ClusterNetworkComponent findCluster(NetworkModel networkModel) {
		for (NetworkComponent networkComponent : networkModel.getNetworkComponents().values()) {
			if (networkComponent instanceof ClusterNetworkComponent) {
				ClusterNetworkComponent clusterNetworkComponent = findInCluster((ClusterNetworkComponent) networkComponent);
				if (clusterNetworkComponent != null) {
					return clusterNetworkComponent;
				}
			}
		}
		return null;
	}

	/**
	 * Find in cluster a the networkComponent and return the cluster or go to subcluster.
	 *
	 * @param clusterNetworkComponent the cluster network component
	 * @return the cluster network component
	 */
	private ClusterNetworkComponent findInCluster(ClusterNetworkComponent clusterNetworkComponent){
		for (NetworkComponent networkComponent : clusterNetworkComponent.getClusterNetworkModel().getNetworkComponents().values()) {
			if (networkComponent.getId().equals(coalitionBehaviour.getThisNetworkComponent().getId())) {
				return clusterNetworkComponent;
			} else if (networkComponent instanceof ClusterNetworkComponent) {
				ClusterNetworkComponent clusterNetworkComponent2 = findInCluster((ClusterNetworkComponent) networkComponent);
				if (clusterNetworkComponent2 != null) {
					return clusterNetworkComponent2;
				}
			}
		}
		return null;
	}

}
