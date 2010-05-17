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
import contmas.interfaces.MoveableAgent;
import contmas.interfaces.TransportOrderOfferer;
import contmas.main.MatchAgentAction;
import contmas.ontology.*;

public class receiveLoadOrders extends ContractNetResponder{
	/**
	 * 
	 */
	private static final long serialVersionUID= -3409830399764472591L;
	private final ContainerHolderAgent myCAgent=(ContainerHolderAgent) this.myAgent;
	private TransportOrderChain curTOC;
	private TransportOrder curTO;
	BlockAddress destinationAddress;

	private final String ANNOUNCEMENT_KEY="__announcement";
	private String conversationID;

	//FSM State strings
	private static final String ENSURE_ROOM="ensure_room";
	private static final String CHECK_FOR_PENDING_SUB_CFP="check_for_pending_sub_cfp";

	//FSM events
	private final Integer HAS_ROOM=0;
	private final Integer TRY_FREEING= -1;

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

	class EnsureRoom extends OneShotBehaviour{
		Integer returnState;

		EnsureRoom(Agent a,DataStore ds){
			super(a);
			setDataStore(ds);
		}

		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action(){
			returnState=HAS_ROOM;

			if( !myCAgent.hasBayMapRoom()){
				if(myCAgent instanceof TransportOrderOfferer){
					TransportOrderChain someTOC;
					someTOC=myCAgent.getSomeTOCOfState(new Announced());
					if(someTOC == null){
						someTOC=myCAgent.getSomeTOCOfState(new InExecution());
					}
					if(someTOC == null){
						someTOC=myCAgent.getSomeTOCOfState(new Assigned());
					}
					if(someTOC == null){
						someTOC=myCAgent.getSomeTOCOfState(new Administered());
						if(someTOC != null){
							myCAgent.echoStatus("BayMap voll, versuche Räumung für",curTOC,ContainerAgent.LOGGING_INFORM);
							myCAgent.touchTOCState(curTOC,new PendingForSubCFP());
							myCAgent.releaseContainer(someTOC,this);
						}
					}

					if(someTOC != null){ // TOC is in one of the above states, so all steps taken, just sit back and relax
						myCAgent.registerForWakeUpCall(this);
						this.block();
						returnState=TRY_FREEING;
					}else{ //keine administrierten TOCs da
						myCAgent.echoStatus("FAILURE: BayMap full, no administered TOCs available, clearing not possible.",ContainerAgent.LOGGING_ERROR);
						returnState=ACLMessage.REFUSE;
					}
				}
			}
		}

		@Override
		public int onEnd(){
			return returnState;
		}
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

