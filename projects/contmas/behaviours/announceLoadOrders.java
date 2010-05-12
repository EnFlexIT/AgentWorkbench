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

import jade.content.AgentAction;
import jade.content.Concept;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import jade.proto.states.ReplySender;
import jade.util.leap.List;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import contmas.agents.ContainerAgent;
import contmas.agents.ContainerHolderAgent;
import contmas.agents.StraddleCarrierAgent;
import contmas.behaviours.receiveLoadOrders.handleAcceptProposal.SendReady;
import contmas.behaviours.receiveLoadOrders.handleAcceptProposal.StartHandling;
import contmas.interfaces.MoveableAgent;
import contmas.ontology.*;

public class announceLoadOrders extends ContractNetInitiator{
	/**
	 * 
	 */
	private static final long serialVersionUID=7080809105355535853L;
	private final LoadList currentLoadList;
	TransportOrderChain curTOC;
	TransportOrder curTO;
	Domain endDomain;

	private final Behaviour masterBehaviour;
	private final ContainerHolderAgent myCAgent=(ContainerHolderAgent) this.myAgent;
	String ACCEPT_KEY="__accept" + hashCode();

	public announceLoadOrders(Agent a,LoadList currentLoadList){
		this(a,currentLoadList,null);
	}

	public announceLoadOrders(Agent a,LoadList currentLoadList,Behaviour masterBehaviour){
		super(a,null);
		this.currentLoadList=currentLoadList;
		this.masterBehaviour=masterBehaviour;

		Behaviour b=new handleAllResultNotifications(a,getDataStore());
		b.setDataStore(getDataStore());

		this.registerHandleAllResultNotifications(b);
	}

	@Override
	protected Vector<ACLMessage> prepareCfps(ACLMessage cfp){
		TransportOrderChain curTOC=(TransportOrderChain) this.currentLoadList.getConsists_of().iterator().next();
		TransportOrderChainState oldState=this.myCAgent.touchTOCState(curTOC,new Announced());
		if(oldState instanceof Administered){
			//OK, go on
		}else if(oldState instanceof Announced){
			this.myCAgent.echoStatus("Auftrag bereits ausgeschrieben, nicht nocheinmal.",curTOC,ContainerAgent.LOGGING_INFORM);
			return null;
		}else{
			this.myCAgent.echoStatus("FAILURE: Auftrag wird nicht ausgeschrieben werden, nicht administriert, wahrscheinlich schon failure.",curTOC,ContainerAgent.LOGGING_NOTICE);
			this.myCAgent.touchTOCState(curTOC,new FailedOut());
			return null;
		}
		this.myCAgent.echoStatus("Schreibe Auftrag aus.",curTOC,ContainerAgent.LOGGING_INFORM);
		cfp=new ACLMessage(ACLMessage.CFP);
		cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		List contractorList=this.myCAgent.determineContractors();
		if((contractorList == null) || contractorList.isEmpty()){
			if(this.masterBehaviour != null){
				this.myCAgent.echoStatus("MasterBehaviour wird neugestartet.",ContainerAgent.LOGGING_INFORM);
				this.masterBehaviour.restart();
			}
			this.myCAgent.echoStatus("FAILURE: Keine Contractors mehr vorhanden. Ausschreibung nicht möglich.",curTOC,ContainerAgent.LOGGING_NOTICE);
			this.myCAgent.touchTOCState(curTOC,new FailedOut());
			return null;
		}
		Iterator<?> allContractors=contractorList.iterator();

		while(allContractors.hasNext()){
			cfp.addReceiver((AID) allContractors.next());
		}
		CallForProposalsOnLoadStage act=new CallForProposalsOnLoadStage();
		act.setRequired_turnover_capacity(this.currentLoadList);
		this.myCAgent.fillMessage(cfp,act);
		Vector<ACLMessage> messages=new Vector<ACLMessage>();
		cfp.setReplyByDate(new Date(System.currentTimeMillis() + 50000)); //500000
		messages.add(cfp);

		//		((ContainerAgent)myAgent).echoStatus("Auftrag ausgeschrieben.",curTOC);
		return messages;
	}

