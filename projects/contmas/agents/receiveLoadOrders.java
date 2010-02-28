package contmas.agents;

import contmas.ontology.AcceptLoadOffer;
import contmas.ontology.Administerd;
import contmas.ontology.AnnounceLoadStatus;
import contmas.ontology.Announced;
import contmas.ontology.CallForProposalsOnLoadStage;
import contmas.ontology.Designator;
import contmas.ontology.Failed;
import contmas.ontology.LoadList;
import contmas.ontology.PendingForSubCFP;
import contmas.ontology.ProposeLoadOffer;
import contmas.ontology.ProposedFor;
import contmas.ontology.Street;
import contmas.ontology.TOCHasState;
import contmas.ontology.TransportOrder;
import contmas.ontology.TransportOrderChain;
import contmas.ontology.TransportOrderChainState;
import jade.content.Concept;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import jade.util.leap.Iterator;

public class receiveLoadOrders extends ContractNetResponder{
	public receiveLoadOrders(Agent a, MessageTemplate mt) {
		super(a, mt);
		Behaviour b=new handleAcceptProposalManual();
		registerHandleAcceptProposal(b);
	}
	
	protected ACLMessage handleCfp(ACLMessage cfp){
		ACLMessage reply = cfp.createReply();
		Concept content;
//		((ContainerAgent)myAgent).echoStatus("CFP empfangen");
		if(cfp.getPerformative()!=ACLMessage.CFP){
			myAgent.putBack(cfp);
			this.reset();
			return null;
		}
		if(cfp.getContent()==null){ //CFP leer
			((ContainerAgent)myAgent).echoStatus("no content");
			reply.setContent("no content");
			reply.setPerformative(ACLMessage.FAILURE);
			return reply;
		}
		content = ((ContainerAgent)myAgent).extractAction(cfp);

		if (content instanceof CallForProposalsOnLoadStage) {
//			((ContainerAgent)myAgent).echoStatus("Stringifyd: "+this.stringifyTransitionTable());

			CallForProposalsOnLoadStage call=(CallForProposalsOnLoadStage) content;
			LoadList liste=call.getRequired_turnover_capacity();
			Iterator allTocs=liste.getAllConsists_of();
			TransportOrderChain curTOC=(TransportOrderChain) allTocs.next();
//			((ContainerAgent)myAgent).echoStatus("Ausschreibung erhalten.",curTOC);

			if(!((ContainerAgent)myAgent).isQueueNotFull()){//schon auf genug Aufträge beworben
				((ContainerAgent)myAgent).echoStatus("schon genug Aufträge");
				reply.setContent("schon genug Aufträge");
		    	reply.setPerformative(ACLMessage.REFUSE);
				return reply;

			} else if (!((ContainerAgent)myAgent).checkPlausibility(call)) {
				((ContainerAgent)myAgent).echoStatus("CFP unplausibel");
				reply.setContent("CFP unplausibel");
		    	reply.setPerformative(ACLMessage.REFUSE);
				return reply;
			} else if (((ContainerAgent)myAgent).determineContractors()!=null && ((ContainerAgent)myAgent).determineContractors().isEmpty()) { //won't work any more like this
				((ContainerAgent)myAgent).echoStatus("Habe keine Subunternehmer, lehne ab.");
				reply.setContent("Habe keine Subunternehmer, lehne ab.");
		    	reply.setPerformative(ACLMessage.REFUSE);
				return reply;
			} else if (((ContainerAgent)myAgent).determineContractors()==null && !((ContainerAgent)myAgent).hasBayMapRoom()) {
				((ContainerAgent)myAgent).echoStatus("Habe keine Subunternehmer und bin voll, lehne ab.");
				reply.setContent("Habe keine Subunternehmer und bin voll, lehne ab.");
		    	reply.setPerformative(ACLMessage.REFUSE);
				return reply;
			} else { //noch Kapazitäten vorhanden und Anfrage plausibel
				//((ContainerAgent)myAgent).echoStatus("noch Kapazitäten vorhanden");
				ProposeLoadOffer act=((ContainerAgent) myAgent).GetLoadProposal(curTOC);
				if(act!=null){
					((ContainerAgent)myAgent).echoStatus("Bewerbe mich für Ausschreibung.",curTOC);
					((ContainerAgent)myAgent).fillMessage(reply,act);
					reply.setPerformative(ACLMessage.PROPOSE);
					return reply;
				} else {
					((ContainerAgent)myAgent).echoStatus("Lehne Ausschreibung ab.",curTOC);
					reply.setContent("keine TransportOrder passt zu mir");
		        	reply.setPerformative(ACLMessage.REFUSE);
		    		return reply;
				}
			}
		} else { //unbekannter inhalt in CFP
			((ContainerAgent)myAgent).echoStatus("ERROR: Unbekannter Inhalt in CFP: "+cfp.getContent());
			reply.setContent("ERROR: Unbekannter Inhalt in CFP: "+cfp.getContent());
			reply.setPerformative(ACLMessage.FAILURE);
			
			return reply;
		}
	}
	
