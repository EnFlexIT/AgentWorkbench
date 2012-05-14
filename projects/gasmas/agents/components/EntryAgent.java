package gasmas.agents.components;

import gasmas.clustering.coalitions.CoalitionPNCResponseBehaviour;
import agentgui.simulationService.agents.SimulationAgent;

public class EntryAgent extends SimulationAgent {

	private static final long serialVersionUID = 8261773171624817683L;

	@Override
	protected void setup() {
		super.setup();
		this.addBehaviour(new CoalitionPNCResponseBehaviour(this));
	}
	
	
}
