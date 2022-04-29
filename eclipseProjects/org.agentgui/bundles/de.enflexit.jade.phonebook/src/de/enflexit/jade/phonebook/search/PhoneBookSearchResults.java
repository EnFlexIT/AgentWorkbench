package de.enflexit.jade.phonebook.search;

import java.io.Serializable;
import java.util.ArrayList;

import de.enflexit.jade.phonebook.AbstractPhoneBookEntry;

/**
 * The Class PhoneBookQueryResponse.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 * @param <T> the generic type
 */
public class PhoneBookSearchResults<T extends AbstractPhoneBookEntry> implements Serializable{

	private static final long serialVersionUID = 1550341761728633000L;
	
	private ArrayList<T> searchResults;
	
	/**
	 * Gets the query results.
	 * @return the query results
	 */
	public ArrayList<T> getSearchResults() {
		if (searchResults==null) {
			searchResults = new ArrayList<T>();
		}
		return searchResults;
	}

}
