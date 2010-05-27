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

import jade.content.AgentAction;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import java.util.Vector;

import contmas.agents.ContainerAgent;
import contmas.ontology.RequestEnvironmentReset;
import contmas.ontology.StartNewContainerHolder;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class requestEnvironmentSetup extends AchieveREInitiator{

	/**
	 * 
	 */
	private static final long serialVersionUID= -1067315000037572948L;
	private AID harbourMaster=null;
	ContainerAgent myAgent;;

	private static ACLMessage getRequestMessage(Agent a){
		ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		return msg;
	}

	public requestEnvironmentSetup(Agent a,AID harbourMaster){
		super(a,requestEnvironmentSetup.getRequestMessage(a));
		myAgent=(ContainerAgent) a;
		this.harbourMaster=harbourMaster;
	}

	@Override
	protected Vector<ACLMessage> prepareRequests(ACLMessage request){
		request.addReceiver(this.harbourMaster);
		AgentAction agAct=new RequestEnvironmentReset();
		myAgent.fillMessage(request,agAct);
		
		Vector<ACLMessage> messages=new Vector<ACLMessage>();
		messages.add(request);
		return messages;
	}

	@Override
	protected void handleInform(ACLMessage msg){
	}
	
	@Override
	protected void handleAgree(ACLMessage msg){
		myAgent.echoStatus("HarbourMaster has agreed to reset.",ContainerAgent.LOGGING_INFORM);
	}

	@Override
	protected void handleRefuse(ACLMessage msg){
	}

	@Override
	protected void handleNotUnderstood(ACLMessage msg){
	}
}