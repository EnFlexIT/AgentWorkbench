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

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ProposeInitiator;

public class ClusterNAProposeBehaviour extends ProposeInitiator {

	/** The message. */
	private ACLMessage message;

	/** The cluster network agent coalition behaviour. */
	private ClusterNACoalitionBehaviour clusterNetworkAgentCoalitionBehaviour;

	/**
	 * Instantiates a new cluster network agent propose behaviour.
	 *
	 * @param clusterNetworkAgentCoalitionBehaviour the cluster network agent coalition behaviour
	 * @param a the a
	 * @param msg the msg
	 * @param networkComponentID the network component id
	 */
	public ClusterNAProposeBehaviour(ClusterNACoalitionBehaviour clusterNetworkAgentCoalitionBehaviour, Agent a, ACLMessage msg) {
		super(a, msg);
		message = msg;
		this.clusterNetworkAgentCoalitionBehaviour = clusterNetworkAgentCoalitionBehaviour;
	}

	/**
	 * Add an agree for this component
	 */
	@Override
	protected void handleAcceptProposal(ACLMessage accept_proposal) {
		clusterNetworkAgentCoalitionBehaviour.addAgree(accept_proposal.getSender().getLocalName());
	}

	/**
	 * delete the clusterAgent, because that Cluster is not possible
	 */
	@Override
	protected void handleRejectProposal(ACLMessage reject_proposal) {
//		myAgent.doDelete();
	}

	/**
	 * Resends the message
	 */
	@Override
	protected void handleNotUnderstood(ACLMessage notUnderstood) {
		reset(message);
	}

}
