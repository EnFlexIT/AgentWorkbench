package gasmas.agents.components;

import gasmas.clustering.coalitions.PassiveNAResponderBehaviour;
import agentgui.simulationService.agents.SimulationAgent;

public class BranchAgent extends GenericNetworkAgent {

	private static final long serialVersionUID = -2040238832853890724L;


	@Override
	protected void setup() {
		super.setup();
//		this.addBehaviour(new PassiveNAResponderBehaviour(this));
	}
}
