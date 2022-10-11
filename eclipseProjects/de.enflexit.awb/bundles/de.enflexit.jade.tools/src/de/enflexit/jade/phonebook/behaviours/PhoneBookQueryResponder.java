package de.enflexit.jade.phonebook.behaviours;

import java.io.IOException;
import java.util.List;

import de.enflexit.jade.phonebook.AbstractPhoneBookEntry;
import de.enflexit.jade.phonebook.PhoneBook;
import de.enflexit.jade.phonebook.search.PhoneBookSearchResults;
import de.enflexit.jade.phonebook.search.PhoneBookSearchFilter;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.SimpleAchieveREResponder;

/**
 * This class implements the responder role of the FIPA query protocol for phone book queries.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 * @param <T> the generic type
 */
public class PhoneBookQueryResponder<T extends AbstractPhoneBookEntry> extends SimpleAchieveREResponder{

	private static final long serialVersionUID = 1416124382854967230L;
	
	private PhoneBook<T> localPhoneBook;
	
	private MessageTemplate messageTemplate;

	/**
	 * Instantiates a new phone book query responder.
	 * @param agent the agent
	 * @param localPhoneBook the local phone book
	 */
	public PhoneBookQueryResponder(Agent agent, PhoneBook<T> localPhoneBook) {
		super(agent, createMessageTemplate());
		this.localPhoneBook = localPhoneBook;
		this.messageTemplate = messageTemplate;
	}
	
	/**
	 * Creates the message template.
	 * @return the message template
	 */
	private static MessageTemplate createMessageTemplate() {
		MessageTemplate matchProtocol = MessageTemplate.MatchProtocol(FIPA_QUERY);
		MessageTemplate matchConversationId = MessageTemplate.MatchConversationId(ConversationID.PHONEBOOK_QUERY.toString());
		return MessageTemplate.and(matchProtocol, matchConversationId);
	}
	
	/**
	 * Gets the message template.
	 * @return the message template
	 */
	public MessageTemplate getMessageTemplate() {
		return messageTemplate;
	}
	
	/* (non-Javadoc)
	 * @see jade.proto.SimpleAchieveREResponder#prepareResponse(jade.lang.acl.ACLMessage)
	 */
	@Override
	protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
		// --- Method not needed, just overridden to suppress the console output from the superclass method
		return null;
	}

	/* (non-Javadoc)
	 * @see jade.proto.SimpleAchieveREResponder#prepareResultNotification(jade.lang.acl.ACLMessage, jade.lang.acl.ACLMessage)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected ACLMessage prepareResultNotification(ACLMessage requestMessage, ACLMessage responseMessage) throws FailureException {
		String errorMessage = null;
		ACLMessage resultMessage = requestMessage.createReply();
		try {
			// --- Extract and process the query ---------- 
			PhoneBookSearchFilter<T> searchFilter = (PhoneBookSearchFilter<T>) requestMessage.getContentObject();
			List<T> searchResults = this.localPhoneBook.searchEntries(searchFilter);
			
			PhoneBookSearchResults<T> queryResponse = new PhoneBookSearchResults<>();
			queryResponse.getSearchResults().addAll(searchResults);
			
			resultMessage.setContentObject(queryResponse);
			
		} catch (UnreadableException e) {
			// --- Handle errors --------------------------
			errorMessage = "Error extracting content object from the query message";
			System.out.println("[" + this.getClass().getSimpleName() + "] " + errorMessage);
			e.printStackTrace();
		} catch (IOException e) {
			errorMessage = "Error setting content object for the result message";
			System.out.println("[" + this.getClass().getSimpleName() + "] " + errorMessage);
			e.printStackTrace();
		}
		
		// --- Finalize result message --------------------
		if (errorMessage==null) {
			resultMessage.setPerformative(ACLMessage.INFORM);
		} else {
			requestMessage.setPerformative(ACLMessage.FAILURE);
			requestMessage.setContent(errorMessage);
		}
		
		return resultMessage;
	}

	
}
