package mas.projects.contmas.agents;

import jade.util.leap.Iterator;
import mas.projects.contmas.ontology.ActiveContainerHolder;
import mas.projects.contmas.ontology.ContainerHolder;
import mas.projects.contmas.ontology.Domain;
import mas.projects.contmas.ontology.LoadList;
import mas.projects.contmas.ontology.PassiveContainerHolder;
import mas.projects.contmas.ontology.TransportOrder;
import mas.projects.contmas.ontology.TransportOrderChain;

public class PassiveContainerAgent extends ContainerAgent {

	public PassiveContainerAgent(String serviceType) {
		this(serviceType, new PassiveContainerHolder());
	}

	public PassiveContainerAgent(String serviceType,PassiveContainerHolder ontologyRepresentation) {
		super(serviceType, ontologyRepresentation);
		ontologyRepresentation.setAdministers(new LoadList());
	}
	
    public TransportOrder findMatchingOrder(TransportOrderChain haystack){
    	Iterator toc=haystack.getAllIs_linked_by();
    	TransportOrder matchingOrder=null;
		echoStatus("findMatchingOrder - jede order in der kette durchlaufen");

    	while(toc.hasNext()){//jede order in der kette durchlaufen
    		TransportOrder curTO=(TransportOrder) toc.next();
//    		echoStatus("curTO: "+curTO.toString());

    		Domain handlerHabitat=(Domain) ontologyRepresentation.getLives_in();
/*
    		ContainerHolder start=(ContainerHolder) curTO.getStarts_at();
    		Domain startHabitat=(Domain) start.getLives_in();
*/
    		ContainerHolder end=(ContainerHolder) curTO.getEnds_at();
    		Domain endHabitat=(Domain) end.getLives_in();
    		echoStatus("handlerHabitat: "+handlerHabitat.getClass().getSimpleName());
    		echoStatus("endHabitat: "+endHabitat.getClass().getSimpleName());

    		if(handlerHabitat.getClass().getSimpleName().equals(endHabitat.getClass().getSimpleName())){ //containeragent lives in the same domain as the target
    			matchingOrder=curTO;
    			echoStatus("passt!");
    			break;
    		}
    	}
    	return matchingOrder;
    }
	public void aquireContainer(TransportOrderChain targetContainer){
		super.aquireContainer(targetContainer);
		((PassiveContainerHolder)this.ontologyRepresentation).getAdministers().addConsists_of(targetContainer); //container ist in BayMap, auftragsbuch hinzufügen
	}
}
