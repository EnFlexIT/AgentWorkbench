package de.enflexit.jade.phonebook.behaviours;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class PhoneBookRegistrationInitiator extends AchieveREInitiator {
	
	private static final long serialVersionUID = -86449969548103113L;

	private static final long RETRY_TIMEOUT = 5000;
	
	private boolean retryOnFailure;
	private ACLMessage registrationMessage;

	/**
	 * Instantiates a new phone book registration initiator.
	 * @param agent the agent
	 * @param msg the registration message
	 * @param retryOnFailure specifies if the registration should be rescheduled if it fails.
	 */
	public PhoneBookRegistrationInitiator(Agent agent, ACLMessage msg, boolean retryOnFailure) {
		super(agent, msg);
		this.registrationMessage = msg;
		this.retryOnFailure = retryOnFailure;
	}
	
	/* (non-Javadoc)
	 * @see jade.proto.Initiator#handleFailure(jade.lang.acl.ACLMessage)
	 */
	@Override
	protected void handleFailure(ACLMessage failure) {
		// --- If configured, retry in case of failure --------------
		if (this.retryOnFailure==true) {
			this.myAgent.addBehaviour(new RetryRegistrationBehaviour(myAgent, RETRY_TIMEOUT));
		}
	}
	
	/**
	 * This behaviour is used to reschedule the registration in case of failure
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	private class RetryRegistrationBehaviour extends WakerBehaviour{

		private static final long serialVersionUID = -1677753694781743360L;

		public RetryRegistrationBehaviour(Agent a, long timeout) {
			super(a, timeout);
		}
		
		@Override
		protected void onWake() {
			this.myAgent.addBehaviour(new PhoneBookRegistrationInitiator(this.myAgent, PhoneBookRegistrationInitiator.this.registrationMessage, PhoneBookRegistrationInitiator.this.retryOnFailure));
		}
		
	}

}
