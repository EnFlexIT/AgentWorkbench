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
import jade.util.leap.Iterator;
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
//	private final String ANNOUNCEMENT_KEY="__announcement"+hashCode();


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
	
	private static MessageTemplate createExtMessageTemplate(Agent a, ACLMessage reservationNotice){
		ACLMessage templateMessage=reservationNotice.createReply();
		templateMessage.setPerformative(ACLMessage.INFORM);
		templateMessage.setReplyWith(null);

		MessageTemplate mt=MessageTemplate.MatchCustom(templateMessage,true);
		mt=MessageTemplate.and(mt,new MessageTemplate(new MatchAgentAction(a,new AnnounceLoadStatus())));
		return mt;
	}


	public receiveLoadOrders(Agent a){
		super(a,receiveLoadOrders.createMessageTemplate(a));

		Behaviour b=new handleAcceptProposal(a,getDataStore());
		this.registerHandleAcceptProposal(b);
	}

	@Override
	protected ACLMessage handleCfp(ACLMessage cfp){
		ACLMessage reply=cfp.createReply();

		CallForProposalsOnLoadStage call=(CallForProposalsOnLoadStage) this.myCAgent.extractAction(cfp);
		LoadList liste=call.getRequired_turnover_capacity();
		Iterator allTocs=liste.getAllConsists_of();
		TransportOrderChain curTOC=(TransportOrderChain) allTocs.next();
//			((ContainerAgent)myAgent).echoStatus("Ausschreibung erhalten.",curTOC);

		if( !this.myCAgent.isQueueNotFull()){//schon auf genug Aufträge beworben
			this.myCAgent.echoStatus("schon genug Aufträge, refusing",ContainerAgent.LOGGING_INFORM);
			reply.setContent("schon genug Aufträge");
			reply.setPerformative(ACLMessage.REFUSE);
			return reply;
			/*
			}else if((this.myCAgent.determineContractors() != null) && this.myCAgent.determineContractors().isEmpty()){ //won't work any more like this
			this.myCAgent.echoStatus("Habe keine Subunternehmer, lehne ab.");
			reply.setContent("Habe keine Subunternehmer, lehne ab.");
			reply.setPerformative(ACLMessage.REFUSE);
			return reply;
			*/
		}else if((this.myCAgent.determineContractors() == null) && !this.myCAgent.hasBayMapRoom()){
			this.myCAgent.echoStatus("Habe keine Subunternehmer und bin voll, lehne ab.",ContainerAgent.LOGGING_NOTICE);
//			reply.setContent("Habe keine Subunternehmer und bin voll, lehne ab.");
			AnnounceLoadStatus loadStatus=ContainerAgent.getLoadStatusAnnouncement(curTOC,"REFUSED");
			this.myCAgent.fillMessage(reply,loadStatus);

			reply.setPerformative(ACLMessage.REFUSE);
			return reply;
		}else{ //noch Kapazitäten vorhanden und Anfrage plausibel
			//((ContainerAgent)myAgent).echoStatus("noch Kapazitäten vorhanden");
			ProposeLoadOffer act=this.myCAgent.getLoadProposal(curTOC);
			if(act != null){
				this.myCAgent.echoStatus("Bewerbe mich für Ausschreibung.",curTOC,ContainerAgent.LOGGING_INFORM);
				this.myCAgent.fillMessage(reply,act);

				reply.setPerformative(ACLMessage.PROPOSE);
				return reply;
			}else{
				this.myCAgent.echoStatus("Lehne Ausschreibung ab.",curTOC,ContainerAgent.LOGGING_INFORM);
				reply.setContent("keine TransportOrder passt zu mir");
				reply.setPerformative(ACLMessage.REFUSE);
				return reply;
			}
		}
	}

	protected class handleAcceptProposal extends SequentialBehaviour{
		TransportOrderChain acceptedTOC;
		TransportOrder acceptedTO;

		WaitUntilTargetReached positionChecker;

		handleAcceptProposal(Agent a,DataStore ds){
			super(a);
			this.setDataStore(ds);

			Behaviour b=new StartHandling(a,ds);
			addSubBehaviour(b);

			b=new EnsureRoom(a,ds);
			addSubBehaviour(b);

			b=new checkForPendingSubCFP(a,ds);
			addSubBehaviour(b);

			b=new StartMoving(a,ds);
			addSubBehaviour(b);

			positionChecker=new WaitUntilTargetReached(a,ds);
			b=positionChecker;
			addSubBehaviour(b);

			b=new SendReady(a,ds);
			addSubBehaviour(b);
		}

		class StartHandling extends OneShotBehaviour{

			StartHandling(Agent a,DataStore ds){
				super(a);
				setDataStore(ds);
			}

			/* (non-Javadoc)
			 * @see jade.core.behaviours.Behaviour#action()
			 */
			@Override
			public void action(){
				ACLMessage accept=(ACLMessage) getDataStore().get(ACCEPT_PROPOSAL_KEY);
				Concept content=myCAgent.extractAction(accept);
				acceptedTOC=((AcceptLoadOffer) content).getCorresponds_to();
				acceptedTO=myCAgent.findMatchingOrder(acceptedTOC); //get transport order TO me
			}
		}

		class EnsureRoom extends OneShotBehaviour{
			EnsureRoom(Agent a,DataStore ds){
				super(a);
				setDataStore(ds);
			}

			/* (non-Javadoc)
			 * @see jade.core.behaviours.Behaviour#action()
			 */
			@Override
			public void action(){

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
								myCAgent.echoStatus("BayMap voll, versuche Räumung für",acceptedTOC,ContainerAgent.LOGGING_INFORM);
								myCAgent.touchTOCState(acceptedTOC,new PendingForSubCFP());
								myCAgent.releaseContainer(someTOC,this);
							}
						}

						if(someTOC != null){
							myCAgent.registerForWakeUpCall(this);
							this.block();
						}else{ //keine administrierten TOCs da
							myCAgent.echoStatus("FAILURE: BayMap full, no administered TOCs available, clearing not possible.",ContainerAgent.LOGGING_NOTICE);
						}
					}
				}
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

				TransportOrderChainState curState=myCAgent.touchTOCState(acceptedTOC);
				if(curState instanceof PendingForSubCFP){
					//TOC bereits angenommen, aber noch kein Platz
					if(myCAgent.countTOCInState(new Announced()) != 0){ // ausschreibungsqueue hat noch inhalt
						myCAgent.echoStatus("Unterauftrag läuft noch:",acceptedTOC,ContainerAgent.LOGGING_INFORM);
						isDone=false;
						block();
					}else{ // ausschreibungsqueque ist leer
						myCAgent.touchTOCState(acceptedTOC,new ProposedFor());
						myCAgent.echoStatus("Keine Unteraufträge mehr, erneut versuchen aufzunehmen.",ContainerAgent.LOGGING_INFORM);
					}
				}
			}

			@Override
			public boolean done(){
				return isDone;
			}
		}

		class StartMoving extends OneShotBehaviour{
			StartMoving(Agent a,DataStore ds){
				super(a);
				this.setDataStore(ds);
			}

			public void action(){
				if(myCAgent instanceof MoveableAgent){
					Domain startsAt=myCAgent.inflateDomain(acceptedTO.getStarts_at().getAbstract_designation());
					MoveableAgent myMovableAgent=((MoveableAgent) myAgent);
					myMovableAgent.addAsapMovementTo(startsAt.getIs_in_position());
					positionChecker.setTargetPosition(startsAt.getIs_in_position());

					InExecution newState=new InExecution();
					newState.setLoad_offer(acceptedTO);
					myCAgent.touchTOCState(acceptedTOC,newState);
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

				AnnounceLoadStatus loadStatusAnnouncement=ContainerAgent.getLoadStatusAnnouncement(acceptedTOC,"READY");

				rply.setPerformative(ACLMessage.INFORM);
				myCAgent.fillMessage(rply,loadStatusAnnouncement);
				getDataStore().put(receiveLoadOrders.this.REPLY_KEY,rply);
				myCAgent.addBehaviour(new listenForLoadStatusAnnouncement(myCAgent,rply));
				myCAgent.doWake();
			}
		}

	}

	@Override
	protected void handleRejectProposal(ACLMessage cfp,ACLMessage propose,ACLMessage accept){
		Concept content=this.myCAgent.extractAction(propose);
		TransportOrderChain acceptedTOC=((ProposeLoadOffer) content).getCorresponds_to();
		//		((ContainerAgent)myAgent).echoStatus("Meine Bewerbung wurde abgelehnt");
		if( !(this.myCAgent.touchTOCState(acceptedTOC,null,true) instanceof ProposedFor)){ //wenn der untersuchte Container dem entspricht, für den sich beworben wurde
			this.myCAgent.echoStatus("ERROR: Auftrag, auf den ich mich beworben habe (abgelehnt), nicht zum Entfernen gefunden.",acceptedTOC,ContainerAgent.LOGGING_ERROR);
		}else{
			//			((ContainerAgent)myAgent).echoStatus("Abgelehnten Auftrag entfernt.",acceptedTOC);
		}
	}

	@Override
	protected void handleOutOfSequence(ACLMessage msg){
		this.myCAgent.echoStatus("ERROR: Unerwartete Nachricht bei recieve (" + msg.getPerformative() + ") empfangen von " + msg.getSender().getLocalName() + ": " + msg.getContent(),ContainerAgent.LOGGING_ERROR);
	}
}