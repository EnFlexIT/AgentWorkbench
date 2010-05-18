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
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.util.leap.Iterator;
import contmas.agents.ContainerHolderAgent;
import contmas.main.MatchAgentAction;
import contmas.ontology.*;

public class listenForLoadingStreamIni extends AchieveREResponder{
	/**
	 * 
	 */
	private static final long serialVersionUID=3755512724278640204L;

	private LoadList currentStep;
	private ContainerHolderAgent myCAgent;

	private static MessageTemplate getMessageTemplate(Agent a){
		MessageTemplate mt=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		mt=MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(a,new RequestExecuteLoadSequence())));
		return mt;
	}

	public listenForLoadingStreamIni(Agent a){
		super(a,listenForLoadingStreamIni.getMessageTemplate(a));
		myCAgent=(ContainerHolderAgent) a;

		handleRequest b=new handleRequest();
		b.setDataStore(getDataStore());
		b.setup();
		registerHandleRequest(b);
	}

	class handleRequest extends SequentialBehaviour{

		/**
		 * 
		 */
		private static final long serialVersionUID=5209714705756129093L;

		private void setup(){
			Behaviour b=new agreeToSequence();
			b.setDataStore(getDataStore());
			addSubBehaviour(b);
			
			b=new executeSteps();
			b.setDataStore(getDataStore());
			addSubBehaviour(b);
		}
	}

	class agreeToSequence extends OneShotBehaviour{
		/**
		 * 
		 */
		private static final long serialVersionUID= -2716134578236595851L;


		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action(){
			ACLMessage request=(ACLMessage) getDataStore().get(REQUEST_KEY);
			Concept content=myCAgent.extractAction(request);
			ACLMessage reply=request.createReply();
			currentStep=((RequestExecuteLoadSequence) content).getNext_step();
			reply.setPerformative(ACLMessage.AGREE);
			myCAgent.send(reply);
		}
	}

	class executeSteps extends SimpleBehaviour{
		/**
		 * 
		 */
		private static final long serialVersionUID= -2716134578236595851L;
		private Boolean done=false;

		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action(){
			if(currentStep != null){
				if(executeStep(currentStep)){ //Step is complete

					currentStep=currentStep.getNext_step();
					ACLMessage request=(ACLMessage) getDataStore().get(REQUEST_KEY);
					RequestExecuteLoadSequence act=(RequestExecuteLoadSequence) myCAgent.extractAction(request);
					ACLMessage reply=request.createReply();
					reply.setPerformative(ACLMessage.INFORM);
					reply.setContent("Step complete " + act.getNext_step().getConsists_of().iterator().next());
					myCAgent.send(reply);
				}else{
					myCAgent.registerForWakeUpCall(this);
					block();
				}
			} else {
				ACLMessage reply=((ACLMessage) getDataStore().get(REQUEST_KEY)).createReply();
				reply.setPerformative(ACLMessage.INFORM);
				reply.setContent("Loading stream complete!");

				done=true;
			}
			return;
		}

		private Boolean executeStep(LoadList toInitiate){
			Boolean completelyExecuted=true;
			Iterator allCurTOCs=toInitiate.getAllConsists_of();
			while(allCurTOCs.hasNext()){
				TransportOrderChain curTOC=(TransportOrderChain) allCurTOCs.next();
				TransportOrderChainState curState=myCAgent.touchTOCState(curTOC);

				if(curState instanceof Administered){
					myCAgent.releaseContainer(curTOC);
				}
				completelyExecuted=completelyExecuted && (curState == null);
			}
			return completelyExecuted;
		}

		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#done()
		 */
		@Override
		public boolean done(){
			return done;
		}
	}

	@Override
	protected ACLMessage prepareResultNotification(ACLMessage request,ACLMessage response){
		return null;
	}
}