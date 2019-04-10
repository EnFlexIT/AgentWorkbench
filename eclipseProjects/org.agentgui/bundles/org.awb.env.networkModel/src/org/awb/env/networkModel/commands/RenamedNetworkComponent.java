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
package org.awb.env.networkModel.commands;

import org.awb.env.networkModel.NetworkComponent;

/**
 * The Class RenamedNetworkComponent serves as descriptor for renamed NetworkComponents.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class RenamedNetworkComponent {
	
	private NetworkComponent networkComponent = null;
	private String oldNetworkComponentID = null;
	private String newNetworkComponentID = null;
	
	/**
	 * Instantiates a new renamed network component.
	 *
	 * @param networkComponent the network component
	 * @param oldCompID the old component ID
	 * @param newCompID the new component ID
	 */
	public RenamedNetworkComponent(NetworkComponent networkComponent, String oldCompID, String newCompID) {
		this.networkComponent = networkComponent;
		this.oldNetworkComponentID = oldCompID;
		this.newNetworkComponentID = newCompID;
	}
	
	/**
	 * Gets the network component.
	 * @return the networkComponent
	 */
	public NetworkComponent getNetworkComponent() {
		return networkComponent;
	}
	/**
	 * Sets the network component.
	 * @param networkComponent the networkComponent to set
	 */
	public void setNetworkComponent(NetworkComponent networkComponent) {
		this.networkComponent = networkComponent;
	}
	
	/**
	 * Gets the old network component id.
	 * @return the oldNetworkComponentID
	 */
	public String getOldNetworkComponentID() {
		return oldNetworkComponentID;
	}
	/**
	 * Sets the old network component id.
	 * @param oldNetworkComponentID the oldNetworkComponentID to set
	 */
	public void setOldNetworkComponentID(String oldNetworkComponentID) {
		this.oldNetworkComponentID = oldNetworkComponentID;
	}
	
	/**
	 * Gets the new network component id.
	 * @return the newNetworkComponentID
	 */
	public String getNewNetworkComponentID() {
		return newNetworkComponentID;
	}
	/**
	 * Sets the new network component id.
	 * @param newNetworkComponentID the newNetworkComponentID to set
	 */
	public void setNewNetworkComponentID(String newNetworkComponentID) {
		this.newNetworkComponentID = newNetworkComponentID;
	}
	
}
