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

import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;

/**
 * The Class NetworkAnalysisFunctions has function used in different Clustering
 * Algorithms doesn't need to be initialized
 */
public abstract class NetworkAnalysisFunctions {

    /**
     * Gets the network component by graph element ID identification
     * 
     * @param graphElement
     *            the graph element
     * @param networkModel
     *            the network model
     * @return the network component by graph element
     */
    public static NetworkComponent getNetworkComponentByGraphElement(
	    GraphElement graphElement, NetworkModel networkModel) {
	String componentID = graphElement.getId().split("_")[0];
	return networkModel.getNetworkComponent(componentID);
    }

    /**
     * Find node in network component.
     * 
     * @param networkComponent
     *            the network component
     * @param networkModel
     *            the network model
     * @return the graph node
     */
    public static GraphNode findNodeInNetworkComponent(
	    NetworkComponent networkComponent, NetworkModel networkModel) {
	for (String graphElementID : networkComponent.getGraphElementIDs()) {
	    GraphElement graphElement = networkModel
		    .getGraphElement(graphElementID);
	    if (graphElement instanceof GraphNode) {
		return (GraphNode) graphElement;
	    }
	}
	return null;
    }
}
