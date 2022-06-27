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

import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphEdgeShapeConfiguration;
import org.awb.env.networkModel.GraphElement;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.controller.ui.BasicGraphGui;
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
	private BasicGraphGui basicGraphGui;
	private VisualizationViewer<GraphNode,GraphEdge> visViewer;
				
	private ConfiguredLineEdit configuredLineEdit;
	
	/**
	 * Instantiates a new UndoableEdit for the movements of GraphNode's.
	 *
	 * @param graphController the graph controller
	 * @param basicGraphGui the current {@link BasicGraphGui}
	 * @param configuredLineEdit the {@link ConfiguredLineEdit} that describes the last edit action
	 */
	public ConfiguredLineEditAction(GraphEnvironmentController graphController, BasicGraphGui basicGraphGui, ConfiguredLineEdit configuredLineEdit) {
		this.graphController = graphController;
		this.basicGraphGui = basicGraphGui;
		this.visViewer = basicGraphGui.getVisualizationViewer(); 	
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
		List<GraphElement> geList = new ArrayList<>();
		geList.add(this.setGraphEdgeConfiguration(this.configuredLineEdit.getGraphEdgeNew()));
		geList.add(this.setGraphNodePosition(this.configuredLineEdit.getGraphNodeNewFrom()));
		geList.add(this.setGraphNodePosition(this.configuredLineEdit.getGraphNodeNewTo()));
		this.setGraphElementSelection(geList);
		this.sendNodesMovedNotification();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		List<GraphElement> geList = new ArrayList<>();
		geList.add(this.setGraphEdgeConfiguration(this.configuredLineEdit.getGraphEdgeOld()));
		geList.add(this.setGraphNodePosition(this.configuredLineEdit.getGraphNodeOldFrom()));
		geList.add(this.setGraphNodePosition(this.configuredLineEdit.getGraphNodeOldTo()));
		this.setGraphElementSelection(geList);
		this.sendNodesMovedNotification();
	}
	
	/**
	 * Sets the position of the specified GraphNode as configured in there.
	 *
	 * @param graphNodeCopy the new position
	 * @return the graph node that was adjusted in the position
	 */
	private GraphNode setGraphNodePosition(GraphNode graphNodeCopy) {
		GraphNode node = (GraphNode) this.graphController.getNetworkModel().getGraphElement(graphNodeCopy.getId());
		if (node!=null) {
			node.getPosition().setLocation(graphNodeCopy.getPosition().getX(), graphNodeCopy.getPosition().getY());
			this.visViewer.getGraphLayout().setLocation(node, this.getGraphNodePositionTransformer().apply(node.getPosition()));
		}
		return node;
	}
	/**
	 * Returns the graph node position transformer.
	 * @return the graph node position transformer
	 */
	private TransformerForGraphNodePosition getGraphNodePositionTransformer() {
		return this.basicGraphGui.getCoordinateSystemPositionTransformer();
	}
	
	/**
	 * Sets the graph edge configuration.
	 * @param graphEdgeCopy the new graph edge configuration
	 */
	private GraphEdge setGraphEdgeConfiguration(GraphEdge graphEdgeCopy) {
		GraphEdge edge = (GraphEdge) this.graphController.getNetworkModel().getGraphElement(graphEdgeCopy.getId());
		if (edge!=null) {
			GraphEdgeShapeConfiguration<? extends Shape> shapeConfig = null;
			if (graphEdgeCopy.getEdgeShapeConfiguration()!=null) {
				shapeConfig = graphEdgeCopy.getEdgeShapeConfiguration().getCopy();
			}
			edge.setEdgeShapeConfiguration(shapeConfig);
		}
		return edge;
	}
	
	/**
	 * Sets the graph element selection to the specified list elements.
	 * @param geList the new graph element selection
	 */
	private void setGraphElementSelection(List<GraphElement> geList) {
		this.visViewer.getPickedVertexState().clear();
		this.visViewer.getPickedEdgeState().clear();
		for (int i = 0; i < geList.size(); i++) {
			GraphElement ge = geList.get(i);
			if (ge instanceof GraphNode) {
				this.visViewer.getPickedVertexState().pick((GraphNode)ge, true);
			} else if (ge instanceof GraphEdge) {
				this.visViewer.getPickedEdgeState().pick((GraphEdge)ge, true);
			}
		}
	}
	
	/**
	 * Send notification to the observer.
	 */
	private void sendNodesMovedNotification() {
		this.graphController.notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Nodes_Moved));
		this.graphController.setProjectUnsaved();
	}
	
}
