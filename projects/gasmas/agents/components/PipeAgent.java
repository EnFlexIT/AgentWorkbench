package gasmas.agents.components;

import gasmas.clustering.coalitions.CoalitionPNCResponseBehaviour;
import agentgui.simulationService.agents.SimulationAgent;

public class PipeAgent extends SimulationAgent {

	private static final long serialVersionUID = 7003278414741221062L;

	@Override
	protected void setup() {
		addBehaviour(new CoalitionPNCResponseBehaviour(this));
	}
}
