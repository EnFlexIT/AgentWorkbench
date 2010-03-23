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

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import java.util.Vector;

import contmas.agents.ContainerAgent;
import contmas.agents.ControlGUIAgent;
import contmas.agents.HarborMasterAgent;
import contmas.ontology.ProvideOntologyRepresentation;
import contmas.ontology.RequestOntologyRepresentation;

public class getOntologyRepresentation extends AchieveREInitiator{
		/**
		 * 
		 */
		private AID agentInQuestion=null;
		 public static ACLMessage getRequestMessage(){
			ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			return msg;
		 }

		public getOntologyRepresentation(Agent a,AID agentInQuestion){
			super(a,getRequestMessage());
			this.agentInQuestion=agentInQuestion;
		}
		
		@Override
		protected Vector<ACLMessage> prepareRequests(ACLMessage request){
			if(myAgent instanceof ControlGUIAgent){
				request.addReceiver(((ControlGUIAgent) myAgent).harbourMaster);
			}else if(myAgent instanceof HarborMasterAgent){
				request.addReceiver(agentInQuestion);
			}
			RequestOntologyRepresentation act=new RequestOntologyRepresentation();
			act.setAgent_in_question(this.agentInQuestion);
			
			ContainerAgent.enableForCommunication(myAgent);
			ContainerAgent.fillMessage(request,act,myAgent);
			
			Vector<ACLMessage> messages=new Vector<ACLMessage>();
			messages.add(request);//			writeLogMsg((((ControlGUIAgent) myAgent).harbourMaster).toString());
			return messages;
		}
		
		@Override
		protected void handleInform(ACLMessage msg){
			if(myAgent instanceof ControlGUIAgent){
				((ControlGUIAgent)myAgent).myGui.printMessageContent(msg.getContent());
			}else if(myAgent instanceof HarborMasterAgent){
				ProvideOntologyRepresentation ontRep=(ProvideOntologyRepresentation)ContainerAgent.extractAction(myAgent,msg);
				((HarborMasterAgent)myAgent).addCachedOntRep(agentInQuestion.getLocalName(),ontRep.getAccording_ontrep());
			}

		}
	}