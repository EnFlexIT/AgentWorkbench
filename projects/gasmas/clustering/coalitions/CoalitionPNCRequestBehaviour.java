package gasmas.clustering.coalitions;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ProposeInitiator;

public class CoalitionPNCRequestBehaviour extends ProposeInitiator {

	private CoalitionPNCAuthorityBehaviour coalitionBehaviour;

	private String networkComponentID;

	public CoalitionPNCRequestBehaviour(CoalitionPNCAuthorityBehaviour coalitionBehaviour, Agent a, ACLMessage msg, String networkComponentID) {
		super(a, msg);
		this.coalitionBehaviour = coalitionBehaviour;
		this.networkComponentID = networkComponentID;
	}

	@Override
	protected void handleAcceptProposal(ACLMessage accept_proposal) {
		coalitionBehaviour.addAgree(networkComponentID);
	}

	@Override
	protected void handleRejectProposal(ACLMessage reject_proposal) {
	}

	protected void handleNotUnderstood(ACLMessage notUnderstood) {
	}
}
