package de.enflexit.jade.phonebook.search;

import java.io.Serializable;

import de.enflexit.jade.phonebook.AbstractPhoneBookEntry;

/**
 * This interface can be implemented to provide filters to search phone books
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public interface PhoneBookSearchFilter extends Serializable{
	
	/**
	 * Checks if the provides {@link AbstractPhoneBookEntry} matches this filter.
	 * @param entry the entry
	 * @return true, if successful
	 */
	public boolean matches(AbstractPhoneBookEntry entry);
}
