package gasmas.clustering.coalitions;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ProposeInitiator;

public class CoalitionANCProposeBehaviour extends ProposeInitiator {

	private ACLMessage message;
	private CoalitionANCAuthorityBehaviour coalitionAuthorityBehaviour;

	private String networkComponentID;

	public CoalitionANCProposeBehaviour(CoalitionANCAuthorityBehaviour coalitionAuthorityBehaviour, Agent a, ACLMessage msg, String networkComponentID) {
		super(a, msg);
		message = msg;
		this.coalitionAuthorityBehaviour = coalitionAuthorityBehaviour;
		this.networkComponentID = networkComponentID;
	}

	@Override
	protected void handleAcceptProposal(ACLMessage accept_proposal) {
		coalitionAuthorityBehaviour.addAgree(networkComponentID);
	}

	@Override
	protected void handleRejectProposal(ACLMessage reject_proposal) {
	}

	@Override
	protected void handleNotUnderstood(ACLMessage notUnderstood) {
		reset(message);
	}

}
