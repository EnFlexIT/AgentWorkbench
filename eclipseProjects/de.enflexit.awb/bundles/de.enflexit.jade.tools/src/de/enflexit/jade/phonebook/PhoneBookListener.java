package de.enflexit.jade.phonebook;


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
public interface PhoneBookListener {
	
	/**
	 * This method is invoked when a new entry is added to the phone book.
	 * @param addedEntry the added entry
	 */
	public void phoneBookEntryAdded(AbstractPhoneBookEntry addedEntry);
	
	/**
	 * This method is invoked when a book entry is removed.
	 * @param removedEntry the removed entry
	 */
	public void phoneBookEntryRemoved(AbstractPhoneBookEntry removedEntry);
}
