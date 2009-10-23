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

	public Integer matchOrder(TransportOrder curTO){
		Integer endMatch=super.matchOrder(curTO);
		Integer startMatch=-1;

		Designator start=(Designator) curTO.getStarts_at();
		Domain startHabitat=(Domain) start.getAbstract_designation();
		Designator end=(Designator) curTO.getStarts_at();
		Domain endHabitat=(Domain) end.getAbstract_designation();
		Iterator capabilities=((ActiveContainerHolder) ontologyRepresentation).getAllCapable_of();
		while (capabilities.hasNext()) {
			Domain capability = (Domain) capabilities.next();
			if(startHabitat.getClass()==capability.getClass()){ //containeragent is able to handle orders in this start-habitat-domain
//    			echoStatus("start passt auch!");
    			startMatch=1;
			}
			if(endMatch!=0 && endMatch!=1 && endHabitat.getClass()==capability.getClass()){ //containeragent is able to handle orders in this end-habitat-domain
//    			echoStatus("end passt (besser)!");
				endMatch=1;
			}
		}
		if(startMatch>-1 && endMatch>-1){
			return startMatch+endMatch;
		}
		return -1;
	}
}