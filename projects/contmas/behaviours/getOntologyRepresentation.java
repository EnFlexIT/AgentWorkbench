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

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import java.util.Vector;

import contmas.agents.ContainerAgent;
import contmas.interfaces.OntRepRequester;
import contmas.ontology.ContainerHolder;
import contmas.ontology.ProvideOntologyRepresentation;
import contmas.ontology.RequestOntologyRepresentation;

public class getOntologyRepresentation extends AchieveREInitiator{
	/**
	 * 
	 */
	private static final long serialVersionUID=3852209142960173705L;
	/**
	 * 
	 */
	private AID agentInQuestion=null;
	private AID requestFrom;
	private OntRepRequester parent;

	public static ACLMessage getRequestMessage(){
		ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		return msg;
	}

	/**
	 * @param myAgent
	 * @param inQuestion
	 */
	public getOntologyRepresentation(Agent myAgent,AID inQuestion){
		super(myAgent,getOntologyRepresentation.getRequestMessage());
		this.agentInQuestion=inQuestion;
		this.requestFrom=this.agentInQuestion;
	}

	/**
	 * @param myAgent
	 * @param inQuestion
	 * @param relayServer TODO
	 * @param newParam TODO
	 */
	public getOntologyRepresentation(Agent myAgent,AID inQuestion,AID requestFrom){
		this(myAgent,inQuestion);
		this.requestFrom=requestFrom;
	}

	private void setParent(){
		if(super.parent != null){ //as subBehaviour
			this.parent=((OntRepRequester) super.parent);
		}else{ //standalone, i.e. directly run by an agent
			this.parent=((OntRepRequester) this.myAgent);
		}
	}

	@Override
	protected Vector<ACLMessage> prepareRequests(ACLMessage request){
		this.setParent();
		request.addReceiver(this.requestFrom);
		RequestOntologyRepresentation act=new RequestOntologyRepresentation();
		act.setAgent_in_question(this.agentInQuestion);

		ContainerAgent.enableForCommunication(this.myAgent);
		ContainerAgent.fillMessage(request,act,this.myAgent);

		Vector<ACLMessage> messages=new Vector<ACLMessage>();
		messages.add(request);
		return messages;
	}

	@Override
	protected void handleInform(ACLMessage msg){
		ProvideOntologyRepresentation act=(ProvideOntologyRepresentation) ContainerAgent.extractAction(this.myAgent,msg);
		this.parent.processOntologyRepresentation(act.getAccording_ontrep(),this.agentInQuestion);
	}

	@Override
	protected void handleRefuse(ACLMessage msg){
		this.parent.processOntologyRepresentation(new ContainerHolder(),this.agentInQuestion);
	}

	@Override
	protected void handleNotUnderstood(ACLMessage msg){
		this.parent.processOntologyRepresentation(new ContainerHolder(),this.agentInQuestion);
	}
}