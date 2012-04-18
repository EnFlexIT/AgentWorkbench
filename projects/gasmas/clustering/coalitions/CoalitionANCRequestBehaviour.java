package gasmas.clustering.coalitions;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class CoalitionANCRequestBehaviour extends AchieveREInitiator {

	private CoalitionANCAuthorityBehaviour coalitionAuthorityBehaviour;

	private String networkComponentID;

	public CoalitionANCRequestBehaviour(CoalitionANCAuthorityBehaviour coalitionAuthorityBehaviour, Agent a, ACLMessage msg, String networkComponentID) {
		super(a, msg);
		this.coalitionAuthorityBehaviour = coalitionAuthorityBehaviour;
		this.networkComponentID = networkComponentID;
	}

	@Override
	protected void handleAgree(ACLMessage agree) {
		coalitionAuthorityBehaviour.addAgree(networkComponentID);
	}

	@Override
	protected void handleRefuse(ACLMessage refuse) {
	}

}
