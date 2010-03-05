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

package contmas.agents;

import jade.content.Concept;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import jade.util.leap.Iterator;
import contmas.ontology.*;

public class receiveLoadOrders extends ContractNetResponder{
	protected class handleAcceptProposal extends SimpleBehaviour{
		private static final long serialVersionUID= -1740553491760609807L;
		private final ContainerAgent myCAgent=(ContainerAgent) this.myAgent;
		private Boolean isDone=false;
		@Override
		public void action(){
			DataStore ds=this.getDataStore();
			ACLMessage accept=(ACLMessage) ds.get(receiveLoadOrders.this.ACCEPT_PROPOSAL_KEY);
			Concept content=this.myCAgent.extractAction(accept);
			TransportOrderChain acceptedTOC=((AcceptLoadOffer) content).getLoad_offer();
			ACLMessage rply=accept.createReply();

			this.myCAgent.echoStatus("Bewerbung wurde akzeptiert. Überprüfe Kapazitäten für ",acceptedTOC);

			TransportOrderChainState curState=this.myCAgent.changeTOCState(acceptedTOC);
			if(curState instanceof ProposedFor){//Angebot wurde angenommen->Alles weitere in die Wege leiten
				if(this.myCAgent.hasBayMapRoom()){
//					((ContainerAgent)myAgent).echoStatus("BayMap hat noch Platz.");

					if(this.myCAgent.aquireContainer(acceptedTOC)){
//						((ContainerAgent)myAgent).echoStatus("Auftrag erfüllt, Container aufgenommen.",acceptedTOC);
						AnnounceLoadStatus loadStatus=ContainerAgent.getLoadStatusAnnouncement(acceptedTOC,"FINISHED");
						rply.setPerformative(ACLMessage.INFORM);
						this.myCAgent.fillMessage(rply,loadStatus);
					}else{
						this.myCAgent.echoStatus("ERROR: Auftrag kann nicht ausgeführt werden.",acceptedTOC);
						AnnounceLoadStatus loadStatus=ContainerAgent.getLoadStatusAnnouncement(acceptedTOC,"FAILURE");
						rply.setPerformative(ACLMessage.FAILURE);
						this.myCAgent.fillMessage(rply,loadStatus);
					}
					
					ds.put(receiveLoadOrders.this.REPLY_KEY,rply);
					this.isDone=true;
					return;
				}else{
					TransportOrderChain curTOC=this.myCAgent.getSomeTOCOfState(new Administered());
					if(curTOC != null){
						this.myCAgent.echoStatus("BayMap voll, versuche Räumung für",acceptedTOC);
						this.myCAgent.changeTOCState(acceptedTOC,new PendingForSubCFP());
						this.myCAgent.releaseContainer(curTOC,this);
						this.block();
						this.isDone=false;
						return;
					}else{ //keine administrierten TOCs da
						this.myCAgent.echoStatus("FAILURE: BayMap voll, keine administrierten TOCs da, Räumung nicht möglich.");
					}
				}
			}else if(curState instanceof PendingForSubCFP){
				//TOC bereits angenommen, aber noch kein Platz
				if(this.myCAgent.countTOCInState(new Announced()) != 0){ // ausschreibungsqueue hat noch inhalt
					this.myCAgent.echoStatus("Unterauftrag läuft noch:",acceptedTOC);
					this.block();
					this.isDone=false;
					return;
				}else{ // ausschreibungsqueque ist leer
					this.myCAgent.changeTOCState(acceptedTOC,new ProposedFor());
					this.myCAgent.echoStatus("Keine Unteraufträge mehr, erneut versuchen aufzunehmen.");
					this.isDone=false;
					return;
				}
			}else if(curState instanceof Failed){
				this.myCAgent.echoStatus("FAILURE: Ausschreibung des Unterauftrags ist bereits fehlgeschlagen.");
			}
			//Ausschreibung ist fehlgeschlagen, keine administrierten TOCs da, Irgendwas schiefgelaufen bei der Ausschreibung des Unterauftrags
			this.myCAgent.changeTOCState(acceptedTOC,new Failed());


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
	private final ContainerAgent myCAgent=(ContainerAgent) this.myAgent;

	public receiveLoadOrders(Agent a,MessageTemplate mt){
		super(a,mt);
		Behaviour b=new handleAcceptProposal();
		this.registerHandleAcceptProposal(b);
	}

	@Override
	protected ACLMessage handleCfp(ACLMessage cfp){
		ACLMessage reply=cfp.createReply();
		Concept content;
		//		((ContainerAgent)myAgent).echoStatus("CFP empfangen");
		if(cfp.getPerformative() != ACLMessage.CFP){
			this.myAgent.putBack(cfp);
			this.reset();
			return null;
		}
		if(cfp.getContent() == null){ //CFP leer
			this.myCAgent.echoStatus("no content");
			reply.setContent("no content");
			reply.setPerformative(ACLMessage.FAILURE);
			return reply;
		}
		content=this.myCAgent.extractAction(cfp);

		if(content instanceof CallForProposalsOnLoadStage){
			//			((ContainerAgent)myAgent).echoStatus("Stringifyd: "+this.stringifyTransitionTable());

			CallForProposalsOnLoadStage call=(CallForProposalsOnLoadStage) content;
			LoadList liste=call.getRequired_turnover_capacity();
			Iterator allTocs=liste.getAllConsists_of();
			TransportOrderChain curTOC=(TransportOrderChain) allTocs.next();
//			((ContainerAgent)myAgent).echoStatus("Ausschreibung erhalten.",curTOC);

			if( !this.myCAgent.isQueueNotFull()){//schon auf genug Aufträge beworben
				this.myCAgent.echoStatus("schon genug Aufträge");
				reply.setContent("schon genug Aufträge");
				reply.setPerformative(ACLMessage.REFUSE);
				return reply;
			}else if((this.myCAgent.determineContractors() != null) && this.myCAgent.determineContractors().isEmpty()){ //won't work any more like this
				this.myCAgent.echoStatus("Habe keine Subunternehmer, lehne ab.");
				reply.setContent("Habe keine Subunternehmer, lehne ab.");
				reply.setPerformative(ACLMessage.REFUSE);
				return reply;
			}else if((this.myCAgent.determineContractors() == null) && !this.myCAgent.hasBayMapRoom()){
				this.myCAgent.echoStatus("Habe keine Subunternehmer und bin voll, lehne ab.");
				reply.setContent("Habe keine Subunternehmer und bin voll, lehne ab.");
				reply.setPerformative(ACLMessage.REFUSE);
				return reply;
			}else{ //noch Kapazitäten vorhanden und Anfrage plausibel
				//((ContainerAgent)myAgent).echoStatus("noch Kapazitäten vorhanden");
				ProposeLoadOffer act=this.myCAgent.GetLoadProposal(curTOC);
				if(act != null){
					this.myCAgent.echoStatus("Bewerbe mich für Ausschreibung.",curTOC);
					this.myCAgent.fillMessage(reply,act);
					reply.setPerformative(ACLMessage.PROPOSE);
					return reply;
				}else{
					this.myCAgent.echoStatus("Lehne Ausschreibung ab.",curTOC);
					reply.setContent("keine TransportOrder passt zu mir");
					reply.setPerformative(ACLMessage.REFUSE);
					return reply;
				}
			}
		}else{ //unbekannter inhalt in CFP
			this.myCAgent.echoStatus("ERROR: Unbekannter Inhalt in CFP: " + cfp.getContent());
			reply.setContent("ERROR: Unbekannter Inhalt in CFP: " + cfp.getContent());
			reply.setPerformative(ACLMessage.FAILURE);

			return reply;
		}
	}

	@Override
	protected void handleRejectProposal(ACLMessage cfp,ACLMessage propose,ACLMessage accept){
		Concept content=this.myCAgent.extractAction(propose);
		TransportOrderChain acceptedTOC=((ProposeLoadOffer) content).getLoad_offer();
		//		((ContainerAgent)myAgent).echoStatus("Meine Bewerbung wurde abgelehnt");
		if( !(this.myCAgent.changeTOCState(acceptedTOC,null,true) instanceof ProposedFor)){ //wenn der untersuchte Container dem entspricht, für den sich beworben wurde
			this.myCAgent.echoStatus("ERROR: Auftrag, auf den ich mich beworben habe (abgelehnt), nicht zum Entfernen gefunden.",acceptedTOC);
		}else{
			//			((ContainerAgent)myAgent).echoStatus("Abgelehnten Auftrag entfernt.",acceptedTOC);
		}
	}

}