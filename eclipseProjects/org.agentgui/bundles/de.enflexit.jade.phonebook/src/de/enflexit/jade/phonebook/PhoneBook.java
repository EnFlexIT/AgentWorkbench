package de.enflexit.jade.phonebook;

import java.util.ArrayList;
import java.util.TreeMap;

import de.enflexit.jade.phonebook.search.PhoneBookSearchFilter;

public class PhoneBook<T extends AbstractPhoneBookEntry> {
	TreeMap<String, T> phoneBookContent;
	
	private TreeMap<String, T> getPhoneBookContent() {
		if (phoneBookContent==null) {
			phoneBookContent = new TreeMap<String, T>();
		}
		return phoneBookContent;
	}
	
	public void addEntry(String identifier, T entry) {
		this.getPhoneBookContent().put(identifier, entry);
	}
	
	public AbstractPhoneBookEntry getEntry(String identifier) {
		return this.getPhoneBookContent().get(identifier);
	}
	
	public ArrayList<T> getEntries(PhoneBookSearchFilter<T> searchFilter){
		ArrayList<T> resultList = new ArrayList<T>();
		for (T entry : this.getPhoneBookContent().values()) {
			if (searchFilter.matches(entry)) {
				resultList.add(entry);
			}
		}
		return resultList;
	}
}
