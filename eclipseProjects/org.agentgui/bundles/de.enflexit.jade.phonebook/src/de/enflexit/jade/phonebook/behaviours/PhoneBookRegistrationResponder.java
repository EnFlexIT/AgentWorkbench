package de.enflexit.jade.phonebook.behaviours;

import de.enflexit.jade.phonebook.AbstractPhoneBookEntry;
import de.enflexit.jade.phonebook.PhoneBook;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;

public class PhoneBookRegistrationResponder<T extends AbstractPhoneBookEntry> extends AchieveREResponder {

	private static final long serialVersionUID = -9126496583768678087L;
	
	private PhoneBook<T> localPhoneBook;

	/**
	 * Instantiates a new phone book registration responder.
	 * @param agent the agent
	 * @param messageTemplate the message template
	 * @param localPhoneBook the local phone book
	 */
	public PhoneBookRegistrationResponder(Agent agent, MessageTemplate messageTemplate, PhoneBook<T> localPhoneBook) {
		super(agent, messageTemplate);
		this.localPhoneBook = localPhoneBook;
	}
	
	/* (non-Javadoc)
	 * @see jade.proto.AchieveREResponder#handleRequest(jade.lang.acl.ACLMessage)
	 */
	@Override
	protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
		Object contentObject = null;
		
		System.out.println("[" + this.myAgent.getLocalName() + "] Received registration request from " + request.getSender().getLocalName());
		
		try {
			contentObject = request.getContentObject();
		} catch (UnreadableException e) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error extracting content object");
			e.printStackTrace();
		}
		if (contentObject != null && contentObject instanceof AbstractPhoneBookEntry) {
			@SuppressWarnings("unchecked")
			T phoneBookEntry = (T) contentObject;
			this.localPhoneBook.addEntry(phoneBookEntry);
			
			System.out.println("[" + this.myAgent.getLocalName() + "] PhoneBookEntry successfully added ");
		}
		
		return super.handleRequest(request);
	}
	
	/* (non-Javadoc)
	 * @see jade.proto.AchieveREResponder#prepareResponse(jade.lang.acl.ACLMessage)
	 */
	@Override
	protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
		return null;
	}

	/* (non-Javadoc)
	 * @see jade.proto.AchieveREResponder#prepareResultNotification(jade.lang.acl.ACLMessage, jade.lang.acl.ACLMessage)
	 */
	@Override
	protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
		response.setPerformative(ACLMessage.CONFIRM);
		return response;
	}

	
}
