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
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.visualisation.DisplayAgent;


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

	private GraphNode graphNode = null;
	private NetworkComponent networkComponent = null;
	
	private boolean useDataModelBase64Encoded = false;
	
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
	 * Instantiates a new data model notification for a NetworkComponent.
	 *
	 * @param networkComponent the NetworkComponent that contains the data model
	 * @param networkModel the current network model
	 * @see NetworkComponent#getCopy(agentgui.envModel.graph.networkModel.NetworkModel)
	 */
	public DataModelNotification(NetworkComponent networkComponent, NetworkModel networkModel) {
		NetworkComponentAdapter4DataModel adapter4DataModel = networkModel.getNetworkComponentAdapter(null, networkComponent).getStoredDataModelAdapter();
		networkComponent.setDataModelBase64(adapter4DataModel.getDataModelBase64Encoded(networkComponent.getDataModel()));
		this.networkComponent = networkComponent.getCopy(networkModel);
	}
	/**
	 * Instantiates a new data model notification for a NetworkComponent.
	 *
	 * @param networkComponent the NetworkComponent that contains the data model
	 * @param networkModel the current network model
	 * @param useDataModelBase64Encoded the use data model base64 encoded
	 * @see NetworkComponent#getCopy(agentgui.envModel.graph.networkModel.NetworkModel)
	 */
	public DataModelNotification(NetworkComponent networkComponent, NetworkModel networkModel, boolean useDataModelBase64Encoded) {
		this.networkComponent = networkComponent.getCopy(networkModel);
		this.useDataModelBase64Encoded = useDataModelBase64Encoded;
	}
	
	
	/**
	 * Instantiates a new data model notification. Be aware that with this constructor, no instance copy will be made
	 * and that this may result to conflict between internal and visualised data model.
	 *
	 * @param graphNode the graph node
	 */
	public DataModelNotification(GraphNode graphNode) {
		this.graphNode = graphNode;
	}
	/**
	 * Instantiates a new data model notification for a GraphNode.
	 *
	 * @param graphNode the GraphNode that contains the data model
	 * @param networkModel the current network model
	 * @see GraphNode#getCopy(agentgui.envModel.graph.networkModel.NetworkModel)
	 */
	public DataModelNotification(GraphNode graphNode, NetworkModel networkModel) {
		NetworkComponentAdapter4DataModel adapter4DataModel = networkModel.getNetworkComponentAdapter(null, graphNode).getStoredDataModelAdapter();
		graphNode.setDataModelBase64(adapter4DataModel.getDataModelBase64Encoded(graphNode.getDataModel()));
		this.graphNode = graphNode.getCopy(networkModel);
	}
	/**
	 * Instantiates a new data model notification for a GraphNode.
	 *
	 * @param graphNode the GraphNode that contains the data model
	 * @param networkModel the current network model
	 * @param useDataModelBase64Encoded the use data model base64 encoded
	 * @see GraphNode#getCopy(agentgui.envModel.graph.networkModel.NetworkModel)
	 */
	public DataModelNotification(GraphNode graphNode, NetworkModel networkModel, boolean useDataModelBase64Encoded) {
		this.graphNode = graphNode.getCopy(networkModel);
		this.useDataModelBase64Encoded = useDataModelBase64Encoded;
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
