package gasmas.agents.components;

import gasmas.clustering.coalitions.CoalitionPNCResponseBehaviour;
import agentgui.simulationService.agents.SimulationAgent;

public class ExitAgent extends SimulationAgent {

	private static final long serialVersionUID = 5755894155609484866L;

	@Override
	protected void setup() {
		super.setup();
		this.addBehaviour(new CoalitionPNCResponseBehaviour(this));
	}
}
