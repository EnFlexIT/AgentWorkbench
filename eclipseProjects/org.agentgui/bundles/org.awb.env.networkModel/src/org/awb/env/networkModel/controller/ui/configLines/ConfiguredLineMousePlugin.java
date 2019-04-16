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
package org.awb.env.networkModel.controller.ui.configLines;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;

import javax.swing.SwingUtilities;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphElement;
import org.awb.env.networkModel.GraphElementLayout;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.controller.ui.BasicGraphGui;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;
import org.awb.env.networkModel.controller.ui.TransformerForGraphNodePosition;
import org.awb.env.networkModel.settings.LayoutSettings;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

/**
 * Handling mouse interaction with graph visualizations in a BasicGraphGUI.
 * 
 * @see BasicGraphGui  
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public class ConfiguredLineMousePlugin extends PickingGraphMousePlugin<GraphNode, GraphEdge> implements MouseWheelListener, MouseMotionListener, Observer {
	
	private GraphEnvironmentController graphController;
	private BasicGraphGui basicGraphGUI;
	private BasicGraphGuiVisViewer<GraphNode, GraphEdge> visViewer; 	
	private TransformerForGraphNodePosition<GraphNode, GraphEdge> graphNodePositionTransformer;
	
	/** Move panel with right currently ? */
	private boolean movePanelWithRightAction = false;
	/** Move node with left currently ? */
	private boolean moveNodeWithLeftAction = false;

	private Vector<GraphNode> nodesTemp = new Vector<GraphNode>();
	private Vector<GraphNode> nodesMoved = new Vector<GraphNode>();
	private HashMap<String, Point2D> nodesMovedOldPositions;
	
	/** Whether to center the zoom at the current mouse position */
	private boolean zoomAtMouse = true;
	/** controls scaling operations */
    private ScalingControl scaler = new CrossoverScalingControl();
    /** the amount to zoom in by */
	protected float in = 1.1f;
	/** the amount to zoom out by */
	protected float out = 1/1.1f;
	
	/**
	 * Constructor.
	 * @param basicGraphGui the BasicGraphGui
	 */
	public ConfiguredLineMousePlugin(BasicGraphGui basicGraphGui) {
		super();
		this.basicGraphGUI = basicGraphGui;
		this.getGraphController().addObserver(this);
	}
	
	/**
	 * Gets the vis viewer.
	 * @return the vis viewer
	 */
	private BasicGraphGuiVisViewer<GraphNode, GraphEdge> getVisViewer(){
		if (this.visViewer==null) {
			this.visViewer = this.basicGraphGUI.getVisualizationViewer();
		}
		return this.visViewer;
	}
	
	/**
	 * Returns the current GraphEnvironmentController.
	 * @return the graph controller
	 */
	private GraphEnvironmentController getGraphController() {
		if (graphController==null) {
			graphController=this.basicGraphGUI.getGraphEnvironmentController();
		}
		return graphController;		
	}
	
	/**
	 * Returns the graph node position transformer.
	 * @return the graph node position transformer
	 */
	private TransformerForGraphNodePosition<GraphNode, GraphEdge> getGraphNodePositionTransformer() {
		if (graphNodePositionTransformer==null) {
			graphNodePositionTransformer = new TransformerForGraphNodePosition<>(this.getGraphController());
		}
		return graphNodePositionTransformer;
	}
	
	/**
	 * Rounds a position value to the closest position using the grid raster.
	 * @param position the position
	 * @param snapRaster the snap raster size
	 * @return the new position coordinate
	 */
	private double roundGridSnap(double position, double snapRaster) {
		
		double dblDivisor = position/snapRaster;  
		double dblDivisorLB = Math.floor(dblDivisor);
		double dblResidual = Math.round(dblDivisor - dblDivisorLB); 
		double rounded = (dblDivisorLB + dblResidual) * snapRaster;
		return rounded;
	}
	
	/**
	 * Sets the nodes moved2 end position.
	 */
	private void setNodesMoved2EndPosition() {
		this.removeAllTemporaryNodes(this.getVisViewer().getGraphLayout().getGraph());
		for (int i = 0; i < this.nodesMoved.size(); i++) {
			GraphNode node = this.nodesMoved.get(i);
			this.getVisViewer().getGraphLayout().setLocation(node, this.getGraphNodePositionTransformer().transform(node.getPosition()));
		}
	}
	
	/**
	 * Sets a temporary node.
	 * @param graph the graph
	 * @param point2d the point2d
	 */
	private void addTemporaryNode(Graph<GraphNode, GraphEdge> graph, GraphNode pickedNode, Point2D point2d) {
		
		GraphNode tmpNode = new GraphNode();
		tmpNode.setPosition(point2d);

		GraphElementLayout layoutPickedNode = pickedNode.getGraphElementLayout(this.basicGraphGUI.getGraphEnvironmentController().getNetworkModel());
		GraphElementLayout layoutTmpNode = tmpNode.getGraphElementLayout(this.basicGraphGUI.getGraphEnvironmentController().getNetworkModel());
		layoutTmpNode.setSize(layoutPickedNode.getSize());
		layoutTmpNode.setShapeForm(layoutPickedNode.getShapeForm());
		layoutTmpNode.setImageReference(layoutPickedNode.getImageReference());
		
		graph.addVertex(tmpNode);
		this.nodesTemp.add(tmpNode);
	}
	/**
	 * Removes all temporary nodes.
	 * @param graph the graph
	 */
	private void removeAllTemporaryNodes(Graph<GraphNode, GraphEdge> graph) {
		for (int i = 0; i < this.nodesTemp.size(); i++) {
			GraphNode node = this.nodesTemp.get(i);
			graph.removeVertex(node);
		}
		this.nodesTemp.removeAllElements();
	}
	
	/**
	 * Sets the reminder for the old positions of the currently moved GraphNodes.
	 * @param graphNodes the graph nodes
	 */
	private void remindOldPositions() {
		nodesMovedOldPositions = new HashMap<String, Point2D>();
		// --- Get selected GraphNodes ----------
		Set<GraphNode> nodesSelected = this.getVisViewer().getPickedVertexState().getPicked();
		for (GraphNode node : nodesSelected) {
			Point2D point = new Point2D.Double(node.getPosition().getX(), node.getPosition().getY());
			nodesMovedOldPositions.put(node.getId(), point);
		}
	}
	/**
	 * Creates the undoable move action.
	 */
	private void createUndoableMoveAction() {
		if (this.nodesMovedOldPositions!=null) {
			if (this.nodesMovedOldPositions.size()>0) {
				// --- Get selected GraphNodes ----------
				Set<GraphNode> nodesSelected = this.getVisViewer().getPickedVertexState().getPicked();
				if (this.nodesMovedOldPositions.size()==nodesSelected.size()) {
					for (GraphNode node : nodesSelected) {
						Point2D pointCurrent = new Point2D.Double(node.getPosition().getX(), node.getPosition().getY());
						Point2D pointStored  = this.nodesMovedOldPositions.get(node.getId());
						if (pointCurrent.equals(pointStored)==false) {
							this.basicGraphGUI.getGraphEnvironmentController().getNetworkModelUndoManager().setGraphNodesMoved(this.getVisViewer(), this.nodesMovedOldPositions);
							break;
						}
					} // end for
					
				} else {
					// --- Should never happen ------------
					this.basicGraphGUI.getGraphEnvironmentController().getNetworkModelUndoManager().setGraphNodesMoved(this.getVisViewer(), this.nodesMovedOldPositions);
				}
			}
			this.nodesMovedOldPositions = null;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent me) {
		//System.err.println("[" + this.getClass().getSimpleName() + "] Mouse MOVED");
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent me){
		System.err.println("[" + this.getClass().getSimpleName() + "] Mouse CLICKED");
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent me) {
		
		Point position = me.getPoint();
		GraphElementAccessor<GraphNode, GraphEdge> ps = this.getVisViewer().getPickSupport();
		GraphNode pickedNode = ps.getVertex(this.getVisViewer().getGraphLayout(), position.getX(), position.getY());
		GraphEdge pickedEdge = ps.getEdge(this.getVisViewer().getGraphLayout(), position.getX(), position.getY());
		
		if (SwingUtilities.isRightMouseButton(me)) {
			if(pickedNode==null && pickedEdge==null){		
				this.movePanelWithRightAction = true;
				this.getVisViewer().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			}
			
		} else if (SwingUtilities.isLeftMouseButton(me)) {
			if (pickedNode!=null) {
				this.moveNodeWithLeftAction = true;	
				this.remindOldPositions();
			}
		}
		
		System.err.println("[" + this.getClass().getSimpleName() + "] Mouse PRESSED");
		if (this.movePanelWithRightAction==true) {
			super.mousePressed(me);
		}

		
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent me) {
		
		super.mouseReleased(me);
		if (SwingUtilities.isRightMouseButton(me)) {
			if (this.movePanelWithRightAction==true) {
				this.movePanelWithRightAction = false;
				this.getVisViewer().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			
		} else if (SwingUtilities.isLeftMouseButton(me)) {
			if (this.moveNodeWithLeftAction==true) {
				this.moveNodeWithLeftAction = false;	
				this.setNodesMoved2EndPosition();
				this.createUndoableMoveAction();
				this.nodesMoved.removeAllElements();
			} 
			
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent me){

		// --- Execute the normal (but corrected) super method ------
		this.mouseDraggedSuperAction(me);
		
		// ----------------------------------------------------------------------------------------
		// --- Action if the right mouse button is pressed and no graph element is selected -------
		// ----------------------------------------------------------------------------------------
		if (this.movePanelWithRightAction==true) {

			MutableTransformer modelTransformer = this.getVisViewer().getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
            try {
                Point2D q = this.getVisViewer().getRenderContext().getMultiLayerTransformer().inverseTransform(down);
                Point2D p = this.getVisViewer().getRenderContext().getMultiLayerTransformer().inverseTransform(me.getPoint());
                float dx = (float) (p.getX()-q.getX());
                float dy = (float) (p.getY()-q.getY());
                
                modelTransformer.translate(dx, dy);
                down.x = me.getX();
                down.y = me.getY();
            } catch(RuntimeException ex) {
                System.err.println("down = "+down+", e = "+me);
                throw ex;
            }
            me.consume();
		}
		
		// ----------------------------------------------------------------------------------------
		// --- Update the GraphNode's position attribute ------------------------------------------ 
		// ----------------------------------------------------------------------------------------
		if (this.moveNodeWithLeftAction==true) {
			
			Graph<GraphNode, GraphEdge> graph = null;
			LayoutSettings layoutSettings = this.basicGraphGUI.getGraphEnvironmentController().getNetworkModel().getLayoutSettings();
			boolean snapToGrid = layoutSettings.isSnap2Grid();
			double snapRaster = layoutSettings.getSnapRaster();
			
			Set<GraphNode> pickedNodes = this.getVisViewer().getPickedVertexState().getPicked();
			for(GraphNode pickedNode: pickedNodes){

				// --- Get the Graph, if not already there --------------------
				if (graph==null) {
					graph = this.basicGraphGUI.getGraphEnvironmentController().getNetworkModel().getGraph();
					this.nodesMoved.removeAllElements();
					this.removeAllTemporaryNodes(graph);
				}
				
				// --- Get the position of the node ---------------------------
				Point2D newPos = this.getVisViewer().getGraphLayout().transform(pickedNode);
				newPos = this.getGraphNodePositionTransformer().inverseTransform(newPos);
				if (snapToGrid==true && snapRaster>0) {
					double xPos = roundGridSnap(newPos.getX(), snapRaster); 
					double yPos = roundGridSnap(newPos.getY(), snapRaster);
					newPos.setLocation(xPos, yPos);
					
					this.nodesMoved.add(pickedNode);
					this.addTemporaryNode(graph, pickedNode, newPos);
				}
				pickedNode.setPosition(newPos);
				
			}
			me.consume();
		}
		
	}

	/**
	 * This is the MouseDragged super action of the super class (because of several exceptions).
	 * @param me the MouseEvent
	 */
	private void mouseDraggedSuperAction(MouseEvent me) {
	
		if (locked == false) {
            VisualizationViewer<GraphNode,GraphEdge> vv = this.basicGraphGUI.getVisualizationViewer();
            if(vertex != null) {
                Point p = me.getPoint();
                Point2D graphPoint = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(p);
                Point2D graphDown = vv.getRenderContext().getMultiLayerTransformer().inverseTransform(down);
                Layout<GraphNode,GraphEdge> layout = vv.getGraphLayout();
                double dx = graphPoint.getX()-graphDown.getX();
                double dy = graphPoint.getY()-graphDown.getY();
                PickedState<GraphNode> ps = vv.getPickedVertexState();
                
                for(GraphNode v : ps.getPicked()) {
                    Point2D vp = layout.transform(v);
                    vp.setLocation(vp.getX()+dx, vp.getY()+dy);
                    layout.setLocation(v, vp);
                }
                down = p;
                me.consume();
                vv.repaint();
                
            } else {
                Point2D out = me.getPoint();
                if(me.getModifiers() == this.addToSelectionModifiers || me.getModifiers() == modifiers) {
                    if (down!=null) {
                    	rect.setFrameFromDiagonal(down,out);
                    	vv.repaint();
                    }
                }
            }
        }
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent me) {

		Point2D mouse = me.getPoint();
		Point2D center = this.getVisViewer().getCenter();
		int amount = me.getWheelRotation();
         
		if(zoomAtMouse) {
			if(amount > 0) {
				scaler.scale(this.getVisViewer(), out, mouse);
			} else if(amount < 0) {
				scaler.scale(this.getVisViewer(), in, mouse);
			}
			
		} else {
			if(amount > 0) {
				scaler.scale(this.getVisViewer(), out, center);
			} else if(amount < 0) {
				scaler.scale(this.getVisViewer(), in, center);
			}
         }
         me.consume();
		
	}

	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object object) {
		
		if (object instanceof NetworkModelNotification) {
			NetworkModelNotification nmNotification = (NetworkModelNotification) object;
			switch (nmNotification.getReason()) {
			case NetworkModelNotification.NETWORK_MODEL_Paste_Action_Stop:
				// TODO
				break;
			}
		}
		
	}
	
}
