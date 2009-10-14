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

			if(!((ContainerAgent)myAgent).checkQueueCapacity()){//schon auf genug Auftr�ge beworben
				((ContainerAgent)myAgent).echoStatus("schon genug Auftr�ge");
				reply.setContent("schon genug Auftr�ge");
		    	reply.setPerformative(ACLMessage.REFUSE);
				return reply;

			} else if (!((ContainerAgent)myAgent).checkPlausibility(call)) {
				((ContainerAgent)myAgent).echoStatus("CFP unplausibel");
				reply.setContent("CFP unplausibel");
		    	reply.setPerformative(ACLMessage.REFUSE);
				return reply;					
			} 
			else { //noch Kapazit�ten vorhanden
				((ContainerAgent)myAgent).echoStatus("noch Kapazit�ten vorhanden");
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
		((ContainerAgent)myAgent).echoStatus("handleAcceptProposal - acceptPerformative: "+accept.getPerformative());

		Iterator queue=((ContainerAgent) myAgent).loadOrdersProposedForQueue.iterator();
		
		ACLMessage inform = accept.createReply();
		Concept content;
		content = ((ContainerAgent)myAgent).extractAction(cfp);

		LoadList requiredCapacity=((CallForProposalsOnLoadStage) content).getRequired_turnover_capacity();
		TransportOrderChain proposedTOC=(TransportOrderChain) requiredCapacity.getAllConsists_of().next();

		while(queue.hasNext()){ //Auftr�ge durchlaufen, auf die beworben wurde, den richtigen bearbeiten
			((ContainerAgent)myAgent).echoStatus("Queue �berpr�fen");
			TransportOrderChain queuedTOC=(TransportOrderChain)queue.next();
			if(proposedTOC.getTransports().getId().equals(queuedTOC.getTransports().getId())){ //wenn der untersuchte Container dem entspricht, f�r den sich beworben wurde
				queue.remove();
				((ContainerAgent)myAgent).echoStatus("Auftrag aus Bewerbungsliste entfernt, mit Bearbeitung beginnen");

				((ContainerAgent) myAgent).aquireContainer(proposedTOC);
				
				((ContainerAgent)myAgent).echoStatus("Auftrag abgearbeitet");
				AnnounceLoadStatus loadStatus=new AnnounceLoadStatus();
				loadStatus.setLoad_status("FINISHED");
				loadStatus.setLoad_offer(proposedTOC);
				((ContainerAgent)myAgent).fillMessage(inform,loadStatus);

				inform.setPerformative(ACLMessage.INFORM);
			}
			if(inform.getPerformative()!=ACLMessage.INFORM){
				inform.setContent("recieveLoadOrders: Auftrag, auf den ich mich beworben habe nicht gefunden");
				inform.setPerformative(ACLMessage.FAILURE);
			}
		}
		return inform;
	}
	protected void handleRejectProposal(ACLMessage cfp,ACLMessage propose, ACLMessage accept){
		((ContainerAgent)myAgent).echoStatus("handleRejectProposal - acceptPerformative");

		Concept content;

		content = ((ContainerAgent)myAgent).extractAction(cfp);

		LoadList requiredCapacity=((CallForProposalsOnLoadStage) content).getRequired_turnover_capacity();
		TransportOrderChain proposedTOC=(TransportOrderChain) requiredCapacity.getAllConsists_of().next();

		Iterator queue=((ContainerAgent) myAgent).loadOrdersProposedForQueue.iterator();
		while(queue.hasNext()){
			((ContainerAgent)myAgent).echoStatus("Queue �berpr�fen");

			TransportOrderChain queuedTOC=(TransportOrderChain) queue.next();

			if(proposedTOC.getTransports().getId().equals(queuedTOC.getTransports().getId())){
				((ContainerAgent)myAgent).echoStatus("Auftrag gefunden, entfernt");

				queue.remove();
			}
		}
	}
}