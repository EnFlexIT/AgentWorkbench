package de.enflexit.jade.phonebook.search;

import java.io.Serializable;
import java.util.ArrayList;

import de.enflexit.jade.phonebook.AbstractPhoneBookEntry;

/**
 * The Class PhoneBookQueryResponse.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class PhoneBookSearchResults implements Serializable{

	private static final long serialVersionUID = 1550341761728633000L;
	
	private ArrayList<AbstractPhoneBookEntry> searchResults;
	
	/**
	 * Gets the query results.
	 * @return the query results
	 */
	public ArrayList<AbstractPhoneBookEntry> getSearchResults() {
		if (searchResults==null) {
			searchResults = new ArrayList<>();
		}
		return searchResults;
	}

}
