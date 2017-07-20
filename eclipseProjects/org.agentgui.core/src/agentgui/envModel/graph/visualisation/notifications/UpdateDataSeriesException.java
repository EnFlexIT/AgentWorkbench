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

import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;

/**
 * The Class UpdateDataSeriesException.
 */
public class UpdateDataSeriesException extends Exception {

	private static final long serialVersionUID = 3574397356538239222L;

	/**
	 * Instantiates a new update data series exception.
	 * @param message the message
	 */
	public UpdateDataSeriesException(String message) {
		super(message);
	}
	
	/**
	 * Instantiates a new update data series exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public UpdateDataSeriesException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Instantiates a new update data series exception.
	 *
	 * @param targetDataModelIndex the target data model index
	 * @param compTypeGraphNodeOrNetworkComponent the current component type, which is either a {@link GraphNode} or a {@link NetworkComponent}
	 * @param compID the ID of the component
	 * @param cause the cause
	 */
	public UpdateDataSeriesException(int targetDataModelIndex, String compTypeGraphNodeOrNetworkComponent, String compID, Throwable cause) {
		super("Error while trying to apply action 'UpdateDataSeries' to data model index " + targetDataModelIndex + " of " + compTypeGraphNodeOrNetworkComponent + " '" + compID + "':", cause);
	}
	
}
