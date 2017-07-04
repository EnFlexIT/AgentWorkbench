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

import java.util.Vector;

/**
 * The Class DisplayAgentNotificationGraphMultiple can be used in order to apply
 * multiple {@link DisplayAgentNotificationGraph} elements to the displays.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public class DisplayAgentNotificationGraphMultiple extends DisplayAgentNotificationGraph {

	private static final long serialVersionUID = -2215484234200157434L;
	
	private Vector<DisplayAgentNotificationGraph> displayNotifications = null;
	
	/**
	 * Instantiates a new Container for a Vector of DisplayAgentNotificationGraph.
	 */
	public DisplayAgentNotificationGraphMultiple() {
	}
	/**
	 * Instantiates a new Container for a given Vector of DisplayAgentNotificationGraph..
	 * @param displayNotifications the display notifications
	 */
	public DisplayAgentNotificationGraphMultiple(Vector<DisplayAgentNotificationGraph> displayNotifications) {
		this.displayNotifications = displayNotifications;
	}
	
	/**
	 * Sets the display notifications.
	 * @param displayNotifications the new display notifications
	 */
	public void setDisplayNotifications(Vector<DisplayAgentNotificationGraph> displayNotifications) {
		this.displayNotifications = displayNotifications;
	}
	/**
	 * Gets the display notifications.
	 * @return the display notifications
	 */
	public Vector<DisplayAgentNotificationGraph> getDisplayNotifications() {
		if (displayNotifications==null) {
			displayNotifications = new Vector<DisplayAgentNotificationGraph>();
		}
		return displayNotifications;
	}

	/**
	 * Adds a single DisplayAgentNotificationGraph to the current list.
	 * @param displayNotification the display notification
	 */
	public void addDisplayNotification(DisplayAgentNotificationGraph displayNotification) {
		this.getDisplayNotifications().add(displayNotification);
	}
	/**
	 * Adds several instances of the type DisplayAgentNotificationGraph to the current list.
	 * @param displayNotifications the display notifications
	 */
	public void addDisplayNotification(Vector<DisplayAgentNotificationGraph> displayNotifications) {
		this.getDisplayNotifications().addAll(displayNotifications);
	}
	
}