	@Override
	protected void handleAllResponses(Vector responses,Vector acceptances){
		TransportOrder bestOffer=null;
		TransportOrderChain bestOfferToc=null;

		ACLMessage bestOfferMessage=null;
		for(Object message: responses){
			ACLMessage propose=(ACLMessage) message;
			if(propose.getPerformative() == ACLMessage.REFUSE){
				this.myCAgent.echoStatus("Ablehnung empfangen von " + propose.getSender().getLocalName() + ": " + propose.getContent(),ContainerAgent.LOGGING_INFORM);
			}
			if((propose.getContent() != null) && (propose.getPerformative() == ACLMessage.PROPOSE)){
				Concept content;
				content=this.myCAgent.extractAction(propose);
				if(content instanceof ProposeLoadOffer){
					ProposeLoadOffer proposal=(ProposeLoadOffer) content;
					TransportOrder offer=this.myCAgent.findMatchingOrder(proposal.getCorresponds_to(),false);
					if((bestOffer == null) || (Long.parseLong(offer.getTakes_until()) < Long.parseLong(bestOffer.getTakes_until()))){ //bisher beste Zeit
						bestOffer=offer;
						bestOfferMessage=propose;
						bestOfferToc=proposal.getCorresponds_to();
					}
				}
			} // End if content !=null
		}
		ACLMessage accept=null;
		curTOC=(TransportOrderChain) this.currentLoadList.getConsists_of().iterator().next();
		
		if(bestOffer == null){ //Abnehmer momentan alle beschäftigt
			this.myCAgent.echoStatus("FAILURE: Nur Ablehnungen empfangen, Abbruch.",ContainerAgent.LOGGING_NOTICE);
			this.myCAgent.touchTOCState(curTOC,new FailedOut());

			notifyMaster("REFUSE");
			
			this.reset();
			return;
		}
		if(bestOffer != null){
			curTO=bestOffer;
			endDomain=myCAgent.inflateDomain(curTO.getEnds_at().getAbstract_designation());
			accept=bestOfferMessage.createReply();
			AcceptLoadOffer act=new AcceptLoadOffer();
			act.setCorresponds_to(bestOfferToc);
			this.myCAgent.fillMessage(accept,act);
			accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
			getDataStore().put(ACCEPT_KEY,accept);
			acceptances.add(accept);
		}
		for(Object message: responses){
			ACLMessage propose=(ACLMessage) message;
			if((propose != bestOfferMessage) && (propose.getPerformative() == ACLMessage.PROPOSE)){
				accept=propose.createReply();
				accept.setPerformative(ACLMessage.REJECT_PROPOSAL);
				acceptances.add(accept);
			}
		}
	}

	class handleAllResultNotifications extends FSMBehaviour{
		String READY_NOTIFICATION_KEY="__ready-notification" + hashCode();
		String RESULT_NOTIFICATION_KEY="__result-notification" + hashCode();
		
		//FSM events
		private final Integer CONTRACTOR_READY=1;
		private final Integer CONTRACTOR_NOT_READY=0;


		//FSM State strings
		private static final String START_MOVING="start_moving";
		private static final String WAIT_UNTIL_TARGET_REACHED="wait_until_target_reached";
		private static final String DROPPER="dropper";
		private static final String SEND_INFORM_FINISHED="send_inform_finished";
		private static final String DUMMY_END="dummy_end";

		WaitUntilTargetReached positionChecker;
		
		handleAllResultNotifications(Agent a,DataStore ds){
			super(a);
			this.setDataStore(ds);
			
			registerFirstState(new StartMoving(a,ds),START_MOVING);
			positionChecker=new WaitUntilTargetReached(a,ds);
			registerState(positionChecker,WAIT_UNTIL_TARGET_REACHED);
			registerState(new Dropper(myAgent,getDataStore()),DROPPER);
			/*
			b=new GetInformReady(myAgent,getDataStore());
			addSubBehaviour(b);
			*/
			registerLastState(new SendNotification(myAgent,getDataStore(),RESULT_NOTIFICATION_KEY,READY_NOTIFICATION_KEY),SEND_INFORM_FINISHED);
			class DummyClass extends OneShotBehaviour{public void action(){/*myCAgent.echoStatus("DUMMYALARM!");*/}}
			registerLastState(new DummyClass(),DUMMY_END);
			
			//register transitions
			registerTransition(START_MOVING,WAIT_UNTIL_TARGET_REACHED,CONTRACTOR_READY);
			registerTransition(START_MOVING,DUMMY_END,CONTRACTOR_NOT_READY);

			registerDefaultTransition(START_MOVING,WAIT_UNTIL_TARGET_REACHED);
			registerDefaultTransition(WAIT_UNTIL_TARGET_REACHED,DROPPER);
			registerDefaultTransition(DROPPER,SEND_INFORM_FINISHED);

		}

