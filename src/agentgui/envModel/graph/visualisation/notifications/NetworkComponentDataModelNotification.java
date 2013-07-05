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
 * The Class NetworkComponentDataModelNotification can be used in order
 * to transfer data model changes of GraphNodes or NetworkComponents to 
 * the {@link DisplayAgent}.
 * In case that just a single part of the data model has to be updated,
 * specify the index by using the method {@link #setDataModelPartUpdateIndex(int)}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class NetworkComponentDataModelNotification extends DisplayAgentNotificationGraph {

	private static final long serialVersionUID = -4596694664751915164L;

	private GraphNode graphNode = null;
	private NetworkComponent networkComponent = null;
	
	private boolean useDataModelBase64Encoded = false;
	
	private int dataModelPartUpdateIndex = -1;
	
	
	/**
	 * Instantiates a new network component data model notification.
	 * @param networkComponent the network component
	 */
	public NetworkComponentDataModelNotification(NetworkComponent networkComponent) {
		this.networkComponent = networkComponent;
	}
	/**
	 * Instantiates a new network component data model notification.
	 * @param networkComponent the network component
	 * @param useDataModelBase64Encoded the use data model base64 encoded
	 */
	public NetworkComponentDataModelNotification(NetworkComponent networkComponent, boolean useDataModelBase64Encoded) {
		this.networkComponent = networkComponent;
		this.useDataModelBase64Encoded = useDataModelBase64Encoded;
	}
	
	/**
	 * Instantiates a new network component data model notification.
	 * @param graphNode the graph node
	 */
	public NetworkComponentDataModelNotification(GraphNode graphNode) {
		this.graphNode = graphNode;
	}
	/**
	 * Instantiates a new network component data model notification.
	 * @param graphNode the graph node
	 * @param useDataModelBase64Encoded the use data model base64 encoded
	 */
	public NetworkComponentDataModelNotification(GraphNode graphNode, boolean useDataModelBase64Encoded) {
		this.graphNode = graphNode;
	}

	/**
	 * Clarifies, if the Base64 encoded data model has to be used.
	 * @return true, if the Base64 data model has to be used
	 */
	public boolean isUseDataModelBase64Encoded() {
		return useDataModelBase64Encoded;
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
				if (this.useDataModelBase64Encoded==true) {
					// --- Case Base64 --------------------
					if (this.networkComponent.getDataModelBase64()==null) {
						return true;
					}
				} else {
					// --- Case Object --------------------
					if (this.networkComponent.getDataModel()==null) {
						return true;
					}
				}
			} else if (this.graphNode!=null) {
				// --- Case GraphNode ---------------------
				if (this.useDataModelBase64Encoded==true) {
					// --- Case Base64 --------------------
					if (this.graphNode.getDataModelBase64()==null) {
						return true;
					}
				} else {
					// --- Case Object --------------------
					if (this.graphNode.getDataModel()==null) {
						return true;
					}
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
		if (isEmpty()==false) {
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
		if (isEmpty()==false) {
			if (this.networkComponent!=null) {
				return true;
			}
		}
		return false;
	}

	
}
