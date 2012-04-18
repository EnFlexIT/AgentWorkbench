package gasmas.clustering.coalitions;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class CoalitionPNCRequestBehaviour extends AchieveREInitiator {

	private CoalitionPNCAuthorityBehaviour coalitionBehaviour;

	private String networkComponentID;

	public CoalitionPNCRequestBehaviour(CoalitionPNCAuthorityBehaviour coalitionBehaviour, Agent a, ACLMessage msg, String networkComponentID) {
		super(a, msg);
		this.coalitionBehaviour = coalitionBehaviour;
		this.networkComponentID = networkComponentID;
	}

	@Override
	protected void handleAgree(ACLMessage agree) {
		coalitionBehaviour.addAgree(networkComponentID);
	}

	@Override
	protected void handleRefuse(ACLMessage refuse) {
	}

}