		class StartMoving extends OneShotBehaviour{
			Integer returnEvent=CONTRACTOR_READY;
			StartMoving(Agent a,DataStore ds){
				super(a);
				this.setDataStore(ds);
			}

			@Override
			public void action(){
				Vector<ACLMessage> resultNotifications=(Vector) this.getDataStore().get(ALL_RESULT_NOTIFICATIONS_KEY);
				for(ACLMessage notification: resultNotifications){ //TODO check on multiple notifications!
									
					AgentAction content=myCAgent.extractAction(notification);
					if(content instanceof AnnounceLoadStatus){
						AnnounceLoadStatus loadStatus=(AnnounceLoadStatus) content;
						this.getDataStore().put(READY_NOTIFICATION_KEY,notification);
						
						if(notification.getPerformative() == ACLMessage.FAILURE){ // && loadStatus.getLoad_status().substring(0, 4).equals("ERROR")) {
							myCAgent.removeFromContractors(notification.getSender());
							myCAgent.touchTOCState(curTOC,new FailedOut());
							myCAgent.echoStatus("Containerabgabe fehlgeschlagen. Msg=" + loadStatus.getLoad_status(),curTOC,ContainerAgent.LOGGING_NOTICE);
							
							notifyMaster("FAILURE");
							myCAgent.wakeSleepingBehaviours(curTOC);
							returnEvent=CONTRACTOR_NOT_READY;
						} else {
							if(myCAgent instanceof MoveableAgent){
								MoveableAgent myMoveableAgent=(MoveableAgent) myCAgent;
								myCAgent.touchTOCState(curTOC,new Assigned());
								myCAgent.echoStatus("Container is going to be dropped at " + endDomain.getId() + " " + StraddleCarrierAgent.positionToString(endDomain.getIs_in_position()) + ", current position: " + StraddleCarrierAgent.positionToString(myMoveableAgent.getCurrentPosition()));
								myMoveableAgent.addAsapMovementTo(endDomain.getIs_in_position());
								setTargetPosition(endDomain.getIs_in_position());
							}
						}
					}
				}
			}
			@Override
			public int onEnd(){
				return returnEvent;
			}
		}

		/*
		class GetInformReady extends MsgReceiver{
			GetInformReady(Agent a,DataStore ds){
				super(a,replyTemplate,MsgReceiver.INFINITE,ds,READY_NOTIFICATION_KEY);
				this.setDataStore(ds);
			}
		}
*/
		class Dropper extends OneShotBehaviour{

			public Dropper(Agent a,DataStore ds){
				super(a);
				setDataStore(ds);
			}

			@Override
			public void action(){
				ACLMessage readyNotification=(ACLMessage) getDataStore().get(READY_NOTIFICATION_KEY);
				if(myCAgent.dropContainer(curTOC)){
					notifyMaster("INFORM");

					ACLMessage statusAnnouncement=readyNotification.createReply();
					statusAnnouncement.setPerformative(ACLMessage.INFORM);
					AnnounceLoadStatus loadStatus=ContainerAgent.getLoadStatusAnnouncement(curTOC,"FINISHED");
					myCAgent.fillMessage(statusAnnouncement,loadStatus);
					getDataStore().put(RESULT_NOTIFICATION_KEY,statusAnnouncement);
				}else{
					myCAgent.echoStatus("FAILURE: Something went hilariously wrong!");
				}
			}
		}

		class SendNotification extends ReplySender{
			public SendNotification(Agent a,DataStore ds,String replyKey,String msgKey){
				super(a,replyKey,msgKey,ds);
			}			
		}
		
		
		
		private void setTargetPosition(Phy_Position targetPosition){
			positionChecker.setTargetPosition(targetPosition);
		}
		
	}

	@Override
	protected void handleOutOfSequence(ACLMessage msg){
		this.myCAgent.echoStatus("ERROR: Unerwartete Nachricht bei announce (" + msg.getPerformative() + ") empfangen von " + msg.getSender().getLocalName() + ": " + msg.getContent(),ContainerAgent.LOGGING_ERROR);
	}
	
	private void notifyMaster(String cause){
		if(masterBehaviour != null){
			myCAgent.echoStatus(cause+": MasterBehaviour wird neugestartet.",ContainerAgent.LOGGING_INFORM);
			masterBehaviour.restart();
		}
	}
	

}