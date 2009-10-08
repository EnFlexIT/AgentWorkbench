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
		try {
			myAgent.getContentManager().fillContent(cfp, act);
			Vector<ACLMessage> messages = new Vector<ACLMessage>();
			cfp.setReplyByDate(new Date(System.currentTimeMillis() + 5000));
			messages.add(cfp);
			return messages;
		} catch (CodecException e) {
			((ContainerAgent)myAgent).echoStatus("announceLoadOrders - prepareCfps - fillContent - CodecException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			((ContainerAgent)myAgent).echoStatus("announceLoadOrders - prepareCfps - fillContent - OntologyException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected void handleAllResponses(Vector responses, Vector acceptances) {
		TransportOrder bestOffer=null;
		ACLMessage bestOfferMessage=null;
		for (Object message : responses) {
			ACLMessage propose = (ACLMessage) message;
			if (propose.getContent() != null) {
				Concept content;
				try {
					//System.out.println("Content: "+propose.getContent());
					content = ((AgentAction) myAgent.getContentManager().extractContent(propose));
					// content = ((AgentAction) myAgent.getContentManager().);
					if (content instanceof ProposeLoadOffer) {
						ProposeLoadOffer proposal = (ProposeLoadOffer) content;
						TransportOrder offer = proposal.getLoad_offer();
						if(bestOffer==null || offer.getTakes()<bestOffer.getTakes()){ //bisher beste Zeit
							bestOffer=offer;
							bestOfferMessage=propose;
						}


					}
				} catch (UngroundedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}// End if content !=null
			
		}
		ACLMessage accept=null;
		if(bestOffer!=null){
			accept = bestOfferMessage.createReply();
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

	protected void handleResultNotification(Vector resultNotifications) {
		System.out.println("Erfolgreich abgeladen");
	}
}
