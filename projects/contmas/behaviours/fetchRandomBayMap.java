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

import java.util.Vector;

import contmas.agents.ContainerAgent;
import contmas.agents.ContainerHolderAgent;
import contmas.agents.OntRepProvider;
import contmas.agents.ShipAgent;
import contmas.ontology.ProvideBayMap;
import contmas.ontology.RequestRandomBayMap;
import contmas.ontology.StartNewContainerHolder;
import jade.content.Concept;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class fetchRandomBayMap extends AchieveREInitiator{
	/**
	 * 
	 */
	private static final long serialVersionUID= -1832052412333457494L;
	private AID randomGenerator=null;
	private OntRepProvider parent=null;

	private static ACLMessage getRequestMessage(Agent a){
		ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		return msg;
	}
	
	public fetchRandomBayMap(Agent a){
		super(a,getRequestMessage(a));
		this.randomGenerator=(AID) ((ContainerAgent) myAgent).getFirstAIDFromDF("random-generation");
	}
	
	private void setParent(){
		if(super.parent!=null){ //as subBehaviour of listenforStartAgent
			this.parent=((OntRepProvider) super.parent);
		} else { //standalone, i.e. directly run by an agent
			this.parent=((OntRepProvider) myAgent);
		}
	}
	
	@Override
	protected Vector<?> prepareRequests(ACLMessage request){
		setParent();
		request.addReceiver(randomGenerator);
		RequestRandomBayMap act=new RequestRandomBayMap();
		act.setX_dimension(parent.getOntologyRepresentation().getContains().getX_dimension());
		act.setY_dimension(parent.getOntologyRepresentation().getContains().getY_dimension());
		act.setZ_dimension(parent.getOntologyRepresentation().getContains().getZ_dimension());
		((ContainerAgent) this.myAgent).fillMessage(request,act);
		Vector<ACLMessage> messages=new Vector<ACLMessage>();
		messages.add(request);
		return messages;
	}

	@Override
	protected void handleInform(ACLMessage msg){
		Concept content=((ContainerAgent) this.myAgent).extractAction(msg);
		this.parent.getOntologyRepresentation().setContains(((ProvideBayMap) content).getProvides());
	}
}