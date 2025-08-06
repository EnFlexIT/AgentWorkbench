package org.awb.env.networkModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.enflexit.common.SerialClone;

/**
 * The Class ClusterNetworkComponent.
 */
public class ClusterNetworkComponent extends NetworkComponent {

	private static final long serialVersionUID = 682444875338053459L;

	private String domain;
	private NetworkModel clusterNetworkModel;

	/**
	 * Instantiates a new cluster network component.
	 *
	 * @param id the id
	 * @param type the type
	 * @param graphElements the graph elements
	 * @param domain the domain
	 * @param clusterNetworkModel the cluster network model
	 */
	public ClusterNetworkComponent(String id, String type, HashSet<GraphElement> graphElements, String domain, NetworkModel clusterNetworkModel) {
		super(id, type, graphElements);
		this.setDomain(domain);
		this.setClusterNetworkModel(clusterNetworkModel);
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
	
	/**
	 * Sets the cluster network model.
	 * @param clusterNetworkModel the new cluster network model
	 */
	public void setClusterNetworkModel(NetworkModel clusterNetworkModel) {
		this.clusterNetworkModel = clusterNetworkModel;
	}
	/**
	 * Gets the cluster network model.
	 * @return the cluster network model
	 */
	public NetworkModel getClusterNetworkModel() {
		return clusterNetworkModel;
	}
	
	/**
	 * Returns all ID's of the {@link NetworkComponent} that belong to the current cluster.
	 * @return the cluster {@link NetworkComponent} ID's
	 */
	public List<String> getNetworkComponentIDs() {
		return new ArrayList<String>(this.getClusterNetworkModel().getNetworkComponents().keySet());
	}

	/**
	 * Returns all {@link NetworkComponent}s that belong to the current cluster.
	 * @return the cluster {@link NetworkComponent}s
	 */
	public List<NetworkComponent> getNetworkComponents() {
		return new ArrayList<NetworkComponent>(this.getClusterNetworkModel().getNetworkComponents().values());
	}

	/**
	 * Gets the Component represented by Node and of the ClusterStar.
	 *
	 * @param graphNode the graph node
	 * @return the receive
	 */
	public NetworkComponent getReceiver(GraphNode graphNode) {
		return clusterNetworkModel.getNetworkComponents((GraphNode) clusterNetworkModel.getGraphElement(graphNode.getId())).iterator().next();
	}
	/**
	 * Returns all connection NetworkComponents (all NetworkComonent with a FreeNode).
	 * @return all connection NetworkComponents
	 */
	public ArrayList<NetworkComponent> getConnectionNetworkComponents() {
		ArrayList<NetworkComponent> connectionNC = new ArrayList<NetworkComponent>();
		for (GraphNode graphNode : clusterNetworkModel.getGraph().getVertices()) {
			if (clusterNetworkModel.isFreeGraphNode(graphNode))
				connectionNC.addAll(clusterNetworkModel.getNetworkComponents(graphNode));
		}
		return connectionNC;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.helper.NetworkComponent#getCopy()
	 */
	@Override
	public ClusterNetworkComponent getCopy() {
		ClusterNetworkComponent copy = null;	
		try {
			copy = SerialClone.clone(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return copy;
	}
	
}
