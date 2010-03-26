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

import contmas.agents.ContainerAgent;
import contmas.agents.OntRepProvider;
import contmas.agents.OntRepRequester;
import contmas.main.MatchAgentAction;
import contmas.ontology.ContainerHolder;
import contmas.ontology.ProvideOntologyRepresentation;
import contmas.ontology.RequestOntologyRepresentation;
import contmas.ontology.StartNewContainerHolder;
import jade.content.Concept;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class receiveStartAgentRequest extends AchieveREResponder{
	/**
	 * 
	 */
	private static final long serialVersionUID=3755512724278640204L;
	private listenForStartAgentReq parent=null;

	private static MessageTemplate getMessageTemplate(Agent a){
		MessageTemplate mt=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		mt=MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(a,new StartNewContainerHolder())));
		return mt;
	}

	public receiveStartAgentRequest(Agent a){
		super(a,receiveStartAgentRequest.getMessageTemplate(a));
	}

	@Override
	protected ACLMessage handleRequest(ACLMessage request){
		this.parent=((listenForStartAgentReq) super.parent);

		Concept content=((ContainerAgent) this.myAgent).extractAction(request);
		ACLMessage reply=request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		StartNewContainerHolder act=((StartNewContainerHolder) content);

		this.parent.ontRep=act.getTo_be_added();
		this.parent.randomize=act.getRandomize();
		this.parent.populate=act.getPopulate();
		
		this.parent.START_AGENT_RESPONSE_KEY=reply;
		this.parent.START_AGENT_REQUEST_KEY=request;
		
		this.parent.nextStep();
		parent.removeSubBehaviour(this);

		return reply;
	}
}