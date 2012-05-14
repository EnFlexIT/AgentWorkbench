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
package gasmas.clustering.coalitions;

import gasmas.agents.components.ClusterNetworkAgent;
import gasmas.clustering.analyse.ComponentFunctions;
import gasmas.clustering.behaviours.ClusteringBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;
import agentgui.simulationService.environment.EnvironmentModel;

/**
 * The Class ClusterNetworkAgentCoalitionBehaviour.
 */
public class ClusterNetworkAgentCoalitionBehaviour extends ParallelBehaviour {

	/** The environment model. */
	private EnvironmentModel environmentModel;

	/** The network component map. */
	private HashMap<String, Boolean> networkComponentMap;

	/** The cluster network component. */
	private ClusterNetworkComponent clusterNetworkComponent;

	/** The bigger clusters. */
	private ArrayList<ClusterNetworkComponent> biggerClusters;

	private boolean clustering = true;

	/**
	 * Instantiates a new cluster network agent coalition behaviour.
	 * @param agent 
	 *
	 * @param environmentModel the environment model
	 * @param clusterNetworkComponent the cluster network component
	 * @param biggerClusters the bigger clusters
	 */
	public ClusterNetworkAgentCoalitionBehaviour(Agent agent, EnvironmentModel environmentModel, ClusterNetworkComponent clusterNetworkComponent, ArrayList<ClusterNetworkComponent> biggerClusters) {
		this.environmentModel = environmentModel;
		this.clusterNetworkComponent = clusterNetworkComponent;
		this.biggerClusters = biggerClusters;
		this.myAgent = agent;
		System.out.println(myAgent.getLocalName() + " " + biggerClusters.size());
		sendMessagesToNCs(clusterNetworkComponent.getNetworkComponentIDs());
	}

	/**
	 * Send messages to active n cs.
	 */
	private void sendMessagesToNCs(HashSet<String> networkComponents) {
		networkComponentMap = new HashMap<String, Boolean>();
		for (String networkComponentID : networkComponents) {
			System.out.print(networkComponentID + ";");
			networkComponentMap.put(networkComponentID, false);
			addSubBehaviour(new ClusterNetworkAgentProposeBehaviour(this, myAgent, createRequest(networkComponentID, clusterNetworkComponent)));
		}
		System.out.println();
	}

	/**
	 * Creates the request.
	 *
	 * @param receiver the receiver
	 * @return the aCL message
	 */
	private ACLMessage createRequest(String receiver, ClusterNetworkComponent clusterNetworkComponent) {
		ACLMessage request = new ACLMessage(ACLMessage.PROPOSE);
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_PROPOSE);
		request.addReceiver(new AID(receiver, AID.ISLOCALNAME));
		try {
			request.setContentObject(clusterNetworkComponent);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return request;
	}

	/**
	 * Adds the agree.
	 *
	 * @param networkComponentID the network component id
	 */
	public void addAgree(String networkComponentID) {
		networkComponentMap.put(networkComponentID, true);
		for (Boolean value : networkComponentMap.values()) {
			if( !value ){
				return;
			}
		}
		if (clustering) {
			clustering = false;
			recreateCluster();
		}
	}

	/**
	 * Recreate cluster.
	 */
	private void recreateCluster() {
		NetworkModel clusteredNM = getClusteredModel();
		clusterNetworkComponent = replaceNetworkModelPartWithCluster(clusteredNM);
		((ClusterNetworkAgent) myAgent).setClusterNetworkComponent(clusterNetworkComponent);
		startSubClustering();
		
		changeDisplay(clusteredNM, clusterNetworkComponent);
	}
	
	/**
	 * Replace network model part with cluster.
	 *
	 * @param networkModel the network model
	 * @return the cluster network component
	 */
	private ClusterNetworkComponent replaceNetworkModelPartWithCluster(NetworkModel networkModel) {
		HashSet<NetworkComponent> networkComponents = new HashSet<NetworkComponent>();
		for (String id : clusterNetworkComponent.getNetworkComponentIDs()) {
			networkComponents.add(networkModel.getNetworkComponent(id));
		}
		return networkModel.replaceComponentsByCluster(networkComponents);
	}

	/**
	 * Start sub clustering.
	 *
	 * @param cluster the cluster
	 */
	private void startSubClustering() {
		if (biggerClusters.size() < 1) {
			return;
		}
		for (ClusterNetworkComponent nextBest : biggerClusters) {
			ClusterNetworkComponent clusterNetworkComponent = replaceNetworkModelPartWithCluster(nextBest.getClusterNetworkModel());
			nextBest.getClusterNetworkModel().renameNetworkComponent(clusterNetworkComponent.getId(), this.clusterNetworkComponent.getId());
			ArrayList<String> activeAgents = getSortedActiveNCIDs(ComponentFunctions.getActiveAgentComponents(nextBest.getClusterNetworkModel()));
			if( activeAgents.size() > 1){
				addSubBehaviour(new ClusterNetworkAgentProposeBehaviour(this, myAgent, createRequest(activeAgents.get(0), nextBest)));
			}
		}

	}
	
	private ArrayList<String> getSortedActiveNCIDs(ArrayList<NetworkComponent> activeNCs) {
		ArrayList<String> activeNCIDs = new ArrayList<String>();
		for (NetworkComponent networkComponent : activeNCs) {
			activeNCIDs.add(networkComponent.getId());
		}
		Collections.sort(activeNCIDs);
		return activeNCIDs;
	}

	/**
	 * Gets the clustered model.
	 *
	 * @return the clustered model
	 */
	private NetworkModel getClusteredModel() {
		NetworkModel networkModel = (NetworkModel) environmentModel.getDisplayEnvironment();
		NetworkModel clusteredNM = networkModel.getAlternativeNetworkModel().get(ClusteringBehaviour.CLUSTER_NETWORK_MODL_NAME);
		if (clusteredNM == null) {
			clusteredNM = networkModel.getCopy();
			clusteredNM.setAlternativeNetworkModel(null);
			changeDisplay(clusteredNM, null);
		}
		return clusteredNM;
	}

	/**
	 * Change display.
	 *
	 * @param clusteredNM the clustered nm
	 * @param cluster the cluster
	 */
	private void changeDisplay(NetworkModel clusteredNM, ClusterNetworkComponent cluster) {
		SimulationServiceHelper simulationServiceHelper = null;
		NetworkModel networkModel = (NetworkModel) environmentModel.getDisplayEnvironment();
		try {
			simulationServiceHelper = (SimulationServiceHelper) myAgent.getHelper(SimulationService.NAME);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		networkModel.getAlternativeNetworkModel().put(ClusteringBehaviour.CLUSTER_NETWORK_MODL_NAME, clusteredNM);
		if (cluster != null) {
			networkModel.getAlternativeNetworkModel().put(cluster.getId(), cluster.getClusterNetworkModel());
		}
		try {
			simulationServiceHelper.setEnvironmentModel(this.environmentModel, true);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
}
