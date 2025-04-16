package de.enflexit.jade.phonebook.behaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.enflexit.jade.phonebook.AbstractPhoneBookEntry;
import de.enflexit.jade.phonebook.PhoneBook;
import de.enflexit.jade.phonebook.PhoneBookEvent;
import de.enflexit.jade.phonebook.PhoneBookListener;
import de.enflexit.jade.phonebook.search.PhoneBookSearchResults;
import de.enflexit.jade.phonebook.search.PhoneBookSearchFilter;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.SimpleAchieveREInitiator;

/**
 * This class implements the initiator role of the FIPA query protocol for phone book queries. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 * @param <GenericPhoneBookEntry> the generic type
 */
public class PhoneBookQueryInitiator<GenericPhoneBookEntry extends AbstractPhoneBookEntry> extends SimpleAchieveREInitiator {

	private static final long serialVersionUID = 1214570247786648424L;
	
	private PhoneBook localPhoneBook;
	private PhoneBookSearchFilter<GenericPhoneBookEntry> searchFilter;
	
	private AID phoneBookMaintainer;
	private boolean retryOnFailure;
	
	private IncreasingRetryIntervalsHelper intervalsHelper;
	
	private ArrayList<PhoneBookListener> listeners;
	
	/**
	 * Instantiates a new phone book query initiator.
	 * @param agent the agent initiating the query
	 * @param localPhoneBook the agent's local phone book
	 * @param phoneBookMaintainer the AID of the phone book maintainer
	 * @param searchFilter the search filter
	 * @param retryOnFailure specifies if the query should be rescheduled if it fails
	 */
	public PhoneBookQueryInitiator(Agent agent, PhoneBook localPhoneBook, AID phoneBookMaintainer, PhoneBookSearchFilter<GenericPhoneBookEntry> searchFilter, boolean retryOnFailure) {
		super(agent, createQueryMessage(phoneBookMaintainer));
		this.localPhoneBook = localPhoneBook;
		this.searchFilter = searchFilter;
		this.phoneBookMaintainer = phoneBookMaintainer;
		this.retryOnFailure = retryOnFailure;
	}
	
	/**
	 * Creates the initial query message.
	 * @param phoneBookMaintainer the phone book maintainer
	 * @return the ACL message
	 */
	private static ACLMessage createQueryMessage(AID phoneBookMaintainer) {
		ACLMessage queryMessage = new ACLMessage(ACLMessage.QUERY_REF);
		queryMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_QUERY);
		queryMessage.setConversationId(PhoneBookQueryResponder.CONVERSATION_ID);
		queryMessage.addReceiver(phoneBookMaintainer);
		return queryMessage;
	}
	
	/* (non-Javadoc)
	 * @see jade.proto.SimpleAchieveREInitiator#prepareRequest(jade.lang.acl.ACLMessage)
	 */
	@Override
	protected ACLMessage prepareRequest(ACLMessage requestMessage) {
		try {
			requestMessage.setContentObject(this.searchFilter);
		} catch (IOException e) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error setting content object for phone book query");
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
			// --- Extract contents ---------------------------------
			Object contentObject = msg.getContentObject();
			if (contentObject!=null && contentObject instanceof PhoneBookSearchResults) {
				@SuppressWarnings("unchecked")
				PhoneBookSearchResults<GenericPhoneBookEntry> queryResult = (PhoneBookSearchResults<GenericPhoneBookEntry>) contentObject;
				
				// --- Add entries to the local phone book ----------
				ArrayList<GenericPhoneBookEntry> results = queryResult.getSearchResults();
				this.localPhoneBook.addEntries(results);
				
				// --- Notify listeners -----------------------------
				this.notifyResultsAvailable(results);
			}
			
		} catch (UnreadableException e) {
			// --- Handle errors --------------------------
			System.err.println("[" + this.getClass().getSimpleName() + "] Unable to extract content object from query response");
			e.printStackTrace();
		} catch (ClassCastException e) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Unexpected result type");
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see jade.proto.SimpleAchieveREInitiator#handleFailure(jade.lang.acl.ACLMessage)
	 */
	@Override
	protected void handleFailure(ACLMessage msg) {
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
	 * This behaviour can be used to reschedule the query after a certain timeout. 
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	private class RetryOnFailureBehaviour extends WakerBehaviour{

		private static final long serialVersionUID = 5524418573211565835L;
		
		private IncreasingRetryIntervalsHelper intervalsHelper;

		/**
		 * Instantiates a new retry on failure behaviour.
		 * @param agent the agent
		 * @param timeout the timeout
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
			PhoneBookQueryInitiator<GenericPhoneBookEntry> nextTry = new PhoneBookQueryInitiator<>(this.myAgent, localPhoneBook, phoneBookMaintainer, searchFilter, retryOnFailure);
			nextTry.setIntervalsHelper(intervalsHelper);
			this.myAgent.addBehaviour(nextTry);
		}
		
	}
	
	/**
	 * Gets the currently registered listeners.
	 * @return the listeners
	 */
	private ArrayList<PhoneBookListener> getListeners() {
		if (listeners==null) {
			listeners = new ArrayList<PhoneBookListener>();
		}
		return listeners;
	}
	
	/**
	 * Notify the listeners about the results.
	 * @param queryResults the query results
	 */
	private void notifyResultsAvailable(List<GenericPhoneBookEntry> queryResults) {
		PhoneBookEvent resultsEvent = new PhoneBookEvent(PhoneBookEvent.Type.QUERY_RESULT_AVAILABLE, queryResults);
		for (int i=0; i<this.getListeners().size(); i++) {
			this.getListeners().get(i).handlePhoneBookEvent(resultsEvent);
		}
		
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
	
}
