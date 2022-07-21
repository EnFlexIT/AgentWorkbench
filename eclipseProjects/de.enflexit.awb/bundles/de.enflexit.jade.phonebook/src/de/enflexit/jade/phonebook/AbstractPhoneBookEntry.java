package de.enflexit.jade.phonebook;

import java.io.Serializable;

import jade.core.AID;

/**
 * Generic superclass for phone book entries
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public abstract class AbstractPhoneBookEntry implements Serializable{
	
	private static final long serialVersionUID = -4353589104009626385L;
	
	private AID agentAID;

	/**
	 * Gets a unique identifier for this {@link AbstractPhoneBookEntry}
	 * @return the unique identifier
	 */
	public abstract String getUniqueIdentifier();
	
	/**
	 * Gets the agent AID.
	 * @return the agent AID
	 */
	public AID getAgentAID() {
		return agentAID;
	}
	/**
	 * Sets the agent AID.
	 * @param agentAID the new agent AID
	 */
	public void setAgentAID(AID agentAID) {
		this.agentAID = agentAID;
	}


	/**
	 * This method allows to implement validity checks.
	 * If you don't want to perform checks, just return true.
	 * @return true, if the entry is valid
	 */
	public abstract boolean isValid();
	
}
