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
package gasmas.clustering;

import java.util.HashMap;

import agentgui.envModel.graph.networkModel.NetworkComponent;

/**
 * The Class ShortestPathBlackboard.
 */
public class ShortestPathBlackboard {

    /** The hash map paths. */
    private HashMap<String, NetworkPath> hashMapPaths = new HashMap<String, NetworkPath>();

    /**
     * Adds the path.
     * 
     * @param networkComponent1
     *            the network component1
     * @param networkComponent2
     *            the network component2
     * @param networkPath
     *            the network path
     */
    public void addPath(NetworkComponent networkComponent1,
	    NetworkComponent networkComponent2, NetworkPath networkPath) {
	hashMapPaths.put(networkComponent1.getId() + networkComponent2.getId(),
		networkPath);
    }

    /**
     * Contains.
     * 
     * @param networkComponent1
     *            the network component1
     * @param networkComponent2
     *            the network component2
     * @return true, if successful
     */
    public boolean contains(NetworkComponent networkComponent1,
	    NetworkComponent networkComponent2) {
	if (hashMapPaths.containsKey(networkComponent1.getId()
		+ networkComponent2.getId())) {
	    return true;
	}
	if (hashMapPaths.containsKey(networkComponent2.getId()
		+ networkComponent1.getId())) {
	    return true;
	}
	return false;
    }

    /**
     * Gets the path.
     * 
     * @param networkComponent1
     *            the network component1
     * @param networkComponent2
     *            the network component2
     * @return the path
     */
    public NetworkPath getPath(NetworkComponent networkComponent1,
	    NetworkComponent networkComponent2) {
	NetworkPath networkPath = hashMapPaths.get(networkComponent1.getId()
		+ networkComponent2.getId());
	if (networkPath == null) {
	    networkPath = hashMapPaths.get(networkComponent2.getId()
		    + networkComponent1.getId());
	}
	return networkPath;
    }

    /**
     * Find most frequent network component.
     * 
     * TODO: Problem chooses first found component
     * 
     * @return the network component
     */
    public NetworkComponent findMostFrequentNetworkComponent() {
	HashMap<NetworkComponent, Integer> componentCounter = new HashMap<NetworkComponent, Integer>();
	for (NetworkPath networkPath : hashMapPaths.values()) {
	    for (NetworkComponent networkComponent : networkPath.getPath()) {
		componentCounter.put(networkComponent, componentCounter
			.get(networkComponent) == null ? new Integer(1)
			: new Integer(componentCounter.get(networkComponent)
				.intValue() + 1));
	    }
	}
	int max = 0;
	NetworkComponent networkComponent = null;
	for (java.util.Map.Entry<NetworkComponent, Integer> entry : componentCounter
		.entrySet()) {
	    if (entry.getValue().intValue() > max) {
		max = entry.getValue().intValue();
		networkComponent = entry.getKey();
	    }
	}
	return networkComponent;
    }
}
