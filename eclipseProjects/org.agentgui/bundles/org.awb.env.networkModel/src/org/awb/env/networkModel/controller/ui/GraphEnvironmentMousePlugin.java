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

package org.awb.env.networkModel.controller.ui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphElement;
import org.awb.env.networkModel.GraphElementLayout;
import org.awb.env.networkModel.GraphGlobals;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.NetworkModelNotification;
import org.awb.env.networkModel.controller.ui.BasicGraphGui.GraphMouseMode;
import org.awb.env.networkModel.settings.LayoutSettings;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
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
public class GraphEnvironmentMousePlugin extends PickingGraphMousePlugin<GraphNode, GraphEdge> implements MouseWheelListener, MouseMotionListener, KeyListener, Observer {
	
	private GraphEnvironmentController graphController;
	private BasicGraphGui basicGraphGUI;
	private BasicGraphGuiVisViewer<GraphNode, GraphEdge> visViewer; 	
	
	private boolean isPasteAction = false;
	private NetworkModel networkModel2Paste;
	private GraphNode graphNodeUpperLeft2Paste;
	private GraphNode[] graphNodes2Paste;
	
	private boolean movePanelWithRightAction = false;
	private boolean moveNodeWithLeftAction = false;

	private Vector<GraphNode> nodesTemp = new Vector<GraphNode>();
	private Vector<GraphNode> nodesMoved = new Vector<GraphNode>();
	private HashMap<String, Point2D> nodesMovedOldPositions;
	

