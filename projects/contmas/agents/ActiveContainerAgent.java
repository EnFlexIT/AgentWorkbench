package contmas.agents;

import contmas.ontology.ActiveContainerHolder;
import contmas.ontology.CallForProposalsOnLoadStage;
import contmas.ontology.Container;
import contmas.ontology.ContainerHolder;
import contmas.ontology.Designator;
import contmas.ontology.Domain;
import contmas.ontology.LoadList;
import contmas.ontology.ProposeLoadOffer;
import contmas.ontology.TransportOrder;
import contmas.ontology.TransportOrderChain;
import jade.core.AID;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

public class ActiveContainerAgent extends ContainerAgent {
	
	public ActiveContainerAgent(String serviceType) {
		this(serviceType, new ActiveContainerHolder());
	}
	
	public ActiveContainerAgent(String serviceType,ActiveContainerHolder ontologyRepresentation) {
		super(serviceType, ontologyRepresentation);
	}

	public Integer matchOrder(TransportOrder curTO){
		Integer endMatch=super.matchOrder(curTO); //standard-Match: AID und ziel ist genau lebensraum
		Integer startMatch=-1;

		Designator start=(Designator) curTO.getStarts_at();
		Domain startHabitat=(Domain) start.getAbstract_designation();
		Designator end=(Designator) curTO.getEnds_at();
		Domain endHabitat=(Domain) end.getAbstract_designation();
		Iterator capabilities=((ActiveContainerHolder) ontologyRepresentation).getAllCapable_of();
		while (capabilities.hasNext()) {
			Domain capability = (Domain) capabilities.next();
			if(startHabitat.getClass()==capability.getClass()){ //containeragent is able to handle orders in this start-habitat-domain
//    			echoStatus("start passt");
    			startMatch=1;
			}
			if(endMatch!=0 && endMatch!=1 && endHabitat.getClass()==capability.getClass()){ //containeragent is able to handle orders in this end-habitat-domain
//    			echoStatus("end passt (besser)");
				endMatch=1;
			}
		}
		if(startMatch>-1 && endMatch>-1){ //order matcht
			return startMatch+endMatch;
		}
		return -1; //order matcht nicht
	}

}