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
package agentgui.envModel.graph.networkModel;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import agentgui.envModel.graph.controller.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.visualisation.DisplayAgent;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * The Environment Network Model.
 * This class encapsulates a JUNG graph representing a grid, with edges representing the grid components.
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 */
public class NetworkModel implements Cloneable, Serializable {
	
	private static final long serialVersionUID = -5712689010090750522L;
	
	/** The original JUNG graph created or imported in the application. */
	private Graph<GraphNode, GraphEdge> graph;
	/** HashMap providing access to the grid components based on the component's agentID */
	private HashMap<String, GraphElement> graphElements;
	/** A list of all NetworkComponents in the GridModel, accessible by ID */
	private HashMap<String, NetworkComponent> networkComponents;
	
	/** 
	 * The user object, which stores the component type settings for example. 
	 * This slot/field is only used during the runtime of the simulation in 
	 * order to provide the settings without accessing the project information. 
	 */
	private GeneralGraphSettings4MAS generalGraphSettings4MAS = null;
	
	/**
	 * This HashMap can hold alternative NetworkModel's that can be used 
	 * to reduce the complexity of the original graph (e.g after clustering).
	 * The NetworkModel's placed in this HashMap will be also displayed
	 * through the {@link DisplayAgent}.
	 */
	private HashMap<String, NetworkModel> alternativeNetworkModel = null;
	
	
	
	/**
	 * Default constructor
	 */
	public NetworkModel(){
		this.graph = new SparseGraph<GraphNode, GraphEdge>();
		this.graphElements = new HashMap<String, GraphElement>();
		this.networkComponents = new HashMap<String, NetworkComponent>();
	}
	
