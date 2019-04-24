package org.awb.env.networkModel.controller.ui.configLines;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;

import javax.swing.SwingUtilities;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphEdgeShapeConfiguration;
import org.awb.env.networkModel.GraphElementLayout;
import org.awb.env.networkModel.GraphNode;
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
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
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
	
	private static final String INTERMEDIATE_GRAPH_NODE_ID_PREFIX = "IGN_";
	
	private GraphEnvironmentController graphController;
	private BasicGraphGui basicGraphGUI;
	private BasicGraphGuiVisViewer<GraphNode, GraphEdge> visViewer; 	
	private TransformerForGraphNodePosition<GraphNode, GraphEdge> graphNodePositionTransformer;
	private IntermediatePointTransformer intermediatePointTransformer;
	
	private boolean movePanelWithRightAction;
	private boolean moveNodeWithLeftAction;

	private Vector<GraphNode> nodesTemp = new Vector<GraphNode>();
	private GraphNode graphNodeMoved;
	
	private ConfiguredLineEdit confLineEdit;
	private GraphNode graphNodeStart;
	private GraphNode graphNodeEnd;
	private GraphEdge editingGraphEdge;
	private GraphEdgeShapeConfiguration<? extends Shape> shapeConfiguration;
	
	private List<GraphNode> intGraphNodes;
	
	
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
	 * Returnss the intermediate point transformer.
	 * @return the intermediate point transformer
	 */
	private IntermediatePointTransformer getIntermediatePointTransformer() {
		if (intermediatePointTransformer==null) {
			intermediatePointTransformer = new IntermediatePointTransformer();
		}
		return intermediatePointTransformer;
	}
	
	
	/**
	 * Returns the GraphEdge that is currently edited.
	 * @return the editing graph edge
	 */
	private GraphEdge getEditingGraphEdge() {
		if (editingGraphEdge==null) {
			String graphEdgeID = this.confLineEdit.getGraphEdgeOld().getId();
			editingGraphEdge = (GraphEdge) this.getGraphController().getNetworkModel().getGraphElement(graphEdgeID);
		}
		return editingGraphEdge;
	}
	/**
	 * Returns the current shape configuration.
	 * @return the shape configuration
	 */
	private GraphEdgeShapeConfiguration<? extends Shape> getShapeConfiguration() {
		if (shapeConfiguration==null) {
			shapeConfiguration = this.getEditingGraphEdge().getEdgeShapeConfiguration();  
		}
		return shapeConfiguration;
	}
	/**
	 * Returns the start graph node .
	 * @return the graph node start
	 */
	private GraphNode getGraphNodeStart() {
		if (graphNodeStart==null) {
			this.setStartAndEndNode();
		}
		return graphNodeStart;
	}
	/**
	 * Returns the end graph node.
	 * @return the graph node end
	 */
	private GraphNode getGraphNodeEnd() {
		if (graphNodeEnd==null) {
			this.setStartAndEndNode();
		}
		return graphNodeEnd;
	}
	/**
	 * Sets the start and end node.
	 */
	private void setStartAndEndNode() {
		
		Graph<GraphNode, GraphEdge> graph = this.getGraphController().getNetworkModel().getGraph();
		if (graph.getEdgeType(this.getEditingGraphEdge())==EdgeType.DIRECTED) {
			// --- We're editing a directed graph edge ----
			this.graphNodeStart = graph.getSource(this.getEditingGraphEdge());
			this.graphNodeEnd   = graph.getDest(this.getEditingGraphEdge());
		} else {
			// --- We're editing an undirected graph edge -
			Pair<GraphNode> graphNodePair = graph.getEndpoints(this.getEditingGraphEdge());
			this.graphNodeStart = graphNodePair.getFirst();
			this.graphNodeEnd   = graphNodePair.getSecond();
		}
	}
	/**
	 * Resets the editing graph elements.
	 */
	private void resetEditingGraphElements() {
		this.graphNodeStart = null;
		this.graphNodeEnd = null;
		this.editingGraphEdge = null;
		this.shapeConfiguration = null;
	}
	/**
	 * Returns the current intermediate graph nodes.
	 * @return the intermediate graph nodes
	 */
	private List<GraphNode> getIntermediateGraphNodes() {
		if (intGraphNodes==null) {
			intGraphNodes = new ArrayList<>();
		}
		return intGraphNodes;
	}
	/**
	 * Adds the intermediate nodes.
	 */
	private void addIntermediateNodes() {
		
		// --- Remove the intermediate nodes first --------
		this.removeIntermediateNodes();
		
		NetworkModel networkModel = this.getGraphController().getNetworkModel();
		GraphElementLayout layoutStartNode = this.getGraphNodeStart().getGraphElementLayout(networkModel);
		// --- Get the intermediate points ----------------
		List<Point2D> intermediatePointList = this.getShapeConfiguration().getIntermediatePoints();
		if (intermediatePointList!=null) {
			for (int i = 0; i < intermediatePointList.size(); i++) {
				
				// --- Get each intermediate point ------------
				Point2D intPoint = intermediatePointList.get(i);
				// --- Transform the position to graph coordinates 
				Point2D graphPoint = this.getIntermediatePointTransformer().transformToGraphCoordinate(intPoint, this.getGraphNodeStart(), this.getGraphNodeEnd());
				
				// --- Create GraphNode -------------
				GraphNode intNode = new GraphNode(INTERMEDIATE_GRAPH_NODE_ID_PREFIX + i, graphPoint);
				
				GraphElementLayout layoutTmpNode = intNode.getGraphElementLayout(networkModel);
				layoutTmpNode.setShowLabel(false);
				layoutTmpNode.setSize(layoutStartNode.getSize());
				layoutTmpNode.setShapeForm(layoutStartNode.getShapeForm());
				
				networkModel.getGraph().addVertex(intNode);
				this.getIntermediateGraphNodes().add(intNode);
				
			}
			this.getVisViewer().repaint();
		}
	}
	/**
	 * Removes the intermediate nodes of the current editing GraphEdge.
	 */
	private void removeIntermediateNodes() {
		for (int i = 0; i < this.getIntermediateGraphNodes().size(); i++) {
			this.getGraphController().getNetworkModel().getGraph().removeVertex(this.getIntermediateGraphNodes().get(i));
		}
		this.getIntermediateGraphNodes().clear();
		this.getVisViewer().repaint();
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
		
		if (this.confLineEdit==null) return;
		
		// --- Clean-up intermediate nodes first ---------- 
		this.removeIntermediateNodes();
	
		// --- Save the new settings ConfiguredLineEdit --- 
		this.confLineEdit.setGraphEdgeNew(this.getEditingGraphEdge().getCopy());
		this.confLineEdit.setGraphNodeNewFrom(this.getGraphNodeStart().getCopy());
		this.confLineEdit.setGraphNodeNewTo(this.getGraphNodeEnd().getCopy());

		this.basicGraphGUI.getGraphEnvironmentController().getNetworkModelUndoManager().setConfiguredLineEdit(this.getVisViewer(), this.confLineEdit);
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
			boolean isGraphNodeIntermediate = graphNodeSelected.getId().startsWith(INTERMEDIATE_GRAPH_NODE_ID_PREFIX);
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
		if (graphNodeChanged!=null) this.getVisViewer().getPickedVertexState().pick(graphNodeChanged, iPicked);
	}
	/**
	 * Returns the opposite node with respect to the current GraphEdge.
	 *
	 * @param graphNode the graph node
	 * @return the opposite node
	 */
	private GraphNode getOppositeNode(GraphNode graphNode) {
		Collection<GraphEdge> incidentEdges = this.getGraphController().getNetworkModel().getGraph().getIncidentEdges(graphNode); 
		if (incidentEdges!=null && incidentEdges.size()>0) {
			GraphEdge edgePicked = this.getVisViewer().getPickedEdgeState().getPicked().iterator().next();
			return this.getGraphController().getNetworkModel().getGraph().getOpposite(graphNode, edgePicked);
		}
		return null;
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
			
			Graph<GraphNode, GraphEdge> graph = this.getGraphController().getNetworkModel().getGraph();
			this.removeAllTemporaryNodes(graph);
			
			LayoutSettings layoutSettings = this.getGraphController().getNetworkModel().getLayoutSettings();
			boolean snapToGrid = layoutSettings.isSnap2Grid();
			double snapRaster = layoutSettings.getSnapRaster();
			
			Set<GraphNode> pickedNodes = this.getVisViewer().getPickedVertexState().getPicked();
			for (GraphNode pickedNode: pickedNodes) {
				// --- Get the position of the node ---------------------------
				Point2D newPos = this.getVisViewer().getGraphLayout().transform(pickedNode);
				newPos = this.getGraphNodePositionTransformer().inverseTransform(newPos);
				if (snapToGrid==true && snapRaster>0) {
					double xPos = roundGridSnap(newPos.getX(), snapRaster); 
					double yPos = roundGridSnap(newPos.getY(), snapRaster);
					newPos.setLocation(xPos, yPos);
					
					this.addTemporaryNode(graph, pickedNode, newPos);
				}
				
				// --- What do we move? ---------------------------------------
				if (this.getIntermediateGraphNodes().contains(pickedNode)==true) {
					// --- Moving an intermediate node ------------------------
					List<Point2D> intGraphNodePositions = new ArrayList<>();
					for (int i = 0; i < this.getIntermediateGraphNodes().size(); i++) {
						GraphNode intGraphNode = this.getIntermediateGraphNodes().get(i);
						Point2D intCoordPosition = this.getIntermediatePointTransformer().transformToIntermediateCoordinate(intGraphNode.getPosition(), this.getGraphNodeStart(), this.getGraphNodeEnd());
						intGraphNodePositions.add(intCoordPosition);
					}
					this.getShapeConfiguration().setIntermediatePoints(intGraphNodePositions);
					
				} else {
					// --- Moving an outer node -------------------------------
					List<Point2D> pointList = this.getShapeConfiguration().getIntermediatePoints();
					for (int i = 0; i < this.getIntermediateGraphNodes().size(); i++) {
						GraphNode intGraphNode = this.getIntermediateGraphNodes().get(i);
						Point2D newIntNodePosition = this.getIntermediatePointTransformer().transformToGraphCoordinate(pointList.get(i), this.getGraphNodeStart(), this.getGraphNodeEnd());
						intGraphNode.setPosition(newIntNodePosition);
						this.getVisViewer().getGraphLayout().setLocation(intGraphNode, this.getGraphNodePositionTransformer().transform(newIntNodePosition));
					}
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
            if (vertex!=null) {
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
			} 
			
		}
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent me) {

		Point2D mouse = me.getPoint();
		if(me.getWheelRotation()>0) {
			this.basicGraphGUI.getScalingControl().scale(this.getVisViewer(), BasicGraphGui.SCALE_FACTOR_OUT, mouse);
		} else {
			this.basicGraphGUI.getScalingControl().scale(this.getVisViewer(), BasicGraphGui.SCALE_FACTOR_IN, mouse);
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
				// --- Get reminder for initial setting ------------- 
				Object infoObject = nmNotification.getInfoObject();
				if (infoObject!=null && infoObject instanceof ConfiguredLineEdit) {
					// --- Finalize edit first ---------------------- 
					if (this.confLineEdit!=null) {
						this.createUndoableEditAction();
					}
					// --- Set new current edit description ---------
					this.confLineEdit = (ConfiguredLineEdit) nmNotification.getInfoObject();
					this.resetEditingGraphElements();
					// --- Set intermediate nodes -------------------
					this.addIntermediateNodes();
				}
				break;
			
			case NetworkModelNotification.NETWORK_MODEL_GraphMouse_Picking:
				// --- Edit finished, finalize edit description -----
				this.createUndoableEditAction();	
				break;
			}
		}
	}
	
}
