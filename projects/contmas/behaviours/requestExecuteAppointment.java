/**
 * @author Hanno - Felix Wagner, 18.05.2010
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

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

import java.util.Vector;

import mas.display.DisplayableAgent;
import mas.movement.MoveToPointBehaviour;

import contmas.agents.ContainerAgent;
import contmas.agents.ContainerHolderAgent;
import contmas.interfaces.MoveableAgent;
import contmas.main.AlreadyMovingException;
import contmas.main.EnvironmentHelper;
import contmas.ontology.*;

/**
 * @author Hanno - Felix Wagner
 *
 */
public class requestExecuteAppointment extends AchieveREInitiator{
	ContainerHolderAgent myCAgent;
	TransportOrderChain curTOC;
	TransportOrder curTO;

	private static ACLMessage getRequestMessage(Agent a){
		ACLMessage msg=new ACLMessage(ACLMessage.REQUEST);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		return msg;
	}

	/**
	 * @param a
	 */
	public requestExecuteAppointment(Agent a,TransportOrderChain curTOC,TransportOrder curTO){
		super(a,getRequestMessage(a));
		myCAgent=(ContainerHolderAgent) a;
		this.curTOC=curTOC;
		this.curTO=curTO;

//		myCAgent.echoStatus("appointmentExecution, startBA="+Const.blockAddressToString(curTO.getStarts_at().getAt_address()),curTOC);
//		myCAgent.echoStatus("appointmentExecution, endBA="+Const.blockAddressToString(curTO.getEnds_at().getAt_address()),curTOC);

		DataStore ds=getDataStore();

		registerHandleInform(new handleInform(a,ds));
		registerPrepareRequests(new prepareRequests(a,ds));
	}

	class prepareRequests extends FSMBehaviour{

		WaitUntilTargetReached positionChecker;

		//FSM State strings
		private static final String START_MOVING="start_moving";
		private static final String WAIT_UNTIL_TARGET_REACHED="wait_until_target_reached";
		private static final String SEND_REQUEST="send_request";

		/**
		 * @param a
		 * @param ds
		 */
		public prepareRequests(Agent a,DataStore ds){
			super(a);
			setDataStore(ds);

			//register states
			registerFirstState(new StartMoving(a,ds),START_MOVING);
			positionChecker=new WaitUntilTargetReached(a,ds);
			registerState(positionChecker,WAIT_UNTIL_TARGET_REACHED);
			registerLastState(new SendRequest(myCAgent,ds),SEND_REQUEST);

			//register transitions
			registerDefaultTransition(START_MOVING,WAIT_UNTIL_TARGET_REACHED);
			registerDefaultTransition(WAIT_UNTIL_TARGET_REACHED,SEND_REQUEST);
		}

		class StartMoving extends SimpleBehaviour{
			Boolean isDone;
			StartMoving(Agent a,DataStore ds){
				super(a);
				this.setDataStore(ds);
			}

			@Override
			public void action(){
				isDone=true;
				
				if(myCAgent instanceof DisplayableAgent){
					MoveableAgent myMoveableAgent=(MoveableAgent) myCAgent;
					TransportOrderChainState oldState=myCAgent.setTOCState(curTOC,new Assigned());

					Phy_Position targetPosition=myCAgent.calculateTargetPosition(curTO.getEnds_at());
					try {
						MoveToPointBehaviour movingBehaviour = myMoveableAgent.addDisplayMove(targetPosition);
						positionChecker.setMovingBehaviour(movingBehaviour);

//						myMoveableAgent.addAsapMovementTo(targetPosition);
						setTargetPosition(targetPosition);
					} catch (AlreadyMovingException e) {
						myCAgent.echoStatus("requestExecuteAppointment.prepareRequests.StartMoving AlreadyMovingException");
						
						myCAgent.registerForWakeUpCall(this);
						isDone=false;
					}
				}
			}

			@Override
			public boolean done() {
				return isDone;
			}
		}

		class SendRequest extends OneShotBehaviour{
			SendRequest(Agent a,DataStore ds){
				super(a);
				this.setDataStore(ds);
			}

			@Override
			public void action(){

				ACLMessage request=(ACLMessage) getDataStore().get(INITIATION_K);

				request.addReceiver(curTO.getEnds_at().getConcrete_designation());

				RequestExecuteAppointment act=new RequestExecuteAppointment();
				act.setCorresponds_to(curTOC);
				act.setLoad_offer(curTO);
				myCAgent.fillMessage(request,act);

				Vector<ACLMessage> messages=new Vector<ACLMessage>();
				messages.add(request);

				TransportOrderChainState oldState=myCAgent.setTOCState(curTOC,new Assigned());
				myCAgent.echoStatus("sent RequestExecuteAppointment, state was " + oldState,curTOC,ContainerAgent.LOGGING_INFORM);

				getDataStore().put(ALL_REQUESTS_KEY,messages);
			}

		}

		private void setTargetPosition(Phy_Position targetPosition){
			positionChecker.setTargetPosition(targetPosition);
		}
	}

	class handleInform extends FSMBehaviour{
		//FSM State strings
		private static final String DROPPER="dropper";
		private static final String DUMMY="dummy";

		public handleInform(Agent a,DataStore ds){
			super(a);
			setDataStore(ds);

			//register states
			registerFirstState(new Dropper(a,ds),DROPPER);
			registerLastState(new DummyState(a,ds),DUMMY);

			//register transitions
			registerDefaultTransition(DROPPER,DUMMY);
		}

		class Dropper extends OneShotBehaviour{
			public Dropper(Agent a,DataStore ds){
				super(a);
				setDataStore(ds);
			}

			@Override
			public void action(){
//				myCAgent.echoStatus("received INFORM",curTOC);
				ACLMessage notification=(ACLMessage) getDataStore().get(REPLY_KEY);
				AnnounceLoadStatus loadStatus=(AnnounceLoadStatus) myCAgent.extractAction(notification);

				if(loadStatus.getLoad_status().equals("FINISHED") && myCAgent.dropContainer(curTOC)){
					myCAgent.wakeSleepingBehaviours(curTOC);
				}else{
					myCAgent.echoStatus("FAILURE: Something went hilariously wrong! Load Status was " + loadStatus.getLoad_status() + " TOCState=" + myCAgent.getTOCState(curTOC),curTOC,ContainerAgent.LOGGING_ERROR);
				}
			}
		}
	}
	@Override
	public void handleFailure(ACLMessage msg){
		myCAgent.echoStatus("received Failure");
	}
	
/*
	@Override
	protected Vector<ACLMessage> prepareRequests(ACLMessage request){
		request.addReceiver(curTO.getEnds_at().getConcrete_designation());

		ContainerAgent.enableForCommunication(this.myAgent);
		RequestExecuteAppointment act=new RequestExecuteAppointment();
		act.setCorresponds_to(curTOC);
		act.setLoad_offer(curTO);
		myCAgent.fillMessage(request,act);

		Vector<ACLMessage> messages=new Vector<ACLMessage>();
		messages.add(request);
		TransportOrderChainState oldState=myCAgent.touchTOCState(curTOC,new InExecution());

		myCAgent.echoStatus("sent RequestExecuteAppointment, state was " + oldState,curTOC);
		return messages;
	}

	@Override
	protected void handleInform(ACLMessage msg){
		myCAgent.echoStatus("received INFORM");
	}
	*/
}
