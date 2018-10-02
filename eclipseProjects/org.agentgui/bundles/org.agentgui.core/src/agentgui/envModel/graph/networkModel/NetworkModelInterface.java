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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import agentgui.envModel.graph.prototypes.DistributionNode;
import edu.uci.ics.jung.graph.Graph;

public interface NetworkModelInterface {

	/**
	 * Sets the general graph settings for the MAS.
	 * @param generalGraphSettings4MAS the new general graph settings for the MAS
	 */
	public abstract void setGeneralGraphSettings4MAS(GeneralGraphSettings4MAS generalGraphSettings4MAS);

	/**
	 * Gets the general graph settings for the MAS.
	 * @return the general graph settings for the MAS
	 */
	public abstract GeneralGraphSettings4MAS getGeneralGraphSettings4MAS();

	/**
	 * Sets the alternative network model.
	 * @param alternativeNetworkModel the alternativeNetworkModel to set
	 */
	public abstract void setAlternativeNetworkModel(HashMap<String, NetworkModel> alternativeNetworkModel);

	/**
	 * Gets the alternative network models.
	 * @return the alternativeNetworkModel
	 */
	public abstract HashMap<String, NetworkModel> getAlternativeNetworkModel();

	/**
	 * Creates a clone of the current instance.
	 * @return the copy
	 */
	public abstract NetworkModel getCopy();

	/**
	 * Returns the GraphElement with the given ID, or null if not found.
	 * @param id The ID to look for
	 * @return the GraphElement
	 */
	public abstract GraphElement getGraphElement(String id);

	/**
	 * Gets the graph elements.
	 * @return graphElements The hashmap of GraphElements
	 */
	public abstract HashMap<String, GraphElement> getGraphElements();

	/**
	 * This method gets the GraphElements that are part of the given NetworkComponent
	 * 
	 * @param networkComponent The NetworkComponent
	 * @return The GraphElements
	 */
	public abstract Vector<GraphElement> getGraphElementsFromNetworkComponent(NetworkComponent networkComponent);

	/**
	 * Gets the graph.
	 * @return The Graph
	 */
	public abstract Graph<GraphNode, GraphEdge> getGraph();
	/**
	 * Sets the the graph of the network model.
	 * @param newGraph the new graph
	 */
	public abstract void setGraph(Graph<GraphNode, GraphEdge> newGraph);

	/**
	 * Adds a network component to the NetworkModel.
	 *
	 * @param networkComponent the network component
	 * @return the network component
	 */
	public abstract NetworkComponent addNetworkComponent(NetworkComponent networkComponent);

	/**
	 * Rename component.
	 * 
	 * @param oldCompID the old comp id
	 * @param newCompID the new comp id
	 */
	public abstract void renameNetworkComponent(String oldCompID, String newCompID);

	/**
	 * This method removes a {@link NetworkComponent} from the GridModel's networkComponents 
	 * HashMap, using its' ID as key.
	 * 
	 * @param networkComponent The NetworkComponent to remove
	 */
	public abstract void removeNetworkComponent(NetworkComponent networkComponent);

	/**
	 * Removes the specified {@link NetworkComponent}'s.
	 * @param networkComponents the {@link NetworkComponent}'s to delete 
	 */
	public abstract void removeNetworkComponents(HashSet<NetworkComponent> networkComponents);

	/**
	 * Removes the specified {@link NetworkComponent}'s.
	 * @param networkComponents the {@link NetworkComponent}'s to delete
	 * @param removeDistributionNodes set true, if DistributionNodes should be removed too 
	 */
	public abstract void removeNetworkComponents(HashSet<NetworkComponent> networkComponents, boolean removeDistributionNodes);
	
	/**
	 * Removes the network components if not in list.
	 *
	 * @param networkComponents the network components
	 * @return the hash set
	 */
	public abstract HashSet<NetworkComponent> removeNetworkComponentsInverse(HashSet<NetworkComponent> networkComponents);

	/**
	 * Gets the a node from network component.
	 * 
	 * @param networkComponent the network component
	 * @return the a node from network component
	 */
	public abstract Vector<GraphNode> getNodesFromNetworkComponent(NetworkComponent networkComponent);

