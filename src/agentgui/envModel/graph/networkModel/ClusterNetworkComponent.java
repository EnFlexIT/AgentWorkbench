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
package agentgui.envModel.graph.networkModel;

import java.util.ArrayList;

/**
 * The Class ClusterNetworkComponent.
 */
public class ClusterNetworkComponent extends NetworkComponent {

    /** The Constant CLUSTER_PREFIX. */
    public static final String CLUSTER_PREFIX = "C_";

    /** The cluster network model. */
    private NetworkModel clusterNetworkModel;

    /** The network component IDs. */
    private ArrayList<String> networkComponentIDs = new ArrayList<String>();

    /**
     * Sets the network component IDs.
     * 
     * @param networkComponentIDs the new network component IDs
     */
    public void setNetworkComponentIDs(ArrayList<String> networkComponentIDs) {
	this.networkComponentIDs = networkComponentIDs;
    }

    /**
     * Gets the network component IDs.
     * 
     * @return the network component IDs
     */
    public ArrayList<String> getNetworkComponentIDs() {
	return networkComponentIDs;
    }

    /**
     * Sets the cluster network model.
     * 
     * @param clusterNetworkModel the new cluster network model
     */
    public void setClusterNetworkModel(NetworkModel clusterNetworkModel) {
	this.clusterNetworkModel = clusterNetworkModel;
    }

    /**
     * Gets the cluster network model.
     * 
     * @return the cluster network model
     */
    public NetworkModel getClusterNetworkModel() {
	return clusterNetworkModel;
    }

    /**
     * Gets the Component represented by an Edge of the Cluster
     * 
     * @param graphNode the graph node
     * @return the receive
     */
    private NetworkComponent getReceive(GraphNode graphNode) {
	return clusterNetworkModel.getNetworkComponent(graphNode).iterator().next();
    }
}
