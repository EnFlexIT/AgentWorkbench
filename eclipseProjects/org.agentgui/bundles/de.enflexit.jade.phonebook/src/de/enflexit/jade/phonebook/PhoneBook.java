package de.enflexit.jade.phonebook;

import java.util.ArrayList;
import java.util.TreeMap;

import de.enflexit.jade.phonebook.search.PhoneBookSearchFilter;

public class PhoneBook<T extends AbstractPhoneBookEntry> {
	TreeMap<String, T> phoneBookContent;
	
	/**
	 * Gets the phone book content.
	 * @return the phone book content
	 */
	private TreeMap<String, T> getPhoneBookContent() {
		if (phoneBookContent==null) {
			phoneBookContent = new TreeMap<String, T>();
		}
		return phoneBookContent;
	}
	
	/**
	 * Adds the specified entry to the phone book.
	 * @param identifier the identifier
	 * @param entry the entry
	 */
	public void addEntry(T entry) {
		this.getPhoneBookContent().put(entry.getUniqueIdentifier(), entry);
	}
	
	/**
	 * Gets the entry with the specified identifier.
	 * @param identifier the identifier
	 * @return the entry
	 */
	public AbstractPhoneBookEntry getEntry(String identifier) {
		return this.getPhoneBookContent().get(identifier);
	}
	
	/**
	 * Gets the entries.
	 * @param searchFilter the search filter
	 * @return the entries
	 */
	public ArrayList<T> getEntries(PhoneBookSearchFilter<T> searchFilter){
		ArrayList<T> resultList = new ArrayList<T>();
		for (T entry : this.getPhoneBookContent().values()) {
			if (searchFilter.matches(entry)) {
				resultList.add(entry);
			}
		}
		return resultList;
	}
	
	/**
	 * Searches for phone book entries that match the provided search filter.
	 * @param searchFilter the search filter
	 * @return the array list
	 */
	public ArrayList<T> searchEntries(PhoneBookSearchFilter<T> searchFilter){
		ArrayList<T> resultList = new ArrayList<T>();
		for (T entry : this.getPhoneBookContent().values()) {
			if (searchFilter.matches(entry)) {
				resultList.add(entry);
			}
		}
		return resultList;
	}
}
