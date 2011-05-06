package agentgui.simulationService.transaction;

import java.io.Serializable;

import jade.core.AID;
import jade.core.Location;


public class EnvironmentManagerDescription implements Serializable {

	private static final long serialVersionUID = -936922187980352665L;

	private AID manAID = null;
	private Location manLocation = null; 
	
	public EnvironmentManagerDescription(AID managerAID, Location managerLocation) {
		this.manAID = managerAID;
		this.manLocation = managerLocation;
	}

	/**
	 * @return the manAID
	 */
	public AID getAID() {
		return manAID;
	}
	/**
	 * @param manAID the manAID to set
	 */
	public void setAID(AID manAID) {
		this.manAID = manAID;
	}

	/**
	 * @return the manLocation
	 */
	public Location getLocation() {
		return manLocation;
	}
	/**
	 * @param manLocation the manLocation to set
	 */
	public void setLocation(Location manLocation) {
		this.manLocation = manLocation;
	}

}
