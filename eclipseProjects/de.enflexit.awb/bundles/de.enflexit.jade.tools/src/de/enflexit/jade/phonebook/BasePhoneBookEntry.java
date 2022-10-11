package de.enflexit.jade.phonebook;

/**
 * This basic implementation of {@link AbstractPhoneBookEntry} can be used if an AID is all you need.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class BasePhoneBookEntry extends AbstractPhoneBookEntry {

	private static final long serialVersionUID = -7075326381062693539L;

	/* (non-Javadoc)
	 * @see de.enflexit.jade.phonebook.AbstractPhoneBookEntry#getUniqueIdentifier()
	 */
	@Override
	public String getUniqueIdentifier() {
		return this.getAgentAID().getName();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.jade.phonebook.AbstractPhoneBookEntry#isValid()
	 */
	@Override
	public boolean isValid() {
		return true;
	}

}
