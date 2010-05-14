/**
 * @author Hanno - Felix Wagner, 13.05.2010
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

import contmas.agents.BayMapOptimisationAgent;
import contmas.agents.ContainerAgent;
import contmas.main.MatchAgentAction;
import contmas.ontology.*;
import jade.content.Concept;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.util.leap.List;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class listenForBlockAddressRequests extends AchieveREResponder{
	BayMapOptimisationAgent myAgent;
	/**
	 * @param a
	 * @param mt
	 */
	public listenForBlockAddressRequests(BayMapOptimisationAgent a){
		super(a,createMessageTemplate(a));
		myAgent=a;
	}

	/**
	 * @return
	 */
	private static MessageTemplate createMessageTemplate(BayMapOptimisationAgent a){
		MessageTemplate mt=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		mt=MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(a,new RequestBlockAddress())));
		return mt;
	}
	
	@Override
	protected ACLMessage handleRequest(ACLMessage request){
		ACLMessage reply=request.createReply();
		Concept content;
		content=this.myAgent.extractAction(request);
		RequestBlockAddress req=(RequestBlockAddress) content;
		BayMap loadBay=req.getProvides();
		List population=req.getProvides_population();
		TransportOrderChain subject=req.getSubjected_toc();

		ProvideBlockAddress act=new ProvideBlockAddress();

		act.setSuiting_address(myAgent.getEmptyBlockAddress(loadBay,subject,population));
		
		reply.setPerformative(ACLMessage.INFORM);
		((ContainerAgent) this.myAgent).fillMessage(reply,act);
		return reply;
	}

}
