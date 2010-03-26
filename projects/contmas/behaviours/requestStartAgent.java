/**
 * @author Hanno - Felix Wagner, 24.03.2010
 * Copyright 2010 Hanno - Felix Wagner
 * 
 * This file is part of ContMAS.
 *
 * ContMAS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ContMAS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ContMAS.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package contmas.behaviours;

import java.util.Vector;

import contmas.agents.ContainerAgent;
import contmas.agents.OntRepRequester;
import contmas.ontology.ContainerHolder;
import contmas.ontology.ProvideOntologyRepresentation;
import contmas.ontology.RequestOntologyRepresentation;
import contmas.ontology.StartNewContainerHolder;
import jade.content.AgentAction;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class requestStartAgent extends AchieveREInitiator{

	private AID harbourMaster=null;
	private StartNewContainerHolder act=null;

	private static ACLMessage getRequestMessage(Agent a){
		ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		return msg;
	}

	public requestStartAgent(Agent a,AID harbourMaster,StartNewContainerHolder act){
		super(a,getRequestMessage(a));
		this.harbourMaster=harbourMaster;

		this.act=act;
	}

	@Override
	protected Vector<ACLMessage> prepareRequests(ACLMessage request){
		request.addReceiver(this.harbourMaster);

		ContainerAgent.enableForCommunication(this.myAgent);
		ContainerAgent.fillMessage(request,act,this.myAgent);

		Vector<ACLMessage> messages=new Vector<ACLMessage>();
		messages.add(request);
		return messages;
	}

	@Override
	protected void handleInform(ACLMessage msg){
	}

	@Override
	protected void handleRefuse(ACLMessage msg){
	}

	@Override
	protected void handleNotUnderstood(ACLMessage msg){
	}
}