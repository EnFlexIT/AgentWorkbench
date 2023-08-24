package org.awb.env.networkModel.helper;

import java.util.Collections;
import java.util.Vector;

import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.settings.ComponentTypeSettings;
import org.awb.env.networkModel.settings.DomainSettings;
import org.awb.env.networkModel.settings.GeneralGraphSettings4MAS;
import org.awb.env.networkModel.settings.GeneralGraphSettings4MAS.ComponentSorting;

/**
 * The Class DomainClustering enables to cluster a {@link NetworkModel} along the specified 'domains' (or sub networks)  
 * that can be found in the {@link GeneralGraphSettings4MAS}. Therefore, the search algorithm searches for connected
 * {@link NetworkComponent}s. As extended Vector of {@link DomainCluster}, the results will be stored locally. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 * 
 * @see NetworkModel
 * @see GeneralGraphSettings4MAS
 * @see GeneralGraphSettings4MAS#getDomainSettings()
 * @see DomainSettings
 */
public class DomainClustering extends Vector<DomainCluster> {

	private static final long serialVersionUID = 6986299395693300427L;

	private NetworkModel networkModel;
	private boolean isPrintClusterResult = false;

	
	/**
	 * Instantiates a new domain cluster that will create the cluster.
	 * @param networkModel the network model
	 */
	public DomainClustering(NetworkModel networkModel) {
		this(networkModel, false);
	}
	/**
	 * Instantiates a new domain cluster that will create the cluster.
	 *
	 * @param networkModel the network model
	 * @param isPrintClusterResult the is print cluster result
	 */
	public DomainClustering(NetworkModel networkModel, boolean isPrintClusterResult) {
		this.networkModel = networkModel;
		this.setPrintClusterResult(isPrintClusterResult);
		if (this.networkModel==null) {
			throw new NullPointerException("[" + this.getClass().getSimpleName() + "] The NetworkModel is not allowed to be null!");
		} else {
			this.create();
		}	
	}
	
	/**
	 * Creates the domain clustering and saves the result into this Vector.
	 */
	public void create() {
		
		// --- Clear old result elements (if any) -----------------------------
		this.clear();
		
		// --- Remind current component sorting -------------------------------
		ComponentSorting cSorting = this.networkModel.getGeneralGraphSettings4MAS().getComponentSorting();
		
		// --- Examine the NetworkModel ---------------------------------------
		Vector<NetworkComponent> netCompVector = this.getNetworkModel().getNetworkComponentVectorSorted();
		for (int i = 0; i < netCompVector.size(); i++) {
			
			NetworkComponent netComp = netCompVector.get(i);
			String domain = this.getDomain(netComp);
			// --- Check if already found and part of a known cluster ---------
			if (this.getDomainCluster(domain, netComp)==null) {
				// --- Create a new domain cluster ----------------------------
				Vector<NetworkComponent> netCompDomainCluster = this.createDomainCluster(netComp);
				DomainCluster newDomainCluster = new DomainCluster(domain, netCompDomainCluster, cSorting);
				this.add(newDomainCluster);
			}
		}
		// --- Finally, sort the resulting domain cluster ---------------------
		Collections.sort(this);
		
		// --- Print the result? ----------------------------------------------
		if (this.isPrintClusterResult()==true) {
			for (int i = 0; i < this.size(); i++) {
				DomainCluster dCluster = this.get(i);
				System.out.println("[" + this.getClass().getSimpleName() + "] Found " + dCluster.getNetworkComponents().size() + " components for domain " + dCluster.getDomain());
			}
		}
		
	}
	
	/**
	 * Returns the cluster of NetworkComponents that can be found for the domain of the specified NetworkComponent.
	 *
	 * @param netComp the NetworkComponent
	 * @return the domain cluster
	 */
	protected Vector<NetworkComponent> createDomainCluster(NetworkComponent netComp) {
		
		// --- Create cluster vector ------------
		Vector<NetworkComponent> domainCluster = new Vector<>(); 
		domainCluster.add(netComp);
		// --- Deep search for domain member ----
		this.deepSearchDomainNeighbours(netComp, this.getDomain(netComp), domainCluster);
		// --- Sort the result ------------------
		Collections.sort(domainCluster);
		
		return domainCluster;
	}
	