			TransportOrderChainState curState=myCAgent.touchTOCState(curTOC);
			if(curState instanceof PendingForSubCFP){
				//TOC bereits angenommen, aber noch kein Platz
				if(myCAgent.countTOCInState(new Announced()) != 0){ // ausschreibungsqueue hat noch inhalt
					myCAgent.echoStatus("Unterauftrag läuft noch:",curTOC,ContainerAgent.LOGGING_INFORM);
					myCAgent.registerForWakeUpCall(this);
					isDone=false;
					block();
				}else{ // ausschreibungsqueque ist leer
					myCAgent.touchTOCState(curTOC,new ProposedFor());
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

			registerState(new EnsureRoom(a,ds),ENSURE_ROOM);
			registerState(new checkForPendingSubCFP(a,ds),CHECK_FOR_PENDING_SUB_CFP);
			registerState(new EnsureRoom(a,ds),ENSURE_ROOM2);


			registerState(new GetFreeBlockAddress(a,ds),GET_FREE_BLOCK_ADDRESS);

			registerLastState(new Epilog(a,ds),EPILOG);

			//register transitions
			registerTransition(PROLOG,EPILOG,I_AM_BUSY);
			registerTransition(PROLOG,EPILOG,HAVE_NO_CONTRACTORS);
			registerTransition(PROLOG,EPILOG,CFP_NOT_SUITABLE);
			registerTransition(PROLOG,ENSURE_ROOM,CFP_OK);

			registerTransition(ENSURE_ROOM,CHECK_FOR_PENDING_SUB_CFP,TRY_FREEING);
			registerTransition(ENSURE_ROOM,GET_FREE_BLOCK_ADDRESS,HAS_ROOM);
			registerTransition(ENSURE_ROOM,EPILOG,ACLMessage.REFUSE);

			registerDefaultTransition(CHECK_FOR_PENDING_SUB_CFP,ENSURE_ROOM2);
			
			registerTransition(ENSURE_ROOM2,CHECK_FOR_PENDING_SUB_CFP,TRY_FREEING);
			registerTransition(ENSURE_ROOM2,GET_FREE_BLOCK_ADDRESS,HAS_ROOM);
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

				conversationID=cfp.getConversationId();
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
					ProposeLoadOffer act=myCAgent.getLoadProposal(curTOC);
					if(act != null){
						myCAgent.echoStatus("Bewerbe mich für Ausschreibung.",curTOC,ContainerAgent.LOGGING_INFORM);
						myCAgent.fillMessage(reply,act);

						curTO=act.getLoad_offer(); //get transport order TO me
						//curTO.getStarts_at().getAt_address(); //startaddress available here

						reply.setPerformative(ACLMessage.PROPOSE);
						returnState=CFP_OK;

					}else{
						myCAgent.echoStatus("Lehne Ausschreibung ab.",curTOC,ContainerAgent.LOGGING_INFORM);
						reply.setContent("keine TransportOrder passt zu mir");
						reply.setPerformative(ACLMessage.REFUSE);
						returnState=CFP_NOT_SUITABLE;
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
				TransportOrderChainState state=new Reserved();
				state.setAt_address(destinationAddress);
				myCAgent.touchTOCState(curTOC,state);
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

					myCAgent.touchTOCState(curTOC,new FailedIn());

					AnnounceLoadStatus loadStatus=ContainerAgent.getLoadStatusAnnouncement(curTOC,"BayMap voll und kann nicht geräumt werden.");
					reply.setPerformative(ACLMessage.REFUSE);
					myCAgent.fillMessage(reply,loadStatus);

					getDataStore().put(REPLY_KEY,reply);
//					myCAgent.echoStatus("REFUSE because of no empty address available");

					myAgent.doWake();
				}
			}

		}
	}

	protected class handleAcceptProposal extends FSMBehaviour{
		//DS Key strings
		private static final String FINISH_ANNOUNCEMENT_KEY="__finish_announcement";

		//FSM State strings
		private static final String START_MOVING="start_moving";
		private static final String WAIT_UNTIL_TARGET_REACHED="wait_until_target_reached";
		private static final String ENSURE_ROOM2="ensure_room2";
		private static final String CHECK_FOR_PENDING_SUB_CFP2="check_for_pending_sub_cfp2";
		private static final String SEND_READY="send_ready";
//		private static final String SEND_REFUSE = "send_refuse";
		private static final String SEND_FAILURE="send_failure";
		private static final String RECEIVE_FINISHED="receive_finished";

		private static final String DO_AQUIRE="do_aquire";

		WaitUntilTargetReached positionChecker;

		ACLMessage reservationNotice;

