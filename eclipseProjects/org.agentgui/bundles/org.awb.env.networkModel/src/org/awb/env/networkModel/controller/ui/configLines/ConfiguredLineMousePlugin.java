package org.awb.env.networkModel.controller.ui.configLines;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;

import javax.swing.SwingUtilities;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphElementLayout;
import org.awb.env.networkModel.GraphNode;
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
 * Handling mouse interaction with graph visualizations in the {@link BasicGraphGui} during line configurations.
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
	
	private boolean movePanelWithRightAction;
	private boolean moveNodeWithLeftAction;

	private Vector<GraphNode> nodesTemp = new Vector<GraphNode>();
	private GraphNode graphNodeMoved;
	
	private boolean zoomAtMouse = true;
    private ScalingControl scaler = new CrossoverScalingControl();
	protected float in = 1.1f;
	protected float out = 1/1.1f;
	
	private ConfiguredLineEdit confLineEdit;	
	
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
		if (this.graphNodeMoved!=null) {
			this.getVisViewer().getGraphLayout().setLocation(this.graphNodeMoved, this.getGraphNodePositionTransformer().transform(this.graphNodeMoved.getPosition()));
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
	 * Creates the undoable move action.
	 */
	private void createUndoableEditAction() {
		
		
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
		
		if (SwingUtilities.isRightMouseButton(me) && pickedNode==null && pickedEdge==null){		
			this.movePanelWithRightAction = true;
			this.getVisViewer().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			
		} else if (SwingUtilities.isLeftMouseButton(me) && this.isAllowedGraphNodeForMoving(pickedNode)==true) {
			this.moveNodeWithLeftAction = true;
			this.graphNodeMoved = pickedNode;
			this.setOppositeGraphNodeMovedPicked(false);
		}
		
		if (this.movePanelWithRightAction==true || this.moveNodeWithLeftAction==true) {
			super.mousePressed(me);
		}
	}
	/**
	 * Checks if the specified graph node is allowed for moving.
	 *
	 * @param graphNodeSelected the graph node
	 * @return true, if is allowed graph node for moving
	 */
	private boolean isAllowedGraphNodeForMoving(GraphNode graphNodeSelected) {
		
		boolean isAllowedMoving = false;
		if (graphNodeSelected!=null) {
			boolean isGraphNodeStart = graphNodeSelected.getId().equals(this.confLineEdit.getGraphNodeOldFrom().getId()); 
			boolean isGraphNodeEnd   = graphNodeSelected.getId().equals(this.confLineEdit.getGraphNodeOldTo().getId()); 
			boolean isGraphNodeIntermediate = false; // TODO
			if (isGraphNodeStart==true || isGraphNodeEnd==true || isGraphNodeIntermediate==true) {
				isAllowedMoving = true;
			}
		}
		return isAllowedMoving;
	}
	/**
	 * Sets the opposite GraphNode that is moved picked or not.
	 * @param iPicked the new opposite graph node moved picked
	 */
	private void setOppositeGraphNodeMovedPicked(boolean iPicked) {
		GraphNode graphNodeChanged = this.getOppositeNode(this.graphNodeMoved);
		this.getVisViewer().getPickedVertexState().pick(graphNodeChanged, iPicked);
	}
	/**
	 * Returns the opposite node with respect to the current GraphEdge.
	 *
	 * @param graphNode the graph node
	 * @return the opposite node
	 */
	private GraphNode getOppositeNode(GraphNode graphNode) {
		GraphEdge edgePicked = this.getVisViewer().getPickedEdgeState().getPicked().iterator().next();
		return this.getGraphController().getNetworkModel().getGraph().getOpposite(graphNode, edgePicked);
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
            } catch (RuntimeException ex) {
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
			for (GraphNode pickedNode: pickedNodes) {

				// --- Get the Graph, if not already there --------------------
				if (graph==null) {
					graph = this.getGraphController().getNetworkModel().getGraph();
					this.removeAllTemporaryNodes(graph);
				}
				
				// --- Get the position of the node ---------------------------
				Point2D newPos = this.getVisViewer().getGraphLayout().transform(pickedNode);
				newPos = this.getGraphNodePositionTransformer().inverseTransform(newPos);
				if (snapToGrid==true && snapRaster>0) {
					double xPos = roundGridSnap(newPos.getX(), snapRaster); 
					double yPos = roundGridSnap(newPos.getY(), snapRaster);
					newPos.setLocation(xPos, yPos);
					
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
				this.setOppositeGraphNodeMovedPicked(true);
//				this.createUndoableMoveAction();
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
			case NetworkModelNotification.NETWORK_MODEL_GraphMouse_EdgeEditing:
				// --- Remind setting of 
				Object infoObject = nmNotification.getInfoObject();
				if (infoObject!=null && infoObject instanceof ConfiguredLineEdit) {
					this.confLineEdit = (ConfiguredLineEdit) nmNotification.getInfoObject();
				}
				break;
			}
		}
		
	}
	
}
