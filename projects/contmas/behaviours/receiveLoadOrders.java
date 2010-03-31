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
import jade.core.behaviours.DataStore;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.proto.ContractNetResponder;
import jade.util.leap.Iterator;
import contmas.agents.ContainerAgent;
import contmas.agents.ContainerHolderAgent;
import contmas.agents.TransportOrderOfferer;
import contmas.main.MatchAgentAction;
import contmas.ontology.*;

public class receiveLoadOrders extends ContractNetResponder{

	protected class handleAcceptProposal extends SimpleBehaviour{

		private static final long serialVersionUID= -1740553491760609807L;
		private final ContainerHolderAgent myCAgent;
		private Boolean isDone=false;

		handleAcceptProposal(ContainerHolderAgent myCAgent){
			this.myCAgent=myCAgent;
		}

		@Override
		public void action(){
			DataStore ds=this.getDataStore();
			ACLMessage accept=(ACLMessage) ds.get(receiveLoadOrders.this.ACCEPT_PROPOSAL_KEY);
			Concept content=this.myCAgent.extractAction(accept);
			TransportOrderChain acceptedTOC=((AcceptLoadOffer) content).getLoad_offer();
			ACLMessage rply=accept.createReply();

			this.myCAgent.echoStatus("Bewerbung wurde akzeptiert. Überprüfe Kapazitäten für ",acceptedTOC,ContainerAgent.LOGGING_INFORM);

			TransportOrderChainState curState=this.myCAgent.touchTOCState(acceptedTOC);
			if(curState instanceof ProposedFor){//Angebot wurde angenommen->Alles weitere in die Wege leiten
				if(this.myCAgent.hasBayMapRoom()){
//					((ContainerAgent)myAgent).echoStatus("BayMap hat noch Platz.");

					if(this.myCAgent.aquireContainer(acceptedTOC)){
//						((ContainerAgent)myAgent).echoStatus("Auftrag erfüllt, Container aufgenommen.",acceptedTOC);
						AnnounceLoadStatus loadStatus=ContainerAgent.getLoadStatusAnnouncement(acceptedTOC,"FINISHED");
						rply.setPerformative(ACLMessage.INFORM);
						this.myCAgent.fillMessage(rply,loadStatus);
						ds.put(receiveLoadOrders.this.REPLY_KEY,rply);
						this.isDone=true;
						this.myAgent.doWake();
						return;
					}else{
						this.myCAgent.echoStatus("ERROR: Auftrag kann nicht ausgeführt werden.",acceptedTOC,ContainerAgent.LOGGING_ERROR);
					}
				}else{
					if(this.myCAgent instanceof TransportOrderOfferer){
						TransportOrderChain curTOC=this.myCAgent.getSomeTOCOfState(new Administered());
						if(curTOC != null){
							this.myCAgent.echoStatus("BayMap voll, versuche Räumung für",acceptedTOC,ContainerAgent.LOGGING_INFORM);
							this.myCAgent.touchTOCState(acceptedTOC,new PendingForSubCFP());
							this.myCAgent.releaseContainer(curTOC,this);
							this.block();
							this.isDone=false;
							return;
						}else{ //keine administrierten TOCs da
							this.myCAgent.echoStatus("FAILURE: BayMap voll, keine administrierten TOCs da, Räumung nicht möglich.",ContainerAgent.LOGGING_NOTICE);
						}
					}else{//Agent kann keine Aufträge abgeben=>Senke
						this.myCAgent.echoStatus("FAILURE: Bin Senke, kann keine Aufträge weitergeben.",ContainerAgent.LOGGING_NOTICE);
					}
				}
			}else if(curState instanceof PendingForSubCFP){
				//TOC bereits angenommen, aber noch kein Platz
				if(this.myCAgent.countTOCInState(new Announced()) != 0){ // ausschreibungsqueue hat noch inhalt
					this.myCAgent.echoStatus("Unterauftrag läuft noch:",acceptedTOC,ContainerAgent.LOGGING_INFORM);
					this.block();
					this.isDone=false;
					return;
				}else{ // ausschreibungsqueque ist leer
					this.myCAgent.touchTOCState(acceptedTOC,new ProposedFor());
					this.myCAgent.echoStatus("Keine Unteraufträge mehr, erneut versuchen aufzunehmen.",ContainerAgent.LOGGING_INFORM);
					this.isDone=false;
					return;
				}
			}else if(curState instanceof Failed){
				this.myCAgent.echoStatus("FAILURE: Ausschreibung des Unterauftrags ist bereits fehlgeschlagen.",ContainerAgent.LOGGING_NOTICE);
			}
			//Ausschreibung ist fehlgeschlagen, keine administrierten TOCs da, Irgendwas schiefgelaufen bei der Ausschreibung des Unterauftrags
			this.myCAgent.touchTOCState(acceptedTOC,new Failed());

			AnnounceLoadStatus loadStatus=ContainerAgent.getLoadStatusAnnouncement(acceptedTOC,"BayMap voll und kann nicht geräumt werden.");
			rply.setPerformative(ACLMessage.FAILURE);
			this.myCAgent.fillMessage(rply,loadStatus);

			ds.put(receiveLoadOrders.this.REPLY_KEY,rply);
			this.isDone=true;
			return;
		}

