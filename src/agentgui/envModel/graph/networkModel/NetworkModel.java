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

import java.awt.Point;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import agentgui.envModel.graph.controller.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.prototypes.DistributionNode;
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
     * Creates a clone of the current instance.
     * 
     * @return the copy
     */
    public NetworkModel getCopy() {

	// ----------------------------------------------------------
	// --- Create a copy of the Graph ---------------------------
	// ----------------------------------------------------------
	Graph<GraphNode, GraphEdge> copyGraph = new SparseGraph<GraphNode, GraphEdge>();
	HashMap<String, GraphElement> copyGraphElements = new HashMap<String, GraphElement>();

	// --- Copy the edges with their nodes of the graph ---------
	Collection<GraphEdge> edgesCollection = this.graph.getEdges();
	GraphEdge[] edges = edgesCollection.toArray(new GraphEdge[edgesCollection.size()]);
	for (int i = 0; i < edges.length; i++) {
	    GraphEdge edge = edges[i];
	    EdgeType edgeType = this.graph.getEdgeType(edge);

	    GraphNode first = this.graph.getEndpoints(edge).getFirst();
	    GraphNode second = this.graph.getEndpoints(edge).getSecond();

	    // --- See if the elements are already there ------------
	    if (copyGraphElements.get(first.getId()) == null) {
		first = first.getCopy();
		copyGraphElements.put(first.getId(), first);
	    } else {
		first = (GraphNode) copyGraphElements.get(first.getId());
	    }
	    if (copyGraphElements.get(second.getId()) == null) {
		second = second.getCopy();
		copyGraphElements.put(second.getId(), second);
	    } else {
		second = (GraphNode) copyGraphElements.get(second.getId());
	    }
	    if (copyGraphElements.get(edge.getId()) == null) {
		edge = edge.getCopy();
		copyGraphElements.put(edge.getId(), edge);
	    } else {
		edge = (GraphEdge) copyGraphElements.get(edge.getId());
	    }

	    // --- Add the edge and their components to the graph ---
	    copyGraph.addEdge(edge, first, second, edgeType);

	}

	// -- Create a copy of the networkComponents ----------------
	HashMap<String, NetworkComponent> copyOfComponents = new HashMap<String, NetworkComponent>(this.networkComponents);

	// -- Create a copy of the generalGraphSettings4MAS ---------
	GeneralGraphSettings4MAS copyOfGeneralGraphSettings4MAS = null;
	if (this.generalGraphSettings4MAS != null) {
	    copyOfGeneralGraphSettings4MAS = this.generalGraphSettings4MAS.getCopy();
	}

	// -- Create a copy of the alternativeNetworkModel ----------
	HashMap<String, NetworkModel> copyOfAlternativeNetworkModel = null;
	if (this.alternativeNetworkModel != null) {
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
     * Returns a list of all GridComponents.
     * 
     * @return The list
     */
    public Collection<GraphEdge> getEdges() {
    	return graph.getEdges();
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
     * This method adds a NetworkComponent to the GridModel's networkComponents HashMap, using its' ID as key.
     * 
     * @param networkComponent The NetworkComponent to add
     */
    public void addNetworkComponent(NetworkComponent networkComponent) {
    	networkComponents.put(networkComponent.getId(), networkComponent);
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
		NetworkComponent networkComponent = new NetworkComponent(id, type, prototypeClassName, directed);
		addNetworkComponent(networkComponent);
		networkComponent.setGraphElements(graphElements);
		refreshGraphElements();
		return networkComponent;
    }

    /**
     * Adds the network component.
     * 
     * @param id the id
     * @param type the type
     * @param prototypeClassName the prototype class name
     * @param agentClassName the agent class name
     * @param directed the directed
     * @param graphElements the graph elements
     * @return the network component
     */
    public NetworkComponent addNetworkComponent(String id, String type, String prototypeClassName, String agentClassName, boolean directed, HashSet<GraphElement> graphElements) {
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
     * This method removes a NetworkComponent from the GridModel's networkComponents HashMap, using its' ID as key.
     * 
     * @param component The NetworkComponent to remove
     */
    public void removeNetworkComponent(NetworkComponent component) {

    	if (component.getPrototypeClassName().equals(DistributionNode.class.getName())) {
    		// ----------------------------------------------------------------
    		// --- A DistributionNode has to be removed -----------------------
    		// ----------------------------------------------------------------
    		String graphElementID = component.getGraphElementIDs().iterator().next();
    		GraphNode graphNode = (GraphNode) this.getGraphElement(graphElementID);

    		this.splitNetworkModelAtNode(graphNode);
    		this.graph.removeVertex(graphNode);
    		this.getGraphElements().remove(graphNode.getId());
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
    		    	this.getGraphElements().remove(graphElement.getId());
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
		// --- Get the components from the controllers GridModel ----
		Iterator<NetworkComponent> components = this.getNetworkComponents().values().iterator();

		// --- iterating through all network components -------------
		int count = 0;
		while (components.hasNext()) { 
		    NetworkComponent comp = components.next();
		    // --- check if the component contains the current node -
		    if (comp.getGraphElementIDs().contains(node.getId())) {
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
        	    	if (netComponent!=networkComponent && comps.contains(netComponent)==false) {
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
	 * @param node - A GraphNode
	 * @return HashSet<NetworkComponent> - The set of components which contain the node
	 */
	public HashSet<NetworkComponent> getNetworkComponent(GraphNode node){						
		// Get the components from the controllers GridModel
		HashSet<NetworkComponent>  compSet = new HashSet<NetworkComponent>();
		Iterator<NetworkComponent> components = this.getNetworkComponents().values().iterator();						
		while(components.hasNext()){ // iterating through all network components
			NetworkComponent comp = components.next();
			// check if the component contains the given node
			if(comp.getGraphElementIDs().contains(node.getId())){
				compSet.add(comp);
			}
		}
		return compSet;		
	}
	
    /**
     * Gets the network components.
     * @return the networkComponents
     */
    public HashMap<String, NetworkComponent> getNetworkComponents() {
    	return networkComponents;
    }

    /**
     * Sets the network components.
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
    	while (networkComponents.get((NetworkComponent.PREFIX_NETWORK_COMPONENT + startInt))!=null ) {
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

    /**
     * Splits the network model at a specified node.
     * @param node the node
     */
    public void splitNetworkModelAtNode(GraphNode node) {
    	
		Graph<GraphNode,GraphEdge> graph = this.getGraph();

		//Get the components containing the node
		Iterator<NetworkComponent> compIter = this.getNetworkComponent(node).iterator();		
//		NetworkComponent comp1 = compIter.next();
		NetworkComponent comp2 = compIter.next();
		
		//Creating the new Graph node
		GraphNode newNode = new GraphNode();
		newNode.setId(this.nextNodeID());
			//Shifting position a bit
		newNode.setPosition(new Point((int)node.getPosition().getX()-20, (int)node.getPosition().getY()-20));
		node.setPosition(new Point((int)node.getPosition().getX()+20, (int)node.getPosition().getY()+20));
		
		//Incident Edges on the node
		Collection<GraphEdge> incidentEdges = graph.getIncidentEdges(node);		
		Iterator<GraphEdge> edgeIter = incidentEdges.iterator();
		while(edgeIter.hasNext()){ // for each incident edge
			GraphEdge edge = edgeIter.next();
			//If the edge is in comp2
			if(comp2.getGraphElementIDs().contains(edge.getId())){						
				//Find the node on the other side of the edge
				GraphNode otherNode = graph.getOpposite(node,edge);
				//Create a new edge with the same ID and type
				GraphEdge newEdge = new GraphEdge(edge.getId(), edge.getComponentType());				
				//if the edge is directed
				if(graph.getSource(edge)!=null) 
				{
					if(graph.getSource(edge) == node)
						graph.addEdge(newEdge,newNode, otherNode, EdgeType.DIRECTED);
					else if(graph.getDest(edge) == node)
						graph.addEdge(newEdge,otherNode, newNode, EdgeType.DIRECTED);
				}
				// if the edge is undirected
				else 
					graph.addEdge(newEdge,newNode, otherNode, EdgeType.UNDIRECTED);
				
				//Removing the old edge from the graph and network model
				graph.removeEdge(edge);
				this.getGraphElements().remove(edge.getId());
				this.getGraphElements().put(newEdge.getId(),newEdge);
			}
		}
		
		//Updating the graph element IDs of the component
		comp2.getGraphElementIDs().remove(node.getId());
		comp2.getGraphElementIDs().add(newNode.getId());
		
		//Adding new node to the network model
		this.getGraphElements().put(newNode.getId(),newNode);
    	
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

}
