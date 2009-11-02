package mas.projects.contmas.agents;

import jade.content.Concept;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import jade.util.leap.Iterator;
import mas.projects.contmas.ontology.AcceptLoadOffer;
import mas.projects.contmas.ontology.AnnounceLoadStatus;
import mas.projects.contmas.ontology.CallForProposalsOnLoadStage;
import mas.projects.contmas.ontology.LoadList;
import mas.projects.contmas.ontology.ProposeLoadOffer;
import mas.projects.contmas.ontology.TransportOrderChain;

public class recieveLoadOrders extends ContractNetResponder{
	public recieveLoadOrders(Agent a, MessageTemplate mt) {
		super(a, mt);
		SequentialBehaviour sb = new SequentialBehaviour(a);
		sb.setDataStore(getDataStore());
/*		Behaviour b=new stowContainer();
		b.setDataStore(getDataStore());
		sb.addSubBehaviour(b);
		*/
//		b=new disposePayload(a);
		Behaviour b=new checkForUtilization();
		b.setDataStore(getDataStore());
		sb.addSubBehaviour(b);
		registerHandleAcceptProposal(sb);
	}
	protected ACLMessage handleCfp(ACLMessage cfp){
		ACLMessage reply = cfp.createReply();
		Concept content;
//		((ContainerAgent)myAgent).echoStatus("CFP empfangen");
		if(cfp.getContent()==null){ //CFP leer
			((ContainerAgent)myAgent).echoStatus("no content");
			reply.setContent("no content");
			reply.setPerformative(ACLMessage.FAILURE);
			return reply;
		}
		content = ((ContainerAgent)myAgent).extractAction(cfp);

		if (content instanceof CallForProposalsOnLoadStage) {
			CallForProposalsOnLoadStage call=(CallForProposalsOnLoadStage) content;
			LoadList liste=call.getRequired_turnover_capacity();
			Iterator allTocs=liste.getAllConsists_of();
			TransportOrderChain curTOC=(TransportOrderChain) allTocs.next();

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
			} 
			else { //noch Kapazitäten vorhanden und anfrage plausibel
				//((ContainerAgent)myAgent).echoStatus("noch Kapazitäten vorhanden");
				ProposeLoadOffer act=((ContainerAgent) myAgent).GetLoadProposal(curTOC);
				if(act!=null){
					((ContainerAgent)myAgent).fillMessage(reply,act);
					reply.setPerformative(ACLMessage.PROPOSE);
					return reply;

				} else {
					((ContainerAgent)myAgent).echoStatus("keine TransportOrder passt zu mir");
					reply.setContent("keine TransportOrder passt zu mir");
		        	reply.setPerformative(ACLMessage.REFUSE);
		    		return reply;

				}
			}
		} else { //unbekannter inhalt in CFP
			((ContainerAgent)myAgent).echoStatus("unbekannter inhalt in CFP");
			reply.setContent("unbekannter inhalt in CFP");
			reply.setPerformative(ACLMessage.FAILURE);
			return reply;
		}
	}
	
	protected class makeWay extends OneShotBehaviour{
		private Integer count=0;
		private Integer maxCount=3;

		public void action(){
			while(count<maxCount){
				((ContainerAgent)myAgent).releaseSomeContainer((SequentialBehaviour)parent);
				count++;
			}
		}
	}
	
	protected class checkForUtilization extends OneShotBehaviour{
		Boolean checkedAgain=false;
		public checkForUtilization(Boolean checkedAgain) {
			this.checkedAgain=checkedAgain;
		}
		public checkForUtilization() {
			this(false);
		}
		public void action(){
			SequentialBehaviour sb=((SequentialBehaviour) getParent());
			Behaviour b;
			if(!((ContainerAgent)myAgent).isBayMapFull()){
				b=new stowContainer();
				b.setDataStore(getDataStore());
				sb.addSubBehaviour(b);
				((ContainerAgent)myAgent).echoStatus("Baymap nicht voll");
			} else {
				if(checkedAgain){ //has been already checked, makeRoom failed, no chance
					b=new prepareFailure();
					b.setDataStore(getDataStore());
					sb.addSubBehaviour(b);
				} else { // try making some room
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
	
	protected class prepareFailure extends OneShotBehaviour{
		public void action() {
			DataStore ds=getDataStore();
			ACLMessage accept=(ACLMessage) ds.get(ACCEPT_PROPOSAL_KEY);
			ACLMessage fail = accept.createReply();	
			AnnounceLoadStatus loadStatus=getLoadStatusAnnouncement(null, "Fehler: BayMap voll und kann nicht gelehrt werden");
			fail.setPerformative(ACLMessage.FAILURE);
			((ContainerAgent)myAgent).fillMessage(fail,loadStatus);

			ds.put(REPLY_KEY, fail);
		}
	}
	
	protected class stowContainer extends SimpleBehaviour{
		private Boolean isDone=false;
		public void action(){
//			((ContainerAgent)myAgent).echoStatus("action");
			DataStore ds=getDataStore();
//			((ContainerAgent)myAgent).echoStatus(ds.toString());
			ACLMessage accept=(ACLMessage) ds.get(ACCEPT_PROPOSAL_KEY);
			ACLMessage inform = accept.createReply();
			Concept content= ((ContainerAgent)myAgent).extractAction(accept);
			TransportOrderChain acceptedTOC=((AcceptLoadOffer) content).getLoad_offer();

//			((ContainerAgent)myAgent).echoStatus("handleAcceptProposal - acceptPerformative: "+accept.getPerformative());
					
			if(!((ContainerAgent) myAgent).aquireContainer(acceptedTOC)){ 
				AnnounceLoadStatus loadStatus=getLoadStatusAnnouncement(acceptedTOC, "Containeraufnahme fehlgeschlagen");
				inform.setPerformative(ACLMessage.FAILURE);
				((ContainerAgent)myAgent).fillMessage(inform,loadStatus);
				ds.put(REPLY_KEY, inform);
				isDone=true;
				return;
			}
			
			((ContainerAgent)myAgent).echoStatus("Auftrag abgearbeitet. CID="+acceptedTOC.getTransports().getId());
			AnnounceLoadStatus loadStatus=getLoadStatusAnnouncement(acceptedTOC,"FINISHED");
			inform.setPerformative(ACLMessage.INFORM);
			((ContainerAgent)myAgent).fillMessage(inform,loadStatus);
			ds.put(REPLY_KEY, inform);
			isDone=true;
			return;
		}

		public boolean done() {
//			((ContainerAgent)myAgent).echoStatus("done");
			return isDone;
		}
	}
	
public AnnounceLoadStatus getLoadStatusAnnouncement(TransportOrderChain curTOC, String content){
	AnnounceLoadStatus loadStatus=new AnnounceLoadStatus();
	loadStatus.setLoad_status(content);
	loadStatus.setLoad_offer(curTOC);
	return loadStatus;
}
	/*
	protected ACLMessage handleAcceptProposal(ACLMessage cfp,ACLMessage propose, ACLMessage accept){
//		((ContainerAgent)myAgent).echoStatus("handleAcceptProposal - acceptPerformative: "+accept.getPerformative());

		ACLMessage inform = accept.createReply();
		inform.setPerformative(ACLMessage.INFORM);
		Concept content= ((ContainerAgent)myAgent).extractAction(accept);
		TransportOrderChain acceptedTOC=((AcceptLoadOffer) content).getLoad_offer();
		if(!((ContainerAgent)myAgent).removeFromQueue(acceptedTOC)){ 
			inform.setContent("handleAcceptProposal: Auftrag, auf den ich mich beworben habe nicht gefunden");
			inform.setPerformative(ACLMessage.FAILURE);
			return inform;
		}
		
		if(((ContainerAgent)myAgent).isBayMapFull()){
			((ContainerAgent)myAgent).echoStatus("Baymap voll");
			block();
			return null;
		}
		
		((ContainerAgent) myAgent).aquireContainer(acceptedTOC);
		
		((ContainerAgent)myAgent).echoStatus("Auftrag abgearbeitet");
		AnnounceLoadStatus loadStatus=new AnnounceLoadStatus();
		loadStatus.setLoad_status("FINISHED");
		loadStatus.setLoad_offer(acceptedTOC);
		((ContainerAgent)myAgent).fillMessage(inform,loadStatus);
		return inform;
	}
	*/
	
	protected void handleRejectProposal(ACLMessage cfp,ACLMessage propose, ACLMessage accept){
//		((ContainerAgent)myAgent).echoStatus("handleRejectProposal - acceptPerformative");
		Concept content= ((ContainerAgent)myAgent).extractAction(propose);
		TransportOrderChain acceptedTOC=((ProposeLoadOffer) content).getLoad_offer();
		if(!((ContainerAgent)myAgent).removeFromQueue(acceptedTOC)){ //wenn der untersuchte Container dem entspricht, für den sich beworben wurde
			((ContainerAgent)myAgent).echoStatus("handleRejectProposal: Auftrag, auf den ich mich beworben habe nicht gefunden");
		} else{
			((ContainerAgent)myAgent).echoStatus("abgelehnten Auftrag entfernt. CID="+acceptedTOC.getTransports().getId());
		}
	}
}