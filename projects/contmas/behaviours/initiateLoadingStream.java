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

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import java.util.Vector;

import contmas.agents.ContainerAgent;
import contmas.ontology.LoadList;
import contmas.ontology.RequestExecuteLoadSequence;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class initiateLoadingStream extends AchieveREInitiator{
	/**
	 * 
	 */
	private static final long serialVersionUID= -1832052412333457494L;
	private LoadList sequenceToInitiate=null;
	private AID agentFrom=null;

	private static ACLMessage getRequestMessage(Agent a){
		ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		return msg;
	}

	public initiateLoadingStream(Agent a,LoadList loadSequence,AID agentFrom){
		super(a,initiateLoadingStream.getRequestMessage(a));
		this.sequenceToInitiate=loadSequence;
		this.agentFrom=agentFrom;
	}

	@Override
	protected Vector<?> prepareRequests(ACLMessage request){
		RequestExecuteLoadSequence act=new RequestExecuteLoadSequence();
		act.setNext_step(this.sequenceToInitiate);
		request.addReceiver(this.agentFrom);

		((ContainerAgent) this.myAgent).fillMessage(request,act);
		Vector<ACLMessage> messages=new Vector<ACLMessage>();
		messages.add(request);
		return messages;
	}

	@Override
	protected void handleAgree(ACLMessage msg){
		((ContainerAgent) this.myAgent).echoStatus("Loading Stream initiation agree received.",ContainerAgent.LOGGING_NOTICE);
	}
}