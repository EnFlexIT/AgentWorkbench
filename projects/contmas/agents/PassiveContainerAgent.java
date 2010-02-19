package contmas.agents;

import contmas.ontology.ActiveContainerHolder;
import contmas.ontology.ContainerHolder;
import contmas.ontology.Designator;
import contmas.ontology.Domain;
import contmas.ontology.LoadList;
import contmas.ontology.PassiveContainerHolder;
import contmas.ontology.TransportOrder;
import contmas.ontology.TransportOrderChain;
import jade.util.leap.ArrayList;
import jade.util.leap.Iterator;
import jade.util.leap.List;

public class PassiveContainerAgent extends ContainerAgent {
	
	public PassiveContainerAgent(String serviceType) {
		this(serviceType, new PassiveContainerHolder());
	}

	public PassiveContainerAgent(String serviceType,PassiveContainerHolder ontologyRepresentation) {
		super(serviceType, ontologyRepresentation);
		ontologyRepresentation.setAdministers(new LoadList());
	}
}
