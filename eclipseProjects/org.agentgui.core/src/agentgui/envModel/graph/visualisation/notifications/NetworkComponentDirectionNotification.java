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
package agentgui.envModel.graph.visualisation.notifications;

import agentgui.envModel.graph.networkModel.NetworkComponent;

/**
 * The Class NetworkComponentDirectionMessage can be used in order to 
 * send a message or a notification to the DisplayAgent that will set 
 * the edge directions.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class NetworkComponentDirectionNotification extends DisplayAgentNotificationGraph {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5845301152343373848L;
	/** The network component. */
	private NetworkComponent networkComponent = null;
	
	
	/**
	 * Instantiates a new message class for the DisplayAgent.
	 * @param networkComponent the network component
	 */
	public NetworkComponentDirectionNotification(NetworkComponent networkComponent) {
		this.networkComponent = networkComponent;
	}
	
	/**
	 * Sets the network component.
	 * @param networkComponent the new network component
	 */
	public void setNetworkComponent(NetworkComponent networkComponent) {
		this.networkComponent = networkComponent;
	}
	/**
	 * Gets the network component.
	 * @return the network component
	 */
	public NetworkComponent getNetworkComponent() {
		return networkComponent;
	}
	
}
