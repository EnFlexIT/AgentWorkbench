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
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;
import agentgui.envModel.graph.visualisation.notifications.GraphDisplayAgentNotification;
import agentgui.envModel.graph.visualisation.notifications.NetworkComponentDirectionNotification;
import agentgui.simulationService.transaction.EnvironmentNotification;

/**
 * The Class DisplayAgentNotificationHandler is used by the {@link DisplayAgent}
 * and applies the {@link GraphDisplayAgentNotification}'s to current visualisation.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public class DisplayAgentNotificationHandler {

	private DisplayAgent myDisplayAgent = null;
	
	
	/**
	 * Instantiates a new display agent notification handler.
	 * @param displayAgent the display agent
	 */
	public DisplayAgentNotificationHandler(DisplayAgent displayAgent) {
		this.myDisplayAgent = displayAgent;
	}
	
	/**
	 * Translates a display notification into a visual representation.
	 * @param environmentNotification the new display notification
	 */
	public EnvironmentNotification setDisplayNotification(EnvironmentNotification notification) {
		
		AID senderAID = notification.getSender();
		GraphDisplayAgentNotification displayNotification = (GraphDisplayAgentNotification) notification.getNotification();
		try {
			// --- Try to apply the current settings ------
			this.doDisplayAction(senderAID, displayNotification);
			
		} catch (Exception ex) {
			System.out.println("Error in DisplayAgent '" + this.myDisplayAgent.getLocalName() + "':");
			ex.printStackTrace();
		}
		return notification;
	}
	
	/**
	 * Do display action.
	 *
	 * @param senderAID the sender aid
	 * @param displayNotification the GraphDisplayAgentNotification
	 */
	private void doDisplayAction(AID senderAID, GraphDisplayAgentNotification displayNotification) {
		
		if (displayNotification instanceof NetworkComponentDirectionNotification) {
			// --- Set directions of the current NetworkComponent --- 
			NetworkComponentDirectionNotification netCompDirection = (NetworkComponentDirectionNotification) displayNotification;
			NetworkComponent netComp = netCompDirection.getNetworkComponent();
			this.myDisplayAgent.getNetworkModel().setDirectionsOfNetworkComponent(netComp);

		}
		this.myDisplayAgent.getGraphEnvironmentController().notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Repaint));

	}
	
}
