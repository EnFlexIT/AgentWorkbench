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
package org.awb.env.networkModel.controller.ui.commands;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.controller.ui.GraphEnvironmentControllerGUI;
import org.awb.env.networkModel.controller.ui.TransformerForGraphNodePosition;
import org.awb.env.networkModel.controller.ui.configLines.ConfiguredLineEdit;

import agentgui.core.application.Language;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * The Class MoveGraphNodes is used in the context of the UndoManager
 * of the {@link GraphEnvironmentControllerGUI} and can Undo or Redo 
 * movements of {@link GraphNode} .
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class ConfiguredLineEditAction extends AbstractUndoableEdit {

	private static final long serialVersionUID = -7057568320137759472L;

	private GraphEnvironmentController graphController;
	private VisualizationViewer<GraphNode,GraphEdge> visViewer;
	private TransformerForGraphNodePosition<GraphNode, GraphEdge> graphNodePositionTransformer;
				
	private ConfiguredLineEdit configuredLineEdit;
	
	
	/**
	 * Instantiates a new UndoableEdit for the movements of GraphNode's.
	 *
	 * @param graphController the graph controller
	 * @param visViewer the vis viewer
	 * @param nodesMovedOldPositions the nodes moved old positions
	 */
	public ConfiguredLineEditAction(GraphEnvironmentController graphController, VisualizationViewer<GraphNode,GraphEdge> visViewer, ConfiguredLineEdit configuredLineEdit) {
		this.graphController = graphController;
		this.visViewer = visViewer; 	
		this.configuredLineEdit = configuredLineEdit;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#getPresentationName()
	 */
	@Override
	public String getPresentationName() {
		return Language.translate("Configure Graph Edge", Language.EN);
	}

	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		this.setGraphEdgeConfiguration(this.configuredLineEdit.getGraphEdgeNew());
		this.setGraphNodePosition(this.configuredLineEdit.getGraphNodeNewFrom());
		this.setGraphNodePosition(this.configuredLineEdit.getGraphNodeNewTo());
		this.sendNodesMovedNotification();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		this.setGraphEdgeConfiguration(this.configuredLineEdit.getGraphEdgeOld());
		this.setGraphNodePosition(this.configuredLineEdit.getGraphNodeOldFrom());
		this.setGraphNodePosition(this.configuredLineEdit.getGraphNodeOldTo());
		this.sendNodesMovedNotification();
	}
	
	/**
	 * Sets the position of the specified GraphNode as configured in there.
	 * @param graphNodeCopy the new position
	 */
	private void setGraphNodePosition(GraphNode graphNodeCopy) {
		GraphNode node = (GraphNode) this.graphController.getNetworkModel().getGraphElement(graphNodeCopy.getId());
		if (node!=null) {
			node.setPosition(graphNodeCopy.getPosition());
			this.visViewer.getGraphLayout().setLocation(node, this.getGraphNodePositionTransformer().transform(node.getPosition()));
		}
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
	
	/**
	 * Sets the graph edge configuration.
	 * @param graphEdgeCopy the new graph edge configuration
	 */
	private void setGraphEdgeConfiguration(GraphEdge graphEdgeCopy) {
		GraphEdge edge = (GraphEdge) this.graphController.getNetworkModel().getGraphElement(graphEdgeCopy.getId());
		edge.setEdgeShapeConfiguration(graphEdgeCopy.getEdgeShapeConfiguration());
	}
	
	/**
	 * Send notification to the observer.
	 */
	private void sendNodesMovedNotification() {
		this.graphController.notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Nodes_Moved));
		this.graphController.setProjectUnsaved();
	}
	
}
