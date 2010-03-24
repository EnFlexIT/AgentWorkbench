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
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import contmas.agents.ContainerAgent;
import contmas.main.MatchAgentAction;
import contmas.ontology.GetCraneList;
import contmas.ontology.ProvideCraneList;

public class offerCraneList extends AchieveREResponder{
	/**
	 * 
	 */
	private static final long serialVersionUID= -4313612086308829396L;

	private static MessageTemplate getMessageTemplate(Agent a){
		MessageTemplate mt=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		mt=MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(a,new GetCraneList())));
		return mt;
	}

	public offerCraneList(Agent a){
		super(a,offerCraneList.getMessageTemplate(a));
	}

	@Override
	protected ACLMessage handleRequest(ACLMessage request){
//			echoStatus("offerCraneList - prepareResponse: "+request.getContent());

		ACLMessage reply=request.createReply();
		((ContainerAgent) this.myAgent).extractAction(request);
		reply.setPerformative(ACLMessage.INFORM);
		ProvideCraneList act=new ProvideCraneList();
		//look for Cranes
		act.setAvailable_cranes(ContainerAgent.toAIDList(((ContainerAgent) this.myAgent).getAIDsFromDF("craning")));
		((ContainerAgent) this.myAgent).fillMessage(reply,act);
		return reply;

	}

	@Override
	protected ACLMessage prepareResultNotification(ACLMessage request,ACLMessage response){
		return null;
	}
}