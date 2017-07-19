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

import agentgui.envModel.graph.networkModel.NetworkModel;

/**
 * The Class EnableNetworkModelUpdateNotification can be used in order 
 * to enable or disable the update of the {@link NetworkModel} within
 * the graph visualisation.
 */
public class EnableNetworkModelUpdateNotification extends DisplayAgentNotificationGraph {

	private static final long serialVersionUID = -2559619706014927385L;

	/** The enable network model update. */
	private boolean enableNetworkModelUpdate;
	
	
	/**
	 * Instantiates a new enable network model update notification.
	 */
	public EnableNetworkModelUpdateNotification() {
	}
	/**
	 * Instantiates a new enable network model update notification.
	 * @param enableNetworkModelUpdate the enable network model update
	 */
	public EnableNetworkModelUpdateNotification(boolean enableNetworkModelUpdate) {
		this.setEnableNetworkModelUpdate(enableNetworkModelUpdate);
	}
	
	/**
	 * Checks if is enable network model update.
	 * @return true, if is enable network model update
	 */
	public boolean isEnableNetworkModelUpdate() {
		return enableNetworkModelUpdate;
	}
	/**
	 * Setter to enable or disable a network model update.
	 * @param enableNetworkModelUpdate the new enable network model update
	 */
	public void setEnableNetworkModelUpdate(boolean enableNetworkModelUpdate) {
		this.enableNetworkModelUpdate = enableNetworkModelUpdate;
	}
	
}