	/**
	 * Constructor.
	 * @param basicGraphGui the BasicGraphGui
	 */
	public GraphEnvironmentMousePlugin(BasicGraphGui basicGraphGui) {
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
	private TransformerForGraphNodePosition<GraphNode, GraphEdge> getCoordinateSystemNodePositionTransformer() {
		return this.basicGraphGUI.getCoordinateSystemPositionTransformer();
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
			this.getVisViewer().getGraphLayout().setLocation(node, this.getCoordinateSystemNodePositionTransformer().transform(node.getPosition()));
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
	
	/**
	 * Acts on the mouse pressed and mouse clicked action.
	 * @param me the {@link MouseEvent}
	 */
	private void mousePressedOrClicked(MouseEvent me) {
		
		// --- Left click ---------------------------------
		if (SwingUtilities.isLeftMouseButton(me) || SwingUtilities.isRightMouseButton(me)){

			// --- Check if an object was selected --------
			Object pickedObject = null;
			Point point = me.getPoint();
			GraphElementAccessor<GraphNode, GraphEdge> ps = this.getVisViewer().getPickSupport();
			GraphNode pickedNode = ps.getVertex(this.getVisViewer().getGraphLayout(), point.getX(), point.getY());
			if (pickedNode!=null) {  
				pickedObject = pickedNode;
			} else {
				GraphEdge pickedEdge = ps.getEdge(this.getVisViewer().getGraphLayout(), point.getX(), point.getY());
				if (pickedEdge!=null) { 
					pickedObject = pickedEdge;
				}
			}

			// --- Only when node or edge is picked -------
			if (pickedObject!=null) {
				if (me.getClickCount()==2){
					// --- Double click ---------
					this.basicGraphGUI.handleObjectDoubleClick(pickedObject);
				} else {
					if (me.isShiftDown()==false) {
						// --- Left click -----------
						this.basicGraphGUI.handleObjectLeftClick(pickedObject);
					}	
				} 
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin#pickContainedVertices(edu.uci.ics.jung.visualization.VisualizationViewer, java.awt.geom.Point2D, java.awt.geom.Point2D, boolean)
	 */
	@Override
	protected void pickContainedVertices(VisualizationViewer<GraphNode, GraphEdge> vv, Point2D down,Point2D out, boolean clear) {
		
		super.pickContainedVertices(vv, down, out, clear);
		NetworkModel networkModel = this.basicGraphGUI.getGraphEnvironmentController().getNetworkModel();
		
		// --- Get the selected nodes ----------------
		Set<GraphNode> nodesSelected = this.getVisViewer().getPickedVertexState().getPicked();
		// --- Get the related NetworkComponent's ---- 
		List<NetworkComponent> components = networkModel.getNetworkComponentsFullySelected(nodesSelected);
		if (components!=null) {
			// --- Run through NetworkComponents -----
			for (int i = 0; i < components.size(); i++) {
				NetworkComponent networkComponent = components.get(i);
				Vector<GraphElement> elements = networkModel.getGraphElementsFromNetworkComponent(networkComponent);
				for (int j = 0; j < elements.size(); j++) {
					GraphElement graphElement = elements.get(j);
					if (graphElement instanceof GraphEdge) {
						this.getVisViewer().getPickedEdgeState().pick((GraphEdge) graphElement, true);
					}
				}
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent me){
		this.mousePressedOrClicked(me);
	}
	
	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent me) {
		
		if (this.isPasteAction==true) {
			if (SwingUtilities.isLeftMouseButton(me)) {
				// --- Finalize paste action --------------
				this.setPasteAction(false, true);
				
			} else if (SwingUtilities.isRightMouseButton(me)) {
				// --- Cancel paste action ----------------
				this.setPasteAction(false, false);	
			}
		}
		
		super.mousePressed(me);

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
			} else {
				this.mousePressedOrClicked(me);	
			}
			
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
			LayoutSettings layoutSettings = this.getGraphController().getNetworkModel().getLayoutSettings();
			boolean snapToGrid = layoutSettings.isSnap2Grid();
			double snapRaster = layoutSettings.getSnapRaster();
			
			Set<GraphNode> pickedNodes = this.getVisViewer().getPickedVertexState().getPicked();
			for(GraphNode pickedNode: pickedNodes){

				// --- Get the Graph, if not already there --------------------
				if (graph==null) {
					graph = this.getGraphController().getNetworkModel().getGraph();
					this.nodesMoved.removeAllElements();
					this.removeAllTemporaryNodes(graph);
				}
				
				// --- Get the position of the node ---------------------------
				Point2D newPos = this.getVisViewer().getGraphLayout().transform(pickedNode);
				newPos = this.getCoordinateSystemNodePositionTransformer().inverseTransform(newPos);
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
		if (me.getWheelRotation()>0) {
			this.basicGraphGUI.zoomOut(me.getPoint());
		} else {
			this.basicGraphGUI.zoomIn(me.getPoint());
		}
		me.consume();
	}

	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent me) {
		
		if (this.isPasteAction==true) {
			
			if (this.networkModel2Paste==null) {
				// --------------------------------------------------
				// --- Integrate the clip board NetworkModel --------
				// --------------------------------------------------
				this.networkModel2Paste = this.graphController.getClipboardNetworkModel().getCopy();
				this.graphController.getNetworkModel().adjustNameDefinitionsOfSupplementNetworkModel(this.networkModel2Paste);
				this.graphController.getNetworkModel().mergeNetworkModel(this.networkModel2Paste, null, false);
				
				// --- Inform about changes -------------------------
				this.graphController.notifyObservers(new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Merged_With_Supplement_NetworkModel));
				this.graphController.setProjectUnsaved();
				
				// --- Mark all GraphElements as selected -----------
				List<NetworkComponent> netCompList = new ArrayList<>(this.networkModel2Paste.getNetworkComponents().values());
				for (int n = 0; n < netCompList.size(); n++) {

					NetworkComponent networkComponent2Paste = netCompList.get(n);
					NetworkComponent networkComponentPasted = this.graphController.getNetworkModel().getNetworkComponent(networkComponent2Paste.getId());
					this.graphController.addAgent(networkComponentPasted);
					
					Vector<GraphElement> elements = this.networkModel2Paste.getGraphElementsFromNetworkComponent(networkComponentPasted);
					for (int i = 0; i < elements.size(); i++) {
						GraphElement graphElement = elements.get(i);
						if (graphElement instanceof GraphEdge) {
							// --- Pick edge ------------------------
							this.getVisViewer().getPickedEdgeState().pick((GraphEdge) graphElement, true);
						} else if (graphElement instanceof GraphNode) {
							// --- Pick node ------------------------
							GraphNode graphNodeCurrent = (GraphNode) graphElement;
							this.getVisViewer().getPickedVertexState().pick(graphNodeCurrent, true);
							
							// --------------------------------------
							// --- Remind the left upper GraphNode --
							if (this.graphNodeUpperLeft2Paste==null) {
								this.graphNodeUpperLeft2Paste = graphNodeCurrent;
							} else {
								// --- Consider coordinate system ---
								Point2D positionOfGraphNodeAtLeftUpperPosition = this.getCoordinateSystemNodePositionTransformer().transform(this.graphNodeUpperLeft2Paste.getPosition());
								Point2D positionOfGraphNodeCurrent = this.getCoordinateSystemNodePositionTransformer().transform(graphNodeCurrent.getPosition()); 
								if (positionOfGraphNodeCurrent.getX()<positionOfGraphNodeAtLeftUpperPosition.getX()) {
									this.graphNodeUpperLeft2Paste = graphNodeCurrent;
								} else if (positionOfGraphNodeCurrent.getX()==positionOfGraphNodeAtLeftUpperPosition.getX()) {
									if (positionOfGraphNodeCurrent.getY()<positionOfGraphNodeAtLeftUpperPosition.getY()) {
										this.graphNodeUpperLeft2Paste = graphNodeCurrent;
									}
								}
							}
							// -------------------------------------------
						}
					}
				}
			}
			
			// ------------------------------------------------------
			// --- React on the Mouse movement ---------------------- 
			// ------------------------------------------------------
			Point2D mousePositionLayout = this.getVisViewer().getRenderContext().getMultiLayerTransformer().inverseTransform(me.getPoint());
			
			// --- Calculate node movement --------------------------
			Point2D ulGraphNodeLayout = this.getCoordinateSystemNodePositionTransformer().transform(this.graphNodeUpperLeft2Paste.getPosition());
			double shiftXLayout = mousePositionLayout.getX() - ulGraphNodeLayout.getX();
			double shiftYLayout = mousePositionLayout.getY() - ulGraphNodeLayout.getY();
			
			// --- Create reminder array for nodes to move ---------- 
			if (this.graphNodes2Paste==null) {
				PickedState<GraphNode> ps = this.getVisViewer().getPickedVertexState();
				this.graphNodes2Paste = new GraphNode[ps.getPicked().size()];
				ps.getPicked().toArray(this.graphNodes2Paste);
			}
			
			// --- Adjust position ----------------------------------
			Layout<GraphNode,GraphEdge> layout = this.getVisViewer().getGraphLayout();
			for (int i = 0; i < this.graphNodes2Paste.length; i++) {
				// --- Set location in Layout -----------------------
				Point2D locLayout = layout.transform(graphNodes2Paste[i]);
                locLayout.setLocation(locLayout.getX()+shiftXLayout, locLayout.getY()+shiftYLayout);
                layout.setLocation(graphNodes2Paste[i], locLayout);
                // --- Set coordinate system location ---------------
                graphNodes2Paste[i].setPosition(this.getCoordinateSystemNodePositionTransformer().inverseTransform(locLayout));
			}
            this.getVisViewer().repaint();
            me.consume();
            
		}
	}
	
	/**
	 * Sets the paste cursor.
	 *
	 * @param isDoPaste the do paste
	 * @param isFinalizePasteAction the finalize paste action
	 */
	private void setPasteAction(boolean isDoPaste, boolean isFinalizePasteAction) {
		
		// --- Are we in picking mode? ------------------------------
		if (this.basicGraphGUI.getGraphMouseMode()==GraphMouseMode.Picking) {
			
			this.isPasteAction = isDoPaste;
			if (this.isPasteAction==true) {
				// --------------------------------------------------
				// --- Initiate paste action ------------------------
				// --------------------------------------------------
				ImageIcon cursorImage = GraphGlobals.getImageIcon("Paste.png");
				Cursor cursorForPaste = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage.getImage(), new Point(0, 0), "Paste");
				this.setCursor(cursorForPaste);
				
			} else {
				// --------------------------------------------------
				// --- Finalize paste action ------------------------
				// --------------------------------------------------
				if (isFinalizePasteAction==true) {
					// --- Create an undoable action ----------------
					this.graphController.getNetworkModelUndoManager().pasteNetworkModel(this.networkModel2Paste);
					
				} else {
					// --- Remove the added components --------------
					List<NetworkComponent> netComps2Remove = new ArrayList<>();
					for (NetworkComponent networkComponentPasted : this.networkModel2Paste.getNetworkComponents().values()) {
						String netCompID = networkComponentPasted.getId();
						NetworkComponent netCompRemove = this.graphController.getNetworkModel().getNetworkComponent(netCompID);
						netComps2Remove.add(netCompRemove);
						this.graphController.removeAgent(networkComponentPasted);
					}
					
					this.graphController.getNetworkModel().removeNetworkComponents(netComps2Remove);
					
					NetworkModelNotification notification = new NetworkModelNotification(NetworkModelNotification.NETWORK_MODEL_Component_Removed);
					notification.setInfoObject(netComps2Remove);
					this.graphController.notifyObservers(notification);
				}
				
				this.setCursor(new Cursor(Cursor.HAND_CURSOR));
				
				this.networkModel2Paste = null;
				this.graphNodeUpperLeft2Paste = null;
				this.graphNodes2Paste = null;
			}
			
			this.getVisViewer().setCursor(this.getCursor());
		}
		
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object object) {
		
		if (object instanceof NetworkModelNotification) {
			NetworkModelNotification nmNotification = (NetworkModelNotification) object;
			switch (nmNotification.getReason()) {
			case NetworkModelNotification.NETWORK_MODEL_Paste_Action_Do:
				this.setPasteAction(true, false);
				break;
			case NetworkModelNotification.NETWORK_MODEL_Paste_Action_Stop:
				this.setPasteAction(false, false);
				break;
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent ke) {
		if (isPasteAction==true) {
			this.setPasteAction(false, false);
		}
	}
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent ke) { }
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent ke) { }

	
}
