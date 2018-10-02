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
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import agentgui.core.classLoadService.ClassLoadServiceUtility;
import agentgui.envModel.graph.GraphGlobals;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.prototypes.ClusterGraphElement;
import agentgui.envModel.graph.prototypes.DistributionNode;
import agentgui.envModel.graph.prototypes.GraphElementPrototype;
import agentgui.envModel.graph.prototypes.StarGraphElement;
import agentgui.envModel.graph.visualisation.DisplayAgent;
import agentgui.simulationService.environment.DisplaytEnvironmentModel;
import de.enflexit.common.SerialClone;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.io.GraphIOException;


/**
 * The Environment Network Model. This class encapsulates a JUNG graph representing a grid, with edges representing the grid components.
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class NetworkModel extends DisplaytEnvironmentModel {

	private static final long serialVersionUID = -5712689010090750522L;

	/** This attribute stores layout settings like the DomainSettings and the ComponentTypeSettings. */
	private GeneralGraphSettings4MAS generalGraphSettings4MAS;
	
	/** The original JUNG graph created or imported in the application. */
	private Graph<GraphNode, GraphEdge> graph;
	/** HashMap that provides faster access to the GraphElement's. */
	private HashMap<String, GraphElement> graphElements;
	/** A list of all NetworkComponents in the NetworkModel, accessible by ID. */
	private TreeMap<String, NetworkComponent> networkComponents;
	/** A list of {@link GraphElement} (that are {@link GraphNode} or {@link GraphEdge}) mapped to one or more {@link NetworkComponent}'s */
	private transient HashMap<GraphElement, NetworkComponents> graphElementToNetworkComponents;  
	
	/** The Hash of NetworkComponentAdapter. */
	private transient HashMap<String, NetworkComponentAdapter> networkComponentAdapterHash;
	
	/**
	 * This HashMap can hold alternative NetworkModel's that can be used to 
	 * reduce the complexity of the original graph (e.g after clustering). 
	 * The NetworkModel's placed in this HashMap will be also displayed 
	 * by the {@link DisplayAgent}.
	 */
	private HashMap<String, NetworkModel> alternativeNetworkModel;

	/** The outer network components of this NetworkModel with no Connections */
	private transient ArrayList<String> outerNetworkComponents;
	private transient int connectionsOfBiggestBranch;


	/**
	 * Default constructor.
	 */
	public NetworkModel() {
		this.graphElements = new HashMap<String, GraphElement>();
		this.networkComponents = new TreeMap<String, NetworkComponent>();
	}

	/**
	 * Sets the general graph settings for the MAS.
	 * @param generalGraphSettings4MAS the new general graph settings for the MAS
	 */
	public void setGeneralGraphSettings4MAS(GeneralGraphSettings4MAS generalGraphSettings4MAS) {
		this.generalGraphSettings4MAS = generalGraphSettings4MAS;
		this.resetGraphElementLayout();
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
	 * Returns the JUNG graph.
	 * @return the JUNG Graph
	 */
	public Graph<GraphNode, GraphEdge> getGraph() {
		if (graph==null) {
			graph = new SparseGraph<GraphNode, GraphEdge>();
		}
		return graph;
	}
	/**
	 * Sets the the graph of the network model.
	 * @param newGraph the new graph
	 */
	public void setGraph(Graph<GraphNode, GraphEdge> newGraph) {
		this.graph = newGraph;
		this.graphElements = new HashMap<String, GraphElement>();
		this.refreshGraphElements();
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
	 * Gets the network components.
	 * @return the networkComponents
	 */
	public TreeMap<String, NetworkComponent> getNetworkComponents() {
		return networkComponents;
	}
	/**
	 * Sets the network components.
	 * @param networkComponents the networkComponents to set
	 */
	public void setNetworkComponents(TreeMap<String, NetworkComponent> networkComponents) {
		this.networkComponents = networkComponents;
		this.refreshGraphElements();
	}
	
	/**
	 * Returns the graph element to network component hash.
	 * @return the graph element to network component hash
	 */
	public synchronized HashMap<GraphElement, NetworkComponents> getGraphElementToNetworkComponentHash() {
		if (graphElementToNetworkComponents==null) {
			graphElementToNetworkComponents = new HashMap<GraphElement, NetworkComponents>();
			this.refreshGraphElements();	
		}
		return graphElementToNetworkComponents;
	}
	/**
	 * Clears the graph element to network component hash.
	 */
	public void clearGraphElementToNetworkComponentHash() {
		this.graphElementToNetworkComponents = new HashMap<GraphElement, NetworkComponents>();
	}
	
	/**
	 * Adds a new relation reminder between a GraphElement and a NetworkComponent.
	 * @param graphElement the graph element
	 * @param networkComponent the network component
	 * @return true, if successful
	 */
	public boolean addGraphElementToNetworkComponentRelation(GraphElement graphElement, NetworkComponent networkComponent) {
		
		boolean done = false;
		if (graphElement==null) return false;
		if (networkComponent==null) return false;
		
		NetworkComponents netComps = this.getGraphElementToNetworkComponentHash().get(graphElement);
		if (netComps==null) {
			netComps = new NetworkComponents();
			netComps.add(networkComponent);
			this.getGraphElementToNetworkComponentHash().put(graphElement, netComps);
			done = true;
			
		} else {
			if (netComps.contains(networkComponent)) {
				done = true;
			} else {
				done = netComps.add(networkComponent);
			}	
		}
		return done;
	}
	/**
	 * Removes a relation reminder between a GraphElement and a NetworkComponent.
	 * @param graphElement the graph element
	 * @param networkComponent the network component
	 */
	private boolean removeGraphElementToNetworkComponentRelation(GraphElement graphElement, NetworkComponent networkComponent) {
		
		boolean done = false;
		if (graphElement==null) return false;
		if (networkComponent==null) return false;
		
		NetworkComponents netComps = this.getGraphElementToNetworkComponentHash().get(graphElement);
		if (netComps==null) {
			done = true;
			
		} else {
			if (netComps.contains(networkComponent)) {
				done = netComps.remove(networkComponent);	
			} else {
				done = true;
			}
			if (netComps.size()==0) {
				this.getGraphElementToNetworkComponentHash().remove(graphElement);
			}
		}
		return done;
	}
	/**
	 * Removes the specified GraphElement from the GraphElementToNetworkComponentHash.
	 * @param graphElement the {@link GraphElement} to remove
	 * @see #getGraphElementToNetworkComponentHash()
	 */
	public void removeGraphElementToNetworkComponent(GraphElement graphElement) {
		this.getGraphElementToNetworkComponentHash().remove(graphElement);
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

	/**
	 * Creates a clone of the current instance.
	 * @return the copy
	 */
	public NetworkModel getCopy() {
		
		synchronized (this) {

			NetworkModel netModel = null;	
			boolean cloneInstance = true;
			if (cloneInstance==true) {
				// -------------------------------------------------------
				// --- Make a serialisation copy the NetworkModel -------- 
				// -------------------------------------------------------
				try {
					netModel = SerialClone.clone(this);
					netModel.refreshGraphElements();
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
			} else {
				// -------------------------------------------------------
				// ---- Alternative approach to copy the NetworkModel ----
				// -------------------------------------------------------
				netModel = new NetworkModel();
				
				// --- Create a copy of the generalGraphSettings4MAS ----
				GeneralGraphSettings4MAS copyOfGeneralGraphSettings4MAS = null;
				if (this.generalGraphSettings4MAS != null) {
					copyOfGeneralGraphSettings4MAS = this.generalGraphSettings4MAS.getCopy();
				}
				netModel.setGeneralGraphSettings4MAS(copyOfGeneralGraphSettings4MAS);

				// --- Copy the graph -----------------------------------
				netModel.setGraph(this.getGraphCopy());
		
				// --- Create a copy of the networkComponents -----------
				TreeMap<String, NetworkComponent> copyOfComponents = new TreeMap<String, NetworkComponent>();
				for (NetworkComponent networkComponent : new ArrayList<NetworkComponent>(this.networkComponents.values())) {
					try {
						// --- Copy NetworkComponent -------------------- 
						if (networkComponent instanceof ClusterNetworkComponent) {
							ClusterNetworkComponent networkComponentCopy = ((ClusterNetworkComponent) networkComponent).getCopy(this);
							copyOfComponents.put(networkComponentCopy.getId(), networkComponentCopy);
						} else {
							NetworkComponent networkComponentCopy = networkComponent.getCopy(this);
							copyOfComponents.put(networkComponentCopy.getId(), networkComponentCopy);
						}
						
					} catch (Exception ex) {
						System.err.println("Error during copy of network component " + networkComponent.getId());
						ex.printStackTrace();
					}
				}
				netModel.setNetworkComponents(copyOfComponents);
				netModel.refreshGraphElements();
		
				// ------------------------------------------------------
				// -- Create a copy of the alternativeNetworkModel ------
				// ------------------------------------------------------
				HashMap<String, NetworkModel> copyOfAlternativeNetworkModel = null;
				if (this.alternativeNetworkModel != null) {
					copyOfAlternativeNetworkModel = new HashMap<String, NetworkModel>();
					Vector<String> altNetModelsName = new Vector<String>(this.alternativeNetworkModel.keySet());
					for (String networkModelName : altNetModelsName) {
						NetworkModel networkModel = this.alternativeNetworkModel.get(networkModelName);
						networkModel = networkModel.getCopy();
						copyOfAlternativeNetworkModel.put(networkModelName, networkModel);
					}
				}
				netModel.setAlternativeNetworkModel(copyOfAlternativeNetworkModel);
				
			}
			return netModel;
		} // end synchronized
	}

	/**
	 * Copy graph and graph elements.
	 * @param netModel the net model
	 */
	private Graph<GraphNode, GraphEdge> getGraphCopy() {

		Graph<GraphNode, GraphEdge> copyGraph = new SparseGraph<GraphNode, GraphEdge>();

		// --- Copy all nodes and remind the relation between ID and new instance -------
		Collection<GraphNode> nodesCollection = this.getGraph().getVertices();
		GraphNode[] nodes = nodesCollection.toArray(new GraphNode[nodesCollection.size()]);
		HashMap<String, GraphNode> graphNodeCopies = new HashMap<String, GraphNode>();
		for (int i = 0; i < nodes.length; i++) {
			GraphNode node = nodes[i];
			GraphNode nodeCopy = node.getCopy(this);
			graphNodeCopies.put(node.getId(), nodeCopy);
			copyGraph.addVertex(nodeCopy);
		}

		// --- Copy the edges -----------------------------------------------------------
		Collection<GraphEdge> edgesCollection = this.getGraph().getEdges();
		GraphEdge[] edges = edgesCollection.toArray(new GraphEdge[edgesCollection.size()]);
		for (int i = 0; i < edges.length; i++) {
			GraphEdge edge = edges[i];
			EdgeType edgeType = this.getGraph().getEdgeType(edge);
			GraphNode first = this.getGraph().getEndpoints(edge).getFirst();
			GraphNode second = this.getGraph().getEndpoints(edge).getSecond();

			GraphNode copyFirst = graphNodeCopies.get(first.getId());
			GraphNode copySecond = graphNodeCopies.get(second.getId());
			GraphEdge copyEdge = edge.getCopy(this);
			copyGraph.addEdge(copyEdge, copyFirst, copySecond, edgeType);
		}
		return copyGraph;
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
			GraphElement ge = this.getGraphElement(graphElementID);
			if (ge != null) {
				elements.add(ge);
			}
		}
		return elements;
	}

	/**
	 * Reloads the the GraphElementsMap.
	 */
	public void refreshGraphElements() {
		if (this.getGraph()!=null) {
			this.graphElements = new HashMap<String, GraphElement>();
			this.registerGraphElement(this.getGraph().getVertices().toArray(new GraphNode[0]));
			this.registerGraphElement(this.getGraph().getEdges().toArray(new GraphEdge[0]));
		}
		
		// --- Refresh the reminder of the relation between GraphElement and NetworkComonent ------ 
		this.clearGraphElementToNetworkComponentHash();
		for (NetworkComponent nc : this.networkComponents.values()) {
			for (String graphElementID : nc.getGraphElementIDs()) {
				GraphElement ge = this.graphElements.get(graphElementID);
				if (ge==null) {
					System.err.println("RefreshGraphElements: Could not find GraphElement '" + graphElementID + "' for NetworkComponent '" + nc.getId() + "'");
				} else {
					this.addGraphElementToNetworkComponentRelation(ge, nc);	
				}
			}
		}
	}

	/**
	 * Register all GraphElemnents used when adding a Component.
	 * @param graphElements the graph elements
	 */
	private void registerGraphElement(GraphElement[] graphElements) {
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
		return this.addNetworkComponent(networkComponent, true);
	}
	
	/**
	 * Adds a network component to the NetworkModel.
	 *
	 * @param networkComponent the network component
	 * @param refreshGraphElements set true, if the graph elements have to be refreshed
	 * @return the network component
	 */
	public NetworkComponent addNetworkComponent(NetworkComponent networkComponent, boolean refreshGraphElements) {
		this.networkComponents.put(networkComponent.getId(), networkComponent);
		if (refreshGraphElements==true) {
			this.refreshGraphElements();
		}
		return networkComponent;
	}

	/**
	 * Rename a graph element (GraphNode or GraphEdge). In case that a GraphNode
	 * is renamed, the changes will also apply to all connected NetworkComponent's. 
	 *
	 * @param oldGraphNodeID the old GraphNode ID
	 * @param newGraphNodeID the new GraphNode ID
	 */
	public void renameGraphNode(String oldGraphNodeID, String newGraphNodeID) {
		
		// --- Rename the GraphNode ----------------------------------------
		GraphElement graphElement = graphElements.get(oldGraphNodeID);
		if (graphElement instanceof GraphNode) {
			
			// --- Look for NetworkComponents, that are knowing this graphElement -
			HashSet<NetworkComponent> components = this.getNetworkComponents((GraphNode) graphElement);
			for (NetworkComponent component : components) {
				// --- Replace the old ID with the new one ---------- 
				HashSet<String> compIDs = component.getGraphElementIDs();
				compIDs.remove(oldGraphNodeID);
				compIDs.add(newGraphNodeID);
			}
			
			graphElements.remove(oldGraphNodeID);
			graphElement.setId(newGraphNodeID);
			graphElements.put(newGraphNodeID, graphElement);
		}
		
	}

	/**
	 * Renames a NetworkComponent.
	 * 
	 * @param oldCompID the old ID of the NetworkComponent 
	 * @param newCompID the new ID of the NetworkComponent
	 */
	public void renameNetworkComponent(String oldCompID, String newCompID) {
		
		NetworkComponent networkComponent = this.networkComponents.get(oldCompID);
		if (networkComponent!=null) {
			HashSet<String> newGraphElementIDs = new HashSet<String>(networkComponent.getGraphElementIDs());
			// --- Rename the corresponding edges of the network component ----
			for (String oldGraphElementID : networkComponent.getGraphElementIDs()) {
				String newGraphElementID = oldGraphElementID.replaceFirst(oldCompID, newCompID);
				if (newGraphElementID.equals(oldGraphElementID)==false) {
					// --- Delete old reference -------------------------------
					newGraphElementIDs.remove(oldGraphElementID);
					// --- rename the edges ----------------------------------- 
					GraphElement graphElement = this.graphElements.get(oldGraphElementID);
					if (graphElement instanceof GraphEdge) {
						this.graphElements.remove(oldGraphElementID);
						graphElement.setId(newGraphElementID);
						this.graphElements.put(newGraphElementID, graphElement);	
					}
					// --- Add new reference ----------------------------------
					newGraphElementIDs.add(newGraphElementID);	
				}
			}

			// --- Update the NetworkComponent --------------------------------
			networkComponent.setGraphElementIDs(newGraphElementIDs);
			networkComponent.setId(newCompID);
			this.networkComponents.remove(oldCompID);
			this.networkComponents.put(newCompID, networkComponent);
			
			// --- Update the NetworkComponent-Layout -------------------------
			for (String graphElementID : networkComponent.getGraphElementIDs()) {
				GraphElement graphElement = this.graphElements.get(graphElementID);
				if (graphElement!=null) {
					graphElement.resetGraphElementLayout(this);
				}
			}
			
		}
		
	}

	/**
	 * This method removes a NetworkComponent from the GridModel's networkComponents 
	 * HashMap, using its' ID as key.
	 * 
	 * @param networkComponent The NetworkComponent to remove
	 */
	public void removeNetworkComponent(NetworkComponent networkComponent) {
		this.removeNetworkComponent(networkComponent, true);
	}
	
	/**
	 * This method removes a NetworkComponent from the GridModel's networkComponents
	 * HashMap, using its' ID as key.
	 *
	 * @param networkComponent The NetworkComponent to remove
	 * @param refreshGraphElements true, if the graph elements have to be refreshed
	 */
	public void removeNetworkComponent(NetworkComponent networkComponent, boolean refreshGraphElements) {
		this.removeNetworkComponent(networkComponent, true, refreshGraphElements);
	}
	
	/**
	 * This method removes a NetworkComponent from the GridModel's networkComponents
	 * HashMap, using its' ID as key.
	 *
	 * @param networkComponent The NetworkComponent to remove
	 * @param removeDistributionNodes the remove distribution nodes
	 * @param refreshGraphElements true, if the graph elements have to be refreshed
	 */
	public void removeNetworkComponent(NetworkComponent networkComponent, boolean removeDistributionNodes, boolean refreshGraphElements) {

		// --- Make sure that the NetworkComponent is on its own ----
		Vector<GraphNodePairs> graphNodePairsToReMerge = new Vector<>();
		HashSet<GraphElement> graphElements = this.getGraphElementsOfNetworkComponent(networkComponent, new GraphNode());
		if (graphElements!=null) {
			for (GraphElement graphElement : graphElements) {
				GraphNode node = (GraphNode) graphElement;
				boolean isDistributionGraphNode = this.isDistributionNode(node)!=null;
				if (isDistributionGraphNode==false || (isDistributionGraphNode==true && removeDistributionNodes==true)) {
					HashSet<NetworkComponent> networkComponents = this.getNetworkComponents(node);
					if (networkComponents.size() > 1) {
						graphNodePairsToReMerge.add(this.splitNetworkModelAtNode(node));
					}	
				}
			}	
		}
		
		// --- Remove the graph elements of this component ----------
		for (GraphElement graphElement : this.getGraphElementsFromNetworkComponent(networkComponent)) {

			if (graphElement instanceof GraphEdge) {
				this.getGraph().removeEdge((GraphEdge) graphElement);
				this.getGraphElements().remove(graphElement.getId());
				
			} else if (graphElement instanceof GraphNode) {
				GraphNode node = (GraphNode) graphElement;
				boolean isDistributionGraphNode = this.isDistributionNode(node)!=null;
				if (isDistributionGraphNode==false || (isDistributionGraphNode==true && removeDistributionNodes==true)) {
					this.getGraph().removeVertex((GraphNode) graphElement);
					this.getGraphElements().remove(graphElement.getId());
				}
				
			}
			this.removeGraphElementToNetworkComponentRelation(graphElement, networkComponent);

		}
		
		// --- Remove the NetworkComponent --------------------------
		this.networkComponents.remove(networkComponent.getId());
		
		// --- Re-Merge the previously splitted nodes, if needed ---- 
		for (GraphNodePairs graphNodePairs : graphNodePairsToReMerge) {
			this.mergeNodes(graphNodePairs);
		}
		
		// --- Refresh the GraphElements ----------------------------
		if (refreshGraphElements==true) {
			this.refreshGraphElements();
		}
	}

	/**
	 * Removes a set of network components.
	 * @param networkComponents the network components
	 */
	public HashSet<NetworkComponent> removeNetworkComponents(HashSet<NetworkComponent> networkComponents) {
		
		HashSet<GraphElement> graphNodes2Remove = new HashSet<GraphElement>();
		HashSet<GraphElement> graphEdges2Remove = new HashSet<GraphElement>();
		
		for (NetworkComponent networkComponent : networkComponents) {
			
			// --- Remove from the list of NetworkComponents --------
			this.networkComponents.remove(networkComponent.getId());
			
			// --- Get graph elements of the components -------------
			for (String graphElemID : networkComponent.getGraphElementIDs()) {
				GraphElement graphElement = this.getGraphElement(graphElemID);
				if (graphElement instanceof GraphNode) {
					if (graphNodes2Remove.contains(graphElement)==false) {
						graphNodes2Remove.add(graphElement);
					}
				} else if (graphElement instanceof GraphEdge) {
					if (graphEdges2Remove.contains(graphElement)==false) {
						graphEdges2Remove.add(graphElement);
					}
				}
			}
		}
		
		// --- Remove edges from the graph --------------------------
		for (GraphElement graphElement : graphEdges2Remove) {
			this.getGraph().removeEdge((GraphEdge) graphElement);
		}
		// --- Remove edges from the graph --------------------------
		for (GraphElement graphElement : graphNodes2Remove) {
			GraphNode graphNode = (GraphNode) graphElement;
			if (this.getGraph().getIncidentEdges(graphNode).size() < 2) {
				this.getGraph().removeVertex(graphNode);
			} 
		}
		
		this.refreshGraphElements();
		return networkComponents;
	}

	/**
	 * Removes all network components if not in the specified list.
	 * @param networkComponentsToKeep the network components to keep when deleting network components
	 */
	public HashSet<NetworkComponent> removeNetworkComponentsInverse(HashSet<NetworkComponent> networkComponentsToKeep) {
		HashSet<NetworkComponent> removed = new HashSet<NetworkComponent>();
		HashSet<String> networkComponentIDs = this.getNetworkComponentsIDs(networkComponentsToKeep);
		for (NetworkComponent networkComponent : new ArrayList<NetworkComponent>(this.networkComponents.values())) {
			if (networkComponentIDs.contains(networkComponent.getId())==false) {
				removeNetworkComponent(networkComponent, true, false);
				removed.add(networkComponent);
			}
		}
		this.refreshGraphElements();
		return removed;
	}
	
	/**
	 * Extracts Network IDs from NetworkComponenList and returns an ID List.
	 * @param networkComponents the network components
	 * @return HashSet<String> of the IDs
	 */
	private HashSet<String> getNetworkComponentsIDs(HashSet<NetworkComponent> networkComponents) {
		HashSet<String> networkComponentIDs = new HashSet<String>();
		for (NetworkComponent networkComponent : networkComponents) {
			networkComponentIDs.add(networkComponent.getId());
		}
		return networkComponentIDs;
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
		if (id==null || id.equals("")) {
			return null;
		} else {
			return networkComponents.get(id);	
		}
	}

	/**
	 * Returns the neighbour NetworkComponent's based on a Vector of NetworkComponent's.
	 *
	 * @param networkComponents the network components
	 * @return the neighbour network components
	 */
	public Vector<NetworkComponent> getNeighbourNetworkComponents(Vector<NetworkComponent> networkComponents) {
		Vector<NetworkComponent> neighbourNetworkComponents = new Vector<NetworkComponent>();
		for (NetworkComponent networkComponent : networkComponents) {
			Vector<NetworkComponent> neighboursFound = getNeighbourNetworkComponents(networkComponent);
			for (NetworkComponent neighbour : neighboursFound) {
				if (neighbourNetworkComponents.contains(neighbour)==false) {
					neighbourNetworkComponents.add(neighbour);	
				}
			}
		}
		return neighbourNetworkComponents;
	}

	/**
	 * Returns the neighbour NetworkComponent's of a single NetworkComponent.
	 * 
	 * @param networkComponent the network component
	 * @return the neighbour network components
	 */
	public Vector<NetworkComponent> getNeighbourNetworkComponents(NetworkComponent networkComponent) {
		Vector<NetworkComponent> comps = null;
		if (networkComponent!=null) {
			comps = new Vector<NetworkComponent>();
			for (GraphNode node : this.getNodesFromNetworkComponent(networkComponent)) {
				NetworkComponents componetsFound = this.getGraphElementToNetworkComponentHash().get(node);
				for (NetworkComponent nc : componetsFound) {
					if (nc!= networkComponent && comps.contains(nc)==false) {
						comps.add(nc);
					}
				}
			}			
		}
		return comps;
	}

	/**
	 * Returns all {@link NetworkComponent}s of the network model ascending sorted by the numeric value of the ID of the component.
	 * @return an ascending sorted NetworkComponent vector sorted
	 */
	public Vector<NetworkComponent> getNetworkComponentVectorSorted() {
		return this.getNetworkComponentVectorSorted(true);
	}
	/**
	 * Returns all {@link NetworkComponent}s of the network model sorted by the numeric value of the ID of the component.
	 * @param ascending set true to sort ascending, false to sort descending
	 * @return a sorted NetworkComponent vector sorted
	 */
	public Vector<NetworkComponent> getNetworkComponentVectorSorted(final boolean ascending) {
		
		Vector<NetworkComponent> netCompVector = new Vector<NetworkComponent>();
		netCompVector.addAll(this.getNetworkComponents().values());
		
		Comparator<NetworkComponent> comp = new Comparator<NetworkComponent>() {
			@Override
			public int compare(NetworkComponent netComp1, NetworkComponent netComp2) {
				// --- Check the pure string case first ------------- 
				String n1NumberString = netComp1.getId().replaceAll("\\D+","");
				String n2NumberString = netComp2.getId().replaceAll("\\D+","");
				if (n1NumberString.isEmpty() && n2NumberString.isEmpty()) {
					if (ascending==true) {
						return netComp1.getId().compareTo(netComp2.getId());
					} else {
						return netComp2.getId().compareTo(netComp1.getId());
					}	
				}
				// --- In case of available numbers -----------------
				if (n1NumberString==null || n1NumberString.isEmpty()==true) n1NumberString = "0";		
				if (n2NumberString==null || n2NumberString.isEmpty()==true) n2NumberString = "0";
				Integer n1 = Integer.parseInt(n1NumberString);
				Integer n2 = Integer.parseInt(n2NumberString);
				if (ascending==true) {
					return n1.compareTo(n2);
				} else {
					return n2.compareTo(n1);
				}
			}
		};

		Collections.sort(netCompVector, comp);
		return netCompVector;
	}
	
	/**
	 * Gets the network component by graph edge id.
	 * 
	 * @param graphEdge the graph edge
	 * @return the network component by graph edge id
	 */
	public NetworkComponent getNetworkComponent(GraphEdge graphEdge) {
		
		NetworkComponent ncFound = null;
		NetworkComponents nCompsFound = this.getGraphElementToNetworkComponentHash().get(graphEdge);
		if (nCompsFound!=null && nCompsFound.isEmpty()==false) {
			// --- Can be just on result ----------------------------
			ncFound = nCompsFound.iterator().next();
			
		} else {
			// --- Search manually ----------------------------------
			for (NetworkComponent networkComponent : new ArrayList<NetworkComponent>(this.networkComponents.values())) {
				if (networkComponent.getGraphElementIDs().contains(graphEdge.getId())) {
					ncFound = networkComponent;
					break;
				}
			}	
		}
		return ncFound;
	}

	/**
	 * Gets all networkComponents contained in a GraphNode Set
	 *
	 * @param graphNodes the graph nodes
	 * @return the network components
	 */
	public HashSet<NetworkComponent> getNetworkComponents(Set<GraphNode> graphNodes) {
		HashSet<NetworkComponent> networkComponents = new HashSet<NetworkComponent>();
		for (GraphNode graphNode : graphNodes) {
			networkComponents.addAll(this.getNetworkComponents(graphNode));
		}
		return networkComponents;
	}

	/**
	 * Returns the {@link NetworkComponent}'s that are fully selected by the given set of GraphNodes.<br>
	 * As an example: if you have selected one vertex of a simple directed edge with two vertices, this
	 * method will return null.  
	 *
	 * @param graphNodes the set of selected {@link GraphNode}'s
	 * @return the {@link NetworkComponent}'s that are fully selected by the given nodes and edges
	 */
	public HashSet<NetworkComponent> getNetworkComponentsFullySelected(Set<GraphNode> graphNodes) {
		
		HashSet<NetworkComponent> componentsFound = new HashSet<NetworkComponent>();
		HashSet<NetworkComponent> componentsAffected = this.getNetworkComponents(graphNodes);

		// --- Take affected components and run through them --------
		for (NetworkComponent networkComponent : componentsAffected) {
			
			int graphElementsInSelection = 0; 
			HashSet<GraphElement> graphElementsOfNetworkComponent = this.getGraphElementsOfNetworkComponent(networkComponent, new GraphNode());
			for (GraphElement element : graphElementsOfNetworkComponent) {
				if (graphNodes.contains(element)) {
					graphElementsInSelection++;
				}
			}
			if (graphElementsInSelection==graphElementsOfNetworkComponent.size()) {
				componentsFound.add(networkComponent);
			}
			
		}
		
		if (componentsFound.size()==0) componentsFound = null;
		return componentsFound;
		
	}
	
	
	/**
	 * Extracts the ID's of the GraphElements of the specified NetworkComponent. The parameter 'searchForInstance' can be
	 * an exemplary instance of {@link GraphNode}, {@link GraphEdge} or null. This can be seen as a filter. 
	 * If a {@link GraphNode} is specified, the method will return all GraphNodes of the {@link NetworkComponent}. 
	 * The same applies for an instance of {@link GraphEdge}, while with Null the method will returns all
	 * {@link GraphElement}'s.
	 *
	 * @param networkComponent the network component
	 * @param searchForInstance the search for instance
	 * @return the hash set
	 */
	public HashSet<String> extractGraphElementIDs(NetworkComponent networkComponent, GraphElement searchForInstance) {
		
		// --- Create the result set -----------------
		HashSet<String> elementIDsFound = new HashSet<String>();
		
		// --- Check the GraphElements ---------------
		Set<String> elements = networkComponent.getGraphElementIDs();
		for (String elementID : elements) {
			GraphElement element = this.getGraphElement(elementID);
			if (element!=null) {
				if (searchForInstance==null) {
					// --- search for everything -----
					elementIDsFound.add(elementID);
				} else if (searchForInstance instanceof GraphNode) {
					// --- search for GraphNode's ----
					if (element instanceof GraphNode) {
						elementIDsFound.add(elementID);
					}
				} else if (searchForInstance instanceof GraphEdge) {
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
	 * Returns the graph elements of a specified NetworkComponent. The parameter 'searchForInstance' can be
	 * an exemplary instance of {@link GraphNode}, {@link GraphEdge} or null. This can be seen as a filter. 
	 * If a {@link GraphNode} is specified, the method will return all GraphNodes of the {@link NetworkComponent}. 
	 * The same applies for an instance of {@link GraphEdge}, while with Null the method will returns all
	 * {@link GraphElement}'s.
	 *
	 * @param networkComponent the network component
	 * @param searchForInstance the search for instance
	 * @return the graph elements of network component
	 */
	public HashSet<GraphElement> getGraphElementsOfNetworkComponent(NetworkComponent networkComponent, GraphElement searchForInstance) {
		
		HashSet<GraphElement> graphElements = null;
		HashSet<String> graphElementIDs = this.extractGraphElementIDs(networkComponent, searchForInstance);
		if (graphElementIDs!=null & graphElementIDs.size()!=0) {
			graphElements = new HashSet<GraphElement>();
			for (String graphElementID : graphElementIDs) {
				GraphElement graphElement = this.getGraphElement(graphElementID);
				if (graphElement!=null) {
					if (graphElements==null) {
						graphElements = new HashSet<GraphElement>();
					}
					graphElements.add(graphElement);
				}
			}
		}
		return graphElements;
	}
	
	/**
	 * Gives the set of network components containing the given node.
	 * 
	 * @param graphNode - A GraphNode
	 * @return HashSet<NetworkComponent> - The set of components which contain the node
	 */
	public HashSet<NetworkComponent> getNetworkComponents(GraphNode graphNode) {
		
		HashSet<NetworkComponent> networkComponentsFound = this.getGraphElementToNetworkComponentHash().get(graphNode);
		if (networkComponentsFound==null) {
			// --- In case that it could not be found, search manually --------
			networkComponentsFound = new HashSet<NetworkComponent>();
			NetworkComponent[] netComps = new NetworkComponent[this.networkComponents.values().size()];
			this.networkComponents.values().toArray(netComps);
			for (int i = 0; i < netComps.length; i++) {
				NetworkComponent networkComponent = netComps[i];
				if (networkComponent.getGraphElementIDs().contains(graphNode.getId())) {
					networkComponentsFound.add(networkComponent);
				}
			}
			
		}
		return networkComponentsFound;
	}

	/**
	 * Generates the next unique network component ID in the series n1, n2, n3, ...
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
	 * @return String The next unique node ID that can be used.
	 */
	public String nextNodeID() {
		return this.nextNodeID(false);
	}
	/**
	 * Generates the next unique node ID in the series PP0, PP1, PP2, ...
	 * 
	 * @param skipNullEntries the skip null entries
	 * @return String The next unique node ID that can be used.
	 */
	private String nextNodeID(boolean skipNullEntries) {
	
		// Finds the current maximum node ID and returns the next one to it.
		long max = -1;
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
				Long num = extractNumericalValue(id);
				if (num!=null && num > max) {
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

		
		// --- Make sure that the information are up to date ------------------
		supplementNetworkModel.refreshGraphElements();
		
		// --- Get and rename the list of GraphNodes --------------------------
		Graph<GraphNode, GraphEdge> graph = supplementNetworkModel.getGraph();
		Vector<GraphNode> nodes = new Vector<GraphNode>(graph.getVertices());
		// --- Change node names and positions --------------------------------
		String newNodeID = GraphNode.GRAPH_NODE_PREFIX + nextNodeIDCounter;
		for (GraphNode graphNode : nodes) {
			
			// --- Find new GraphNodeID ---------------------------------------
			while (this.getGraphElement(newNodeID)!=null || supplementNetworkModel.getGraphElement(newNodeID)!=null) {
				nextNodeIDCounter++;
				newNodeID = GraphNode.GRAPH_NODE_PREFIX + nextNodeIDCounter;
			}
			
			// --- Configure new name -----------------------------------------
			String oldNodeID = graphNode.getId();
			graphNode.setId(newNodeID);
			supplementNetworkModel.getGraphElements().remove(oldNodeID);
			supplementNetworkModel.getGraphElements().put(newNodeID, graphNode);
			
			// --- Change to new ID also in the other affected components ----- 
			NetworkComponents netComps = supplementNetworkModel.getGraphElementToNetworkComponentHash().get(graphNode);
			if (netComps!=null) {
				for (NetworkComponent netComp : netComps) {
					netComp.getGraphElementIDs().remove(oldNodeID);
					netComp.getGraphElementIDs().add(newNodeID);
				}	
			}

		}
		
		// --- Get the list of NetworkComponents ------------------------------
		String newCompID = GeneralGraphSettings4MAS.PREFIX_NETWORK_COMPONENT + nextCompIDCounter;
		NetworkComponent[] netComps = new NetworkComponent[supplementNetworkModel.getNetworkComponents().values().size()]; 
		supplementNetworkModel.getNetworkComponents().values().toArray(netComps);
		for (int i = 0; i < netComps.length; i++) {
			
			// --- Find new NetworkComponentID --------------------------------
			while (this.getNetworkComponent(newCompID)!=null || supplementNetworkModel.getNetworkComponent(newCompID)!=null) {
				nextCompIDCounter++;
				newCompID = GeneralGraphSettings4MAS.PREFIX_NETWORK_COMPONENT + nextCompIDCounter;
			}
			
			// --- Configure new name -----------------------------------------
			NetworkComponent nc = netComps[i];
			String oldCompID = nc.getId();
			nc.setId(newCompID);
			supplementNetworkModel.getNetworkComponents().remove(oldCompID);
			supplementNetworkModel.getNetworkComponents().put(newCompID, nc);
			
			HashSet<GraphElement> edgesFound = supplementNetworkModel.getGraphElementsOfNetworkComponent(nc, new GraphEdge(null, null));
			if (edgesFound!=null && edgesFound.size()>0) {
				for (GraphElement element : edgesFound) {
					
					GraphEdge edge = (GraphEdge) element;
					String edgeIDOld = edge.getId();
					String edgeIDNew = edge.getId().replace(oldCompID, newCompID);
					edge.setId(edgeIDNew);
					
					supplementNetworkModel.getGraphElements().remove(edgeIDOld);
					supplementNetworkModel.getGraphElements().put(edgeIDNew, edge);
					
					NetworkComponents netCompsToChange = supplementNetworkModel.getGraphElementToNetworkComponentHash().get(edge);
					for (NetworkComponent netCompToChange : netCompsToChange) {
						// --- Change to the new ID
						netCompToChange.getGraphElementIDs().remove(edgeIDOld);
						netCompToChange.getGraphElementIDs().add(edgeIDNew);
					}
				}	
			}
			
		}
		supplementNetworkModel.refreshGraphElements();
		return supplementNetworkModel;
	}

	/**
	 * Extract the numerical value from a String.
	 * @param expression the expression
	 * @return the integer value
	 */
	private Long extractNumericalValue(String expression) {
		String numericString = "";
		Long numeric = new Long(-1);
		for (int i = 0; i < expression.length(); i++) {
			String letter = Character.toString(expression.charAt(i));
			if (letter.matches("[0-9]")) {
				numericString += letter;
			}
		}
		if (numericString.equals("") == false) {
			try {
				numeric = Long.parseLong(numericString);	
			} catch (Exception e) {
				numeric = new Long(-1);
			}
			
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
					this.getGraph().removeVertex(graphNode);
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
			this.getGraph().addEdge(edge, centerOfCluster, (GraphNode) graphElement, EdgeType.UNDIRECTED);
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
	 * @param nodes2Merge the nodes2 merge
	 * @return the graph node pairs
	 */
	public GraphNodePairs mergeNetworkModel(NetworkModel supplementNetworkModel, GraphNodePairs nodes2Merge) {
		return this.mergeNetworkModel(supplementNetworkModel, nodes2Merge, true);	
	}
	
	/**
	 * Merges the current NetworkModel with an incoming NetworkModel as supplement.
	 *
	 * @param supplementNetworkModel the supplement network model
	 * @param nodes2Merge the merge description
	 * @param adjustNameDefinitions the adjust name definitions
	 * @return the residual GraphNode, which connects the two NetworkModel's
	 */
	public GraphNodePairs mergeNetworkModel(NetworkModel supplementNetworkModel, GraphNodePairs nodes2Merge, boolean adjustNameDefinitions) {

		NetworkModel srcNM = supplementNetworkModel;
		
		// --- 1. Adjust the names of the supplement NetworkModel, in order to avoid name clashes -
		if (adjustNameDefinitions==true) {
			srcNM = this.adjustNameDefinitionsOfSupplementNetworkModel(supplementNetworkModel);	
		}

		// --- 2. Add the new graph to the current graph ------------------------------------------
		Graph<GraphNode, GraphEdge> suppGraph = supplementNetworkModel.getGraph();

		// --- 3. Run through the list of new NetworkComponents -----------------------------------
		for (String netCompName : srcNM.getNetworkComponents().keySet()) {
			// --- Get the network component ------------------------------------------------------
			NetworkComponent nc = srcNM.getNetworkComponents().get(netCompName);

			// --- Get the graph elements of this NetworkComponent -------------------------------- 
			Vector<GraphElement> graphElements = supplementNetworkModel.getGraphElementsFromNetworkComponent(nc);
			for (int run=1; run<=2; run++) {
				// --- Run twice through this list of graphElements -------------------------------
				for (int i = 0; i < graphElements.size(); i++) {
					if (run==1 && graphElements.get(i) instanceof GraphNode) {
						// --------------------------------------------------------------
						// --- First run: GraphNodes ------------------------------------
						// --------------------------------------------------------------
						GraphNode node = (GraphNode)graphElements.get(i);
						if (this.graphElements.get(node.getId())==null) {
							// --- GraphNode locally new --------------------------------
							this.getGraph().addVertex(node);	
							this.graphElements.put(node.getId(), node);
						} else {
							// --- GraphNode locally available --------------------------
							node = (GraphNode) this.graphElements.get(node.getId());
						}
						this.addGraphElementToNetworkComponentRelation(node, nc);
						
					} else if (run==2 && graphElements.get(i) instanceof GraphEdge) {
						// --------------------------------------------------------------
						// --- Second run: GraphEdge ------------------------------------
						// --------------------------------------------------------------
						GraphEdge edge = (GraphEdge) graphElements.get(i);
						GraphNode node1 = (GraphNode) this.graphElements.get(suppGraph.getEndpoints(edge).getFirst().getId());
						GraphNode node2 = (GraphNode) this.graphElements.get(suppGraph.getEndpoints(edge).getSecond().getId());
						this.getGraph().addEdge(edge, node1, node2, suppGraph.getEdgeType(edge));
						this.graphElements.put(edge.getId(), edge);
						this.addGraphElementToNetworkComponentRelation(edge, nc);
						
					}
				}	
			} 
			// --- Add this network component 
			this.addNetworkComponent(srcNM.getNetworkComponents().get(netCompName), false);
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
		// --- Get first GraphNode and NetworkCommponent ----------------------
		GraphNode graphNode1 = (GraphNode) this.getGraphElement(nodes2Merge.getGraphNode1().getId());
		if (graphNode1==null) return null;
		NetworkComponent comp1 = this.getNetworkComponents(graphNode1).iterator().next();
		
		// --------------------------------------------------------------------
		// --- Walk through the list of GraphNode that have to be merged ------
		for (GraphNode graphNode2 : nodes2Merge.getGraphNode2Hash() ) {
			
			// --- Make sure that this is a current GraphNode -----------------
			graphNode2 = (GraphNode) this.getGraphElement(graphNode2.getId());
			if (graphNode2==null) continue;
			NetworkComponent comp2 = this.getNetworkComponents(graphNode2).iterator().next();

			// --- Find the intersection set of the Graph elements of the two NetworkComponent
			// --- NetworkComponent in order to make sure that they are not already connected 
			HashSet<String> intersection = new HashSet<String>(comp1.getGraphElementIDs());
			intersection.retainAll(comp2.getGraphElementIDs());
			// Checking the constraint - Two network components can have maximum one node in common
			if (intersection.size() == 0) {
				// --- No intersection node found - proceed -------------------
				for (GraphEdge edgeOld : this.getGraph().getIncidentEdges(graphNode2)) {
					// --- switch connection to graphNode1 ----------
					GraphEdge edgeNew = this.switchEdgeBetweenGraphNodes(edgeOld, graphNode1, graphNode2);
					this.removeGraphElementToNetworkComponent(edgeOld);
					this.addGraphElementToNetworkComponentRelation(edgeNew, comp2);
					
					// --- store revert information -----------------
					GraphNodePairsRevert revert = new GraphNodePairsRevert(graphNode2, edgeNew);
					revertInfos.add(revert);
				}
				// --- Updating the graph element IDs of the component --------
				comp2.getGraphElementIDs().remove(graphNode2.getId());
				comp2.getGraphElementIDs().add(graphNode1.getId());
				this.addGraphElementToNetworkComponentRelation(graphNode1, comp2);
				
				// --- Removing node2 from the graph and network model --------
				this.getGraph().removeVertex(graphNode2);
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
				this.getGraph().addVertex(graphNode);
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
		return this.splitNetworkModelAtNode(node2SplitAt, false);
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
		NetworkComponent lastNetComp = netCompVector.get(netCompVector.size()-1);
		if (lastNetComp.getPrototypeClassName().equals(DistributionNode.class.getName())) {
			moveOppositeNode = true;
		}
		
		for (int i = 0; i < (netCompVector.size() - 1); i++) {
			NetworkComponent component = netCompVector.get(i);
			// --- Incident Edges on the node ---------------------------------
			for (GraphEdge odlEdge : this.getGraph().getIncidentEdges(node2SplitAt)) { // for each incident edge
				// --- If the edge is in comp2 --------------------------------
				if (component.getGraphElementIDs().contains(odlEdge.getId())) {
					// --- Creating a new Graph node --------------------------
					GraphNode newNode = new GraphNode();
					newNode.setId(this.nextNodeID());
					newNode.setPosition(node2SplitAt.getPosition());
					this.graphElements.put(newNode.getId(), newNode);

					// --- Switch the connection to the new node --------------
					GraphEdge newEdge = switchEdgeBetweenGraphNodes(odlEdge, newNode, node2SplitAt);
					this.removeGraphElementToNetworkComponent(odlEdge);
					this.addGraphElementToNetworkComponentRelation(newEdge, component);

					component.getGraphElementIDs().add(newNode.getId());
					component.getGraphElementIDs().remove(node2SplitAt.getId());
					
					graphNodeConnections.add(newNode);
					
					if (moveOppositeNode==true) {
						// --- Shift position of the new node a bit -----------
						GraphNode otherNode = this.getGraph().getOpposite(newNode, newEdge);
						newNode.setPosition(this.getShiftedPosition(otherNode, newNode));	
					}

				}
			}
			// --- Updating the graph element IDs of the component ------------
			component.getGraphElementIDs().remove(node2SplitAt.getId());

		} // --- 'end for' for components -------------------------------------
		this.refreshGraphElements();
		
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
		GraphNode otherNode = this.getGraph().getOpposite(oldGraphNode, edge);
		// Create a new edge with the same ID and type
		GraphEdge newEdge = new GraphEdge(edge.getId(), edge.getComponentType());

		if (this.getGraph().getSource(edge) != null) {
			// if the edge is directed
			if (this.getGraph().getSource(edge)==oldGraphNode) {
				this.getGraph().addEdge(newEdge, newGraphNode, otherNode, EdgeType.DIRECTED);
			} else if (this.getGraph().getDest(edge)==oldGraphNode) {
				this.getGraph().addEdge(newEdge, otherNode, newGraphNode, EdgeType.DIRECTED);
			}
		} else {
			// if the edge is undirected
			this.getGraph().addEdge(newEdge, newGraphNode, otherNode, EdgeType.UNDIRECTED);
		}
		// Removing the old edge from the graph and network model
		this.getGraph().removeEdge(edge);
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
		return this.getShiftedPosition(fixedNode, shiftNode, move);
	}
	
	/**
	 * Gets a shifted position for a node in relation to the raster size of the component type settings.
	 *
	 * @param fixedNode the fixed node
	 * @param shiftNode the shift node
	 * @param move the move
	 * @return the shifted position
	 */
	public Point2D getShiftedPosition(GraphNode fixedNode, GraphNode shiftNode, double move) {

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
	 * @param componentHashSet the component hash set
	 * @return the network component vector with distribution node as last
	 */
	public Vector<NetworkComponent> getNetworkComponentVectorWithDistributionNodeAsLast(HashSet<NetworkComponent> componentHashSet) {

		NetworkComponent distributionNodeComponent = null;
		Vector<NetworkComponent> newComponentVector = new Vector<NetworkComponent>();
		for (NetworkComponent component : componentHashSet) {
			if (component.getPrototypeClassName().equals(DistributionNode.class.getName())) {
				distributionNodeComponent = component;
			} else {
				newComponentVector.add(component);
			}
		}
		if (distributionNodeComponent!=null) {
			newComponentVector.add(distributionNodeComponent);
		}
		return newComponentVector;
	}
	/**
	 * Returns the first {@link DistributionNode} NetworkComponent, if available.
	 * @param componentHashSet the component hash set
	 * @return the distribution node
	 */
	public NetworkComponent getDistributionNode(HashSet<NetworkComponent> componentHashSet) {
		
		NetworkComponent distributionNodeComponent = null;
		for (NetworkComponent component : componentHashSet) {
			if (component.getPrototypeClassName().equals(DistributionNode.class.getName())) {
				distributionNodeComponent = component;
				break;
			}
		}
		return distributionNodeComponent;
	}
	
	/**
	 * Checks, if a component list contains a DistributionNode.
	 * 
	 * @param networkComponents the components as HashSet<NetworkComponent>
	 * @return the network component, which is the DistributionNode or null
	 */
	public NetworkComponent containsDistributionNode(HashSet<NetworkComponent> networkComponents) {
		for (NetworkComponent component : networkComponents) {
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
		return getClusterNetworkComponents(new ArrayList<NetworkComponent>(this.networkComponents.values()));
	}

	/**
	 * Gets the cluster components of a collection of clusterComponents.
	 *
	 * @param collectionOfNetworkComponents the components
	 * @return the cluster components
	 */
	public ArrayList<ClusterNetworkComponent> getClusterNetworkComponents(Collection<NetworkComponent> collectionOfNetworkComponents) {
		ArrayList<ClusterNetworkComponent> clusterComponents = new ArrayList<ClusterNetworkComponent>();
		for (NetworkComponent networkComponent : collectionOfNetworkComponents) {
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
			graphElement = (GraphElementPrototype) ClassLoadServiceUtility.newInstance(comp.getPrototypeClassName());

		} catch (ClassNotFoundException ex) {
			System.err.println(" GraphElementPrototype class must be in class path.");
			ex.printStackTrace();
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
	 * @param networkComponent The network component containing the node having the star prototype
	 */
	public boolean isCenterNodeOfStar(GraphNode node, NetworkComponent networkComponent) {
		for (String graphElementID : networkComponent.getGraphElementIDs()) {
			GraphElement elem = getGraphElement(graphElementID);
			// The center node should be incident on all the edges of the component
			if (elem instanceof GraphEdge && !this.getGraph().isIncident(node, (GraphEdge) elem)) {
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
	 * Checks if the specified GraphNode is the main GraphNode of a DistributionNode.
	 * @param graphNode the GraphNode
	 * @return true, if the GraphNode is the main GraphNode of a DistributionNode
	 */
	public NetworkComponent isDistributionNode(GraphNode graphNode) {
		HashSet<NetworkComponent> components = this.getNetworkComponents(graphNode);
		return this.containsDistributionNode(components);
	}
	
	/**
	 * Resets the GraphElementLayout for every GraphNode or GraphEdge.
	 */
	public void resetGraphElementLayout() {
		for (GraphElement graphElement : this.graphElements.values()) {
			if (graphElement.graphElementLayout!=null) {
				graphElement.resetGraphElementLayout(this);	
			}
		}
	}
	
	/**
	 * Replace NetworkComponents by one ClusterComponent.
	 *
	 * @param networkComponentsToCluster The List of {@link NetworkComponent}'s that are to be grouped into a cluster
	 * @param distributionNodesAreOuterNodes the distribution nodes are outer nodes
	 * @return the new {@link ClusterNetworkComponent} that was inserted into the model
	 */
	public ClusterNetworkComponent replaceComponentsByCluster(HashSet<NetworkComponent> networkComponentsToCluster, boolean distributionNodesAreOuterNodes) {
		
		// --- Maybe another instance: Get real group of NetworkComponents ---- 
		HashSet<NetworkComponent> clusterNC2Use = new HashSet<NetworkComponent>();
		for (NetworkComponent netComp : networkComponentsToCluster) {
			clusterNC2Use.add(this.getNetworkComponent(netComp.getId()));
		}
		
		// ---------- Get Domain of current NetworkComponent -------------------
		String domain = null;
		NetworkComponent networkComponent = clusterNC2Use.iterator().next();
		if (networkComponent instanceof ClusterNetworkComponent) {
			domain = ((ClusterNetworkComponent) networkComponent).getDomain();
		} else {
			String compType = networkComponent.getType();
			ComponentTypeSettings cts = this.generalGraphSettings4MAS.getCurrentCTS().get(compType);
			domain = cts.getDomain();	
		}
		// --- Get cluster agent class name -----------------------------------
		DomainSettings dSettings = this.generalGraphSettings4MAS.getDomainSettings().get(domain);
		String clusterAgentClassName = null;
		if (dSettings!=null) {
			clusterAgentClassName = dSettings.getClusterAgent();
		}
		
		// ---------- Prepare Parameters for ClusterComponent -----------------
		NetworkModel clusterNetworkModel = this.getCopy();
		clusterNetworkModel.setAlternativeNetworkModel(null);
		clusterNetworkModel.removeNetworkComponentsInverse(clusterNC2Use);
		clusterNetworkModel.resetGraphElementLayout();
		
		// ----------- Get outer GraphNode of the NetworkModel ----------------
		Vector<GraphNode> outerNodes = this.getOuterConnectionNodes(clusterNC2Use, distributionNodesAreOuterNodes);
		// ----------- Get remaining GraphElements of current outer nodes -----
		HashMap<GraphNode, HashSet<GraphElement>> outerNodesRemainingGraphElements = this.getOuterConnectionRemainingGraphElements(outerNodes, clusterNC2Use);
		// ----------- Remove clustered NetworkComponents ---------------------
		for (NetworkComponent netComp : clusterNC2Use) {
			if (distributionNodesAreOuterNodes==false) {
				this.removeNetworkComponent(netComp, true, false);
			} else {
				if (netComp.getPrototypeClassName().equals(DistributionNode.class.getName())==false) {
					this.removeNetworkComponent(netComp, false, false);
				}
			}
		}
		// --- Correct outer nodes after NetworkComponents were removed -------
		outerNodes = this.getOuterConnectionNodesCorrected(outerNodesRemainingGraphElements);

		// ----------- Create Cluster GraphElement ----------------------------
		ClusterGraphElement clusterGraphElement = new ClusterGraphElement(outerNodes, this.nextNetworkComponentID());
		HashSet<GraphElement> graphElements = clusterGraphElement.addToGraph(this);
		// ----------- Set position of central GraphNode ----------------------
		Rectangle2D rectangle = GraphGlobals.getGraphSpreadDimension(clusterNetworkModel.getGraph().getVertices());
		clusterGraphElement.getCentralGraphNode().setPosition(new Point2D.Double(rectangle.getCenterX(), rectangle.getCenterY()));
		// ----------- Create ClusterNetworkComponent -------------------------
		ClusterNetworkComponent clusterComponent = new ClusterNetworkComponent(clusterGraphElement.getId(), clusterGraphElement.getType(), clusterAgentClassName, graphElements, clusterGraphElement.isDirected(), domain, clusterNetworkModel);
		this.addNetworkComponent(clusterComponent);
		this.refreshGraphElements();

		// --- Add the created cluster as an alternative network model --------
		this.getAlternativeNetworkModel().put(clusterComponent.getId(), clusterNetworkModel);
		
		return clusterComponent;
	}

	/**
	 * Merges Cluster NetworkModel with this NetworkModel and removes the Cluster if it's part of this model
	 *
	 * @param clusterNetworkComponent the cluster network component
	 */
	public void replaceClusterByComponents(ClusterNetworkComponent clusterNetworkComponent) {
		removeNetworkComponent(clusterNetworkComponent);
		for( GraphNode graphNode : clusterNetworkComponent.getClusterNetworkModel().getGraph().getVertices()) {
			
			if (getGraphElement(graphNode.getId()) == null) {
				GraphNode graphNodeCopy = graphNode.getCopy(this);
				this.getGraph().addVertex(graphNodeCopy);
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
			this.getGraph().addEdge(graphEdgeNew, copyFirst, copySecond, edgeType);
		}

		for (NetworkComponent networkComponent : clusterNetworkComponent.getClusterNetworkModel().getNetworkComponents().values()) {
			addNetworkComponent(networkComponent);
		}
		refreshGraphElements();
	}

	/**
	 * Returns the outer, not connected GraphNodes of a NetworkModel.
	 *
	 * @param networkComponents the {@link NetworkComponent}'s that aare building a group within the graph (BE AWARE OF THE RIGHT INSTANCE)
	 * @param setDistributionNodesToOuterNodes if true, distribution nodes will always set to outer nodes
	 * @return the outer nodes
	 */
	public Vector<GraphNode> getOuterConnectionNodes(HashSet<NetworkComponent> networkComponents, boolean setDistributionNodesToOuterNodes) {
		
		Vector<GraphNode> outerNodes = new Vector<GraphNode>();
		
		// --- Walk through the list of specified NetworkCompnents ------------
		for (NetworkComponent netComp : networkComponents) {
			// --- Get all GraphNodes of a NetworkComponent -------------------
			HashSet<GraphElement> nodeElements = this.getGraphElementsOfNetworkComponent(netComp, new GraphNode());
			for (GraphElement nodeElement : nodeElements) {
				// --- Get connected NetworkComponents of this GraphNode ------
				NetworkComponents connNetComps = this.getGraphElementToNetworkComponentHash().get(nodeElement);
				// --- Distribution Node within neighbours and set outer? -----
				if (setDistributionNodesToOuterNodes==true && this.containsDistributionNode(connNetComps)!=null && outerNodes.contains((GraphNode)nodeElement)==false) {
					// --- Add to outer nodes ---------------------------------
					outerNodes.add((GraphNode)nodeElement);	
					
				} else {
					// --- Are the connected NetworkComponents in the group -------	
					for (NetworkComponent connNetComp : connNetComps) {
						if (networkComponents.contains(connNetComp)==false && outerNodes.contains((GraphNode)nodeElement)==false) {
							// --- Found outer node -------------------------------
							outerNodes.add((GraphNode)nodeElement);	
							break;
						}
					} 
				} // end if
			}
		}
		return outerNodes;
	}

	/**
	 * Gets the outer connection remaining graph elements.
	 *
	 * @param outerNodes the outer nodes 
	 * @param networkComponentsToCluster the network components to cluster
	 * @return the outer connection remaining graph elements
	 */
	private HashMap<GraphNode, HashSet<GraphElement>> getOuterConnectionRemainingGraphElements(Vector<GraphNode> outerNodes, HashSet<NetworkComponent> networkComponentsToCluster) {
		
		HashMap<GraphNode, HashSet<GraphElement>> ocge = new HashMap<GraphNode, HashSet<GraphElement>>();
		for (GraphNode graphNode1 : outerNodes) {
			
			// --- Get edges that are leaving / reaching this GraphNode ------- 
			Vector<GraphEdge> edges = new Vector<GraphEdge>(this.getGraph().getIncidentEdges(graphNode1));
			for (GraphEdge edge : edges) {
				// --- Is the edge part of the NetworkComponets to Cluster? ---
				if (networkComponentsToCluster.contains(this.getNetworkComponent(edge))==false) {
					// --- Get opposite GraphNode -----------------------------
					GraphNode graphNode2 = this.getGraph().getOpposite(graphNode1, edge);
					HashSet<GraphElement> remainingGE = new HashSet<GraphElement>();
					remainingGE.add(edge);
					remainingGE.add(graphNode2);
					ocge.put(graphNode1, remainingGE);
					// --- one reminder is enough ! ---------------------------
					break;
				}
			} // end for edges 
		} // end for outerNodes
		return ocge;
	}
	
	/**
	 * Gets the outer connection nodes corrected.
	 *
	 * @param outerNodesRemainingGraphElements the outer nodes remaining graph elements
	 * @return the outer connection nodes corrected
	 */
	private Vector<GraphNode> getOuterConnectionNodesCorrected(HashMap<GraphNode, HashSet<GraphElement>> outerNodesRemainingGraphElements) {
		
		Vector<GraphNode> outerNodes = new Vector<GraphNode>();
		for (GraphNode graphNode : outerNodesRemainingGraphElements.keySet()) {
			
			// --- Check, if the GraphNode is still there -----------
			GraphElement graphNodeCheck = this.getGraphElement(graphNode.getId());
			if (graphNodeCheck!=null) {
				// --- Node is still there - just take it -----------
				outerNodes.add((GraphNode) graphNodeCheck);
				
			} else {
				// --- Search by using remaining GraphElements ------
				GraphEdge edge = null;
				GraphNode node1 = null;
				HashSet<GraphElement> rGE = outerNodesRemainingGraphElements.get(graphNode);
				for (GraphElement graphElement : rGE) {
					GraphElement geFound = this.getGraphElement(graphElement.getId());
					if (geFound instanceof GraphNode) {
						node1 = (GraphNode) geFound;
					} else if (geFound instanceof GraphEdge) {
						edge = (GraphEdge) geFound;
					}
				} // end for
				
				if (edge!=null && node1!=null) {
					GraphNode graphNodeFound = this.getGraph().getOpposite(node1, edge);
					if (graphNodeFound!=null) {
						outerNodes.add(graphNodeFound);	
					}
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
		for (GraphNode graphNode : this.getGraph().getVertices()) {
			if (isFreeGraphNode(graphNode)) {
				NetworkComponent networkComponent = getNetworkComponents(graphNode).iterator().next();
				outerNetworkComponents.add(networkComponent.getId());
			}
		}
		return outerNetworkComponents;
	}

	/**
	 * Gets the connections of biggest branch.
	 *
	 * @return the connections of biggest branch
	 */
	public int getConnectionsOfBiggestBranch() {
		if (connectionsOfBiggestBranch < 1) {
			for (NetworkComponent networkComponent : new ArrayList<NetworkComponent>(networkComponents.values())) {
				if (!(networkComponent instanceof ClusterNetworkComponent)) {
					int nodes = getNodesFromNetworkComponent(networkComponent).size() - 1;
					if (nodes > connectionsOfBiggestBranch) {
						connectionsOfBiggestBranch = nodes;
					}
				}
			}
		}
		return connectionsOfBiggestBranch;
	}


	/**
	 * Sets the directions of the specified NetworkComponent.
	 * @param networkComponent the NetworkComponent
	 */
	public void setDirectionsOfNetworkComponent(NetworkComponent networkComponent) {
		
		HashMap<String, GraphEdgeDirection> edgeHash = networkComponent.getEdgeDirections();
		for (GraphEdgeDirection direction : edgeHash.values()) {
			// --- Set direction in this NetworkModel -------------------------
			this.setGraphEdgeDirection(direction);

			// ----------------------------------------------------------------
			// --- Set the direction also to the alternative NetworkModels ----
			// ----------------------------------------------------------------
			if (this.alternativeNetworkModel!=null && this.alternativeNetworkModel.size()>0) {
				for (NetworkModel altNetModel : this.alternativeNetworkModel.values()) {
					if (altNetModel.getNetworkComponent(networkComponent.getId())!=null) {
						altNetModel.setGraphEdgeDirection(direction);
					}
				}
			}
			// ----------------------------------------------------------------
		}
	}
	
	/**
	 * Sets the GraphEdge direction.
	 * @param graphEdgeDirection the new graph edge direction
	 */
	public void setGraphEdgeDirection(GraphEdgeDirection graphEdgeDirection) {
		
		GraphEdge graphEdge 	= (GraphEdge) this.graphElements.get(graphEdgeDirection.getGraphEdgeID());
		GraphNode graphNodeFrom = (GraphNode) this.graphElements.get(graphEdgeDirection.getGraphNodeIDFrom());
		GraphNode graphNodeTo   = (GraphNode) this.graphElements.get(graphEdgeDirection.getGraphNodeIDTo());
		
		if (graphEdge!=null && graphNodeFrom!=null && graphNodeTo!=null) {
			// --- Set graph directed ----------------
			this.getGraph().removeEdge(graphEdge);
			this.getGraph().addEdge(graphEdge, graphNodeFrom, graphNodeTo, EdgeType.DIRECTED);
		} 
		
	}

	/**
	 * Returns the NetworkComponentAdapter for the specified NetworkComponent.
	 *
	 * @param networkComponent the NetworkComponent
	 * @return the network component adapter
	 */
	public NetworkComponentAdapter getNetworkComponentAdapter(GraphEnvironmentController graphController, NetworkComponent networkComponent) {
		return this.getNetworkComponentAdapter(graphController, networkComponent.getType());
	}

	/**
	 * Returns the NetworkComponentAdapter for the specified GraphNode.
	 *
	 * @param graphController the current graph controller, if there is one. Can also be null.
	 * @param graphNode the graph node
	 * @return the network component adapter
	 */
	public NetworkComponentAdapter getNetworkComponentAdapter(GraphEnvironmentController graphController, GraphNode graphNode) {
		String domain = this.getDomain(graphNode);
		if (domain!=null) {
			String searchFor = GeneralGraphSettings4MAS.GRAPH_NODE_NETWORK_COMPONENT_ADAPTER_PREFIX + domain;
			return this.getNetworkComponentAdapter(graphController, searchFor);
		}
		return null;
	}
	
	/**
	 * Returns the NetworkComponentAdapter for the specified type of component.
	 *
	 * @param componentTypeName the component type name
	 * @return the network component adapter
	 */
	private NetworkComponentAdapter getNetworkComponentAdapter(GraphEnvironmentController graphController, String componentTypeName) {
		
		if (this.networkComponentAdapterHash==null) {
			this.networkComponentAdapterHash = new HashMap<String, NetworkComponentAdapter>();
		}
		NetworkComponentAdapter netCompAdapter = this.networkComponentAdapterHash.get(componentTypeName);
		if (netCompAdapter==null) {
			// --- Create corresponding NetworkComponentAdapter -----
			netCompAdapter = this.createNetworkComponentAdapter(graphController, componentTypeName);
			this.networkComponentAdapterHash.put(componentTypeName, netCompAdapter);
		}
		return netCompAdapter;
	}
	
	
	/**
	 * Creates the {@link NetworkComponentAdapter} that is specified with the component type name.
	 *
	 * @param graphController the current graph controller
	 * @param componentTypeName the component type name
	 * @return the network component adapter
	 */
	public NetworkComponentAdapter createNetworkComponentAdapter(GraphEnvironmentController graphController, String componentTypeName) {
		
		NetworkComponentAdapter netCompAdapter = null;
		
		// --------------------------------------------------------------------------
		// --- Find and initialise the corresponding NetworkComponentAdapter --------
		// --------------------------------------------------------------------------
		String adapterClassname = null;
		if (componentTypeName.startsWith(GeneralGraphSettings4MAS.GRAPH_NODE_NETWORK_COMPONENT_ADAPTER_PREFIX)) {
			// --- Find the NetworkComponentAdapter for the GraphNode ---------------
			String searchFor = componentTypeName.replace(GeneralGraphSettings4MAS.GRAPH_NODE_NETWORK_COMPONENT_ADAPTER_PREFIX, "");
			DomainSettings ds = this.generalGraphSettings4MAS.getDomainSettings().get(searchFor);
			if (ds!=null) {
				adapterClassname = ds.getAdapterClass();	
			}
			
		} else {
			// --- Create the NetworkComponentAdapter, if it exists -----------------
			ComponentTypeSettings cts = this.generalGraphSettings4MAS.getCurrentCTS().get(componentTypeName);
			if (cts!=null) {
				adapterClassname = cts.getAdapterClass();	
			}
			
		}
		// --------------------------------------------------------------------------
		// --- Initialise the found NetworkComponentAdapter -------------------------
		// --------------------------------------------------------------------------
		if (adapterClassname!=null) {
			try {
				netCompAdapter = ClassLoadServiceUtility.getNetworkComponentAdapterInstance(adapterClassname, graphController);
			} catch (Exception ex) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Could not initiate class '" + adapterClassname + "'");
				System.err.println(ex.getMessage());
				//ex.printStackTrace();
			}
		}
		return netCompAdapter;
	}
	
	/**
	 * Gets the domain of a GraphElement.
	 *
	 * @param graphElement the graph element
	 * @return the domain
	 */
	public String getDomain(GraphElement graphElement) {
		
		String domain = null;
		if (graphElement instanceof GraphNode) {
			
			// --- Make a majority decision for the domain --------------------
			HashMap<String, Integer> domainCountings = new HashMap<String, Integer>(); 
			String domainTmp = null;

			// --- Check all NetworkComponents --------------------------------
			for (NetworkComponent netComp : this.getNetworkComponents((GraphNode) graphElement)) {
				
				if (netComp instanceof ClusterNetworkComponent) {
					domainTmp = ((ClusterNetworkComponent)netComp).getDomain();
				} else {
					domainTmp = this.generalGraphSettings4MAS.getCurrentCTS().get(netComp.getType()).getDomain();
					// --- For a DistributionNode return the result -----------
					if (netComp.getPrototypeClassName().equals(DistributionNode.class.getName())) {
						return domainTmp;
					}
				}
				
				if (domainTmp!=null) {
					Integer noOfDomain = domainCountings.get(domainTmp);
					if (noOfDomain==null) {
						domainCountings.put(domainTmp, 1);
					} else {
						domainCountings.put(domainTmp, noOfDomain+1);
					}
				}
			}

			// --- Determine the domain ---------------------------------------
			if (domainCountings.size()==1) {
				domain = domainTmp;
			} else {
				// --- Find the maximum counting of domains ------------------- 
				Integer countsMax = 0; 
				Set<String> domainKeys = domainCountings.keySet();
				for (String domainKey : domainKeys) {
					Integer counts = domainCountings.get(domainKey);
					if (counts > countsMax) {
						domain = domainKey;
						countsMax = counts;
					}
				}
			}
			
		} else if (graphElement instanceof GraphEdge) {
			// --- Get the corresponding NetworkComponent and have a look ----- 
			NetworkComponent networkComponent = this.getNetworkComponent((GraphEdge) graphElement);
			domain = this.generalGraphSettings4MAS.getCurrentCTS().get(networkComponent.getType()).getDomain();;
			
		}
		return domain;
	}
	
	
	/**
	 * Load the network topology from a GraphML file.
	 * @param graphMlFile the GraphML file
	 * @return true, if successful
	 */
	public boolean loadGraphFile(File graphMlFile) {
		
		boolean success = false;
		GraphModelReader graphModelReader = null;
		if (graphMlFile.exists()) {
			try {
				graphModelReader = new GraphModelReader(graphMlFile);
				this.setGraph(graphModelReader.readGraph());
				success = true;
				
			} catch (GraphIOException e) {
				e.printStackTrace();
			} finally {
				if (graphModelReader!=null) {
					try {
						graphModelReader.close();
					} catch (GraphIOException gIOEx) {
						gIOEx.printStackTrace();
					}
				}
			}
		}
		return success;
	}
	/**
	 * Save the network topology to a GraphML file.
	 * @param graphMlFile the GraphML file
	 */
	public boolean saveGraphFile(File graphMlFile) {
		
		boolean success = false;
		FileWriter fw = null;
		PrintWriter pw = null;
		try {

			fw = new FileWriter(graphMlFile);
			pw = new PrintWriter(fw);
			new GraphModelWriter().save(this.getGraph(), pw);
			success=true;
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			pw.close();
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return success;
	}
	
	
	/**
	 * Load the network component definitions from an XML file.
	 * @param componentsXmlFile the components file
	 * @return true, if successful
	 */
	public boolean loadComponentsFile(File componentsXmlFile) {
		
		boolean success = false;
		if (componentsXmlFile.exists()) {
		
			FileReader componentReader = null;
			try {
				componentReader = new FileReader(componentsXmlFile);

				JAXBContext context = JAXBContext.newInstance(NetworkComponentList.class);
				Unmarshaller unmarsh = context.createUnmarshaller();
				NetworkComponentList compList = (NetworkComponentList) unmarsh.unmarshal(componentReader);
				this.setNetworkComponents(compList.getComponentList());
				success = true;
				
			} catch (JAXBException | IOException e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					componentReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return success;
	}
	/**
	 * Save the network component definitions to an XML file.
	 * @param componentsXmlFile the component file
	 * @return true, if successful
	 */
	public boolean saveComponentsFile(File componentsXmlFile) {
		
		boolean success = false;
		FileWriter componentFileWriter = null;
		try {
			componentFileWriter = new FileWriter(componentsXmlFile);
			JAXBContext context = JAXBContext.newInstance(NetworkComponentList.class);
			Marshaller marsh = context.createMarshaller();
			marsh.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marsh.marshal(new NetworkComponentList(this.getNetworkComponents()), componentFileWriter);
			success = true;
			
		} catch (IOException | JAXBException e) {
			System.err.println("Error saving network components!");
			e.printStackTrace();
		} finally {
			if (componentFileWriter!=null) {
				try {
					componentFileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return success;
	}
	
}
