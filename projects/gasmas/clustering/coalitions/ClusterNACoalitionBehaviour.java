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

import gasmas.ontology.ClusterNotification;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.simulationService.agents.SimulationAgent;

/**
 * The Class ClusterNetworkAgentCoalitionBehaviour.
 */
public class ClusterNACoalitionBehaviour extends ParallelBehaviour {

	private static final long serialVersionUID = -7320798990075791087L;

	/** The network component map. */
	private HashMap<String, Boolean> networkComponentMap;

	/** The cluster network component. */
	private ClusterNetworkComponent clusterNetworkComponent;

	/**
	 * Instantiates a new cluster network agent coalition behaviour.
	 *
	 * @param agent the agent
	 * @param environmentModel the environment model
	 * @param clusterNetworkComponent the cluster network component
	 * @param clusteringBehaviour 
	 */
	public ClusterNACoalitionBehaviour(Agent agent, ClusterNetworkComponent clusterNetworkComponent) {
		this.clusterNetworkComponent = clusterNetworkComponent;
		this.myAgent = agent;
		sendMessagesToNCs();
	}

	/**
	 * Send messages to=
	 *
	 * @param networkComponents the network components
	 */
	private void sendMessagesToNCs() {
		networkComponentMap = new HashMap<String, Boolean>();
		for (String networkComponentID : clusterNetworkComponent.getNetworkComponentIDs()) {
			networkComponentMap.put(networkComponentID, false);
			addSubBehaviour(new ClusterNAProposeBehaviour(this, myAgent, createRequest(networkComponentID, clusterNetworkComponent)));
		}
	}

	/**
	 * Creates the request and sends the clusterComponent as suggestion
	 *
	 * @param receiver the receiver
	 * @param clusterNetworkComponent the cluster network component
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
	 * Adds the agree of a component, when all are in it starts replacing the Cluster 
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
		recreateCluster();
	}

	/**
	 * Recreate cluster within the ClusterNetworkModel
	 */
	private void recreateCluster() {
// 		Old clustering notification
//		clusterNetworkComponent.setId(myAgent.getLocalName());
//		((SimulationAgent) myAgent).sendManagerNotification(clusterNetworkComponent);
		// --- Send the cluster notification to the manager agent ---
		HashSet<String> found = clusterNetworkComponent.getNetworkComponentIDs();
		ClusterNotification cn = new ClusterNotification();
		cn.setNotificationObject(found);
		cn.setReason("furtherClustering::"+ clusterNetworkComponent.getId());
		((SimulationAgent) myAgent).sendManagerNotification(cn);
	}
}
