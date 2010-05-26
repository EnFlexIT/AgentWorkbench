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
import jade.util.leap.List;

import java.util.Vector;

import contmas.agents.ContainerAgent;
import contmas.agents.ContainerHolderAgent;
import contmas.interfaces.LoadingReceiver;
import contmas.interfaces.OptimisationClient;
import contmas.ontology.*;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class requestBlockAddress extends AchieveREInitiator{
	/**
	 * 
	 */
	private static final long serialVersionUID= -1832052412333457494L;
	ContainerHolderAgent myAgent=null;
	private AID optimizer=null;
	private BayMap rawBayMap=null;
	private TransportOrderChain subject=null;
	private BlockAddress resultMemory;
	private List population;

	private static ACLMessage getRequestMessage(Agent a){
		ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		return msg;
	}

	public requestBlockAddress(ContainerHolderAgent a,BayMap rawBayMap,List population,TransportOrderChain subject, BlockAddress resultMemory){
		super(a,requestBlockAddress.getRequestMessage(a));
		myAgent=a;
		this.optimizer=((OptimisationClient)this.myAgent).getOptimizer();
		this.rawBayMap=rawBayMap;
		this.subject=subject;
		this.resultMemory=resultMemory;
		this.population=population;
	}

	@Override
	protected Vector<?> prepareRequests(ACLMessage request){
		request.addReceiver(this.optimizer);
		RequestBlockAddress act=new RequestBlockAddress();
		act.setProvides(this.rawBayMap);
		act.setSubjected_toc(subject);
		act.setProvides_population(population);
		
		((ContainerAgent) this.myAgent).fillMessage(request,act);
		Vector<ACLMessage> messages=new Vector<ACLMessage>();
		messages.add(request);
		return messages;
	}

	@Override
	protected void handleInform(ACLMessage msg){
//		myAgent.echoStatus("requestBlockAddress - handleInform");
		ProvideBlockAddress act=(ProvideBlockAddress) this.myAgent.extractAction(msg);
		BlockAddress suitingAddress=act.getSuiting_address();
		resultMemory.setLocates(suitingAddress.getLocates());
		resultMemory.setX_dimension(suitingAddress.getX_dimension());
		resultMemory.setY_dimension(suitingAddress.getY_dimension());
		resultMemory.setZ_dimension(suitingAddress.getZ_dimension());
	}
}