	/**
	 * This method gets the NetworkComponent with the given ID from the GridModel's networkComponents HashMap.
	 * 
	 * @param id The ID
	 * @return The NetworkComponent
	 */
	public abstract NetworkComponent getNetworkComponent(String id);


	/**
	 * Gets the neighbour network components.
	 *
	 * @param networkComponents the network components
	 * @return the neighbour network components
	 */
	public abstract Vector<NetworkComponent> getNeighbourNetworkComponents(Vector<NetworkComponent> networkComponents);

	/**
	 * Gets the neighbour network components.
	 * 
	 * @param networkComponent the network component
	 * @return the neighbour network components
	 */
	public abstract Vector<NetworkComponent> getNeighbourNetworkComponents(NetworkComponent networkComponent);

	/**
	 * Gets the network component by graph edge id.
	 * 
	 * @param graphEdge the graph edge
	 * @return the network component by graph edge id
	 */
	public abstract NetworkComponent getNetworkComponent(GraphEdge graphEdge);

	/**
	 * Gets all NetworkComponents contained in a GraphNode Set
	 *
	 * @param graphNodes the graph nodes
	 * @return the network components
	 */
	public abstract HashSet<NetworkComponent> getNetworkComponents(Set<GraphNode> graphNodes);

	
	/**
	 * Returns the {@link NetworkComponent}'s that are fully selected by the given set of GraphNodes.<br>
	 * As an example: if you have selected one vertex of a simple directed edge with two vertices, this
	 * method will return null.  
	 *
	 * @param graphNodes the set of selected {@link GraphNode}'s
	 * @return the {@link NetworkComponent}'s that are fully selected by the given nodes and edges
	 */
	public abstract HashSet<NetworkComponent> getNetworkComponentsFullySelected(Set<GraphNode> graphNodes);
	
	
	/**
	 * Extracts the ID's of the GraphElements of the specified NetworkComponent. The parameter 'searchForInstance' can be
	 * an exemplary instance of {@link GraphNode}, {@link GraphEdge} or null. This can be seen as a filter. 
	 * If a {@link GraphNode} is specified, the method will return all GraphNodes of the {@link NetworkComponent}. 
	 * The same applies for an instance of {@link GraphEdge}, while with Null the method will returns all
	 * {@link GraphElement}'s.
	 *
	 * @param networkComponent the network component
	 * @param searchForInstance the search for instance
	 * @return the HashSet of the GraphElement ID's
	 */
	public abstract HashSet<String> extractGraphElementIDs(NetworkComponent networkComponent, GraphElement searchForInstance);
	
		
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
	public abstract HashSet<GraphElement> getGraphElementsOfNetworkComponent(NetworkComponent networkComponent, GraphElement searchForInstance);
	
	/**
	 * Gives the set of network components containing the given node.
	 * 
	 * @param graphNode - A GraphNode
	 * @return HashSet<NetworkComponent> - The set of components which contain the node
	 */
	public abstract HashSet<NetworkComponent> getNetworkComponents(GraphNode graphNode);

	/**
	 * Gets the network components.
	 * @return the networkComponents
	 */
	public abstract TreeMap<String, NetworkComponent> getNetworkComponents();
	/**
	 * Sets the network components.
	 * @param networkComponents the networkComponents to set
	 */
	public abstract void setNetworkComponents(TreeMap<String, NetworkComponent> networkComponents);

	
	/**
	 * Generates the next unique network component ID in the series n1, n2, n3, ...
	 * @return the next unique network component ID
	 */
	public abstract String nextNetworkComponentID();

	/**
	 * Generates the next unique node ID in the series PP0, PP1, PP2, ...
	 * @return String The next unique node ID that can be used.
	 */
	public abstract String nextNodeID();
	
	/**
	 * Corrects the name definitions of a supplement NetworkModel in  
	 * order to avoid name clashes with the current NetworkModel.
	 *
	 * @param supplementNetworkModel a supplement NetworkModel
	 * @return the NetworkModel with corrected names
	 */
	public abstract NetworkModel adjustNameDefinitionsOfSupplementNetworkModel(NetworkModel supplementNetworkModel);
	
