package mas.projects.contmas.agents;

import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import jade.util.leap.Iterator;
import mas.projects.contmas.ontology.AcceptLoadOffer;
import mas.projects.contmas.ontology.AnnounceLoadStatus;
import mas.projects.contmas.ontology.CallForProposalsOnLoadStage;
import mas.projects.contmas.ontology.LoadList;
import mas.projects.contmas.ontology.ProposeLoadOffer;
import mas.projects.contmas.ontology.TransportOrder;
import mas.projects.contmas.ontology.TransportOrderChain;

public class recieveLoadOrders extends ContractNetResponder{
	public recieveLoadOrders(Agent a, MessageTemplate mt) {
		super(a, mt);
	}
	protected ACLMessage handleCfp(ACLMessage cfp){
		ACLMessage reply = cfp.createReply();
		Concept content;
		((ContainerAgent)myAgent).echoStatus("CFP empfangen");
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
			else { //noch Kapazitäten vorhanden
				//((ContainerAgent)myAgent).echoStatus("noch Kapazitäten vorhanden");
				ProposeLoadOffer act=((ContainerAgent) myAgent).GetLoadProposal(curTOC);
				if(act!=null){
					((ContainerAgent)myAgent).fillMessage(reply,act);
					reply.setPerformative(ACLMessage.PROPOSE);
				} else {
					((ContainerAgent)myAgent).echoStatus("keine TransportOrder passt zu mir");
					reply.setContent("keine TransportOrder passt zu mir");
		        	reply.setPerformative(ACLMessage.REFUSE);
				}
			}
		} else { //unbekannter inhalt in CFP
			((ContainerAgent)myAgent).echoStatus("unbekannter inhalt in CFP");
			reply.setContent("unbekannter inhalt in CFP");
			reply.setPerformative(ACLMessage.FAILURE);
			return reply;		
		}
		return reply;
	}
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
	protected void handleRejectProposal(ACLMessage cfp,ACLMessage propose, ACLMessage accept){
//		((ContainerAgent)myAgent).echoStatus("handleRejectProposal - acceptPerformative");
		Concept content= ((ContainerAgent)myAgent).extractAction(propose);
		TransportOrderChain acceptedTOC=((ProposeLoadOffer) content).getLoad_offer();
		if(!((ContainerAgent)myAgent).removeFromQueue(acceptedTOC)){ //wenn der untersuchte Container dem entspricht, für den sich beworben wurde
			((ContainerAgent)myAgent).echoStatus("handleRejectProposal: Auftrag, auf den ich mich beworben habe nicht gefunden");
		} else{
			((ContainerAgent)myAgent).echoStatus("abgelehnten Auftrag entfernt");
		}
	}
}