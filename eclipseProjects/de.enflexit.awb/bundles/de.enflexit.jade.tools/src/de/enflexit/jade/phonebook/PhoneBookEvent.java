package de.enflexit.jade.phonebook;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents phone book related events. It specifies the
 * event type, and provides a list of affected entries.
 *
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 * @param <GenericPhoneBookEntry> the generic type
 */
public class PhoneBookEvent<GenericPhoneBookEntry extends AbstractPhoneBookEntry> {
	
	// The possible types of phone book events.
	public static enum Type{
		ENTRIES_ADDED, 
		ENTRIES_REMOVED, 
		REGISTRATION_DONE, 
		QUERY_RESULT_AVAILABLE
	}
	
	private Type type;
	private List<GenericPhoneBookEntry> affectedEntries;

	/**
	 * Instantiates a new empty phone book event.
	 */
	public PhoneBookEvent() {
	}
	
	/**
	 * Instantiates a new phone book event that affects a single entry.
	 * @param type the type
	 * @param affectedEntry the affected entry
	 */
	public PhoneBookEvent(Type type, GenericPhoneBookEntry affectedEntry) {
		ArrayList<GenericPhoneBookEntry> affectedEntries = new ArrayList<GenericPhoneBookEntry>();
		affectedEntries.add(affectedEntry);
		this.type = type;
		this.affectedEntries = affectedEntries;
	}
	/**
	 * Instantiates a new phone book event that affects multiple entries.
	 * @param type the event type
	 * @param affectedEntries the affected entries
	 */
	public PhoneBookEvent(Type type, List<GenericPhoneBookEntry> affectedEntries) {
		this.type = type;
		this.affectedEntries = affectedEntries;
	}
	/**
	 * Gets the type.
	 * @return the type
	 */
	public Type getType() {
		return type;
	}
	/**
	 * Sets the type.
	 * @param type the new type
	 */
	public void setType(Type type) {
		this.type = type;
	}
	/**
	 * Gets the affected entries.
	 * @return the affected entries
	 */
	public List<GenericPhoneBookEntry> getAffectedEntries() {
		return affectedEntries;
	}
	/**
	 * Sets the affected entries.
	 * @param affectedEntries the new affected entries
	 */
	public void setAffectedEntries(List<GenericPhoneBookEntry> affectedEntries) {
		this.affectedEntries = affectedEntries;
	}
	
	/**
	 * Gets the first affected entry, for easier handling of events that affect only one entry.
	 * @return the first affected entry
	 */
	public GenericPhoneBookEntry getFirstAffectedEntry() {
		if (this.getAffectedEntries().size()>0) {
			return affectedEntries.get(0);
		}
		return null;
	}
	
}
