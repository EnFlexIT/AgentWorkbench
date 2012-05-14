package gasmas.clustering.coalitions;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;

public class CoalitionANCWakerBehaviour extends WakerBehaviour {

	private CoalitionBehaviour coalitionBehaviour;

	private String nameOfAuthority;

	public CoalitionANCWakerBehaviour(Agent a, long timeout, CoalitionBehaviour coalitionBehaviour, String nameOfAuthority) {
		super(a, timeout);
		this.coalitionBehaviour = coalitionBehaviour;
		this.nameOfAuthority = nameOfAuthority;
	}
	
	@Override
	protected void onWake() {
		coalitionBehaviour.informCoalitionAuthority(nameOfAuthority);
	}
}
