package de.enflexit.jade.phonebook;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents phone book related events. It specifies the
 * event type, and provides a list of affected entries.
 *
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class PhoneBookEvent {
	
	// The possible types of phone book events.
	public static enum Type{
		ENTRIES_ADDED, 
		ENTRIES_REMOVED, 
		REGISTRATION_DONE, 
		QUERY_RESULT_AVAILABLE
	}
	
	private Type type;
	private List<? extends AbstractPhoneBookEntry> affectedEntries;

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
	public PhoneBookEvent(Type type, AbstractPhoneBookEntry affectedEntry) {
		ArrayList<AbstractPhoneBookEntry> affectedEntries = new ArrayList<>();
		affectedEntries.add(affectedEntry);
		this.type = type;
		this.affectedEntries = affectedEntries;
	}
	/**
	 * Instantiates a new phone book event that affects multiple entries.
	 * @param type the event type
	 * @param affectedEntries the affected entries
	 */
	public PhoneBookEvent(Type type, List<? extends AbstractPhoneBookEntry> affectedEntries) {
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
	public List<? extends AbstractPhoneBookEntry> getAffectedEntries() {
		return affectedEntries;
	}
	/**
	 * Sets the affected entries.
	 * @param affectedEntries the new affected entries
	 */
	public void setAffectedEntries(List<AbstractPhoneBookEntry> affectedEntries) {
		this.affectedEntries = affectedEntries;
	}
	
	/**
	 * Gets the first affected entry, for easier handling of events that affect only one entry.
	 * @return the first affected entry
	 */
	public AbstractPhoneBookEntry getFirstAffectedEntry() {
		if (this.getAffectedEntries().size()>0) {
			return affectedEntries.get(0);
		}
		return null;
	}
	
}
