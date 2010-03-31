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
import contmas.ontology.BayMap;
import contmas.ontology.ProvideRandomLoadSequence;
import contmas.ontology.RequestRandomLoadSequence;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class getRandomLoadSequence extends AchieveREInitiator{
	/**
	 * 
	 */
	private static final long serialVersionUID= -1832052412333457494L;
	private AID randomGenerator=null;
	private BayMap bayMapToBeSequenced=null;
	private AID agentForWhich=null;

	private LoadingReceiver parent=null;

	private static ACLMessage getRequestMessage(Agent a){
		ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		return msg;
	}

	public getRandomLoadSequence(Agent a,BayMap bayMap,AID agent){
		super(a,getRandomLoadSequence.getRequestMessage(a));
		this.randomGenerator=((ContainerAgent) this.myAgent).getFirstAIDFromDF("random-generation");
		this.bayMapToBeSequenced=bayMap;
		this.agentForWhich=agent;

	}

	private void setParent(){
		if(super.parent != null){ //as subBehaviour of listenforStartAgent
			this.parent=((LoadingReceiver) super.parent);
		}else{ //standalone, i.e. directly run by an agent
			this.parent=((LoadingReceiver) this.myAgent);
		}
	}

	@Override
	protected Vector<?> prepareRequests(ACLMessage request){
		this.setParent();
		request.addReceiver(this.randomGenerator);
		RequestRandomLoadSequence act=new RequestRandomLoadSequence();
		act.setProvides(this.bayMapToBeSequenced);

		((ContainerAgent) this.myAgent).fillMessage(request,act);
		Vector<ACLMessage> messages=new Vector<ACLMessage>();
		messages.add(request);
		return messages;
	}

	@Override
	protected void handleInform(ACLMessage msg){
		ProvideRandomLoadSequence act=((ProvideRandomLoadSequence) ((ContainerAgent) this.myAgent).extractAction(msg));
		this.parent.processLoadSequence(act.getNext_step(),this.agentForWhich);
	}
}