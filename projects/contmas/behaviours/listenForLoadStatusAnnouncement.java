/**
 * @author Hanno - Felix Wagner Copyright 2010 Hanno - Felix Wagner This file is
 *         part of ContMAS. ContMAS is free software: you can redistribute it
 *         and/or modify it under the terms of the GNU Lesser General Public
 *         License as published by the Free Software Foundation, either version
 *         3 of the License, or (at your option) any later version. ContMAS is
 *         distributed in the hope that it will be useful, but WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *         FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 *         License for more details. You should have received a copy of the GNU
 *         Lesser General Public License along with ContMAS. If not, see
 *         <http://www.gnu.org/licenses/>.
 */

package contmas.behaviours;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.states.MsgReceiver;
import contmas.agents.ContainerAgent;
import contmas.agents.ContainerHolderAgent;
import contmas.main.MatchAgentAction;
import contmas.ontology.AnnounceLoadStatus;
import contmas.ontology.BlockAddress;

public class listenForLoadStatusAnnouncement extends MsgReceiver{
	private static final long serialVersionUID= -4440040520781720185L;
	private ContainerHolderAgent myAgent;

//	private final String ANNOUNCEMENT_KEY="__announcement"+hashCode();

	private static MessageTemplate createMessageTemplate(Agent a,ACLMessage reservationNotice){
		ACLMessage templateMessage=reservationNotice.createReply();
		templateMessage.setPerformative(ACLMessage.INFORM);
		templateMessage.setReplyWith(null);

		MessageTemplate mt=MessageTemplate.MatchCustom(templateMessage,true);
		mt=MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(a,new AnnounceLoadStatus())));
		return mt;
	}

	public listenForLoadStatusAnnouncement(Agent a,ACLMessage reservationNotice){
		super(a,listenForLoadStatusAnnouncement.createMessageTemplate(a,reservationNotice),MsgReceiver.INFINITE,new DataStore(),"__announcement" + reservationNotice.getConversationId());
		myAgent=(ContainerHolderAgent) a;
	}

	@Override
	public void handleMessage(ACLMessage request){
		AnnounceLoadStatus act=(AnnounceLoadStatus) this.myAgent.extractAction(request);
		if(act.getLoad_status().equals("FINISHED")){
			if(act.getCorresponds_to() == null){
				myAgent.echoStatus("aha",ContainerAgent.LOGGING_DEBUG);

			}

			if( !myAgent.aquireContainer(act.getCorresponds_to(),new BlockAddress())){
				myAgent.echoStatus("Something went wrong! Couldn't aquire!",ContainerAgent.LOGGING_ERROR);

			}

		}
		myAgent.echoStatus("LoadStatus received: " + act.getLoad_status(),ContainerAgent.LOGGING_DEBUG);
	}
}