	/**
	 * Creates a clone of the current instance
	 */
	public NetworkModel getCopy() {
			
		// ----------------------------------------------------------
		// --- Create a copy of the Graph ---------------------------
		// ----------------------------------------------------------
		Graph<GraphNode, GraphEdge> copyGraph = new SparseGraph<GraphNode, GraphEdge>();
		HashMap<String, GraphElement> copyGraphElements = new HashMap<String, GraphElement>();
		
		// --- Copy the edges with their nodes of the graph ---------
		Collection<GraphEdge> edgesCollection = this.graph.getEdges();
		GraphEdge[] edges = edgesCollection.toArray(new GraphEdge [edgesCollection.size()]);
		for (int i = 0; i < edges.length; i++) {
			GraphEdge edge = edges[i];
			EdgeType edgeType =  this.graph.getEdgeType(edge);
			
			GraphNode first  = this.graph.getEndpoints(edge).getFirst();
			GraphNode second = this.graph.getEndpoints(edge).getSecond();
			
			// --- See if the elements are already there ------------
			if (copyGraphElements.get(first.getId())==null) {
				first = first.getCopy();
				copyGraphElements.put(first.getId(), first);
			} else {
				first = (GraphNode) copyGraphElements.get(first.getId());
			}
			if (copyGraphElements.get(second.getId())==null) {
				second = second.getCopy();
				copyGraphElements.put(second.getId(), second);
			} else { 
				second = (GraphNode) copyGraphElements.get(second.getId());
			}
			if (copyGraphElements.get(edge.getId())==null) {
				edge = edge.getCopy();
				copyGraphElements.put(edge.getId(), edge);
			} else{
				edge = (GraphEdge) copyGraphElements.get(edge.getId());
			}
			
			// --- Add the edge and their components to the graph ---			
			copyGraph.addEdge(edge, first, second, edgeType);
			
		}

		// -- Create a copy of the networkComponents ----------------
		HashMap<String, NetworkComponent> copyOfComponents = new HashMap<String, NetworkComponent>(this.networkComponents);

		// -- Create a copy of the generalGraphSettings4MAS ---------
		GeneralGraphSettings4MAS copyOfGeneralGraphSettings4MAS = null;
		if (this.generalGraphSettings4MAS!=null) {
			copyOfGeneralGraphSettings4MAS = this.generalGraphSettings4MAS.getCopy();
		}
		
		// -- Create a copy of the alternativeNetworkModel ----------
		HashMap<String, NetworkModel> copyOfAlternativeNetworkModel = null; 
		if (this.alternativeNetworkModel!=null) {
			copyOfAlternativeNetworkModel = new HashMap<String, NetworkModel>(this.alternativeNetworkModel);
		}
		
		// ----------------------------------------------------------
		// -- Create a new NetworkModel -----------------------------
		// ----------------------------------------------------------
		NetworkModel netModel = new NetworkModel();
		netModel.setGraph(copyGraph);
		netModel.setNetworkComponents(copyOfComponents);
		netModel.setGeneralGraphSettings4MAS(copyOfGeneralGraphSettings4MAS);
		netModel.setAlternativeNetworkModel(copyOfAlternativeNetworkModel);
		return netModel;
		
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
	 * @param newGraph
	 */
	public void setGraph(Graph<GraphNode, GraphEdge> newGraph) {

		this.graph = newGraph;
		this.graphElements = new HashMap<String, GraphElement>();
		
		if (this.graph!=null) {
			// --- Register all nodes to the graphElement HashMap ------- 
			Iterator<GraphNode> nodeIterator = this.graph.getVertices().iterator();
			while(nodeIterator.hasNext()){
				GraphNode node = nodeIterator.next();
				graphElements.put(node.getId(), node);
			}
			// --- Register all edges to the graphElement HashMap -------
			Iterator<GraphEdge> edgeIterator = this.graph.getEdges().iterator();
			while(edgeIterator.hasNext()){
				GraphEdge edge = edgeIterator.next();
				graphElements.put(edge.getId(), edge);
			}
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
	public void setNetworkComponents(HashMap<String, NetworkComponent> networkComponents) {
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
		return this.nextNodeID(false);
	}
	/**
	 * Generates the next unique node ID in the series PP0, PP1, PP2, ... 
	 * @return String The next unique node ID that can be used.
	 */
	public String nextNodeID(boolean skipNullEntries) {
		
		//Finds the current maximum node ID and returns the next one to it.		
		int max = -1;
		boolean errEntry = false;
		
		Collection<GraphNode> nodeCollection = getGraph().getVertices();
		GraphNode[] nodes = nodeCollection.toArray(new GraphNode[nodeCollection.size()]);
		for (int i = 0; i < nodes.length; i++) {
			
			GraphNode node = nodes[i];
			String id = node.getId();
			if (id==null) {
				errEntry = true;
			} else if (id.equals("null")) {
				errEntry = true;
			} else {
				errEntry = false;
			}

			if (errEntry==true && skipNullEntries==false) {
				id = this.nextNodeID(true);
				node.setId(id);
				errEntry = false;
			}
			
			// --- normal operation -------------
			if (errEntry==false) {
				int num = Integer.parseInt(id.substring(2));
				if(num>max) {
					max = num;
				}	
			}
			
		}
		return "PP"+(max+1);
	}

	/**
	 * Sets the general graph settings for the MAS.
	 * @param generalGraphSettings4MAS the new general graph settings for the MAS
	 */
	public void setGeneralGraphSettings4MAS(GeneralGraphSettings4MAS generalGraphSettings4MAS) {
		this.generalGraphSettings4MAS = generalGraphSettings4MAS;
	}
	/**
	 * Gets the general graph settings for the MAS.
	 * @return the general graph settings for the MAS
	 */
	public GeneralGraphSettings4MAS getGeneralGraphSettings4MAS() {
		return generalGraphSettings4MAS;
	}

	/**
	 * Sets the alternative network model.
	 * @param alternativeNetworkModel the alternativeNetworkModel to set
	 */
	public void setAlternativeNetworkModel(HashMap<String, NetworkModel> alternativeNetworkModel) {
		this.alternativeNetworkModel = alternativeNetworkModel;
	}
	/**
	 * Gets the alternative network models.
	 * @return the alternativeNetworkModel
	 */
	public HashMap<String, NetworkModel> getAlternativeNetworkModel() {
		if (alternativeNetworkModel==null) {
			alternativeNetworkModel = new HashMap<String, NetworkModel>();
		}
		return alternativeNetworkModel;
	}
	
}
