/**
 * @author Hanno - Felix Wagner, 25.03.2010
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
import jade.proto.AchieveREInitiator;
import jade.util.leap.Iterator;

import java.util.Vector;

import contmas.agents.ContainerAgent;
import contmas.agents.ContainerHolderAgent;
import contmas.interfaces.OntRepProvider;
import contmas.ontology.*;

public class requestPopulatedBayMap extends AchieveREInitiator{
	private OntRepProvider parent=null;
	/**
	 * 
	 */
	private static final long serialVersionUID= -6587230887404034233L;

	private static ACLMessage getRequestMessage(Agent a){
		ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		return msg;
	}

	public requestPopulatedBayMap(Agent a){
		super(a,requestPopulatedBayMap.getRequestMessage(a));
	}

	@Override
	protected Vector<?> prepareRequests(ACLMessage request){
		this.setParent();
		request.addReceiver(((ContainerAgent) this.myAgent).getRandomGenerator());
		//BayMap aus Agent auslesen
		RequestPopulatedBayMap act=new RequestPopulatedBayMap();
		act.setPopulate_on(this.parent.getOntologyRepresentation().getContains());
		((ContainerAgent) this.myAgent).fillMessage(request,act);
		Vector<ACLMessage> messages=new Vector<ACLMessage>();
		messages.add(request);
		return messages;
	}

	@Override
	protected void handleInform(ACLMessage msg){
		Concept content;
		ContainerHolder ontRep=this.parent.getOntologyRepresentation();
		content=((ContainerAgent) this.myAgent).extractAction(msg);
		ontRep.setContains(((ProvidePopulatedBayMap) content).getProvides());
		Iterator allConts=ontRep.getContains().getAllIs_filled_with();
		while(allConts.hasNext()){
			BlockAddress curBaymap=(BlockAddress) allConts.next();
			TransportOrderChain curTOC=new TransportOrderChain();
			curTOC.setTransports(curBaymap.getLocates());
			ContainerHolderAgent.touchTOCState(curTOC,new Administered(),true,ontRep);
		}
	}

	private void setParent(){
		if(super.parent != null){ //as subBehaviour of listenforStartAgent
			this.parent=((OntRepProvider) super.parent);
		}else{ //standalone, i.e. directly run by an agent
			this.parent=((OntRepProvider) this.myAgent);
		}
	}
}