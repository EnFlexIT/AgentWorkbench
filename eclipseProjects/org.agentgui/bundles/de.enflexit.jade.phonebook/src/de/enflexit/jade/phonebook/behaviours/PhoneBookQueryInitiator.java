package de.enflexit.jade.phonebook.behaviours;

import java.util.List;

import de.enflexit.jade.phonebook.AbstractPhoneBookEntry;
import de.enflexit.jade.phonebook.PhoneBook;
import de.enflexit.jade.phonebook.search.PhoneBookQueryResponse;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;

public class PhoneBookQueryInitiator<T extends AbstractPhoneBookEntry> extends AchieveREInitiator {

	private static final long serialVersionUID = 1214570247786648424L;
	
	private PhoneBook<T> localPhoneBook;

	public PhoneBookQueryInitiator(Agent a, ACLMessage msg, PhoneBook<T> localPhoneBook) {
		super(a, msg);
		this.localPhoneBook = localPhoneBook;
	}

	/* (non-Javadoc)
	 * @see jade.proto.AchieveREInitiator#handleInform(jade.lang.acl.ACLMessage)
	 */
	@Override
	protected void handleInform(ACLMessage responseMessage) {
		if (responseMessage!=null) {
			try {
				@SuppressWarnings("unchecked")
				PhoneBookQueryResponse<T> response = (PhoneBookQueryResponse<T>) responseMessage.getContentObject();
				if (response!=null) {
					List<T> results = response.getQueryResults();
					
					for (int i=0; i<results.size(); i++) {
						T result = results.get(i);
						this.localPhoneBook.addEntry(result);
					}
				}
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	

}
