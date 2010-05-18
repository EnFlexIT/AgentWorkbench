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

package contmas.behaviours;

import java.util.Vector;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import contmas.agents.ContainerAgent;
import contmas.agents.ContainerHolderAgent;
import contmas.agents.HarborMasterAgent;
import contmas.agents.StraddleCarrierAgent;
import contmas.interfaces.MoveableAgent;
import contmas.main.MatchAgentAction;
import contmas.ontology.*;

public class listenForExecuteAppointmentReq extends AchieveREResponder{
	private static final long serialVersionUID= -4440040520781720185L;
	private TransportOrderChain curTOC;
	private TransportOrder curTO;
	private ContainerHolderAgent myCAgent;
	private BlockAddress destinationAddress;

	private static MessageTemplate createMessageTemplate(Agent a){
		MessageTemplate mt=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		mt=MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(a,new RequestExecuteAppointment())));
		return mt;
	}

	public listenForExecuteAppointmentReq(Agent a){
		super(a,listenForExecuteAppointmentReq.createMessageTemplate(a));
		myCAgent=(ContainerHolderAgent) a;
		registerHandleRequest(new handleRequest(a,getDataStore()));
	}

	class handleRequest extends FSMBehaviour{

		WaitUntilTargetReached positionChecker;

		//FSM State strings
		private static final String PARSE_REQUEST="parse_request";
		private static final String START_MOVING="start_moving";
		private static final String WAIT_UNTIL_TARGET_REACHED="wait_until_target_reached";
		private static final String DO_AQUIRE="do_aquire";
		private static final String SEND_INFORM="send_inform";

		/**
		 * @param a
		 * @param ds
		 */
		public handleRequest(Agent a,DataStore ds){
			super(a);
			setDataStore(ds);

			//register states
			registerFirstState(new ParseRequest(a,ds),PARSE_REQUEST);
			registerState(new StartMoving(a,ds),START_MOVING);
			positionChecker=new WaitUntilTargetReached(a,ds);
			registerState(positionChecker,WAIT_UNTIL_TARGET_REACHED);
			registerState(new DoAquire(a,ds),DO_AQUIRE);
			registerLastState(new SendInform(myCAgent,ds),SEND_INFORM);

			//register transitions
			registerDefaultTransition(PARSE_REQUEST,START_MOVING);
			registerDefaultTransition(START_MOVING,WAIT_UNTIL_TARGET_REACHED);
			registerDefaultTransition(WAIT_UNTIL_TARGET_REACHED,DO_AQUIRE);
			registerDefaultTransition(DO_AQUIRE,SEND_INFORM);
		}

		class ParseRequest extends OneShotBehaviour{
			ParseRequest(Agent a,DataStore ds){
				super(a);
				this.setDataStore(ds);
			}

			@Override
			public void action(){
				ACLMessage request=(ACLMessage) getDataStore().get(REQUEST_KEY);

				RequestExecuteAppointment inAct=(RequestExecuteAppointment) myCAgent.extractAction(request);
				curTO=inAct.getLoad_offer();
				curTOC=inAct.getCorresponds_to();

				TransportOrderChainState curState=myCAgent.touchTOCState(curTOC);
				destinationAddress=curState.getAt_address();
				if( !(curState instanceof PlannedIn)){
					myCAgent.echoStatus("TOC not PlannedIn",curTOC);
				}
			}
		}

		class StartMoving extends OneShotBehaviour{
			StartMoving(Agent a,DataStore ds){
				super(a);
				this.setDataStore(ds);
			}

			@Override
			public void action(){
				Domain startDomain=myCAgent.inflateDomain(curTO.getStarts_at().getAbstract_designation());
				if(myCAgent instanceof MoveableAgent){
					MoveableAgent myMoveableAgent=(MoveableAgent) myCAgent;
					TransportOrderChainState oldState=myCAgent.touchTOCState(curTOC,new Assigned());

					myCAgent.echoStatus("Container is going to be picked up at " + startDomain.getId() + " " + StraddleCarrierAgent.positionToString(startDomain.getIs_in_position()) + ", current position: " + StraddleCarrierAgent.positionToString(myMoveableAgent.getCurrentPosition()));
					myMoveableAgent.addAsapMovementTo(startDomain.getIs_in_position());
					setTargetPosition(startDomain.getIs_in_position());
				}
			}
		}

		class DoAquire extends OneShotBehaviour{
			ContainerHolderAgent myAgent;

			DoAquire(Agent a,DataStore ds){
				super(a);
				myAgent=(ContainerHolderAgent) a;
				setDataStore(ds);
			}

			/* (non-Javadoc)
			 * @see jade.core.behaviours.Behaviour#action()
			 */
			@Override
			public void action(){
//				myAgent.echoStatus("doAquire");

				if( !myAgent.aquireContainer(curTOC,destinationAddress)){
					myAgent.echoStatus("Something went wrong! Couldn't aquire!",curTOC,ContainerAgent.LOGGING_ERROR);
				}
				myAgent.echoStatus("aquired: ",curTOC);
			}
		}

		class SendInform extends OneShotBehaviour{
			SendInform(Agent a,DataStore ds){
				super(a);
				this.setDataStore(ds);
			}

			@Override
			public void action(){

				ACLMessage request=(ACLMessage) getDataStore().get(REQUEST_KEY);

				ACLMessage reply=request.createReply();
				reply.setPerformative(ACLMessage.INFORM);
				AnnounceLoadStatus act=new AnnounceLoadStatus();

				act.setCorresponds_to(curTOC);
				act.setLoad_status("FINISHED");

				((ContainerAgent) this.myAgent).fillMessage(reply,act);
				myCAgent.echoStatus("sent INFORM FINISHED");

				getDataStore().put(RESPONSE_KEY,reply);
			}

		}

		private void setTargetPosition(Phy_Position targetPosition){
			positionChecker.setTargetPosition(targetPosition);
		}
	}
/*
	@Override
	protected ACLMessage handleRequest(ACLMessage request){
		RequestExecuteAppointment inAct=(RequestExecuteAppointment) myCAgent.extractAction(request);
		curTO=inAct.getLoad_offer();
		curTOC=inAct.getCorresponds_to();

		if( !(myCAgent.touchTOCState(curTOC) instanceof PlannedIn)){
			myCAgent.echoStatus("TOC not PlannedIn",curTOC);
		}

		ACLMessage reply=request.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		AnnounceLoadStatus act=new AnnounceLoadStatus();

		act.setCorresponds_to(curTOC);
		act.setLoad_status("FINISHED");

		((ContainerAgent) this.myAgent).fillMessage(reply,act);
		myCAgent.echoStatus("sent INFORM FINISHED");

		return reply;

	}

	@Override
	protected ACLMessage prepareResultNotification(ACLMessage request,ACLMessage response){
		return null;
	}
	*/
}