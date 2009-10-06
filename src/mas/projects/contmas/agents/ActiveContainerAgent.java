package mas.projects.contmas.agents;

import jade.util.leap.Iterator;
import mas.projects.contmas.ontology.ActiveContainerHolder;
import mas.projects.contmas.ontology.Container;
import mas.projects.contmas.ontology.ContainerHolder;
import mas.projects.contmas.ontology.Domain;
import mas.projects.contmas.ontology.LoadList;
import mas.projects.contmas.ontology.ProposeLoadOffer;
import mas.projects.contmas.ontology.TransportOrder;
import mas.projects.contmas.ontology.TransportOrderChain;

public class ActiveContainerAgent extends ContainerAgent {
	public LoadList commissions=new LoadList();

	public ActiveContainerAgent(String serviceType) {
		this(serviceType, new ActiveContainerHolder());
	}
	public ActiveContainerAgent(String serviceType,ActiveContainerHolder ontologyRepresentation) {
		super(serviceType, ontologyRepresentation);
	}
	
	public ProposeLoadOffer GetLoadProposal(TransportOrder call){
		ProposeLoadOffer act=new ProposeLoadOffer();
		call.setTakes(0);
		act.setLoad_offer(call);
		return act;
	}
	
	public void aquireContainer(Container targetContainer){
		
	}
	
    public TransportOrder findMatchingOrder(TransportOrderChain haystack){
    	Iterator toc=haystack.getAllIs_linked_by();
    	TransportOrder matchingOrder=null;
		System.out.println("findMatchingOrder - jede order in der kette durchlaufen");

    	while(toc.hasNext()){
    		TransportOrder curTO=(TransportOrder) toc.next();
    		System.out.println("curTO: "+curTO.toString());

    		Iterator capabilities=((ActiveContainerHolder) ontologyRepresentation).getAllCapable_of();
    		ContainerHolder start=(ContainerHolder) curTO.getStarts_at();
    		Domain startHabitat=(Domain) start.getLives_in();
    		while (capabilities.hasNext()) {
    			Domain capability = (Domain) capabilities.next();
        		System.out.println("capability: "+capability.toString());
        		if(startHabitat.getClass().getSimpleName().equals(capability.getClass().getSimpleName())){ //containeragent is able to handle orders in this start-habitat-domain
	    			matchingOrder=curTO;
	        		System.out.println("passt!");
	    			break;
				}
			}
    		ContainerHolder end=(ContainerHolder) curTO.getEnds_at();
    		//TODO den passenden ContainerHolder herausfinden, den spezifischsten, aber der auf operator und ausschreiber passt
    		/*
    		Class operator=ontologyRepresentation.getClass();
    		if(operator.getSimpleName().equals("Crane") && start.getClass().getSimpleName().equals("Ship")){ //TODO hardcoded
    			matchingOrder=curTO;
    		}
    		*/
    	}
    	return matchingOrder;
    }
}
