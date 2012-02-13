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
import java.util.Vector;

import agentgui.envModel.graph.controller.GeneralGraphSettings4MAS;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import edu.uci.ics.jung.graph.Graph;

public class NetworkModelAction {
	
	
	private GraphEnvironmentController graphController = null;
	private NetworkModel networkModel = null;
	
	/**
	 * Instantiates a new network model action.
	 * @param controller the controller
	 */
	public NetworkModelAction(GraphEnvironmentController controller, NetworkModel networkModel) {
		this.graphController=controller;
		this.networkModel = networkModel;
	}
	
	/**
	 * Notify observer.
	 * @param notification the notification
	 */
	private void notifyObserver(NetworkModelNotification notification) {
		this.graphController.notifyObservers(notification);
	}
	
	
	public void setGeneralGraphSettings4MAS(GeneralGraphSettings4MAS generalGraphSettings4MAS) {
		this.networkModel.setGeneralGraphSettings4MAS(generalGraphSettings4MAS);
	}
	public GeneralGraphSettings4MAS getGeneralGraphSettings4MAS() {
		return this.networkModel.getGeneralGraphSettings4MAS();
	}

	public void setAlternativeNetworkModel(HashMap<String, NetworkModel> alternativeNetworkModel) {
		this.networkModel.setAlternativeNetworkModel(alternativeNetworkModel);
	}
	public HashMap<String, NetworkModel> getAlternativeNetworkModel() {
		return this.networkModel.getAlternativeNetworkModel();
	}

	public NetworkModel getCopy() {
		return this.networkModel.getCopy();
	}

	public GraphElement getGraphElement(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public HashMap<String, GraphElement> getGraphElements() {
		// TODO Auto-generated method stub
		return null;
	}

	public Vector<GraphElement> getGraphElementsFromNetworkComponent(NetworkComponent networkComponent) {
		// TODO Auto-generated method stub
		return null;
	}

	public Graph<GraphNode, GraphEdge> getGraph() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setGraph(Graph<GraphNode, GraphEdge> newGraph) {
		// TODO Auto-generated method stub
		
	}

	public NetworkComponent addNetworkComponent(NetworkComponent networkComponent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void renameComponent(String oldCompID, String newCompID) {
		// TODO Auto-generated method stub
		
	}

	public void removeNetworkComponents(
			Collection<NetworkComponent> networkComponents) {
		// TODO Auto-generated method stub
		
	}

	public void removeInverseNetworkComponents(Collection<NetworkComponent> networkComponents) {
		// TODO Auto-generated method stub
		
	}

	public void removeNetworkComponent(NetworkComponent networkComponent) {
		// TODO Auto-generated method stub
		
	}

	public Vector<GraphNode> getNodesFromNetworkComponent(NetworkComponent networkComponent) {
		// TODO Auto-generated method stub
		return null;
	}

	public NetworkComponent getNetworkComponent(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public HashSet<NetworkComponent> getNeighbourNetworkComponents(HashSet<NetworkComponent> networkComponents) {
		// TODO Auto-generated method stub
		return null;
	}

	public Vector<NetworkComponent> getNeighbourNetworkComponents(NetworkComponent networkComponent) {
		// TODO Auto-generated method stub
		return null;
	}

	public NetworkComponent getNetworkComponent(GraphEdge graphEdge) {
		// TODO Auto-generated method stub
		return null;
	}

	public HashSet<NetworkComponent> getNetworkComponents(Set<GraphNode> graphNodes) {
		// TODO Auto-generated method stub
		return null;
	}

	public HashSet<NetworkComponent> getNetworkComponents(GraphNode graphNode) {
		// TODO Auto-generated method stub
		return null;
	}

	public HashMap<String, NetworkComponent> getNetworkComponents() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setNetworkComponents(HashMap<String, NetworkComponent> networkComponents) {
		// TODO Auto-generated method stub
		
	}

	public String nextNetworkComponentID() {
		// TODO Auto-generated method stub
		return null;
	}

	public String nextNodeID() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean mergeNodes(GraphNode node1, GraphNode node2) {
		// TODO Auto-generated method stub
		return false;
	}

	public void splitNetworkModelAtNode(GraphNode node2SplitAt) {
		// TODO Auto-generated method stub
		
	}

	public Point2D getShiftedPosition(GraphNode fixedNode, GraphNode shiftNode) {
		// TODO Auto-generated method stub
		return null;
	}

	public Vector<NetworkComponent> getNetworkComponentVectorWithDistributionNodeAsLast(HashSet<NetworkComponent> componentVector) {
		// TODO Auto-generated method stub
		return null;
	}

	public NetworkComponent containsDistributionNode(HashSet<NetworkComponent> components) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSingleClusterHashSet(HashSet<NetworkComponent> components) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isStarGraphElement(NetworkComponent comp) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCenterNodeOfStar(GraphNode node, NetworkComponent comp) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isFreeGraphNode(GraphNode graphNode) {
		// TODO Auto-generated method stub
		return false;
	}

	public ClusterNetworkComponent replaceComponentsByCluster(HashSet<NetworkComponent> networkComponents) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<NetworkComponent> getOuterNetworkComponents() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<ClusterNetworkComponent> getClusterComponents() {
		// TODO Auto-generated method stub
		return null;
	}

}
