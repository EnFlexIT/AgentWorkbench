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
import java.util.HashSet;

/**
 * The Class ClusterNetworkComponent.
 */
public class ClusterNetworkComponent extends NetworkComponent {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 682444875338053459L;

	/** The domain. which this cluster contains to */
	private String domain;
	
	/** The cluster network model. */
	private NetworkModel clusterNetworkModel;

	/** The network component IDs. */
	private ArrayList<String> networkComponentIDs = new ArrayList<String>();

	/**
	 * Instantiates a new cluster network component.
	 *
	 * @param id the id
	 * @param type the type
	 * @param prototypeClassName the prototype class name
	 * @param graphElements the graph elements
	 * @param directed the directed
	 * @param networkComponentIDs the network component i ds
	 * @param clusterNetworkModel the cluster network model
	 */
	public ClusterNetworkComponent(String id, String type, String prototypeClassName, 
			String agentClassName, HashSet<GraphElement> graphElements, boolean directed, 
			ArrayList<String> networkComponentIDs, NetworkModel clusterNetworkModel) {
		
		super(id, type, prototypeClassName, agentClassName, graphElements, directed);
		this.networkComponentIDs = networkComponentIDs;
		this.clusterNetworkModel = clusterNetworkModel;
	}

	@Override
	public ClusterNetworkComponent getCopy() {
	
		ArrayList<String> copyComponentIDs = new ArrayList<String>(this.networkComponentIDs);
		
		ClusterNetworkComponent copy = new ClusterNetworkComponent(this.id, this.type, this.prototypeClassName, 
				this.agentClassName, null, this.directed, copyComponentIDs, this.clusterNetworkModel.getCopy());
		copy.setGraphElementIDs(this.getGraphElementIDs());
		return copy;
		
		
	}
	
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
	 * Gets the size.
	 *
	 * @return the size
	 */
	public int getSize() {
		return networkComponentIDs.size();
	}

	/**
	 * Gets the Component represented by Node and of the ClusterStar.
	 *
	 * @param graphNode the graph node
	 * @return the receive
	 */
	private NetworkComponent getReceiver(GraphNode graphNode) {
		return clusterNetworkModel.getNetworkComponents(graphNode).iterator().next();
	}

	/**
	 * Sets the domain.
	 * @param domain the new domain
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}
	/**
	 * Returns the domain.
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}
}
