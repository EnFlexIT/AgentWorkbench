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
import java.util.List;

import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;

/**
 * @author Apo
 *
 */
public class NetworkPath {

	/**
	 * Network Path as ArrayList
	 */
	private ArrayList<NetworkComponent> path = new ArrayList<NetworkComponent>();

	private NetworkModel networkModel;

	/**
	 * 
	 * 
	 * @param networkModel
	 * @param graphElementsPath
	 */
	public NetworkPath(NetworkModel networkModel, List<GraphEdge> graphElementsPath) {
		this.networkModel = networkModel;
		graphElemntsPathToComponentsPath(graphElementsPath);
	}

	/**
	 * Identifies
	 * 
	 * @param graphElementsPath
	 * @param networkModel
	 */
	private void graphElemntsPathToComponentsPath(List<GraphEdge> graphElementsPath) {
		for (GraphEdge graphEdge : graphElementsPath) {
			NetworkComponent networkComponent = networkModel.getNetworkComponent(graphEdge);
			if (!path.contains(networkComponent)) {
				path.add(networkComponent);
			}
		}
	}

	/**
	 * Gets the path.
	 * 
	 * @return the path
	 */
	public ArrayList<NetworkComponent> getPath() {
		return path;
	}
}