/**
 * @author Hanno - Felix Wagner
 * Copyright 2010 Hanno - Felix Wagner
 * This file is
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

import jade.content.Concept;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.proto.ContractNetResponder;
import contmas.agents.ContainerAgent;
import contmas.agents.ContainerHolderAgent;
import contmas.interfaces.TacticalMemorizer;
import contmas.main.MatchAgentAction;
import contmas.ontology.*;

public class receiveLoadOrders extends ContractNetResponder{
	/**
	 * 
	 */
	private static final long serialVersionUID= -3409830399764472591L;
	final ContainerHolderAgent myCAgent=(ContainerHolderAgent) this.myAgent;
	private ProposeLoadOffer loadOffer;
	TransportOrderChain curTOC;
	TransportOrder curTO;
	BlockAddress destinationAddress;
	private EnsureRoom roomEnsurer;

	//FSM State strings
	private static final String CHECK_FOR_PENDING_SUB_CFP="check_for_pending_sub_cfp";



	private static MessageTemplate createMessageTemplate(Agent a){
		MessageTemplate mtallg=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		MessageTemplate mtThisSect=null;
		MessageTemplate mtSect=null;

		mtThisSect=new MessageTemplate(new MatchAgentAction(a,new CallForProposalsOnLoadStage()));
		mtThisSect=MessageTemplate.and(mtThisSect,MessageTemplate.MatchPerformative(ACLMessage.CFP));
		mtSect=mtThisSect;

		mtThisSect=new MessageTemplate(new MatchAgentAction(a,new AcceptLoadOffer()));
		mtThisSect=MessageTemplate.and(mtThisSect,MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL));
		mtSect=MessageTemplate.or(mtSect,mtThisSect);

		mtThisSect=MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL);
		mtSect=MessageTemplate.or(mtSect,mtThisSect);

		return MessageTemplate.and(mtallg,mtSect);
	}

