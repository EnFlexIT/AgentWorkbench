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
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import contmas.agents.ContainerAgent;
import contmas.agents.HarborMasterAgent;
import contmas.ontology.ProvideOntologyRepresentation;
import contmas.ontology.RequestOntologyRepresentation;

public class listenForOntRepRequest extends AchieveREResponder{
		/**
		 * 
		 */
		private static final long serialVersionUID=3755512724278640204L;

		public listenForOntRepRequest(Agent a,MessageTemplate mt){
			super(a,mt);
		}

		@Override
		protected ACLMessage handleRequest(ACLMessage request){
//			echoStatus("listenForOntRepRequest - prepareResponse: "+request.getContent());

			ACLMessage reply=request.createReply();
			Concept content=((ContainerAgent) this.myAgent).extractAction(request);
			String nameInQuestion=((RequestOntologyRepresentation) content).getAgent_in_question().getLocalName();
			if(myAgent instanceof HarborMasterAgent){
				if(((HarborMasterAgent) myAgent).isAlreadyCached(nameInQuestion)){
					reply.setPerformative(ACLMessage.INFORM);
					ProvideOntologyRepresentation act=new ProvideOntologyRepresentation();
					act.setAccording_ontrep(((HarborMasterAgent) myAgent).getCachedOntRep(nameInQuestion));
					((ContainerAgent) this.myAgent).fillMessage(reply,act);
					return reply;
				}else{
					AID inQuestion=new AID();
					inQuestion.setLocalName(nameInQuestion);
					myAgent.addBehaviour(new getOntologyRepresentation(myAgent,inQuestion));
				}
			}else{
				reply.setPerformative(ACLMessage.INFORM);
				ProvideOntologyRepresentation act=new ProvideOntologyRepresentation();
				act.setAccording_ontrep(((ContainerAgent)myAgent).getOntologyRepresentation());
				((ContainerAgent) this.myAgent).fillMessage(reply,act);
				return reply;
			}
			myAgent.postMessage(request);
			block(200);
			return null;
		}

		@Override
		protected ACLMessage prepareResultNotification(ACLMessage request,ACLMessage response){
			return null;
		}
	}