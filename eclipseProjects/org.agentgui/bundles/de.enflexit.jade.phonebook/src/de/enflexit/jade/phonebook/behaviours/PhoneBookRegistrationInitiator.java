package de.enflexit.jade.phonebook.behaviours;

import java.io.IOException;

import de.enflexit.jade.phonebook.AbstractPhoneBookEntry;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SimpleAchieveREInitiator;

/**
 * This class implements the initiator role of the FIPA Request protocol for the phone book registration.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class PhoneBookRegistrationInitiator extends SimpleAchieveREInitiator {

	private static final long serialVersionUID = -6099020571543668933L;
	
	private AbstractPhoneBookEntry myPhoneBookEntry;
	private AID phoneBookMaintainer;
	
	private boolean retryOnFailure;
	private IncreasingRetryIntervalsHelper intervalsHelper;
	
	/**
	 * Instantiates a new phone book registration initiator.
	 * @param agent the agent
	 * @param myPhoneBookEntry the registering agent's phone book entry
	 * @param phoneBookMaintainer the phonebook maintainer's AID
	 */
	public PhoneBookRegistrationInitiator(Agent agent, AbstractPhoneBookEntry myPhoneBookEntry, AID phoneBookMaintainer, boolean retryOnFailure) {
		super(agent, createRequestMessage(myPhoneBookEntry, phoneBookMaintainer));
		this.myPhoneBookEntry = myPhoneBookEntry;
		this.phoneBookMaintainer = phoneBookMaintainer;
		this.retryOnFailure = retryOnFailure;
	}
	
	/**
	 * Creates the initial request message.
	 * @param myPhoneBookEntry the registering agent's phone book entry
	 * @param phonebookMaintainer the phonebook maintainer's AID
	 * @return the ACL message
	 */
	private static ACLMessage createRequestMessage(AbstractPhoneBookEntry myPhoneBookEntry, AID phoneBookMaintainer) {
		ACLMessage requestMessage = new ACLMessage(ACLMessage.REQUEST);
		requestMessage.addReceiver(phoneBookMaintainer);
		requestMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		requestMessage.setConversationId(ConversationID.PHONEBOOK_REGISTRATION.toString());
		try {
			requestMessage.setContentObject(myPhoneBookEntry);
		} catch (IOException e) {
			System.out.println("[" + PhoneBookRegistrationInitiator.class.getSimpleName() + "] Error setting content object for the reqistration request of " + myPhoneBookEntry.getUniqueIdentifier());
			e.printStackTrace();
		}
		return requestMessage;
	}
	
	/* (non-Javadoc)
	 * @see jade.proto.SimpleAchieveREInitiator#handleFailure(jade.lang.acl.ACLMessage)
	 */
	@Override
	protected void handleFailure(ACLMessage msg) {
		System.out.println("[" + this.myAgent.getLocalName() + "] Registration failed");
		if (this.retryOnFailure==true) {
			this.myAgent.addBehaviour(new RetryOnFailureBehaviour(this.myAgent, this.getIntervalsHelper()));
		}
	}
	
	/**
	 * Gets the intervals helper.
	 * @return the intervals helper
	 */
	private IncreasingRetryIntervalsHelper getIntervalsHelper() {
		if (intervalsHelper==null) {
			intervalsHelper = new IncreasingRetryIntervalsHelper();
		}
		return intervalsHelper;
	}

	/**
	 * Sets the intervals helper.
	 * @param intervalsHelper the new intervals helper
	 */
	private void setIntervalsHelper(IncreasingRetryIntervalsHelper intervalsHelper) {
		this.intervalsHelper = intervalsHelper;
	}
	
	/**
	 * This class implements a WakerBehaviour that reschedules the registration after a fixed timeout. 
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	private class RetryOnFailureBehaviour extends WakerBehaviour{

		private static final long serialVersionUID = 6546607375571793796L;
		private IncreasingRetryIntervalsHelper intervalsHelper;

		/**
		 * Instantiates a new retry on failure behaviour.
		 * @param agent the agent performing the behaviour.
		 * @param timeout the retry timeout
		 */
		public RetryOnFailureBehaviour(Agent agent, IncreasingRetryIntervalsHelper intervalsHelper) {
			super(agent, intervalsHelper.getCurrentRetryInterval());
			this.intervalsHelper = intervalsHelper;
		}

		/* (non-Javadoc)
		 * @see jade.core.behaviours.WakerBehaviour#onWake()
		 */
		@Override
		protected void onWake() {
			// --- Trigger a new registration attempt -----
			PhoneBookRegistrationInitiator nextTry = new PhoneBookRegistrationInitiator(this.myAgent, myPhoneBookEntry, phoneBookMaintainer, retryOnFailure);
			nextTry.setIntervalsHelper(this.intervalsHelper);
			this.myAgent.addBehaviour(nextTry);
		}
		
	}

}
