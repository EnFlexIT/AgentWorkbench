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
import jade.util.leap.Iterator;
import contmas.agents.ContainerAgent;
import contmas.agents.ContainerHolderAgent;
import contmas.main.MatchAgentAction;
import contmas.ontology.LoadList;
import contmas.ontology.RequestExecuteLoadSequence;
import contmas.ontology.TransportOrderChain;

public class listenForLoadingStreamIni extends AchieveREResponder{
	/**
	 * 
	 */
	private static final long serialVersionUID=3755512724278640204L;

	private static MessageTemplate getMessageTemplate(Agent a){
		MessageTemplate mt=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		mt=MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(a,new RequestExecuteLoadSequence())));
		return mt;
	}

	public listenForLoadingStreamIni(Agent a){
		super(a,listenForLoadingStreamIni.getMessageTemplate(a));
	}

	@Override
	protected ACLMessage handleRequest(ACLMessage request){
		Concept content=((ContainerAgent) this.myAgent).extractAction(request);
		ACLMessage reply=request.createReply();
		LoadList toInitiate=((RequestExecuteLoadSequence) content).getNext_step();
		reply.setPerformative(ACLMessage.AGREE);
		while(toInitiate != null){
			Iterator allCurTOCs=toInitiate.getAllConsists_of();
			while(allCurTOCs.hasNext()){
				TransportOrderChain curTOC=(TransportOrderChain) allCurTOCs.next();
				((ContainerHolderAgent) this.myAgent).releaseContainer(curTOC,null);
				toInitiate=toInitiate.getNext_step();
			}
		}

		return reply;

	}

	@Override
	protected ACLMessage prepareResultNotification(ACLMessage request,ACLMessage response){
		return null;
	}
}