package de.enflexit.jade.phonebook.behaviours;

import java.io.IOException;

import de.enflexit.jade.phonebook.AbstractPhoneBookEntry;
import de.enflexit.jade.phonebook.PhoneBook;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.SimpleAchieveREResponder;

/**
 * This class implements the Responder role of a FIPA request protocol for phone book registrations. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 * @param <GenericPhoneBookEntry> the generic type
 */
public class PhoneBookRegistrationResponder<GenericPhoneBookEntry extends AbstractPhoneBookEntry> extends SimpleAchieveREResponder {

	private static final long serialVersionUID = -9126496583768678087L;
	
	private PhoneBook localPhoneBook;
	
	/**
	 * Instantiates a new phone book registration responder.
	 * @param agent the agent
	 * @param localPhoneBook the local phone book
	 */
	public PhoneBookRegistrationResponder(Agent agent, PhoneBook localPhoneBook) {
		super(agent, createMessageTemplate());
		this.localPhoneBook = localPhoneBook;
	}

	/**
	 * Creates the message template for this protocol responder.
	 * @return the message template
	 */
	private static MessageTemplate createMessageTemplate() {
		MessageTemplate matchProtocol = MessageTemplate.MatchProtocol(FIPA_REQUEST);
		MessageTemplate matchConversationID = MessageTemplate.MatchConversationId(ConversationID.PHONEBOOK_REGISTRATION.toString());
		return MessageTemplate.and(matchProtocol, matchConversationID);
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
	@Override
	protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
		ACLMessage resultNotification = request.createReply();
		String errorMessage = null;
		
		try {
			// --- Extract message content ----------------
			Object contentObject = request.getContentObject();
			if (contentObject!=null && contentObject instanceof AbstractPhoneBookEntry) {
				@SuppressWarnings("unchecked")
				GenericPhoneBookEntry phoneBookEntry = (GenericPhoneBookEntry) contentObject;
				phoneBookEntry = this.processPhoneBookEntry(phoneBookEntry);
				this.localPhoneBook.addEntry(phoneBookEntry);
				resultNotification.setPerformative(ACLMessage.CONFIRM);
				resultNotification.setContentObject(phoneBookEntry);
			}
		} catch (UnreadableException e) {
			// --- Handle errors --------------------------
			errorMessage = "Unable to extract message content";
			e.printStackTrace();
		} catch (ClassCastException e) {
			errorMessage = "Unexpected content object class";
			e.printStackTrace();
		} catch (IOException e) {
			errorMessage = "Could not set content object for result notification";
			e.printStackTrace();
		}
		
		// --- Prepare the result notification message ----
		if (errorMessage==null) {
			resultNotification.setPerformative(ACLMessage.INFORM);
		} else {
			resultNotification.setPerformative(ACLMessage.FAILURE);
			resultNotification.setContent(errorMessage);
			System.err.println("[" + this.getClass().getSimpleName() + "] Error processing phone book registration request from " + request.getSender().getLocalName() + ": " + errorMessage);
		}
		
		return resultNotification;
	}
	
	/**
	 * This method can be overridden if the received phone book entry should be processed in any way
	 * before storing and returning it. The default implementation just returns the original entry 
	 * @param entry the entry
	 * @return the generic phone book entry
	 */
	protected GenericPhoneBookEntry processPhoneBookEntry(GenericPhoneBookEntry entry) {
		return entry;
	}
	
	/**
	 * Gets the local phone book.
	 *
	 * @return the local phone book
	 */
	public PhoneBook getLocalPhoneBook() {
		return localPhoneBook;
	}
}
