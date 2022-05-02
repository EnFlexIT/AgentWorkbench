package de.enflexit.jade.phonebook.behaviours;

import java.io.IOException;

import de.enflexit.jade.phonebook.AbstractPhoneBookEntry;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class PhoneBookRegistrationInitiatorNonProtocol<T extends AbstractPhoneBookEntry> extends OneShotBehaviour {

	private static final long serialVersionUID = -3787913971076112502L;
	
	private static final long MAXIMUM_WAIT = 5000;
	private static final long RETRY_TIME = 5000;
	
	ACLMessage registrationMessage;
	
	private boolean retryOnFailure;
	
	/**
	 * Instantiates a new phone book registration initiator.
	 * @param phoneBookMaintainer the phone book maintainer
	 * @param myPhoneBookEntry the my phone book entry
	 */
	public PhoneBookRegistrationInitiatorNonProtocol(AID phoneBookMaintainer, T myPhoneBookEntry, boolean retryOnFailure) {
		this.retryOnFailure = retryOnFailure;
		this.registrationMessage = this.createRegistraitonMessage(phoneBookMaintainer, myPhoneBookEntry);
	}
	
	
	/**
	 * Instantiates a new phone book registration initiator.
	 * @param registrationMessage the registration message
	 * @param retryOnFailure the retry on failure
	 */
	private PhoneBookRegistrationInitiatorNonProtocol(ACLMessage registrationMessage, boolean retryOnFailure) {
		this.registrationMessage = registrationMessage;
		this.retryOnFailure = retryOnFailure;
	}


	private ACLMessage createRegistraitonMessage(AID phoneBookMaintainer, T myPhoneBookEntry) {
		ACLMessage message = null;
		try {
			message = new ACLMessage(ACLMessage.REQUEST);
			message.addReceiver(phoneBookMaintainer);
			message.setConversationId(ConversationID.PHONEBOOK_REGISTRATION.toString());
			message.setContentObject(myPhoneBookEntry);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action() {
		this.myAgent.send(this.registrationMessage);
		
		if (this.retryOnFailure==true) {
			MessageTemplate mt = MessageTemplate.MatchConversationId(ConversationID.PHONEBOOK_REGISTRATION.toString());
			ACLMessage reply = this.myAgent.blockingReceive(mt, MAXIMUM_WAIT);
			
			if (reply==null || reply.getPerformative()!=ACLMessage.CONFIRM) {
				this.myAgent.addBehaviour(new RetryRegistrationBehaviour(this.myAgent, RETRY_TIME));
			}
		}
	}
	
	private class RetryRegistrationBehaviour extends WakerBehaviour {
		
		private static final long serialVersionUID = -1677753694781743360L;

		public RetryRegistrationBehaviour(Agent agent, long timeout) {
			super(agent, timeout);
		}
		
		@Override
		protected void onWake() {
			this.myAgent.addBehaviour(new PhoneBookRegistrationInitiatorNonProtocol<>(PhoneBookRegistrationInitiatorNonProtocol.this.registrationMessage, retryOnFailure));
		}
		
	}
	
}
