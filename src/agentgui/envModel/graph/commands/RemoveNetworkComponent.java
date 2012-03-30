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
package agentgui.envModel.graph.commands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import edu.uci.ics.jung.graph.Graph;

import agentgui.core.application.Language;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.GraphEdge;
import agentgui.envModel.graph.networkModel.GraphNode;
import agentgui.envModel.graph.networkModel.GraphNodePairs;
import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.networkModel.NetworkModelNotification;
import agentgui.envModel.graph.prototypes.DistributionNode;

/**
 * This action can be used in order to remove a NetworkComponent.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class RemoveNetworkComponent extends AbstractUndoableEdit {

	private static final long serialVersionUID = -4772137855514690242L;

	private GraphEnvironmentController graphController = null;
	private Vector<NetworkComponent> networkComponents2Remove = null;
	
	private NetworkModel extractedNetworkModel = null;
	private HashMap<NetworkComponent, Vector<GraphNodePairs>> nodeConnections = null; 
	
	
	/**
	 * Instantiates the new action in order to remove a set of NetworkComponents's.
	 *
	 * @param graphController the graph controller
	 * @param networkComponent2Remove the network component2 remove
	 */
	public RemoveNetworkComponent(GraphEnvironmentController graphController, HashSet<NetworkComponent> networkComponents2Remove) {
		super();
		this.graphController = graphController;
		this.networkComponents2Remove = new Vector<NetworkComponent>(networkComponents2Remove);
		this.doEdit();
	}

	/**
	 * Do the wished edit.
	 */
	private void doEdit() {
		
		this.nodeConnections = new HashMap<NetworkComponent, Vector<GraphNodePairs>>();
		this.extractedNetworkModel = new NetworkModel();
		
		for (int i = 0; i < this.networkComponents2Remove.size(); i++) {

			NetworkComponent networkComponent = this.networkComponents2Remove.get(i);
			
			this.transfer2localNetworkModel(networkComponent);
			this.graphController.getNetworkModel().removeNetworkComponent(networkComponent);
			this.graphController.removeAgent(networkComponent);
			
			NetworkModelNotification  notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Component_Removed);
			notification.setInfoObject(networkComponent);
			this.graphController.notifyObservers(notification);
		}
		this.graphController.setProjectUnsaved();
		
	}
	
	/**
	 * Transfer the components to remove to the local temporal NetworkModel.
	 * @param networkComponent the NetworkComponent
	 */
	private void transfer2localNetworkModel(NetworkComponent networkComponent) {
		
		NetworkModel sourceNetworkModel = this.graphController.getNetworkModel();
		NetworkModel destinNetworkModel = this.extractedNetworkModel;
		
		// --- Work on the graph ----------------------------------------------
		Graph<GraphNode, GraphEdge> sourceGraph = sourceNetworkModel.getGraph();
		Graph<GraphNode, GraphEdge> destinGraph = destinNetworkModel.getGraph();
		
		// --- Split the nodes of the component from the rest of --------------
		// --- the network and remind these connections 		 --------------
		Vector<GraphNodePairs> nodeConnections4Component = new Vector<GraphNodePairs>();
		HashSet<String> nodeIDs = sourceNetworkModel.extractElementIDs(networkComponent, true);
		for (String nodeID: nodeIDs) {
			GraphNode node2SplitAt = (GraphNode) sourceNetworkModel.getGraphElement(nodeID);
			// --- Split the connection node ------------------------
			GraphNodePairs couples = sourceNetworkModel.splitNetworkModelAtNode(node2SplitAt, false);
			// --- Remind the connections of this node --------------
			if (couples.getGraphNode2Hash().size()>0) {
				nodeConnections4Component.add(couples);	
			}
		}
		// --- Remind the connections of this NetworkComponent ---------------- 
		this.nodeConnections.put(networkComponent, nodeConnections4Component);
		
		// --- Transfer the graph representation of the network component ----- 
		NetworkComponent netComp = sourceNetworkModel.getNetworkComponent(networkComponent.getId());
		HashSet<String> edgeIDs = sourceNetworkModel.extractElementIDs(netComp, false);
		for (String edgeID: edgeIDs) {
			GraphEdge edge = (GraphEdge) sourceNetworkModel.getGraphElement(edgeID);
			GraphNode node1 = sourceGraph.getEndpoints(edge).getFirst();
			GraphNode node2 = sourceGraph.getEndpoints(edge).getSecond();
			destinGraph.addEdge(edge, node1, node2, sourceGraph.getEdgeType(edge));
		}
		
		// --- DistributionNode ? ---------------------------------------------
		if (netComp.getPrototypeClassName().equalsIgnoreCase(DistributionNode.class.getName())) {
			GraphNode node = (GraphNode) sourceNetworkModel.getGraphElement(nodeIDs.iterator().next());
			destinGraph.addVertex(node);
		}
		
		// --- Work on the network component ----------------------------------
		destinNetworkModel.addNetworkComponent(networkComponent);
		
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#getPresentationName()
	 */
	@Override
	public String getPresentationName() {
		return Language.translate("Netzwerkkomponente(n) entfernen");
	}

	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		this.doEdit();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	@Override
	public void undo() throws CannotUndoException {
		super.undo();

		for (int i = this.networkComponents2Remove.size()-1; i > -1; i--) {

			NetworkComponent networkComponent = this.networkComponents2Remove.get(i);
			this.transfer2GlobalNetworkModel(networkComponent);
			
			this.graphController.getNetworkModel().addNetworkComponent(networkComponent);
			this.graphController.addAgent(networkComponent);
			
			NetworkModelNotification  notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Component_Added);
			notification.setInfoObject(networkComponent);
			this.graphController.notifyObservers(notification);
		}
		this.graphController.setProjectUnsaved();
	
	}

	/**
	 * Transfer the components that have been removed back to the global NetworkModel.
	 * @param networkComponent the NetworkComponent
	 */
	private void transfer2GlobalNetworkModel(NetworkComponent networkComponent) {
		
		NetworkModel sourceNetworkModel = this.extractedNetworkModel;
		NetworkModel destinNetworkModel = this.graphController.getNetworkModel();
		
		// --- Work on the graph ----------------------------------------------
		Graph<GraphNode, GraphEdge> sourceGraph = sourceNetworkModel.getGraph();
		Graph<GraphNode, GraphEdge> destinGraph = destinNetworkModel.getGraph();
		
		// --- Transfer the graph representation of the network component ----- 
		NetworkComponent netComp = sourceNetworkModel.getNetworkComponent(networkComponent.getId());
		HashSet<String> nodeIDs = sourceNetworkModel.extractElementIDs(networkComponent, true);
		HashSet<String> edgeIDs = sourceNetworkModel.extractElementIDs(netComp, false);
		for (String edgeID: edgeIDs) {
			GraphEdge edge = (GraphEdge) sourceNetworkModel.getGraphElement(edgeID);
			GraphNode node1 = sourceGraph.getEndpoints(edge).getFirst();
			GraphNode node2 = sourceGraph.getEndpoints(edge).getSecond();
			destinGraph.addEdge(edge, node1, node2, sourceGraph.getEdgeType(edge));
		}
		
		// --- DistributionNode ? ---------------------------------------------
		if (netComp.getPrototypeClassName().equalsIgnoreCase(DistributionNode.class.getName())) {
			GraphNode node = (GraphNode) sourceNetworkModel.getGraphElement(nodeIDs.iterator().next());
			destinGraph.addVertex(node);
		}
		
		// --- Work on the network component ----------------------------------
		destinNetworkModel.addNetworkComponent(networkComponent);
		
		// --- Merge the old connections --------------------------------------
		Vector<GraphNodePairs> connections = this.nodeConnections.get(networkComponent); 
		for (int i = 0; i < connections.size(); i++) {
			GraphNodePairs conection = connections.get(i);
			destinNetworkModel.mergeNodes(conection);
		}
		
	}
	
}
