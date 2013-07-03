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
package agentgui.envModel.graph.visualisation;

import jade.core.AID;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.GraphElementLayout;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;
import agentgui.envModel.graph.visualisation.notifications.DisplayAgentNotificationGraph;
import agentgui.envModel.graph.visualisation.notifications.DisplayAgentNotificationGraphMultiple;
import agentgui.envModel.graph.visualisation.notifications.GraphLayoutNotification;
import agentgui.envModel.graph.visualisation.notifications.NetworkComponentDirectionNotification;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * The Class DisplayAgentNotificationHandler is used by the {@link DisplayAgent}
 * and applies the {@link DisplayAgentNotificationGraph}'s to current visualisation.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public class DisplayAgentNotificationHandler {


	
	/**
	 * Instantiates a new display agent notification handler.
	 */
	public DisplayAgentNotificationHandler() {
	}
	
	/**
	 * Translates a display notification into a visual representation.
	 * @param environmentNotification the new display notification
	 */
	public EnvironmentNotification setDisplayNotification(GraphEnvironmentController graphController, EnvironmentNotification notification) {
		
		AID senderAID = notification.getSender();
		
		if (notification.getNotification() instanceof DisplayAgentNotificationGraphMultiple) {
			// ------------------------------------------------------
			// --- Work on multiple notifications -------------------
			DisplayAgentNotificationGraphMultiple displayNotifications = (DisplayAgentNotificationGraphMultiple) notification.getNotification();
			for (int i = 0; i < displayNotifications.getDisplayNotifications().size(); i++) {
				// --- Work on a single notification ----------------
				DisplayAgentNotificationGraph displayNotification =  displayNotifications.getDisplayNotifications().get(i);
				try {
					// --- Try to apply the current settings --------
					this.doDisplayAction(graphController, senderAID, displayNotification);
					
				} catch (Exception ex) {
					System.out.println("=> Error in DisplayAgent!");
					ex.printStackTrace();
				}
			}
			
		} else if (notification.getNotification() instanceof DisplayAgentNotificationGraph) {
			// ------------------------------------------------------
			// --- Work on a single notification --------------------
			DisplayAgentNotificationGraph displayNotification = (DisplayAgentNotificationGraph) notification.getNotification();
			try {
				// --- Try to apply the current settings ------------
				this.doDisplayAction(graphController, senderAID, displayNotification);
				
			} catch (Exception ex) {
				System.out.println("=> Error in DisplayAgent!");
				ex.printStackTrace();
			}
			
		} 
		return notification;
	}
	
	/**
	 * Do the concrete display action.
	 *
	 * @param senderAID the sender aid
	 * @param displayNotification the GraphDisplayAgentNotification
	 */
	private void doDisplayAction(GraphEnvironmentController graphController, AID senderAID, DisplayAgentNotificationGraph displayNotification) {
		
		if (displayNotification instanceof NetworkComponentDirectionNotification) {
			// ------------------------------------------------------
			// --- Set directions of the current NetworkComponent ---
			// ------------------------------------------------------
			NetworkComponentDirectionNotification netCompDirection = (NetworkComponentDirectionNotification) displayNotification;
			NetworkComponent netComp = netCompDirection.getNetworkComponent();
			graphController.getNetworkModel().setDirectionsOfNetworkComponent(netComp);

		} else if (displayNotification instanceof GraphLayoutNotification) {
			// ------------------------------------------------------
			// --- Set the layout changes to the GraphElements ------
			// ------------------------------------------------------
			GraphLayoutNotification layoutNotifications = (GraphLayoutNotification) displayNotification;
			for (int i = 0; i < layoutNotifications.getGraphElementLayouts().size(); i++) {
				
				GraphElementLayout graphElementLayout = layoutNotifications.getGraphElementLayouts().get(i);
				String graphElementID = graphElementLayout.getGraphElement().getId();
				
				// --- Get the local element and apply the layout ---
				GraphElement localGrahElement = graphController.getNetworkModel().getGraphElement(graphElementID);
				graphElementLayout.setGraphElement(localGrahElement);
				localGrahElement.setGraphElementLayout(graphElementLayout);
			}

		}
		// --- Repaint the Graph ------------------------------------
		graphController.notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Repaint));

	}
	
}
