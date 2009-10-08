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
import mas.projects.contmas.ontology.CallForProposalsOnLoadStage;
import mas.projects.contmas.ontology.LoadList;
import mas.projects.contmas.ontology.ProposeLoadOffer;
import mas.projects.contmas.ontology.TransportOrder;
import mas.projects.contmas.ontology.TransportOrderChain;

public class recieveLoadOrders extends ContractNetResponder{
	public recieveLoadOrders(Agent a, MessageTemplate mt) {
		super(a, mt);
		// TODO Auto-generated constructor stub
	}
	protected ACLMessage handleCfp(ACLMessage cfp){
		ACLMessage reply = cfp.createReply();
		Concept content;
		((ContainerAgent)myAgent).echoStatus("cfpEmpfangen");
		try {
			if(cfp.getContent()==null){
				reply.setPerformative(ACLMessage.FAILURE);
				((ContainerAgent)myAgent).echoStatus("noContent");
	    		return reply;
			}
			content = ((AgentAction) myAgent.getContentManager().extractContent(cfp));

	        if (content instanceof CallForProposalsOnLoadStage) {
				((ContainerAgent)myAgent).echoStatus("lengthOfQueue: "+((ContainerAgent) myAgent).lengthOfQueue+", loadOrderPostQueue.size(): "+((ContainerAgent) myAgent).loadOrdersProposedForQueue.size());

	        	if(((ContainerAgent) myAgent).loadOrdersProposedForQueue.size()>=((ContainerAgent) myAgent).lengthOfQueue){//schon auf genug Aufträge beworben
	        		((ContainerAgent)myAgent).echoStatus("schon genug Aufträge");
		        	reply.setPerformative(ACLMessage.REFUSE);
		    		return reply;

	        	} else {
					((ContainerAgent)myAgent).echoStatus("noch Kapazitäten vorhanden");

		        	reply.setPerformative(ACLMessage.PROPOSE);
	        		((ContainerAgent) myAgent).loadOrdersProposedForQueue.add(((CallForProposalsOnLoadStage)content).getRequired_turnover_capacity());
		        	//System.out.println("ist auch ein call for proposals");

		        	CallForProposalsOnLoadStage call=(CallForProposalsOnLoadStage) content;
		        	LoadList liste=call.getRequired_turnover_capacity();
		        	//System.out.println("LoadList besteht aus "+liste.getConsists_of().size()+" TransportOrderChains");

		        	Iterator allTocs=liste.getAllConsists_of();
		        	TransportOrder matchingOrder=((ContainerAgent) myAgent).findMatchingOrder((TransportOrderChain) allTocs.next());

		        	if(matchingOrder!=null){
						((ContainerAgent)myAgent).echoStatus("TransportOrder gefunden, die zu mir passt");

						ProposeLoadOffer act=(((ActiveContainerAgent) myAgent).GetLoadProposal(matchingOrder));
						reply.setPerformative(ACLMessage.PROPOSE);

						myAgent.getContentManager().fillContent(reply, act);
		        	} else {
						((ContainerAgent)myAgent).echoStatus("keine TransportOrder passt zu mir");
		        	}
	        	}


	        } else {
	        	((ContainerAgent)myAgent).echoStatus("unbekannter inhalt in CFP");
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
		((ContainerAgent)myAgent).echoStatus("Auftragsannahme empfangen");
		ACLMessage inform = accept.createReply();
		Concept content;
		try {
			content = ((AgentAction) myAgent.getContentManager().extractContent(cfp));
			LoadList requiredCapacity=((CallForProposalsOnLoadStage) content).getRequired_turnover_capacity();
			TransportOrderChain proposedTOC=(TransportOrderChain) requiredCapacity.getAllConsists_of().next(); 				//TODO Bisher: Variante nur einer pro, erweitern

			Iterator queue=((ContainerAgent) myAgent).loadOrdersProposedForQueue.iterator();
			
			while(queue.hasNext()){ //Aufträge durchlaufen, auf die beworben wurde, den richtigen bearbeiten
				((ContainerAgent)myAgent).echoStatus("Auftrag überprüfen");
				LoadList curList=(LoadList) queue.next();
				TransportOrderChain queuedTOC=(TransportOrderChain) curList.getAllConsists_of().next();				//TODO Bisher: Variante nur einer pro, erweitern
				if(proposedTOC.getTransports().getId().equals(queuedTOC.getTransports().getId())){ //wenn der untersuchte Container dem entspricht, für den sich beworben wurde
					queue.remove();
					((ContainerAgent)myAgent).echoStatus("Auftrag aus Bewerbungsliste entfernt, mit Bearbeitung beginnen");

					((ActiveContainerAgent) myAgent).aquireContainer(proposedTOC);
					
					((ContainerAgent)myAgent).echoStatus("Auftrag abgearbeitet");
					inform.setPerformative(ACLMessage.INFORM);
				}
				if(inform.getPerformative()!=ACLMessage.INFORM){
					inform.setPerformative(ACLMessage.FAILURE);
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
			Iterator queue=((ContainerAgent) myAgent).loadOrdersProposedForQueue.iterator();
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
