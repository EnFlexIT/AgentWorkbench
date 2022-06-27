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

import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.visualisation.DisplayAgent;

/**
 * The Class DataModelNotification can be used in order to transfer data model 
 * changes of GraphNodes or NetworkComponents to the {@link DisplayAgent}.
 * In case that just a single part of the data model has to be updated,
 * specify the index by using the method {@link #setDataModelPartUpdateIndex(int)}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class DataModelNotification extends DisplayAgentNotificationGraph {

	private static final long serialVersionUID = -4596694664751915164L;

	private GraphNode graphNode;
	private NetworkComponent networkComponent;
	
	private int dataModelPartUpdateIndex = -1;
	
	
	/**
	 * Instantiates a new data model notification. Be aware that with this constructor, no instance copy will be made
	 * and that this may result to conflict between internal and visualised data model. 
	 * @param networkComponent the network component
	 */
	public DataModelNotification(NetworkComponent networkComponent) {
		this.networkComponent = networkComponent;
	}
	/**
	 * Instantiates a new data model notification. Be aware that with this constructor, no instance copy will be made
	 * and that this may result to conflict between internal and visualized data model.
	 *
	 * @param graphNode the graph node
	 */
	public DataModelNotification(GraphNode graphNode) {
		this.graphNode = graphNode;
	}
	
	
	/**
	 * Sets the index of the data model that has to be updated.
	 * @param dataModelPartUpdateIndex the new data model part update index
	 */
	public void setDataModelPartUpdateIndex(int dataModelPartUpdateIndex) {
		this.dataModelPartUpdateIndex = dataModelPartUpdateIndex;
	}
	/**
	 * Gets the data model part update index.
	 * @return the data model part update index
	 */
	public int getDataModelPartUpdateIndex() {
		return dataModelPartUpdateIndex;
	}

	/**
	 * Gets the graph node.
	 * @return the graphNode
	 */
	public GraphNode getGraphNode() {
		return graphNode;
	}
	/**
	 * Gets the network component.
	 * @return the networkComponent
	 */
	public NetworkComponent getNetworkComponent() {
		return networkComponent;
	}
	
	/**
	 * Checks if is empty.
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		if (this.graphNode==null && this.networkComponent==null) {
			return true;
		} else {
			if (this.networkComponent!=null) {
				// --- Case NetworkComponent --------------
				if (this.networkComponent.getDataModel()==null) {
					return true;
				}
				
			} else if (this.graphNode!=null) {
				// --- Case GraphNode ---------------------
				if (this.graphNode.getDataModel()==null) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Checks if this is a graph node configuration.
	 * @return true, if is graph node configuration
	 */
	public boolean isGraphNodeConfiguration() {
		if (this.isEmpty()==false) {
			if (this.graphNode!=null) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Checks if this is a network component configuration.
	 * @return true, if is network component configuration
	 */
	public boolean isNetworkComponentConfiguration() {
		if (this.isEmpty()==false) {
			if (this.networkComponent!=null) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the local settings are designated for a specified NetworkComponent.
	 *
	 * @param networkComponent the NetworkComponent
	 * @return true, if is for network component
	 */
	public boolean isForNetworkComponent(NetworkComponent networkComponent) {
		if (networkComponent==null) return false;
		if (this.networkComponent==null) return false;
		if (this.networkComponent.getId().equals(networkComponent.getId())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if the local settings are designated for a specified GraphNode.
	 *
	 * @param graphNode the GraphNode
	 * @return true, if is for graph node
	 */
	public boolean isForGraphNode(GraphNode graphNode) {
		if (graphNode==null) return false;
		if (this.graphNode==null) return false;
		if (this.graphNode.getId().equals(graphNode.getId())) {
			return true;
		} else {
			return false;
		}
	}
	
}
