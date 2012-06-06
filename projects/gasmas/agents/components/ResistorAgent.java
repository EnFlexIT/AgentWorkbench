package gasmas.agents.components;

import gasmas.clustering.coalitions.PassiveNAResponderBehaviour;
import agentgui.simulationService.agents.SimulationAgent;

public class ResistorAgent extends GenericNetworkAgent {

	private static final long serialVersionUID = 7003278414741221062L;

	@Override
	protected void setup() {
		super.setup();
//		this.addBehaviour(new PassiveNAResponderBehaviour(this));
	}
}