		handleAcceptProposal(Agent a,DataStore ds){
			super(a);
			this.setDataStore(ds);

			//register states
			registerFirstState(new EnsureRoom(a,ds),ENSURE_ROOM);
			registerState(new checkForPendingSubCFP(a,ds),CHECK_FOR_PENDING_SUB_CFP);
			registerState(new StartMoving(a,ds),START_MOVING);
			positionChecker=new WaitUntilTargetReached(a,ds);
			registerState(positionChecker,WAIT_UNTIL_TARGET_REACHED);
			registerState(new EnsureRoom(a,ds),ENSURE_ROOM2);
			registerState(new checkForPendingSubCFP(a,ds),CHECK_FOR_PENDING_SUB_CFP2);
			registerState(new SendReady(a,ds),SEND_READY);
//			registerLastState(new SendRefuse(a,ds),SEND_REFUSE);
			registerLastState(new SendFailure(a,ds),SEND_FAILURE);
			registerState(new ReceiveFinishedAnnouncement(a,ds),RECEIVE_FINISHED);
			registerLastState(new DoAquire(a,ds),DO_AQUIRE);

			//register transitions
			registerTransition(ENSURE_ROOM,CHECK_FOR_PENDING_SUB_CFP,TRY_FREEING);
			registerTransition(ENSURE_ROOM,START_MOVING,HAS_ROOM);
			registerTransition(ENSURE_ROOM,SEND_FAILURE,ACLMessage.REFUSE);

			registerDefaultTransition(CHECK_FOR_PENDING_SUB_CFP,START_MOVING);
			registerDefaultTransition(START_MOVING,WAIT_UNTIL_TARGET_REACHED);
			registerDefaultTransition(WAIT_UNTIL_TARGET_REACHED,ENSURE_ROOM2);

			registerTransition(ENSURE_ROOM2,CHECK_FOR_PENDING_SUB_CFP2,TRY_FREEING);
			registerTransition(ENSURE_ROOM2,SEND_READY,HAS_ROOM);
			registerDefaultTransition(CHECK_FOR_PENDING_SUB_CFP2,SEND_READY);
			registerDefaultTransition(SEND_READY,RECEIVE_FINISHED);
			registerDefaultTransition(RECEIVE_FINISHED,DO_AQUIRE);

			registerTransition(ENSURE_ROOM2,SEND_FAILURE,ACLMessage.REFUSE);

		}

		class StartMoving extends OneShotBehaviour{
			StartMoving(Agent a,DataStore ds){
				super(a);
				this.setDataStore(ds);
			}

			public void action(){

				if(myCAgent instanceof MoveableAgent){
					Domain startsAt=myCAgent.inflateDomain(curTO.getStarts_at().getAbstract_designation());
					MoveableAgent myMovableAgent=((MoveableAgent) myAgent);
					myMovableAgent.addAsapMovementTo(startsAt.getIs_in_position());
					positionChecker.setTargetPosition(startsAt.getIs_in_position());

					InExecution newState=new InExecution();
					newState.setLoad_offer(curTO);
					myCAgent.touchTOCState(curTOC,newState);
				}
			}
		}

		class SendReady extends OneShotBehaviour{
			SendReady(Agent a,DataStore ds){
				super(a);
				this.setDataStore(ds);
			}

			@Override
			public void action(){

				ACLMessage accept=(ACLMessage) getDataStore().get(ACCEPT_PROPOSAL_KEY);
				ACLMessage rply=accept.createReply();

				AnnounceLoadStatus loadStatusAnnouncement=ContainerAgent.getLoadStatusAnnouncement(curTOC,"READY");

				rply.setPerformative(ACLMessage.INFORM);
				myCAgent.fillMessage(rply,loadStatusAnnouncement);
				reservationNotice=rply;
				getDataStore().put(receiveLoadOrders.this.REPLY_KEY,rply);
				myCAgent.send(rply);
				myCAgent.doWake();

			}

		}

		public class ReceiveFinishedAnnouncement extends SimpleBehaviour{
			ContainerHolderAgent myAgent;
			private Boolean isDone;
			private static final long serialVersionUID= -4440040520781720185L;

			public ReceiveFinishedAnnouncement(Agent a,DataStore ds){
				super(a);
				myAgent=(ContainerHolderAgent) a;
				setDataStore(ds);
			}

			@Override
			public void action(){
				isDone=false;
				MessageTemplate mt=createMessageTemplateFinished(myAgent,reservationNotice);
//				myCAgent.echoStatus("ReceiveFinishedAnnouncement");
				ACLMessage msg=myCAgent.receive(mt);
				if(msg != null){
//					myCAgent.echoStatus("something received");

					if(this.myAgent.extractAction(msg) instanceof AnnounceLoadStatus){
//						myCAgent.echoStatus("AnnounceLoadStatus extracted");

						AnnounceLoadStatus act=(AnnounceLoadStatus) this.myAgent.extractAction(msg);

						if(act.getLoad_status().equals("FINISHED")){
//							myAgent.echoStatus("fine");
							getDataStore().put(FINISH_ANNOUNCEMENT_KEY,msg);
							isDone=true;
						}
					}
				}else{
//					myAgent.echoStatus("ReceiveFinishedAnnouncement: blocking");

					block();
				}
			}

