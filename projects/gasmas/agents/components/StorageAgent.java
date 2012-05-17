package gasmas.agents.components;

import gasmas.clustering.coalitions.PassiveNAResponderBehaviour;
import agentgui.simulationService.agents.SimulationAgent;

public class StorageAgent extends SimulationAgent {

	private static final long serialVersionUID = -5409238667267455376L;

	@Override
	protected void setup() {
		super.setup();
		this.addBehaviour(new PassiveNAResponderBehaviour(this));
	}
	
	
}
