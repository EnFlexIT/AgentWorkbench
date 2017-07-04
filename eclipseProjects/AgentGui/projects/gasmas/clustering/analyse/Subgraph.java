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
package gasmas.clustering.analyse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;

import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;

/**
 * The Class Subgraph. Represents a possible Cluster with Interfaces
 */
public class Subgraph {

	/** The network components. */
	private ArrayList<String> networkComponents;

	/** The interface network components. */
	private ArrayList<String> interfaceNetworkComponents;

	private ArrayList<String> path;

	/**
	 * Instantiates a new subgraph.
	 *
	 * @param networkComponents the network components
	 * @param interfaceNetworkComponents the interface network components
	 */
	public Subgraph(ArrayList<String> networkComponents, ArrayList<String> interfaceNetworkComponents) {
		this.networkComponents = networkComponents;
		this.interfaceNetworkComponents = interfaceNetworkComponents;
	}

	/**
	 * Gets the interface network components.
	 *
	 * @return the interface network components
	 */
	public ArrayList<String> getInterfaceNetworkComponents() {
		return interfaceNetworkComponents;
	}

	/**
	 * Gets the network components as IDs
	 *
	 * @return the network components
	 */
	public ArrayList<String> getNetworkComponents() {
		return networkComponents;
	}

	/**
	 * Gets the network components.
	 *
	 * @param networkModel the network model
	 * @return the network components
	 */
	public HashSet<NetworkComponent> getNetworkComponents(NetworkModel networkModel) {
		HashSet<NetworkComponent> networkComponents = new HashSet<NetworkComponent>();
		for (String networkComponentID : this.networkComponents) {
			networkComponents.add(networkModel.getNetworkComponent(networkComponentID));
		}
		return networkComponents;
	}

	public boolean hasMultipleConnectionsOnABranch(NetworkModel networkModel){
		HashSet<NetworkComponent> multipleConnections = new HashSet<NetworkComponent>();
		ArrayList<NetworkComponent> neighbours = new ArrayList<NetworkComponent>();
		for (String id : interfaceNetworkComponents) {
			Vector<NetworkComponent> neighboursNC = networkModel.getNeighbourNetworkComponents(networkModel.getNetworkComponent(id));
			for (NetworkComponent networkComponent : neighboursNC) {
				if (neighbours.contains(networkComponent)) {
					multipleConnections.add(networkComponent);
				} else {
					neighbours.add(networkComponent);
				}
			}
		}
		for (NetworkComponent networkComponent : multipleConnections) {
			if (networkComponents.contains(networkComponent.getId())) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String string = "";
		for (String networkComponentID : networkComponents) {
			string += networkComponentID + ";";
		}
		string += "   Interfaces:";
		for (String networkComponentID : interfaceNetworkComponents) {
			string += networkComponentID + ";";
		}
		if (path != null) {
			string +=" : ";
			for (String networkComponentID : path) {
				string += networkComponentID + ";";
			}
		}
		return string;
	}

	public void setPath(ArrayList<String> path) {
		this.path = path;
	}
}
