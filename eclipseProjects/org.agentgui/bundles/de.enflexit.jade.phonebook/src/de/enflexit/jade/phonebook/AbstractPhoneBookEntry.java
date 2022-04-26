package de.enflexit.jade.phonebook;

import java.io.Serializable;

/**
 * Generic superclass for phone book entries
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public abstract class AbstractPhoneBookEntry implements Serializable{
	
	private static final long serialVersionUID = -4353589104009626385L;

	/**
	 * Gets a unique identifier for this {@link AbstractPhoneBookEntry}
	 * @return the unique identifier
	 */
	public abstract String getUniqueIdentifier();
	
}
