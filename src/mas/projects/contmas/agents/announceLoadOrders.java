package mas.projects.contmas.agents;

import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import jade.util.leap.List;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import mas.projects.contmas.ontology.AcceptLoadOffer;
import mas.projects.contmas.ontology.AnnounceLoadStatus;
import mas.projects.contmas.ontology.CallForProposalsOnLoadStage;
import mas.projects.contmas.ontology.LoadList;
import mas.projects.contmas.ontology.ProposeLoadOffer;
import mas.projects.contmas.ontology.TransportOrder;
import mas.projects.contmas.ontology.TransportOrderChain;

public class announceLoadOrders extends ContractNetInitiator {
	private LoadList currentLoadList = null;

	public announceLoadOrders(Agent a, LoadList currentLoadList) {
		super(a, null);
		this.currentLoadList = currentLoadList;
	}

	protected Vector prepareCfps(ACLMessage cfp) {
		cfp = new ContainerMessage(ACLMessage.CFP);
		cfp.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
		List contractorList = ((ContainerAgent) myAgent).determineContractors();
		Iterator allContractors = contractorList.iterator();
		while (allContractors.hasNext()) {
			cfp.addReceiver((AID) allContractors.next());
		}
		CallForProposalsOnLoadStage act = new CallForProposalsOnLoadStage();
		act.setRequired_turnover_capacity(this.currentLoadList);
			((ContainerAgent)myAgent).fillMessage(cfp, act);
			Vector<ACLMessage> messages = new Vector<ACLMessage>();
			cfp.setReplyByDate(new Date(System.currentTimeMillis() + 5000));
			messages.add(cfp);
			return messages;
	}

	protected void handleAllResponses(Vector responses, Vector acceptances) {
		TransportOrder bestOffer=null;
		TransportOrderChain bestOfferToc=null;

		ACLMessage bestOfferMessage=null;
		for (Object message : responses) {
			ACLMessage propose = (ACLMessage) message;
			if (propose.getContent() != null) {
				Concept content;
					//System.out.println("Content: "+propose.getContent());
					content = ((ContainerAgent) myAgent).extractAction(propose);
					// content = ((AgentAction) myAgent.getContentManager().);
					if (content instanceof ProposeLoadOffer) {
						ProposeLoadOffer proposal = (ProposeLoadOffer) content;
						TransportOrder offer = ((ContainerAgent)myAgent).findMatchingOutgoingOrder(proposal.getLoad_offer());
						if(bestOffer==null || offer.getTakes()<bestOffer.getTakes()){ //bisher beste Zeit
							bestOffer=offer;
							bestOfferMessage=propose;
							bestOfferToc=proposal.getLoad_offer();
						}
					}



			}// End if content !=null
			
		}
		ACLMessage accept=null;
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
					if(loadStatus.getLoad_status().equals("FINISHED")){
						((ContainerAgent)myAgent).echoStatus("AnnounceLoadStatus FINISHED empfangen, bearbeiten");
						if(((ContainerAgent)myAgent).removeContainerFromBayMap(loadStatus.getLoad_offer())){
							((ContainerAgent)myAgent).echoStatus("Erfolgreich abgeladen");
						}
					}
				}
		}
	}
}
