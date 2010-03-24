/**
 * @author Hanno - Felix Wagner, 22.03.2010
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

import jade.content.Concept;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import contmas.agents.ContainerAgent;
import contmas.agents.OntRepProvider;
import contmas.main.MatchAgentAction;
import contmas.ontology.ContainerHolder;
import contmas.ontology.ProvideOntologyRepresentation;
import contmas.ontology.RequestOntologyRepresentation;

public class listenForOntRepRequest extends AchieveREResponder{
	/**
	 * 
	 */
	private static final long serialVersionUID=3755512724278640204L;
	private ContainerHolder accordingOntrep=null;

	private static MessageTemplate getMessageTemplate(Agent a){
		MessageTemplate mt=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		mt=MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(a,new RequestOntologyRepresentation())));
		return mt;
	}

	public listenForOntRepRequest(Agent a){
		super(a,listenForOntRepRequest.getMessageTemplate(a));
	}

	@Override
	protected ACLMessage handleRequest(ACLMessage request){
		Concept content=((ContainerAgent) this.myAgent).extractAction(request);
		AID inQuestion=((RequestOntologyRepresentation) content).getAgent_in_question();
		this.accordingOntrep=((OntRepProvider) myAgent).getOntologyRepresentation(inQuestion);

		if(this.accordingOntrep == null){
			this.block();
			this.myAgent.putBack(request);
			return null;
		}else{
			ACLMessage reply=request.createReply();
			reply.setPerformative(ACLMessage.INFORM);
			ProvideOntologyRepresentation act=new ProvideOntologyRepresentation();
			act.setAccording_ontrep(this.accordingOntrep);
			((ContainerAgent) this.myAgent).fillMessage(reply,act);
			return reply;
		}
	}

	@Override
	protected ACLMessage prepareResultNotification(ACLMessage request,ACLMessage response){
		return null;
	}
}