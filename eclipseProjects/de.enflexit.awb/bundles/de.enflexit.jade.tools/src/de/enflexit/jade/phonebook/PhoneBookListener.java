package de.enflexit.jade.phonebook;

import java.util.List;

/**
 * The listener interface for receiving phoneBook events.
 * The class that is interested in processing a phoneBook
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addPhoneBookListener<code> method. When
 * the phoneBook event occurs, that object's appropriate
 * method is invoked.
 *
 * @see PhoneBookEvent
 */
public interface PhoneBookListener<GenericPhoneBookEntry extends AbstractPhoneBookEntry> {
	
	/**
	 * This method is invoked when a single phone book entry was added to the phone book.
	 * @param addedEntry the added entry
	 */
	public void phoneBookEntryAdded(GenericPhoneBookEntry addedEntry);
	/**
	 * This method is invoked when multiple book entries were added to the phone book.
	 * @param entries the entries
	 */
	public void phoneBookEntriesAdded(List<GenericPhoneBookEntry> entries);
	
	/**
	 * This method is invoked when a phone book entry was removed.
	 * @param removedEntry the removed entry
	 */
	public void phoneBookEntryRemoved(GenericPhoneBookEntry removedEntry);
	
}
