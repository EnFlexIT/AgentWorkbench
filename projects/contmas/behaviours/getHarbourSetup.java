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
import java.util.Vector;

import contmas.agents.ContainerAgent;
import contmas.agents.ControlGUIAgent;
import contmas.agents.HarborMasterAgent;
import contmas.agents.OntRepRequester;
import contmas.ontology.*;

public class getHarbourSetup extends AchieveREInitiator{
	/**
	 * 
	 */
	private static final long serialVersionUID=3852209142960173705L;
	/**
	 * 
	 */
	private AID requestFrom;

	public static ACLMessage getRequestMessage(){
		ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		return msg;
	}

	/**
	 * @param myAgent
	 * @param inQuestion
	 */
	public getHarbourSetup(Agent myAgent,AID requestFrom){
		super(myAgent,getHarbourSetup.getRequestMessage());
		this.requestFrom=requestFrom;
	}

	@Override
	protected Vector<ACLMessage> prepareRequests(ACLMessage request){
		request.addReceiver(this.requestFrom);
		RequestHarbourSetup act=new RequestHarbourSetup();

		ContainerAgent.enableForCommunication(this.myAgent);
		ContainerAgent.fillMessage(request,act,this.myAgent);

		Vector<ACLMessage> messages=new Vector<ACLMessage>();
		messages.add(request);
		return messages;
	}

	@Override
	protected void handleInform(ACLMessage msg){
		ProvideHarbourSetup act=(ProvideHarbourSetup) ContainerAgent.extractAction(myAgent,msg);
		((ControlGUIAgent) myAgent).processHarbourLayout(act.getCurrent_harbour_layout());
	}
}