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

package contmas.agents;

import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import contmas.ontology.*;

public class HarborMasterAgent extends ContainerAgent{
	public class listenForEnroll extends AchieveREResponder{

		private static final long serialVersionUID= -4440040520781720185L;

		public listenForEnroll(Agent a,MessageTemplate mt){
			super(a,mt);
		}

		@Override
		protected ACLMessage prepareResponse(ACLMessage request){
			ContentElement content;
			content=((ContainerAgent) this.myAgent).extractAction(request);
			Concept action=((AgentAction) content);
			if(action instanceof EnrollAtHarbor){
				ACLMessage rplyMsg=request.createReply();
				rplyMsg.setPerformative(ACLMessage.INFORM);
				AssignHarborQuay act=new AssignHarborQuay();
				Quay concept=new Quay();
				concept.setLies_in(new Sea());
				act.setAssigned_quay(concept);
				((ContainerAgent) this.myAgent).fillMessage(rplyMsg,act);
				return rplyMsg;
			}
			return null;
		}
	}

	public class offerCraneList extends AchieveREResponder{
		/**
		 * 
		 */
		private static final long serialVersionUID= -4313612086308829396L;

		public offerCraneList(Agent a,MessageTemplate mt){
			super(a,mt);
		}

		@Override
		protected ACLMessage prepareResponse(ACLMessage request){
			ACLMessage reply=request.createReply();
			Concept content=((ContainerAgent) this.myAgent).extractAction(request);
			if(content instanceof GetCraneList){
				reply.setPerformative(ACLMessage.INFORM);
				ProvideCraneList act=new ProvideCraneList();
				//look for Cranes
				act.setAvailable_cranes(HarborMasterAgent.this.toAIDList(HarborMasterAgent.this.getAIDsFromDF("craning")));
				((ContainerAgent) this.myAgent).fillMessage(reply,act);
			}else{
				reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			}
			return reply;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID= -2094037480381093187L;

	public HarborMasterAgent(){
		super("harbor-managing");
	}

	@Override
	protected void setup(){
		super.setup();
		//create filter for incoming messages
		MessageTemplate mt=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		//		echoStatus("HarborMaster gestartet (selbst)");
		this.setupEnvironment();
		this.addBehaviour(new listenForEnroll(this,mt));
		this.addBehaviour(new offerCraneList(this,mt));
	}

	protected void setupEnvironment(){
		AgentContainer c=this.getContainerController();
		AgentController a;
		try{
			Crane ontologyRepresentation=new Crane();
			Domain terminalArea=new Land();
			Domain habitat=new Rail();
			habitat.setLies_in(terminalArea);
			ontologyRepresentation.setLives_in(habitat);
			Domain capability=new Rail();
			ontologyRepresentation.addCapable_of(capability);
			capability=new Street();
			ontologyRepresentation.addCapable_of(capability);
			capability=new Sea();
			ontologyRepresentation.addCapable_of(capability);
			a=c.acceptNewAgent("Crane #1",new CraneAgent(ontologyRepresentation));
			a.start();

			ontologyRepresentation=new Crane();
			terminalArea=new Land();
			habitat=new Rail();
			habitat.setLies_in(terminalArea);
			ontologyRepresentation.setLives_in(habitat);
			capability=new Rail();
			ontologyRepresentation.addCapable_of(capability);
			capability=new Street();
			ontologyRepresentation.addCapable_of(capability);
			capability=new Sea();
			ontologyRepresentation.addCapable_of(capability);
			a=c.acceptNewAgent("Crane #2",new CraneAgent(ontologyRepresentation));
			a.start();

			AGV AGVontologyRepresentation=new AGV();
			habitat=new Street();
			habitat.setLies_in(terminalArea);
			AGVontologyRepresentation.setLives_in(habitat);
			a=c.acceptNewAgent("AGV #1",new AGVAgent(AGVontologyRepresentation));
			a.start();

			AGVontologyRepresentation=new AGV();
			habitat=new Street();
			habitat.setLies_in(terminalArea);
			AGVontologyRepresentation.setLives_in(habitat);
			a=c.acceptNewAgent("AGV #2",new AGVAgent(AGVontologyRepresentation));
			a.start();

			AGVontologyRepresentation=new AGV();
			habitat=new Street();
			habitat.setLies_in(terminalArea);
			AGVontologyRepresentation.setLives_in(habitat);
			a=c.acceptNewAgent("AGV #3",new AGVAgent(AGVontologyRepresentation));
			a.start();

		}catch(StaleProxyException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}