	/**
	 * Merges the current NetworkModel with an incoming NetworkModel as supplement.
	 *
	 * @param supplementNetworkModel the supplement NetworkModel
	 * @param node2Merge the nodes to merge as GraphNodePairs
	 */
	public abstract void mergeNetworkModel(NetworkModel supplementNetworkModel, GraphNodePairs node2Merge);
	
	/**
	 * Gets the valid configuration for a GraphNodePair, that can be used for merging nodes.
	 *
	 * @param graphNodePairs the graph node pairs
	 * @return the valid GraphNodePair for merging couples of GraphNodes
	 */
	public GraphNodePairs getValidGraphNodePairConfig4Merging(GraphNodePairs graphNodePairs);
	
	/**
	 * Merges the network model by using at least two (selected) nodes.
	 * @param nodes2Merge the nodes that have to be merge, as GraphNodePairs
	 */
	public abstract void mergeNodes(GraphNodePairs nodes2Merge);

	/**
	 * Splits the network model at a specified node.
	 * @param node2SplitAt the node
	 */
	public abstract void splitNetworkModelAtNode(GraphNode node2SplitAt);

	/**
	 * Gets a shifted position for a node in relation to the raster size of the component type settings.
	 * @param fixedNode the fixed node
	 * @param shiftNode the shift node
	 * @return the shifted position
	 */
	public abstract Point2D getShiftedPosition(GraphNode fixedNode,
			GraphNode shiftNode);

	/**
	 * Returns the network component vector with the DistributionNode as last.
	 * 
	 * @param componentHashSet the component hash set
	 * @return the network component vector with distribution node as last
	 */
	public abstract Vector<NetworkComponent> getNetworkComponentVectorWithDistributionNodeAsLast(HashSet<NetworkComponent> componentHashSet);

	/**
	 * Returns the first {@link DistributionNode} NetworkComponent, if available.
	 * 
	 * @param componentHashSet the component hash set
	 * @return the distribution node
	 */
	public abstract NetworkComponent getDistributionNode(HashSet<NetworkComponent> componentHashSet);
	
	/**
	 * Checks, if a component list contains distribution node.
	 * 
	 * @param components the components as HashSet<NetworkComponent>
	 * @return the network component, which is the DistributionNode or null
	 */
	public abstract NetworkComponent containsDistributionNode(
			HashSet<NetworkComponent> components);

	/**
	 * Returns the cluster components of the NetworkModel.
	 * @return the cluster components
	 */
	public abstract ArrayList<ClusterNetworkComponent> getClusterComponents();

	/**
	 * Gets the cluster components of a collection of clusterComponents.
	 *
	 * @param components the components
	 * @return the cluster components
	 */
	public abstract ArrayList<ClusterNetworkComponent> getClusterComponents(
			Collection<NetworkComponent> components);

	/**
	 * Checks whether a network component is in the star graph element
	 * 
	 * @param comp the network component
	 * @return true if the component is a star graph element
	 */
	public abstract boolean isStarGraphElement(NetworkComponent comp);

	/**
	 * Given a node and a graph component of star prototype, checks whether the node is the center of the star or not.
	 * 
	 * @param node The node to be checked
	 * @param comp The network component containing the node having the star prototype
	 */
	public abstract boolean isCenterNodeOfStar(GraphNode node,
			NetworkComponent comp);

	/**
	 * Checks if is free node.
	 *
	 * @param graphNode the GraphNode
	 * @return true, if is free node
	 */
	public abstract boolean isFreeGraphNode(GraphNode graphNode);

	/**
	 * Replace NetworkComponents by one ClusterComponent.
	 * @param networkComponents A List of NetworkComponents
	 */
	public abstract ClusterNetworkComponent replaceComponentsByCluster(HashSet<NetworkComponent> networkComponents, boolean distributionNodesAreOuterNodes);

	/**
	 * Gets the outer network components.
	 * @return the outer network components
	 */
	public abstract ArrayList<String> getOuterNetworkComponentIDs();
	
	/**
	 * Gets the connections of biggest branch.
	 *
	 * @return the connections of biggest branch
	 */
	public abstract int getConnectionsOfBiggestBranch();
}