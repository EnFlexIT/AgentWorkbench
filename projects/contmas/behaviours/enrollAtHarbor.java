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
import jade.util.leap.List;

import java.util.Vector;

import contmas.agents.ContainerAgent;
import contmas.agents.ShipAgent;
import contmas.ontology.AssignBerth;
import contmas.ontology.EnrollAtHarbor;
import contmas.ontology.Ship;

public class enrollAtHarbor extends AchieveREInitiator{

	private static final long serialVersionUID= -1583891049645164006L;
	private ShipAgent mySAgent=null;

	private static ACLMessage getRequestMessage(Agent a){
		ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		return msg;
	}

	public enrollAtHarbor(Agent a){
		super(a,enrollAtHarbor.getRequestMessage(a));
		this.mySAgent=((ShipAgent) this.myAgent);
	}

	@Override
	protected Vector<ACLMessage> prepareRequests(ACLMessage request){
		request.addReceiver(this.mySAgent.getHarbourMaster());
		EnrollAtHarbor act=new EnrollAtHarbor();
		act.setShip_length(((Ship) this.mySAgent.getOntologyRepresentation()).getShip_length());
		((ContainerAgent) this.myAgent).fillMessage(request,act);

		Vector<ACLMessage> messages=new Vector<ACLMessage>();
		messages.add(request);
		return messages;
	}

	@Override
	protected void handleInform(ACLMessage msg){
		Concept content;
		content=((ContainerAgent) this.myAgent).extractAction(msg);

		if(content instanceof AssignBerth){
			List craneList=((AssignBerth) content).getAvailable_cranes();
			this.mySAgent.getOntologyRepresentation().setContractors(craneList);
		}
	}
}