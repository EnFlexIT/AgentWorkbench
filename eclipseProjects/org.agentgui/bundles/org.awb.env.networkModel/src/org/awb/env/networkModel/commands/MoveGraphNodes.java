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
package org.awb.env.networkModel.commands;

import java.awt.geom.Point2D;
import java.util.HashMap;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphElement;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.controller.ui.GraphEnvironmentControllerGUI;
import org.awb.env.networkModel.controller.ui.TransformerForGraphNodePosition;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.picking.MultiPickedState;
import edu.uci.ics.jung.visualization.picking.PickedState;

import agentgui.core.application.Language;

/**
 * The Class MoveGraphNodes is used in the context of the UndoManager
 * of the {@link GraphEnvironmentControllerGUI} and can Undo or Redo 
 * movements of {@link GraphNode} .
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class MoveGraphNodes extends AbstractUndoableEdit {

	private static final long serialVersionUID = -7057568320137759472L;

	private GraphEnvironmentController graphController;
	private VisualizationViewer<GraphNode,GraphEdge> visViewer; 	
	
	private HashMap<String, Point2D> nodesMovedOldPositions;
	private HashMap<String, Point2D> nodesMovedNewPositions;
	
	private TransformerForGraphNodePosition<GraphNode, GraphEdge> graphNodePositionTransformer;
	
	/**
	 * Instantiates a new UndoableEdit for the movements of GraphNode's.
	 *
	 * @param graphController the graph controller
	 * @param visViewer the vis viewer
	 * @param nodesMovedOldPositions the nodes moved old positions
	 */
	public MoveGraphNodes(GraphEnvironmentController graphController, VisualizationViewer<GraphNode,GraphEdge> visViewer, HashMap<String, Point2D> nodesMovedOldPositions) {
		
		this.graphController = graphController;
		this.visViewer = visViewer; 	
		this.nodesMovedOldPositions = nodesMovedOldPositions;
		
		// --- evaluate the current positions ---
		this.nodesMovedNewPositions = new HashMap<String, Point2D>();
		for (String nodeID : this.nodesMovedOldPositions.keySet()) {
			GraphElement graphElement = this.graphController.getNetworkModel().getGraphElement(nodeID);
			if (graphElement instanceof GraphNode) {
				GraphNode node = (GraphNode) this.graphController.getNetworkModel().getGraphElement(nodeID);
				if (node!=null) {
					Point2D point = new Point2D.Double(node.getPosition().getX(), node.getPosition().getY());
					this.nodesMovedNewPositions.put(node.getId(), point);	
				}	
			}
		}
		this.sendNodesMovedNotification();
	}
	
	/**
	 * Sets the positions of the GraphNodes as configured in the HashMap.
	 * @param nodes2Move the nodes2 move
	 */
	private void setPositions(HashMap<String, Point2D> nodes2Move) {
		
		PickedState<GraphNode> ps = new MultiPickedState<GraphNode>();
		this.visViewer.setPickedVertexState(ps);
		
		for (String nodeID : nodes2Move.keySet()) {
			GraphNode node = (GraphNode) this.graphController.getNetworkModel().getGraphElement(nodeID);
			if (node!=null) {
				node.setPosition(nodes2Move.get(nodeID));
				this.visViewer.getGraphLayout().setLocation(node, this.getGraphNodePositionTransformer().transform(node.getPosition()));
				ps.pick(node, true);	
			}
		}
		
		this.sendNodesMovedNotification();
	}
	
	/**
	 * Send notification to the observer.
	 */
	private void sendNodesMovedNotification() {
		this.graphController.notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Nodes_Moved));
		this.graphController.setProjectUnsaved();
	}
	/**
	 * Returns the graph node position transformer.
	 * @return the graph node position transformer
	 */
	private TransformerForGraphNodePosition<GraphNode, GraphEdge> getGraphNodePositionTransformer() {
		if (graphNodePositionTransformer==null) {
			graphNodePositionTransformer = new TransformerForGraphNodePosition<>(this.graphController);
		}
		return graphNodePositionTransformer;
	}

	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#getPresentationName()
	 */
	@Override
	public String getPresentationName() {
		return Language.translate("Knoten verschieben");
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		this.setPositions(nodesMovedNewPositions);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		this.setPositions(nodesMovedOldPositions);
	}
	
}