			@Override
			public boolean done(){
				return isDone;
			}
		}

		class DoAquire extends SimpleBehaviour{
			ContainerHolderAgent myAgent;

			DoAquire(Agent a,DataStore ds){
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
//				myAgent.echoStatus("doAquire");
				isDone=true;

				ACLMessage request=(ACLMessage) getDataStore().get(FINISH_ANNOUNCEMENT_KEY);
				AnnounceLoadStatus act=(AnnounceLoadStatus) this.myAgent.extractAction(request);
				if(act.getLoad_status().equals("FINISHED")){
					if(act.getCorresponds_to() == null){
//						myAgent.echoStatus("FINISHED recieved, but getCorresponds_to== null");

					}
					if( !myAgent.aquireContainer(act.getCorresponds_to(),destinationAddress)){
						myAgent.echoStatus("Something went wrong! Couldn't aquire!",curTOC,ContainerAgent.LOGGING_ERROR);
					}
					myAgent.doWake();

				}
				myAgent.echoStatus("LoadStatus received: " + act.getLoad_status(),curTOC,ContainerAgent.LOGGING_NOTICE);
				getDataStore().remove(receiveLoadOrders.this.REPLY_KEY);
			}

			/* (non-Javadoc)
			 * @see jade.core.behaviours.Behaviour#done()
			 */
			@Override
			public boolean done(){
				return isDone;
			}
		}

		class SendRefuse extends OneShotBehaviour{
			SendRefuse(Agent a,DataStore ds){
				super(a);
				this.setDataStore(ds);
			}

			@Override
			public void action(){
				ACLMessage accept=(ACLMessage) getDataStore().get(ACCEPT_PROPOSAL_KEY);
				ACLMessage rply=accept.createReply();

				rply.setPerformative(ACLMessage.REFUSE);
				getDataStore().put(receiveLoadOrders.this.REPLY_KEY,rply);
				myCAgent.doWake();
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

				myCAgent.touchTOCState(curTOC,new FailedIn());

				AnnounceLoadStatus loadStatus=ContainerAgent.getLoadStatusAnnouncement(curTOC,"BayMap voll und kann nicht geräumt werden.");
				rply.setPerformative(ACLMessage.FAILURE);
				myCAgent.fillMessage(rply,loadStatus);

				getDataStore().put(receiveLoadOrders.this.REPLY_KEY,rply);
				myCAgent.doWake();
			}
		}

	}

	@Override
	protected void handleRejectProposal(ACLMessage cfp,ACLMessage propose,ACLMessage accept){
		Concept content=this.myCAgent.extractAction(propose);
		TransportOrderChain acceptedTOC=((ProposeLoadOffer) content).getCorresponds_to();
		//		((ContainerAgent)myAgent).echoStatus("Meine Bewerbung wurde abgelehnt");
		TransportOrderChainState oldState=this.myCAgent.touchTOCState(acceptedTOC,null,true);
		if( !(oldState instanceof ProposedFor)){ //wenn der untersuchte Container dem entspricht, für den sich beworben wurde
			this.myCAgent.echoStatus("ERROR: Auftrag, auf den ich mich beworben habe (abgelehnt), nicht zum Entfernen gefunden. War "+oldState.getClass().getSimpleName(),acceptedTOC,ContainerAgent.LOGGING_ERROR);
		}else{
			//			((ContainerAgent)myAgent).echoStatus("Abgelehnten Auftrag entfernt.",acceptedTOC);
		}
	}

	@Override
	protected void handleOutOfSequence(ACLMessage msg){
		this.myCAgent.echoStatus("ERROR: Unerwartete Nachricht bei recieve (" + msg.getPerformative() + ") empfangen von " + msg.getSender().getLocalName() + ": " + msg.getContent(),ContainerAgent.LOGGING_ERROR);
	}
}