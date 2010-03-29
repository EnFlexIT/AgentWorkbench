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

import jade.content.ContentElement;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import contmas.agents.ContainerAgent;
import contmas.main.MatchAgentAction;
import contmas.ontology.AssignHarborQuay;
import contmas.ontology.EnrollAtHarbor;
import contmas.ontology.Quay;
import contmas.ontology.Sea;

public class listenForEnroll extends AchieveREResponder{
	private static final long serialVersionUID= -4440040520781720185L;

	private static MessageTemplate createMessageTemplate(Agent a){
		MessageTemplate mt=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		mt=MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(a,new EnrollAtHarbor())));
		return mt;
	}

	public listenForEnroll(Agent a){
		super(a,listenForEnroll.createMessageTemplate(a));
	}

	@Override
	protected ACLMessage handleRequest(ACLMessage request){
//			echoStatus("listenForEnroll - prepareResponse: "+request.getContent());
		ACLMessage reply=request.createReply();

		ContentElement content;
		content=((ContainerAgent) this.myAgent).extractAction(request);
		reply.setPerformative(ACLMessage.INFORM);
		AssignHarborQuay act=new AssignHarborQuay();
		Quay concept=new Quay();
		concept.setLies_in(new Sea());
		act.setAssigned_quay(concept);
		act.setAvailable_cranes(ContainerAgent.toAIDList(((ContainerAgent) this.myAgent).getAIDsFromDF("craning")));
		((ContainerAgent) this.myAgent).fillMessage(reply,act);
		return reply;

	}

	@Override
	protected ACLMessage prepareResultNotification(ACLMessage request,ACLMessage response){
		return null;
	}
}