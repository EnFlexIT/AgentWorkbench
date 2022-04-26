package de.enflexit.jade.phonebook.search;

import java.io.Serializable;

import de.enflexit.jade.phonebook.AbstractPhoneBookEntry;

/**
 * This class specifies a query to request phone book entries from a central provider.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 * @param <T> the generic type
 */
public class PhoneBookQuery <T extends AbstractPhoneBookEntry> implements Serializable
{
	private static final long serialVersionUID = -8669963430967210610L;
	
	private String identifier;
	private PhoneBookSearchFilter<T> searchFilter;
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public PhoneBookSearchFilter<T> getSearchFilter() {
		return searchFilter;
	}
	public void setSearchFilter(PhoneBookSearchFilter<T> searchFilter) {
		this.searchFilter = searchFilter;
	}
	
	
}
