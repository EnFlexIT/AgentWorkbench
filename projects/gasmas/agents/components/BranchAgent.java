package gasmas.agents.components;

import gasmas.clustering.coalitions.CoalitionPNCResponseBehaviour;
import agentgui.simulationService.agents.SimulationAgent;

public class BranchAgent extends SimulationAgent {

	private static final long serialVersionUID = -2040238832853890724L;

	@Override
	protected void setup() {
		addBehaviour(new CoalitionPNCResponseBehaviour(this));
	}
}
