package mas.projects.contmas.agents;

import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;
import mas.projects.contmas.ontology.ActiveContainerHolder;
import mas.projects.contmas.ontology.CallForProposalsOnLoadStage;
import mas.projects.contmas.ontology.Container;
import mas.projects.contmas.ontology.ContainerHolder;
import mas.projects.contmas.ontology.Designator;
import mas.projects.contmas.ontology.Domain;
import mas.projects.contmas.ontology.LoadList;
import mas.projects.contmas.ontology.ProposeLoadOffer;
import mas.projects.contmas.ontology.TransportOrder;
import mas.projects.contmas.ontology.TransportOrderChain;

public class ActiveContainerAgent extends ContainerAgent {
	protected List contractors=new ArrayList();
	
	public ActiveContainerAgent(String serviceType) {
		this(serviceType, new ActiveContainerHolder());
	}
	
	public ActiveContainerAgent(String serviceType,ActiveContainerHolder ontologyRepresentation) {
		super(serviceType, ontologyRepresentation);
		ontologyRepresentation.setAdministers(new LoadList());
	}
	
	public void aquireContainer(TransportOrderChain targetContainer){
		super.aquireContainer(targetContainer);
		((ActiveContainerHolder)this.ontologyRepresentation).getAdministers().addConsists_of(targetContainer); //container auftragsbuch hinzufügen
	}

    public TransportOrder findMatchingOrder(TransportOrderChain haystack){
    	Iterator toc=haystack.getAllIs_linked_by();
    	TransportOrder matchingOrder=null;
		echoStatus("findMatchingOrder - jede order in der kette durchlaufen");

    	while(toc.hasNext()){
    		TransportOrder curTO=(TransportOrder) toc.next();
    		Iterator capabilities=((ActiveContainerHolder) ontologyRepresentation).getAllCapable_of();
    		Designator start=(Designator) curTO.getStarts_at();
    		Domain startHabitat=(Domain) start.getAbstract_designation();
    		while (capabilities.hasNext()) {
    			Domain capability = (Domain) capabilities.next();
    			if(startHabitat.getClass().getSimpleName().equals(capability.getClass().getSimpleName())){ //containeragent is able to handle orders in this start-habitat-domain
	    			matchingOrder=curTO;
	    			echoStatus("passt!");
	    			break;
				}
			}
    	}
    	return matchingOrder;
    }
    public TransportOrder findMatchingOutgoingOrder(TransportOrderChain haystack){
    	TransportOrder matchingOrder=null;
    	Iterator toc=haystack.getAllIs_linked_by();
    	while(toc.hasNext()){//jede order in der kette durchlaufen
    		TransportOrder curTO=(TransportOrder) toc.next();
    		Designator start=(Designator) curTO.getStarts_at();
    		if(start.getConcrete_designation().getName().equals(getAID().getName())){ 
    			matchingOrder=curTO;
    			break;
    		}
    	}
    	return matchingOrder;
    }
}