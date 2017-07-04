package gasmas.clustering.coalitions;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ProposeInitiator;

public class ActiveNAProposeBehaviour extends ProposeInitiator {

	private ACLMessage message;
	private ActiveNAAuthorityBehaviour coalitionAuthorityBehaviour;


	public ActiveNAProposeBehaviour(ActiveNAAuthorityBehaviour coalitionAuthorityBehaviour, Agent a, ACLMessage msg) {
		super(a, msg);
		message = msg;
		this.coalitionAuthorityBehaviour = coalitionAuthorityBehaviour;
	}

	@Override
	protected void handleAcceptProposal(ACLMessage accept_proposal) {
		if (coalitionAuthorityBehaviour != null) {
			coalitionAuthorityBehaviour.addAgree(accept_proposal.getSender().getLocalName());
		}
	}

	@Override
	protected void handleRejectProposal(ACLMessage reject_proposal) {
		// if rejected wait: there will be no coalition, so wait for better suggestion
	}

	@Override
	protected void handleNotUnderstood(ACLMessage notUnderstood) {
		reset(message);
	}

}
