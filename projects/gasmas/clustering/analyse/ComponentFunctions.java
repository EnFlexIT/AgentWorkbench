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

import gasmas.agents.components.EntryAgent;
import gasmas.agents.components.ExitAgent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import agentgui.envModel.graph.networkModel.ClusterNetworkComponent;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphEdgeDirection;
import agentgui.envModel.graph.networkModel.GraphElement;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;

/**
 * The Class ActiveNetworkComponents.
 */
public class ComponentFunctions {

	/** The active network component agent class prefix. */
	private static final String networkComponentAgentClassPrefix = "gasmas.agents.components.";

	/** The active network component agent classes. */
	private final static String[] activeNetworkComponentAgentClasses = new String[] { "Compressor", "ControlValve", "SimpleValve", "Entry", "Exit", "Storage" };
	private final static String[] activeClasses = new String[] { "Compressor", "ControlValve", "ClusterNetwork" };

	/**
	 * Identify active components.
	 *
	 * @param networkModel the network model
	 * @return the active network components
	 */
	public static ArrayList<NetworkComponent> getActiveNetworkComponents(NetworkModel networkModel) {
		return findAgentComponentsTypes(networkModel, activeNetworkComponentAgentClasses);
	}

	public static ArrayList<NetworkComponent> getActiveAgentComponents(NetworkModel networkModel) {
		return findAgentComponentsTypes(networkModel, activeClasses);
	}

	private static ArrayList<NetworkComponent> findAgentComponentsTypes(NetworkModel networkModel, String[] componentTypes) {
		ArrayList<NetworkComponent> activeNetworkComponents = new ArrayList<NetworkComponent>();
		for (NetworkComponent networkComponent : networkModel.getNetworkComponents().values()) {
			String agentClassName = networkComponent.getAgentClassName();
			if (agentClassName != null) {
				for (String activeAgentClassType : componentTypes) {
					if (agentClassName.equals(networkComponentAgentClassPrefix + activeAgentClassType + "Agent")) {
						activeNetworkComponents.add(networkComponent);
					}
				}
			}
		}
		return activeNetworkComponents;
	}

	public static void printAmountOfDiffernetTypesOfAgents(String name, NetworkModel networkModel) {
		HashMap<String, Integer> map = new HashMap<String, Integer>(); 
		for (NetworkComponent networkComponent : new ArrayList<NetworkComponent>(networkModel.getNetworkComponents().values())) {
			Integer i = map.get(networkComponent.getAgentClassName());
			if (i == null) {
				i = new Integer(0);
			}
			map.put(networkComponent.getAgentClassName(), i + 1);
		}
		System.out.println(name + " " + new Date().getTime());
		for (Entry<String, Integer> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		System.out.println("Sum Components " + networkModel.getNetworkComponents().size());
	}
	
	public static void printAmountOfNodesEdgesAgents(String name, NetworkModel networkModel) {
		int numberGraphNodes = 0;
		int numberGraphEdges = 0;
		for (GraphElement graphElement : networkModel.getGraphElements().values()) {
			if (graphElement instanceof GraphNode){
				numberGraphNodes++;
			}else if (graphElement instanceof GraphEdge){
				numberGraphEdges++;
			}
		}
		System.out.println("Number of agents / network components in " + name + ": " + networkModel.getNetworkComponents().keySet().size() +"\n Number of Nodes: " + numberGraphNodes   + " Number of Edges: " + numberGraphEdges);
		
	}
	
	public static void printAmountOfFixedEdgeDirections(String name, NetworkModel networkModel) {
		int i = 0;
		for (NetworkComponent networkComponent : networkModel.getNetworkComponents().values()) {
			if (networkComponent.getEdgeDirections() != null){
				for (GraphEdgeDirection iterable_element : networkComponent.getEdgeDirections().values()) {
					if (iterable_element.getGraphNodeIDTo()!=null)
						i++;
				}
					
			}
		}
		System.out.println(" Number of directed egdes in " + name + ": " + i);
	}
	
	public static void printAmountOfConnectionsWithEnviroment(ClusterNetworkComponent clusterNetworkComponent) {
		int i = 0;
		for (NetworkComponent networkComponent : clusterNetworkComponent.getConnectionNetworkComponents()) {
			if (!networkComponent.getAgentClassName().equals(EntryAgent.class.getName()) && !networkComponent.getAgentClassName().equals(ExitAgent.class.getName())) {
				i++;
			}
		}
		System.out.println(" " + clusterNetworkComponent.getId() + " has " + i + " connections with the enviroment");
	}
}
