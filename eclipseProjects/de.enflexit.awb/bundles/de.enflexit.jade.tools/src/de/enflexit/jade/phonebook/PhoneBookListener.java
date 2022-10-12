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
 * @param <GenericPhoneBookEntry> the generic type
 * @see PhoneBookEvent
 */
public interface PhoneBookListener {
	
	/**
	 * Override this method to handle phone book related events.
	 * @param event the event
	 */
	public void notifyEvent(PhoneBookEvent event);
}
