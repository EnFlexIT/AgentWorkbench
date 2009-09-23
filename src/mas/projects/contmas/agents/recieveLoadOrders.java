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
		try {
			if(cfp.getContent()==null){
				reply.setPerformative(ACLMessage.FAILURE);
	    		return reply;
			}
			content = ((AgentAction) myAgent.getContentManager().extractContent(cfp));

	        if (content instanceof CallForProposalsOnLoadStage) {
	        	if(((CraneAgent) myAgent).loadOrderPostQueue.size()>=((CraneAgent) myAgent).lengthOfQueue){//schon genug Aufträge
	        		System.out.println("schon genug Aufträge");
		        	reply.setPerformative(ACLMessage.REFUSE);
		    		return reply;

	        	} else { 
		        	reply.setPerformative(ACLMessage.PROPOSE);
	        		((CraneAgent) myAgent).loadOrderPostQueue.add(((CallForProposalsOnLoadStage)content).getRequired_turnover_capacity());
		        	//System.out.println("ist auch ein call for proposals");

		        	CallForProposalsOnLoadStage call=(CallForProposalsOnLoadStage) content;
		        	LoadList liste=call.getRequired_turnover_capacity();
		        	//System.out.println("LoadList besteht aus "+liste.getConsists_of().size()+" TransportOrderChains");

		        	Iterator allTocs=liste.getAllConsists_of();
		        	Iterator toc=((TransportOrderChain) allTocs.next()).getAllIs_linked_by();
		        	TransportOrder matchingOrder=null;

		        	while(toc.hasNext()){
						//System.out.println("ein chain-link weiter");

		        		TransportOrder curTO=(TransportOrder) toc.next();
		        		ContainerHolder start=(ContainerHolder) curTO.getStarts_at();
		        		ContainerHolder end=(ContainerHolder) curTO.getEnds_at();
		        		//TODO den passenden ContainerHolder herausfinden, den spezifischsten, aber der auf operator und ausschreiber passt
		        		Class operator=((ContainerAgent) myAgent).ontologyRepresentation.getClass();
		        		//System.out.println("operator: "+operator.getSimpleName());
		        		//System.out.println("startat: "+start.getClass().getSimpleName());
		        		if(operator.getSimpleName().equals("Crane") && start.getClass().getSimpleName().equals("Ship")){
							//System.out.println("Start und Ende passen");
		        			matchingOrder=curTO;
		        		}
		        	}
		        	if(matchingOrder!=null){
						//System.out.println("passende TransportOrder gefunden");

						ProposeLoadOffer act=new ProposeLoadOffer();
						Random RandomGenerator=new Random(); 
						matchingOrder.setTakes(RandomGenerator.nextFloat());
						act.setLoad_offer(matchingOrder);
						reply.setPerformative(ACLMessage.PROPOSE);

						myAgent.getContentManager().fillContent(reply, act);
		        	}
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
		return reply;
	}
	protected ACLMessage handleAcceptProposal(ACLMessage cfp,ACLMessage propose, ACLMessage accept){
		ACLMessage inform = accept.createReply();
		inform.setPerformative(ACLMessage.INFORM);
		Concept content;
		try {
			content = ((AgentAction) myAgent.getContentManager().extractContent(cfp));
			System.out.println("Vor entfernen"+((CraneAgent) myAgent).loadOrderPostQueue.size());
			Iterator queue=((CraneAgent) myAgent).loadOrderPostQueue.iterator();
			while(queue.hasNext()){
				LoadList curList=(LoadList) queue.next();
//				curList.getAllConsists_of().next()
				if(((CallForProposalsOnLoadStage) content).getRequired_turnover_capacity()==curList){
					queue.remove();
//					System.out.println(queue.);

				}
			}
			System.out.println("Nach entfernen"+((CraneAgent) myAgent).loadOrderPostQueue.size());
			System.out.println("Auftrag abgearbeitet, auswarteschlage entfernen");
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
	
}
