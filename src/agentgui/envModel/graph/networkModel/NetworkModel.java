/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://sourceforge.net/projects/agentgui/
 * http://www.dawis.wiwi.uni-due.de/ 
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

package agentgui.envModel.graph.networkModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;

/**
 * The Environment Network Model.
 * This class encapsulates a JUNG graph representing a grid, with edges representing the grid components.
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 * @author <br>Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 *
 */
public class NetworkModel{
	/**
	 * The JUNG graph.
	 */
	private Graph<GraphNode, GraphEdge> graph;
	/**
	 * HashMap providing access to the grid components based on the component's agentID
	 */
	private HashMap<String, GraphElement> graphElements;
	/**
	 * A list of all NetworkComponents in the GridModel, accessible by ID
	 */
	private HashMap<String, NetworkComponent> networkComponents;
	/**
	 * Default constructor
	 */
	public NetworkModel(){
		this.graph = new SparseGraph<GraphNode, GraphEdge>();
		this.graphElements = new HashMap<String, GraphElement>();
		this.networkComponents = new HashMap<String, NetworkComponent>();
	}
	
	/**
	 * Returns the GridComponent with the given ID, or null if not found.
	 * @param id The ID to look for
	 * @return The GridComponent
	 */
	public GraphElement getGraphElement(String id){
		return graphElements.get(id);
	}
	/**
	 * @return graphElements The hashmap of GraphElements
	 */
	public  HashMap<String, GraphElement> getGraphElements(){
		return graphElements;
	}
	/**
	 * Returns a list of all GridComponents
	 * @return The list
	 */
	public Collection<GraphEdge> getEdges() {
		return graph.getEdges();
	}
	/**
	 * 
	 * @return The Graph
	 */
	public Graph<GraphNode, GraphEdge> getGraph() {
		return graph;
	}
	/**
	 * Sets the the graph of the network model
	 * @param graph
	 */
	public void setGraph(Graph<GraphNode, GraphEdge> graph) {
		this.graph = graph;
		
		// Create HashMap of components
		this.graphElements = new HashMap<String, GraphElement>();
		Iterator<GraphNode> nodeIterator = graph.getVertices().iterator();
		while(nodeIterator.hasNext()){
			GraphNode node = nodeIterator.next();
			graphElements.put(node.getId(), node);
		}
		Iterator<GraphEdge> edgeIterator = graph.getEdges().iterator();
		while(edgeIterator.hasNext()){
			GraphEdge edge = edgeIterator.next();
			graphElements.put(edge.getId(), edge);
		}
	}
	/**
	 * This method adds a NetworkComponent to the GridModel's networkComponents HashMap, using its' ID as key
	 * @param component The NetworkComponent to add
	 */
	public void addNetworkComponent(NetworkComponent component){
		networkComponents.put(component.getId(), component);
	}
	
	/**
	 * This method removes a NetworkComponent from the GridModel's networkComponents HashMap, using its' ID as key
	 * @param component The NetworkComponent to remove
	 */
	public void removeNetworkComponent(NetworkComponent component){
		networkComponents.remove(component.getId());
	}
	/**
	 * This method gets the NetworkComponent with the given ID from the GridModel's networkComponents HashMap
	 * @param id The ID
	 * @return The NetworkComponent
	 */
	public NetworkComponent getNetworkComponent(String id){
		return networkComponents.get(id);
	}
	/**
	 * @return the networkComponents
	 */
	public HashMap<String, NetworkComponent> getNetworkComponents() {
		return networkComponents;
	}
	/**
	 * @param networkComponents the networkComponents to set
	 */
	public void setNetworkComponents(
			HashMap<String, NetworkComponent> networkComponents) {
		this.networkComponents = networkComponents;
	}
	
	/**
	 * Generates the next unique network component ID in the series n1, n2, n3, ... 
	 * @return the next unique network component ID 
	 */
	public String nextNetworkComponentID() {
		//Finds the current maximum network component ID and returns the next one to it.		
		HashMap<String, NetworkComponent> networkComponents = null;
		networkComponents = getNetworkComponents();
		Iterator<NetworkComponent> components = networkComponents.values().iterator();
		int max = -1;
		while(components.hasNext()){
			NetworkComponent component = components.next();
			int num = Integer.parseInt(component.getId().substring(1));
			if(num>max)
				max = num;
		}
		return "n"+(max+1);
	}
	
	/**
	 * Generates the next unique node ID in the series PP0, PP1, PP2, ... 
	 * @return String The next unique node ID that can be used.
	 */
	public String nextNodeID() {
		//Finds the current maximum node ID and returns the next one to it.		
		Collection<GraphNode> vertices = null;
		vertices = getGraph().getVertices();
		Iterator<GraphNode> vertexIter = vertices.iterator();
		int max = -1;
		while(vertexIter .hasNext()){
			GraphNode vertex = vertexIter .next();
			int num = Integer.parseInt(vertex.getId().substring(2));
			if(num>max)
				max = num;
		}
		return "PP"+(max+1);
	}
}
