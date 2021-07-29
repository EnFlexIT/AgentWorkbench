package org.awb.env.maps;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import org.awb.env.maps.OSMZoomLevels.ZoomLevel;
import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiVisViewer;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController;
import org.awb.env.networkModel.controller.ui.TransformerForGraphNodePosition;
import org.awb.env.networkModel.controller.ui.ZoomController;

import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * The OSMZoomController.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class OSMZoomController extends BasicGraphGuiZoomController implements ZoomController {

	private BaseMapService baseMapService;
	
	private GraphEnvironmentController graphController;
	private BasicGraphGuiVisViewer<GraphNode, GraphEdge> visViewer;

	private OSMScalingControl scalingControl;
	private ZoomLevel zoomLevel;
	
	/**
	 * Instantiates a new OSM zoom controller.
	 * @param baseMapService the base map service
	 */
	public OSMZoomController(BaseMapService baseMapService) {
		this.baseMapService = baseMapService;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#setGraphEnvironmentController(org.awb.env.networkModel.controller.GraphEnvironmentController)
	 */
	@Override
	public void setGraphEnvironmentController(GraphEnvironmentController graphController) {
		this.graphController = graphController;
	}
	/**
	 * Gets the graph environment controller.
	 * @return the graphController
	 */
	public GraphEnvironmentController getGraphEnvironmentController() {
		return graphController;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#setVisualizationViewer(edu.uci.ics.jung.visualization.VisualizationViewer)
	 */
	@Override
	public void setVisualizationViewer(BasicGraphGuiVisViewer<GraphNode, GraphEdge> visViewer) {
		this.visViewer = visViewer;
	}
	/**
	 * Returns the visualization viewer.
	 * @return the visViewer
	 */
	public BasicGraphGuiVisViewer<GraphNode, GraphEdge> getVisualizationViewer() {
		return visViewer;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#getScalingControl()
	 */
	@Override
	public OSMScalingControl getScalingControl() {
		if (this.scalingControl == null) {
			OSMScalingControl crossoverScalingControl = new OSMScalingControl();
			crossoverScalingControl.setCrossover(1.0);
			this.scalingControl = crossoverScalingControl;
		}
		return scalingControl;
	}
	
	/**
	 * Returns the current zoom level of the {@link OSMScalingControl}.
	 * @return the zoom level
	 */
	public ZoomLevel getZoomLevel() {
		return this.zoomLevel;
	}
	/**
	 * Sets the zoom level.
	 * @param newZoomLevel the new zoom level
	 */
	public void setZoomLevel(ZoomLevel zoomLevel) {
		this.setZoomLevel(zoomLevel, null);
	}
	/**
	 * Sets the zoom level for the specified position on screen.
	 *
	 * @param newZoomLevel the new zoom level
	 * @param scalePointOnScreen the scale point on screen
	 */
	public void setZoomLevel(ZoomLevel newZoomLevel, Point2D scalePointOnScreen) {
		if (this.zoomLevel==null || newZoomLevel.equals(this.zoomLevel)==false) {
			this.zoomLevel = newZoomLevel;
			if (this.getVisualizationViewer()!=null) {
				if (scalePointOnScreen==null) scalePointOnScreen = this.getVisualizationViewerCenter();
				this.setVisualizationViewerSelected();
				this.getScalingControl().scale(this.getVisualizationViewer(), this.zoomLevel, scalePointOnScreen);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomIn()
	 */
	@Override
	public void zoomIn() {
		this.zoomIn(this.getVisualizationViewerCenter());
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomIn(java.awt.geom.Point2D)
	 */
	@Override
	public void zoomIn(Point2D pointOnScreen) {
		double currLatitude = this.baseMapService.getMapRenderer().getCenterGeoCoordinate().getLatitude();
		this.setZoomLevel(OSMZoomLevels.getInstance().getNextZoomLevel(this.getZoomLevel(), 1, currLatitude), pointOnScreen);
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomOut()
	 */
	@Override
	public void zoomOut() {
		this.zoomOut(this.getVisualizationViewerCenter());
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomOut(java.awt.geom.Point2D)
	 */
	@Override
	public void zoomOut(Point2D pointOnScreen) {
		double currLatitude = this.baseMapService.getMapRenderer().getCenterGeoCoordinate().getLatitude();
		this.setZoomLevel(OSMZoomLevels.getInstance().getNextZoomLevel(this.getZoomLevel(), -1, currLatitude), pointOnScreen);
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomOneToOneMoveFocus()
	 */
	@Override
	public void zoomOneToOneMoveFocus() {
		super.zoomOneToOneMoveFocus();
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomToFitToWindow()
	 */
	@Override
	public void zoomToFitToWindow() {
		super.zoomToFitToWindow();
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomToFitToWindow(edu.uci.ics.jung.visualization.VisualizationViewer)
	 */
	@Override
	public void zoomToFitToWindow(VisualizationViewer<GraphNode, GraphEdge> visViewer) {
		super.zoomToFitToWindow(visViewer);
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.controller.ui.BasicGraphGuiZoomController#zoomToComponent()
	 */
	@Override
	public void zoomToComponent() {
		super.zoomToComponent();
	}

	
	/**
	 * Return the specified point on screen to a point in graph coordinates.
	 *
	 * @param pointOnScreen the point on the screen (e.g. the mouse position or the center of the screen)
	 * @return the point in graph coordinates
	 */
	public Point2D getPointInGraphCoordinates(Point2D pointOnScreen) {
		Point2D pointGraph = null;
		try {
			Point2D pointJung = this.getAffineTransform().inverseTransform(pointOnScreen, null);
			TransformerForGraphNodePosition cspTransformer = this.getBasicGraphGui().getCoordinateSystemPositionTransformer();
			pointGraph = cspTransformer.inverseTransform(pointJung);
			
		} catch (NoninvertibleTransformException ex) {
			ex.printStackTrace();
		}
		return pointGraph;
	}
	/**
	 * Return the affine transform from the current visualization viewer.
	 *
	 * @param visViewer the vis viewer
	 * @return the affine transform
	 */
	private AffineTransform getAffineTransform() {

		AffineTransform lat = this.getVisualizationViewer().getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).getTransform();
		AffineTransform vat = this.getVisualizationViewer().getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).getTransform();
		
		Graphics graphics = this.getVisualizationViewer().getGraphics();
		Graphics2D g2d = (Graphics2D) graphics;

		// --- Define new, concatenated transformer ---------------------------
		AffineTransform at = new AffineTransform();
		at.concatenate(g2d.getTransform());
		at.concatenate(vat);
		at.concatenate(lat);
		return at;
	}

}
