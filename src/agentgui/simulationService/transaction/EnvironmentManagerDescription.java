/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.simulationService.transaction;

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
