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
import org.awb.env.networkModel.settings.LayoutSettings.CoordinateSystemXDirection;

import agentgui.core.application.Language;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * The Class MoveGraphNodes is used in the context of the UndoManager
 * of the {@link GraphEnvironmentControllerGUI} and can Undo or Redo 
 * movements of {@link GraphNode} .
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AlignNodesAction extends AbstractUndoableEdit {

	private static final long serialVersionUID = -7057568320137759472L;

	public enum Alignment {
		Left,
		Right,
		Top,
		Bottom
	}
	
	private GraphEnvironmentController graphController;
	private BasicGraphGui basicGraphGui;
	private VisualizationViewer<GraphNode,GraphEdge> visViewer;
	private CoordinateSystemXDirection coordXDirection;
	
	private Alignment alignment;
	private HashMap<String, Point2D> oldPositionHashMap;
	
	private boolean movementIntoXDirection;
	private double movementNewValue;
	
	private boolean hasChangedPositions;
	
	/**
	 * Instantiates a new UndoableEdit for the movements of GraphNode's.
	 *
	 * @param graphController the graph controller
	 * @param basicGraphGui the basic graph gui
	 * @param alignment the alignment
	 */
	public AlignNodesAction(GraphEnvironmentController graphController, BasicGraphGui basicGraphGui, Alignment alignment) {
		this.graphController = graphController;
		this.basicGraphGui = basicGraphGui;
		this.visViewer = basicGraphGui.getVisualizationViewer();
		this.alignment = alignment;
		this.coordXDirection = this.graphController.getNetworkModel().getLayoutSettings().getCoordinateSystemXDirection();
		
		this.determineGraphNodePositionMovement();
		this.alignNodes(false);
	}
	
	/**
	 * Checks if this action changed any position. If not, the undo manager may neglect this action.
	 * @return true, if any GraphNode was moved
	 */
	public boolean hasChangedPositions() {
		return hasChangedPositions;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#getPresentationName()
	 */
	@Override
	public String getPresentationName() {
		return Language.translate("Align graph nodes", Language.EN);
	}

	/**
	 * Returns the graph node position transformer.
	 * @return the graph node position transformer
	 */
	private TransformerForGraphNodePosition getGraphNodePositionTransformer() {
		return this.basicGraphGui.getCoordinateSystemPositionTransformer();
	}
	
	/**
	 * Returns the reminder HashMap for old positions.
	 * @return the old position hash map
	 */
	private HashMap<String, Point2D> getOldPositionHashMap() {
		if (oldPositionHashMap==null) {
			oldPositionHashMap = new HashMap<>(); 
		}
		return oldPositionHashMap;
	}
	
	/**
	 * Determines the graph node position movement.
	 */
	private void determineGraphNodePositionMovement() {
		
		double minX = Double.MAX_VALUE;
		double maxX = -Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double maxY = -Double.MAX_VALUE;
		
		GraphNode graphNodeMinX = null;
		GraphNode graphNodeMaxX = null;
		GraphNode graphNodeMinY = null;
		GraphNode graphNodeMaxY = null;
		
		List<GraphNode> graphNodeList = new ArrayList<>(this.visViewer.getPickedVertexState().getPicked());
		if (graphNodeList.size()==0) return;
		
		// --- Find destination size --------------------------------
		for (int i = 0; i < graphNodeList.size(); i++) {
			
			GraphNode graphNode = graphNodeList.get(i);
			Point2D pointOnPanel = this.getGraphNodePositionTransformer().transform(graphNode);
			
			// --- Get minimum and maximum x/y-positions ------------
			if (pointOnPanel.getX() < minX) {
				minX = pointOnPanel.getX();
				graphNodeMinX = graphNode;
			}
			if (pointOnPanel.getX() > maxX) {
				maxX = pointOnPanel.getX();
				graphNodeMaxX = graphNode;
			}
			if (pointOnPanel.getY() < minY) {
				minY = pointOnPanel.getY();
				graphNodeMinY = graphNode;
			}
			if (pointOnPanel.getY() > maxY) {
				maxY = pointOnPanel.getY();
				graphNodeMaxY = graphNode;
			}
			// --- Remind old positions -----------------------------
			this.getOldPositionHashMap().put(graphNode.getId(), new Point2D.Double(graphNode.getPosition().getX(), graphNode.getPosition().getY()));
		}
		
		// --- Where to move (in visualization coordinates)? --------
		switch (alignment) {
		case Right:
			// --- Move into maxX direction -----------
			this.movementIntoXDirection = this.isMovementIntoXDirection(true);
			this.movementNewValue = this.getMovementPositionValue(graphNodeMaxX.getPosition(), true);
			break;
		case Bottom:
			// --- Move into maxY direction -----------
			this.movementIntoXDirection = this.isMovementIntoXDirection(false);
			this.movementNewValue = this.getMovementPositionValue(graphNodeMaxY.getPosition(), false);
			break;
		case Left:
			// --- Move into minX direction -----------
			this.movementIntoXDirection = this.isMovementIntoXDirection(true);
			this.movementNewValue = this.getMovementPositionValue(graphNodeMinX.getPosition(), true);
			break;
		case Top:
			// --- Move into minY direction -----------
			this.movementIntoXDirection = this.isMovementIntoXDirection(false);
			this.movementNewValue = this.getMovementPositionValue(graphNodeMinY.getPosition(), false);
			break;
		}
	}
	
	/**
	 * Checks, with respect to the current coordinate system, if the designated movement goes into the X-direction.
	 *
	 * @param isScreenMovementIntoXDirection the is screen movement into X direction
	 * @return true, if is movement into X direction
	 */
	private boolean isMovementIntoXDirection(boolean isScreenMovementIntoXDirection) {
		
		boolean isXMovement = isScreenMovementIntoXDirection;
		if (this.coordXDirection==CoordinateSystemXDirection.North || this.coordXDirection==CoordinateSystemXDirection.South) {
			isXMovement = !isScreenMovementIntoXDirection;
		}
		return isXMovement;
	}
	/**
	 * Returns the movement position value with respect to the current coordinate system.
	 *
	 * @param position the position
	 * @param isGetScreenXValue the indicator, if the x-value has to be returned with respect to the screen coordinates 
	 * @return the movement position value
	 */
	private double getMovementPositionValue(Point2D position, boolean isGetScreenXValue) {
		
		double positionValue = 0.0;
		switch (this.coordXDirection) {
		case East:
		case West:
			if (isGetScreenXValue==true) {
				positionValue = position.getX();
			} else {
				positionValue = position.getY();
			}
			break;

		case North:
		case South:
			if (isGetScreenXValue==true) {
				positionValue = position.getY();
			} else {
				positionValue = position.getX();
			}
			break;
		}
		return positionValue;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		this.alignNodes(false);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		this.alignNodes(true);
	}
	
	/**
	 * Aligns or reverts the GraphNodes.
	 * @param revertPosition the revert position
	 */
	private void alignNodes(boolean revertPosition) {
		
		List<GraphElement> graphNodeList = new ArrayList<>(); 
		List<String> graphNodeIDList = new ArrayList<>(this.getOldPositionHashMap().keySet());
		for (int i = 0; i < graphNodeIDList.size(); i++) {
			
			String graphNodeID = graphNodeIDList.get(i);
			
			// --- Get the GraphNode to be moved --------------------
			GraphNode graphNode = (GraphNode) this.graphController.getNetworkModel().getGraphElement(graphNodeID);
			if (graphNode!=null) {
				
				graphNodeList.add(graphNode);
				if (revertPosition==false) {
					// --- Do the actual alignment ------------------
					Point2D posOld = graphNode.getPosition();
					Point2D posNew = null;
					if (this.movementIntoXDirection==true) {
						posNew = new Point2D.Double(this.movementNewValue, posOld.getY());
					} else {
						posNew = new Point2D.Double(posOld.getX(), this.movementNewValue);
					}	
					if (posNew.equals(posOld)==false) {
						this.hasChangedPositions = true;
						graphNode.setPosition(posNew);
					}
					
				} else {
					Point2D pos = this.getOldPositionHashMap().get(graphNodeID);
					graphNode.setPosition(new Point2D.Double(pos.getX(), pos.getY()));
					
				}
				// -- Set position on Screen ------------------------
				this.visViewer.getGraphLayout().setLocation(graphNode, this.getGraphNodePositionTransformer().transform(graphNode.getPosition()));
			}
		}
		
		if (revertPosition==true) {
			this.setGraphElementSelection(graphNodeList);
		}
		
		this.sendNodesMovedNotification();
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