//	private MessageTemplate createMessageTemplateFinished(Agent a){
//		
//		MessageTemplate mt=new MessageTemplate(new MatchAgentAction(a,new AnnounceLoadStatus()));
//		/*
//		MessageTemplate mt=MessageTemplate.MatchConversationId(conversationID);
//		mt=MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(a,new AnnounceLoadStatus())));
//		*/
//		return mt;
//	}

	private MessageTemplate createMessageTemplateFinished(Agent a,ACLMessage reservationNotice){
		ACLMessage templateMessage=reservationNotice.createReply();
		templateMessage.setPerformative(ACLMessage.INFORM);
		templateMessage.setReplyWith(null);

		MessageTemplate mt=MessageTemplate.MatchCustom(templateMessage,true);
		mt=MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(a,new AnnounceLoadStatus())));
		return mt;
	}

	public receiveLoadOrders(Agent a){
		super(a,receiveLoadOrders.createMessageTemplate(a));

		Behaviour b=new handleCfp(a,getDataStore());
		this.registerHandleCfp(b);

		b=new handleAcceptProposal(a,getDataStore());
		this.registerHandleAcceptProposal(b);
	}

	class checkForPendingSubCFP extends SimpleBehaviour{
		Boolean isDone;

		checkForPendingSubCFP(Agent a,DataStore ds){
			super(a);
			setDataStore(ds);
		}

		@Override
		public void action(){
			isDone=true;

			TransportOrderChainState curState=myCAgent.getTOCState(curTOC);
			if(curState instanceof PendingForSubCFP){
				//TOC bereits angenommen, aber noch kein Platz
				if(myCAgent.countTOCInState(new Announced()) != 0){ // ausschreibungsqueue hat noch inhalt
					myCAgent.echoStatus("Unterauftrag läuft noch:",curTOC,ContainerAgent.LOGGING_DEBUG);
					myCAgent.registerForWakeUpCall(this);
					isDone=false;
				}else{ // ausschreibungsqueque ist leer
					myCAgent.setTOCState(curTOC,new ProposedFor());
					myCAgent.echoStatus("Keine Unteraufträge mehr, erneut versuchen aufzunehmen.",ContainerAgent.LOGGING_INFORM);
				}
			}
		}

		@Override
		public boolean done(){
			return isDone;
		}
	}

	protected class handleCfp extends FSMBehaviour{

		//FSM State strings
		private static final String PROLOG="prolog";
		private static final String GET_FREE_BLOCK_ADDRESS="get_free_block_address";
		private static final String EPILOG="epilog";
		private static final String ENSURE_ROOM="ensure_room";

		private static final String ENSURE_ROOM2="ensure_room2";

		//FSM State events
		private final Integer I_AM_BUSY= -3;
		private final Integer HAVE_NO_CONTRACTORS= -2;
		private final Integer CFP_NOT_SUITABLE= -1;
		private final Integer CFP_OK=1;

		handleCfp(Agent a,DataStore ds){
			super(a);
			this.setDataStore(ds);

			//register states
			registerFirstState(new Prolog(a,ds),PROLOG);

			roomEnsurer=new EnsureRoom(a,ds);
			registerState(roomEnsurer,ENSURE_ROOM);
			registerState(new checkForPendingSubCFP(a,ds),CHECK_FOR_PENDING_SUB_CFP);
			registerState(roomEnsurer,ENSURE_ROOM2);


			registerState(new GetFreeBlockAddress(a,ds),GET_FREE_BLOCK_ADDRESS);

			registerLastState(new Epilog(a,ds),EPILOG);

			//register transitions
			registerTransition(PROLOG,EPILOG,I_AM_BUSY);
			registerTransition(PROLOG,EPILOG,HAVE_NO_CONTRACTORS);
			registerTransition(PROLOG,EPILOG,CFP_NOT_SUITABLE);
			registerTransition(PROLOG,ENSURE_ROOM,CFP_OK);

			registerTransition(ENSURE_ROOM,CHECK_FOR_PENDING_SUB_CFP,EnsureRoom.TRY_FREEING);
			registerTransition(ENSURE_ROOM,GET_FREE_BLOCK_ADDRESS,EnsureRoom.HAS_ROOM);
			registerTransition(ENSURE_ROOM,EPILOG,ACLMessage.REFUSE);

			registerDefaultTransition(CHECK_FOR_PENDING_SUB_CFP,ENSURE_ROOM2);
			
			registerTransition(ENSURE_ROOM2,CHECK_FOR_PENDING_SUB_CFP,EnsureRoom.TRY_FREEING);
			registerTransition(ENSURE_ROOM2,GET_FREE_BLOCK_ADDRESS,EnsureRoom.HAS_ROOM);
			registerTransition(ENSURE_ROOM2,EPILOG,ACLMessage.REFUSE);

			registerDefaultTransition(GET_FREE_BLOCK_ADDRESS,EPILOG);
		}

		class Prolog extends OneShotBehaviour{

			private Integer returnState;

			/**
			 * @param a
			 * @param ds
			 */
			public Prolog(Agent a,DataStore ds){
				super(a);
				setDataStore(ds);
			}

			/* (non-Javadoc)
			 * @see jade.core.behaviours.Behaviour#action()
			 */
			@Override
			public void action(){
				returnState=CFP_OK;

				ACLMessage cfp=(ACLMessage) getDataStore().get(CFP_KEY);
				ACLMessage reply=cfp.createReply();

				destinationAddress=null;

				CallForProposalsOnLoadStage call=(CallForProposalsOnLoadStage) myCAgent.extractAction(cfp);
				curTOC=call.getCorresponds_to();
//					((ContainerAgent)myAgent).echoStatus("Ausschreibung erhalten.",curTOC);

				if( !myCAgent.isQueueNotFull()){//schon auf genug Aufträge beworben
					myCAgent.echoStatus("schon genug Aufträge, refusing",ContainerAgent.LOGGING_INFORM);
					reply.setContent("schon genug Aufträge");
					reply.setPerformative(ACLMessage.REFUSE);
					returnState=I_AM_BUSY;

					/*
					}else if((this.myCAgent.determineContractors() != null) && this.myCAgent.determineContractors().isEmpty()){ //won't work any more like this
					this.myCAgent.echoStatus("Habe keine Subunternehmer, lehne ab.");
					reply.setContent("Habe keine Subunternehmer, lehne ab.");
					reply.setPerformative(ACLMessage.REFUSE);
					return reply;
					*/
				}else if((myCAgent.determineContractors() == null) && !myCAgent.hasBayMapRoom()){
					myCAgent.echoStatus("Habe keine Subunternehmer und bin voll, lehne ab.",ContainerAgent.LOGGING_NOTICE);
//					reply.setContent("Habe keine Subunternehmer und bin voll, lehne ab.");
					AnnounceLoadStatus loadStatus=ContainerAgent.getLoadStatusAnnouncement(curTOC,"REFUSED");
					myCAgent.fillMessage(reply,loadStatus);

					reply.setPerformative(ACLMessage.REFUSE);
					returnState=HAVE_NO_CONTRACTORS;

				}else{ //noch Kapazitäten vorhanden und Anfrage plausibel
					//((ContainerAgent)myAgent).echoStatus("noch Kapazitäten vorhanden");
					loadOffer=myCAgent.getLoadProposal(curTOC);
					if(loadOffer == null){
						myCAgent.echoStatus("Lehne Ausschreibung ab.",curTOC,ContainerAgent.LOGGING_INFORM);
						reply.setContent("keine TransportOrder passt zu mir");
						reply.setPerformative(ACLMessage.REFUSE);
						returnState=CFP_NOT_SUITABLE;
					}else{
						myCAgent.echoStatus("Bewerbe mich für Ausschreibung.",curTOC,ContainerAgent.LOGGING_INFORM);

						curTO=loadOffer.getLoad_offer(); //get transport order TO me
						//curTO.getStarts_at().getAt_address(); //startaddress available here
						
						roomEnsurer.configure(curTOC, curTO);
						
						returnState=CFP_OK;
					}
				}

				getDataStore().put(REPLY_KEY,reply);
			}

			@Override
			public int onEnd(){
//				myCAgent.echoStatus("returnState"+returnState);
				return returnState;
			}

		}

		class GetFreeBlockAddress extends SimpleBehaviour{
			ContainerHolderAgent myAgent;

			GetFreeBlockAddress(Agent a,DataStore ds){
				super(a);
				myAgent=(ContainerHolderAgent) a;
				setDataStore(ds);
			}

			private Boolean isDone;

			/* (non-Javadoc)
			 * @see jade.core.behaviours.Behaviour#action()
			 */
			@Override
			public void action(){
				isDone=true;
				if(destinationAddress == null){
					destinationAddress=myAgent.getEmptyBlockAddress(curTOC); //zieladresse besorgen
					isDone=false;
				}else if(destinationAddress.getX_dimension() == -1 || destinationAddress.getY_dimension() == -1 || destinationAddress.getZ_dimension() == -1){

					isDone=false;
					block(100);
				}
				curTO.getEnds_at().setAt_address(destinationAddress);
				loadOffer.getLoad_offer().getEnds_at().setAt_address(destinationAddress);

				TransportOrderChainState state=new Reserved();
				state.setAt_address(destinationAddress);
				state.setLoad_offer(curTO);
				myCAgent.setTOCState(curTOC,state);
			}

			/* (non-Javadoc)
			 * @see jade.core.behaviours.Behaviour#done()
			 */
			@Override
			public boolean done(){
				return isDone;
			}
		}

		class Epilog extends OneShotBehaviour{

			/**
			 * @param a
			 * @param ds
			 */
			public Epilog(Agent a,DataStore ds){
				super(a);
				setDataStore(ds);
			}

			/* (non-Javadoc)
			 * @see jade.core.behaviours.Behaviour#action()
			 */
			@Override
			public void action(){
//				myCAgent.echoStatus("This is Epilog speaking");
				if(destinationAddress == null){ //only, if no space available, i.e. no Address can be found
					ACLMessage reply=(ACLMessage) getDataStore().get(REPLY_KEY);

					myCAgent.setTOCState(curTOC,new FailedIn());

					AnnounceLoadStatus loadStatus=ContainerAgent.getLoadStatusAnnouncement(curTOC,"BayMap voll und kann nicht geräumt werden.");
					reply.setPerformative(ACLMessage.REFUSE);
					myCAgent.fillMessage(reply,loadStatus);

					getDataStore().put(REPLY_KEY,reply);
//					myCAgent.echoStatus("REFUSE because of no empty address available");

					myAgent.doWake();
				} else {
					ACLMessage cfp=(ACLMessage) getDataStore().get(CFP_KEY);
					ACLMessage reply=cfp.createReply();

					myCAgent.fillMessage(reply,loadOffer);

					reply.setPerformative(ACLMessage.PROPOSE);
					getDataStore().put(REPLY_KEY,reply);

				}
			}

		}
	}

	protected class handleAcceptProposal extends FSMBehaviour{
		//FSM State strings
		private static final String ENSURE_ROOM3="ensure_room3";

//		private static final String ENSURE_ROOM4="ensure_room4";
		private static final String CHECK_FOR_PENDING_SUB_CFP2="check_for_pending_sub_cfp2";
		private static final String SEND_PENDING="send_pending";
		private static final String SEND_FAILURE="send_failure";

		handleAcceptProposal(Agent a,DataStore ds){
			super(a);
			this.setDataStore(ds);

			//register states
			registerFirstState(roomEnsurer,ENSURE_ROOM3);
			registerState(new checkForPendingSubCFP(a,ds),CHECK_FOR_PENDING_SUB_CFP);
//			registerState(roomEnsurer,ENSURE_ROOM4);
			registerState(new checkForPendingSubCFP(a,ds),CHECK_FOR_PENDING_SUB_CFP2);
			registerLastState(new SendPending(a,ds),SEND_PENDING);
			registerLastState(new SendFailure(a,ds),SEND_FAILURE);

			//register transitions
			registerTransition(ENSURE_ROOM3,CHECK_FOR_PENDING_SUB_CFP,EnsureRoom.TRY_FREEING);
			registerTransition(ENSURE_ROOM3,SEND_PENDING,EnsureRoom.HAS_ROOM);
			registerTransition(ENSURE_ROOM3,SEND_FAILURE,ACLMessage.REFUSE);

			registerDefaultTransition(CHECK_FOR_PENDING_SUB_CFP,SEND_PENDING);

			/*
			registerDefaultTransition(CHECK_FOR_PENDING_SUB_CFP,ENSURE_ROOM4);
			
			registerTransition(ENSURE_ROOM4,CHECK_FOR_PENDING_SUB_CFP2,EnsureRoom.TRY_FREEING);
			registerTransition(ENSURE_ROOM4,SEND_PENDING,EnsureRoom.HAS_ROOM);
			registerTransition(ENSURE_ROOM4,SEND_FAILURE,ACLMessage.REFUSE);

			registerDefaultTransition(CHECK_FOR_PENDING_SUB_CFP2,SEND_PENDING);
*/

		}

		class SendPending extends OneShotBehaviour{
			SendPending(Agent a,DataStore ds){
				super(a);
				this.setDataStore(ds);
			}

			@Override
			public void action(){
				ACLMessage accept=(ACLMessage) getDataStore().get(ACCEPT_PROPOSAL_KEY);
				ACLMessage rply=accept.createReply();

				myCAgent.setTOCState(curTOC,new PlannedIn());
				
				AnnounceLoadStatus loadStatusAnnouncement=ContainerAgent.getLoadStatusAnnouncement(curTOC,"PENDING");

				rply.setPerformative(ACLMessage.INFORM);
				myCAgent.fillMessage(rply,loadStatusAnnouncement);
				getDataStore().put(receiveLoadOrders.this.REPLY_KEY,rply);
			}
		}

		class SendFailure extends OneShotBehaviour{
			SendFailure(Agent a,DataStore ds){
				super(a);
				this.setDataStore(ds);
			}

			@Override
			public void action(){
				ACLMessage accept=(ACLMessage) getDataStore().get(ACCEPT_PROPOSAL_KEY);
				ACLMessage rply=accept.createReply();

				myCAgent.setTOCState(curTOC,new FailedIn());

				AnnounceLoadStatus loadStatus=ContainerAgent.getLoadStatusAnnouncement(curTOC,"BayMap voll und kann nicht geräumt werden.");
				rply.setPerformative(ACLMessage.FAILURE);
				myCAgent.fillMessage(rply,loadStatus);
				getDataStore().put(receiveLoadOrders.this.REPLY_KEY,rply);
			}
		}

	}

	@Override
	protected void handleRejectProposal(ACLMessage cfp,ACLMessage propose,ACLMessage accept){
		Concept content=this.myCAgent.extractAction(propose);
		TransportOrderChain acceptedTOC=((ProposeLoadOffer) content).getCorresponds_to();
		//		((ContainerAgent)myAgent).echoStatus("Meine Bewerbung wurde abgelehnt");
		TransportOrderChainState oldState=this.myCAgent.touchTOCState(acceptedTOC,null,true);
		if( !(oldState instanceof Reserved)){ //wenn der untersuchte Container dem entspricht, für den sich beworben wurde
			this.myCAgent.echoStatus("ERROR: Auftrag, auf den ich mich beworben habe (abgelehnt), nicht zum Entfernen gefunden. War "+oldState.getClass().getSimpleName(),acceptedTOC,ContainerAgent.LOGGING_ERROR);
		}else{
			//			((ContainerAgent)myAgent).echoStatus("Abgelehnten Auftrag entfernt.",acceptedTOC);
			if(this.myCAgent instanceof TacticalMemorizer){
				((TacticalMemorizer) this.myCAgent).memorizeTacticalTarget(oldState.getLoad_offer().getStarts_at());
			}
		}
	}

	@Override
	protected void handleOutOfSequence(ACLMessage msg){
		this.myCAgent.echoStatus("ERROR: Unerwartete Nachricht bei recieve (" + msg.getPerformative() + ") empfangen von " + msg.getSender().getLocalName() + ": " + msg.getContent(),ContainerAgent.LOGGING_ERROR);
	}
}