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
package org.awb.env.networkModel.visualisation.notifications;

import org.awb.env.networkModel.controller.ui.messaging.GraphUIMessage;

/**
 * The Class UIMessage can be used to send UI message to the GraphEnvironment.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class UIMessage extends DisplayAgentNotificationGraph {

	private static final long serialVersionUID = 6696715442945396770L;

	private GraphUIMessage graphUIMessage;
	
	/**
	 * Instantiates a new UI message.
	 * @param graphUIMessage the graph UI message
	 */
	public UIMessage(GraphUIMessage graphUIMessage) {
		this.setGraphUIMessage(graphUIMessage);
	}
	
	/**
	 * Sets the graph UI message.
	 * @param graphUIMessage the new graph UI message
	 */
	public void setGraphUIMessage(GraphUIMessage graphUIMessage) {
		this.graphUIMessage = graphUIMessage;
	}
	/**
	 * Gets the graph UI message.
	 * @return the graph UI message
	 */
	public GraphUIMessage getGraphUIMessage() {
		return graphUIMessage;
	}
	
}
