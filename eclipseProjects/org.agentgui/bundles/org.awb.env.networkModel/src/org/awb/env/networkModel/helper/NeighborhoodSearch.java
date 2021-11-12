package org.awb.env.networkModel.helper;

import java.util.Vector;

import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.NetworkModel;

/**
 * The Class NeighborhoodSearch enables to search in the specified NetworkModel for specific neighbor components.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class NeighborhoodSearch {

	private NetworkModel networkModel;
	private NetworkComponent sourceNetComp;

	
	/**
	 * Instantiates a new NeighborhoodSearch.
	 *
	 * @param networkModel the network model
	 * @param sourceComponentID the source component ID
	 */
	public NeighborhoodSearch(NetworkModel networkModel, String sourceComponentID) {
		if (networkModel==null) {
			throw new IllegalArgumentException("The network model is not allowed to be null!");
		}
		if (sourceComponentID==null || sourceComponentID.isEmpty()==true) {
			throw new IllegalArgumentException("The source network component ID is not allowed to be null!");
		}
		this.networkModel = networkModel;
		this.setSourceNetworkComponent(this.networkModel.getNetworkComponent(sourceComponentID));
	}
	
	/**
	 * Instantiates a new NeighborhoodSearch.
	 *
	 * @param networkModel the network model
	 * @param sourceComponentID the source component ID
	 */
	public NeighborhoodSearch(NetworkModel networkModel, NetworkComponent srcNetComp) {
		if (networkModel==null) {
			throw new IllegalArgumentException("The network model is not allowed to be null!");
		}
		if (srcNetComp==null) {
			throw new IllegalArgumentException("The source network component is not allowed to be null!");
		}
		this.networkModel = networkModel;
		this.setSourceNetworkComponent(srcNetComp);
	}
	
	
	/**
	 * Returns the NetworkComponent of the specified source NetworkComponent.
	 * @return the network component
	 */
	public NetworkComponent getSourceNetworkComponent() {
		return sourceNetComp;
	}
	/**
	 * Sets the source network component.
	 * @param srcNetComp the new source network component
	 */
	public void setSourceNetworkComponent(NetworkComponent srcNetComp) {
		sourceNetComp = srcNetComp;
	}
	
	/**
	 * Returns the next neighbor network components of the specified type.
	 *
	 * @param componentType the component type
	 * @return the next network component
	 */
	public Vector<NetworkComponent> getNextNeighborNetworkComponent(String componentType) {
		
		// --- Get the current NetworkModel and find the next Sensor ------
		Vector<NetworkComponent> neighbors = this.networkModel.getNeighbourNetworkComponents(this.getSourceNetworkComponent());
		Vector<NetworkComponent> compsFound = this.filterForComponent(neighbors, componentType);
		
		int neighboursSizeLast = neighbors.size();
		while (compsFound.size()==0) {
			neighbors = this.networkModel.getNeighbourNetworkComponents(neighbors);
			compsFound = this.filterForComponent(neighbors, componentType);
			// --- Exit without result? -----
			if (neighbors.size()==neighboursSizeLast) {
				break;
			} else {
				neighboursSizeLast = neighbors.size(); 
			}
		}
		return compsFound;
	}
	/**
	 * Filters the specified list of NetworkComponents for specified component types.
	 *
	 * @param netCompList the list of NetworkComponent to search in
	 * @param componentType the component type to search for
	 * @return the filtered vector
	 */
	private Vector<NetworkComponent> filterForComponent(Vector<NetworkComponent> netCompList, String componentType) {
		
		Vector<NetworkComponent> compsFound = new Vector<>();
		for (int i = 0; i < netCompList.size(); i++) {
			NetworkComponent netComp = netCompList.get(i);
			if (netComp.getType().equals(componentType)==true) {
				compsFound.add(netComp); 
			}
		}
		return compsFound;
	}
	
}
