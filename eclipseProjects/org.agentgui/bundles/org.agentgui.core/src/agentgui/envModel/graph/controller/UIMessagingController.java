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
package agentgui.envModel.graph.controller;

import agentgui.envModel.graph.controller.ui.messaging.MessagingJInternalFrame;

/**
 * The Class UIMessagingController enables to send messages to the UI user.
 * 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class UIMessagingController {

	private GraphEnvironmentController graphController;
	
	private MessagingJInternalFrame messagingFrame;
	
	/**
	 * Instantiates a new UI messaging controller.
	 * @param graphController the graph controller
	 */
	public UIMessagingController(GraphEnvironmentController graphController) {
		this.graphController = graphController;
	}
	
	/**
	 * Checks if the UI is available.
	 * @return true, if the UI is available
	 */
	private boolean isAvailableUI() {
		return this.graphController.getGraphEnvironmentControllerGUI()!=null;
	}
	
	/**
	 * Shows the messaging UI.
	 */
	public void showMessagingUI() {
		if (isAvailableUI()==false) return;
		this.getMessagingFrame().registerAtDesktopAndSetVisible();
	}
	/**
	 * Returns the messaging frame.
	 * @return the messaging frame
	 */
	private MessagingJInternalFrame getMessagingFrame() {
		if (messagingFrame==null) {
			messagingFrame = new MessagingJInternalFrame(this.graphController);
		}
		return messagingFrame;
	}
	
}
