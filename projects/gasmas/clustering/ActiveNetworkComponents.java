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

import java.util.ArrayList;

import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;

/**
 * The Class ActiveNetworkComponents.
 */
public class ActiveNetworkComponents {

	/** The active network component agent class prefix. */
	private static final String networkComponentAgentClassPrefix = "gasmas.agents.components.";

	/** The active network component agent classes. */
	private final static String[] activeNetworkComponentAgentClasses = new String[] { "CompressorAgent", "EntryAgent", "ExitAgent", "StorageAgent" };

	/**
	 * Identify active components.
	 *
	 * @param networkModel the network model
	 * @return the active network components
	 */
	public static ArrayList<NetworkComponent> getActiveNetworkComponents(NetworkModel networkModel) {

		ArrayList<NetworkComponent> activeNetworkComponents = new ArrayList<NetworkComponent>();
		for (NetworkComponent networkComponent : networkModel.getNetworkComponents().values()) {
			String agentClassName = networkComponent.getAgentClassName();
			if (agentClassName != null) {
				for (String activeAgentClassType : activeNetworkComponentAgentClasses) {
					if (agentClassName.equals(networkComponentAgentClassPrefix + activeAgentClassType)) {
						activeNetworkComponents.add(networkComponent);
					}
				}
			}
		}
		return activeNetworkComponents;
	}

}