		@Override
		public boolean done(){
			return this.isDone;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID= -3409830399764472591L;
	private final ContainerHolderAgent myCAgent=(ContainerHolderAgent) this.myAgent;

	private static MessageTemplate createMessageTemplate(Agent a){
		MessageTemplate mtallg=AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		MessageTemplate mtthissect=null;
		MessageTemplate mtsect=null;

		mtthissect=new MessageTemplate(new MatchAgentAction(a,new CallForProposalsOnLoadStage()));
		mtthissect=MessageTemplate.and(mtthissect,MessageTemplate.MatchPerformative(ACLMessage.CFP));
		mtsect=mtthissect;

		mtthissect=new MessageTemplate(new MatchAgentAction(a,new AcceptLoadOffer()));
		mtthissect=MessageTemplate.and(mtthissect,MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL));
		mtsect=MessageTemplate.or(mtsect,mtthissect);

		mtthissect=MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL);
		mtsect=MessageTemplate.or(mtsect,mtthissect);

		return MessageTemplate.and(mtallg,mtsect);
	}

	public receiveLoadOrders(Agent a){
		super(a,receiveLoadOrders.createMessageTemplate(a));
		this.registerHandleAcceptProposal(new handleAcceptProposal(this.myCAgent));
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

	@Override
	protected void handleOutOfSequence(ACLMessage msg){
		this.myCAgent.echoStatus("ERROR: Unerwartete Nachricht bei recieve (" + msg.getPerformative() + ") empfangen von " + msg.getSender().getLocalName() + ": " + msg.getContent(),ContainerAgent.LOGGING_ERROR);
	}

	@Override
	protected void handleRejectProposal(ACLMessage cfp,ACLMessage propose,ACLMessage accept){
		Concept content=this.myCAgent.extractAction(propose);
		TransportOrderChain acceptedTOC=((ProposeLoadOffer) content).getLoad_offer();
		//		((ContainerAgent)myAgent).echoStatus("Meine Bewerbung wurde abgelehnt");
		if( !(this.myCAgent.touchTOCState(acceptedTOC,null,true) instanceof ProposedFor)){ //wenn der untersuchte Container dem entspricht, für den sich beworben wurde
			this.myCAgent.echoStatus("ERROR: Auftrag, auf den ich mich beworben habe (abgelehnt), nicht zum Entfernen gefunden.",acceptedTOC,ContainerAgent.LOGGING_ERROR);
		}else{
			//			((ContainerAgent)myAgent).echoStatus("Abgelehnten Auftrag entfernt.",acceptedTOC);
		}
	}

}