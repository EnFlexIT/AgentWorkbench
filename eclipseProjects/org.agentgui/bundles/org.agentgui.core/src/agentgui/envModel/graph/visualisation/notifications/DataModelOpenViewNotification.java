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
 * The Class DataModelOpenViewNotification can be used in order to
 * open the visualisation of the data model of a NetworkComponent or GraphNode.
 */
public class DataModelOpenViewNotification extends DisplayAgentNotificationGraph {

	private static final long serialVersionUID = -3034557124142379283L;

	private String graphNodeID = null;
	private String networkComponentID = null;

	
	/**
	 * Instantiates a new data model open view notification.
	 * @param networkComponent the network component
	 */
	public DataModelOpenViewNotification(NetworkComponent networkComponent) {
		this.networkComponentID = networkComponent.getId();
	}
	/**
	 * Instantiates a new data model open view notification.
	 * @param graphNode the graph node
	 */
	public DataModelOpenViewNotification(GraphNode graphNode) {
		this.graphNodeID = graphNode.getId();
	}

	/**
	 * Gets the graph node.
	 * @return the graphNode
	 */
	public String getGraphNodeID() {
		return graphNodeID;
	}
	/**
	 * Gets the network component.
	 * @return the networkComponent
	 */
	public String getNetworkComponentID() {
		return networkComponentID;
	}
	
	/**
	 * Checks if is empty.
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		if (this.graphNodeID==null && this.networkComponentID==null) {
			return true;
		} 
		return false;
	}

	/**
	 * Checks if this is a notification for a GraphNode.
	 * @return true, if this is GraphNode notification
	 */
	public boolean isGraphNodeView() {
		if (isEmpty()==false) {
			if (this.graphNodeID!=null) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if this is a notification for a NetworkComponent .
	 * @return true, if this is a NetworkComponent notification
	 */
	public boolean isNetworkComponentView() {
		if (isEmpty()==false) {
			if (this.networkComponentID!=null) {
				return true;
			}
		}
		return false;
	}
	
}