	/**
	 * Determines the domain specific neighbors of the specified NetworkComponent and places the result into the specified vector.
	 *
	 * @param netComp the NetworkComponent that serves as the search base for direct neighbors
	 * @param targetDomain the target domain
	 * @param domainNeighbours the already known domain neighbors
	 */
	protected void deepSearchDomainNeighbours(NetworkComponent netComp, String targetDomain, Vector<NetworkComponent> domainNeighbours) {
		
		// --- Get the neighbor components of the current NetworkComponetn ----
		Vector<NetworkComponent> neighbourNetCompVector = this.getNetworkModel().getNeighbourNetworkComponents(netComp);
		for (int i = 0; i < neighbourNetCompVector.size(); i++) {
			// --- Check each neighbor found ----------------------------------
			NetworkComponent neighbourNetComp = neighbourNetCompVector.get(i);
			// --- If already part of the result continue loop ---------------- 
			if (domainNeighbours.contains(neighbourNetComp)==true) continue;
			
			// --- Get domain connections of the neighbor found --------------- 
			Vector<String> domainConnections = this.getNetworkComponentsDomainConnections(neighbourNetComp);
			if (domainConnections.contains(targetDomain)==true) {
				// --- Add as a domain neighbor -------------------------------
				domainNeighbours.add(neighbourNetComp);
				// --- Check for further neighbors of that NetworkComponent ---   
				this.deepSearchDomainNeighbours(neighbourNetComp, targetDomain, domainNeighbours);
				
			}
		}
	}
	/**
	 * Returns all domains to which a NetworkComponent is connected to.
	 *
	 * @param netComp the NetworkComponent
	 * @return the network components domain connections
	 */
	protected Vector<String> getNetworkComponentsDomainConnections(NetworkComponent netComp) {
		
		Vector<String> domainConnections = new Vector<>();
		domainConnections.add(this.getDomain(netComp));
		
		// --- Get the neighbors of the component -------------------------
		if (this.getNetworkModel().isDistributionNode(netComp)==true) {
			Vector<NetworkComponent> neighbourNetCompVector = this.getNetworkModel().getNeighbourNetworkComponents(netComp);
			for (int i = 0; i < neighbourNetCompVector.size(); i++) {
				NetworkComponent neighbourNetComp = neighbourNetCompVector.get(i);
				String neighbourDomain = this.getDomain(neighbourNetComp);
				if (domainConnections.contains(neighbourDomain)==false) {
					domainConnections.add(neighbourDomain);
				}
			}
		}
		return domainConnections;
	}
	
	// --------------------------------------------------------------
	// --- From here some help methods can be found -----------------
	// --------------------------------------------------------------
	/**
	 * Returns the local NetworkModel.
	 * 
	 * @return the network model
	 */
	protected NetworkModel getNetworkModel() {
		return this.networkModel;
	}
	/**
	 * Returns the GeneralGraphSettings4MAS.
	 * 
	 * @return the general graph settings 4 MAS
	 */
	protected GeneralGraphSettings4MAS getGeneralGraphSettings4MAS() {
		return this.getNetworkModel().getGeneralGraphSettings4MAS();
	}
	/**
	 * Returns the {@link ComponentTypeSettings} for a {@link NetworkComponent}.
	 *
	 * @param netComp the NetworkComponent
	 * @return the component type settings
	 */
	protected ComponentTypeSettings getComponentTypeSettings(NetworkComponent netComp) {
		return this.getGeneralGraphSettings4MAS().getCurrentCTS().get(netComp.getType());
	}
	/**
	 * Returns the domain for the current NetworkComponent.
	 *
	 * @param netComp the NetworkComponent
	 * @return the domain
	 */
	protected String getDomain(NetworkComponent netComp) {
		ComponentTypeSettings cts = this.getComponentTypeSettings(netComp);
		if (cts!=null) {
			return cts.getDomain();
		}
		return null;
	}

	
	/**
	 * Sets to print (or not) the cluster result.
	 * @param isPrintClusterResult the new prints the cluster result
	 */
	public void setPrintClusterResult(boolean isPrintClusterResult) {
		this.isPrintClusterResult = isPrintClusterResult;
	}
	/**
	 * Checks if the cluster result has to be printed.
	 * @return true, if is prints the cluster result
	 */
	public boolean isPrintClusterResult() {
		return isPrintClusterResult;
	}
	
	
	// --------------------------------------------------------------
	// --- From here some query methods -----------------------------
	// --------------------------------------------------------------
	/**
	 * Returns the domain cluster for the specified domain and the specified NetworkComponent.
	 *
	 * @param domain the domain
	 * @param netComp the NetworkComponent to check for
	 * @return the domain cluster
	 */
	public DomainCluster getDomainCluster(String domain, NetworkComponent netComp) {
		for (int i = 0; i < this.size(); i++) {
			DomainCluster dCluster = this.get(i);
			if (dCluster.getDomain().equals(domain) && dCluster.getNetworkComponents().contains(netComp)==true) {
				return dCluster;
			}
		}
		return null;
	}
	/**
	 * Returns the {@link DomainCluster} that match with the specified by domain.
	 *
	 * @param domain the domain
	 * @return the domain cluster by domain
	 */
	public Vector<DomainCluster> getDomainClusterByDomain(String domain) {
		
		Vector<DomainCluster> dcVectorFound = new Vector<>();
		for (int i = 0; i < this.size(); i++) {
			DomainCluster dcCheck = this.get(i);
			if (dcCheck.getDomain().equals(domain)==true) {
				dcVectorFound.add(dcCheck);
			}
		}
		return dcVectorFound;
	}
	
}
