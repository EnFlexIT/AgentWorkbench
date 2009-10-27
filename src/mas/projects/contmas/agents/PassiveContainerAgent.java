package mas.projects.contmas.agents;

import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;
import mas.projects.contmas.ontology.ActiveContainerHolder;
import mas.projects.contmas.ontology.ContainerHolder;
import mas.projects.contmas.ontology.Designator;
import mas.projects.contmas.ontology.Domain;
import mas.projects.contmas.ontology.LoadList;
import mas.projects.contmas.ontology.PassiveContainerHolder;
import mas.projects.contmas.ontology.TransportOrder;
import mas.projects.contmas.ontology.TransportOrderChain;

public class PassiveContainerAgent extends ContainerAgent {
	protected List contractors=new ArrayList();
	
	public PassiveContainerAgent(String serviceType) {
		this(serviceType, new PassiveContainerHolder());
	}

	public PassiveContainerAgent(String serviceType,PassiveContainerHolder ontologyRepresentation) {
		super(serviceType, ontologyRepresentation);
		ontologyRepresentation.setAdministers(new LoadList());
	}
}
