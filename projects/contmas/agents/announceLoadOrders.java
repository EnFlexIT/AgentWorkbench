package contmas.agents;

import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import jade.util.leap.List;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import contmas.ontology.AcceptLoadOffer;
import contmas.ontology.Administerd;
import contmas.ontology.AnnounceLoadStatus;
import contmas.ontology.Announced;
import contmas.ontology.CallForProposalsOnLoadStage;
import contmas.ontology.Failed;
import contmas.ontology.LoadList;
import contmas.ontology.ProposeLoadOffer;
import contmas.ontology.TransportOrder;
import contmas.ontology.TransportOrderChain;


public class announceLoadOrders extends ContractNetInitiator {
	private LoadList currentLoadList = null;
	private Behaviour masterBehaviour= null;

	public announceLoadOrders(Agent a, LoadList currentLoadList) {
		this(a, currentLoadList,null);
	}
	
	public announceLoadOrders(Agent a, LoadList currentLoadList, Behaviour masterBehaviour) {
		super(a, null);
		this.currentLoadList = currentLoadList;
		this.masterBehaviour = masterBehaviour;
	}

	protected Vector prepareCfps(ACLMessage cfp) {
		TransportOrderChain curTOC=(TransportOrderChain) currentLoadList.getConsists_of().iterator().next();
		if(!(((ContainerAgent)myAgent).changeTOCState(curTOC, new Announced()) instanceof Administerd)){
			((ContainerAgent)myAgent).echoStatus("FAILURE: Auftrag wird nicht ausgeschrieben werden, nicht administriert, wahrscheinlich schon failure.",curTOC);
			((ContainerAgent)myAgent).changeTOCState(curTOC, new Failed());
			return null;
		}
		((ContainerAgent)myAgent).echoStatus("Schreibe Auftrag aus.", curTOC );
		cfp = new ACLMessage(ACLMessage.CFP);
		cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		List contractorList = ((ContainerAgent) myAgent).determineContractors();
		if(contractorList==null || contractorList.isEmpty()){
			if(masterBehaviour!=null){
				((ContainerAgent)myAgent).echoStatus("MasterBehaviour wird neugestartet.");
				masterBehaviour.restart();
			}
			((ContainerAgent)myAgent).echoStatus("FAILURE: Keine Contractors mehr vorhanden. Ausschreibung nicht möglich.",curTOC);
			((ContainerAgent)myAgent).changeTOCState(curTOC, new Failed());
			return null;
		}
		Iterator allContractors = contractorList.iterator();

		while (allContractors.hasNext()) {
			cfp.addReceiver((AID) allContractors.next());
		}
		CallForProposalsOnLoadStage act = new CallForProposalsOnLoadStage();
		act.setRequired_turnover_capacity(this.currentLoadList);
		((ContainerAgent)myAgent).fillMessage(cfp, act);
		Vector<ACLMessage> messages = new Vector<ACLMessage>();
		cfp.setReplyByDate(new Date(System.currentTimeMillis() + 5000)); //500000
		messages.add(cfp);

//		((ContainerAgent)myAgent).echoStatus("Auftrag ausgeschrieben.",curTOC);
		return messages;
	}

