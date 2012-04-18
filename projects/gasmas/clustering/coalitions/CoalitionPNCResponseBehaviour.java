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
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;
import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;

/**
 * The Class CoalitionPNCBehaviour.
 */
public class CoalitionPNCResponseBehaviour extends AchieveREResponder {

	/** The cluster network component. */
	private ClusterNetworkComponent clusterNetworkComponent;

	/**
	 * Instantiates a new coalition pnc behaviour.
	 *
	 * @param a the a
	 */
	public CoalitionPNCResponseBehaviour(Agent a) {
		super(a, AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST));
	}
	
	@Override
	protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
		ACLMessage response = request.createReply();
		ClusterNetworkComponent clusterNetworkComponent = null;
		try {
			clusterNetworkComponent = (ClusterNetworkComponent) request.getContentObject();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}

		if (clusterNetworkComponent == null) {
			response.setPerformative(ACLMessage.NOT_UNDERSTOOD);
		}
		if (this.clusterNetworkComponent == null || clusterNetworkComponent == this.clusterNetworkComponent) {
			response.setPerformative(ACLMessage.AGREE);
		} else {
			response.setPerformative(ACLMessage.REFUSE);
		}
		return response;
	}

	/* (non-Javadoc)
	 * @see jade.proto.AchieveREResponder#prepareResultNotification(jade.lang.acl.ACLMessage, jade.lang.acl.ACLMessage)
	 */
	@Override
	protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
		return null;
	}

}
