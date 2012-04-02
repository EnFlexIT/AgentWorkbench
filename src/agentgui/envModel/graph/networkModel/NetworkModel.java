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

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import agentgui.envModel.graph.controller.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.prototypes.ClusterGraphElement;
import agentgui.envModel.graph.prototypes.DistributionNode;
import agentgui.envModel.graph.prototypes.GraphElementPrototype;
import agentgui.envModel.graph.prototypes.StarGraphElement;
import agentgui.envModel.graph.visualisation.DisplayAgent;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * The Environment Network Model. This class encapsulates a JUNG graph representing a grid, with edges representing the grid components.
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 */
public class NetworkModel implements Cloneable, Serializable {

	private static final long serialVersionUID = -5712689010090750522L;

	/** The original JUNG graph created or imported in the application. */
	private Graph<GraphNode, GraphEdge> graph;

	/** HashMap providing access to the grid components based on the component's agentID. */
	private HashMap<String, GraphElement> graphElements;

	/** A list of all NetworkComponents in the GridModel, accessible by ID. */
	private HashMap<String, NetworkComponent> networkComponents;

	/** The outer network components of this NetworkModel with no Connections */
	private transient ArrayList<String> outerNetworkComponents;

	/** This instance stores the DomainSettings and the ComponentTypeSettings. */
	private GeneralGraphSettings4MAS generalGraphSettings4MAS = null;

	/**
	 * This HashMap can hold alternative NetworkModel's that can be used to 
	 * reduce the complexity of the original graph (e.g after clustering). 
	 * The NetworkModel's placed in this HashMap will be also displayed 
	 * through the {@link DisplayAgent}.
	 */
	private HashMap<String, NetworkModel> alternativeNetworkModel = null;

