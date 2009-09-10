package mas.projects.contmas.agents;

import mas.projects.contmas.ontology.ActiveContainerHolder;

public class ActiveContainerAgent extends ContainerAgent {

	public ActiveContainerAgent(String serviceType) {
		this(serviceType, new ActiveContainerHolder());
	}
	public ActiveContainerAgent(String serviceType,ActiveContainerHolder ontologyRepresentation) {
		super(serviceType, ontologyRepresentation);
	}
}
