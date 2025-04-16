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
package org.awb.env.networkModel.controller;

import org.awb.env.networkModel.controller.ui.messaging.GraphUIMessage;
import org.awb.env.networkModel.controller.ui.messaging.MessagingJInternalFrame;
import org.awb.env.networkModel.controller.ui.messaging.TestStateMessage;
import org.awb.env.networkModel.controller.ui.messaging.GraphUIMessage.GraphUIMessageType;

/**
 * The Class UIMessagingController enables to send messages to the UI user.
 * 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class UIMessagingController {

	private boolean debug = false;
	
	private GraphEnvironmentController graphController;
	private MessagingJInternalFrame messagingFrame;
	
	/**
	 * Instantiates a new UI messaging controller.
	 * @param graphController the graph controller
	 */
	public UIMessagingController(GraphEnvironmentController graphController) {
		this.graphController = graphController;
		// --- Debugging --------------
		if (debug==true) {
			this.addTestMessages();
		}
	}
	/**
	 * Adds the test messages.
	 */
	private void addTestMessages() {

		try {
			this.addMessage(new GraphUIMessage(GraphUIMessageType.Information, "Test Message for the Developing of the message widget."));
			this.addMessage(new GraphUIMessage(GraphUIMessageType.Warning, "Warning Message for the Developing of the message widget."));
			this.addMessage(new GraphUIMessage(GraphUIMessageType.Error, "Error Message for the Developing of the message widget."));
			Thread.sleep(500);
			
			this.addMessage(new GraphUIMessage(GraphUIMessageType.Information, "Test Message for the Developing of the message widget."));
			this.addMessage(new GraphUIMessage(GraphUIMessageType.Warning, "Warning Message for the Developing of the message widget."));
			this.addMessage(new GraphUIMessage(GraphUIMessageType.Error, "Error Message for the Developing of the message widget."));
			Thread.sleep(500);
			
			this.addMessage(new GraphUIMessage(GraphUIMessageType.Information, "Test Message for the Developing of the message widget."));
			this.addMessage(new GraphUIMessage(GraphUIMessageType.Warning, "Warning Message for the Developing of the message widget."));
			this.addMessage(new GraphUIMessage(GraphUIMessageType.Error, "Error Message for the Developing of the message widget."));
			
			this.addMessage(new TestStateMessage("Test of the state panels"));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

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
	 * Adds the specified GraphUIMessage to the table of messages .
	 * @param graphUiMessage the GraphUIMessage to add
	 */
	public void addMessage(GraphUIMessage graphUiMessage) {
		this.getMessagingFrame().addMessage(graphUiMessage);
		this.showMessagingUI();
	}
	/**
	 * Adds the specified message to the table of messages.
	 *
	 * @param timeStamp the time stamp of the message
	 * @param messageType the message type
	 * @param message the message itself
	 */
	public void addMessage(long timeStamp, GraphUIMessageType messageType, String message) {
		this.getMessagingFrame().addMessage(timeStamp, messageType, message);
		this.showMessagingUI();
	}
	
}
