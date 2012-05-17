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

import jade.core.AID;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkComponent;

/**
 * The Class CoalitionANCAuthorityBehaviour.
 */
public class ActiveNAAuthorityBehaviour extends ParallelBehaviour {

	/** The coalition behaviour. */
	private CoalitionBehaviour coalitionBehaviour;
	
	/** The active nc map. */
	private HashMap<String, Boolean> activeNCMap = new HashMap<String, Boolean>();

	/**
	 * Instantiates a new coalition anc authority behaviour.
	 *
	 * @param coalitionBehaviour the coalition behaviour
	 * @param activeNCs the active n cs
	 */
	public ActiveNAAuthorityBehaviour(CoalitionBehaviour coalitionBehaviour, ArrayList<NetworkComponent> activeNCs) {
		this.coalitionBehaviour = coalitionBehaviour;
		activeNCs.remove(0);
		sendMessagesToActiveNCs(activeNCs);
	}
	
	/**
	 * Send messages to active n cs.
	 *
	 * @param activeNCs the active n cs
	 */
	private void sendMessagesToActiveNCs(ArrayList<NetworkComponent> activeNCs) {
		for (NetworkComponent networkComponent : activeNCs) {
			activeNCMap.put(networkComponent.getId(), false);
			addSubBehaviour(new ActiveNAProposeBehaviour(this, myAgent, createRequest(networkComponent.getId(), coalitionBehaviour.getSuggestedClusterNetworkComponent())));
		}
	}

	/**
	 * Creates the request.
	 *
	 * @param receiver the receiver
	 * @param clusterNetworkComponent the cluster network component
	 * @return the aCL message
	 */
	public static ACLMessage createRequest(String receiver, ClusterNetworkComponent clusterNetworkComponent) {
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
		activeNCMap.put(networkComponentID, true);
		for (Boolean value : activeNCMap.values()) {
			if( !value ){
				return;
			}
		}
		coalitionBehaviour.startClusterAgent();
	}
}
