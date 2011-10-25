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

import jade.core.AID;

import java.io.Serializable;

import agentgui.simulationService.SimulationService;

/**
 * This class is used as generalized notification to agents, that are using 
 * the {@link SimulationService}. 
 *  
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class EnvironmentNotification implements Serializable {

	private static final long serialVersionUID = -5953628202535502036L;

	private AID sender;
	private boolean comingFromManager;
	private Object notification;
	
	/**
	 * Instantiates a new environment notification.
	 *
	 * @param senderAID the senders AID
	 * @param comingFromManager true, if this message comes from the manager of a simulation
	 * @param notification the generalized notification
	 */
	public EnvironmentNotification(AID senderAID, boolean comingFromManager, Object notification) {
		this.sender = senderAID;
		this.comingFromManager = comingFromManager;
		this.notification = notification;
	}
	
	/**
	 * Sets the sender.
	 * @param sender the sender to set
	 */
	public void setSender(AID sender) {
		this.sender = sender;
	}
	/**
	 * Gets the sender.
	 * @return the sender
	 */
	public AID getSender() {
		return sender;
	}
	
	/**
	 * Sets that the notification is the coming from the manager.
	 * @param comingFromManager the commingFromManager to set
	 */
	public void setComingFromManager(boolean comingFromManager) {
		this.comingFromManager = comingFromManager;
	}
	/**
	 * Checks if the notification is coming from the manager.
	 * @return the commingFromManager
	 */
	public boolean isComingFromManager() {
		return comingFromManager;
	}

	/**
	 * Sets the notification.
	 * @param notification the notification to set
	 */
	public void setNotification(Object notification) {
		this.notification = notification;
	}
	/**
	 * Gets the notification.
	 * @return the notification
	 */
	public Object getNotification() {
		return notification;
	}
	
}
