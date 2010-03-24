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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

import contmas.agents.ContainerAgent;
import contmas.agents.ControlGUIAgent;
import contmas.agents.HarborMasterAgent;
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
	private Method callbackMethod;

	public static ACLMessage getRequestMessage(){
		ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		return msg;
	}

/*
	public getOntologyRepresentation(Agent a,AID agentInQuestion){
		super(a,getRequestMessage());
		this.agentInQuestion=agentInQuestion;
	}
	*/

	/**
	 * @param myAgent
	 * @param inQuestion
	 * @param method
	 */
	public getOntologyRepresentation(Agent myAgent,AID inQuestion,Method method){
		super(myAgent,getOntologyRepresentation.getRequestMessage());
		this.agentInQuestion=inQuestion;
		this.callbackMethod=method;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Vector<ACLMessage> prepareRequests(ACLMessage request){
		if(this.myAgent instanceof ControlGUIAgent){
			request.addReceiver(((ControlGUIAgent) this.myAgent).harbourMaster);
		}else if(this.myAgent instanceof HarborMasterAgent){
			request.addReceiver(this.agentInQuestion);
		}
		RequestOntologyRepresentation act=new RequestOntologyRepresentation();
		act.setAgent_in_question(this.agentInQuestion);

		ContainerAgent.enableForCommunication(this.myAgent);
		ContainerAgent.fillMessage(request,act,this.myAgent);

		Vector<ACLMessage> messages=new Vector<ACLMessage>();
		messages.add(request);//			writeLogMsg((((ControlGUIAgent) myAgent).harbourMaster).toString());
		return messages;
	}

	@Override
	protected void handleInform(ACLMessage msg){
		try{
			this.callbackMethod.invoke(this.myAgent,msg);
		}catch(IllegalArgumentException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(IllegalAccessException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(InvocationTargetException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}