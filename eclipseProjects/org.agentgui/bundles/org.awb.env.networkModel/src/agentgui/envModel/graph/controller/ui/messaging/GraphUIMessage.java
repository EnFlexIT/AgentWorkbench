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
package agentgui.envModel.graph.controller.ui.messaging;

import java.io.Serializable;

import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.controller.UIMessagingController;

/**
 * The Class GraphUIMessage serves as serializable message object that can be transferred 
 * to the {@link GraphEnvironmentController}'s {@link UIMessagingController} to display 
 * different types of messages.
 * 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class GraphUIMessage implements Serializable {

	private static final long serialVersionUID = -7759597721676701252L;

	/** The enumeration GraphUIMessageType. */
	public enum GraphUIMessageType {
		Information,
		Warning,
		Error
	}
	
	private long timeStamp;
	private GraphUIMessageType messageType;
	private String message;
	

	/**
	 * Instantiates a new empty GraphUIMessage.
	 */
	public GraphUIMessage() { }
	/**
	 * Instantiates a new GraphUIMessage with the current time stamp.
	 *
	 * @param messageType the message type
	 * @param message the message
	 */
	public GraphUIMessage(GraphUIMessageType messageType, String message) {
		this(System.currentTimeMillis(), messageType, message);
	}
	/**
	 * Instantiates a new GraphUIMessage.
	 *
	 * @param timeStamp the time stamp
	 * @param messageType the message type
	 * @param message the message
	 */
	public GraphUIMessage(long timeStamp, GraphUIMessageType messageType, String message) {
		this.setTimeStamp(timeStamp);
		this.setMessageType(messageType);
		this.setMessage(message);
	}

	/**
	 * Gets the time stamp.
	 * @return the time stamp
	 */
	public long getTimeStamp() {
		return timeStamp;
	}
	/**
	 * Sets the time stamp.
	 * @param timeStamp the new time stamp
	 */
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * Gets the message type.
	 * @return the message type
	 */
	public GraphUIMessageType getMessageType() {
		return messageType;
	}
	/**
	 * Sets the message type.
	 * @param messageType the new message type
	 */
	public void setMessageType(GraphUIMessageType messageType) {
		this.messageType = messageType;
	}

	/**
	 * Gets the message.
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * Sets the message.
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
}