	protected class handleAcceptProposalManual extends SimpleBehaviour{
		Boolean isDone=false;
		public void action(){
			DataStore ds=getDataStore();
			ACLMessage accept=(ACLMessage) ds.get(ACCEPT_PROPOSAL_KEY);
			Concept content= ((ContainerAgent)myAgent).extractAction(accept);
			TransportOrderChain acceptedTOC=((AcceptLoadOffer) content).getLoad_offer();
			((ContainerAgent)myAgent).echoStatus("Bewerbung wurde akzeptiert. Überprüfe Kapazitäten für ",acceptedTOC);

			TransportOrderChainState curState = ((ContainerAgent)myAgent).changeTOCState(acceptedTOC);
			if(curState instanceof ProposedFor){//Angebot wurde angenommen->Alles weitere in die Wege leiten
				if(((ContainerAgent)myAgent).hasBayMapRoom()){
//					((ContainerAgent)myAgent).echoStatus("BayMap hat noch Platz.");
					ds.put(REPLY_KEY,stowContainer(accept));
					isDone=true;
					return;
				} else {
					TransportOrderChain curTOC=((ContainerAgent)myAgent).getSomeTOCOfState(new Administerd());
					if(curTOC!=null){
						((ContainerAgent)myAgent).echoStatus("BayMap voll, versuche Räumung für",acceptedTOC);
						((ContainerAgent)myAgent).changeTOCState(acceptedTOC, new PendingForSubCFP());
						((ContainerAgent)myAgent).releaseContainer(curTOC,this);
						this.block();
						isDone=false;
						return;
					} else { //keine administrierten TOCs da
						((ContainerAgent)myAgent).echoStatus("FAILURE: BayMap voll, keine administrierten TOCs da, Räumung nicht möglich.");
					}
				}
			} else if(curState instanceof PendingForSubCFP){
				//TOC bereits angenommen, aber noch kein Platz
				if(((ContainerAgent)myAgent).countTOCInState(new Announced())!=0){ // ausschreibungsqueue hat noch inhalt
					((ContainerAgent)myAgent).echoStatus("Unterauftrag läuft noch:",acceptedTOC);
					this.block();
					isDone=false;
					return;
				} else { // ausschreibungsqueque ist leer
					((ContainerAgent)myAgent).changeTOCState(acceptedTOC, new ProposedFor());
					((ContainerAgent)myAgent).echoStatus("Keine Unteraufträge mehr, erneut versuchen aufzunehmen.");
					isDone=false;
					return;
				}
			} else if(curState instanceof Failed){
				((ContainerAgent)myAgent).echoStatus("FAILURE: Ausschreibung des Unterauftrags ist bereits fehlgeschlagen.");
			}
			//Ausschreibung ist fehlgeschlagen, keine administrierten TOCs da, Irgendwas schiefgelaufen bei der Ausschreibung des Unterauftrags
			((ContainerAgent)myAgent).changeTOCState(acceptedTOC, new Failed());
			ds.put(REPLY_KEY, prepareFailure(accept));
			isDone=true;
			return;
		}
		
		public boolean done() {
			return isDone;
		}
	}
	
	protected void handleRejectProposal(ACLMessage cfp,ACLMessage propose, ACLMessage accept){
		Concept content=((ContainerAgent)myAgent).extractAction(propose);
		TransportOrderChain acceptedTOC=((ProposeLoadOffer) content).getLoad_offer();
//		((ContainerAgent)myAgent).echoStatus("Meine Bewerbung wurde abgelehnt");
		if(!(((ContainerAgent)myAgent).changeTOCState(acceptedTOC, null, true) instanceof ProposedFor)){ //wenn der untersuchte Container dem entspricht, für den sich beworben wurde
			((ContainerAgent)myAgent).echoStatus("ERROR: Auftrag, auf den ich mich beworben habe (abgelehnt), nicht zum Entfernen gefunden.",acceptedTOC);
		} else{
//			((ContainerAgent)myAgent).echoStatus("Abgelehnten Auftrag entfernt.",acceptedTOC);
		}
	}
	
	protected ACLMessage stowContainer(ACLMessage accept){
//		((ContainerAgent)myAgent).echoStatus("action");
		ACLMessage rply = accept.createReply();
		Concept content= ((ContainerAgent)myAgent).extractAction(accept);
		TransportOrderChain acceptedTOC=((AcceptLoadOffer) content).getLoad_offer();

//		((ContainerAgent)myAgent).echoStatus("Auftrag wird bearbeitet.",acceptedTOC);
				
		if(((ContainerAgent) myAgent).aquireContainer(acceptedTOC)){ 
//			((ContainerAgent)myAgent).echoStatus("Auftrag erfüllt, Container aufgenommen.",acceptedTOC);
			AnnounceLoadStatus loadStatus=getLoadStatusAnnouncement(acceptedTOC,"FINISHED");
			rply.setPerformative(ACLMessage.INFORM);
			((ContainerAgent)myAgent).fillMessage(rply,loadStatus);
			return rply;
		} else {
			((ContainerAgent)myAgent).echoStatus("ERROR: Auftrag kann nicht ausgeführt werden.",acceptedTOC);
			AnnounceLoadStatus loadStatus=getLoadStatusAnnouncement(acceptedTOC, "FAILURE");
			rply.setPerformative(ACLMessage.FAILURE);
			((ContainerAgent)myAgent).fillMessage(rply,loadStatus);
			return rply;
		}
	}
	
	protected ACLMessage prepareFailure(ACLMessage accept){
		ACLMessage fail = accept.createReply();	
		Concept content= ((ContainerAgent)myAgent).extractAction(accept);
		TransportOrderChain acceptedTOC=((AcceptLoadOffer) content).getLoad_offer();

		AnnounceLoadStatus loadStatus=getLoadStatusAnnouncement(acceptedTOC, "BayMap voll und kann nicht geräumt werden.");
		fail.setPerformative(ACLMessage.FAILURE);
		((ContainerAgent)myAgent).fillMessage(fail,loadStatus);
		return fail;
	}
	
	public AnnounceLoadStatus getLoadStatusAnnouncement(TransportOrderChain curTOC, String content){
		AnnounceLoadStatus loadStatus=new AnnounceLoadStatus();
		loadStatus.setLoad_status(content);
		loadStatus.setLoad_offer(curTOC);
		return loadStatus;
	}
}