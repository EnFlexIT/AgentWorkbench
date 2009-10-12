package mas.projects.contmas.agents;

import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;
import mas.projects.contmas.ontology.ActiveContainerHolder;
import mas.projects.contmas.ontology.Container;
import mas.projects.contmas.ontology.ContainerHolder;
import mas.projects.contmas.ontology.Domain;
import mas.projects.contmas.ontology.LoadList;
import mas.projects.contmas.ontology.ProposeLoadOffer;
import mas.projects.contmas.ontology.TransportOrder;
import mas.projects.contmas.ontology.TransportOrderChain;

public class ActiveContainerAgent extends ContainerAgent {
	
	public ActiveContainerAgent(String serviceType) {
		this(serviceType, new ActiveContainerHolder());
	}
	
	public ActiveContainerAgent(String serviceType,ActiveContainerHolder ontologyRepresentation) {
		super(serviceType, ontologyRepresentation);
		ontologyRepresentation.setAdministers(new LoadList());
	}
	

	
	public void aquireContainer(TransportOrderChain targetContainer){
		super.aquireContainer(targetContainer);
		((ActiveContainerHolder)this.ontologyRepresentation).getAdministers().addConsists_of(targetContainer); //container ist in BayMap, auftragsbuch hinzufügen
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
    	}
    	return matchingOrder;
    }

}