	protected void handleAllResponses(Vector responses, Vector acceptances) {
		TransportOrder bestOffer=null;
		TransportOrderChain bestOfferToc=null;

		ACLMessage bestOfferMessage=null;
		for (Object message : responses) {
			ACLMessage propose = (ACLMessage) message;
			if (propose.getPerformative()==ACLMessage.REFUSE) {
				((ContainerAgent)myAgent).echoStatus("Ablehnung empfangen von "+propose.getSender().getLocalName()+": "+propose.getContent());
			}
			if (propose.getContent() != null && propose.getPerformative()==ACLMessage.PROPOSE) {
				Concept content;
				content = ((ContainerAgent) myAgent).extractAction(propose);
				if (content instanceof ProposeLoadOffer) {
					ProposeLoadOffer proposal = (ProposeLoadOffer) content;
					TransportOrder offer = ((ContainerAgent)myAgent).findMatchingOrder(proposal.getLoad_offer(),false);
					if(bestOffer==null || offer.getTakes()<bestOffer.getTakes()){ //bisher beste Zeit
						bestOffer=offer;
						bestOfferMessage=propose;
						bestOfferToc=proposal.getLoad_offer();
					}
				}
			} // End if content !=null
		}
		ACLMessage accept=null;
		TransportOrderChain curTOC=(TransportOrderChain) currentLoadList.getConsists_of().iterator().next();

		if(bestOffer==null){ //Abnehmer momentan alle beschäftigt
			((ContainerAgent)myAgent).echoStatus("FAILURE: Nur Ablehnungen empfangen, Abbruch.");
			((ContainerAgent)myAgent).changeTOCState(curTOC, new Failed());
			if(masterBehaviour!=null){
				((ContainerAgent)myAgent).echoStatus("REFUSE: MasterBehaviour wird neugestartet.");
				masterBehaviour.restart();
			}
			this.reset();
			return;

			/* Alt: warten, benötigt aber weiteren zwischenspeicher oder so
			if(((ContainerAgent)myAgent).isInFailedQueue(curTOC)){
				if(masterBehaviour!=null){
					((ContainerAgent)myAgent).echoStatus("REFUSE: MasterBehaviour wird neugestartet.");
					masterBehaviour.restart();
				}
				return;
			} else {
				((ContainerAgent)myAgent).echoStatus("Nur Ablehnungen empfangen, versuche es nochmal");
				((ContainerAgent)myAgent).doWait(1000); //kurz warten, vielleicht beruhigt sich ja die Lage
				((ContainerAgent)myAgent).changeTOCState(curTOC, new Failed());
				this.reset();
				return;	
			}
			*/
		}
		if(bestOffer!=null){
			accept = bestOfferMessage.createReply();
			AcceptLoadOffer act = new AcceptLoadOffer();
			act.setLoad_offer(bestOfferToc);
			((ContainerAgent)myAgent).fillMessage(accept, act);
			accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
			acceptances.add(accept);
		}
		for (Object message : responses) {
			ACLMessage propose = (ACLMessage) message;
			if (propose != bestOfferMessage && propose.getPerformative()==ACLMessage.PROPOSE) {
				accept = propose.createReply();
				accept.setPerformative(ACLMessage.REJECT_PROPOSAL);
				acceptances.add(accept);
			}
		}
	}

	protected void handleAllResultNotifications(Vector resultNotifications) {
		for (Object message : resultNotifications) {
			ACLMessage notification = (ACLMessage) message;
			AgentAction content = ((ContainerAgent) myAgent).extractAction(notification);
			if (content instanceof AnnounceLoadStatus) {
				AnnounceLoadStatus loadStatus=(AnnounceLoadStatus) content;
				TransportOrderChain load_offer=loadStatus.getLoad_offer();
				if(notification.getPerformative()==ACLMessage.INFORM && loadStatus.getLoad_status().equals("FINISHED")){
//					((ContainerAgent)myAgent).echoStatus("AnnounceLoadStatus FINISHED empfangen, bearbeiten");
					if(((ContainerAgent)myAgent).removeContainerFromBayMap(load_offer)){
						((ContainerAgent)myAgent).changeTOCState(load_offer, null, true);
						((ContainerAgent)myAgent).echoStatus("Erfolgreich losgeworden (Meldung+BayMap-Entfernung).",load_offer);
						if(masterBehaviour!=null){
							((ContainerAgent)myAgent).echoStatus("INFORM: MasterBehaviour wird neugestartet.");
							masterBehaviour.restart();
						}
					}
				} else if (notification.getPerformative()==ACLMessage.FAILURE) { // && loadStatus.getLoad_status().substring(0, 4).equals("ERROR")) {
					((ContainerAgent)myAgent).removeFromContractors(notification.getSender());
					((ContainerAgent)myAgent).changeTOCState(load_offer, new Failed());
					((ContainerAgent)myAgent).echoStatus("Containerabgabe fehlgeschlagen. "+loadStatus.getLoad_status(),load_offer);
					if(masterBehaviour!=null){
						((ContainerAgent)myAgent).echoStatus("FAILURE: MasterBehaviour wird neugestartet.");
						masterBehaviour.restart();
					}
				}					
			} 
		}
	}
	
	protected void handleOutOfSequence(ACLMessage msg) {
		((ContainerAgent)myAgent).echoStatus("ERROR: Unerwartete Nachricht empfangen: "+msg.getPerformative());
	}
}
