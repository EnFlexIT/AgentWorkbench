package contmas.agents;

import contmas.ontology.AcceptLoadOffer;
import contmas.ontology.AnnounceLoadStatus;
import contmas.ontology.CallForProposalsOnLoadStage;
import contmas.ontology.Designator;
import contmas.ontology.LoadList;
import contmas.ontology.ProposeLoadOffer;
import contmas.ontology.Street;
import contmas.ontology.TransportOrder;
import contmas.ontology.TransportOrderChain;
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
			} else if (((ContainerAgent)myAgent).determineContractors()!=null && ((ContainerAgent)myAgent).determineContractors().isEmpty()) {
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
	
//	protected ACLMessage handleAcceptProposalManual(ACLMessage cfp,ACLMessage propose, ACLMessage accept){
	protected class handleAcceptProposalManual extends SimpleBehaviour{
	Boolean isDone=false;
	public void action(){
		DataStore ds=getDataStore();
		ACLMessage accept=(ACLMessage) ds.get(ACCEPT_PROPOSAL_KEY);

		
		Concept content= ((ContainerAgent)myAgent).extractAction(accept);
		TransportOrderChain acceptedTOC=((AcceptLoadOffer) content).getLoad_offer();

		if(((ContainerAgent)myAgent).isAlreadyPending(acceptedTOC)){  // && ausschreibungsqueue hat noch inhalt
			if(!(((ContainerAgent)myAgent).announcedQueue.isEmpty())){
				((ContainerAgent)myAgent).echoStatus("unterauftrag läuft noch:",acceptedTOC);

				//myAgent.putBack(accept);
				this.block();
				isDone=false;
				return;
			} else {
				((ContainerAgent)myAgent).pendingQueue.add(acceptedTOC);
			}
		} 
		

		((ContainerAgent)myAgent).echoStatus("Bewerbung wurde akzeptiert. Überprüfe Kapazitäten für ",acceptedTOC);
		
		if(((ContainerAgent)myAgent).hasBayMapRoom()){
//			((ContainerAgent)myAgent).echoStatus("BayMap hat noch Platz.");

//			return stowContainer(accept);
			ds.put(REPLY_KEY,stowContainer(accept));
//			((ContainerAgent)myAgent).echoStatus("REPLY_KEY ist "+((ACLMessage) ds.get(REPLY_KEY)).getPerformative());

			isDone=true;
			if(((ContainerAgent)myAgent).isAlreadyPending(acceptedTOC)){
				((ContainerAgent)myAgent).removeFromPendingQueue(acceptedTOC);
			}
			return;
			
		} else {

			if(releaseSomeContainer()){ // try making some room

				((ContainerAgent)myAgent).echoStatus("BayMap voll, versuche Räumung für",acceptedTOC);
				/* Eigentlich nicht, denn sonst würden ja auch alle bisherigen Antworten wiederholt
				myAgent.putBack(cfp);
				myAgent.putBack(propose);
				*/
				/*
				myAgent.putBack(accept);
				isDone=true;
				this.getParent().block();
				((FSMBehaviour)this.getParent()).resetStates(new String[]{"Handle-Accept-Proposal"});
//				((FSMBehaviour)this.getParent()).
				//return null;
				myAgent.addBehaviour(this);
				*/
				//myAgent.putBack(accept);
				this.block();
				isDone=false;

				return;

			} else { //has been already checked, makeRoom failed, no chance
				((ContainerAgent)myAgent).echoStatus("BayMap voll, Räumung fehlgeschlagen.");
				//TODO (?) removeCommission
				
				ds.put(REPLY_KEY, prepareFailure(accept));
				isDone=true;
				return;
//				return prepareFailure(accept);
			}
		}
	}
	
	public boolean done() {
		return isDone;
	}
	
	}
	
	protected void handleRejectProposal(ACLMessage cfp,ACLMessage propose, ACLMessage accept){
		Concept content=((ContainerAgent)myAgent).extractAction(propose);
		TransportOrderChain acceptedTOC=((ProposeLoadOffer) content).getLoad_offer();
//		((ContainerAgent)myAgent).echoStatus("Meine Bewerbung wurde abgelehnt");
		if(!((ContainerAgent)myAgent).removeFromQueue(acceptedTOC)){ //wenn der untersuchte Container dem entspricht, für den sich beworben wurde
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
	public Boolean releaseSomeContainer(){ //irgendeinen
		Iterator commissions=((ContainerAgent)myAgent).ontologyRepresentation.getAdministers().getAllConsists_of();
		while(commissions.hasNext()){ //Agent hat Transportaufträge abzuarbeiten
			TransportOrderChain curTOC=((TransportOrderChain) commissions.next());
			((ContainerAgent)myAgent).echoStatus("Verfüge über Transportgüter, versuche Container abzugeben.",curTOC);
			return releaseContainer(curTOC);
		}
		((ContainerAgent)myAgent).echoStatus("ERROR: Keine Transportgüter verfügbar.");
		return false;
	}
	
	public Boolean releaseContainer(TransportOrderChain curTOC){
//		TransportOrderChain TOChain=new TransportOrderChain();
//		TOChain.setTransports(curTOC.getTransports());
		if(((ContainerAgent)myAgent).isInFailedQueue(curTOC)){// && !((ContainerAgent)myAgent).isInAnnouncedQueue(curTOC)){ //Ausschreibung bereits fehlgeschlagen, abbrechen!
			((ContainerAgent)myAgent).echoStatus("Ausschreibung bereits fehlgeschlagen, abbrechen!",curTOC);
			return false;
		}
		if(((ContainerAgent)myAgent).isInAnnouncedQueue(curTOC)){ //Ausschreibung läuft noch, nicht nochmal, abwarten!
			((ContainerAgent)myAgent).echoStatus("Ausschreibung läuft noch, nicht nochmal, abwarten!",curTOC);
			return true;
		}
		TransportOrderChain TOChain=curTOC;

		TransportOrder TO=new TransportOrder();
		
		Designator myself=new Designator();
		myself.setType("concrete");
		myself.setConcrete_designation(((ContainerAgent)myAgent).getAID());
		
		Designator target=new Designator(); //TODO mögliche ziele herausfinden
		target.setType("abstract");
		target.setAbstract_designation(new Street());//TODO change to Land but implement recursive Domain-determination in passiveHolder

		TO.setStarts_at(myself);
		TO.setEnds_at(target);
		TOChain.addIs_linked_by(TO);
		LoadList newCommission=new LoadList();
		newCommission.addConsists_of(TOChain);
		Behaviour b=new announceLoadOrders(((ContainerAgent)myAgent), newCommission,this);
		((ContainerAgent)myAgent).addBehaviour(b);
		return true; //Ausschreibung erfolgt
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
/*
protected class checkForUtilization extends OneShotBehaviour{
	Boolean checkedAgain=false;
	public checkForUtilization(Boolean checkedAgain) {
		this.checkedAgain=checkedAgain;
	}
	public checkForUtilization() {
		this(false);
	}
	public void action(){
//		((ContainerAgent)myAgent).echoStatus("Bewerbung wurde akzeptiert. Überprüfe Kapazitäten.");
		SequentialBehaviour sb=((SequentialBehaviour) getParent());
		Behaviour b;
		if(((ContainerAgent)myAgent).hasBayMapRoom()){
//			((ContainerAgent)myAgent).echoStatus("BayMap hat noch Platz.");
			b=new stowContainer();
			b.setDataStore(getDataStore());
			sb.addSubBehaviour(b);
		} else {
			if(checkedAgain){ //has been already checked, makeRoom failed, no chance
				((ContainerAgent)myAgent).echoStatus("BayMap voll, Räumung fehlgeschlagen.");
				b=new prepareFailure();
				b.setDataStore(getDataStore());
				sb.addSubBehaviour(b);
			} else { // try making some room
				((ContainerAgent)myAgent).echoStatus("BayMap voll, versuche Räumung.");
				b=new makeWay();
				b.setDataStore(getDataStore());
				sb.addSubBehaviour(b);
				b=new checkForUtilization(true);
				b.setDataStore(getDataStore());
				sb.addSubBehaviour(b);
			}
		}
	}
}
*/


//CONTENT OF CONSTRUCTOR
/*
SequentialBehaviour sb = new SequentialBehaviour(a);
sb.setDataStore(getDataStore());
*/

/*		
Behaviour b=new stowContainer();
b.setDataStore(getDataStore());
sb.addSubBehaviour(b);
*/
//b=new disposePayload(a);

/*
Behaviour b=new checkForUtilization();
b.setDataStore(getDataStore());
sb.addSubBehaviour(b);
registerHandleAcceptProposal(sb);
*/