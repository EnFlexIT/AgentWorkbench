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
	protected class handleAcceptProposalManual extends SimpleBehaviour{

		private static final long serialVersionUID= -1740553491760609807L;
		Boolean isDone=false;

		@Override
		public void action(){
			DataStore ds=this.getDataStore();
			ACLMessage accept=(ACLMessage) ds.get(receiveLoadOrders.this.ACCEPT_PROPOSAL_KEY);
			Concept content=((ContainerAgent) this.myAgent).extractAction(accept);
			TransportOrderChain acceptedTOC=((AcceptLoadOffer) content).getLoad_offer();
			((ContainerAgent) this.myAgent).echoStatus("Bewerbung wurde akzeptiert. Überprüfe Kapazitäten für ",acceptedTOC);

			TransportOrderChainState curState=((ContainerAgent) this.myAgent).changeTOCState(acceptedTOC);
			if(curState instanceof ProposedFor){//Angebot wurde angenommen->Alles weitere in die Wege leiten
				if(((ContainerAgent) this.myAgent).hasBayMapRoom()){
					//					((ContainerAgent)myAgent).echoStatus("BayMap hat noch Platz.");
					ds.put(receiveLoadOrders.this.REPLY_KEY,receiveLoadOrders.this.stowContainer(accept));
					this.isDone=true;
					return;
				}else{
					TransportOrderChain curTOC=((ContainerAgent) this.myAgent).getSomeTOCOfState(new Administered());
					if(curTOC != null){
						((ContainerAgent) this.myAgent).echoStatus("BayMap voll, versuche Räumung für",acceptedTOC);
						((ContainerAgent) this.myAgent).changeTOCState(acceptedTOC,new PendingForSubCFP());
						((ContainerAgent) this.myAgent).releaseContainer(curTOC,this);
						this.block();
						this.isDone=false;
						return;
					}else{ //keine administrierten TOCs da
						((ContainerAgent) this.myAgent).echoStatus("FAILURE: BayMap voll, keine administrierten TOCs da, Räumung nicht möglich.");
					}
				}
			}else if(curState instanceof PendingForSubCFP){
				//TOC bereits angenommen, aber noch kein Platz
				if(((ContainerAgent) this.myAgent).countTOCInState(new Announced()) != 0){ // ausschreibungsqueue hat noch inhalt
					((ContainerAgent) this.myAgent).echoStatus("Unterauftrag läuft noch:",acceptedTOC);
					this.block();
					this.isDone=false;
					return;
				}else{ // ausschreibungsqueque ist leer
					((ContainerAgent) this.myAgent).changeTOCState(acceptedTOC,new ProposedFor());
					((ContainerAgent) this.myAgent).echoStatus("Keine Unteraufträge mehr, erneut versuchen aufzunehmen.");
					this.isDone=false;
					return;
				}
			}else if(curState instanceof Failed){
				((ContainerAgent) this.myAgent).echoStatus("FAILURE: Ausschreibung des Unterauftrags ist bereits fehlgeschlagen.");
			}
			//Ausschreibung ist fehlgeschlagen, keine administrierten TOCs da, Irgendwas schiefgelaufen bei der Ausschreibung des Unterauftrags
			((ContainerAgent) this.myAgent).changeTOCState(acceptedTOC,new Failed());
			ds.put(receiveLoadOrders.this.REPLY_KEY,receiveLoadOrders.this.prepareFailure(accept));
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

	public receiveLoadOrders(Agent a,MessageTemplate mt){
		super(a,mt);
		Behaviour b=new handleAcceptProposalManual();
		this.registerHandleAcceptProposal(b);
	}

	public AnnounceLoadStatus getLoadStatusAnnouncement(TransportOrderChain curTOC,String content){
		AnnounceLoadStatus loadStatus=new AnnounceLoadStatus();
		loadStatus.setLoad_status(content);
		loadStatus.setLoad_offer(curTOC);
		return loadStatus;
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
			((ContainerAgent) this.myAgent).echoStatus("no content");
			reply.setContent("no content");
			reply.setPerformative(ACLMessage.FAILURE);
			return reply;
		}
		content=((ContainerAgent) this.myAgent).extractAction(cfp);

		if(content instanceof CallForProposalsOnLoadStage){
			//			((ContainerAgent)myAgent).echoStatus("Stringifyd: "+this.stringifyTransitionTable());

			CallForProposalsOnLoadStage call=(CallForProposalsOnLoadStage) content;
			LoadList liste=call.getRequired_turnover_capacity();
			Iterator allTocs=liste.getAllConsists_of();
			TransportOrderChain curTOC=(TransportOrderChain) allTocs.next();
			//			((ContainerAgent)myAgent).echoStatus("Ausschreibung erhalten.",curTOC);

			if( !((ContainerAgent) this.myAgent).isQueueNotFull()){//schon auf genug Aufträge beworben
				((ContainerAgent) this.myAgent).echoStatus("schon genug Aufträge");
				reply.setContent("schon genug Aufträge");
				reply.setPerformative(ACLMessage.REFUSE);
				return reply;

			}else if( !((ContainerAgent) this.myAgent).checkPlausibility(call)){
				((ContainerAgent) this.myAgent).echoStatus("CFP unplausibel");
				reply.setContent("CFP unplausibel");
				reply.setPerformative(ACLMessage.REFUSE);
				return reply;
			}else if((((ContainerAgent) this.myAgent).determineContractors() != null) && ((ContainerAgent) this.myAgent).determineContractors().isEmpty()){ //won't work any more like this
				((ContainerAgent) this.myAgent).echoStatus("Habe keine Subunternehmer, lehne ab.");
				reply.setContent("Habe keine Subunternehmer, lehne ab.");
				reply.setPerformative(ACLMessage.REFUSE);
				return reply;
			}else if((((ContainerAgent) this.myAgent).determineContractors() == null) && !((ContainerAgent) this.myAgent).hasBayMapRoom()){
				((ContainerAgent) this.myAgent).echoStatus("Habe keine Subunternehmer und bin voll, lehne ab.");
				reply.setContent("Habe keine Subunternehmer und bin voll, lehne ab.");
				reply.setPerformative(ACLMessage.REFUSE);
				return reply;
			}else{ //noch Kapazitäten vorhanden und Anfrage plausibel
				//((ContainerAgent)myAgent).echoStatus("noch Kapazitäten vorhanden");
				ProposeLoadOffer act=((ContainerAgent) this.myAgent).GetLoadProposal(curTOC);
				if(act != null){
					((ContainerAgent) this.myAgent).echoStatus("Bewerbe mich für Ausschreibung.",curTOC);
					((ContainerAgent) this.myAgent).fillMessage(reply,act);
					reply.setPerformative(ACLMessage.PROPOSE);
					return reply;
				}else{
					((ContainerAgent) this.myAgent).echoStatus("Lehne Ausschreibung ab.",curTOC);
					reply.setContent("keine TransportOrder passt zu mir");
					reply.setPerformative(ACLMessage.REFUSE);
					return reply;
				}
			}
		}else{ //unbekannter inhalt in CFP
			((ContainerAgent) this.myAgent).echoStatus("ERROR: Unbekannter Inhalt in CFP: " + cfp.getContent());
			reply.setContent("ERROR: Unbekannter Inhalt in CFP: " + cfp.getContent());
			reply.setPerformative(ACLMessage.FAILURE);

			return reply;
		}
	}

	@Override
	protected void handleRejectProposal(ACLMessage cfp,ACLMessage propose,ACLMessage accept){
		Concept content=((ContainerAgent) this.myAgent).extractAction(propose);
		TransportOrderChain acceptedTOC=((ProposeLoadOffer) content).getLoad_offer();
		//		((ContainerAgent)myAgent).echoStatus("Meine Bewerbung wurde abgelehnt");
		if( !(((ContainerAgent) this.myAgent).changeTOCState(acceptedTOC,null,true) instanceof ProposedFor)){ //wenn der untersuchte Container dem entspricht, für den sich beworben wurde
			((ContainerAgent) this.myAgent).echoStatus("ERROR: Auftrag, auf den ich mich beworben habe (abgelehnt), nicht zum Entfernen gefunden.",acceptedTOC);
		}else{
			//			((ContainerAgent)myAgent).echoStatus("Abgelehnten Auftrag entfernt.",acceptedTOC);
		}
	}

	protected ACLMessage prepareFailure(ACLMessage accept){
		ACLMessage fail=accept.createReply();
		Concept content=((ContainerAgent) this.myAgent).extractAction(accept);
		TransportOrderChain acceptedTOC=((AcceptLoadOffer) content).getLoad_offer();

		AnnounceLoadStatus loadStatus=this.getLoadStatusAnnouncement(acceptedTOC,"BayMap voll und kann nicht geräumt werden.");
		fail.setPerformative(ACLMessage.FAILURE);
		((ContainerAgent) this.myAgent).fillMessage(fail,loadStatus);
		return fail;
	}

	protected ACLMessage stowContainer(ACLMessage accept){
		//		((ContainerAgent)myAgent).echoStatus("action");
		ACLMessage rply=accept.createReply();
		Concept content=((ContainerAgent) this.myAgent).extractAction(accept);
		TransportOrderChain acceptedTOC=((AcceptLoadOffer) content).getLoad_offer();

		//		((ContainerAgent)myAgent).echoStatus("Auftrag wird bearbeitet.",acceptedTOC);

		if(((ContainerAgent) this.myAgent).aquireContainer(acceptedTOC)){
			//			((ContainerAgent)myAgent).echoStatus("Auftrag erfüllt, Container aufgenommen.",acceptedTOC);
			AnnounceLoadStatus loadStatus=this.getLoadStatusAnnouncement(acceptedTOC,"FINISHED");
			rply.setPerformative(ACLMessage.INFORM);
			((ContainerAgent) this.myAgent).fillMessage(rply,loadStatus);
			return rply;
		}else{
			((ContainerAgent) this.myAgent).echoStatus("ERROR: Auftrag kann nicht ausgeführt werden.",acceptedTOC);
			AnnounceLoadStatus loadStatus=this.getLoadStatusAnnouncement(acceptedTOC,"FAILURE");
			rply.setPerformative(ACLMessage.FAILURE);
			((ContainerAgent) this.myAgent).fillMessage(rply,loadStatus);
			return rply;
		}
	}
}