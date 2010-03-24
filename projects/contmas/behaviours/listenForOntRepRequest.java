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
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import contmas.agents.ContainerAgent;
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
	String nameInQuestion;
	private Method callbackMethod;

	private static MessageTemplate getMessageTemplate(Agent a){
		MessageTemplate mt=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		mt=MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(a,new RequestOntologyRepresentation())));
		return mt;
	}

	/*
	public listenForOntRepRequest(Agent a){
		super(a,getMessageTemplate(a));
	}
	*/
	public listenForOntRepRequest(Agent a,Method method){
		super(a,listenForOntRepRequest.getMessageTemplate(a));
//			this(a);
		this.callbackMethod=method;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ACLMessage handleRequest(ACLMessage request){
		/*
		if(((HarborMasterAgent) myAgent).isAlreadyCached(nameInQuestion)){
			reply.setPerformative(ACLMessage.INFORM);
			ProvideOntologyRepresentation act=new ProvideOntologyRepresentation();
			act.setAccording_ontrep(((HarborMasterAgent) myAgent).getCachedOntRep(nameInQuestion));
			((ContainerAgent) this.myAgent).fillMessage(reply,act);
			return reply;
		}else{*/
//			echoStatus("listenForOntRepRequest - prepareResponse: "+request.getContent());
		Concept content=((ContainerAgent) this.myAgent).extractAction(request);
		this.nameInQuestion=((RequestOntologyRepresentation) content).getAgent_in_question().getLocalName();
		try{
			this.accordingOntrep=(ContainerHolder) this.callbackMethod.invoke(this.myAgent,this.nameInQuestion);
		}catch(IllegalArgumentException e1){
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch(IllegalAccessException e1){
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch(InvocationTargetException e1){
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if(this.accordingOntrep == null){
			this.block();
			this.myAgent.putBack(request);
//				myAgent.postMessage(request);
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

	public void notifyOnCompletion(ContainerHolder accordingOntrep){
		this.accordingOntrep=accordingOntrep;
	}

	@Override
	protected ACLMessage prepareResultNotification(ACLMessage request,ACLMessage response){
		return null;
	}
}