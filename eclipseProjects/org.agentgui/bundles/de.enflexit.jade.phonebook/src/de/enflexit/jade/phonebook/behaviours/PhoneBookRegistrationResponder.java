package de.enflexit.jade.phonebook.behaviours;

import de.enflexit.jade.phonebook.AbstractPhoneBookEntry;
import de.enflexit.jade.phonebook.PhoneBook;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;

public class PhoneBookRegistrationResponder<T extends AbstractPhoneBookEntry> extends AchieveREResponder {

	private static final long serialVersionUID = -9126496583768678087L;
	
	private PhoneBook<T> localPhoneBook;

	public PhoneBookRegistrationResponder(Agent a, MessageTemplate mt, PhoneBook<T> localPhoneBook) {
		super(a, mt);
		this.localPhoneBook = localPhoneBook;
	}
	
	/* (non-Javadoc)
	 * @see jade.proto.AchieveREResponder#handleRequest(jade.lang.acl.ACLMessage)
	 */
	@Override
	protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
		Object contentObject = null;
		
		try {
			contentObject = request.getContentObject();
		} catch (UnreadableException e) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Error extracting content object");
			e.printStackTrace();
		}
		if (contentObject instanceof AbstractPhoneBookEntry) {
			@SuppressWarnings("unchecked")
			T phoneBookEntry = (T) contentObject;
			this.localPhoneBook.addEntry(phoneBookEntry.getUniqueIdentifier(), phoneBookEntry);
		}
		
		return super.handleRequest(request);
	}
	
	/* (non-Javadoc)
	 * @see jade.proto.AchieveREResponder#prepareResponse(jade.lang.acl.ACLMessage)
	 */
	@Override
	protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
		ACLMessage response = request.createReply();
		response.setPerformative(ACLMessage.CONFIRM);
		return response;
	}

}
