package de.enflexit.awb.simulation.transaction;

import java.io.Serializable;

import jade.core.AID;
import jade.core.Location;

/**
 * This class provides the description of the manager, who is managing a simulation.
 * It consists of the AID an the location of that agent. 
 *   
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class EnvironmentManagerDescription implements Serializable {

	private static final long serialVersionUID = -936922187980352665L;

	private AID manAID = null;
	private Location manLocation = null; 
	
	/**
	 * Instantiates a new environment manager description.
	 *
	 * @param managerAID the managers AID
	 * @param managerLocation the managers Location
	 */
	public EnvironmentManagerDescription(AID managerAID, Location managerLocation) {
		this.manAID = managerAID;
		this.manLocation = managerLocation;
	}

	/**
	 * Returns the AID of the manager.
	 * @return the manAID
	 */
	public AID getAID() {
		return manAID;
	}
	/**
	 * Sets the AID of the manager.
	 * @param manAID the manAID to set
	 */
	public void setAID(AID manAID) {
		this.manAID = manAID;
	}

	/**
	 * Returns the location.
	 * @return the Location of the manager
	 */
	public Location getLocation() {
		return manLocation;
	}
	/**
	 * Sets the location of the manager.
	 * @param managerLocation the Location of the manager
	 */
	public void setLocation(Location managerLocation) {
		this.manLocation = managerLocation;
	}

}
