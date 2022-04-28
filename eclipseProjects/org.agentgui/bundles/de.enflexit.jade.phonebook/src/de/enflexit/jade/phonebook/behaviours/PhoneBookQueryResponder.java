package de.enflexit.jade.phonebook.behaviours;

import java.io.IOException;
import java.util.List;

import de.enflexit.jade.phonebook.AbstractPhoneBookEntry;
import de.enflexit.jade.phonebook.PhoneBook;
import de.enflexit.jade.phonebook.search.PhoneBookQuery;
import de.enflexit.jade.phonebook.search.PhoneBookQueryResponse;
import de.enflexit.jade.phonebook.search.PhoneBookSearchFilter;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREResponder;

public class PhoneBookQueryResponder<T extends AbstractPhoneBookEntry> extends AchieveREResponder{

	private static final long serialVersionUID = 1416124382854967230L;
	
	private PhoneBook<T> localPhoneBook;

	public PhoneBookQueryResponder(Agent agent, MessageTemplate messageTemplate, PhoneBook<T> localPhoneBook) {
		super(agent, messageTemplate);
		this.localPhoneBook = localPhoneBook;
	}
	
	@Override
	protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ACLMessage prepareResultNotification(ACLMessage requestMessage, ACLMessage responseMessage) throws FailureException {
		PhoneBookQuery<T> query;
		try {
			query = (PhoneBookQuery<T>) requestMessage.getContentObject();
			PhoneBookSearchFilter<T> searchFilter = query.getSearchFilter();
			List<T> searchResults = this.localPhoneBook.searchEntries(searchFilter);
			
			PhoneBookQueryResponse<T> queryResponse = new PhoneBookQueryResponse<>();
			queryResponse.getQueryResults().addAll(searchResults);
			
			responseMessage.setContentObject(queryResponse);
			
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.prepareResultNotification(requestMessage, responseMessage);
	}

	
}
