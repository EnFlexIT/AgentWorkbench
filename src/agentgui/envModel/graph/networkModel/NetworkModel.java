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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5712689010090750522L;

	/** The original JUNG graph created or imported in the application. */
	private Graph<GraphNode, GraphEdge> graph;

	/** HashMap providing access to the grid components based on the component's agentID. */
	private HashMap<String, GraphElement> graphElements;

	/** A list of all NetworkComponents in the GridModel, accessible by ID. */
	private HashMap<String, NetworkComponent> networkComponents;

	/**
	 * The user object, which stores the component type settings for example. This slot/field is only used during the runtime of the simulation in order to provide the settings without accessing the
	 * project information.
	 */
	private GeneralGraphSettings4MAS generalGraphSettings4MAS = null;

	/**
	 * This HashMap can hold alternative NetworkModel's that can be used to reduce the complexity of the original graph (e.g after clustering). The NetworkModel's placed in this HashMap will be also
	 * displayed through the {@link DisplayAgent}.
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
	 * Extracts Element IDs from ElementList and returns an ID List
	 *
	 * @param graphElements the graph Elements
	 * @return HashSet of the IDs
	 */
	public static HashSet<String> graphElementIDs(HashSet<GraphElement> graphElements) {
		HashSet<String> graphElementIDs = new HashSet<String>();
		for (GraphElement graphElement : graphElements) {
			String id = graphElement.getId();
			graphElementIDs.add(id);
		}
		return graphElementIDs;
	}

	/**
	 * Extracts Network IDs from NetworkComponenList and returns an ID List
	 *
	 * @param networkComponents the network components
	 * @return ArrayList of the IDs
	 */
	public static ArrayList<String> networkComponentsIDs(Collection<NetworkComponent> networkComponents) {
		ArrayList<String> networkComponentIDs = new ArrayList<String>();
		for (NetworkComponent networkComponent : networkComponents) {
			networkComponentIDs.add(networkComponent.getId());
		}
		return networkComponentIDs;
	}

	/**
	 * Creates a clone of the current instance.
	 * 
	 * @return the copy
	 */
	public NetworkModel getCopy() {

		NetworkModel netModel = new NetworkModel();
		this.copyGraphAndGraphElemnets(netModel);

		// -- Create a copy of the networkComponents ----------------
		HashMap<String, NetworkComponent> copyOfComponents = new HashMap<String, NetworkComponent>(this.networkComponents);
		netModel.setNetworkComponents(copyOfComponents);

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
	 * Copy graph and graph elemnets.
	 * 
	 * @param netModel the net model
	 */
	private void copyGraphAndGraphElemnets(NetworkModel netModel) {

		Graph<GraphNode, GraphEdge> copyGraph = new SparseGraph<GraphNode, GraphEdge>();
		HashMap<String, GraphElement> copyGraphElements = new HashMap<String, GraphElement>();
		// --- Copy the edges with their nodes of the graph ---------
		Collection<GraphEdge> edgesCollection = this.graph.getEdges();
		GraphEdge[] edges = edgesCollection.toArray(new GraphEdge[edgesCollection.size()]);
		for (int i = 0; i < edges.length; i++) {
			GraphEdge edge = edges[i];
			GraphNode first = this.graph.getEndpoints(edge).getFirst();
			GraphNode second = this.graph.getEndpoints(edge).getSecond();

			first = (GraphNode) copyGraphElement(copyGraphElements, first);
			second = (GraphNode) copyGraphElement(copyGraphElements, second);
			edge = (GraphEdge) copyGraphElement(copyGraphElements, edge);
			// --- Add the edge and their components to the graph ---
			copyGraph.addEdge(edge, first, second, graph.getEdgeType(edge));
		}
		netModel.setGraph(copyGraph);
	}

	/**
	 * Copy graph element.
	 * 
	 * @param copyGraphElements the copy graph elements
	 * @param graphElement the graph element
	 * @return the graph element
	 */
	private GraphElement copyGraphElement(HashMap<String, GraphElement> copyGraphElements, GraphElement graphElement) {
		if (copyGraphElements.get(graphElement.getId()) == null) {
			graphElement = graphElement.getCopy();
			copyGraphElements.put(graphElement.getId(), graphElement);
		} else {
			graphElement = copyGraphElements.get(graphElement.getId());
		}
		return graphElement;
	}

	/**
	 * Returns the GridComponent with the given ID, or null if not found.
	 * 
	 * @param id The ID to look for
	 * @return The GridComponent
	 */
	public GraphElement getGraphElement(String id) {
		return graphElements.get(id);
	}

	/**
	 * Gets the graph elements.
	 * 
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
			elements.add(getGraphElement(graphElementID));
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
	 * Refresh graph elements.
	 */
	private void refreshGraphElements() {
		if (this.graph != null) {
			register(graph.getVertices().toArray(new GraphNode[0]));
			register(graph.getEdges().toArray(new GraphEdge[0]));
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
	 * Adds the network component.
	 * 
	 * @param id the id
	 * @param type the type
	 * @param prototypeClassName the prototype class name
	 * @param directed the directed
	 * @param graphElements the graph elements
	 * @return the network component
	 */
	public NetworkComponent addNetworkComponent(String id, String type, String prototypeClassName, boolean directed, HashSet<GraphElement> graphElements) {
		NetworkComponent networkComponent = new NetworkComponent(id, type, prototypeClassName, graphElements, directed);
		networkComponents.put(networkComponent.getId(), networkComponent);
		refreshGraphElements();
		return networkComponent;
	}

	/**
	 * Adds the network component.
	 * 
	 * @param id the id
	 * @param type the type
	 * @param prototypeClassName the prototype class name
	 * @param directed the directed
	 * @param graphElements the graph elements
	 * @param agentClassName the agent class name
	 * @return the network component
	 */
	public NetworkComponent addNetworkComponent(String id, String type, String prototypeClassName, boolean directed, HashSet<GraphElement> graphElements, String agentClassName) {
		NetworkComponent networkComponent = addNetworkComponent(id, type, prototypeClassName, directed, graphElements);
		networkComponent.setAgentClassName(agentClassName);
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
	 * Removes the distribution node.
	 * 
	 * @param graphElementID the graph element id
	 */
	private void removeDistributionNode(String graphElementID) {
		GraphNode graphNode = (GraphNode) this.getGraphElement(graphElementID);
		this.splitNetworkModelAtNode(graphNode);
		this.graph.removeVertex(graphNode);
		graphElements.remove(graphNode.getId());
	}

	/**
	 * Removes the network components.
	 * 
	 * @param networkComponents the network components
	 */
	public void removeNetworkComponents(Collection<NetworkComponent> networkComponents) {
		for (NetworkComponent networkComponent : networkComponents) {
			removeNetworkComponent(networkComponent);
		}
	}

	/**
	 * Removes the network components if not in list.
	 * 
	 * @param networkComponents the network components
	 */
	public void removeNetworkComponentsIfNotInList(Collection<NetworkComponent> networkComponents) {
		for (NetworkComponent networkComponent : this.networkComponents.values()) {
			if (!networkComponents.contains(networkComponent)) {
				this.removeNetworkComponent(networkComponent);
			}
		}
	}

	/**
	 * This method removes a NetworkComponent from the GridModel's networkComponents HashMap, using its' ID as key.
	 * 
	 * @param component The NetworkComponent to remove
	 */
	public void removeNetworkComponent(NetworkComponent component) {
		if (component.getPrototypeClassName().equals(DistributionNode.class.getName())) {
			// ----------------------------------------------------------------
			// --- A DistributionNode has to be removed -----------------------
			// ----------------------------------------------------------------
			removeDistributionNode(component.getGraphElementIDs().iterator().next());
			// ----------------------------------------------------------------
		} else {
			// ----------------------------------------------------------------
			// --- Another element has to be removed --------------------------
			// ----------------------------------------------------------------
			for (String graphElementID : component.getGraphElementIDs()) {
				GraphElement graphElement = this.getGraphElement(graphElementID);
				if (graphElement instanceof GraphEdge) {
					this.graph.removeEdge((GraphEdge) graphElement);
					// --- Remove from the HashMap of GraphElements -----------
					this.graphElements.remove(graphElement.getId());
				} else if (graphElement instanceof GraphNode && (this.getNetworkComponentCount((GraphNode) graphElement) == 1)) {
					this.graph.removeVertex((GraphNode) graphElement);
					// --- Remove from the HashMap of GraphElements -----------
					graphElements.remove(graphElement.getId());
				}
			}
			// ----------------------------------------------------------------
		}
		networkComponents.remove(component.getId());
	}

	/**
	 * Returns the number of network components which have the given node.
	 * 
	 * @param node Vertex in the Graph
	 * @return count No of network components containing the given node
	 */
	public int getNetworkComponentCount(GraphNode node) {
		int count = 0;
		for (NetworkComponent networkComponent : networkComponents.values()) {
			// --- check if the component contains the current node -
			if (networkComponent.getGraphElementIDs().contains(node.getId())) {
				count++;
			}
		}
		return count;
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
			for (NetworkComponent netComponent : this.getNetworkComponents().values()) {
				// --- check if the component contains the current node -------
				if (netComponent.getGraphElementIDs().contains(node.getId())) {
					// --- Add component to result list -----------------------
					if (netComponent != networkComponent && comps.contains(netComponent) == false) {
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
		String[] componentID = graphEdge.getId().split("_");
		// gets the networkComponent by removing the last part of the ID containing the edge no
		return getNetworkComponent(graphEdge.getId().replace("_" + componentID[componentID.length - 1], ""));
	}

	/**
	 * Gives the set of network components containing the given node.
	 * 
	 * @param node - A GraphNode
	 * @return HashSet<NetworkComponent> - The set of components which contain the node
	 */
	public HashSet<NetworkComponent> getNetworkComponent(GraphNode node) {
		HashSet<NetworkComponent> compSet = new HashSet<NetworkComponent>();
		Iterator<NetworkComponent> components = this.getNetworkComponents().values().iterator();
		while (components.hasNext()) {
			NetworkComponent comp = components.next();
			if (comp.getGraphElementIDs().contains(node.getId())) {
				compSet.add(comp);
			}
		}
		return compSet;
	}

	/**
	 * Replace NetworkComponents by one ClusterComponent.
	 * 
	 * @param networkComponents A List of NetworkComponents
	 */
	public void replaceComonentsByCluster(HashSet<NetworkComponent> networkComponents) {
		// ---------- Prepare Parameters for ClusterComponent ------------------------------
		NetworkModel clusterNetworkModel = this.getCopy();
		clusterNetworkModel.alternativeNetworkModel = null;
		clusterNetworkModel.removeNetworkComponentsIfNotInList(networkComponents);
		Vector<GraphNode> outerNodes = new Vector<GraphNode>();
		for (NetworkComponent networkComponent : networkComponents) {
			for (GraphNode graphNode : getNodesFromNetworkComponent(networkComponent)) {
				if (isFreeNode(graphNode)) {
					outerNodes.add(graphNode);
				}
			}
		}
		// ----------- Create Cluster Prototype and Component ---------------------
		String clusterComponentID = nextNetworkComponentID();
		ClusterGraphElement clusterGraphElement = new ClusterGraphElement(outerNodes, clusterComponentID);
		HashSet<GraphElement> clusterElements = new ClusterGraphElement(outerNodes, clusterComponentID).addToGraph(this);
		ClusterNetworkComponent clusterNetworkComponent = new ClusterNetworkComponent(clusterComponentID, clusterGraphElement.getType(), "ClusterGraphElement", clusterElements,
				clusterGraphElement.isDirected(), NetworkModel.networkComponentsIDs(networkComponents), clusterNetworkModel);
		// --------- remove clustered etworkComponent from this NetworkModel and add clusterComponent -----
		removeNetworkComponents(networkComponents);
		networkComponents.add(clusterNetworkComponent);
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
	}

	/**
	 * Generates the next unique network component ID in the series n1, n2, n3, ...
	 * 
	 * @return the next unique network component ID
	 */
	public String nextNetworkComponentID() {
		// --- Finds the current maximum network component ID and returns the next one to it. -----
		int startInt = networkComponents.size();
		while (networkComponents.get((NetworkComponent.PREFIX_NETWORK_COMPONENT + startInt)) != null) {
			startInt++;
		}
		return NetworkComponent.PREFIX_NETWORK_COMPONENT + (startInt);
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
	public String nextNodeID(boolean skipNullEntries) {

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

	public boolean mergeNodes(GraphNode node1, GraphNode node2) {
		// Get the Network components from the nodes
		NetworkComponent comp1 = this.getNetworkComponent(node1).iterator().next();
		NetworkComponent comp2 = this.getNetworkComponent(node2).iterator().next();
		// Finding the intersection set of the Graph elements of the two network components
		HashSet<String> intersection = new HashSet<String>(comp1.getGraphElementIDs());
		intersection.retainAll(comp2.getGraphElementIDs());
		// Checking the constraint - Two network components can have maximum one node in common
		if (intersection.size() == 0) {
			// No common node
			for (GraphEdge edge : graph.getIncidentEdges(node2)) {
				// Find the node on the other side of the edge
				GraphNode otherNode = graph.getOpposite(node2, edge);
				// Create a new edge with the same ID and type
				GraphEdge newEdge = new GraphEdge(edge.getId(), edge.getComponentType());

				if (graph.getSource(edge) != null) {
					// if the edge is directed
					if (graph.getSource(edge) == node2)
						graph.addEdge(newEdge, node1, otherNode, EdgeType.DIRECTED);
					else if (graph.getDest(edge) == node2)
						graph.addEdge(newEdge, otherNode, node1, EdgeType.DIRECTED);
				} else {
					// if the edge is undirected
					graph.addEdge(newEdge, node1, otherNode, EdgeType.UNDIRECTED);
				}
				// Removing the old edge from the graph and network model
				graph.removeEdge(edge);
				graphElements.remove(edge.getId());
				graphElements.put(newEdge.getId(), newEdge);
			}
			// Updating the graph element IDs of the component
			comp2.getGraphElementIDs().remove(node2.getId());
			comp2.getGraphElementIDs().add(node1.getId());
			// Removing node2 from the graph and network model
			graph.removeVertex(node2);
			graphElements.remove(node2.getId());
			return true;
		}
		return false;
	}

	/**
	 * Splits the network model at a specified node.
	 * 
	 * @param node2SplitAt the node
	 */
	public void splitNetworkModelAtNode(GraphNode node2SplitAt) {
		// --- Get the components containing the node -------------------------
		HashSet<NetworkComponent> componentHashSet = this.getNetworkComponent(node2SplitAt);
		NetworkComponent containsDistributionNode = componentListContainsDistributionNode(componentHashSet);
		// --- If the component list contains a DistributionNode, -------------
		// --- this component should be the last one in the list! -------------
		Vector<NetworkComponent> componentVector = this.getNetworkComponentHashSetAsVector(componentHashSet);
		if (containsDistributionNode != null) {
			componentVector = this.getNetworkComponentVectorWithDistributionNodeAsLast(componentVector);
		}
		for (int i = 0; i < (componentVector.size() - 1); i++) {
			NetworkComponent component = componentVector.get(i);
			// --- Creating a new Graph node ----------------------------------
			GraphNode newNode = new GraphNode();
			newNode.setId(this.nextNodeID());
			newNode.setPosition(node2SplitAt.getPosition());

			// --- Incident Edges on the node ---------------------------------
			Collection<GraphEdge> incidentEdges = this.graph.getIncidentEdges(node2SplitAt);
			Iterator<GraphEdge> edgeIter = incidentEdges.iterator();
			while (edgeIter.hasNext()) { // for each incident edge
				GraphEdge edge = edgeIter.next();
				// --- If the edge is in comp2 --------------------------------
				if (component.getGraphElementIDs().contains(edge.getId())) {
					// --- Find the node on the other side of the edge --------
					GraphNode otherNode = this.graph.getOpposite(node2SplitAt, edge);
					// --- Shift position of the new node a bit ---------------
					newNode.setPosition(this.getShiftedPosition(otherNode, newNode));

					// --- Create a new edge with the same ID and type --------
					GraphEdge newEdge = new GraphEdge(edge.getId(), edge.getComponentType());
					// --- if the edge is directed ----------------------------
					if (this.graph.getSource(edge) != null) {
						// --- The edge is directed ---------------------------
						if (this.graph.getSource(edge) == node2SplitAt) {
							this.graph.addEdge(newEdge, newNode, otherNode, EdgeType.DIRECTED);
						} else if (this.graph.getDest(edge) == node2SplitAt) {
							this.graph.addEdge(newEdge, otherNode, newNode, EdgeType.DIRECTED);
						}
					} else {
						// --- The edge is undirected -------------------------
						this.graph.addEdge(newEdge, newNode, otherNode, EdgeType.UNDIRECTED);
					}

					// --- Remove old edge from graph and network model -------
					this.graph.removeEdge(edge);
					this.getGraphElements().remove(edge.getId());
					this.getGraphElements().put(newEdge.getId(), newEdge);
				}
			}

			// --- Updating the graph element IDs of the component ------------
			component.getGraphElementIDs().remove(node2SplitAt.getId());
			component.getGraphElementIDs().add(newNode.getId());

			// --- Adding new node to the network model -----------------------
			this.getGraphElements().put(newNode.getId(), newNode);
		} // --- 'end for' for components ---
	}
    
    /**
     * Gets a shifted position for a node in relation to the raster size of the component type settings.
     * @param fixedNode the fixed node
     * @param shiftNode the shift node
     * @return the shifted position
     */
    public Point2D getShiftedPosition(GraphNode fixedNode, GraphNode shiftNode) {
    	
    	double move = this.generalGraphSettings4MAS.getSnapRaster();
    	
    	double fixedNodeX = fixedNode.getPosition().getX();
    	double fixedNodeY = fixedNode.getPosition().getY();
    	
    	double shiftNodeX = shiftNode.getPosition().getX();
    	double shiftNodeY = shiftNode.getPosition().getY();
    	
    	double radians = Math.atan2((shiftNodeY-fixedNodeY), (shiftNodeX-fixedNodeX));
    	
    	shiftNodeX = shiftNodeX - move * Math.cos(radians);
    	shiftNodeY = shiftNodeY - move * Math.sin(radians);
    	
    	Point2D newPosition = new Point2D.Double(shiftNodeX, shiftNodeY);
    	return newPosition;
    }
    
    /**
     * Gets the network component HashSet as vector.
     * @param componentHashSet the component HashSet
     * @return the network component hash as vector
     */
    public Vector<NetworkComponent> getNetworkComponentHashSetAsVector(HashSet<NetworkComponent> componentHashSet) {
    	Vector<NetworkComponent> compVector = new Vector<NetworkComponent>();
    	Iterator<NetworkComponent> compIt = componentHashSet.iterator();		
		while(compIt.hasNext()) {
			compVector.addElement(compIt.next());
		}
		return compVector;
	}

	/**
	 * Returns the network component vector with the DistributionNode as last.
	 * 
	 * @param componentVector the component vector
	 * @return the network component vector with distribution node as last
	 */
	public Vector<NetworkComponent> getNetworkComponentVectorWithDistributionNodeAsLast(Vector<NetworkComponent> componentVector) {
		Vector<NetworkComponent> newComponentVector = new Vector<NetworkComponent>();
		NetworkComponent distributionNodeComponent = null;

		for (int i = 0; i < componentVector.size(); i++) {
			NetworkComponent component = componentVector.get(i);
			if (component.getPrototypeClassName().equals(DistributionNode.class.getName())) {
				distributionNodeComponent = component;
			} else {
				newComponentVector.addElement(component);
			}
		}
		if (distributionNodeComponent != null) {
			newComponentVector.addElement(distributionNodeComponent);
		}
		return newComponentVector;
	}

	/**
	 * Checks, if a component list contains distribution node.
	 * 
	 * @param components the components as HashSet<NetworkComponent>
	 * @return the network component, which is the DistributionNode or null
	 */
	public NetworkComponent componentListContainsDistributionNode(HashSet<NetworkComponent> components) {
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
	 * Sets the general graph settings for the MAS.
	 * 
	 * @param generalGraphSettings4MAS the new general graph settings for the MAS
	 */
	public void setGeneralGraphSettings4MAS(GeneralGraphSettings4MAS generalGraphSettings4MAS) {
		this.generalGraphSettings4MAS = generalGraphSettings4MAS;
	}

	/**
	 * Gets the general graph settings for the MAS.
	 * 
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
	 * 
	 * @param alternativeNetworkModel the alternativeNetworkModel to set
	 */
	public void setAlternativeNetworkModel(HashMap<String, NetworkModel> alternativeNetworkModel) {
		this.alternativeNetworkModel = alternativeNetworkModel;
	}

	/**
	 * Gets the alternative network models.
	 * 
	 * @return the alternativeNetworkModel
	 */
	public HashMap<String, NetworkModel> getAlternativeNetworkModel() {
		if (alternativeNetworkModel == null) {
			alternativeNetworkModel = new HashMap<String, NetworkModel>();
		}
		return alternativeNetworkModel;
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
	 * @param object the object
	 * @return true, if is free node
	 */
	public boolean isFreeNode(Object object) {
		if (object instanceof GraphNode) {
			GraphNode graphNode = (GraphNode) object;
			// --- The number of network components containing this node ------
			HashSet<NetworkComponent> networkComponents = getNetworkComponent(graphNode);
			if (networkComponents.size() == 1) {
				for (NetworkComponent networkComponent : networkComponents) {
					// --- Node is present in only one component and not center of a star ------------------
					if (isStarGraphElement(networkComponent) && isCenterNodeOfStar(graphNode, networkComponent)) {
						return false;
					}
				}
				return true;
			}
			for (NetworkComponent networkComponent : networkComponents) {
				// --- Node is present in several components ------------------
				if (networkComponent.getPrototypeClassName().equals(DistributionNode.class.getName())) {
					return true;
				}
			}
		}
		return false;
	}
}
