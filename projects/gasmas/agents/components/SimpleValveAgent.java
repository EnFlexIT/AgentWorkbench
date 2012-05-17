package gasmas.agents.components;

import gasmas.clustering.coalitions.PassiveNAResponderBehaviour;
import agentgui.simulationService.agents.SimulationAgent;

public class SimpleValveAgent extends SimulationAgent {

	private static final long serialVersionUID = -3041440524193040672L;

	@Override
	protected void setup() {
		super.setup();
		this.addBehaviour(new PassiveNAResponderBehaviour(this));
	}
}
