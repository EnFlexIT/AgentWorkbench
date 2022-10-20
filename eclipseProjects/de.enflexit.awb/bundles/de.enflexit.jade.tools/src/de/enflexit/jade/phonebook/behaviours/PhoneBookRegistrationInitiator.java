package de.enflexit.jade.phonebook.behaviours;

import java.io.IOException;
import java.util.ArrayList;

import de.enflexit.jade.phonebook.AbstractPhoneBookEntry;
import de.enflexit.jade.phonebook.PhoneBookEvent;
import de.enflexit.jade.phonebook.PhoneBookListener;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
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
	
	protected boolean debug = false;
	
	private ArrayList<PhoneBookListener> listeners;
	
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
		requestMessage.setConversationId(PhoneBookRegistrationResponder.CONVERSATION_ID);
		try {
			requestMessage.setContentObject(myPhoneBookEntry);
		} catch (IOException e) {
			System.out.println("[" + PhoneBookRegistrationInitiator.class.getSimpleName() + "] Error setting content object for the reqistration request of " + myPhoneBookEntry.getLocalName());
			e.printStackTrace();
		}
		return requestMessage;
	}
	
	/* (non-Javadoc)
	 * @see jade.proto.SimpleAchieveREInitiator#handleInform(jade.lang.acl.ACLMessage)
	 */
	@Override
	protected void handleInform(ACLMessage msg) {
		
		try {
			Object contentObject = msg.getContentObject();
			if (contentObject!=null && contentObject instanceof AbstractPhoneBookEntry) {
				AbstractPhoneBookEntry returnedPhoneBookEntry = (AbstractPhoneBookEntry) contentObject;
				
				this.notifyDone(returnedPhoneBookEntry);
			}
			if (debug==true) {
				System.out.println("[" + this.myAgent.getLocalName() + "] Phonebook registration successful");
			}
		} catch (UnreadableException e) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Could not extract content from the inform message!");
			e.printStackTrace();
		}
		
	}

	/* (non-Javadoc)
	 * @see jade.proto.SimpleAchieveREInitiator#handleFailure(jade.lang.acl.ACLMessage)
	 */
	@Override
	protected void handleFailure(ACLMessage msg) {
		if (debug==true) {
			System.out.println("[" + this.myAgent.getLocalName() + "] Phonebook registration failed");
		}
		if (this.retryOnFailure==true) {
			if (debug==true) {
				System.out.println("[" + this.myAgent.getLocalName() + "] Trying again after " + this.getIntervalsHelper().getCurrentIntervalWithoutIncrease()/1000 + " s");
			}
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
	
	/**
	 * Gets the registered listeners.
	 * @return the listeners
	 */
	private ArrayList<PhoneBookListener> getListeners() {
		if (listeners==null) {
			listeners = new ArrayList<PhoneBookListener>();
		}
		return listeners;
	}
	
	/**
	 * Adds the phone book listener.
	 * @param listener the listener
	 */
	public void addPhoneBookListener(PhoneBookListener listener) {
		if (this.getListeners().contains(listener)==false) {
			this.getListeners().add(listener);
		}
	}
	
	/**
	 * Removes the phone book listener.
	 * @param listener the listener
	 */
	public void removePhoneBookListener(PhoneBookListener listener) {
		if (this.getListeners().contains(listener)==true) {
			this.getListeners().remove(listener);
		}
	}
	
	/**
	 * Notifies the registered listeners that the registration is done.
	 * @param returnedEntry the returned entry
	 */
	private void notifyDone(AbstractPhoneBookEntry returnedEntry) {
		PhoneBookEvent successEvent = new PhoneBookEvent(PhoneBookEvent.Type.REGISTRATION_DONE, returnedEntry);
		for (int i=0; i<this.getListeners().size(); i++) {
			this.getListeners().get(i).notifyEvent(successEvent);
		}
	}

}
