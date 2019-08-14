package org.awb.env.networkModel.helper;

import java.util.Vector;

import org.awb.env.networkModel.NetworkComponent;

/**
 * The Class DomainCluster serves as aggregator for a domain and the corresponding NetworkComponents.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 * 
 * @see DomainClustering
 */
public class DomainCluster implements Comparable<DomainCluster> {
	
	private String domain;
	private Vector<NetworkComponent> networkComponents;
	
	/**
	 * Instantiates a new domain cluster.
	 *
	 * @param domain the domain
	 * @param networkComponents the network components
	 */
	public DomainCluster(String domain, Vector<NetworkComponent> networkComponents) {
		if (domain==null || domain.isEmpty()==true) {
			throw new NullPointerException("[" + this.getClass().getSimpleName() + "] The domain is not allowed to be null or empty!");
		}
		if (networkComponents==null || networkComponents.size()==0) {
			throw new NullPointerException("[" + this.getClass().getSimpleName() + "] The Vector of network components is not allowed to be null or empty!");
		}
		this.setDomain(domain);
		this.setNetworkComponents(networkComponents);
	}
	
	/**
	 * Sets the domain.
	 * @param domain the new domain
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}
	/**
	 * Returns the current domain.
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}
	
	/**
	 * Sets the network components.
	 * @param networkComponents the new network components
	 */
	public void setNetworkComponents(Vector<NetworkComponent> networkComponents) {
		this.networkComponents = networkComponents;
	}
	/**
	 * Returns the NetworkComponents that belong to the current domain cluster.
	 * @return the network components
	 */
	public Vector<NetworkComponent> getNetworkComponents() {
		return networkComponents;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(DomainCluster dc) {
		
		if (dc==null) return 1;
		
		if (this.getDomain().equals(dc.getDomain())==true) {
			// --- Compare the first ID of NetworkComponents ----
			NetworkComponent firstNetCompThis = this.getNetworkComponents().get(0);
			NetworkComponent firstNetCompDC = dc.getNetworkComponents().get(0);
			return firstNetCompThis.getId().compareTo(firstNetCompDC.getId());
		}
		return this.getDomain().compareTo(dc.getDomain());
	}
}
