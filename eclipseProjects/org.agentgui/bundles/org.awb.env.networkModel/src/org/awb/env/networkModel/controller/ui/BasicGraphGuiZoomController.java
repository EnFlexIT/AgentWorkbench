package org.awb.env.networkModel.controller.ui;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyVetoException;
import java.util.List;
import java.util.Set;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphGlobals;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.controller.GraphEnvironmentController;

import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;

/**
 * The Class BasicGraphGuiScalingControl contains the JUNG {@link ScalingControl} 
 * that is to be used in the current context (e.g. for Map integration).
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class BasicGraphGuiZoomController implements ZoomController {

	public static final float SCALE_FACTOR_IN = 1.1f;
	public static final float SCALE_FACTOR_OUT = 1.0f / 1.1f;
	
	private double graphMargin = BasicGraphGui.graphMargin;
	private Point2D defaultScaleAtPoint = new Point2D.Double(graphMargin, graphMargin);
	
	private GraphEnvironmentController graphController;
	private BasicGraphGuiVisViewer<GraphNode, GraphEdge> visViewer;
	private ScalingControl scalingControl;
	
	
	/**
	 * Instantiates a new BasicGraphGuiScalingControl.
	 *
	 * @param graphController the graph controller
	 * @param visViewer the vis viewer
	 */
	public BasicGraphGuiZoomController(GraphEnvironmentController graphController, BasicGraphGuiVisViewer<GraphNode, GraphEdge> visViewer) {
		this.setGraphEnvironmentController(graphController);
		this.setVisualizationViewer(visViewer);
	}
	
	/**
	 * Return the graph environment controller.
	 * @return the graph environment controller
	 */
	public GraphEnvironmentController getGraphEnvironmentController() {
		return graphController;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.ZoomController#setGraphEnvironmentController(org.awb.env.networkModel.controller.GraphEnvironmentController)
	 */
	@Override
	public void setGraphEnvironmentController(GraphEnvironmentController graphController) {
		this.graphController = graphController;
	}
	
	/**
	 * Returns the current visualization viewer.
	 * @return the visualization viewer
	 */
	protected BasicGraphGuiVisViewer<GraphNode, GraphEdge> getVisualizationViewer() {
		return this.visViewer;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.ZoomController#setVisualizationViewer(org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer)
	 */
	@Override
	public void setVisualizationViewer(BasicGraphGuiVisViewer<GraphNode, GraphEdge> visViewer) {
		this.visViewer = visViewer;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.ZoomController#getScalingControl()
	 */
	@Override
	public ScalingControl getScalingControl() {
		if (scalingControl==null) {
			CrossoverScalingControl crossoverScalingControl = new CrossoverScalingControl();
			crossoverScalingControl.setCrossover(1.0);
			scalingControl = crossoverScalingControl;
		}
		return scalingControl;
	}
	
	
	/**
	 * Gets the default point to scale at for zooming.
	 * @return the default scale at point
	 */
	private Point2D getDefaultScaleAtPoint() {
		Rectangle2D rectVis = this.getVisualizationViewer().getVisibleRect();
		if (rectVis.isEmpty()==false) {
			this.defaultScaleAtPoint = new Point2D.Double(rectVis.getCenterX(), rectVis.getCenterY());
		}
		return defaultScaleAtPoint;
	}
	/**
	 * Sets the default point to scale at for zooming..
	 * @param scalePoint the new default scale at point
	 */
	private void setDefaultScaleAtPoint(Point2D scalePoint) {
		defaultScaleAtPoint = scalePoint;
	}
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.ZoomController#zoomIn()
	 */
	@Override
	public void zoomIn() {
		this.zoomIn(this.getDefaultScaleAtPoint());
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.ZoomController#zoomIn(java.awt.geom.Point2D)
	 */
	@Override
	public void zoomIn(Point2D zoomAtPoint) {
		this.zoom(SCALE_FACTOR_IN, zoomAtPoint);
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.ZoomController#zoomOut()
	 */
	@Override
	public void zoomOut() {
		this.zoomOut(this.getDefaultScaleAtPoint());
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.ZoomController#zoomOut(java.awt.geom.Point2D)
	 */
	@Override
	public void zoomOut(Point2D zoomAtPoint) {
		this.zoom(SCALE_FACTOR_OUT, zoomAtPoint);
	}
	/**
	 * Private zoom method that accumulates all possible calls to the scaling control.
	 *
	 * @param scaleAmount the scale amount
	 * @param zoomAtPoint the zoom at point
	 */
	private void zoom(float scaleAmount, Point2D zoomAtPoint) {
		
		// --- Set selected frame to the parent internal frame ------
		BasicGraphGuiRootJSplitPane parentInternalFrame = this.getGraphEnvironmentController().getGraphEnvironmentControllerGUI().getBasicGraphGuiRootJSplitPane();
		if (parentInternalFrame.isSelected()==false) {
			try {
				parentInternalFrame.setSelected(true);
			} catch (PropertyVetoException pvEx) {
				pvEx.printStackTrace();
			}
		}
		
		// --- Scale the graph to the scale amount ------------------
		this.getScalingControl().scale(this.getVisualizationViewer(), scaleAmount, zoomAtPoint);	
	}
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.ZoomController#zoomOneToOneMoveFocus()
	 */
	@Override
	public void zoomOneToOneMoveFocus() {
		this.setVisualizationViewerToFitToWindow(this.getVisualizationViewer(), false);
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.ZoomController#zoomToFitToWindow()
	 */
	@Override
	public void zoomToFitToWindow() {
		this.zoomToFitToWindow(this.getVisualizationViewer());
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.ZoomController#zoomToFitToWindow(edu.uci.ics.jung.visualization.VisualizationViewer)
	 */
	@Override
	public void zoomToFitToWindow(VisualizationViewer<GraphNode, GraphEdge> visViewer) {
		this.setVisualizationViewerToFitToWindow(visViewer, true);
	}
	
	/**
	 * Sets the visualization viewer to fit to window.
	 *
	 * @param visViewer the vis viewer
	 * @param scaleAtCoordinateSource the scale at coordinate source
	 */
	private void setVisualizationViewerToFitToWindow(VisualizationViewer<GraphNode, GraphEdge> visViewer, boolean scaleAtCoordinateSource) {
		
		if (visViewer.getVisibleRect().isEmpty()) return;

		// ----------------------------------------------------------
		// --- Get coordinate systems position ----------------------
		Point2D coordinateSourcePoint = CoordinateSystemSourcePosition.getCoordinateSystemSourcePointInVisualizationViewer(visViewer, this.getGraphEnvironmentController().getNetworkModel().getLayoutSettings());
		this.setDefaultScaleAtPoint(coordinateSourcePoint);
		
		// ----------------------------------------------------------
		// --- Reset view and layout to identity -------------------- 
		visViewer.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).setToIdentity();
		visViewer.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).setToIdentity();
		
		// ----------------------------------------------------------
		// --- Calculate the movement in the view -------------------
		Rectangle2D graphRectangle = GraphGlobals.getGraphSpreadDimension(visViewer.getGraphLayout().getGraph());
		double moveX = (graphRectangle.getX() * (-1)) + this.graphMargin;
		double moveY = (graphRectangle.getY() * (-1)) + this.graphMargin;

		// --- Transform coordinate to LayoutSettings ---------------
		TransformerForGraphNodePosition<GraphNode, GraphEdge> positionTransformer = new TransformerForGraphNodePosition<>(this.getGraphEnvironmentController());
		Point2D visualPosition = positionTransformer.transform(new Point2D.Double(moveX, moveY));
		moveX = visualPosition.getX() + coordinateSourcePoint.getX();
		moveY = visualPosition.getY() + coordinateSourcePoint.getY();
		
		// --- Set focus movement -----------------------------------
		visViewer.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).translate(moveX, moveY);
		if (scaleAtCoordinateSource==false) return;

		
		// ----------------------------------------------------------		
		// --- Calculate the scaling --------------------------------
		double graphWidth = graphRectangle.getWidth() + 2 * this.graphMargin;
		double graphHeight = graphRectangle.getHeight() + 2 * this.graphMargin;
		Point2D farthestCorner = positionTransformer.transform(new Point2D.Double(graphWidth, graphHeight));
		graphWidth = Math.abs(farthestCorner.getX());
		graphHeight = Math.abs(farthestCorner.getY());
		
		double visWidth = visViewer.getVisibleRect().getWidth();
		double visHeight = visViewer.getVisibleRect().getHeight();

		double scaleX = visWidth / graphWidth;
		double scaleY = visHeight / graphHeight;
		
		// --- Set scaling ------------------------------------------
		double scale = Math.min(scaleX, scaleY);;
		if (scale!= 0 && scale!=1) {
			this.getScalingControl().scale(visViewer, (float) scale, coordinateSourcePoint);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.ZoomController#zoomToComponent()
	 */
	@Override
	public void zoomToComponent() {
		
		Set<GraphNode> nodesPicked = this.getVisualizationViewer().getPickedVertexState().getPicked();
		if (nodesPicked.size()!=0) {
			List<NetworkComponent> components = this.getGraphEnvironmentController().getNetworkModel().getNetworkComponentsFullySelected(nodesPicked);
			if (components!=null && components.size()!=0) {
				// --- Get the dimension of the selected nodes ------ 
				Rectangle2D areaSelected = GraphGlobals.getGraphSpreadDimension(nodesPicked);
				Point2D areaCenter = new Point2D.Double(areaSelected.getCenterX(), areaSelected.getCenterY());
				// --- Create temporary GraphNode -------------------
				GraphNode tmpNode = new GraphNode("tmPCenter", areaCenter);
				this.getVisualizationViewer().getGraphLayout().getGraph().addVertex(tmpNode);
				// --- Get the needed positions ---------------------
				Point2D tmpNodePos = this.getVisualizationViewer().getGraphLayout().transform(tmpNode);
				Point2D visViewCenter = this.getVisualizationViewer().getRenderContext().getMultiLayerTransformer().inverseTransform(this.getVisualizationViewer().getCenter());
				// --- Calculate movement ---------------------------
				final double dx = (visViewCenter.getX() - tmpNodePos.getX());
				final double dy = (visViewCenter.getY() - tmpNodePos.getY());
				// --- Remove temporary GraphNode and move view -----
				this.getVisualizationViewer().getGraphLayout().getGraph().removeVertex(tmpNode);
				this.getVisualizationViewer().getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).translate(dx, dy); 
			}
		}
	}
	
	
}
