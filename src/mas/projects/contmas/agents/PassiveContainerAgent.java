package mas.projects.contmas.agents;

import mas.projects.contmas.ontology.PassiveContainerHolder;

public class PassiveContainerAgent extends ContainerAgent {

	public PassiveContainerAgent(String serviceType) {
		super(serviceType, new PassiveContainerHolder());
	}

}
