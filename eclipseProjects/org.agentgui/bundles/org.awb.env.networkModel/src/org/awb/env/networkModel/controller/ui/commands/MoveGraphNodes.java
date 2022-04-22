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

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphElement;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.controller.ui.BasicGraphGui;
import org.awb.env.networkModel.controller.ui.GraphEnvironmentControllerGUI;
import org.awb.env.networkModel.controller.ui.TransformerForGraphNodePosition;
import org.awb.env.networkModel.controller.ui.configLines.PolylineConfiguration;

import agentgui.core.application.Language;
import de.enflexit.common.SerialClone;
import de.enflexit.geography.coordinates.AbstractCoordinate;
import edu.uci.ics.jung.visualization.VisualizationViewer;

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
	private BasicGraphGui basicGraphGui;
	private VisualizationViewer<GraphNode, GraphEdge> visViewer;

	private HashMap<String, Point2D> nodesMovedOldCoordinate;
	private HashMap<String, Point2D> nodesMovedNewCoordinate;

	private HashMap<String, List<Point2D>> polylinesMovedOldPositions;
	private HashMap<String, List<Point2D>> polylinesMovedNewPositions;
	
	
	/**
	 * Instantiates a new UndoableEdit for the movements of GraphNode's.
	 *
	 * @param graphController the graph controller
	 * @param basicGraphGui the current {@link BasicGraphGui}
	 * @param nodesMovedOldCoordinates the nodes moved old positions
	 * @param polylinesMovedOldPositions the polylines moved old positions
	 */
	public MoveGraphNodes(GraphEnvironmentController graphController, BasicGraphGui basicGraphGui, HashMap<String, Point2D> nodesMovedOldCoordinates, HashMap<String, List<Point2D>> polylinesMovedOldPositions) {
		this.graphController = graphController;
		this.basicGraphGui = basicGraphGui;
		this.visViewer = basicGraphGui.getVisualizationViewer();
		this.nodesMovedOldCoordinate = nodesMovedOldCoordinates;
		this.polylinesMovedOldPositions = polylinesMovedOldPositions;

		// --- Evaluate the current GraphNode positions -------------
		this.nodesMovedNewCoordinate = new HashMap<String, Point2D>();
		List<String> nodeIDList = new ArrayList<>(this.nodesMovedOldCoordinate.keySet());
		for (int i = 0; i < nodeIDList.size(); i++) {
			String nodeID = nodeIDList.get(i);
			GraphElement graphElement = this.graphController.getNetworkModel().getGraphElement(nodeID);
			if (graphElement instanceof GraphNode) {
				GraphNode graphNode = (GraphNode) this.graphController.getNetworkModel().getGraphElement(nodeID);
				if (graphNode != null) {
					this.nodesMovedNewCoordinate.put(nodeID, graphNode.getCoordinate());
				}
			}
		}

		// --- Evaluate the current Polyline node positions ---------
		if (this.polylinesMovedOldPositions != null && this.polylinesMovedOldPositions.size() > 0) {

			this.polylinesMovedNewPositions = new HashMap<>();
			List<String> edgeIDList = new ArrayList<>(this.polylinesMovedOldPositions.keySet());
			for (int i = 0; i < edgeIDList.size(); i++) {
				String edgeID = edgeIDList.get(i);
				GraphElement graphElement = this.graphController.getNetworkModel().getGraphElement(edgeID);
				if (graphElement instanceof GraphEdge) {
					GraphEdge edge = (GraphEdge) graphElement;
					if (edge.getEdgeShapeConfiguration() instanceof PolylineConfiguration) {
						PolylineConfiguration polyLineConfig = (PolylineConfiguration) edge.getEdgeShapeConfiguration();
						// --- Copy intermediate points -------------
						List<Point2D> intPointList = new ArrayList<>();
						for (int j = 0; j < polyLineConfig.getIntermediatePoints().size(); j++) {
							intPointList.add(SerialClone.clone(polyLineConfig.getIntermediatePoints().get(j)));
						}
						this.polylinesMovedNewPositions.put(edgeID, intPointList);
					}
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

		this.visViewer.getPickedVertexState().clear();

		List<String> nodeIDList = new ArrayList<>(nodes2Move.keySet());
		for (int i = 0; i < nodeIDList.size(); i++) {
			String nodeID = nodeIDList.get(i);
			GraphNode graphNode = (GraphNode) this.graphController.getNetworkModel().getGraphElement(nodeID);
			if (graphNode != null) {
				Point2D pos = nodes2Move.get(nodeID);
				if (pos instanceof AbstractCoordinate) {
					graphNode.setCoordinate((AbstractCoordinate) pos);
				} else {
					graphNode.setPosition(pos);
				}
				this.visViewer.getGraphLayout().setLocation(graphNode, this.getGraphNodePositionTransformer().apply(graphNode.getPosition()));
				this.visViewer.getPickedVertexState().pick(graphNode, true);
			}
		}

		this.sendNodesMovedNotification();
	}
	/**
	 * Sets the intermediate point positions.
	 * @param polylinesToMove the polylines to move
	 */
	private void setIntermediatePointPositions(HashMap<String, List<Point2D>> polylinesToMove) {

		if (polylinesToMove == null || polylinesToMove.size() == 0) return;

		List<String> nodeIDList = new ArrayList<>(polylinesToMove.keySet());
		for (int i = 0; i < nodeIDList.size(); i++) {
			String edgeID = nodeIDList.get(i);
			GraphEdge edge = (GraphEdge) this.graphController.getNetworkModel().getGraphElement(edgeID);
			if (edge != null) {
				if (edge.getEdgeShapeConfiguration() instanceof PolylineConfiguration) {
					PolylineConfiguration polylineConfig = (PolylineConfiguration) edge.getEdgeShapeConfiguration();
					polylineConfig.setIntermediatePoints(polylinesToMove.get(edgeID));
					this.visViewer.getPickedEdgeState().pick(edge, true);
				}
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
	/**
	 * Returns the graph node position transformer.
	 * @return the graph node position transformer
	 */
	private TransformerForGraphNodePosition getGraphNodePositionTransformer() {
		return this.basicGraphGui.getCoordinateSystemPositionTransformer();
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
		this.setPositions(this.nodesMovedNewCoordinate);
		this.setIntermediatePointPositions(this.polylinesMovedNewPositions);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		this.setPositions(this.nodesMovedOldCoordinate);
		this.setIntermediatePointPositions(this.polylinesMovedOldPositions);
	}

}
