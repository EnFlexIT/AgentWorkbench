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
		if(false){
			reply.setPerformative(ACLMessage.REFUSE);
		}else{
			reply.setPerformative(ACLMessage.PROPOSE);
			Concept content;
			try {
			content = ((AgentAction) myAgent.getContentManager().extractContent(cfp));
	        if(content instanceof CallForProposalsOnLoadStage) {
	        	CallForProposalsOnLoadStage call=(CallForProposalsOnLoadStage) content;
	        	LoadList liste =call.getRequired_turnover_capacity();
	        	Iterator toc=liste.getConsists_of().getAllIs_linked_by();
	        	TransportOrder matchingOrder=null;

	        	while(toc.hasNext()){
	        		TransportOrder curTO=(TransportOrder) toc.next();
	        		ContainerHolder start=(ContainerHolder) curTO.getStarts_at();
	        		ContainerHolder end=(ContainerHolder) curTO.getEnds_at();
	        		//TODO den passenden ContainerHolder herausfinden, den spezifischsten, aber der auf operator und ausschreiber passt
	        		Class operator=((ContainerAgent) myAgent).ontologyRepresentation.getClass();
	        		System.out.println("operator: "+operator.getSimpleName());
	        		System.out.println("startat: "+start.getClass().getSimpleName());
	        		if(operator.getSimpleName()=="Crane" && start.getClass().getSimpleName()=="Ship"){
	        			matchingOrder=curTO;
	        		}
	        	}
				ProposeLoadOffer act=new ProposeLoadOffer();
				Random RandomGenerator=new Random(); 
				matchingOrder.setTakes(RandomGenerator.nextFloat());
				act.setLoad_offer(matchingOrder);
				myAgent.getContentManager().fillContent(cfp, act);
	        }
				
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		return reply;
	}
	protected ACLMessage handleAcceptProposal(ACLMessage cfp,ACLMessage propose, ACLMessage accept){
		ACLMessage inform = accept.createReply();
		inform.setPerformative(ACLMessage.INFORM);
		return inform;
	}
	
}
