package de.enflexit.jade.phonebook.search;

import java.io.Serializable;
import java.util.ArrayList;

import de.enflexit.jade.phonebook.AbstractPhoneBookEntry;

/**
 * The Class PhoneBookQueryResponse.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class PhoneBookSearchResults<GenericPhoneBookEntry extends AbstractPhoneBookEntry> implements Serializable{

	private static final long serialVersionUID = 1550341761728633000L;
	
	private ArrayList<GenericPhoneBookEntry> searchResults;
	
	/**
	 * Gets the query results.
	 * @return the query results
	 */
	public ArrayList<GenericPhoneBookEntry> getSearchResults() {
		if (searchResults==null) {
			searchResults = new ArrayList<>();
		}
		return searchResults;
	}

}