	/**
	 * Default constructor.
	 */
	public NetworkModel() {
		this.graph = new SparseGraph<GraphNode, GraphEdge>();
		this.graphElements = new HashMap<String, GraphElement>();
		this.networkComponents = new HashMap<String, NetworkComponent>();
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
		if (generalGraphSettings4MAS == null) {
			generalGraphSettings4MAS = new GeneralGraphSettings4MAS();
		}
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
		if (alternativeNetworkModel == null) {
			alternativeNetworkModel = new HashMap<String, NetworkModel>();
		}
		return alternativeNetworkModel;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected NetworkModel clone() {
		try {
			return (NetworkModel) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Creates a clone of the current instance.
	 * @return the copy
	 */
	public NetworkModel getCopy() {

		NetworkModel netModel = new NetworkModel();
		netModel.setGraph(this.getGraphCopy());

		// -- Create a copy of the networkComponents ----------------
		HashMap<String, NetworkComponent> copyOfComponents = new HashMap<String, NetworkComponent>();
		for (NetworkComponent networkComponent : this.networkComponents.values()) {
			if (networkComponent instanceof ClusterNetworkComponent) {
				ClusterNetworkComponent networkComponentCopy = ((ClusterNetworkComponent) networkComponent).getCopy();
				copyOfComponents.put(networkComponentCopy.getId(), networkComponentCopy);
			} else {
				NetworkComponent networkComponentCopy = networkComponent.getCopy();
				copyOfComponents.put(networkComponentCopy.getId(), networkComponentCopy);
			}
		}
		netModel.setNetworkComponents(copyOfComponents);
		netModel.refreshGraphElements();

		// -- Create a copy of the generalGraphSettings4MAS ---------
		GeneralGraphSettings4MAS copyOfGeneralGraphSettings4MAS = null;
		if (this.generalGraphSettings4MAS != null) {
			copyOfGeneralGraphSettings4MAS = this.generalGraphSettings4MAS.getCopy();
		}
		netModel.setGeneralGraphSettings4MAS(copyOfGeneralGraphSettings4MAS);

		// -- Create a copy of the alternativeNetworkModel ----------
		HashMap<String, NetworkModel> copyOfAlternativeNetworkModel = null;
		if (this.alternativeNetworkModel != null) {
			copyOfAlternativeNetworkModel = new HashMap<String, NetworkModel>(this.alternativeNetworkModel);
		}
		netModel.setAlternativeNetworkModel(copyOfAlternativeNetworkModel);

		return netModel;
	}

	/**
	 * Copy graph and graph elements.
	 * @param netModel the net model
	 */
	private Graph<GraphNode, GraphEdge> getGraphCopy() {

		Graph<GraphNode, GraphEdge> copyGraph = new SparseGraph<GraphNode, GraphEdge>();

		// --- Copy all nodes and remind the relation between ID and new instance -------
		Collection<GraphNode> nodesCollection = this.graph.getVertices();
		GraphNode[] nodes = nodesCollection.toArray(new GraphNode[nodesCollection.size()]);
		HashMap<String, GraphNode> graphNodeCopies = new HashMap<String, GraphNode>();
		for (int i = 0; i < nodes.length; i++) {
			GraphNode node = nodes[i];
			GraphNode nodeCopy = node.getCopy();
			graphNodeCopies.put(node.getId(), nodeCopy);
			copyGraph.addVertex(nodeCopy);
		}

		// --- Copy the edges -----------------------------------------------------------
		Collection<GraphEdge> edgesCollection = this.graph.getEdges();
		GraphEdge[] edges = edgesCollection.toArray(new GraphEdge[edgesCollection.size()]);
		for (int i = 0; i < edges.length; i++) {
			GraphEdge edge = edges[i];
			EdgeType edgeType = this.graph.getEdgeType(edge);
			GraphNode first = this.graph.getEndpoints(edge).getFirst();
			GraphNode second = this.graph.getEndpoints(edge).getSecond();

			GraphNode copyFirst = graphNodeCopies.get(first.getId());
			GraphNode copySecond = graphNodeCopies.get(second.getId());
			copyGraph.addEdge(edge, copyFirst, copySecond, edgeType);

		}
		return copyGraph;
	}

	/**
	 * Extracts Network IDs from NetworkComponenList and returns an ID List
	 *
	 * @param networkComponents the network components
	 * @return ArrayList of the IDs
	 */
	public HashSet<String> getNetworkComponentsIDs(HashSet<NetworkComponent> networkComponents) {
		HashSet<String> networkComponentIDs = new HashSet<String>();
		for (NetworkComponent networkComponent : networkComponents) {
			networkComponentIDs.add(networkComponent.getId());
		}
		return networkComponentIDs;
	}

	/**
	 * Returns the GraphElement with the given ID, or null if not found.
	 * @param id The ID to look for
	 * @return the GraphElement
	 */
	public GraphElement getGraphElement(String id) {
		return graphElements.get(id);
	}

	/**
	 * Gets the graph elements.
	 * @return graphElements The hashmap of GraphElements
	 */
	public HashMap<String, GraphElement> getGraphElements() {
		return graphElements;
	}

	/**
	 * This method gets the GraphElements that are part of the given NetworkComponent
	 * 
	 * @param networkComponent The NetworkComponent
	 * @return The GraphElements
	 */
	public Vector<GraphElement> getGraphElementsFromNetworkComponent(NetworkComponent networkComponent) {
		Vector<GraphElement> elements = new Vector<GraphElement>();
		for (String graphElementID : networkComponent.getGraphElementIDs()) {
			GraphElement ge = getGraphElement(graphElementID);
			if (ge != null) {
				elements.add(ge);
			}
		}
		return elements;
	}

	/**
	 * Gets the graph.
	 * 
	 * @return The Graph
	 */
	public Graph<GraphNode, GraphEdge> getGraph() {
		return graph;
	}

	/**
	 * Sets the the graph of the network model.
	 * 
	 * @param newGraph the new graph
	 */
	public void setGraph(Graph<GraphNode, GraphEdge> newGraph) {
		this.graph = newGraph;
		this.graphElements = new HashMap<String, GraphElement>();
		this.refreshGraphElements();
	}

	/**
	 * Reloads the the GraphElementsMap.
	 */
	private void refreshGraphElements() {
		if (this.graph != null) {
			this.graphElements = new HashMap<String, GraphElement>();
			this.register(graph.getVertices().toArray(new GraphNode[0]));
			this.register(graph.getEdges().toArray(new GraphEdge[0]));
		}
	}

	/**
	 * Register all GraphElemnents used when adding a Component.
	 * 
	 * @param graphElements the graph elements
	 */
	private void register(GraphElement[] graphElements) {
		for (GraphElement graphElement : graphElements) {
			this.graphElements.put(graphElement.getId(), graphElement);
		}
	}

	/**
	 * Adds a network component to the NetworkModel.
	 *
	 * @param networkComponent the network component
	 * @return the network component
	 */
	public NetworkComponent addNetworkComponent(NetworkComponent networkComponent) {
		networkComponents.put(networkComponent.getId(), networkComponent);
		this.refreshGraphElements();
		return networkComponent;
	}

	/**
	 * Rename graph element.
	 * 
	 * @param graphElementID the graph element id
	 * @param oldComponentID the old component id
	 * @param newComponentID the new component id
	 * @return the string
	 */
	private String renameGraphElement(String graphElementID, String oldComponentID, String newComponentID) {
		GraphElement graphElement = graphElements.get(graphElementID);
		graphElements.remove(graphElementID);
		graphElement.setId(graphElement.getId().replaceFirst(oldComponentID, newComponentID));
		graphElements.put(graphElement.getId(), graphElement);
		return graphElement.getId();
	}

	/**
	 * Rename component.
	 * 
	 * @param oldCompID the old comp id
	 * @param newCompID the new comp id
	 */
	public void renameComponent(String oldCompID, String newCompID) {
		NetworkComponent networkComponent = networkComponents.get(oldCompID);
		// Temporary set
		HashSet<String> newGraphElementIDs = new HashSet<String>(networkComponent.getGraphElementIDs());
		// Renaming the corresponding edges of the network component
		for (String graphElementID : networkComponent.getGraphElementIDs()) {
			newGraphElementIDs.remove(graphElementID);
			newGraphElementIDs.add(renameGraphElement(graphElementID, oldCompID, newCompID));
		}

		// Updating the network component
		networkComponent.setGraphElementIDs(newGraphElementIDs);
		networkComponent.setId(newCompID);
		networkComponents.remove(oldCompID);
		networkComponents.put(newCompID, networkComponent);
	}

	/**
	 * This method removes a NetworkComponent from the GridModel's networkComponents 
	 * HashMap, using its' ID as key.
	 * 
	 * @param networkComponent The NetworkComponent to remove
	 */
	public void removeNetworkComponent(NetworkComponent networkComponent) {

		if (networkComponent.getPrototypeClassName().equals(DistributionNode.class.getName())) {
			// ----------------------------------------------------------------
			// --- A DistributionNode has to be removed -----------------------
			// ----------------------------------------------------------------
			String graphElementID = networkComponent.getGraphElementIDs().iterator().next();
			GraphNode graphNode = (GraphNode) this.getGraphElement(graphElementID);
			this.splitNetworkModelAtNode(graphNode);
			this.graph.removeVertex(graphNode);
			this.graphElements.remove(graphNode.getId());
			// ----------------------------------------------------------------
		} else {
			// ----------------------------------------------------------------
			// --- Another element has to be removed --------------------------
			// ----------------------------------------------------------------
			for (String graphElementID : networkComponent.getGraphElementIDs()) {
				GraphElement graphElement = this.getGraphElement(graphElementID);
				if (graphElement instanceof GraphEdge) {
					graph.removeEdge((GraphEdge) graphElement);
				} else if (graphElement instanceof GraphNode) {
					HashSet<NetworkComponent> networkComponents = this.getNetworkComponents((GraphNode) graphElement);
					if (networkComponents.size() < 2) {
						this.graph.removeVertex((GraphNode) graphElement);
					}
				}
			}
			// ----------------------------------------------------------------
		}
		networkComponents.remove(networkComponent.getId());
		this.refreshGraphElements();
	}

	/**
	 * Removes the network components.
	 * @param networkComponents the network components
	 */
	public HashSet<NetworkComponent> removeNetworkComponents(HashSet<NetworkComponent> networkComponents) {
		for (NetworkComponent networkComponent : networkComponents) {
			this.removeNetworkComponent(networkComponent);
		}
		return networkComponents;
	}

	/**
	 * Removes the network components if not in list.
	 * @param networkComponents the network components
	 */
	public HashSet<NetworkComponent> removeNetworkComponentsInverse(HashSet<NetworkComponent> networkComponents) {
		HashSet<NetworkComponent> removed = new HashSet<NetworkComponent>();
		HashSet<String> networkComponentIDs = getNetworkComponentsIDs(networkComponents);
		for (NetworkComponent networkComponent : new ArrayList<NetworkComponent>(this.networkComponents.values())) {
			if (!networkComponentIDs.contains(networkComponent.getId())) {
				removeNetworkComponent(networkComponent);
				removed.add(networkComponent);
			}
		}
		return removed;
	}

	/**
	 * Gets the a node from network component.
	 * 
	 * @param networkComponent the network component
	 * @return the a node from network component
	 */
	public Vector<GraphNode> getNodesFromNetworkComponent(NetworkComponent networkComponent) {
		Vector<GraphNode> nodeList = new Vector<GraphNode>();
		for (String graphElementID : networkComponent.getGraphElementIDs()) {
			GraphElement graphElement = graphElements.get(graphElementID);
			if (graphElement instanceof GraphNode) {
				nodeList.add((GraphNode) graphElement);
			}
		}
		return nodeList;
	}

	/**
	 * This method gets the NetworkComponent with the given ID from the GridModel's networkComponents HashMap.
	 * 
	 * @param id The ID
	 * @return The NetworkComponent
	 */
	public NetworkComponent getNetworkComponent(String id) {
		return networkComponents.get(id);
	}

	public HashSet<NetworkComponent> getNeighbourNetworkComponents(HashSet<NetworkComponent> networkComponents) {
		HashSet<NetworkComponent> neighbourNetworkComponents = new HashSet<NetworkComponent>();
		for (NetworkComponent networkComponent : networkComponents) {
			neighbourNetworkComponents.addAll(getNeighbourNetworkComponents(networkComponent));
		}
		return neighbourNetworkComponents;
	}

	/**
	 * Gets the neighbour network components.
	 * 
	 * @param networkComponent the network component
	 * @return the neighbour network components
	 */
	public Vector<NetworkComponent> getNeighbourNetworkComponents(NetworkComponent networkComponent) {
		Vector<NetworkComponent> comps = new Vector<NetworkComponent>();
		Vector<GraphNode> nodes = this.getNodesFromNetworkComponent(networkComponent);
		for (int i = 0; i < nodes.size(); i++) {
			GraphNode node = nodes.get(i);
			for (NetworkComponent netComponent : this.networkComponents.values()) {
				// --- check if the component contains the current node -------
				if (netComponent.getGraphElementIDs().contains(node.getId())) {
					// --- Add component to result list -----------------------
					if (netComponent != networkComponent) {
						comps.add(netComponent);
						break;
					}
				}
			}
		}
		return comps;
	}

	/**
	 * Gets the network component by graph edge id.
	 * 
	 * @param graphEdge the graph edge
	 * @return the network component by graph edge id
	 */
	public NetworkComponent getNetworkComponent(GraphEdge graphEdge) {
		for (NetworkComponent networkComponent : this.networkComponents.values()) {
			if (networkComponent.getGraphElementIDs().contains(graphEdge.getId())) {
				return networkComponent;
			}
		}
		System.out.println("Edge not part of Component");
		return null;
	}

	/**
	 * Gets alls networkComponents contained in a GraphNode Set
	 *
	 * @param graphNodes the graph nodes
	 * @return the network components
	 */
	public HashSet<NetworkComponent> getNetworkComponents(Set<GraphNode> graphNodes) {
		HashSet<NetworkComponent> networkComponents = new HashSet<NetworkComponent>();
		for (GraphNode graphNode : graphNodes) {
			networkComponents.addAll(getNetworkComponents(graphNode));
		}
		return networkComponents;
	}

	/**
	 * Returns the {@link NetworkComponent}'s that are fully selected by the given set of GraphNodes.<br>
	 * As an example: if you have selected one vertex of a simple directed edge with two vertices, this
	 * method will return null.  
	 *
	 * @param graphNodes the GraphNodes
	 * @return the {@link NetworkComponent}'s that are fully selected by the given GraphNodes's
	 */
	public HashSet<NetworkComponent> getNetworkComponentsFullySelected(Set<GraphNode> graphNodes) {
		
		// --- Create a reminder for the IDs of the GraphNodes ------
		Set<String> idSetGraphNodes = new HashSet<String>();
		for (GraphNode graphNode : graphNodes) {
			idSetGraphNodes.add(graphNode.getId());
		}
		
		// --- Get the affected components --------------------------
		HashSet<NetworkComponent> componentsAffected = this.getNetworkComponents(graphNodes);
		HashSet<NetworkComponent> componentsFound = new HashSet<NetworkComponent>();
		
		// --- Run through the list of components -------------------
		for (NetworkComponent component : componentsAffected) {
			// --- Extract all GraphNode IDs ------------------------
			Set<String> compIDSetAffecetd = this.extractElementIDs(component, true);
			Set<String> compIDSetFound = new HashSet<String>();
			for (String compID : compIDSetAffecetd) {
				// --- Is this a node in the selection? -------------
				if (idSetGraphNodes.contains(compID)) {
					compIDSetFound.add(compID);
				}
			}
			// --- Add the NetworkComponent to the result
			if (compIDSetFound.size()==compIDSetAffecetd.size()) {
				componentsFound.add(component);
			}
			
		}
		
		if (componentsFound.size()==0) {
			return null;
		}
		return componentsFound;
		
	}
	
	
	/**
	 * Extract graph node i ds.
	 *
	 * @param networkComponent the network component
	 * @return the hash set
	 */
	public HashSet<String> extractElementIDs(NetworkComponent networkComponent, boolean search4GraphNodes) {
		
		// --- Create the result set -----------------
		HashSet<String> elementIDsFound = new HashSet<String>();
		
		// --- Check the GraphElements ---------------
		Set<String> elements = networkComponent.getGraphElementIDs();
		for (String elementID : elements) {
			GraphElement element = this.getGraphElement(elementID);
			if (element!=null) {
				if (search4GraphNodes==true) {
					// --- search for GraphNode's ----
					if (element instanceof GraphNode) {
						elementIDsFound.add(elementID);
					}
				} else {
					// --- search for GraphEdge's ----
					if (element instanceof GraphEdge) {
						elementIDsFound.add(elementID);
					}
				}
			}
		}
		return elementIDsFound;
	}
	
	/**
	 * Gives the set of network components containing the given node.
	 * 
	 * @param graphNode - A GraphNode
	 * @return HashSet<NetworkComponent> - The set of components which contain the node
	 */
	public HashSet<NetworkComponent> getNetworkComponents(GraphNode graphNode) {
		HashSet<NetworkComponent> networkComponents = new HashSet<NetworkComponent>();
		for (NetworkComponent networkComponent : this.networkComponents.values()) {
			if (networkComponent.getGraphElementIDs().contains(graphNode.getId())) {
				networkComponents.add(networkComponent);
			}
		}
		return networkComponents;
	}

	/**
	 * Gets the network components.
	 * 
	 * @return the networkComponents
	 */
	public HashMap<String, NetworkComponent> getNetworkComponents() {
		return networkComponents;
	}

	/**
	 * Sets the network components.
	 * 
	 * @param networkComponents the networkComponents to set
	 */
	public void setNetworkComponents(HashMap<String, NetworkComponent> networkComponents) {
		this.networkComponents = networkComponents;
		this.refreshGraphElements();
	}

	/**
	 * Generates the next unique network component ID in the series n1, n2, n3, ...
	 * 
	 * @return the next unique network component ID
	 */
	public String nextNetworkComponentID() {
		// --- Finds the current maximum network component ID and returns the next one to it. -----
		int startInt = networkComponents.size();
		while (networkComponents.get((GeneralGraphSettings4MAS.PREFIX_NETWORK_COMPONENT + startInt)) != null) {
			startInt++;
		}
		return GeneralGraphSettings4MAS.PREFIX_NETWORK_COMPONENT + (startInt);
	}

	/**
	 * Generates the next unique node ID in the series PP0, PP1, PP2, ...
	 * 
	 * @return String The next unique node ID that can be used.
	 */
	public String nextNodeID() {
		return nextNodeID(false);
	}

	/**
	 * Generates the next unique node ID in the series PP0, PP1, PP2, ...
	 * 
	 * @param skipNullEntries the skip null entries
	 * @return String The next unique node ID that can be used.
	 */
	private String nextNodeID(boolean skipNullEntries) {

		// Finds the current maximum node ID and returns the next one to it.
		int max = -1;
		boolean errEntry = false;

		Collection<GraphNode> nodeCollection = getGraph().getVertices();
		GraphNode[] nodes = nodeCollection.toArray(new GraphNode[0]);
		for (int i = 0; i < nodes.length; i++) {
			GraphNode node = nodes[i];
			String id = node.getId();
			errEntry = (id == null || id.equals("null")) ? true : false;

			if (errEntry == true && skipNullEntries == false) {
				id = this.nextNodeID(true);
				node.setId(id);
				errEntry = false;
			}
			// --- normal operation -------------
			if (errEntry == false) {
				int num = Integer.parseInt(id.replace(GraphNode.GRAPH_NODE_PREFIX, ""));
				if (num > max) {
					max = num;
				}
			}
		}
		return GraphNode.GRAPH_NODE_PREFIX + (max + 1);
	}

	/**
	 * Corrects the name definitions of a supplement NetworkModel in  
	 * order to avoid name clashes with the current NetworkModel.
	 *
	 * @param supplementNetworkModel a supplement NetworkModel
	 * @return the NetworkModel with corrected names
	 */
	public NetworkModel adjustNameDefinitionsOfSupplementNetworkModel(NetworkModel supplementNetworkModel) {

		if (supplementNetworkModel == this) {
			return supplementNetworkModel;
		}

		// --- Get the general counting information for components and edges --
		String nextCompID = this.nextNetworkComponentID().replace(GeneralGraphSettings4MAS.PREFIX_NETWORK_COMPONENT, "");
		String nextNodeID = this.nextNodeID().replace(GraphNode.GRAPH_NODE_PREFIX, "");
		int nextCompIDCounter = Integer.parseInt(nextCompID);
		int nextNodeIDCounter = Integer.parseInt(nextNodeID);

		// --- Get the graph and component information ------------------------
		Vector<String> netCompNames = new Vector<String>(supplementNetworkModel.getNetworkComponents().keySet());
		Collections.sort(netCompNames, this.getComparator4PrefixedNames());
		Graph<GraphNode, GraphEdge> graph = supplementNetworkModel.getGraph();
		Vector<GraphNode> nodes = new Vector<GraphNode>(graph.getVertices());
		Collections.sort(nodes, this.getComparator4Nodes());
		Vector<GraphEdge> edges = new Vector<GraphEdge>(graph.getEdges());

		// --- Reminder for changed names of nodes and edges ------------------
		HashMap<String, String> mapNodeIDs = new HashMap<String, String>();
		HashMap<String, String> mapEdgeIDs = new HashMap<String, String>();

		// --- Change node names and positions --------------------------------
		for (GraphNode node : nodes) {
			String oldNodeID = new String(node.getId());
			String newNodeID = GraphNode.GRAPH_NODE_PREFIX + nextNodeIDCounter;
			mapNodeIDs.put(oldNodeID, newNodeID); // --- Remind ---
			// --- Apply changes ----------------------------------------------
			node.setId(newNodeID);
			nextNodeIDCounter++;
		}

		// --- Change component names and remind name changes of edges --------
		HashMap<String, NetworkComponent> newNetworkCoponents = new HashMap<String, NetworkComponent>();
		for (String netCompNameOld : netCompNames) {

			String netCompNameNew = GeneralGraphSettings4MAS.PREFIX_NETWORK_COMPONENT + nextCompIDCounter;

			NetworkComponent netComp = supplementNetworkModel.getNetworkComponents().get(netCompNameOld);
			HashSet<String> graphElementsOld = netComp.getGraphElementIDs();
			HashSet<String> graphElementsNew = new HashSet<String>();

			for (String elementNameOld : graphElementsOld) {
				String elementNameNew = null;
				if (elementNameOld.startsWith(GraphNode.GRAPH_NODE_PREFIX)) {
					// --- Node name to change ------------
					elementNameNew = mapNodeIDs.get(elementNameOld);
				} else {
					// --- Edge Name to change ------------
					elementNameNew = elementNameOld.replace(netCompNameOld, netCompNameNew);
					mapEdgeIDs.put(elementNameOld, elementNameNew); // --- Remind ---
				}
				graphElementsNew.add(elementNameNew);
			}

			// --- Remove component from supplement NetworkModel --------------
			supplementNetworkModel.getNetworkComponents().remove(netCompNameOld);
			// --- Configure the component ------------------------------------
			netComp.setId(netCompNameNew);
			netComp.setGraphElementIDs(graphElementsNew);
			// --- Add to reminder for NetworkCompentens; add later -----------
			newNetworkCoponents.put(netCompNameNew, netComp);

			nextCompIDCounter++;

		}

		// --- Rename the edges in the graph ----------------------------------
		for (GraphEdge edge : edges) {
			String oldEdgeID = edge.getId();
			String newEdgeID = mapEdgeIDs.get(oldEdgeID);
			edge.setId(newEdgeID);
		}
		// --- Set the list of NetworkComponent's -----------------------------
		supplementNetworkModel.setNetworkComponents(newNetworkCoponents);

		return supplementNetworkModel;
	}

	/**
	 * Returns the comparator for GraphNodes.
	 * @return the comparator for GraphNode
	 */
	private Comparator<GraphNode> getComparator4Nodes() {

		Comparator<GraphNode> comp = new Comparator<GraphNode>() {
			@Override
			public int compare(GraphNode node1, GraphNode node2) {
				String o1 = node1.getId();
				String o2 = node2.getId();
				Integer o1Int = extractNumericalValue(o1);
				Integer o2Int = extractNumericalValue(o2);

				if (o1Int != null && o2Int != null) {
					return o1Int.compareTo(o2Int);
				} else if (o1Int == null && o2Int != null) {
					return -1;
				} else if (o1Int != null && o2Int == null) {
					return 1;
				} else {
					return o1.compareTo(o2);
				}
			}
		};
		return comp;
	}

	/**
	 * Gets the comparator for prefixed names.
	 * @return the comparator for prefixed names
	 */
	private Comparator<String> getComparator4PrefixedNames() {

		Comparator<String> comp = new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				Integer o1Int = extractNumericalValue(o1);
				Integer o2Int = extractNumericalValue(o2);
				if (o1Int != null && o2Int != null) {
					return o1Int.compareTo(o2Int);
				} else if (o1Int == null && o2Int != null) {
					return -1;
				} else if (o1Int != null && o2Int == null) {
					return 1;
				} else {
					return o1.compareTo(o2);
				}
			}
		};
		return comp;
	}

	/**
	 * Extract the numerical value from a String.
	 * @param expression the expression
	 * @return the integer value
	 */
	private Integer extractNumericalValue(String expression) {
		String numericString = "";
		Integer numeric = null;
		for (int i = 0; i < expression.length(); i++) {
			String letter = Character.toString(expression.charAt(i));
			if (letter.matches("[0-9]")) {
				numericString += letter;
			}
		}
		if (numericString.equals("") == false) {
			numeric = Integer.parseInt(numericString);
		}
		return numeric;
	}
	
	/**
	 * Merge two Clusters which are part of the same model and connected to each other
	 *
	 * @param clusterNC the cluster nc
	 * @param supplementNC the supplement nc
	 */
	public void mergeClusters(ClusterNetworkComponent clusterNC, ClusterNetworkComponent supplementNC) {
		GraphNode centerOfCluster = null;
		for (GraphNode graphNode : getNodesFromNetworkComponent(clusterNC)) {
			if (isCenterNodeOfStar(graphNode, clusterNC)) {
				centerOfCluster = graphNode;
			}
		}

		// get all Nodes which remain as connections, remove the rest
		Vector<GraphElement> graphElements = getGraphElementsFromNetworkComponent(supplementNC);
		for (GraphElement graphElement : new ArrayList<GraphElement>(graphElements)) {
			if (graphElement instanceof GraphEdge) {
				graphElements.remove(graphElement);
			}
			if (graphElement instanceof GraphNode) {
				GraphNode graphNode = (GraphNode) graphElement;
				HashSet<NetworkComponent> components = getNetworkComponents(graphNode);
				if (components.size() == 1 && components.contains(supplementNC)) {
					graphElements.remove(graphNode);
				}
				if (getNetworkComponents(graphNode).contains(clusterNC)) {
					clusterNC.getGraphElementIDs().remove(graphNode.getId());
					graph.removeVertex(graphNode);
					graphElements.remove(graphNode);
				}
			}
		}
		// add new Edges
		HashSet<String> graphElementIDs = clusterNC.getGraphElementIDs();
		int counter = 0;
		for (GraphElement graphElement : graphElements) {
			while( graphElementIDs.contains(clusterNC +"_" + counter) ){
				counter++;
			}
			GraphEdge edge = new GraphEdge(clusterNC + "_" + counter, GeneralGraphSettings4MAS.NETWORK_COMPONENT_TYPE_4_CLUSTER);
			graph.addEdge(edge, centerOfCluster, (GraphNode) graphElement, EdgeType.UNDIRECTED);
			graphElementIDs.add(edge.getId());
			graphElementIDs.add(graphElement.getId());
		}
		
		removeNetworkComponent(supplementNC);
		clusterNC.getClusterNetworkModel().replaceClusterByComponents(supplementNC);
	}

	/**
	 * Merges the current NetworkModel with an incoming NetworkModel as supplement.
	 *
	 * @param supplementNetworkModel the supplement network model
	 * @param nodes2Merge the merge description
	 * @return the residual GraphNode, which connects the two NetworkModel's
	 */
	public GraphNodePairs mergeNetworkModel(NetworkModel supplementNetworkModel, GraphNodePairs nodes2Merge) {

		// --- 1. Adjust the names of the supplement NetworkModel, in order to avoid name clashes -
		NetworkModel srcNM = adjustNameDefinitionsOfSupplementNetworkModel(supplementNetworkModel);

		// --- 2. Add the new graph to the current graph ------------------------------------------
		Graph<GraphNode, GraphEdge> suppGraph = supplementNetworkModel.getGraph();
		// --- 2. a) Nodes ------------------------------------------------------------------------
		for (GraphNode node : suppGraph.getVertices()) {
			this.graph.addVertex(node);
		}
		// --- 2. b) Nodes ------------------------------------------------------------------------
		for (GraphEdge edge : suppGraph.getEdges()) {
			GraphNode node1 = suppGraph.getEndpoints(edge).getFirst();
			GraphNode node2 = suppGraph.getEndpoints(edge).getSecond();
			this.graph.addEdge(edge, node1, node2, suppGraph.getEdgeType(edge));
		}

		// --- 3. Add the NetworkComponents to the model ------------------------------------------
		for (String netCompName : srcNM.getNetworkComponents().keySet()) {
			this.addNetworkComponent(srcNM.getNetworkComponents().get(netCompName));
		}

		// --- 4. Merge the specified nodes -------------------------------------------------------
		return this.mergeNodes(nodes2Merge);

	}

	/**
	 * Gets the valid configuration for a GraphNodePair, that can be used for merging nodes.
	 *
	 * @param graphNodePairs the graph node pairs
	 * @return the valid GraphNodePair for merging couples of GraphNodes
	 */
	public GraphNodePairs getValidGraphNodePairConfig4Merging(GraphNodePairs graphNodePairs) {
		
		GraphNodePairs validConfig = null;
		HashSet<GraphNode> nodes2Merge = new HashSet<GraphNode>();
		HashSet<GraphNode> distributionNodes = new HashSet<GraphNode>();
		
		// --- Get the first component --------------------
		if (containsDistributionNode(this.getNetworkComponents(graphNodePairs.getGraphNode1()))!=null) {
			distributionNodes.add(graphNodePairs.getGraphNode1());
		}
		nodes2Merge.add(graphNodePairs.getGraphNode1());
		// --- Get all other components -------------------
		for (GraphNode node : graphNodePairs.getGraphNode2Hash()) {
			if (containsDistributionNode(this.getNetworkComponents(node))!=null) {
				distributionNodes.add(node);
			}
			nodes2Merge.add(node);
		}
		
		// ------------------------------------------------
		// --- Validate current configuration -------------
		// ------------------------------------------------
		if (distributionNodes.size()==0) {
			// --- Not more than two nodes can be merged --
			if (nodes2Merge.size()==1) {
				// Nothing to merge -----------------------
				return null;
			} else if (nodes2Merge.size()>2) {
				// Without DistributionNode, not more ----- 
				// than two nodes can be merged       -----
				return null;
			}
			return graphNodePairs;
			
		} else if (distributionNodes.size()>1) {
			// --- Found more than one DistributionNode ---
			// --- That is not a valid configuration    ---
			return null;
			
		} 
		
		// --------------------------------------------------------------------
		// --- Is the single DistributionNode on the GraphNode1 position ? ----
		// --------------------------------------------------------------------
		GraphNode distributionNode = distributionNodes.iterator().next(); 
		if (graphNodePairs.getGraphNode1()==distributionNode) {
			// --- That is OK ---------------------------------------
			return graphNodePairs;
		} 

		// --- In case of merging, a DistributionNode should -------- 
		// --- always on the graphNode1 position             --------
		nodes2Merge.remove(distributionNode);
		validConfig = new GraphNodePairs(distributionNode, nodes2Merge);
		return validConfig;
	}
	
	
	/**
	 * Merges the network model by using at least two (selected) nodes.
	 *
	 * @param nodes2Merge the nodes that have to be merge, as GraphNodePairs
	 * @return the residual GraphNode, after the merge process
	 */
	public GraphNodePairs mergeNodes(GraphNodePairs nodes2Merge) {

		// --- Preliminary check ----------------------------------------------
		if (nodes2Merge==null)return null;
		if (nodes2Merge.getGraphNode1()==null) return null;
		if (nodes2Merge.getGraphNode2Hash()==null) return null;
		
		// --- Have a look to the case of one or more DistributionNode's ------
		nodes2Merge = this.getValidGraphNodePairConfig4Merging(nodes2Merge);
		if (nodes2Merge==null) return null;
		
		// --- Create revert information --------------------------------------
		HashSet<GraphNodePairsRevert> revertInfos = new HashSet<GraphNodePairsRevert>();
		
		// --------------------------------------------------------------------
		// --- Walk through the list of GraphNode that have to be merged ------
		GraphNode graphNode1 = (GraphNode) this.getGraphElement(nodes2Merge.getGraphNode1().getId());
		for (GraphNode graphNode2 : nodes2Merge.getGraphNode2Hash() ) {
			
			// --- Make sure that this is a current GraphNode -----------------
			graphNode2 = (GraphNode) this.getGraphElement(graphNode2.getId());
			
			NetworkComponent comp1 = this.getNetworkComponents(graphNode1).iterator().next();
			NetworkComponent comp2 = this.getNetworkComponents(graphNode2).iterator().next();

			// Finding the intersection set of the Graph elements of the two network components
			HashSet<String> intersection = new HashSet<String>(comp1.getGraphElementIDs());
			intersection.retainAll(comp2.getGraphElementIDs());
			// Checking the constraint - Two network components can have maximum one node in common
			if (intersection.size() == 0) {

				// --- No intersection node found - proceed -------------------
				for (GraphEdge edge : this.graph.getIncidentEdges(graphNode2)) {
					// --- switch connection to graphNode1 ----------
					GraphEdge newGraphEdge = this.switchEdgeBetweenGraphNodes(edge, graphNode1, graphNode2);
					// --- store revert information -----------------
					GraphNodePairsRevert revert = new GraphNodePairsRevert(graphNode2, newGraphEdge);
					revertInfos.add(revert);
				}
				// --- Updating the graph element IDs of the component --------
				comp2.getGraphElementIDs().remove(graphNode2.getId());
				comp2.getGraphElementIDs().add(graphNode1.getId());
				// --- Removing node2 from the graph and network model --------
				this.graph.removeVertex(graphNode2);
				this.graphElements.remove(graphNode2.getId());
			}
		}
		
		nodes2Merge.setRevertInfos(revertInfos);
		return nodes2Merge;
	}

	/**
	 * Merge nodes revert.
	 *
	 * @param nodes2Merge the nodes2 merge
	 */
	public void mergeNodesRevert(GraphNodePairs nodes2Merge) {
	
		if (nodes2Merge==null) return;
		if (nodes2Merge.getGraphNode1()==null) return;
		if (nodes2Merge.getGraphNode2Hash()==null) return;
		
		// --------------------------------------------------------------------
		// --- Walk through the list of revert informations -------------------
		
		GraphNode mergedGraphNode = (GraphNode) this.getGraphElement(nodes2Merge.getGraphNode1().getId());
		for (GraphNodePairsRevert revertInfo : nodes2Merge.getRevertInfos() ) {
			
			GraphEdge graphEdge = (GraphEdge) this.getGraphElement(revertInfo.getGraphEdge().getId());
			GraphNode graphNode = (GraphNode) this.getGraphElement(revertInfo.getGraphNode().getId());
			if (graphNode==null) {
				graphNode = revertInfo.getGraphNode();
				this.graph.addVertex(graphNode);
			}
			this.switchEdgeBetweenGraphNodes(graphEdge, graphNode, mergedGraphNode);
			
			NetworkComponent comp1 = this.getNetworkComponent(graphEdge);
			comp1.getGraphElementIDs().remove(mergedGraphNode.getId());
			comp1.getGraphElementIDs().add(graphNode.getId());
			this.graphElements.put(graphNode.getId(), graphNode);
			
		}
		
	}
	
	/**
	 * Splits the network model at a specified node.
	 * @param node2SplitAt the node
	 * @return the GraphNodePairs that can be used to undo this operation
	 */
	public GraphNodePairs splitNetworkModelAtNode(GraphNode node2SplitAt) {
		return this.splitNetworkModelAtNode(node2SplitAt, true);
	}
	
	/**
	 * Splits the network model at a specified node.
	 *
	 * @param node2SplitAt the node
	 * @param moveOppositeNode the move opposite node
	 * @return the GraphNodePairs that can be used to undo this operation
	 */
	public GraphNodePairs splitNetworkModelAtNode(GraphNode node2SplitAt, boolean moveOppositeNode) {
		
		GraphNodePairs graphNodePair = null;
		HashSet<GraphNode> graphNodeConnections = new HashSet<GraphNode>();
		
		// --- Get the components containing the node -------------------------
		HashSet<NetworkComponent> netCompHash = this.getNetworkComponents(node2SplitAt);
		// --- Sort the list of component: ------------------------------------
		// --- If the component list contains a DistributionNode, -------------
		// --- this component should be the last one in the list! -------------
		Vector<NetworkComponent> netCompVector = this.getNetworkComponentVectorWithDistributionNodeAsLast(netCompHash);
		for (int i = 0; i < (netCompVector.size() - 1); i++) {
			NetworkComponent component = netCompVector.get(i);
			// --- Incident Edges on the node ---------------------------------
			for (GraphEdge edge : this.graph.getIncidentEdges(node2SplitAt)) { // for each incident edge
				// --- If the edge is in comp2 --------------------------------
				if (component.getGraphElementIDs().contains(edge.getId())) {
					// --- Creating a new Graph node --------------------------
					GraphNode newNode = new GraphNode();
					newNode.setId(this.nextNodeID());
					newNode.setPosition(node2SplitAt.getPosition());

					// --- Switch the connection to the new node --------------
					GraphEdge newEdge = switchEdgeBetweenGraphNodes(edge, newNode, node2SplitAt);
					graphNodeConnections.add(newNode);

					if (moveOppositeNode==true) {
						// --- Shift position of the new node a bit -----------
						GraphNode otherNode = this.graph.getOpposite(newNode, newEdge);
						newNode.setPosition(this.getShiftedPosition(otherNode, newNode));	
					}

					component.getGraphElementIDs().add(newNode.getId());
					// --- Adding new node to the network model ---------------
					this.graphElements.put(newNode.getId(), newNode);
				}
			}
			// --- Updating the graph element IDs of the component ------------
			component.getGraphElementIDs().remove(node2SplitAt.getId());

		} // --- 'end for' for components -------------------------------------
		refreshGraphElements();
		
		// -- Set return value ------------------------------------------------
		graphNodePair = new GraphNodePairs(node2SplitAt, graphNodeConnections);
		return graphNodePair;
		
	}

	/**
	 * Switches the coupling of an edge between an old and a new GraphNode. 
	 * This is used for splitting and merging GraphNodes.
	 *
	 * @param edge the edge to switch between GraphNodes
	 * @param newGraphNode the new GraphNode for the edge
	 * @param oldGraphNode the old GraphNode for the edge
	 * @return the graph node
	 */
	private GraphEdge switchEdgeBetweenGraphNodes(GraphEdge edge, GraphNode newGraphNode, GraphNode oldGraphNode) {
		
		// Find the node on the other side of the edge
		GraphNode otherNode = graph.getOpposite(oldGraphNode, edge);
		// Create a new edge with the same ID and type
		GraphEdge newEdge = new GraphEdge(edge.getId(), edge.getComponentType());

		if (graph.getSource(edge) != null) {
			// if the edge is directed
			if (graph.getSource(edge) == oldGraphNode)
				graph.addEdge(newEdge, newGraphNode, otherNode, EdgeType.DIRECTED);
			else if (graph.getDest(edge) == oldGraphNode)
				graph.addEdge(newEdge, otherNode, newGraphNode, EdgeType.DIRECTED);
		} else {
			// if the edge is undirected
			graph.addEdge(newEdge, newGraphNode, otherNode, EdgeType.UNDIRECTED);
		}
		// Removing the old edge from the graph and network model
		graph.removeEdge(edge);
		graphElements.remove(edge.getId());
		graphElements.put(newEdge.getId(), newEdge);

		return newEdge;
	}

	/**
	 * Gets a shifted position for a node in relation to the raster size of the component type settings.
	 * @param fixedNode the fixed node
	 * @param shiftNode the shift node
	 * @return the shifted position
	 */
	public Point2D getShiftedPosition(GraphNode fixedNode, GraphNode shiftNode) {

		double move = this.generalGraphSettings4MAS.getSnapRaster() * 2;

		double fixedNodeX = fixedNode.getPosition().getX();
		double fixedNodeY = fixedNode.getPosition().getY();

		double shiftNodeX = shiftNode.getPosition().getX();
		double shiftNodeY = shiftNode.getPosition().getY();

		double radians = Math.atan2((shiftNodeY - fixedNodeY), (shiftNodeX - fixedNodeX));

		shiftNodeX = shiftNodeX - move * Math.cos(radians);
		shiftNodeY = shiftNodeY - move * Math.sin(radians);

		Point2D newPosition = new Point2D.Double(shiftNodeX, shiftNodeY);
		return newPosition;
	}

	/**
	 * Returns the network component vector with the DistributionNode as last.
	 * 
	 * @param componentVector the component vector
	 * @return the network component vector with distribution node as last
	 */
	public Vector<NetworkComponent> getNetworkComponentVectorWithDistributionNodeAsLast(HashSet<NetworkComponent> componentVector) {

		NetworkComponent distributionNodeComponent = null;
		Vector<NetworkComponent> newComponentVector = new Vector<NetworkComponent>();
		for (NetworkComponent component : componentVector) {
			if (component.getPrototypeClassName().equals(DistributionNode.class.getName())) {
				distributionNodeComponent = component;
			} else {
				newComponentVector.add(component);
			}
		}
		if (distributionNodeComponent != null) {
			newComponentVector.add(distributionNodeComponent);
		}
		return newComponentVector;
	}

	/**
	 * Checks, if a component list contains distribution node.
	 * 
	 * @param components the components as HashSet<NetworkComponent>
	 * @return the network component, which is the DistributionNode or null
	 */
	public NetworkComponent containsDistributionNode(HashSet<NetworkComponent> components) {
		Iterator<NetworkComponent> compIt = components.iterator();
		while (compIt.hasNext()) {
			NetworkComponent component = compIt.next();
			if (component.getPrototypeClassName().equals(DistributionNode.class.getName())) {
				return component;
			}
		}
		return null;
	}

	/**
	* Returns the cluster components of the NetworkModel.
	* @return the cluster components
	*/
	public ArrayList<ClusterNetworkComponent> getClusterComponents() {
		return getClusterComponents(this.networkComponents.values());
	}

	/**
	 * Gets the cluster components of a collection of clusterComponents.
	 *
	 * @param components the components
	 * @return the cluster components
	 */
	public ArrayList<ClusterNetworkComponent> getClusterComponents(Collection<NetworkComponent> components) {
		ArrayList<ClusterNetworkComponent> clusterComponents = new ArrayList<ClusterNetworkComponent>();
		for (NetworkComponent networkComponent : components) {
			if (networkComponent instanceof ClusterNetworkComponent) {
				clusterComponents.add((ClusterNetworkComponent) networkComponent);
			}
		}
		return clusterComponents;
	}

	/**
	 * Checks whether a network component is in the star graph element
	 * 
	 * @param comp the network component
	 * @return true if the component is a star graph element
	 */
	public boolean isStarGraphElement(NetworkComponent comp) {
		GraphElementPrototype graphElement = null;
		try {
			Class<?> theClass;
			theClass = Class.forName(comp.getPrototypeClassName());
			graphElement = (GraphElementPrototype) theClass.newInstance();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			System.err.println(ex + " GraphElementPrototype class must be in class path.");
		} catch (InstantiationException ex) {
			System.err.println(ex + " GraphElementPrototype class must be concrete.");
		} catch (IllegalAccessException ex) {
			System.err.println(ex + " GraphElementPrototype class must have a no-arg constructor.");
		}
		// --- StarGraphElement is the super class of all star graph elements ---
		if (graphElement instanceof StarGraphElement) {
			return true;
		}
		return false;
	}

	/**
	 * Given a node and a graph component of star prototype, checks whether the node is the center of the star or not.
	 * 
	 * @param node The node to be checked
	 * @param comp The network component containing the node having the star prototype
	 */
	public boolean isCenterNodeOfStar(GraphNode node, NetworkComponent comp) {
		for (String graphElementID : comp.getGraphElementIDs()) {
			GraphElement elem = getGraphElement(graphElementID);
			// The center node should be incident on all the edges of the component
			if (elem instanceof GraphEdge && !graph.isIncident(node, (GraphEdge) elem)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if is free node.
	 *
	 * @param graphNode the GraphNode
	 * @return true, if is free node
	 */
	public boolean isFreeGraphNode(GraphNode graphNode) {

		// --- The number of network components containing this node ------
		HashSet<NetworkComponent> networkComponents = getNetworkComponents(graphNode);
		if (networkComponents.size() == 1) {
			NetworkComponent networkComponent = networkComponents.iterator().next();
			// --- Node is present in only one component and not center of a star ------------------
			if (isStarGraphElement(networkComponent) && isCenterNodeOfStar(graphNode, networkComponent)) {
				return false;
			}
			return true;
		}
		for (NetworkComponent networkComponent : networkComponents) {
			// --- Node is present in several components ------------------
			if (networkComponent.getPrototypeClassName().equals(DistributionNode.class.getName())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Replace NetworkComponents by one ClusterComponent.
	 * @param networkComponents A List of NetworkComponents
	 */
	public ClusterNetworkComponent replaceComponentsByCluster(HashSet<NetworkComponent> networkComponents) {
		// ---------- Prepare Parameters for ClusterComponent ------------------------------
		NetworkModel clusterNetworkModel = this.getCopy();
		clusterNetworkModel.setAlternativeNetworkModel(null);
		clusterNetworkModel.removeNetworkComponentsInverse(networkComponents);
		removeNetworkComponents(networkComponents);

		// ----------- Create Cluster Prototype and Component ---------------------
		Vector<GraphNode> outerNodes = getOuterNodes(networkComponents);
		String clusterComponentID = nextNetworkComponentID();
		ClusterGraphElement clusterGraphElement = new ClusterGraphElement(outerNodes, clusterComponentID);
		HashSet<GraphElement> clusterElements = new ClusterGraphElement(outerNodes, clusterComponentID).addToGraph(this);
		ClusterNetworkComponent clusterComponent = new ClusterNetworkComponent(clusterComponentID, clusterGraphElement.getType(), null, clusterElements, clusterGraphElement.isDirected(),
				getNetworkComponentsIDs(networkComponents), clusterNetworkModel);
		this.networkComponents.put(clusterComponent.getId(), clusterComponent);
		refreshGraphElements();
		return clusterComponent;
	}

	/**
	 * Merges Cluster NetworkModel with this NetworkModel and removes the Cluster if it's part of this model
	 *
	 * @param clusterNetworkComponent the cluster network component
	 */
	public void replaceClusterByComponents(ClusterNetworkComponent clusterNetworkComponent) {
		for( GraphNode graphNode : clusterNetworkComponent.getClusterNetworkModel().getGraph().getVertices())
		{
			if (getGraphElement(graphNode.getId()) == null) {
				GraphNode graphNodeCopy = graphNode.getCopy();
				graph.addVertex(graphNodeCopy);
				graphElements.put(graphNodeCopy.getId(), graphNodeCopy);
			}
		}
		
		for (GraphEdge graphEdge : clusterNetworkComponent.getClusterNetworkModel().getGraph().getEdges()) {
			GraphEdge graphEdgeNew = new GraphEdge(graphEdge.getId(), graphEdge.getComponentType());
			EdgeType edgeType = clusterNetworkComponent.getClusterNetworkModel().getGraph().getEdgeType(graphEdge);
			GraphNode first = clusterNetworkComponent.getClusterNetworkModel().getGraph().getEndpoints(graphEdge).getFirst();
			GraphNode second = clusterNetworkComponent.getClusterNetworkModel().getGraph().getEndpoints(graphEdge).getSecond();

			GraphNode copyFirst = (GraphNode) graphElements.get(first.getId());
			GraphNode copySecond = (GraphNode) graphElements.get(second.getId());
			graph.addEdge(graphEdgeNew, copyFirst, copySecond, edgeType);
		}

		for (NetworkComponent networkComponent : clusterNetworkComponent.getClusterNetworkModel().getNetworkComponents().values()) {
			addNetworkComponent(networkComponent);
		}
		if (getNetworkComponent(clusterNetworkComponent.getId()) != null) {
			removeNetworkComponent(clusterNetworkComponent);
		}
		refreshGraphElements();
	}

	/**
	 * Gets the outer nodes.
	 *
	 * @param networkComponents the network components
	 * @return the outer nodes
	 */
	private Vector<GraphNode> getOuterNodes(HashSet<NetworkComponent> networkComponents) {
		Vector<GraphNode> outerNodes = new Vector<GraphNode>();
		for (NetworkComponent networkComponent : networkComponents) {
			for (GraphNode graphNode : getNodesFromNetworkComponent(networkComponent)) {
				if (isFreeGraphNode(graphNode)) {
					outerNodes.add(graphNode);
				}
			}
		}
		return outerNodes;
	}

	/**
	 * Gets the outer network components. Is build only one time, after generation or after copy
	 * 
	 * @return the outer network components
	 */
	public ArrayList<String> getOuterNetworkComponentIDs() {
		if (outerNetworkComponents != null) {
			return outerNetworkComponents;
		}
		outerNetworkComponents = new ArrayList<String>();
		for (GraphNode graphNode : graph.getVertices()) {
			if (isFreeGraphNode(graphNode)) {
				NetworkComponent networkComponent = getNetworkComponents(graphNode).iterator().next();
				if (!networkComponent.getType().equals("Exit")) {
					outerNetworkComponents.add(networkComponent.getId());
				}
			}
		}
		return outerNetworkComponents;
	}

}
