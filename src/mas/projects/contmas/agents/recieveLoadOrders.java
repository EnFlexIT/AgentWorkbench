package mas.projects.contmas.agents;

import java.sql.PreparedStatement;
import java.util.Random;

import mas.projects.contmas.ontology.CallForProposalsOnLoadStage;
import mas.projects.contmas.ontology.ContainerHolder;
import mas.projects.contmas.ontology.LoadList;
import mas.projects.contmas.ontology.ProposeLoadOffer;
import mas.projects.contmas.ontology.ProvideCraneList;
import mas.projects.contmas.ontology.Ship;
import mas.projects.contmas.ontology.TransportOrder;
import mas.projects.contmas.ontology.TransportOrderChain;

import mas.projects.contmas.ontology.CallForProposalsOnLoadStage;
import mas.projects.contmas.ontology.ProvidePopulatedBayMap;

import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import jade.util.leap.Iterator;
import jade.util.leap.List;

public class recieveLoadOrders extends ContractNetResponder{
	public recieveLoadOrders(Agent a, MessageTemplate mt) {
		super(a, mt);
		// TODO Auto-generated constructor stub
	}
	protected ACLMessage handleCfp(ACLMessage cfp){
		ACLMessage reply = cfp.createReply();
		Concept content;
		System.out.println(myAgent.getAID().getLocalName()+": cfpEmpfangen");
		try {
			if(cfp.getContent()==null){
				reply.setPerformative(ACLMessage.FAILURE);
				System.out.println(myAgent.getAID().getLocalName()+": noContent");
	    		return reply;
			}
			content = ((AgentAction) myAgent.getContentManager().extractContent(cfp));

	        if (content instanceof CallForProposalsOnLoadStage) {
				System.out.println(myAgent.getAID().getLocalName()+": lengthOfQueue: "+((CraneAgent) myAgent).lengthOfQueue+", loadOrderPostQueue.size(): "+((CraneAgent) myAgent).loadOrderPostQueue.size());

	        	if(((CraneAgent) myAgent).loadOrderPostQueue.size()>=((CraneAgent) myAgent).lengthOfQueue){//schon genug Aufträge
					System.out.println(myAgent.getAID().getLocalName()+": schon genug Aufträge");
		        	reply.setPerformative(ACLMessage.REFUSE);
		    		return reply;

	        	} else {
					System.out.println(myAgent.getAID().getLocalName()+": noch Kapazitäten vorhanden");

		        	reply.setPerformative(ACLMessage.PROPOSE);
	        		((CraneAgent) myAgent).loadOrderPostQueue.add(((CallForProposalsOnLoadStage)content).getRequired_turnover_capacity());
		        	//System.out.println("ist auch ein call for proposals");

		        	CallForProposalsOnLoadStage call=(CallForProposalsOnLoadStage) content;
		        	LoadList liste=call.getRequired_turnover_capacity();
		        	//System.out.println("LoadList besteht aus "+liste.getConsists_of().size()+" TransportOrderChains");

		        	Iterator allTocs=liste.getAllConsists_of();
		        	TransportOrder matchingOrder=((ContainerAgent) myAgent).findMatchingOrder((TransportOrderChain) allTocs.next());

		        	if(matchingOrder!=null){
						System.out.println(myAgent.getAID().getLocalName()+": TransportOrder gefunden, die zu mir passt");

						ProposeLoadOffer act=(((ActiveContainerAgent) myAgent).GetLoadProposal(matchingOrder));
						reply.setPerformative(ACLMessage.PROPOSE);

						myAgent.getContentManager().fillContent(reply, act);
		        	} else {
						System.out.println(myAgent.getAID().getLocalName()+": keine TransportOrder passt zu mir");
		        	}
	        	}


	        } else {
	        	System.out.println(myAgent.getAID().getLocalName()+": unbekannter inhalt in CFP");
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
		return reply;
	}
	protected ACLMessage handleAcceptProposal(ACLMessage cfp,ACLMessage propose, ACLMessage accept){
		System.out.println(myAgent.getAID().getLocalName()+": Auftragsannahme empfangen");
		ACLMessage inform = accept.createReply();
		inform.setPerformative(ACLMessage.INFORM);
		Concept content;
		try {
			content = ((AgentAction) myAgent.getContentManager().extractContent(cfp));
			Iterator queue=((CraneAgent) myAgent).loadOrderPostQueue.iterator();
			System.out.println(myAgent.getAID().getLocalName()+": Auftrag abgearbeitet");

			while(queue.hasNext()){
				System.out.println(myAgent.getAID().getLocalName()+": Auftrag überprüfen");
				LoadList curList=(LoadList) queue.next();
				LoadList requiredCapacity=((CallForProposalsOnLoadStage) content).getRequired_turnover_capacity();
				TransportOrderChain proposedTOC=(TransportOrderChain) requiredCapacity.getAllConsists_of().next();
				TransportOrderChain queuedTOC=(TransportOrderChain) curList.getAllConsists_of().next();
				if(proposedTOC.getTransports().getId().equals(queuedTOC.getTransports().getId())){
					((ActiveContainerAgent) myAgent).aquireContainer(proposedTOC.getTransports());
					LoadList commissions=new LoadList();
					commissions.addConsists_of(proposedTOC);
					((ActiveContainerAgent) myAgent).commissions=commissions;

					queue.remove();
					System.out.println(myAgent.getAID().getLocalName()+": Aus Warteschlage entfernt");
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
		return inform;
	}
	protected void handleRejectProposal(ACLMessage cfp,ACLMessage propose, ACLMessage accept){
		Concept content;
		try {
			content = ((AgentAction) myAgent.getContentManager().extractContent(cfp));
			Iterator queue=((CraneAgent) myAgent).loadOrderPostQueue.iterator();
			while(queue.hasNext()){
				LoadList curList=(LoadList) queue.next();
				LoadList requiredCapacity=((CallForProposalsOnLoadStage) content).getRequired_turnover_capacity();
				TransportOrderChain proposedTOC=(TransportOrderChain) requiredCapacity.getAllConsists_of().next();
				TransportOrderChain queuedTOC=(TransportOrderChain) curList.getAllConsists_of().next();
				if(proposedTOC.getTransports().getId().equals(queuedTOC.getTransports().getId())){
					queue.remove();
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
	}
}
