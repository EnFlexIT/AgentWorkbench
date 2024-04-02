package org.awb.env.networkModel.helper;

import java.util.HashSet;
import java.util.Vector;

import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.settings.GeneralGraphSettings4MAS.ComponentSorting;

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
	private ComponentSorting componentSorting;
	private HashSet<String> networkComponentIDHashSet;
	
	/**
	 * Instantiates a new domain cluster.
	 *
	 * @param domain the domain
	 * @param networkComponents the network components
	 */
	public DomainCluster(String domain, Vector<NetworkComponent> networkComponents, ComponentSorting componentSorting) {
		if (domain==null || domain.isEmpty()==true) {
			throw new NullPointerException("[" + this.getClass().getSimpleName() + "] The domain is not allowed to be null or empty!");
		}
		if (networkComponents==null || networkComponents.size()==0) {
			throw new NullPointerException("[" + this.getClass().getSimpleName() + "] The Vector of network components is not allowed to be null or empty!");
		}
		this.setDomain(domain);
		this.setNetworkComponents(networkComponents);
		this.setComponentSorting(componentSorting);
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
	
	/**
	 * Sets the component sorting.
	 * @param componentSorting the new component sorting
	 */
	public void setComponentSorting(ComponentSorting componentSorting) {
		this.componentSorting = componentSorting;
	}
	/**
	 * Returns the component sorting.
	 * @return the component sorting
	 */
	public ComponentSorting getComponentSorting() {
		if (componentSorting==null) {
			componentSorting = ComponentSorting.Alphabetical;
		}
		return componentSorting;
	}
	
	
	/**
	 * Returns the network component ID hash set.
	 * @return the network component ID hash set
	 */
	private HashSet<String> getNetworkComponentIDHashSet() {
		if (networkComponentIDHashSet==null && this.getNetworkComponents()!=null) {
			networkComponentIDHashSet = new HashSet<String>();
			for (int i=0; i<this.getNetworkComponents().size(); i++) {
				networkComponentIDHashSet.add(this.getNetworkComponents().get(i).getId());
			}
		}
		return networkComponentIDHashSet;
	}
	
	/**
	 * Checks if the network component with the specified ID is part of this domain cluster.
	 * @param networkComponentID the network component ID
	 * @return true, if is part of domain cluster
	 */
	public boolean isPartOfDomainCluster(String networkComponentID) {
		if (this.getNetworkComponentIDHashSet()!=null) {
			return this.getNetworkComponentIDHashSet().contains(networkComponentID);
		}
		return false;
	}
	
	/**
	 * Checks if the specified network component is part of this domain cluster.
	 * @param networkComponent the network component
	 * @return true, if is part of domain cluster
	 */
	public boolean isPartOfDomainCluster(NetworkComponent networkComponent) {
		if (this.getNetworkComponents()!=null) {
			return this.getNetworkComponents().contains(networkComponent);
		}
		return false;
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
			
			if (DomainCluster.this.getComponentSorting()==ComponentSorting.Alphanumeric) {
				Long ncID1 = this.parseLong(firstNetCompThis.getId());
				Long ncID2 = this.parseLong(firstNetCompDC.getId());
				if (ncID1!=null && ncID2!=null) {
					return ncID1.compareTo(ncID2);
				} else if (ncID1==null & ncID2!=null) {
					return -1;
				} else if (ncID1!=null & ncID2==null) {
					return 1;
				}							
			}
			return firstNetCompThis.getId().compareTo(firstNetCompDC.getId());
		}
		return this.getDomain().compareTo(dc.getDomain());
	}
	
	/**
	 * Parses the specified long.
	 *
	 * @param netCompID the net comp ID
	 * @return the long
	 */
	private Long parseLong(String netCompID) {
		Long longValue = null;
		String ncIdNumberString = netCompID.replaceAll("\\D+","");
		if (ncIdNumberString.length()>0) {
			try {
				longValue = Long.parseLong(ncIdNumberString);
			} catch (NumberFormatException nfEx) {
				// nfEx.printStackTrace();
			}
		}
		return longValue;
	}
	
}
